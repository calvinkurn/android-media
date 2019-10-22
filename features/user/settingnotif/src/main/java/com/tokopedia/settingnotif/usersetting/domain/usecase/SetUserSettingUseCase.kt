package com.tokopedia.settingnotif.usersetting.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.setusersetting.SetUserSettingResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class SetUserSettingUseCase @Inject constructor(
        val context: Context?,
        val gqlUseCase: GraphqlUseCase
) : UseCase<SetUserSettingResponse>() {

    private val PARAM_TYPE = "type"
    private val PARAM_DATA = "data"

    override fun createObservable(params: RequestParams?): Observable<SetUserSettingResponse> {
        if (context == null) {
            return Observable.error(IllegalStateException("Something error. Try again later"))
        }

        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_set_user_setting)
        val gqlRequest = GraphqlRequest(query, SetUserSettingResponse::class.java, params?.parameters)

        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(gqlRequest)

        return gqlUseCase.createObservable(RequestParams.EMPTY)
                .map { gqlResponse ->
                    gqlResponse.getData<SetUserSettingResponse>(SetUserSettingResponse::class.java)
                }
    }

    fun createParams(notificationType: String, updatedSettingIds: List<Map<String, Any>>): RequestParams {
        return RequestParams.create().apply {
            putString(PARAM_TYPE, notificationType)
            putObject(PARAM_DATA, updatedSettingIds)
        }
    }

}