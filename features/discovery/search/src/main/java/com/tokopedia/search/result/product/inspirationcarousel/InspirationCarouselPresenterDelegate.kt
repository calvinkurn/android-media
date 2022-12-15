package com.tokopedia.search.result.product.inspirationcarousel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.product.broadmatch.BroadMatchDataView
import com.tokopedia.search.result.product.inspirationbundle.InspirationProductBundlingDataViewMapper.convertToInspirationProductBundleDataView
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcPresenterDelegate
import com.tokopedia.search.result.product.suggestion.SuggestionDataView
import com.tokopedia.search.result.product.videowidget.InspirationCarouselVideoDataView
import javax.inject.Inject

class InspirationCarouselPresenterDelegate @Inject constructor(
    private val view: InspirationCarouselView,
    private val inspirationListAtcPresenterDelegate: InspirationListAtcPresenterDelegate,
): InspirationCarouselPresenter {

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
}
