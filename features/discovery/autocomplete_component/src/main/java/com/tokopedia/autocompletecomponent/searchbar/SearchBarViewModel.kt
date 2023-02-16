package com.tokopedia.autocompletecomponent.searchbar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.util.CoachMarkLocalCache
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.model.SearchParameter
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

class SearchBarViewModel @Inject constructor(
    private val coachMarkLocalCache: CoachMarkLocalCache,
    dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.main) {
    private val _searchParameterLiveData: MutableLiveData<SearchParameter> = MutableLiveData()

    val searchParameterLiveData: LiveData<SearchParameter> = _searchParameterLiveData.asFlow()
        .debounce(SEARCH_PARAMETER_DELAY_MS)
        .asLiveData()

    private val activeSearchParameter: SearchParameter
        get() {
            return _searchParameterLiveData.value ?: SearchParameter()
        }

    private val _mpsStateLiveData: MutableLiveData<SearchBarMpsState> = MutableLiveData(
        SearchBarMpsState()
    )
    val mpsStateLiveData: LiveData<SearchBarMpsState>
        get() = _mpsStateLiveData

    private val _searchBarKeywords: MutableLiveData<List<SearchBarKeyword>> = MutableLiveData(
        emptyList()
    )
    val searchBarKeywords: LiveData<List<SearchBarKeyword>>
        get() = _searchBarKeywords

    var activeKeyword: SearchBarKeyword = SearchBarKeyword()
        private set
    private val _activeKeywordLiveData: MutableLiveData<SearchBarKeyword> = MutableLiveData(
        activeKeyword
    )
    val activeKeywordLiveData: LiveData<SearchBarKeyword>
        get() = _activeKeywordLiveData

    var isCoachMarkIconPlusAlreadyDisplayed: Boolean = false
        private set

    var isCoachMarkKeywordAddedAlreadyDisplayed: Boolean = false
        private set

    val isMpsEnabled: Boolean
        get() = _mpsStateLiveData.value?.isMpsEnabled == true

    fun restoreSearchParameter(
        searchParameter: SearchParameter,
        searchBarKeyword: SearchBarKeyword,
    ) {
        val searchBarKeywords = mutableListOf<SearchBarKeyword>()
        if (searchParameter.hasQuery1()) {
            searchBarKeywords.add(
                SearchBarKeyword(
                    keyword = searchParameter.get(SearchApiConst.Q1),
                )
            )
        }
        if (searchParameter.hasQuery2()) {
            searchBarKeywords.add(
                SearchBarKeyword(
                    position = 1,
                    keyword = searchParameter.get(SearchApiConst.Q2),
                )
            )
        }
        if (searchParameter.hasQuery3()) {
            searchBarKeywords.add(
                SearchBarKeyword(
                    position = 2,
                    keyword = searchParameter.get(SearchApiConst.Q3),
                )
            )
        }
        activeKeyword = searchBarKeyword
        _activeKeywordLiveData.postValue(activeKeyword)
        _searchBarKeywords.postValue(searchBarKeywords)
        _searchParameterLiveData.postValue(SearchParameter(searchParameter))
    }

    fun showSearch(searchParameter: SearchParameter) {
        _searchParameterLiveData.postValue(SearchParameter(searchParameter))
    }

    fun onQueryUpdated(query: String) {
        if (query == activeKeyword.keyword) return
        val keyword = activeKeyword.copy(keyword = query)
        val searchParameter = activeSearchParameter
        val keywords = _searchBarKeywords.value ?: emptyList()
        val currentIndex = keywords.indexOfFirst { it.position == keyword.position }
        if (currentIndex != -1) {
            val newList = keywords.filter { it.position != keyword.position }
            val updatedList = if (keyword.keyword.isNotBlank()) (newList + keyword) else newList
            val sortedKeywords = updatedList.sortedBy { it.position }
            searchParameter.setSearchQueries(sortedKeywords.map { it.keyword })
            _searchBarKeywords.postValue(sortedKeywords)
        } else {
            searchParameter.setSearchQuery(keyword.keyword)
        }
        _searchParameterLiveData.postValue(searchParameter)
        activeKeyword = keyword
    }

    fun onKeywordAdded(query: String?) {
        if (!query.isNullOrBlank()) {
            val keyword = activeKeyword
            val currentKeywords = _searchBarKeywords.value ?: emptyList()
            val cleanedQuery = keyword.keyword.trim()
            val hasMaxKeywords = currentKeywords.size > 2
            val hasSameKeyword = currentKeywords.any { cleanedQuery == it.keyword }
            if (hasMaxKeywords || hasSameKeyword) return
            val addedKeyword = if (coachMarkLocalCache.shouldShowAddedKeywordCoachMark()) {
                coachMarkLocalCache.markShowAddedKeywordCoachMark()
                keyword.copy(
                    keyword = cleanedQuery,
                    shouldShowCoachMark = true,
                )
            } else keyword.copy(keyword = cleanedQuery)
            val keywords = currentKeywords + addedKeyword
            val sortedKeywords = keywords.sortWithNewIndex()
            val newKeyword = SearchBarKeyword(
                position = sortedKeywords.size,
            )
            _searchBarKeywords.postValue(sortedKeywords)
            activeKeyword = newKeyword
            _activeKeywordLiveData.postValue(newKeyword)

            val searchParameter = activeSearchParameter
            searchParameter.setSearchQueries(sortedKeywords.map { it.keyword })
            _searchParameterLiveData.postValue(searchParameter)

            updateMpsState()
        }
    }

    fun onKeywordSelected(keyword: SearchBarKeyword) {
        activeKeyword = keyword
        _activeKeywordLiveData.postValue(keyword)
    }

    private fun List<SearchBarKeyword>.sortWithNewIndex(): List<SearchBarKeyword> {
        return mapIndexed { index, searchBarKeyword ->
            searchBarKeyword.copy(position = index)
        }
    }

    fun onKeywordRemoved(keyword: SearchBarKeyword) {
        val keywords = (_searchBarKeywords.value ?: emptyList()) - keyword
        val sortedKeywords = keywords.sortWithNewIndex()
        val query = if (keyword.position == activeKeyword.position) {
            activeKeyword = SearchBarKeyword(position = sortedKeywords.size)
            _activeKeywordLiveData.postValue(activeKeyword)
            activeKeyword.keyword
        } else activeKeyword.keyword
        _searchBarKeywords.postValue(sortedKeywords)

        val searchParameter = activeSearchParameter
        if (sortedKeywords.isNotEmpty()) {
            searchParameter.setSearchQueries(sortedKeywords.map { it.keyword })
        } else {
            searchParameter.setSearchQuery(query)
        }

        _searchParameterLiveData.postValue(searchParameter)
    }

    private fun updateMpsState() {
        val currentMpsState = _mpsStateLiveData.value ?: SearchBarMpsState()
        if (!currentMpsState.isMpsEnabled) return
        val shouldShowMpsCoachMark = coachMarkLocalCache.shouldShowPlusIconCoachMark()
        val isMpsAnimationEnabled = coachMarkLocalCache.shouldShowAddedKeywordCoachMark()
        val mpsState = currentMpsState.copy(
            isMpsAnimationEnabled = isMpsAnimationEnabled,
            shouldShowCoachMark = shouldShowMpsCoachMark,
        )
        _mpsStateLiveData.postValue(mpsState)
    }

    fun enableMps() {
        val shouldShowMpsCoachMark = coachMarkLocalCache.shouldShowPlusIconCoachMark()
        val isMpsAnimationEnabled = coachMarkLocalCache.shouldShowAddedKeywordCoachMark()
        val mpsState = SearchBarMpsState(
            isMpsEnabled = true,
            isMpsAnimationEnabled = isMpsAnimationEnabled,
            shouldShowCoachMark = shouldShowMpsCoachMark,
        )
        _mpsStateLiveData.postValue(mpsState)
    }

    fun disableMps() {
        _mpsStateLiveData.postValue(SearchBarMpsState())
    }

    fun onInitialStateItemSelected(item: BaseItemInitialStateSearch) {
        val currentKeywords = _searchBarKeywords.value ?: emptyList()
        val hasMaxKeywords = currentKeywords.size > 2
        val hasSameKeyword = currentKeywords.any { item.title == it.keyword }
        if (hasMaxKeywords || hasSameKeyword) return

        val searchParameter = activeSearchParameter
        val newKeyword = SearchBarKeyword(
            position = currentKeywords.size,
            keyword = item.title,
        )
        val newKeywords = currentKeywords + newKeyword
        activeKeyword = activeKeyword.copy(position = newKeywords.size)
        searchParameter.setSearchQueries(newKeywords.map { it.keyword })
        _searchParameterLiveData.postValue(searchParameter)
        _searchBarKeywords.postValue(newKeywords)
        _activeKeywordLiveData.postValue(activeKeyword)
    }

    fun markCoachMarkIconPlusAlreadyDisplayed() {
        this.isCoachMarkIconPlusAlreadyDisplayed = true
        coachMarkLocalCache.markShowPlusIconCoachMark()
    }

    fun markCoachMarkKeywordAddedAlreadyDisplayed() {
        this.isCoachMarkKeywordAddedAlreadyDisplayed = true
    }

    fun getSubmitSearchParameter(): SearchParameter {
        val searchParameter = SearchParameter(activeSearchParameter)
        val keywords = _searchBarKeywords.value ?: emptyList()
        if (keywords.size == 1) {
            searchParameter.setSearchQuery(keywords.first().keyword)
        }
        return searchParameter
    }

    companion object {
        private const val SEARCH_PARAMETER_DELAY_MS: Long = 200
    }
}
