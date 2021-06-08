package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionItem
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionTopShop
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.autocomplete.suggestion.domain.usecase.SuggestionTrackerUseCase
import com.tokopedia.autocomplete.suggestion.domain.usecase.SuggestionUseCase
import com.tokopedia.autocomplete.suggestion.doubleline.convertToDoubleLineVisitableList
import com.tokopedia.autocomplete.suggestion.doubleline.convertToDoubleLineWithoutImageVisitableList
import com.tokopedia.autocomplete.suggestion.productline.convertToSuggestionProductLineDataView
import com.tokopedia.autocomplete.suggestion.singleline.convertToSingleLineVisitableList
import com.tokopedia.autocomplete.suggestion.title.convertToTitleHeader
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopCardDataView
import com.tokopedia.autocomplete.suggestion.topshop.convertToTopShopWidgetVisitableList
import com.tokopedia.autocomplete.util.getProfileIdFromApplink
import com.tokopedia.autocomplete.util.getShopIdFromApplink
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

class SuggestionPresenter @Inject constructor() : BaseDaggerPresenter<SuggestionContract.View>(), SuggestionContract.Presenter {

    @Inject
    lateinit var getSuggestionUseCase: UseCase<SuggestionUniverse>

    @Inject
    lateinit var suggestionTrackerUseCase: UseCase<Void?>

    @Inject
    lateinit var userSession: UserSessionInterface

    private var listVisitable = mutableListOf<Visitable<*>>()

    private var isTyping = false

    private var searchParameter = HashMap<String, String>()

    private var shouldAddSeparator = true

    fun setSearchParameter(searchParameter: HashMap<String, String>) {
        this.searchParameter = searchParameter
    }

    fun getSearchParameter(): Map<String, String> {
        return searchParameter
    }

    private fun getQueryKey(): String {
        return searchParameter[SearchApiConst.Q] ?: ""
    }

    fun setIsTyping(isTyping: Boolean) {
        this.isTyping = isTyping
    }

    private fun getUserId(): String {
        return if (userSession.isLoggedIn) userSession.userId else "0"
    }

    override fun search() {
        val warehouseId = view?.chooseAddressData?.warehouse_id ?: ""
        getSuggestionUseCase.execute(
                createGetSuggestionParams(searchParameter, isTyping, warehouseId),
                createGetSuggestionSubscriber()
        )
    }

    private fun createGetSuggestionParams(searchParameter: HashMap<String, String>, isTyping: Boolean, warehouseId: String) = SuggestionUseCase.getParams(
        searchParameter,
        userSession.deviceId,
        userSession.userId,
        isTyping,
        warehouseId
    )

