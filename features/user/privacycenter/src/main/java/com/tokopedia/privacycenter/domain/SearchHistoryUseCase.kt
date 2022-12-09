package com.tokopedia.privacycenter.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.ItemSearch
import com.tokopedia.privacycenter.data.SearchHistoryResponse
import com.tokopedia.privacycenter.utils.generateUrlParamString
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SearchHistoryUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, PrivacyCenterStateResult<List<ItemSearch>>>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            query universe_initial_state(
                ${'$'}param: String!
            ){
              universe_initial_state(param: ${'$'}param) {
                data{
                  id
                  header
                  label_action
                  feature_id
                  tracking_option
                  items{
                    id
                    template
                    image_url
                    applink
                    url
                    title
                    subtitle
                    icon_title
                    icon_subtitle
                    label
                    label_type
                    shortcut_image
                    type
                    discount_percentage
                    original_price
                    campaign_code
                    component_id
                  }
                }
              }
            }
        """.trimIndent()

    override suspend fun execute(params: Unit): PrivacyCenterStateResult<List<ItemSearch>> {
        val registrationId = userSession.deviceId
        val userId = userSession.userId
        val uniqueId = getUniqueId(userId, registrationId)

        val variable = RequestParams.create()
        variable.putAll(
            mapOf(
                KEY_DEVICE to VALUE_DEVICE,
                KEY_SOURCE to VALUE_SOURCE,
                KEY_COUNT to VALUE_COUNT,
                KEY_USER_ID to userId,
                KEY_UNIQUE_ID to uniqueId,
                KEY_DEVICE_ID to registrationId
            )
        )

        val parameter = mapOf(PARAM to generateUrlParamString(variable.parameters))

        val response: SearchHistoryResponse = repository.request(graphqlQuery(), parameter)

        val recentSearch: List<ItemSearch> =
            response.universeInitialState.data.find { it.id == KEY_RECENT_SEARCH }?.items ?: emptyList()

        return PrivacyCenterStateResult.Success(recentSearch)
    }

    private fun getUniqueId(userId: String, registrationId: String) =
        if (userId.isNotEmpty()) {
            AuthHelper.getMD5Hash(userId)
        } else {
            AuthHelper.getMD5Hash(registrationId)
        }

    companion object {
        private const val PARAM = "param"
        private const val KEY_DEVICE = "device"
        private const val VALUE_DEVICE = "android"
        private const val KEY_SOURCE = "source"
        private const val VALUE_SOURCE = "privacy_center"
        private const val KEY_COUNT = "count"
        private const val VALUE_COUNT = "10"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_UNIQUE_ID = "unique_id"
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_RECENT_SEARCH = "recent_search"
    }
}
