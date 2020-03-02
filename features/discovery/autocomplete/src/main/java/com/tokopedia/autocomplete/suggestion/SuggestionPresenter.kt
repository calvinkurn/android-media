package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.autocomplete.TabSuggestionViewModel
import com.tokopedia.autocomplete.domain.model.SearchData
import com.tokopedia.autocomplete.usecase.AutoCompleteUseCase
import com.tokopedia.autocomplete.viewmodel.TitleSearch
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

class SuggestionPresenter @Inject constructor() : BaseDaggerPresenter<SuggestionContract.View>(), SuggestionContract.Presenter {

    companion object {
        const val DIGITAL = "top_digital"
        const val CATEGORY = "category"
        const val AUTOCOMPLETE = "autocomplete"
        const val HOTLIST = "hotlist"
        const val IN_CATEGORY = "in_category"
        const val SHOP = "shop"
        const val PROFILE = "profile"
        const val TOP_PROFILE = "top_profile"
    }

    private var querySearch = ""

    @Inject
    lateinit var autoCompleteUseCase: AutoCompleteUseCase

    @Inject
    lateinit var userSession: UserSessionInterface

    private var allSuggestionList = mutableListOf<Visitable<*>>()
    private var productSuggestionList = mutableListOf<Visitable<*>>()
    private var shopSuggestionList = mutableListOf<Visitable<*>>()

    override fun search(searchParameter: SearchParameter) {
        this.querySearch = searchParameter.getSearchQuery()
        autoCompleteUseCase.execute(
                AutoCompleteUseCase.getParams(
                        searchParameter.getSearchParameterMap(),
                        userSession.deviceId,
                        userSession.userId
                ),
                object : Subscriber<List<SearchData>>() {
                    override fun onNext(t: List<SearchData>) {
                        val tabAutoCompleteViewModel = TabSuggestionViewModel()
                        tabAutoCompleteViewModel.searchTerm = querySearch

                        allSuggestionList.clear()
                        productSuggestionList.clear()
                        shopSuggestionList.clear()

                        for (searchData in t) {
                            if (searchData.items.size > 0) {
                                when (searchData.id) {
                                    DIGITAL, CATEGORY, AUTOCOMPLETE, HOTLIST, IN_CATEGORY, SHOP, PROFILE, TOP_PROFILE -> {
                                        tabAutoCompleteViewModel.addList(searchData)
                                    }
                                }
                            }
                        }
                        prepareFragmentResult(tabAutoCompleteViewModel.list, tabAutoCompleteViewModel.searchTerm)
                        view.showSuggestionResult(allSuggestionList, productSuggestionList, shopSuggestionList)
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                }
        )
    }

    private fun prepareFragmentResult(data: List<SearchData>, searchTerm: String) {
        for (searchData in data) {
            when (searchData.id) {
                SearchData.AUTOCOMPLETE_DIGITAL -> {
                    allSuggestionList.addAll(searchData.convertDigitalSearchToVisitableList(searchTerm))
                }
                SearchData.AUTOCOMPLETE_CATEGORY -> {
                    allSuggestionList.addAll(searchData.convertCategorySearchToVisitableList(searchTerm).insertTitle(searchData.name))
                }
                SearchData.AUTOCOMPLETE_DEFAULT -> {
                    val list = searchData.convertAutoCompleteSearchToVisitableList(searchTerm)
                    allSuggestionList.addAll(list)
                    productSuggestionList.addAll(list)
                }
                SearchData.AUTOCOMPLETE_HOTLIST -> {
                    allSuggestionList.addAll(searchData.convertHotlistSearchToVisitableList(searchTerm).insertTitle(searchData.name))
                }
                SearchData.AUTOCOMPLETE_IN_CATEGORY -> {
                    val list = searchData.convertInCategorySearchToVisitableList(searchTerm)
                    allSuggestionList.addAll(list)
                    productSuggestionList.addAll(list)
                }
                SearchData.AUTOCOMPLETE_SHOP -> {
                    val list = searchData.convertShopSearchToVisitableList(searchTerm)
                    shopSuggestionList.addAll(list)
                    allSuggestionList.addAll(list.insertTitle(searchData.name))
                }
                SearchData.AUTOCOMPLETE_PROFILE -> {
                    allSuggestionList.addAll(searchData.convertProfileSearchToVisitableList(searchTerm).insertTitle(searchData.name))
                }
                SearchData.AUTOCOMPLETE_TOP_PROFILE -> {
                    allSuggestionList.addAll(searchData.convertTopProfileSearchToVisitableList(searchTerm))
                }
            }
        }
    }

    private fun MutableList<Visitable<*>>.insertTitle(name: String): List<Visitable<*>> {
        val titleSearch = TitleSearch()
        titleSearch.title = name
        this.add(0, titleSearch)
        return this
    }

    override fun detachView() {
        super.detachView()
        autoCompleteUseCase.unsubscribe()
    }

}