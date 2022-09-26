package com.tokopedia.search.result.product.inspirationlistatc

import android.content.Context
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.result.presentation.view.fragment.ProductListFragment
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import javax.inject.Inject

class InspirationListAtcListenerDelegate @Inject constructor(
    val recycledViewPool: RecyclerView.RecycledViewPool,
    val productListFragment: ProductListFragment,
    @SearchContext
    context: Context,
): InspirationListAtcListener,
    ApplinkOpener by ApplinkOpenerDelegate,
    ContextProvider by WeakReferenceContextProvider(context) {

    override fun onListAtcSeeMoreClicked(data: InspirationCarouselDataView.Option) {
        openApplink(context, data.applink)
    }

    override fun onListAtcItemClicked(product: InspirationCarouselDataView.Option.Product) {
        openApplink(context, product.applink)
    }

    override fun onListAtcItemImpressed(product: InspirationCarouselDataView.Option.Product) {

    }

    override fun onListAtcItemAddToCart(product: InspirationCarouselDataView.Option.Product) {
        context?.let {
            AtcVariantHelper.goToAtcVariant(
                it,
                productId = product.id,
                pageSource = VariantPageSource.SRP_PAGESOURCE,
                shopId = product.shopId.toString(),
                startActivitResult = { intent, reqCode ->
                    productListFragment.startActivityForResult(intent, reqCode)
                }
            )
        }
    }

    override val carouselRecycledViewPool: RecyclerView.RecycledViewPool?
        get() = recycledViewPool
}