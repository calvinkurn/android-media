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
        onSuccess: (HowToPayInstruction) -> Unit,
        onFail: (Throwable) -> Unit
    ) {
        useCaseRequestParams = RequestParams().apply {
            putObject(KEY_HTP_RESPONSE, htpResponse)
        }
        this.execute(onSuccess, onFail, useCaseRequestParams)
    }

    override suspend fun executeOnBackground(): HowToPayInstruction {
        val htpResponse: HowToPayGqlResponse = useCaseRequestParams
            .getObject(KEY_HTP_RESPONSE) as HowToPayGqlResponse
        htpResponse.howToPayData.let { htpData ->
            val howToPayChannelData = getHowToPay(htpData.helpPageJSON)
            htpData.helpPageData = howToPayChannelData

            return HowToPayInstruction(htpData)
        }
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

