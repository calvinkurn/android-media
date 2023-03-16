package com.tokopedia.search.result.product.wishlist

import android.content.Context
import android.os.Bundle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WISHLIST_PRODUCT_ID
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.ProductCardOptionsModel.WishlistResult
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.search.analytics.RecommendationTracking
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.ClassNameProvider
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import javax.inject.Inject
import com.tokopedia.wishlist_common.R as Rwishlist

@SearchScope
class WishlistViewDelegate @Inject constructor(
    private val wishlistHelper: WishlistHelper,
    @SearchContext
    context: Context?,
    queryKeyProvider: QueryKeyProvider,
    fragmentProvider: FragmentProvider,
    classNameProvider: ClassNameProvider,
) : WishlistView,
    ContextProvider by WeakReferenceContextProvider(context),
    QueryKeyProvider by queryKeyProvider,
    FragmentProvider by fragmentProvider,
    ClassNameProvider by classNameProvider {

    override fun launchLoginActivity(productId: String?) {
        val extras = Bundle().apply {
            putString(WISHLIST_PRODUCT_ID, productId)
        }

        val intent = RouteManager.getIntent(getFragment().activity, ApplinkConst.LOGIN).apply {
            putExtras(extras)
        }

        getFragment().startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    override fun trackWishlistRecommendationProductLoginUser(isAddWishlist: Boolean) {
        RecommendationTracking.eventUserClickProductToWishlistForUserLogin(isAddWishlist)
    }

    override fun trackWishlistRecommendationProductNonLoginUser() {
        RecommendationTracking.eventUserClickProductToWishlistForNonLogin()
    }

    override fun trackWishlistProduct(wishlistTrackingModel: WishlistTrackingModel) {
        WishlistTracking.eventSuccessWishlistSearchResultProduct(wishlistTrackingModel)
    }

    override fun updateWishlistStatus(productId: String, isWishlisted: Boolean) {
        wishlistHelper.updateWishlistStatus(productId, isWishlisted)
    }

    override fun showMessageSuccessWishlistAction(wishlistResult: WishlistResult) {
        val view = getFragment().view ?: return
        val context = context ?: return

        if (wishlistResult.isAddWishlist)
            AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(wishlistResult, context, view)
        else
            AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(wishlistResult, context, view)
    }

    override fun hitWishlistClickUrl(productCardOptionsModel: ProductCardOptionsModel) {
        context?.let {
            TopAdsUrlHitter(it).hitClickUrl(
                className,
                productCardOptionsModel.topAdsClickUrl + CLICK_TYPE_WISHLIST,
                productCardOptionsModel.productId,
                productCardOptionsModel.productName,
                productCardOptionsModel.productImageUrl
            )
        }
    }

    override fun showMessageFailedWishlistAction(wishlistResult: WishlistResult) {
        val view = getFragment().view ?: return
        val errorMessage = getWishlistErrorMessage(wishlistResult)
        val ctaText = wishlistResult.ctaTextV2.ifEmpty { "" }

        getFragment().context?.let {
            AddRemoveWishlistV2Handler.showWishlistV2ErrorToasterWithCta(
                errorMessage,
                ctaText,
                wishlistResult.ctaActionV2,
                view,
                it,
            )
        }
    }

    private fun getWishlistErrorMessage(wishlistResult: WishlistResult) =
        if (wishlistResult.messageV2.isNotEmpty())
            wishlistResult.messageV2
        else if (wishlistResult.isAddWishlist)
            getFragment().getString(Rwishlist.string.on_failed_add_to_wishlist_msg)
        else
            getFragment().getString(Rwishlist.string.on_failed_remove_from_wishlist_msg)

    companion object {
        private const val CLICK_TYPE_WISHLIST = "&click_type=wishlist"
        private const val REQUEST_CODE_LOGIN = 561
    }
}
