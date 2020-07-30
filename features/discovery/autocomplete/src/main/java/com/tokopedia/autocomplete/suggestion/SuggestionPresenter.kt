package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.autocomplete.suggestion.doubleline.convertSuggestionItemToDoubleLineVisitableList
import com.tokopedia.autocomplete.suggestion.singleline.convertSuggestionItemToSingleLineVisitableList
import com.tokopedia.autocomplete.suggestion.title.convertToTitleHeader
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

class SuggestionPresenter @Inject constructor() : BaseDaggerPresenter<SuggestionContract.View>(), SuggestionContract.Presenter {

    private var querySearch = ""

    @Inject
    lateinit var getSuggestionUseCase: UseCase<SuggestionData>

    @Inject
    lateinit var suggestionTrackerUseCase: UseCase<Void?>

    @Inject
    lateinit var userSession: UserSessionInterface

    private var listVisitable = mutableListOf<Visitable<*>>()

    private var isTyping = false

    fun setIsTyping(isTyping: Boolean) {
        this.isTyping = isTyping
    }

    override fun search(searchParameter: SearchParameter) {
        this.querySearch = searchParameter.getSearchQuery()

        getSuggestionUseCase.execute(
                createGetSuggestionParams(searchParameter, isTyping),
                createGetSuggestionSubscriber()
        )
    }

    private fun createGetSuggestionParams(searchParameter: SearchParameter, isTyping: Boolean) = SuggestionUseCase.getParams(
        searchParameter.getSearchParameterMap(),
        userSession.deviceId,
        userSession.userId,
        isTyping
    )

    private fun createGetSuggestionSubscriber(): Subscriber<SuggestionData> = object : Subscriber<SuggestionData>() {
        override fun onNext(suggestionData: SuggestionData) {
            onSuccessReceivedSuggestion(suggestionData)
        }

        override fun onCompleted() { }

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun onSuccessReceivedSuggestion(suggestionData: SuggestionData) {
        clearListVisitable()
        updateListVisitable(suggestionData)
        notifyView()
    }

    private fun clearListVisitable() {
        listVisitable.clear()
    }

    private fun updateListVisitable(suggestionData: SuggestionData) {
        val typePosition = HashMap<String, Int?>()
        for (item in suggestionData.items) {
            if (suggestionData.items.isNotEmpty()) {
                when (item.template) {
                    SUGGESTION_HEADER -> addTitleToVisitable(item)
                    SUGGESTION_SINGLE_LINE -> addSingleLineToVisitable(typePosition, item)
                    SUGGESTION_DOUBLE_LINE -> addDoubleLineToVisitable(typePosition, item)
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
            item.convertSuggestionItemToSingleLineVisitableList(querySearch, position = it)
        }?.let {
            listVisitable.add(
                    it
            )
        }
    }

    private fun addDoubleLineToVisitable(typePosition: HashMap<String, Int?>, item: SuggestionItem) {
        typePosition.incrementPosition(item.type)
        typePosition[item.type]?.let { item.convertSuggestionItemToDoubleLineVisitableList(querySearch, position = it) }?.let {
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

    override fun onSuggestionItemClicked(item: BaseSuggestionViewModel) {
        trackSuggestionItemWithUrl(item)
        trackEventItemClicked(item)

        view?.dropKeyBoard()
        view?.route(item.applink)
        view?.finish()
    }

    private fun trackSuggestionItemWithUrl(item: BaseSuggestionViewModel) {
        if (item.urlTracker.isNotEmpty()) {
            val requestParam = createSuggestionTrackerParams(item.urlTracker)

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

    private fun getShopIdFromApplink(applink: String): String {
        return applink.substringWithPrefixAndSuffix("tokopedia://shop/", "?")
    }

    private fun String.substringWithPrefixAndSuffix(prefix: String, suffix: String): String {
        val suffixIndex = indexOf(suffix)

        val startIndex = prefix.length
        val endIndex = if (suffixIndex == -1) length else suffixIndex

        return try {
            if (startsWith(prefix)) {
                substring(startIndex, endIndex)
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
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

    private fun getProfileIdFromApplink(applink: String): String {
        return applink.substringWithPrefixAndSuffix("tokopedia://people/", "?")
    }

    override fun detachView() {
        super.detachView()
        getSuggestionUseCase.unsubscribe()
        suggestionTrackerUseCase.unsubscribe()
    }
}