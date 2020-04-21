package com.tokopedia.settingnotif.usersetting.domain

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.data.pojo.setusersetting.SetUserSettingResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class SetUserSettingUseCase @Inject constructor(
        val context: Context?,
        private val useCase: GraphqlUseCase
) : UseCase<SetUserSettingResponse>() {

    override fun createObservable(params: RequestParams?): Observable<SetUserSettingResponse> {
        if (context == null) return Observable.error(IllegalStateException("Something error. Try again later"))

        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_set_user_setting)
        val request = GraphqlRequest(query, SetUserSettingResponse::class.java, params?.parameters)

        useCase.clearRequest()
        useCase.addRequest(request)

        return useCase.createObservable(RequestParams.EMPTY).map { gqlResponse ->
            gqlResponse.getData<SetUserSettingResponse>(
                    SetUserSettingResponse::class.java
            )
        }
    }

    companion object {
        private const val PARAM_TYPE = "type"
        private const val PARAM_DATA = "data"

        fun params(
                notificationType: String,
                updatedSettingIds: List<Map<String, Any>>
        ): RequestParams {
            return RequestParams.create().apply {
                putString(PARAM_TYPE, notificationType)
                putObject(PARAM_DATA, updatedSettingIds)
            }
        }
    }

}