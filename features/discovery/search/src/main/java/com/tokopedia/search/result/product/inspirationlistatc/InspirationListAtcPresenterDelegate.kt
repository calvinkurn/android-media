package com.tokopedia.search.result.product.inspirationlistatc

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@SearchScope
class InspirationListAtcPresenterDelegate @Inject constructor(
    private val addToCartUseCase: AddToCartUseCase,
    private val userSession: UserSessionInterface,
    private val inspirationListAtcView: InspirationListAtcView,
    searchParameterProvider: SearchParameterProvider,
): InspirationListAtcPresenter,
    SearchParameterProvider by searchParameterProvider {

    companion object {
        private const val DEFAULT_USER_ID = "0"
    }

    override fun onListAtcItemAddToCart(
        product: InspirationCarouselDataView.Option.Product,
        type: String,
    ) {
        if (product.shouldOpenVariantBottomSheet()) {
            inspirationListAtcView.trackAddToCartVariant(product)

            inspirationListAtcView.openVariantBottomSheet(product, type)
        } else {
            executeAtcCommon(product)
        }
    }

    private fun onAddToCartUseCaseSuccess(
        addToCartDataModel: AddToCartDataModel?,
        product: InspirationCarouselDataView.Option.Product,
    ) {
        inspirationListAtcView.updateSearchBarNotification()

        val message = addToCartDataModel?.data?.message?.firstOrNull() ?: ""
        inspirationListAtcView.openAddToCartToaster(message, true)

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
        product: InspirationCarouselDataView.Option.Product,
    ) {
        val requestParams = product.createAddToCartRequestParams()

        addToCartUseCase.setParams(requestParams)
        addToCartUseCase.execute(
            { onAddToCartUseCaseSuccess(it, product) },
            ::onAddToCartUseCaseFailed
        )
    }

    private fun InspirationCarouselDataView.Option.Product.createAddToCartRequestParams(): AddToCartRequestParams {
        return AddToCartRequestParams(
            productId = id.toLongOrZero(),
            shopId = shopId.toIntOrZero(),
            quantity = minOrder.toIntOrZero(),
            productName = name,
            price = priceStr,
            userId = if (userSession.isLoggedIn) userSession.userId else DEFAULT_USER_ID
        )
    }

    fun convertInspirationCarouselToInspirationListAtc(
        data: InspirationCarouselDataView
    ): List<Visitable<*>> {
        return data.options.map {
            InspirationListAtcDataView(
                option = it,
                type = data.type,
            )
        }
    }
}
