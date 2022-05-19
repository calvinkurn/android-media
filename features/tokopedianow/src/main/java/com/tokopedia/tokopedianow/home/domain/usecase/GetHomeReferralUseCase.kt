package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.home.presentation.model.HomeReferralDataModel
import javax.inject.Inject

class GetHomeReferralUseCase @Inject constructor(
    private val validateReferralUserUseCase: ValidateReferralUserUseCase,
    private val getReferralSenderHomeUseCase: GetReferralSenderHomeUseCase,
    private val getReferralReceiverHomeUseCase: GetReferralReceiverHomeUseCase
) {

    companion object {
        private const val SUCCESS_CODE = "200"
        private const val REFERRAL_SENDER = 1
    }

    suspend fun execute(slug: String): HomeReferralDataModel {
        val response = validateReferralUserUseCase.execute(slug)
        if (response.gamiReferralValidateUser.resultStatus.code == SUCCESS_CODE) {
            val status = response.gamiReferralValidateUser.status
            val isSender = REFERRAL_SENDER == status

            return if (isSender) {
                getReferralSender(slug, status)
            } else {
                getReferralReceiver(slug, status)
            }
        } else {
            throw MessageErrorException()
        }
    }

    private suspend fun getReferralSender(slug: String, status: Int): HomeReferralDataModel {
        val response = getReferralSenderHomeUseCase.execute(slug)
        val gamiReferralSenderHome = response.gamiReferralSenderHome

        if(gamiReferralSenderHome.resultStatus.code == SUCCESS_CODE) {
            val metaData = gamiReferralSenderHome.sharingMetaData
            val reward = gamiReferralSenderHome.reward
            return HomeReferralDataModel(
                ogImage = metaData.ogImage,
                ogTitle = metaData.ogTitle,
                ogDescription = metaData.ogDescription,
                textDescription = metaData.textDescription,
                sharingUrlParam = "$slug/${gamiReferralSenderHome.sharingMetaData.sharingUrl}",
                userStatus = status.toString(),
                maxReward = reward.maxReward,
                isSender = true
            )
        } else {
            throw MessageErrorException(gamiReferralSenderHome.resultStatus.reason)
        }
    }

    private suspend fun getReferralReceiver(slug: String, status: Int): HomeReferralDataModel {
        val response = getReferralReceiverHomeUseCase.execute(slug)
        val gamiReferralReceiverHome = response.gamiReferralReceiverHome

        if(gamiReferralReceiverHome.resultStatus.code == SUCCESS_CODE) {
            val reward = gamiReferralReceiverHome.reward
            return HomeReferralDataModel(
                userStatus = status.toString(),
                maxReward = reward.maxReward,
                isSender = false
            )
        } else {
            throw MessageErrorException(gamiReferralReceiverHome.resultStatus.reason)
        }
    }
}