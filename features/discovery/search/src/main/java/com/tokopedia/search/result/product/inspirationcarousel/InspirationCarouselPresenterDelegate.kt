package com.tokopedia.search.result.product.inspirationcarousel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.product.ClassNameProvider
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.result.product.broadmatch.BroadMatchDataView
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressPresenterDelegate
import com.tokopedia.search.result.product.inspirationbundle.InspirationProductBundlingDataViewMapper.convertToInspirationProductBundleDataView
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcPresenterDelegate
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.search.result.product.suggestion.SuggestionDataView
import com.tokopedia.search.result.product.videowidget.InspirationCarouselVideoDataView
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.usecase.UseCase
import dagger.Lazy
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

@SearchScope
class InspirationCarouselPresenterDelegate @Inject constructor(
    private val view: InspirationCarouselView,
    private val inspirationListAtcPresenterDelegate: InspirationListAtcPresenterDelegate,
    private val topAdsUrlHitter: TopAdsUrlHitter,
    private val classNameProvider: ClassNameProvider,
    private val requestParamsGenerator: RequestParamsGenerator,
    @param:Named(SearchConstant.SearchProduct.SEARCH_PRODUCT_GET_INSPIRATION_CAROUSEL_CHIPS_PRODUCTS_USE_CASE)
    private val getInspirationCarouselChipsUseCase: Lazy<UseCase<InspirationCarouselChipsProductModel>>,
    private val chooseAddressDelegate: ChooseAddressPresenterDelegate,
    private val viewUpdater: ViewUpdater
): InspirationCarouselPresenter,
    ApplinkOpener by ApplinkOpenerDelegate {

    private var inspirationCarouselDataViewList = mutableListOf<InspirationCarouselDataView>()

    fun setInspirationCarouselDataViewList(
        inspirationCarouselDataViewList: List<InspirationCarouselDataView>
    ) {
        this.inspirationCarouselDataViewList = inspirationCarouselDataViewList.toMutableList()
    }

    fun processInspirationCarouselPosition(
        productList: List<Visitable<*>>,
        externalReference: String,
        action: (Int, List<Visitable<*>>) -> Unit,
    ) {
        if (inspirationCarouselDataViewList.isEmpty()) return

        val inspirationCarouselViewModelIterator = inspirationCarouselDataViewList.iterator()
        while (inspirationCarouselViewModelIterator.hasNext()) {
            val data = inspirationCarouselViewModelIterator.next()

            if (isInvalidInspirationCarouselLayout(data)) {
                inspirationCarouselViewModelIterator.remove()
                continue
            }

            if (data.position <= productList.size && shouldShowInspirationCarousel(data.layout)) {
                val inspirationCarouselVisitableList =
                    constructInspirationCarouselVisitableList(data, externalReference)

                action(data.position, inspirationCarouselVisitableList)

                inspirationCarouselViewModelIterator.remove()
            }
        }
    }

    private fun isInvalidInspirationCarouselLayout(data: InspirationCarouselDataView) : Boolean {
        return data.isInvalidCarouselChipsLayout()
            || data.isInvalidCarouselVideoLayout()
            || data.isInvalidProductBundleLayout()
    }

    private fun InspirationCarouselDataView.isInvalidCarouselChipsLayout() : Boolean {
        return layout == LAYOUT_INSPIRATION_CAROUSEL_CHIPS && isFirstOptionHasNoProducts()
    }

    private fun InspirationCarouselDataView.isInvalidCarouselVideoLayout() : Boolean {
        return isVideoLayout() && isFirstOptionHasNoProducts()
    }

    private fun InspirationCarouselDataView.isFirstOptionHasNoProducts() : Boolean {
        val firstOption = options.getOrNull(0)
        return firstOption != null && !firstOption.hasProducts()
    }

    private fun InspirationCarouselDataView.isInvalidProductBundleLayout() : Boolean {
        return isBundleLayout()
            && (options.size < PRODUCT_BUNDLE_MINIMUM_SIZE
                || options.size > PRODUCT_BUNDLE_MAXIMUM_SIZE)
    }

    private fun shouldShowInspirationCarousel(layout: String): Boolean {
        return showInspirationCarouselLayout.contains(layout)
    }

    private fun constructInspirationCarouselVisitableList(
        data: InspirationCarouselDataView,
        externalReference: String,
    ) = when {
            data.isDynamicProductLayout() ->
                convertInspirationCarouselToBroadMatch(data, externalReference)
            data.isVideoLayout() ->
                convertInspirationCarouselToInspirationCarouselVideo(data)
            data.isBundleLayout() ->
                convertInspirationCarouselToInspirationProductBundle(data, externalReference)
            data.isListAtcLayout() ->
                inspirationListAtcPresenterDelegate.convertInspirationCarouselToInspirationListAtc(data)
            else ->
                listOf(data)
        }

    private fun InspirationCarouselDataView.isDynamicProductLayout() =
        layout == LAYOUT_INSPIRATION_CAROUSEL_DYNAMIC_PRODUCT
    private fun InspirationCarouselDataView.isVideoLayout() =
        layout == LAYOUT_INSPIRATION_CAROUSEL_VIDEO
    private fun InspirationCarouselDataView.isBundleLayout() =
        layout == LAYOUT_INSPIRATION_CAROUSEL_BUNDLE
    private fun InspirationCarouselDataView.isListAtcLayout() =
        layout == LAYOUT_INSPIRATION_CAROUSEL_LIST_ATC

    private fun convertInspirationCarouselToInspirationProductBundle(
        data: InspirationCarouselDataView,
        externalReference: String
    ): List<Visitable<*>> {
        return listOf(data.convertToInspirationProductBundleDataView(
            view.queryKey,
            externalReference,
        ))
    }

    private fun convertInspirationCarouselToInspirationCarouselVideo(data: InspirationCarouselDataView) : List<Visitable<*>> {
        return listOf(InspirationCarouselVideoDataView(data))
    }

    private fun convertInspirationCarouselToBroadMatch(
        data: InspirationCarouselDataView,
        externalReference: String,
    ): List<Visitable<*>> {
        val broadMatchVisitableList = mutableListOf<Visitable<*>>()

        val hasTitle = data.title.isNotEmpty()

        if (hasTitle)
            broadMatchVisitableList.add(SuggestionDataView.create(data))

        broadMatchVisitableList.addAll(
            BroadMatchDataView.createList(data, externalReference, !hasTitle)
        )

        return broadMatchVisitableList
    }

    companion object {
        private val showInspirationCarouselLayout = listOf(
            LAYOUT_INSPIRATION_CAROUSEL_INFO,
            LAYOUT_INSPIRATION_CAROUSEL_LIST,
            LAYOUT_INSPIRATION_CAROUSEL_GRID,
            LAYOUT_INSPIRATION_CAROUSEL_CHIPS,
            LAYOUT_INSPIRATION_CAROUSEL_DYNAMIC_PRODUCT,
            LAYOUT_INSPIRATION_CAROUSEL_BUNDLE,
            LAYOUT_INSPIRATION_CAROUSEL_LIST_ATC,
            LAYOUT_INSPIRATION_CAROUSEL_VIDEO,
        )
    }

    override fun onInspirationCarouselProductImpressed(product: InspirationCarouselDataView.Option.Product) {
        if(product.isOrganicAds) sendTrackingImpressInspirationCarouselAds(product)

        when(product.layout) {
            LAYOUT_INSPIRATION_CAROUSEL_GRID ->
                view.trackEventImpressionInspirationCarouselGridItem(product)
            LAYOUT_INSPIRATION_CAROUSEL_CHIPS ->
                view.trackEventImpressionInspirationCarouselChipsItem(product)
            else -> view.trackEventImpressionInspirationCarouselListItem(product)
        }
    }

    private fun sendTrackingImpressInspirationCarouselAds(product: InspirationCarouselDataView.Option.Product) {
        topAdsUrlHitter.hitImpressionUrl(
            classNameProvider.className,
            product.topAdsViewUrl,
            product.id,
            product.name,
            product.imgUrl,
            SearchConstant.TopAdsComponent.ORGANIC_ADS
        )
    }

    override fun onInspirationCarouselProductClick(product: InspirationCarouselDataView.Option.Product) {
        view.openLink(product.applink, product.url)

        when(product.layout) {
            LAYOUT_INSPIRATION_CAROUSEL_GRID ->
                view.trackEventClickInspirationCarouselGridItem(product)
            LAYOUT_INSPIRATION_CAROUSEL_CHIPS ->
                view.trackEventClickInspirationCarouselChipsItem(product)
            else -> view.trackEventClickInspirationCarouselListItem(product)
        }

        if(product.isOrganicAds) sendTrackingClickInspirationCarouselAds(product)
    }

    private fun sendTrackingClickInspirationCarouselAds(product: InspirationCarouselDataView.Option.Product) {
        topAdsUrlHitter.hitClickUrl(
            classNameProvider.className,
            product.topAdsClickUrl,
            product.id,
            product.name,
            product.imgUrl,
            SearchConstant.TopAdsComponent.ORGANIC_ADS
        )
    }

    override fun onInspirationCarouselChipsClick(
        adapterPosition: Int,
        inspirationCarouselViewModel: InspirationCarouselDataView,
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
        searchParameter: Map<String, Any>,
    ) {
        changeActiveInspirationCarouselChips(inspirationCarouselViewModel, clickedInspirationCarouselOption)

        view.trackInspirationCarouselChipsClicked(clickedInspirationCarouselOption)
        viewUpdater.refreshItemAtIndex(adapterPosition)

        if (clickedInspirationCarouselOption.hasProducts()) return

        getInspirationCarouselChipProducts(
            adapterPosition,
            clickedInspirationCarouselOption,
            searchParameter,
            inspirationCarouselViewModel.title,
        )
    }

    private fun changeActiveInspirationCarouselChips(
        inspirationCarouselViewModel: InspirationCarouselDataView,
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
    ) {
        inspirationCarouselViewModel.options.forEach {
            it.isChipsActive = false
        }

        clickedInspirationCarouselOption.isChipsActive = true
    }

    private fun getInspirationCarouselChipProducts(
        adapterPosition: Int,
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
        searchParameter: Map<String, Any>,
        inspirationCarouselTitle: String,
    ) {
        getInspirationCarouselChipsUseCase.get().unsubscribe()

        val requestParams = requestParamsGenerator.createGetInspirationCarouselChipProductsRequestParams(
            clickedInspirationCarouselOption,
            searchParameter,
            chooseAddressDelegate.getChooseAddressParams(),
        )

        getInspirationCarouselChipsUseCase.get().execute(
            requestParams,
            createGetInspirationCarouselChipProductsSubscriber(
                adapterPosition,
                clickedInspirationCarouselOption,
                inspirationCarouselTitle,
            )
        )
    }

    private fun createGetInspirationCarouselChipProductsSubscriber(
        adapterPosition: Int,
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
        inspirationCarouselTitle: String,
    ): Subscriber<InspirationCarouselChipsProductModel> {
        return object : Subscriber<InspirationCarouselChipsProductModel>() {
            override fun onCompleted() { }

            override fun onError(e: Throwable) {}

            override fun onNext(inspirationCarouselChipsProductModel: InspirationCarouselChipsProductModel) {
                getInspirationCarouselChipsSuccess(
                    adapterPosition,
                    inspirationCarouselChipsProductModel,
                    clickedInspirationCarouselOption,
                    inspirationCarouselTitle
                )
            }
        }
    }

    private fun getInspirationCarouselChipsSuccess(
        adapterPosition: Int,
        inspirationCarouselChipsProductModel: InspirationCarouselChipsProductModel,
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
        inspirationCarouselTitle: String,
    ) {
        val mapper = InspirationCarouselProductDataViewMapper()
        val productList = mapper.convertToInspirationCarouselProductDataView(
            inspirationCarouselChipsProductModel.searchProductCarouselByIdentifier.product,
            clickedInspirationCarouselOption.optionPosition,
            clickedInspirationCarouselOption.inspirationCarouselType,
            clickedInspirationCarouselOption.layout,
            this::productLabelGroupToLabelGroupDataView,
            clickedInspirationCarouselOption.title,
            inspirationCarouselTitle,
            clickedInspirationCarouselOption.dimension90,
            clickedInspirationCarouselOption.externalReference,
        )

        clickedInspirationCarouselOption.product = productList

        viewUpdater.refreshItemAtIndex(adapterPosition)
    }

    private fun productLabelGroupToLabelGroupDataView(
        productLabelGroupList: List<SearchProductModel.ProductLabelGroup>,
    ): List<LabelGroupDataView> {
        return productLabelGroupList.map {
            LabelGroupDataView(
                it.position,
                it.type,
                it.title,
                it.url,
            )
        }
    }
}
