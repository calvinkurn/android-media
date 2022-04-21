package com.tokopedia.tokopatch.domain.repository

import com.tokopedia.tokopatch.domain.api.OAuthApiService
import com.tokopedia.tokopatch.domain.api.PatchApiService
import com.tokopedia.tokopatch.domain.api.RetrofitClient
import com.tokopedia.tokopatch.domain.data.DataDao
import com.tokopedia.tokopatch.domain.data.DataResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


/**
 * Author errysuprayogi on 09,February,2020
 */
class PatchRepository(private val dataDao: DataDao, versionCode: String) {

    private var TAG: String = PatchRepository::class.java.simpleName
    private var service: PatchApiService = RetrofitClient.webservice
    private var authClient: OAuthApiService = RetrofitClient.oauthService

    companion object {
        fun getInstance(dataDao: DataDao, versionCode: String): PatchRepository {
            val instance: PatchRepository by lazy {
                PatchRepository(dataDao, versionCode)
            }
            return instance
        }
    }

    val allData: List<DataResponse.Result> = dataDao.getAllResultList(versionCode)

    suspend fun flush() {
        dataDao.deleteAll()
    }

    suspend fun insert(result: DataResponse.Result) {
        dataDao.insertAll(result)
    }

    suspend fun oauth2(token: String): String {
        val payload = "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion=${token}"
        val body = payload.toRequestBody("text/plain".toMediaTypeOrNull())
        return authClient.token(body).body()?.token ?: ""
    }

    fun getPatchV2(
        token: String, applicationId: String, versionName: String, buildNumber: String,
        env: String,
        onSuccess: ((DataResponse) -> Unit),
        onError: ((Throwable) -> Unit)
    ) {
        service.getPatch(
            authorization = "Bearer ${token}",
            applicationId,
            versionName,
            buildNumber,
            env
        ).enqueue(object : Callback<DataResponse> {
            override fun onResponse(
                call: Call<DataResponse>,
                response: Response<DataResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess)
                } else {
                    Timber.e("Something wrong with getPatchV2 " +
                            "code: ${response.code()} " +
                            "message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

}
