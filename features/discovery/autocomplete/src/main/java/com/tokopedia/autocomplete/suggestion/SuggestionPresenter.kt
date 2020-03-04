package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.autocomplete.analytics.AutocompleteTracking
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

class SuggestionPresenter @Inject constructor() : BaseDaggerPresenter<SuggestionContract.View>(), SuggestionContract.Presenter {

    private var querySearch = ""

    @Inject
    lateinit var suggestionUseCase: SuggestionUseCase

    @Inject
    lateinit var userSession: UserSessionInterface

    private var listVisitable = mutableListOf<Visitable<*>>()

    override fun search(searchParameter: SearchParameter) {
        this.querySearch = searchParameter.getSearchQuery()
        suggestionUseCase.execute(
                SuggestionUseCase.getParams(
                        searchParameter.getSearchParameterMap(),
                        userSession.deviceId,
                        userSession.userId
                ),
                object : Subscriber<SuggestionData>() {
                    override fun onNext(suggestionData: SuggestionData) {
                        clearListVisitable()
                        updateListVisitable(suggestionData)
                        notifyView()
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                }
        )
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

    override fun onItemClicked(item: BaseSuggestionViewModel) {
        when {
            item.type.equals(TYPE_KEYWORD, ignoreCase = true) -> {
                AutocompleteTracking.eventClickKeyword(getKeywordEventLabelForTracking(item))
            }
            item.type.equals(TYPE_CURATED, ignoreCase = true) -> {
                AutocompleteTracking.eventClickCurated(getCuratedEventLabelForTracking(item))
            }
            item.type.equals(TYPE_SHOP, ignoreCase = true) -> {
                AutocompleteTracking.eventClickShop(getShopEventLabelForTracking(item))
            }
            item.type.equals(TYPE_PROFILE, ignoreCase = true) -> {
                AutocompleteTracking.eventClickProfile(getProfileEventLabelForTracking(item))
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
        val prefix = "tokopedia://people/"

        return try {
            if (applink.startsWith(prefix)) {
                applink.substring(prefix.length)
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
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
        val prefix = "tokopedia://shop/"

        return try {
            if (applink.startsWith(prefix)) {
                applink.substring(prefix.length, applink.indexOf("?"))
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    override fun detachView() {
        super.detachView()
        suggestionUseCase.unsubscribe()
    }
}