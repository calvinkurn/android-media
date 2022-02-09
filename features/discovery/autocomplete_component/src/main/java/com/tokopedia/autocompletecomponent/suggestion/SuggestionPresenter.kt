package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.autocompletecomponent.suggestion.chips.convertToSuggestionChipWidgetDataView
import com.tokopedia.autocompletecomponent.suggestion.domain.SuggestionRequestUtils
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionTopShop
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.autocompletecomponent.suggestion.domain.suggestiontracker.SuggestionTrackerUseCase
import com.tokopedia.autocompletecomponent.suggestion.doubleline.convertToDoubleLineShopAds
import com.tokopedia.autocompletecomponent.suggestion.doubleline.convertToDoubleLineVisitableList
import com.tokopedia.autocompletecomponent.suggestion.doubleline.convertToDoubleLineWithoutImageVisitableList
import com.tokopedia.autocompletecomponent.suggestion.productline.convertToSuggestionProductLineDataView
import com.tokopedia.autocompletecomponent.suggestion.separator.SuggestionSeparatorDataView
import com.tokopedia.autocompletecomponent.suggestion.singleline.convertToSingleLineVisitableList
import com.tokopedia.autocompletecomponent.suggestion.title.convertToTitleHeader
import com.tokopedia.autocompletecomponent.suggestion.topshop.SuggestionTopShopCardDataView
import com.tokopedia.autocompletecomponent.suggestion.topshop.convertToTopShopWidgetVisitableList
import com.tokopedia.autocompletecomponent.util.getProfileIdFromApplink
import com.tokopedia.autocompletecomponent.util.getShopIdFromApplink
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.Dimension90Utils
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.usecase.UseCase as RxUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

