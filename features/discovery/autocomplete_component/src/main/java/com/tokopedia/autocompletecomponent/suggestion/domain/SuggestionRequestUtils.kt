package com.tokopedia.autocompletecomponent.suggestion.domain

import com.tokopedia.authentication.AuthHelper
import com.tokopedia.autocompletecomponent.util.CPM
import com.tokopedia.autocompletecomponent.util.CPM_ITEM_COUNT
import com.tokopedia.autocompletecomponent.util.CPM_PAGE
import com.tokopedia.autocompletecomponent.util.CPM_SEARCH_AUTO
import com.tokopedia.autocompletecomponent.util.CPM_ST
import com.tokopedia.autocompletecomponent.util.CPM_KEY_TEMPLATE
import com.tokopedia.autocompletecomponent.util.CPM_TEMPLATE
import com.tokopedia.autocompletecomponent.util.DEFAULT_COUNT
import com.tokopedia.autocompletecomponent.util.DEVICE_ID
import com.tokopedia.autocompletecomponent.util.IS_TYPING
import com.tokopedia.autocompletecomponent.util.KEY_COUNT
import com.tokopedia.autocompletecomponent.util.SEARCHBAR
import com.tokopedia.autocompletecomponent.util.putChooseAddressParams
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_DEVICE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEVICE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.SOURCE
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.UNIQUE_ID
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topads.sdk.domain.TopAdsParams
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface

object SuggestionRequestUtils {

    const val SUGGESTION_QUERY = """
        query universe_suggestion(${'$'}params: String!) {
          universe_suggestion(param: ${'$'}params) {
            data {
              id
              name
              items {
                template
                type
                applink
                url
                title
                subtitle
                icon_title
                icon_subtitle
                shortcut_image
                image_url
                url_tracker
                label
                label_type
                tracking {
                  code
                }
                discount_percentage
                original_price
                tracking_option
                component_id
                child_items {
                  template
                  type
                  applink
                  url
                  title
                }
              }
            }
            top_shops{
              type
              id
              applink
              url
              title
              subtitle
              icon_title
              icon_subtitle
              url_tracker
              image_url
              products{
                image_url
              }
            }
          }
        }
    """

    const val HEADLINE_ADS_QUERY = """
        query HeadlineAds(${'$'}headline_params: String!) {
            headlineAds: displayAdsV3(displayParams: ${'$'}headline_params) {
                data {
                    ad_click_url
                    applinks
                    headline {
                        name
                        image {
                            full_url
                            full_ecs
                        }
                        badges {
                            image_url
                        }
                    }
                }
            }
        }
    """

    fun getParams(
        searchParameter: Map<String, Any>,
        userSession: UserSessionInterface,
        isTyping: Boolean,
        chooseAddressData: LocalCacheModel,
    ): RequestParams {
        val registrationId = userSession.deviceId
        val userId = userSession.userId
        val uniqueId = getUniqueId(userId, registrationId)

        val params = RequestParams.create()
        params.putAll(searchParameter)

        params.putString(DEVICE, DEFAULT_VALUE_OF_PARAMETER_DEVICE)
        params.putString(SOURCE, SEARCHBAR)
        params.putString(KEY_COUNT, DEFAULT_COUNT)
        params.putString(UNIQUE_ID, uniqueId)
        params.putString(DEVICE_ID, registrationId)
        params.putBoolean(IS_TYPING, isTyping)
        params.putChooseAddressParams(chooseAddressData)

        return params
    }

    private fun getUniqueId(userId: String, registrationId: String) =
        if (userId.isNotEmpty()) AuthHelper.getMD5Hash(userId)
        else AuthHelper.getMD5Hash(registrationId)

    internal fun createHeadlineParams(requestParams: RequestParams): Map<String, String> {
        val keyword = requestParams.parameters[SearchApiConst.Q]?.toString() ?: ""

        return mapOf(
            TopAdsParams.KEY_EP to CPM,
            TopAdsParams.KEY_ITEM to CPM_ITEM_COUNT,
            TopAdsParams.KEY_SRC to CPM_SEARCH_AUTO,
            TopAdsParams.KEY_PAGE to CPM_PAGE,
            SearchApiConst.ACTIVE_TAB to CPM_ST,
            CPM_KEY_TEMPLATE to CPM_TEMPLATE,
            DEVICE to DEFAULT_VALUE_OF_PARAMETER_DEVICE,
            SearchApiConst.Q to keyword,
        )
    }

    internal fun <T: Any> GraphqlUseCase<T>.prepare(
        query: String,
        tClass: Class<T>,
        params: Map<String, Any?>,
    ) {
        setGraphqlQuery(query)
        setTypeClass(tClass)
        setRequestParams(params)
    }

    internal fun <T: Any> GraphqlUseCase<T>.prepare(
        query: GqlQueryInterface,
        tClass: Class<T>,
        params: Map<String, Any?>,
    ) {
        setGraphqlQuery(query)
        setTypeClass(tClass)
        setRequestParams(params)
    }
}