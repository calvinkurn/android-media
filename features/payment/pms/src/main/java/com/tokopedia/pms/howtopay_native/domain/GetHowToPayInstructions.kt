package com.tokopedia.pms.howtopay_native.domain

import android.content.res.Resources
import com.google.gson.Gson
import com.tokopedia.pms.howtopay_native.data.model.*
import com.tokopedia.pms.howtopay_native.di.HowToPayRawJsonFileResID
import com.tokopedia.pms.howtopay_native.util.FileReadException
import com.tokopedia.pms.howtopay_native.util.NoInstructionFoundException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.io.*
import javax.inject.Inject

class GetHowToPayInstructions @Inject constructor(val resources: Resources,
                                                  @HowToPayRawJsonFileResID val jsonFileResId: Int)
    : UseCase<HowToPayInstruction>() {

    fun getHowToPayInstruction(requestParams: RequestParams,
                               onSuccess: (HowToPayInstruction) -> Unit,
                               onFail: (Throwable) -> Unit) {
        this.execute(onSuccess, onFail, requestParams)
    }

    override suspend fun executeOnBackground(): HowToPayInstruction {
        val appLinkPaymentInfo: AppLinkPaymentInfo = useCaseRequestParams
                .getObject(Key_AppLinkPaymentInfo) as AppLinkPaymentInfo
        val howToPay = getHowToPay(appLinkPaymentInfo.payment_code,
                appLinkPaymentInfo.gateway_name, appLinkPaymentInfo.total_amount)
        return when (appLinkPaymentInfo.payment_type) {
            KEY_VA -> getMultiOptionInstructions(VirtualAccount, appLinkPaymentInfo,
                    howToPay.virtualAccountGatewayList)
            KEY_SYARIAH -> getMultiOptionInstructions(Syariah, appLinkPaymentInfo,
                    howToPay.syariahGatewayList)
            KEY_TRANSFER -> SingleChannelGatewayResult(BankTransfer, howToPay.bankTransferInstructions)
            KEY_STORE -> SingleChannelGatewayResult(Store, howToPay.storeInstructions)
            KEY_KLIKBCA -> SingleChannelGatewayResult(KlickBCA, howToPay.klikBCA)
            else -> throw NoInstructionFoundException()
        }
    }

    private fun getMultiOptionInstructions(paymentType: PaymentType,
                                           appLinkPaymentInfo: AppLinkPaymentInfo,
                                           multiChannelGatewayList: ArrayList<MultiChannelGateway>)
            : HowToPayInstruction {
        multiChannelGatewayList.forEach { multiChannelGateway ->
            if (appLinkPaymentInfo.gateway_code == multiChannelGateway.gatewayCode
                    && appLinkPaymentInfo.gateway_name == multiChannelGateway.gatewayName &&
                    appLinkPaymentInfo.bank_code == multiChannelGateway.bankCode) {
                return MultiChannelGatewayResult(paymentType, multiChannelGateway)
            }
        }
        throw NoInstructionFoundException()
    }

    private fun getHowToPay(paymentCode: String, gatewayName: String, amount: String): HowToPay {
        val inputStream: InputStream = resources.openRawResource(jsonFileResId)
        try {
            val writer: Writer = StringWriter()
            val buffer = CharArray(bufferSize)

            val reader: Reader = BufferedReader(InputStreamReader(inputStream, UTF_8))
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
            var jsonString: String = writer.toString()
            jsonString = jsonString.replace(STORE_NAME_REGEX, gatewayName)
            jsonString = jsonString.replace(PAYMENT_CODE_REGEX, paymentCode)
            jsonString = jsonString.replace(TRANSACTION_AMOUNT_REGEX, amount)
            return Gson().fromJson(jsonString, HowToPay::class.java)
        } catch (e: Exception) {
            throw FileReadException()
        } finally {
            inputStream.close()
        }
    }

    fun getRequestParam(appLinkPaymentInfo: AppLinkPaymentInfo): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(Key_AppLinkPaymentInfo, appLinkPaymentInfo)
        return requestParams
    }

    companion object {

        private const val bufferSize = 1024
        private const val UTF_8 = "UTF-8"
        private const val Key_AppLinkPaymentInfo = "key_applinkpaymentinfo"

        const val KEY_VA = "VA"
        const val KEY_SYARIAH = "SYARIAH"
        const val KEY_TRANSFER = "TRANSFER"
        const val KEY_STORE = "STORE"
        const val KEY_KLIKBCA = "KLIKBCA"

        const val STORE_NAME_REGEX = "{store_name}"
        const val PAYMENT_CODE_REGEX = "{payment_code}"
        const val TRANSACTION_AMOUNT_REGEX = "{trn_amount}"
    }
}

