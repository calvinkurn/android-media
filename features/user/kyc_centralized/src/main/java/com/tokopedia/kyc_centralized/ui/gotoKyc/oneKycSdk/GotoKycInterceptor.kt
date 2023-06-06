package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import com.tokopedia.abstraction.common.di.scope.ActivityScope
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
        if (request.header("x-project-id") == null) {
            requestBuilder.addHeader("x-project-id", projectId)
        }
        return chain.proceed(requestBuilder.build())
    }
}
