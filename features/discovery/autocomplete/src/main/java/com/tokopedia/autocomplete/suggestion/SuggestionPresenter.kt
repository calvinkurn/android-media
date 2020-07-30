package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionItem
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionTopShop
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.autocomplete.suggestion.domain.usecase.SuggestionTrackerUseCase
import com.tokopedia.autocomplete.suggestion.domain.usecase.SuggestionUseCase
import com.tokopedia.autocomplete.suggestion.doubleline.convertSuggestionItemToDoubleLineVisitableList
import com.tokopedia.autocomplete.suggestion.singleline.convertSuggestionItemToSingleLineVisitableList
import com.tokopedia.autocomplete.suggestion.title.convertToTitleHeader
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopCardViewModel
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

    override fun search() {
        getSuggestionUseCase.execute(
                createGetSuggestionParams(searchParameter, isTyping),
                createGetSuggestionSubscriber()
        )
    }

    private fun createGetSuggestionParams(searchParameter: HashMap<String, String>, isTyping: Boolean) = SuggestionUseCase.getParams(
        searchParameter,
        userSession.deviceId,
        userSession.userId,
        isTyping
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
        for (item in suggestionUniverse.data.items) {
            if (suggestionUniverse.data.items.isNotEmpty()) {
                when (item.template) {
                    SUGGESTION_HEADER -> addTitleToVisitable(item)
                    SUGGESTION_SINGLE_LINE -> addSingleLineToVisitable(typePosition, item)
                    SUGGESTION_DOUBLE_LINE -> addDoubleLineToVisitable(typePosition, item)
                    SUGGESTION_TOP_SHOP_WIDGET -> addTopShopWidgetToVisitable(typePosition, item, suggestionUniverse.topShop)
                    else -> addSingleLineToVisitable(typePosition, item)
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
            item.convertSuggestionItemToSingleLineVisitableList(getQueryKey(), position = it)
        }?.let {
            listVisitable.add(
                    it
            )
        }
    }

    private fun addDoubleLineToVisitable(typePosition: HashMap<String, Int?>, item: SuggestionItem) {
        typePosition.incrementPosition(item.type)
        typePosition[item.type]?.let { item.convertSuggestionItemToDoubleLineVisitableList(getQueryKey(), position = it) }?.let {
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

    override fun onSuggestionItemClicked(item: BaseSuggestionViewModel) {
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

    private fun trackEventItemClicked(item: BaseSuggestionViewModel) {
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
        }
    }

    private fun getKeywordEventLabelForTracking(item: BaseSuggestionViewModel): String {
        return String.format(
                "keyword: %s - value: %s - po: %s - applink: %s",
                item.title,
                item.searchTerm,
                item.position,
                item.applink
        )
    }

    private fun getCuratedEventLabelForTracking(item: BaseSuggestionViewModel): String {
        return String.format(
                "keyword: %s - product: %s - po: %s - page: %s",
                item.searchTerm,
                item.title,
                item.position,
                item.applink
        )
    }

    private fun getShopEventLabelForTracking(item: BaseSuggestionViewModel): String {
        return String.format(
                "%s - keyword: %s - shop: %s",
                getShopIdFromApplink(item.applink),
                item.searchTerm,
                item.title
        )
    }

    private fun getProfileEventLabelForTracking(item: BaseSuggestionViewModel): String {
        return String.format(
                "keyword: %s - profile: %s - profile id: %s - po: %s",
                item.searchTerm,
                item.title,
                getProfileIdFromApplink(item.applink),
                item.position.toString()
        )
    }

    override fun onTopShopCardClicked(card: SuggestionTopShopCardViewModel) {
        trackSuggestionItemWithUrl(card.urlTracker)
        trackEventTopShopClicked(card)

        view?.dropKeyBoard()
        view?.route(card.applink, searchParameter)
        view?.finish()
    }

    private fun trackEventTopShopClicked(card: SuggestionTopShopCardViewModel) {
        when (card.type) {
            SUGGESTION_TOP_SHOP -> {
                view?.trackEventClickTopShopCard(getEventLabelForTopShop(card))
            }
            SUGGESTION_TOP_SHOP_SEE_MORE -> {
                view?.trackEventClickTopShopSeeMore(getEventLabelForTopShopSeeMore())
            }
        }
    }

    private fun getEventLabelForTopShop(card: SuggestionTopShopCardViewModel): String {
        return "${card.id} - keyword: ${getQueryKey()}"
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