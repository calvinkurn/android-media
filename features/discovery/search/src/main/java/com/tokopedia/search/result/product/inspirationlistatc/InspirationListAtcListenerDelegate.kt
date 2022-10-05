package com.tokopedia.search.result.product.inspirationlistatc

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.search.R
import com.tokopedia.search.analytics.SearchTracking.getInspirationCarouselUnificationListName
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.result.presentation.view.fragment.ProductListFragment
import com.tokopedia.search.result.product.ProductListParameterListener
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnification
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnificationDataMapper.createCarouselTrackingUnificationData
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.search_activity_search.*
import javax.inject.Inject

@SearchScope
class InspirationListAtcListenerDelegate @Inject constructor(
    private val trackingQueue: TrackingQueue,
    private val inspirationCarouselTrackingUnification: InspirationCarouselTrackingUnification,
    private val inspirationListAtcPresenter: InspirationListAtcPresenter,
    @SearchContext
    context: Context,
    searchParameterProvider: SearchParameterProvider,
    productListFragment: ProductListFragment,
): InspirationListAtcListener,
    ApplinkOpener by ApplinkOpenerDelegate,
    ContextProvider by WeakReferenceContextProvider(context),
    SearchParameterProvider by searchParameterProvider,
    FragmentProvider by productListFragment {

    override fun onListAtcSeeMoreClicked(data: InspirationCarouselDataView.Option) {
        inspirationCarouselTrackingUnification.trackCarouselClickSeeAll(data.keyword, data)
        openApplink(context, data.applink)
    }

    override fun onListAtcItemClicked(product: InspirationCarouselDataView.Option.Product) {
        val trackingData = createCarouselTrackingUnificationData(product, getSearchParameter())
        inspirationCarouselTrackingUnification.trackCarouselClick(trackingData)

        openApplink(context, product.applink)
    }

    override fun onListAtcItemImpressed(product: InspirationCarouselDataView.Option.Product) {
        val trackingData = createCarouselTrackingUnificationData(product, getSearchParameter())
        inspirationCarouselTrackingUnification.trackCarouselImpression(trackingQueue, trackingData)
    }

    override fun onListAtcItemAddToCart(
        product: InspirationCarouselDataView.Option.Product,
        type: String
    ) {
        inspirationListAtcPresenter.onListAtcItemAddToCart(product, type)
    }

    override fun onAddToCartSuccess(addToCartDataModel: AddToCartDataModel?) {
        (getFragment().activity as SearchActivity).searchNavigationToolbar?.updateNotification()

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
    }
}
