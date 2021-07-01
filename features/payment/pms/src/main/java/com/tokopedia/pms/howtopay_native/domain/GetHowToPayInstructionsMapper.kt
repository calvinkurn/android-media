package com.tokopedia.pms.howtopay_native.domain

import com.google.gson.Gson
import com.tokopedia.pms.howtopay_native.data.model.HelpPageData
import com.tokopedia.pms.howtopay_native.data.model.HowToPayData
import com.tokopedia.pms.howtopay_native.data.model.HowToPayGqlResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetHowToPayInstructionsMapper @Inject constructor() : UseCase<HowToPayData>() {

    fun getHowToPayInstruction(
        htpResponse: HowToPayGqlResponse,
        onSuccess: (HowToPayData) -> Unit,
        onFail: (Throwable) -> Unit
    ) {
        useCaseRequestParams = RequestParams().apply {
            putObject(KEY_HTP_RESPONSE, htpResponse)
        }
        this.execute(onSuccess, onFail, useCaseRequestParams)
    }

    override suspend fun executeOnBackground(): HowToPayData {
        val htpResponse: HowToPayGqlResponse = useCaseRequestParams
            .getObject(KEY_HTP_RESPONSE) as HowToPayGqlResponse
        htpResponse.howToPayData.let { htpData ->
            val howToPayChannelData = getHowToPay(htpData.helpPageJSON)
            htpData.helpPageData = howToPayChannelData
            return htpData
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
    }
}

