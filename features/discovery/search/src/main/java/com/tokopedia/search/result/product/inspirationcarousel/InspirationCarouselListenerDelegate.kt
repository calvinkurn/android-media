package com.tokopedia.search.result.product.inspirationcarousel

import android.content.Context
import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.SearchParameterProvider
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTracking
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import com.tokopedia.trackingoptimizer.TrackingQueue

class InspirationCarouselListenerDelegate(
    queryKeyProvider: QueryKeyProvider,
    context: Context?,
    searchParameterProvider: SearchParameterProvider,
    private val trackingQueue: TrackingQueue,
    private val userId: String,
    private val presenter: InspirationCarouselPresenter?,
): InspirationCarouselListener,
    QueryKeyProvider by queryKeyProvider,
    ContextProvider by WeakReferenceContextProvider(context),
    ApplinkOpener by ApplinkOpenerDelegate,
    SearchParameterProvider by searchParameterProvider {

    override fun onInspirationCarouselInfoProductClicked(product: InspirationCarouselDataView.Option.Product) {
        openApplink(context, product.applink)

        val products = ArrayList<Any>()
        products.add(product.getInspirationCarouselInfoProductAsObjectDataLayer())

        InspirationCarouselTracking.trackEventClickInspirationCarouselInfoProduct(
            product.inspirationCarouselType,
            queryKey,
            products,
        )
    }

    override fun onInspirationCarouselSeeAllClicked(
        inspirationCarouselDataViewOption: InspirationCarouselDataView.Option,
    ) {
        openApplink(context, inspirationCarouselDataViewOption.applink)

        InspirationCarouselTracking.trackCarouselClickSeeAll(
            queryKey,
            inspirationCarouselDataViewOption,
        )
    }

    override fun onInspirationCarouselGridBannerClicked(option: InspirationCarouselDataView.Option) {
        openApplink(context, option.bannerApplinkUrl)

        InspirationCarouselTracking.trackEventClickInspirationCarouselGridBanner(
            option.inspirationCarouselType,
            queryKey,
            option.getBannerDataLayer(queryKey),
            userId,
        )
    }

    override fun onInspirationCarouselChipsSeeAllClicked(
        inspirationCarouselDataViewOption: InspirationCarouselDataView.Option,
    ) {
        openApplink(context, inspirationCarouselDataViewOption.applink)

        InspirationCarouselTracking.trackCarouselClickSeeAll(
            queryKey,
            inspirationCarouselDataViewOption,
        )
    }

    override fun onInspirationCarouselListProductImpressed(
        product: InspirationCarouselDataView.Option.Product
    ) {
        presenter?.onInspirationCarouselProductImpressed(product)
    }

    override fun onInspirationCarouselOptionImpressed1Px(option: InspirationCarouselDataView.Option) {
        AppLogSearch.eventSearchResultShow(option.asByteIOSearchResult())
    }

    override fun onInspirationCarouselListProductClicked(
        product: InspirationCarouselDataView.Option.Product
    ) {
        presenter?.onInspirationCarouselProductClick(product)
    }

    override fun onInspirationCarouselGridProductImpressed(
        product: InspirationCarouselDataView.Option.Product
    ) {
        presenter?.onInspirationCarouselProductImpressed(product)
    }

    override fun onInspirationCarouselGridProductImpressed1Px(
        product: InspirationCarouselDataView.Option.Product,
    ) {
        AppLogSearch.eventSearchResultShow(
            product.asByteIOSearchResult(null)
        )

        AppLogSearch.eventProductShow(product.asByteIOProduct())
    }

    override fun onInspirationCarouselGridProductClicked(
        product: InspirationCarouselDataView.Option.Product,
    ) {
        presenter?.onInspirationCarouselProductClick(product)
    }

    override fun onInspirationCarouselChipsProductClicked(
        product: InspirationCarouselDataView.Option.Product,
    ) {
        presenter?.onInspirationCarouselProductClick(product)
    }

    override fun onImpressedInspirationCarouselChipsProduct(
        product: InspirationCarouselDataView.Option.Product,
    ) {
        presenter?.onInspirationCarouselProductImpressed(product)
    }

    override fun onInspirationCarouselChipsClicked(
        inspirationCarouselAdapterPosition: Int,
        inspirationCarouselViewModel: InspirationCarouselDataView,
        inspirationCarouselOption: InspirationCarouselDataView.Option,
    ) {
        presenter?.onInspirationCarouselChipsClick(
            adapterPosition = inspirationCarouselAdapterPosition,
            inspirationCarouselViewModel = inspirationCarouselViewModel,
            clickedInspirationCarouselOption = inspirationCarouselOption,
            searchParameter = getSearchParameter()?.getSearchParameterMap() ?: mapOf()
        )
    }
}
