package com.tokopedia.payment.fingerprint.data

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.payment.fingerprint.data.model.DataResponseSavePublicKey
import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint
import com.tokopedia.payment.fingerprint.data.model.ResponseRegisterFingerprint
import org.json.JSONObject
import retrofit2.Response
import rx.Observable
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 3/23/18.
 */
class FingerprintDataSourceCloud @Inject constructor(private val fingerprintApi: FingerprintApi,
                                                     private val accountFingerprintApi: AccountFingerprintApi) {

    fun saveFingerPrint(params: HashMap<String, Any?>): Observable<Boolean> {
        return fingerprintApi.saveFingerPrint(params).map { dataResponseResponse: Response<ResponseRegisterFingerprint> ->
            if (dataResponseResponse.isSuccessful && dataResponseResponse.body() != null) {
                val responseRegisterFingerprint = dataResponseResponse.body()
                return@map responseRegisterFingerprint?.isSuccess ?: false
            } else {
                return@map false
            }
        }
    }

    fun paymentWithFingerPrint(params: HashMap<String, Any?>): Observable<ResponsePaymentFingerprint?> {
        return fingerprintApi.paymentWithFingerPrint(params).map { dataResponseResponse: Response<ResponsePaymentFingerprint> ->
            if (dataResponseResponse.isSuccessful && dataResponseResponse.body() != null) {
                return@map dataResponseResponse.body()
            } else {
                return@map null
            }
        }
    }

    fun savePublicKey(params: HashMap<String, String?>): Observable<Boolean> {
        return accountFingerprintApi.savePublicKey(params).map { dataResponseResponse: Response<DataResponse<DataResponseSavePublicKey>> ->
            if (dataResponseResponse.isSuccessful && dataResponseResponse.body() != null && dataResponseResponse.body()?.data != null) {
                return@map (dataResponseResponse.body()?.data?.success ?: 0) == SUCCESS_VALUE
            } else {
                return@map false
            }
        }
    }

    fun getDataPostOtp(transactionId: String): Observable<HashMap<String, String>?> {
        return fingerprintApi.getPostDataOtp(generateMapPostData(transactionId)).map { dataResponseResponse: Response<JsonElement> ->
            if (dataResponseResponse.isSuccessful && dataResponseResponse.body() != null) {
                try {
                    val jsonObject = dataResponseResponse.body()!!.asJsonObject
                    val isSuccess = jsonObject["success"].asBoolean
                    if (isSuccess) {
                        val jsonObjectData = jsonObject["data"].asJsonObject
                        return@map jsonToMap(jsonObjectData)
                    } else {
                        return@map null
                    }
                } catch (e: Exception) {
                    Timber.w(e)
                    return@map null
                }
            } else {
                return@map null
            }
        }
    }

    @Throws(Exception::class)
    private fun jsonToMap(json: JsonObject): HashMap<String, String> {
        var retMap = HashMap<String, String>()
        if (json !== JSONObject.NULL) {
            retMap = toMap(json)
        }
        return retMap
    }

    @Throws(Exception::class)
    private fun toMap(json: JsonObject): HashMap<String, String> {
        val map = HashMap<String, String>()
        val keysItr = json.entrySet().iterator()
        for (entry in keysItr) {
            map[entry.key] = entry.value.asString
        }
        return map
    }

    private fun generateMapPostData(transactionId: String): HashMap<String, String> {
        return hashMapOf(TRANSACTION_ID to transactionId)
    }

    companion object {
        private const val SUCCESS_VALUE = 1
        private const val TRANSACTION_ID = "transaction_id"
    }

}