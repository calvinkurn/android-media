package com.tokopedia.play.broadcaster.shorts.domain.usecase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.shorts.domain.model.OnboardAffiliateRequestModel
import com.tokopedia.play.broadcaster.shorts.domain.model.OnboardAffiliateResponseModel
import com.tokopedia.url.TokopediaUrl
import javax.inject.Inject

class OnBoardAffiliateUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers,
) : CoroutineUseCase<OnboardAffiliateRequestModel, OnboardAffiliateResponseModel>(dispatcher.io) {

    override suspend fun execute(params: OnboardAffiliateRequestModel): OnboardAffiliateResponseModel {
        return try {
            val param = generateParams(params)
            repository.request(graphqlQuery(), param)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            throw MessageErrorException(e.message)
        }
    }

    private fun generateParams(params: OnboardAffiliateRequestModel): Map<String, Any> {
        return mapOf(
            KEY_PARAM_CHANNEL to listOf(
                mapOf(
                    KEY_PARAM_NAME to VALUE_PARAM_NAME,
                    KEY_PARAM_CHANNEL_ID to params.channelID,
                    KEY_PARAM_PROFILE_ID to String.format(valueParamID, params.profileID),
                )
            )
        )
    }

    override fun graphqlQuery(): String {
        return """
            mutation onboardAffiliate(
                ${"$${KEY_PARAM_CHANNEL}"}: [OnboardAffiliateChannelRequest!]!
            ) {
              onboardAffiliate(input: {
                $KEY_PARAM_CHANNEL: ${"$$KEY_PARAM_CHANNEL"}
              }) {
                Data {
                  Status
                  Error {
                    ErrorType
                    Message
                  }
                }
              }
            }
        """.trimIndent()
    }

    companion object {
        private const val KEY_PARAM_CHANNEL = "Channel"
        private const val KEY_PARAM_NAME = "Name"
        private const val VALUE_PARAM_NAME = "play"
        private const val KEY_PARAM_CHANNEL_ID = "ChannelID"
        private const val KEY_PARAM_PROFILE_ID = "ProfileID"
        private val valueParamID = "${TokopediaUrl.getInstance().WEB}people/%s"
    }

}