    private fun createGetSuggestionSubscriber(): Subscriber<SuggestionUniverse> = object : Subscriber<SuggestionUniverse>() {
        override fun onNext(suggestionUniverse: SuggestionUniverse) {
            onSuccessReceivedSuggestion(suggestionUniverse)
        }

        override fun onCompleted() { }

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun onSuccessReceivedSuggestion(suggestionUniverse: SuggestionUniverse) {
        clearListVisitable()
        updateListVisitable(suggestionUniverse)
        notifyView()
    }

    private fun clearListVisitable() {
        listVisitable.clear()
    }

    private fun updateListVisitable(suggestionUniverse: SuggestionUniverse) {
        val typePosition = HashMap<String, Int?>()
        shouldAddSeparator = true
        for (item in suggestionUniverse.data.items) {
            if (suggestionUniverse.data.items.isNotEmpty()) {
                when (item.template) {
                    SUGGESTION_HEADER -> addTitleToVisitable(item)
                    SUGGESTION_SINGLE_LINE -> addSingleLineToVisitable(typePosition, item)
                    SUGGESTION_DOUBLE_LINE -> addDoubleLineToVisitable(typePosition, item)
                    SUGGESTION_TOP_SHOP_WIDGET -> addTopShopWidgetToVisitable(typePosition, item, suggestionUniverse.topShop)
                    SUGGESTION_DOUBLE_LINE_WITHOUT_IMAGE -> addDoubleLineWithoutImageToVisitable(typePosition, item)
                    SUGGESTION_PRODUCT_LINE -> addProductLineToVisitable(typePosition, item)
                }
            }
        }
    }

    private fun addTitleToVisitable(item: SuggestionItem) {
        listVisitable.add(item.convertToTitleHeader())
    }

    private fun addSingleLineToVisitable(typePosition: HashMap<String, Int?>, item: SuggestionItem) {
        typePosition.incrementPosition(item.type)
        typePosition[item.type]?.let {
            item.convertToSingleLineVisitableList(getQueryKey(), position = it)
        }?.let {
            listVisitable.add(
                it
            )
        }
    }

    private fun addDoubleLineToVisitable(typePosition: HashMap<String, Int?>, item: SuggestionItem) {
        typePosition.incrementPosition(item.type)
        typePosition[item.type]?.let { item.convertToDoubleLineVisitableList(getQueryKey(), position = it) }?.let {
            listVisitable.add(
                    it
            )
        }
    }

    private fun addTopShopWidgetToVisitable(typePosition: HashMap<String, Int?>, item: SuggestionItem, listTopShop: List<SuggestionTopShop>) {
        if (listTopShop.size > 1) {
            typePosition.incrementPosition(item.type)
            typePosition[item.type]?.let {
                item.convertToTopShopWidgetVisitableList(position = it, listTopShop = listTopShop)
            }?.let {
                listVisitable.add(
                        it
                )
            }
        }
    }

    private fun addDoubleLineWithoutImageToVisitable(typePosition: HashMap<String, Int?>, item: SuggestionItem) {
        if (typePosition.isEmpty()) {
            processDoubleLineWithoutImageAtTop(typePosition, item)
        } else {
            processDoubleLineWithoutImageAtBottom(typePosition, item)
        }
    }

    private fun processDoubleLineWithoutImageAtTop(typePosition: HashMap<String, Int?>, item: SuggestionItem) {
        processDoubleLineWithoutImageToVisitable(typePosition, item)
    }

    private fun processDoubleLineWithoutImageAtBottom(typePosition: HashMap<String, Int?>, item: SuggestionItem) {
        addSuggestionSeparator()
        processDoubleLineWithoutImageToVisitable(typePosition, item)
    }

    private fun addSuggestionSeparator() {
        if (shouldAddSeparator) {
            listVisitable.add(SuggestionSeparatorDataView())
        }
    }

    private fun processDoubleLineWithoutImageToVisitable(typePosition: HashMap<String, Int?>, item: SuggestionItem) {
        typePosition.incrementPosition(item.type)
        typePosition[item.type]?.let { item.convertToDoubleLineWithoutImageVisitableList(getQueryKey(), position = it) }?.let {
            listVisitable.add(
                    it
            )
        }
        shouldAddSeparator = false
    }

    private fun addProductLineToVisitable(typePosition: HashMap<String, Int?>, item: SuggestionItem) {
        typePosition.incrementPosition(item.type)
        typePosition[item.type]?.let {
            item.convertToSuggestionProductLineDataView(getQueryKey(), position = it)
        }?.let {
            listVisitable.add(
                    it
            )
        }
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
        trackEventItemClicked(item)

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

    private fun trackEventItemClicked(item: BaseSuggestionDataView) {
        when (item.type) {
            TYPE_KEYWORD -> {
                view?.trackEventClickKeyword(getKeywordEventLabelForTracking(item))
            }
            TYPE_CURATED -> {
                view?.trackEventClickCurated(getCuratedEventLabelForTracking(item), item.trackingCode)
            }
            TYPE_SHOP -> {
                view?.trackEventClickShop(getShopEventLabelForTracking(item))
            }
            TYPE_PROFILE -> {
                view?.trackEventClickProfile(getProfileEventLabelForTracking(item))
            }
            TYPE_RECENT_KEYWORD -> {
                view?.trackEventClickRecentKeyword(item.title)
            }
            TYPE_LOCAL -> {
                view?.trackEventClickLocalKeyword(getLocalEventLabelForTracking(item), getUserId())
            }
            TYPE_GLOBAL -> {
                view?.trackEventClickGlobalKeyword(getGlobalEventLabelForTracking(item), getUserId())
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

    override fun detachView() {
        super.detachView()
        getSuggestionUseCase.unsubscribe()
        suggestionTrackerUseCase.unsubscribe()
    }
}