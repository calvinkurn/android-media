package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.play.broadcaster.type.EtalaseType
import com.tokopedia.play.broadcaster.util.error.DefaultNetworkThrowable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Created by jegul on 02/06/20
 */
class GetSelfEtalaseListUseCase @Inject constructor(
        dispatcher: CoroutineDispatchers,
        private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
        private val userSession: UserSessionInterface
) : UseCase<List<ShopEtalaseModel>>(
        defaultDispatchers = dispatcher.io
) {

    private val params: RequestParams
        get() = GetShopEtalaseByShopUseCase.createRequestParams(
                shopId = userSession.shopId,
                hideNoCount = true,
                hideShowCaseGroup = false,
                isOwner = true
        )

    override suspend fun executeOnBackground(): List<ShopEtalaseModel> {
        try {
            return getShopEtalaseByShopUseCase.getData(params).filter {
                it.id == ALL_PRODUCTS_ID || EtalaseType.getByType(it.type, it.id) == EtalaseType.User
            }
        } catch (e: Throwable) {
            throw if (e.hasCauseType(listOf(UnknownHostException::class.java, SocketTimeoutException::class.java))) DefaultNetworkThrowable()
            else e
        }
    }

    private fun Throwable.hasCauseType(errorClassList: List<Class<out Throwable>>): Boolean {
        var cause: Throwable? = this
        while (cause?.cause != null) {
            cause = cause.cause
            if (cause!!::class.java in errorClassList) return true
        }
        return false
    }

    companion object {

        private const val ALL_PRODUCTS_ID = "2"
    }
}