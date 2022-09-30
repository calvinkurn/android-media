package com.tokopedia.search.result.product.inspirationlistatc

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.iris.Iris
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.search.R
import com.tokopedia.search.analytics.SearchTracking.getInspirationCarouselUnificationListName
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.result.presentation.view.fragment.ProductListFragment
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnification
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.search_activity_search.*
import rx.Subscriber
import javax.inject.Inject

class InspirationListAtcListenerDelegate @Inject constructor(
    private val recycledViewPool: RecyclerView.RecycledViewPool,
    private val productListFragment: ProductListFragment,
    private val addToCartUseCase: AddToCartUseCase,
    private val userSession: UserSessionInterface,
    private val trackingQueue: TrackingQueue,
    private val inspirationCarouselTrackingUnification: InspirationCarouselTrackingUnification,
    @SearchContext
    context: Context,
    searchParameterProvider: SearchParameterProvider,
): InspirationListAtcListener,
    ApplinkOpener by ApplinkOpenerDelegate,
    ContextProvider by WeakReferenceContextProvider(context),
    SearchParameterProvider by searchParameterProvider {

    companion object {
        private const val DEFAULT_USER_ID = "0"
    }

    private val clickedProducts = mutableListOf<InspirationCarouselDataView.Option.Product>()

    override fun onListAtcSeeMoreClicked(data: InspirationCarouselDataView.Option) {
        inspirationCarouselTrackingUnification.trackCarouselClickSeeAll(data.keyword, data)
        openApplink(context, data.applink)
    }

    override fun onListAtcItemClicked(product: InspirationCarouselDataView.Option.Product) {
        val trackingData = createCarouselTrackingUnificationData(product, getSearchParameter())
        inspirationCarouselTrackingUnification.trackCarouselAtcClick(trackingData)

        clickedProducts.add(product)

        openApplink(context, product.applink)
    }

    override fun onListAtcItemImpressed(product: InspirationCarouselDataView.Option.Product) {
        val trackingData = createCarouselTrackingUnificationData(product, getSearchParameter())
        inspirationCarouselTrackingUnification.trackCarouselImpression(trackingQueue, trackingData)
    }

    override fun onListAtcItemAddToCart(
        product: InspirationCarouselDataView.Option.Product,
        type: String,
    ) {
        if (product.shouldOpenVariantBottomSheet()) {
            context?.let {
                AtcVariantHelper.goToAtcVariant(
                    it,
                    productId = product.id,
                    pageSource = VariantPageSource.SRP_PAGESOURCE,
                    shopId = product.shopId.toString(),
                    trackerCdListName = getInspirationCarouselUnificationListName(
                        type,
                        product.componentId,
                    ),
                    startActivitResult = { intent, reqCode ->
                        productListFragment.startActivityForResult(intent, reqCode)
                    }
                )
            }
        } else {
            executeAtcCommon(::onAddToCartUseCaseSuccess, ::onAddToCartUseCaseFailed, product)
        }
    }

    private fun onAddToCartUseCaseSuccess(addToCartDataModel: AddToCartDataModel?) {
        (productListFragment.activity as SearchActivity).searchNavigationToolbar?.updateNotification()

        productListFragment.view?.let {
            Toaster.build(
                it,
                addToCartDataModel?.data?.message?.firstOrNull() ?: "",
                Snackbar.LENGTH_SHORT,
                Toaster.TYPE_NORMAL,
                productListFragment.getString(R.string.search_see_cart),
            ) {
                openApplink(context, ApplinkConst.CART)
            }.show()
        }
    }

    private fun onAddToCartUseCaseFailed(throwable: Throwable?) {
        throwable?.printStackTrace()
    }

    override val carouselRecycledViewPool: RecyclerView.RecycledViewPool?
        get() = recycledViewPool

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
            productId = id.toLong(),
            shopId = shopId,
            quantity = minOrder,
            productName = name,
            price = priceStr,
            userId = if (userSession.isLoggedIn) userSession.userId else DEFAULT_USER_ID
        )
    }

    fun getProductById(id: String): InspirationCarouselDataView.Option.Product? {
        return clickedProducts.firstOrNull { it.id == id }
    }
}