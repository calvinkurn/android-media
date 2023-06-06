package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

@ActivityScope
class GotoKycInterceptor @Inject constructor() : Interceptor {

    @Volatile
    private var projectId: String = ""

    fun setProjectId(projectId: String) {
        this.projectId = projectId
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
        if (request.header(KEY_X_PROJECT_ID) == null) {
            requestBuilder.addHeader(KEY_X_PROJECT_ID, kycSharedPreference.getProjectId())
        }
        return chain.proceed(requestBuilder.build())
    }

    companion object {
        private const val KEY_X_PROJECT_ID = "x-project-id"
    }
}
