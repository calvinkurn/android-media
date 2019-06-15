package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.GetShopStatusCloudSource
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import javax.inject.Inject

class GetShopStatusUseCase @Inject constructor(
        private val getShopStatusCloudSource: GetShopStatusCloudSource,
        private val userSession: UserSessionInterface) : UseCase<ShopStatusModel>() {
    override fun createObservable(requestParams: RequestParams?): Observable<ShopStatusModel> {
        return getShopStatusCloudSource.getShopStatus(userSession.shopId)
    }
}