package com.tokopedia.search.result.product.addtocart

import android.content.Context
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.search.R
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener
import com.tokopedia.search.result.product.ClassNameProvider
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnification
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

@SearchScope
class AddToCartViewDelegate @Inject constructor(
    private val searchNavigationListener: SearchNavigationListener?,
    private val topAdsUrlHitter: TopAdsUrlHitter,
    searchParameterProvider: SearchParameterProvider,
    classNameProvider: ClassNameProvider,
    @SearchContext
    context: Context,
    fragmentProvider: FragmentProvider,
    queryKeyProvider: QueryKeyProvider,
): AddToCartView,
    SearchParameterProvider by searchParameterProvider,
    ContextProvider by WeakReferenceContextProvider(context),
    FragmentProvider by fragmentProvider,
    QueryKeyProvider by queryKeyProvider,
    ApplinkOpener by ApplinkOpenerDelegate,
    ClassNameProvider by classNameProvider {

    override fun trackAddToCart(trackingData: InspirationCarouselTrackingUnification.Data) {

    }

    override fun openAddToCartToaster(message: String, isSuccess: Boolean) {
        getFragment().view?.let {
            Toaster.build(
                it,
                message,
                Snackbar.LENGTH_SHORT,
                Toaster.TYPE_NORMAL,
                if (isSuccess) getFragment().getString(R.string.search_see_cart) else "",
            ) {
                if (isSuccess) openApplink(context, ApplinkConst.CART)
            }.show()
        }
    }

    override fun openVariantBottomSheet(
        data: ProductItemDataView,
        type: String
    ) {
        context?.let {
            AtcVariantHelper.goToAtcVariant(
                it,
                productId = data.productID,
                pageSource = VariantPageSource.SRP_PAGESOURCE,
                shopId = data.shopID,
                trackerCdListName = SearchTracking.getInspirationCarouselUnificationListName(
                    type,
                    "",
                ),
                startActivitResult = { intent, reqCode ->
                    getFragment().startActivityForResult(intent, reqCode)
                }
            )
        }
    }

    override fun trackAddToCartVariant(addToCartData: AddToCartData) {
//        product.asSearchComponentTracking(queryKey).click(TrackApp.getInstance().gtm)
    }

    override fun updateSearchBarNotification() {
        searchNavigationListener?.updateSearchBarNotification()
    }

    override fun trackAdsClick(addToCartData: AddToCartData) {
        topAdsUrlHitter.hitClickUrl(
            className,
            addToCartData.topAdsClickUrl,
            addToCartData.productId,
            addToCartData.productName,
            addToCartData.imageUrl,
            SearchConstant.TopAdsComponent.ORGANIC_ADS
        )
    }
}
