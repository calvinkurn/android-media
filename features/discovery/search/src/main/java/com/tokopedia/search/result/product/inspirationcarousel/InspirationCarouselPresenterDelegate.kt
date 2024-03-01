package com.tokopedia.search.result.product.inspirationcarousel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.iris.Iris
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.InspirationCarouselChipsProductModel
import com.tokopedia.search.result.domain.model.SearchCouponModel
import com.tokopedia.search.result.domain.model.SearchCouponModel.Companion.isValidCoupon
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchRedeemCouponModel
import com.tokopedia.search.result.presentation.model.CouponDataView
import com.tokopedia.search.result.presentation.model.LabelGroupDataView
import com.tokopedia.search.result.product.ClassNameProvider
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.result.product.broadmatch.BroadMatchDataView
import com.tokopedia.search.result.product.byteio.ByteIOTrackingDataFactory
import com.tokopedia.search.result.product.chooseaddress.ChooseAddressPresenterDelegate
import com.tokopedia.search.result.product.deduplication.Deduplication
import com.tokopedia.search.result.product.inspirationbundle.InspirationProductBundlingDataViewMapper.convertToInspirationProductBundleDataView
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcPresenterDelegate
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordCardView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproducttitle.InspirationProductTitleDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.InspirationSeamlessMapper
import com.tokopedia.search.result.product.separator.VerticalSeparatorDataView
import com.tokopedia.search.result.product.suggestion.SuggestionDataView
import com.tokopedia.search.result.product.videowidget.InspirationCarouselVideoDataView
import com.tokopedia.search.utils.applinkopener.ApplinkOpener
import com.tokopedia.search.utils.applinkopener.ApplinkOpenerDelegate
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.usecase.UseCase
import dagger.Lazy
import org.json.JSONObject
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.suspendCoroutine

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
    private val viewUpdater: ViewUpdater,
    private val deduplication: Deduplication,
    @Named(SearchConstant.SearchCoupon.SEARCH_COUPON_USE_CASE)
    private val couponUseCase: com.tokopedia.usecase.coroutines.UseCase<SearchCouponModel>,
    @Named(SearchConstant.SearchCoupon.SEARCH_COUPON_REDEEM_USE_CASE)
    private val redeemCouponUseCase: com.tokopedia.usecase.coroutines.UseCase<SearchRedeemCouponModel>,
    private val iris: Iris,
    private val byteIOTrackingDataFactory: ByteIOTrackingDataFactory
) : InspirationCarouselPresenter,
    ApplinkOpener by ApplinkOpenerDelegate {

    private var inspirationCarouselSeamlessDataViewList =
        mutableListOf<InspirationCarouselDataView>()
    private var inspirationCarouselDataViewList = mutableListOf<InspirationCarouselDataView>()

    fun setInspirationCarouselSeamlessDataViewList(
        inspirationCarouselSeamlessDataViewList: List<InspirationCarouselDataView>
    ) {
        this.inspirationCarouselSeamlessDataViewList =
            inspirationCarouselSeamlessDataViewList.toMutableList()
    }

    fun processInspirationCarouselSeamlessPosition(
        totalProductItem: Int,
        externalReference: String,
        keyword: String,
        action: (Int, List<Visitable<*>>) -> Unit
    ) {
        if (inspirationCarouselSeamlessDataViewList.isEmpty()) return

        val iterator = inspirationCarouselSeamlessDataViewList.iterator()
        while (iterator.hasNext()) {
            val data = iterator.next()

            if (data.position <= totalProductItem) {
                val inspirationKeywordVisitableList = mutableListOf<Visitable<*>>()
                val (inspirationKeyboard, inspirationProduct, isOneOrMoreItemIsEmptyImage) =
                    InspirationSeamlessMapper.convertToInspirationList(
                        data.options,
                        externalReference,
                        deduplication
                    )
                inspirationKeywordVisitableList.add(
                    InspirationKeywordCardView.create(
                        data.title,
                        inspirationKeyboard,
                        isOneOrMoreItemIsEmptyImage,
                        data.type,
                        keyword
                    )
                )
                inspirationKeywordVisitableList.addAll(inspirationProduct)

                action(data.position, inspirationKeywordVisitableList)

                iterator.remove()
            }
        }
    }

    fun setInspirationCarouselDataViewList(
        inspirationCarouselDataViewList: List<InspirationCarouselDataView>
    ) {
        this.inspirationCarouselDataViewList = inspirationCarouselDataViewList.toMutableList()
    }

    fun processInspirationCarouselPosition(
        totalProductItem: Int,
        externalReference: String,
        isFirstPage: Boolean,
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

            if (data.position <= totalProductItem && shouldShowInspirationCarousel(data.layout)) {
                val inspirationCarouselVisitableList =
                    constructInspirationCarouselVisitableList(data, externalReference, isFirstPage)

                inspirationCarouselVisitableList?.let { action(data.position, it) }

                inspirationCarouselViewModelIterator.remove()
            }
        }
    }

    private fun isInvalidInspirationCarouselLayout(data: InspirationCarouselDataView): Boolean {
        return data.isInvalidCarouselChipsLayout() ||
            data.isInvalidCarouselVideoLayout() ||
            data.isInvalidProductBundleLayout()
    }

    private fun InspirationCarouselDataView.isInvalidCarouselChipsLayout(): Boolean {
        return layout == LAYOUT_INSPIRATION_CAROUSEL_CHIPS && isFirstOptionHasNoProducts()
    }

    private fun InspirationCarouselDataView.isInvalidCarouselVideoLayout(): Boolean {
        return isVideoLayout() && isFirstOptionHasNoProducts()
    }

    private fun InspirationCarouselDataView.isFirstOptionHasNoProducts(): Boolean {
        val firstOption = options.getOrNull(0)
        return firstOption != null && !firstOption.hasProducts()
    }

    private fun InspirationCarouselDataView.isInvalidProductBundleLayout(): Boolean {
        return isBundleLayout() &&
            (
                options.size < PRODUCT_BUNDLE_MINIMUM_SIZE ||
                    options.size > PRODUCT_BUNDLE_MAXIMUM_SIZE
                )
    }

    private fun shouldShowInspirationCarousel(layout: String): Boolean {
        return showInspirationCarouselLayout.contains(layout)
    }

    private fun constructInspirationCarouselVisitableList(
        data: InspirationCarouselDataView,
        externalReference: String,
        isFirstPage: Boolean,
    ) = when {
        data.isDynamicProductLayout() ->
            convertInspirationCarouselToBroadMatch(data, externalReference, isFirstPage)

        data.isVideoLayout() ->
            convertInspirationCarouselToInspirationCarouselVideo(data)

        data.isBundleLayout() ->
            convertInspirationCarouselToInspirationProductBundle(data, externalReference)

        data.isListAtcLayout() ->
            inspirationListAtcPresenterDelegate.convertInspirationCarouselToInspirationListAtc(data)

        data.isChipsLayout() ->
            convertInspirationCarouselToChipsCarousel(data)

        data.isSeamlessProductLayout() ->
            convertInspirationCarouselToSeamlessInspiration(data, externalReference)

        data.isCouponLayout() ->
            convertInspirationCarouselToCoupon(data)

        else ->
            listOf(data)
    }

    private fun convertInspirationCarouselToInspirationProductBundle(
        data: InspirationCarouselDataView,
        externalReference: String
    ): List<Visitable<*>> {
        return listOf(
            data.convertToInspirationProductBundleDataView(
                view.queryKey,
                externalReference
            )
        )
    }

    private fun convertInspirationCarouselToInspirationCarouselVideo(
        data: InspirationCarouselDataView
    ): List<Visitable<*>> {
        return listOf(InspirationCarouselVideoDataView(data))
    }

    private fun convertInspirationCarouselToBroadMatch(
        data: InspirationCarouselDataView,
        externalReference: String,
        isFirstPage: Boolean,
    ): List<Visitable<*>>? {
        val broadMatchVisitableList = mutableListOf<Visitable<*>>()

        val hasTitle = data.title.isNotEmpty()

        if (hasTitle) {
            broadMatchVisitableList.add(SuggestionDataView.create(data))
        }

        val optionList =
            BroadMatchDataView.createList(
                data,
                externalReference,
                !hasTitle,
                deduplication,
                byteIOTrackingDataFactory.create(isFirstPage),
            )

        if (optionList.isEmpty()) return null

        broadMatchVisitableList.addAll(optionList)

        return broadMatchVisitableList
    }

    private fun convertInspirationCarouselToChipsCarousel(
        data: InspirationCarouselDataView
    ): List<InspirationCarouselDataView>? {
        val firstOption = data.options.firstOrNull() ?: return null
        val productList = deduplication.removeDuplicate(firstOption.product)

        if (!deduplication.isCarouselWithinThreshold(firstOption, productList)) return null

        return listOf(
            data.copy(
                options = data.options.mapIndexed { index, option ->
                    if (index == 0) {
                        option.copy(product = productList)
                    } else {
                        option
                    }
                }
            )
        )
    }

    private fun convertInspirationCarouselToSeamlessInspiration(
        data: InspirationCarouselDataView,
        externalReference: String
    ): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        visitableList.add(InspirationProductTitleDataView.create(data))

        visitableList.addAll(
            data.options.flatMap { option ->
                InspirationSeamlessMapper.convertToInspirationProductDataView(
                    option,
                    externalReference,
                    deduplication
                )
            }
        )

        visitableList.add(VerticalSeparatorDataView)

        return visitableList
    }

    private fun convertInspirationCarouselToCoupon(
        data: InspirationCarouselDataView
    ): List<Visitable<*>> {
        return listOf(CouponDataView.create(data))
    }

    override fun onInspirationCarouselProductImpressed(product: InspirationCarouselDataView.Option.Product) {
        if (product.isOrganicAds) sendTrackingImpressInspirationCarouselAds(product)

        when (product.layout) {
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

        when (product.layout) {
            LAYOUT_INSPIRATION_CAROUSEL_GRID ->
                view.trackEventClickInspirationCarouselGridItem(product)

            LAYOUT_INSPIRATION_CAROUSEL_CHIPS ->
                view.trackEventClickInspirationCarouselChipsItem(product)

            else -> view.trackEventClickInspirationCarouselListItem(product)
        }

        if (product.isOrganicAds) sendTrackingClickInspirationCarouselAds(product)
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
        searchParameter: Map<String, Any>
    ) {
        changeActiveInspirationCarouselChips(
            inspirationCarouselViewModel,
            clickedInspirationCarouselOption
        )

        view.trackInspirationCarouselChipsClicked(clickedInspirationCarouselOption)
        viewUpdater.refreshItemAtIndex(adapterPosition)

        if (clickedInspirationCarouselOption.hasProducts()) return

        getInspirationCarouselChipProducts(
            adapterPosition,
            clickedInspirationCarouselOption,
            searchParameter,
            inspirationCarouselViewModel.title
        )
    }

    private fun changeActiveInspirationCarouselChips(
        inspirationCarouselViewModel: InspirationCarouselDataView,
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option
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
        inspirationCarouselTitle: String
    ) {
        getInspirationCarouselChipsUseCase.get().unsubscribe()

        val requestParams =
            requestParamsGenerator.createGetInspirationCarouselChipProductsRequestParams(
                clickedInspirationCarouselOption,
                searchParameter,
                chooseAddressDelegate.getChooseAddressParams()
            )

        getInspirationCarouselChipsUseCase.get().execute(
            requestParams,
            createGetInspirationCarouselChipProductsSubscriber(
                adapterPosition,
                clickedInspirationCarouselOption,
                inspirationCarouselTitle
            )
        )
    }

    private fun createGetInspirationCarouselChipProductsSubscriber(
        adapterPosition: Int,
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
        inspirationCarouselTitle: String
    ): Subscriber<InspirationCarouselChipsProductModel> {
        return object : Subscriber<InspirationCarouselChipsProductModel>() {
            override fun onCompleted() {}

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
        inspirationCarouselTitle: String
    ) {
        val mapper = InspirationCarouselProductDataViewMapper()
        val inspirationCarouselProduct =
            inspirationCarouselChipsProductModel.searchProductCarouselByIdentifier.product

        val productList = mapper.convertToInspirationCarouselProductDataView(
            inspirationCarouselProduct = inspirationCarouselProduct,
            productPosition = clickedInspirationCarouselOption.optionPosition,
            inspirationCarouselType = clickedInspirationCarouselOption.inspirationCarouselType,
            layout = clickedInspirationCarouselOption.layout,
            mapLabelGroupDataViewList = this::productLabelGroupToLabelGroupDataView,
            optionTitle = clickedInspirationCarouselOption.title,
            carouselTitle = inspirationCarouselTitle,
            dimension90 = clickedInspirationCarouselOption.dimension90,
            externalReference = clickedInspirationCarouselOption.externalReference,
            byteIOTrackingData = clickedInspirationCarouselOption.byteIOTrackingData,
            trackingOption = clickedInspirationCarouselOption.trackingOption,
        )

        clickedInspirationCarouselOption.product = productList

        viewUpdater.refreshItemAtIndex(adapterPosition)
    }

    private fun productLabelGroupToLabelGroupDataView(
        productLabelGroupList: List<SearchProductModel.ProductLabelGroup>
    ): List<LabelGroupDataView> {
        return productLabelGroupList.map {
            LabelGroupDataView(
                it.position,
                it.type,
                it.title,
                it.url
            )
        }
    }

    override suspend fun getInspirationCouponData(
        visitableList: MutableList<Visitable<*>>,
        dataView: CouponDataView
    ) = suspendCoroutine {
        val requestParams = dataView.createGetCouponDataRequestParam()
        couponUseCase.execute(
            { couponDataModel ->
                it.resumeWith(processCouponDataModel(visitableList, couponDataModel, dataView))
            },
            { _ ->
                it.resumeWith(processCouponDataModel(visitableList, null, dataView))
            },
            requestParams
        )
    }

    private fun processCouponDataModel(
        visitableList: MutableList<Visitable<*>>,
        couponDataModel: SearchCouponModel?,
        dataView: CouponDataView
    ): Result<List<Visitable<*>>> {
        val validCouponWidgetList =
            couponDataModel?.promoCatalogGetCouponListWidget?.couponListWidget?.filter {
                it.widgetInfo?.ctaList?.get(0).isValidCoupon()
            }

        if (couponDataModel == null || validCouponWidgetList.isNullOrEmpty()
        ) {
            visitableList.remove(dataView)
            return Result.success(visitableList)
        }

        dataView.updateCouponModel(couponDataModel)
        return Result.success(visitableList)
    }

    override fun ctaCoupon(dataView: CouponDataView, item: SearchCouponModel.CouponListWidget) {
        view.trackEventCtaCouponItem(dataView, item)
        when (item.widgetInfo?.ctaList?.getOrNull(0)?.type) {
            CouponDataView.CTA_TYPE_CLAIM -> {
                claimCoupon(dataView, item)
            }

            CouponDataView.CTA_TYPE_REDIRECT -> {
                openLink(item)
            }
        }
    }

    private fun openLink(item: SearchCouponModel.CouponListWidget) {
        val jsonMetaDataCta = item.widgetInfo?.ctaList?.getOrNull(0)?.jsonMetadata?.let {
            JSONObject(
                it
            )
        } ?: return
        val appLink = jsonMetaDataCta.optString(CouponDataView.JSON_METADATA_APP_LINK)
        val url = jsonMetaDataCta.optString(CouponDataView.JSON_METADATA_URL)
        view.openLink(appLink, url)
    }

    private fun claimCoupon(
        dataView: CouponDataView,
        item: SearchCouponModel.CouponListWidget
    ) {
        val requestParams = dataView.createRedeemRequestParam(item)
        redeemCouponUseCase.execute(
            { redeemCouponModel ->
                if (redeemCouponModel.hachikoRedeem?.coupons?.isNotEmpty() == true) {
                    dataView.updateCouponCtaData(redeemCouponModel, item)
                    viewUpdater.itemList?.indexOf(dataView)
                        ?.let { viewUpdater.refreshItemAtIndex(it) }

                    redeemCouponModel.hachikoRedeem?.redeemMessage?.let {
                        viewUpdater.couponRedeemToaster(
                            it
                        )
                    }
                }
            },
            { throwable ->
                when (throwable) {
                    is MessageErrorException -> {
                        throwable.message?.let { viewUpdater.couponRedeemToaster(it) }
                    }

                    else -> {
                    }
                }
            },
            requestParams
        )
    }

    override fun onCouponImpressed(couponDataView: CouponDataView) {
        couponDataView.impress(iris)
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
            LAYOUT_INSPIRATION_CAROUSEL_SEAMLESS,
            LAYOUT_INSPIRATION_CAROUSEL_SEAMLESS_PRODUCT,
            LAYOUT_INSPIRATION_CAROUSEL_CARD_COUPON
        )
    }
}
