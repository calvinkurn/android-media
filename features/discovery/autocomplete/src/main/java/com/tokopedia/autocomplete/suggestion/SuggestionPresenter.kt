package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
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
                        listVisitable.clear()
                        val typePosition = HashMap<String, Int?>()
                        for (item in suggestionData.items) {
                            if (suggestionData.items.isNotEmpty()) {
                                when (item.template) {
                                    SuggestionData.SUGGESTION_HEADER -> listVisitable.add(
                                            item.convertToTitleHeader()
                                    )
                                    SuggestionData.SUGGESTION_SINGLE_LINE -> {
                                        typePosition.setPosition(item.type)
                                        typePosition[item.type]?.let {
                                            item.convertSuggestionItemToSingleLineVisitableList(querySearch, position = it)
                                        }?.let {
                                            listVisitable.add(
                                                    it
                                            )
                                        }
                                    }
                                    SuggestionData.SUGGESTION_DOUBLE_LINE -> {
                                        typePosition.setPosition(item.type)
                                        typePosition[item.type]?.let { item.convertSuggestionItemToDoubleLineVisitableList(querySearch, position = it) }?.let {
                                            listVisitable.add(
                                                    it
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        view.showSuggestionResult(listVisitable)
                    }

                    private fun HashMap<String, Int?>.setPosition(key: String){
                        if(this.containsKey(key)) {
                            this[key] = this[key]?.plus(1)
                        } else {
                            this[key] = 1
                        }
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                }
        )
    }

    override fun detachView() {
        super.detachView()
        suggestionUseCase.unsubscribe()
    }
}