package com.tokopedia.search.result.product.addtocart

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcView
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AddToCartPresenterDelegate @Inject constructor(
    private val addToCartUseCase: AddToCartUseCase,
    private val userSession: UserSessionInterface,
    private val inspirationListAtcView: InspirationListAtcView,
    searchParameterProvider: SearchParameterProvider,
): AddToCartPresenter,
    SearchParameterProvider by searchParameterProvider {

    @Suppress("LateinitUsage")
    override lateinit var productAddedToCart: InspirationCarouselDataView.Option.Product
        private set

    override fun addToCart(addToCartData: AddToCartData?) {
        addToCartData ?: return

        if (addToCartData.shouldOpenVariantBottomSheet()) {
//            inspirationListAtcView.trackAddToCartVariant(product)
//
//            inspirationListAtcView.openVariantBottomSheet(product, type)
        } else {
            executeAtcCommon(::onAddToCartUseCaseSuccess, ::onAddToCartUseCaseFailed, addToCartData)
        }
    }

    private fun onAddToCartUseCaseSuccess(addToCartDataModel: AddToCartDataModel?) {
//        inspirationListAtcView.updateSearchBarNotification()

        val message = addToCartDataModel?.data?.message?.firstOrNull() ?: ""
//        inspirationListAtcView.openAddToCartToaster(message, true)

        val product = productAddedToCart
        val cartId = addToCartDataModel?.data?.cartId ?: ""
        val quantity = addToCartDataModel?.data?.quantity ?: 0

        val trackingData =
            InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData(
                product,
                getSearchParameter(),
                cartId,
                quantity
            )
        inspirationListAtcView.trackItemClick(trackingData)
        inspirationListAtcView.trackAddToCart(trackingData)
    }

    private fun onAddToCartUseCaseFailed(throwable: Throwable?) {
        val message = throwable?.message ?: ""
        inspirationListAtcView.openAddToCartToaster(message, false)
    }

    private fun executeAtcCommon(
        onAddToCartUseCaseSuccess: (addToCartDataModel: AddToCartDataModel?) -> Unit,
        onAddToCartUseCaseFailed: (Throwable) -> Unit,
        addToCartData: AddToCartData,
    ) {
        val requestParams = addToCartData.createAddToCartRequestParams()

        addToCartUseCase.setParams(requestParams)
        addToCartUseCase.execute(onAddToCartUseCaseSuccess, onAddToCartUseCaseFailed)
    }


    private fun AddToCartData.createAddToCartRequestParams(): AddToCartRequestParams {
        return AddToCartRequestParams(
            productId = productId.toLongOrZero(),
            shopId = shopId.toIntOrZero(),
            quantity = quantity,
            productName = productName,
            price = priceStr,
            userId = if (userSession.isLoggedIn) userSession.userId else "0"
        )
    }

}
