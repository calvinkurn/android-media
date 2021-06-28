package com.tokopedia.pms.howtopay_native.domain

import com.google.gson.Gson
import com.tokopedia.pms.howtopay_native.data.model.*
import com.tokopedia.pms.howtopay_native.util.NoInstructionFoundException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetHowToPayInstructionsMapper @Inject constructor() : UseCase<HowToPayInstruction>() {

    fun getHowToPayInstruction(
        htpResponse: HowToPayGqlResponse,
        appLinkPaymentInfo: AppLinkPaymentInfo,
        onSuccess: (HowToPayInstruction) -> Unit,
        onFail: (Throwable) -> Unit
    ) {
        useCaseRequestParams = RequestParams().apply {
            putObject(KEY_HTP_RESPONSE, htpResponse)
            putObject(Key_AppLinkPaymentInfo, appLinkPaymentInfo)
        }
        this.execute(onSuccess, onFail, useCaseRequestParams)
    }

    override suspend fun executeOnBackground(): HowToPayInstruction {
        val appLinkPaymentInfo: AppLinkPaymentInfo = useCaseRequestParams
            .getObject(Key_AppLinkPaymentInfo) as AppLinkPaymentInfo
        val htpData: HowToPayGqlResponse = useCaseRequestParams
            .getObject(KEY_HTP_RESPONSE) as HowToPayGqlResponse

        val howToPayChannelData = getHowToPay(htpData.howToPayData.helpPageJSON)
        if (howToPayChannelData.channelList.isNullOrEmpty())
            throw NoInstructionFoundException()
        else
            return when (appLinkPaymentInfo.payment_type) {
                KEY_VA -> getChannelResult(VirtualAccount, howToPayChannelData)
                KEY_SYARIAH -> getChannelResult(Syariah, howToPayChannelData)
                KEY_TRANSFER -> getChannelResult(BankTransfer, howToPayChannelData)
                KEY_STORE -> getChannelResult(Store, howToPayChannelData)
                KEY_KLIKBCA -> getChannelResult(KlickBCA, howToPayChannelData)
                else -> throw NoInstructionFoundException()
            }
    }

    private fun getChannelResult(
        paymentType: PaymentType,
        helpPageData: HelpPageData
    ): HowToPayInstruction {
        return if (
            helpPageData.channelList.size > 1) MultiChannelGatewayResult(paymentType, helpPageData)
        else helpPageData.channelList
            .getOrNull(0)?.channelSteps?.let {
                SingleChannelGatewayResult(paymentType, it)
            } ?: kotlin.run { SingleChannelGatewayResult(paymentType, arrayListOf()) }
    }


    private fun getHowToPay(helpPageJSON: String?): HelpPageData {
        try {
            return Gson().fromJson(helpPageJSON, HelpPageData::class.java)
        } catch (e: Exception) {
            throw NullPointerException()
        }
    }

    companion object {

        private const val KEY_HTP_RESPONSE = "key_htp_response"
        private const val Key_AppLinkPaymentInfo = "key_applinkpaymentinfo"

        const val KEY_VA = "VA"
        const val KEY_SYARIAH = "SYARIAH"
        const val KEY_TRANSFER = "TRANSFER"
        const val KEY_STORE = "STORE"
        const val KEY_KLIKBCA = "KLIKBCA"

    }
}

