package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.play.broadcaster.type.EtalaseType
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
) : UseCase<List<ShopEtalaseModel>>(
        defaultDispatchers = ioDispatcher
) {

    private val params: RequestParams
        get() = GetShopEtalaseByShopUseCase.createRequestParams(
                shopId = userSession.shopId,
                hideNoCount = true,
                hideShowCaseGroup = false,
                isOwner = true
        )

    override suspend fun executeOnBackground(): List<ShopEtalaseModel> {
        return getShopEtalaseByShopUseCase.getData(params).filter {
            it.id.toInt() == ALL_PRODUCTS_ID || EtalaseType.getByType(it.type) == EtalaseType.User
        }
    }

    companion object {

        private const val ALL_PRODUCTS_ID = 2
    }
}