class SuggestionPresenter @Inject constructor(
    @Named(GET_SUGGESTION_USE_CASE)
    private val getSuggestionUseCase: UseCase<SuggestionUniverse>,
    @Named(SUGGESTION_TRACKER_USE_CASE)
    private val suggestionTrackerUseCase: RxUseCase<Void?>,
    private val topAdsUrlHitter: Lazy<TopAdsUrlHitter>,
    private val userSession: UserSessionInterface,
) : BaseDaggerPresenter<SuggestionContract.View>(), SuggestionContract.Presenter {

    private val listVisitable = mutableListOf<Visitable<*>>()
    private var isTyping = false
    private var searchParameter = mutableMapOf<String, String>()
    private var shouldAddSeparator = true

    override fun getSearchParameter(): Map<String, String> {
        return searchParameter
    }

    private fun getQueryKey(): String {
        return searchParameter[SearchApiConst.Q] ?: ""
    }

    override fun setIsTyping(isTyping: Boolean) {
        this.isTyping = isTyping
    }

    private fun getUserId(): String {
        return if (userSession.isLoggedIn) userSession.userId else "0"
    }

    //dimension90 = pageSource
    private fun getDimension90(): String {
        return Dimension90Utils.getDimension90(searchParameter)
    }

    private fun isTokoNow(): Boolean {
        return UrlParamUtils.isTokoNow(searchParameter)
    }

    override fun getSuggestion(searchParameter: Map<String, String>) {
        this.searchParameter = searchParameter.toMutableMap()

        getSuggestionUseCase.execute(
            ::onSuccessReceivedSuggestion,
            Throwable::printStackTrace,
            createGetSuggestionParams()
        )
    }

    private fun createGetSuggestionParams(): RequestParams {
        val chooseAddressData = view?.chooseAddressData ?: LocalCacheModel()

        return SuggestionRequestUtils.getParams(
            searchParameter,
            userSession,
            isTyping,
            chooseAddressData,
        )
    }

    private fun onSuccessReceivedSuggestion(suggestionUniverse: SuggestionUniverse) {
        clearListVisitable()
        updateListVisitable(suggestionUniverse)

        val visit = listVisitable
        notifyView()
    }

    private fun clearListVisitable() {
        listVisitable.clear()
    }

    private fun updateListVisitable(suggestionUniverse: SuggestionUniverse) {
        val typePosition = hashMapOf<String, Int?>()
        shouldAddSeparator = true
        for (item in suggestionUniverse.data.items) {
            if (suggestionUniverse.data.items.isEmpty()) continue

            when (item.template) {
                SUGGESTION_HEADER -> addTitleToVisitable(item)
                SUGGESTION_SINGLE_LINE -> addSingleLineToVisitable(typePosition, item)
                SUGGESTION_DOUBLE_LINE ->
                    addDoubleLineToVisitable(typePosition, item)
                SUGGESTION_DOUBLE_LINE_SHOP_ADS ->
                    addDoubleLineShopAdsToVisitable(typePosition, item, suggestionUniverse.cpmModel)
                SUGGESTION_TOP_SHOP_WIDGET ->
                    addTopShopWidgetToVisitable(typePosition, item, suggestionUniverse.topShop)
                SUGGESTION_DOUBLE_LINE_WITHOUT_IMAGE ->
                    addDoubleLineWithoutImageToVisitable(typePosition, item)
                SUGGESTION_PRODUCT_LINE -> addProductLineToVisitable(typePosition, item)
                SUGGESTION_CHIP_WIDGET -> addChipWidgetToVisitable(typePosition, item)
            }
        }
    }

    private fun addTitleToVisitable(item: SuggestionItem) {
        listVisitable.add(item.convertToTitleHeader())
    }

    private fun addSingleLineToVisitable(typePosition: HashMap<String, Int?>, item: SuggestionItem) {
        typePosition.incrementPosition(item.type)

        val singleLineDataView = item.convertToSingleLineVisitableList(
            getQueryKey(),
            position = typePosition[item.type] ?: 0,
            dimension90 = getDimension90()
        )

        listVisitable.add(singleLineDataView)
    }

    private fun addDoubleLineToVisitable(
        typePosition: HashMap<String, Int?>,
        item: SuggestionItem,
    ) {
        typePosition.incrementPosition(item.type)

        val doubleLineDataView = item.convertToDoubleLineVisitableList(
            getQueryKey(),
            position = typePosition[item.type] ?: 0,
            dimension90 = getDimension90()
        )

        listVisitable.add(doubleLineDataView)
    }

    private fun addDoubleLineShopAdsToVisitable(
        typePosition: HashMap<String, Int?>,
        item: SuggestionItem,
        cpmModel: CpmModel,
    ) {
        val cpmData = cpmModel.data.firstOrNull() ?: return

        typePosition.incrementPosition(TYPE_SHOP)

        val doubleLineDataView = item.convertToDoubleLineShopAds(
            getQueryKey(),
            position = typePosition[TYPE_SHOP] ?: 0,
            dimension90 = getDimension90(),
            cpmData,
        )

        listVisitable.add(doubleLineDataView)
    }

    private fun addTopShopWidgetToVisitable(
        typePosition: HashMap<String, Int?>,
        item: SuggestionItem,
        listTopShop: List<SuggestionTopShop>,
    ) {
        if (listTopShop.size <= 1) return

        typePosition.incrementPosition(item.type)

        val topShopWidgetDataView = item.convertToTopShopWidgetVisitableList(
            position = typePosition[item.type] ?: 0,
            listTopShop = listTopShop,
        )

        listVisitable.add(topShopWidgetDataView)
    }

    private fun addDoubleLineWithoutImageToVisitable(
        typePosition: HashMap<String, Int?>,
        item: SuggestionItem,
    ) {
        if (typePosition.isEmpty()) {
            processDoubleLineWithoutImageAtTop(typePosition, item)
        } else {
            processDoubleLineWithoutImageAtBottom(typePosition, item)
        }
    }

    private fun processDoubleLineWithoutImageAtTop(
        typePosition: HashMap<String, Int?>,
        item: SuggestionItem,
    ) {
        processDoubleLineWithoutImageToVisitable(typePosition, item)
    }

    private fun processDoubleLineWithoutImageAtBottom(
        typePosition: HashMap<String, Int?>,
        item: SuggestionItem,
    ) {
        addSuggestionSeparator()
        processDoubleLineWithoutImageToVisitable(typePosition, item)
    }

    private fun addSuggestionSeparator() {
        if (!shouldAddSeparator) return

        listVisitable.add(SuggestionSeparatorDataView())
    }

    private fun processDoubleLineWithoutImageToVisitable(
        typePosition: HashMap<String, Int?>,
        item: SuggestionItem,
    ) {
        typePosition.incrementPosition(item.type)

        val doubleLineWithoutImageDataView = item.convertToDoubleLineWithoutImageVisitableList(
            getQueryKey(),
            position = typePosition[item.type] ?: 0,
            dimension90 = getDimension90()
        )

        listVisitable.add(doubleLineWithoutImageDataView)

        shouldAddSeparator = false
    }

    private fun addProductLineToVisitable(typePosition: HashMap<String, Int?>, item: SuggestionItem) {
        typePosition.incrementPosition(item.type)

        val productLineDataView =
            item.convertToSuggestionProductLineDataView(
                getQueryKey(),
                position = typePosition[item.type] ?: 0,
                dimension90 = getDimension90()
            )

        listVisitable.add(productLineDataView)
    }

    private fun addChipWidgetToVisitable(typePosition: HashMap<String, Int?>, item: SuggestionItem) {
        typePosition.incrementPosition(item.type)

        val chipWidgetDataView = item.convertToSuggestionChipWidgetDataView(
            getQueryKey(),
            position = typePosition[item.type] ?: 0,
            dimension90 = getDimension90()
        )

        listVisitable.add(chipWidgetDataView)
    }

    private fun notifyView() {
        view.showSuggestionResult(listVisitable)
    }

    private fun HashMap<String, Int?>.incrementPosition(key: String) {
        if (this.containsKey(key)) {
            this[key] = this[key]?.plus(1)
        } else {
            this[key] = 1
        }
    }

    override fun onSuggestionItemClicked(item: BaseSuggestionDataView) {
        trackSuggestionItemWithUrl(item.urlTracker)
        trackSuggestionItemClick(item)
        trackSuggestionShopAds(item)

        view?.dropKeyBoard()
        view?.route(item.applink, searchParameter)
        view?.finish()
    }

    private fun trackSuggestionItemWithUrl(urlTracker: String) {
        if (urlTracker.isNotEmpty()) {
            val requestParam = createSuggestionTrackerParams(urlTracker)

            suggestionTrackerUseCase.execute(requestParam, createEmptySubscriberForUrlTracker())
        }
    }

    private fun createSuggestionTrackerParams(urlTracker:String) = SuggestionTrackerUseCase.getParams(
            urlTracker,
            userSession.deviceId,
            userSession.userId
    )

    private fun createEmptySubscriberForUrlTracker() = object : Subscriber<Void?>() {
        override fun onNext(t: Void?) {

        }

        override fun onCompleted() {

        }

        override fun onError(e: Throwable?) {

        }
    }

    private fun trackSuggestionItemClick(item: BaseSuggestionDataView) {
        if (isTokoNow()) trackTokoNowEventItemClicked(item)
        else trackEventItemClicked(item)
    }

    private fun trackTokoNowEventItemClicked(item: BaseSuggestionDataView) {
        when(item.type) {
            TYPE_KEYWORD -> {
                view?.trackTokoNowEventClickKeyword(
                    getTokoNowKeywordEventLabelForTracking(item),
                    item,
                )
            }
            TYPE_CURATED -> {
                view?.trackTokoNowEventClickCurated(getCuratedEventLabelForTracking(item), item)
            }
            else -> {
                view?.trackEventClick(item)
            }
        }
    }

    private fun getTokoNowKeywordEventLabelForTracking(item: BaseSuggestionDataView): String {
        return String.format(
                "keyword: %s - value: %s - po: %s - page: %s",
                item.title,
                item.searchTerm,
                item.position,
                item.applink
        )
    }

    private fun trackEventItemClicked(item: BaseSuggestionDataView) {
        when (item.type) {
            TYPE_KEYWORD -> {
                view?.trackEventClickKeyword(
                    getKeywordEventLabelForTracking(item),
                    item.dimension90,
                    item,
                )
            }
            TYPE_CURATED -> {
                view?.trackEventClickCurated(
                    getCuratedEventLabelForTracking(item),
                    item.trackingCode,
                    item.dimension90,
                    item,
                )
            }
            TYPE_SHOP -> {
                view?.trackEventClickShop(
                    getShopEventLabelForTracking(item),
                    item.dimension90,
                    item,
                )
            }
            TYPE_PROFILE -> {
                view?.trackEventClickProfile(getProfileEventLabelForTracking(item), item)
            }
            TYPE_RECENT_KEYWORD -> {
                view?.trackEventClickRecentKeyword(item.title, item.dimension90, item)
            }
            TYPE_LOCAL -> {
                view?.trackEventClickLocalKeyword(
                    getLocalEventLabelForTracking(item),
                    getUserId(),
                    item.dimension90,
                    item,
                )
            }
            TYPE_GLOBAL -> {
                view?.trackEventClickGlobalKeyword(
                    getGlobalEventLabelForTracking(item),
                    getUserId(),
                    item.dimension90,
                    item,
                )
            }
            TYPE_PRODUCT -> {
                view?.trackEventClickProductLine(
                    item,
                    getGlobalEventLabelForTracking(item),
                    getUserId(),
                )
            }
            TYPE_LIGHT -> {
                view?.trackEventClickCurated(
                    getCuratedLightEventLabelForTracking(item),
                    item.trackingCode,
                    item.dimension90,
                    item,
                )
            }
            else -> {
                view?.trackEventClick(item)
            }
        }
    }

    private fun getKeywordEventLabelForTracking(item: BaseSuggestionDataView): String {
        return String.format(
                "keyword: %s - value: %s - po: %s - applink: %s",
                item.title,
                item.searchTerm,
                item.position,
                item.applink
        )
    }

    private fun getCuratedEventLabelForTracking(item: BaseSuggestionDataView): String {
        return String.format(
                "keyword: %s - product: %s - po: %s - page: %s",
                item.searchTerm,
                item.title,
                item.position,
                item.applink
        )
    }

    private fun getShopEventLabelForTracking(item: BaseSuggestionDataView): String {
        return String.format(
                "%s - keyword: %s - shop: %s",
                getShopIdFromApplink(item.applink),
                item.searchTerm,
                item.title
        )
    }

    private fun getProfileEventLabelForTracking(item: BaseSuggestionDataView): String {
        return String.format(
                "keyword: %s - profile: %s - profile id: %s - po: %s",
                item.searchTerm,
                item.title,
                getProfileIdFromApplink(item.applink),
                item.position.toString()
        )
    }

    private fun getLocalEventLabelForTracking(item: BaseSuggestionDataView): String {
        return String.format(
                "keyword: %s - value: %s - applink: %s - campaign: %s",
                item.title,
                item.searchTerm,
                item.applink,
                getCampaignFromLocal()
        )
    }

    private fun getCuratedLightEventLabelForTracking(item: BaseSuggestionDataView): String {
        return String.format(
            "keyword: %s - product: %s - po: %s - page: %s",
            item.searchTerm,
            item.subtitle,
            item.position,
            item.applink
        )
    }

    private fun getCampaignFromLocal(): String {
        return searchParameter[SearchApiConst.SRP_PAGE_TITLE] ?: ""
    }

    private fun getGlobalEventLabelForTracking(item: BaseSuggestionDataView): String {
        return String.format(
                "keyword: %s - value: %s - applink: %s",
                item.title,
                item.searchTerm,
                item.applink
        )
    }

    private fun trackSuggestionShopAds(item: BaseSuggestionDataView) {
        val shopAdsDataView = item.shopAdsDataView ?: return
        val className = view?.className ?: ""

        topAdsUrlHitter.get().hitClickUrl(
            className,
            shopAdsDataView.clickUrl,
            "",
            "",
            shopAdsDataView.imageUrl,
        )
    }

    override fun onSuggestionItemImpressed(item: BaseSuggestionDataView) {
        when (item.type) {
            TYPE_LIGHT -> impressCurated(item, getCuratedLightEventLabelForTracking(item))
            TYPE_CURATED -> impressCurated(item, getCuratedEventLabelForTracking(item))
            else -> impressSuggestion(item)
        }

        impressTopAds(item)
    }

    private fun impressCurated(item: BaseSuggestionDataView, label: String) {
        view?.trackEventImpressCurated(label, item.trackingCode, item.dimension90, item)
    }

    private fun impressSuggestion(item: BaseSuggestionDataView) {
        view?.trackEventImpression(item)
    }

    private fun impressTopAds(item: BaseSuggestionDataView) {
        val shopAdsDataView = item.shopAdsDataView ?: return
        val className = view?.className ?: ""

        topAdsUrlHitter.get().hitImpressionUrl(
            className,
            shopAdsDataView.impressionUrl,
            "",
            "",
            shopAdsDataView.imageUrl,
        )
    }

    override fun onTopShopCardClicked(cardData: SuggestionTopShopCardDataView) {
        trackSuggestionItemWithUrl(cardData.urlTracker)
        trackEventTopShopClicked(cardData)

        view?.dropKeyBoard()
        view?.route(cardData.applink, searchParameter)
        view?.finish()
    }

    private fun trackEventTopShopClicked(cardData: SuggestionTopShopCardDataView) {
        when (cardData.type) {
            SUGGESTION_TOP_SHOP -> {
                view?.trackEventClickTopShopCard(getEventLabelForTopShop(cardData))
            }
            SUGGESTION_TOP_SHOP_SEE_MORE -> {
                view?.trackEventClickTopShopSeeMore(getEventLabelForTopShopSeeMore())
            }
        }
    }

    private fun getEventLabelForTopShop(cardData: SuggestionTopShopCardDataView): String {
        return "${cardData.id} - keyword: ${getQueryKey()}"
    }

    private fun getEventLabelForTopShopSeeMore(): String {
        return "keyword: ${getQueryKey()}"
    }

    override fun onSuggestionChipClicked(
        baseSuggestionDataView: BaseSuggestionDataView,
        item: BaseSuggestionDataView.ChildItem,
    ) {
        val label = "keyword: ${item.title} " +
            "- value: ${item.searchTerm} " +
            "- po: ${item.position} " +
            "- page: ${item.applink}"

        view?.trackClickChip(label, item.dimension90, baseSuggestionDataView)

        view?.dropKeyBoard()
        view?.route(item.applink, searchParameter)
        view?.finish()
    }

    override fun detachView() {
        super.detachView()
        getSuggestionUseCase.cancelJobs()
        suggestionTrackerUseCase.unsubscribe()
    }
}