package com.tokopedia.autocomplete.suggestion.view.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.autocomplete.TabSuggestionViewModel
import com.tokopedia.autocomplete.domain.model.SearchData
import com.tokopedia.autocomplete.suggestion.utils.*
import com.tokopedia.autocomplete.suggestion.view.SuggestionContract
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

    private var allFragmentList = mutableListOf<Visitable<*>>()
    private var productFragmentList = mutableListOf<Visitable<*>>()
    private var shopFragmentList = mutableListOf<Visitable<*>>()

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
                        allFragmentList.clear()
                        productFragmentList.clear()
                        shopFragmentList.clear()

                        loop@ for (searchData in t) {
                            if (searchData.items.size > 0) {
                                when (searchData.id) {
                                    DIGITAL, CATEGORY, AUTOCOMPLETE, HOTLIST, IN_CATEGORY, SHOP, PROFILE, TOP_PROFILE -> {
                                        tabAutoCompleteViewModel.searchTerm = querySearch
                                        tabAutoCompleteViewModel.addList(searchData)
                                        continue@loop
                                    }
                                }
                            }
                        }
                        prepareFragmentResult(tabAutoCompleteViewModel.list, tabAutoCompleteViewModel.searchTerm)
                        view.showSuggestionResult(allFragmentList, productFragmentList, shopFragmentList)
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                }
        )
    }

    private fun prepareFragmentResult(data: List<SearchData>, searchTerm: String) {
        loop@ for (searchData in data) {
            when (searchData.id) {
                SearchData.AUTOCOMPLETE_DIGITAL -> {
                    allFragmentList.addAll(searchData.convertDigitalSearchToVisitableList(searchTerm))
                    continue@loop
                }
                SearchData.AUTOCOMPLETE_CATEGORY -> {
                    allFragmentList.addAll(searchData.convertCategorySearchToVisitableList(searchTerm).insertTitle(searchData.name))
                    continue@loop
                }
                SearchData.AUTOCOMPLETE_DEFAULT -> {
                    val list = searchData.convertAutoCompleteSearchToVisitableList(searchTerm)
                    allFragmentList.addAll(list)
                    productFragmentList.addAll(list)
                    continue@loop
                }
                SearchData.AUTOCOMPLETE_HOTLIST -> {
                    allFragmentList.addAll(searchData.convertHotlistSearchToVisitableList(searchTerm).insertTitle(searchData.name))
                    continue@loop
                }
                SearchData.AUTOCOMPLETE_IN_CATEGORY -> {
                    val list = searchData.convertInCategorySearchToVisitableList(searchTerm)
                    allFragmentList.addAll(list)
                    productFragmentList.addAll(list)
                    continue@loop
                }
                SearchData.AUTOCOMPLETE_SHOP -> {
                    val list = searchData.convertShopSearchToVisitableList(searchTerm)
                    shopFragmentList.addAll(list)
                    allFragmentList.addAll(list.insertTitle(searchData.name))
                    continue@loop
                }
                SearchData.AUTOCOMPLETE_PROFILE -> {
                    allFragmentList.addAll(searchData.convertProfileSearchToVisitableList(searchTerm).insertTitle(searchData.name))
                    continue@loop
                }
                SearchData.AUTOCOMPLETE_TOP_PROFILE -> {
                    allFragmentList.addAll(searchData.convertTopProfileSearchToVisitableList(searchTerm))
                    continue@loop
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
