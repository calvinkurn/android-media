package com.tokopedia.search.result.product.addtocart

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.addtocart.analytics.AddToCartTracking
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcView
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@SearchScope
class AddToCartPresenterDelegate @Inject constructor(
    private val addToCartUseCase: AddToCartUseCase,
    private val userSession: UserSessionInterface,
    private val addToCartView: AddToCartView,
    private val addToCartTracking: AddToCartTracking,
    searchParameterProvider: SearchParameterProvider,
): AddToCartPresenter,
    SearchParameterProvider by searchParameterProvider {

    @Suppress("LateinitUsage")
    override lateinit var productAddedToCart: ProductItemDataView
        private set

    override fun addToCart(data: ProductItemDataView?) {
        data ?: return
        productAddedToCart = data

        if (data.shouldOpenVariantBottomSheet()) {
//            inspirationListAtcView.trackAddToCartVariant(product)
//
            addToCartView.openVariantBottomSheet(data, "")

        } else {
            executeAtcCommon(::onAddToCartUseCaseSuccess, ::onAddToCartUseCaseFailed, data)
        }
    }

    private fun onAddToCartUseCaseSuccess(addToCartDataModel: AddToCartDataModel?) {
        addToCartView.updateSearchBarNotification()

        val message = addToCartDataModel?.data?.message?.firstOrNull() ?: ""
        addToCartView.openAddToCartToaster(message, true)

//        addToCartTracking.trackItemClick(productAddedToCart)
//        addToCartTracking.trackAddToCartSuccess(productAddedToCart)
    }

    private fun onAddToCartUseCaseFailed(throwable: Throwable?) {
        val message = throwable?.message ?: ""
        addToCartView.openAddToCartToaster(message, false)
    }

    private fun executeAtcCommon(
        onAddToCartUseCaseSuccess: (addToCartDataModel: AddToCartDataModel?) -> Unit,
        onAddToCartUseCaseFailed: (Throwable) -> Unit,
        data: ProductItemDataView,
    ) {
        val requestParams = data.createAddToCartRequestParams()

        addToCartUseCase.setParams(requestParams)
        addToCartUseCase.execute(onAddToCartUseCaseSuccess, onAddToCartUseCaseFailed)
    }


    private fun ProductItemDataView.createAddToCartRequestParams(): AddToCartRequestParams {
        return AddToCartRequestParams(
            productId = productID.toLongOrZero(),
            shopId = shopID.toIntOrZero(),
            quantity = minOrder,
            productName = productName,
            price = price,
            userId = if (userSession.isLoggedIn) userSession.userId else "0"
        )
    }

}
