package com.tokopedia.settingnotif.usersetting.domain

import android.content.Context
import androidx.annotation.RawRes
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.settingnotif.usersetting.data.mapper.UserSettingFieldMapper
import com.tokopedia.settingnotif.usersetting.data.pojo.UserNotificationResponse
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingDataView
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class GetUserSettingUseCase @Inject constructor(
        val context: Context?,
        private val useCase: GraphqlUseCase,
        @RawRes val query: Int
) : UseCase<UserSettingDataView>() {

    override fun createObservable(requestParams: RequestParams?): Observable<UserSettingDataView> {
        if (context == null) return Observable.error(IllegalStateException("Something error. Try again later"))

        val query = GraphqlHelper.loadRawString(context.resources, query)
        val request = GraphqlRequest(query, UserNotificationResponse::class.java)

        useCase.clearRequest()
        useCase.addRequest(request)

        return useCase.createObservable(RequestParams.EMPTY).map { gqlResponse ->
            gqlResponse.getData<UserNotificationResponse>(
                    UserNotificationResponse::class.java
            )
        }.map(UserSettingFieldMapper())
    }

}