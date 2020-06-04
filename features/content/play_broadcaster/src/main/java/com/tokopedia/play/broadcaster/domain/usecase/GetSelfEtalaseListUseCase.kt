package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 02/06/20
 */
class GetSelfEtalaseListUseCase @Inject constructor(
        @Named(PlayBroadcastDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
        private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
        private val userSession: UserSessionInterface
) : UseCase<ArrayList<ShopEtalaseModel>>(
        defaultDispatchers = ioDispatcher
) {

    private val params: RequestParams
        get() = GetShopEtalaseByShopUseCase.createRequestParams(
                shopId = userSession.shopId,
                hideNoCount = true,
                hideShowCaseGroup = true,
                isOwner = true
        )

    override suspend fun executeOnBackground(): ArrayList<ShopEtalaseModel> {
        return getShopEtalaseByShopUseCase.getData(params)
    }
}