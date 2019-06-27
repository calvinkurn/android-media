package com.tokopedia.settingnotif.usersetting.domain.usecase

import android.content.Context
import android.support.annotation.RawRes
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.settingnotif.usersetting.domain.mapper.UserSettingFieldMapper
import com.tokopedia.settingnotif.usersetting.domain.pojo.UserNotificationResponse
import com.tokopedia.settingnotif.usersetting.view.viewmodel.UserSettingViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class GetUserSettingUseCase @Inject constructor(
        val context: Context?,
        val gqlUseCase: GraphqlUseCase,
        @RawRes val gqlQueryRaw: Int
) : UseCase<UserSettingViewModel>() {

    override fun createObservable(requestParams: RequestParams?): Observable<UserSettingViewModel> {
        if (context == null) {
            return Observable.error(IllegalStateException("Something error. Try again later"))
        }

        val query = GraphqlHelper.loadRawString(context.resources, gqlQueryRaw)
        val gqlRequest = GraphqlRequest(query, UserNotificationResponse::class.java)

        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(gqlRequest)

        return gqlUseCase.createObservable(RequestParams.EMPTY)
                .map { gqlResponse ->
                    gqlResponse.getData<UserNotificationResponse>(UserNotificationResponse::class.java)
                }
                .map(UserSettingFieldMapper())

//        return Observable.just(SettingHelper.createDummyResponse()).map(UserSettingFieldMapper())
    }

}