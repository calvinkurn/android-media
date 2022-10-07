package com.tokopedia.search.result.product.inspirationlistatc

import android.content.Context
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.search.R
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.result.presentation.view.fragment.ProductListFragment
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnification
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.search_activity_search.*
import javax.inject.Inject

@SearchScope
class InspirationListAtcPresenterDelegate @Inject constructor(
    private val addToCartUseCase: AddToCartUseCase,
    private val userSession: UserSessionInterface,
    private val inspirationCarouselTrackingUnification: InspirationCarouselTrackingUnification,
    @SearchContext
    context: Context,
    searchParameterProvider: SearchParameterProvider,
    queryKeyProvider: QueryKeyProvider,
    searchNavigationListener: SearchNavigationListener,
    fragmentProvider: FragmentProvider,
): InspirationListAtcPresenter,
    ContextProvider by WeakReferenceContextProvider(context),
    FragmentProvider by fragmentProvider,
    ApplinkOpener by ApplinkOpenerDelegate,
    SearchParameterProvider by searchParameterProvider,
    QueryKeyProvider by queryKeyProvider,
    SearchNavigationListener by searchNavigationListener {

    companion object {
        private const val DEFAULT_USER_ID = "0"
    }

    var productAddedToCart: InspirationCarouselDataView.Option.Product? = null

    override fun onListAtcItemAddToCart(
        product: InspirationCarouselDataView.Option.Product,
        type: String
    ) {
        productAddedToCart = product

        if (product.shouldOpenVariantBottomSheet()) {
            product.asSearchComponentTracking(queryKey).click(TrackApp.getInstance().gtm)

            context?.let {
                AtcVariantHelper.goToAtcVariant(
                    it,
                    productId = product.id,
                    pageSource = VariantPageSource.SRP_PAGESOURCE,
                    shopId = product.shopId,
                    trackerCdListName = SearchTracking.getInspirationCarouselUnificationListName(
                        type,
                        product.componentId,
                    ),
                    startActivitResult = { intent, reqCode ->
                        getFragment().startActivityForResult(intent, reqCode)
                    }
                )
            }
        } else {
            executeAtcCommon(::onAddToCartUseCaseSuccess, ::onAddToCartUseCaseFailed, product)
        }
    }

    private fun onAddToCartUseCaseSuccess(addToCartDataModel: AddToCartDataModel?) {
        updateCartCounter()

        getFragment().view?.let {
            Toaster.build(
                it,
                addToCartDataModel?.data?.message?.firstOrNull() ?: "",
                Snackbar.LENGTH_SHORT,
                Toaster.TYPE_NORMAL,
                getFragment().getString(R.string.search_see_cart),
            ) {
                openApplink(context, ApplinkConst.CART)
            }.show()
        }

        val product = productAddedToCart ?: InspirationCarouselDataView.Option.Product()
        val cartId = addToCartDataModel?.data?.cartId ?: ""
        val quantity = addToCartDataModel?.data?.quantity ?: 0

        val trackingData =
            InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData(
                product,
                getSearchParameter(),
                cartId,
                quantity
            )
        inspirationCarouselTrackingUnification.trackCarouselClick(trackingData)
        inspirationCarouselTrackingUnification.trackCarouselClickAtc(trackingData)
    }

    private fun onAddToCartUseCaseFailed(throwable: Throwable?) {
        throwable?.printStackTrace()
    }

    private fun executeAtcCommon(
        onAddToCartUseCaseSuccess: (addToCartDataModel: AddToCartDataModel?) -> Unit,
        onAddToCartUseCaseFailed: (Throwable) -> Unit,
        product: InspirationCarouselDataView.Option.Product,
    ) {
        val requestParams = product.createAddToCartRequestParams()

        addToCartUseCase.setParams(requestParams)
        addToCartUseCase.execute(onAddToCartUseCaseSuccess, onAddToCartUseCaseFailed)
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
