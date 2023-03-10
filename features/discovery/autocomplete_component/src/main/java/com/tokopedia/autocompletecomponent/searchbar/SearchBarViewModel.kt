package com.tokopedia.autocompletecomponent.searchbar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.util.CoachMarkLocalCache
import com.tokopedia.autocompletecomponent.util.clearSearchQuery
import com.tokopedia.autocompletecomponent.util.hasQuery
import com.tokopedia.autocompletecomponent.util.hasQuery1
import com.tokopedia.autocompletecomponent.util.hasQuery2
import com.tokopedia.autocompletecomponent.util.hasQuery3
import com.tokopedia.autocompletecomponent.util.isMps
import com.tokopedia.autocompletecomponent.util.setSearchQueries
import com.tokopedia.autocompletecomponent.util.setSearchQuery
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.view.orZero
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

class SearchBarViewModel @Inject constructor(
    private val coachMarkLocalCache: CoachMarkLocalCache,
    dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.main) {
    private val _searchParameterLiveData: MutableLiveData<Map<String, String>> = MutableLiveData()

    val searchParameterLiveData: LiveData<Map<String, String>> = _searchParameterLiveData.asFlow()
        .debounce(SEARCH_PARAMETER_DELAY_MS)
        .asLiveData()

    private val activeSearchParameter: Map<String, String>
        get() = _searchParameterLiveData.value ?: mapOf()

    private val _searchBarStateLiveData: MutableLiveData<SearchBarState> = MutableLiveData(
        SearchBarState()
    )
    val searchBarStateLiveData: LiveData<SearchBarState>
        get() = _searchBarStateLiveData
    private val currentSearchBarState: SearchBarState
        get() = _searchBarStateLiveData.value ?: SearchBarState()

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
        get() = _searchBarStateLiveData.value?.isMpsEnabled == true

    fun restoreSearchParameter(
        searchParameter: Map<String, String>,
        searchBarKeyword: SearchBarKeyword,
    ) {
        val searchBarKeywords = mutableListOf<SearchBarKeyword>()
        if (searchParameter.hasQuery1()) {
            searchBarKeywords.add(
                SearchBarKeyword(
                    keyword = searchParameter.get(SearchApiConst.Q1).orEmpty(),
                )
            )
        }
        if (searchParameter.hasQuery2()) {
            searchBarKeywords.add(
                SearchBarKeyword(
                    position = 1,
                    keyword = searchParameter.get(SearchApiConst.Q2).orEmpty(),
                )
            )
        }
        if (searchParameter.hasQuery3()) {
            searchBarKeywords.add(
                SearchBarKeyword(
                    position = 2,
                    keyword = searchParameter.get(SearchApiConst.Q3).orEmpty(),
                )
            )
        }
        activeKeyword = searchBarKeyword
        _activeKeywordLiveData.value = activeKeyword
        _searchBarKeywords.value = searchBarKeywords
        _searchParameterLiveData.value = HashMap(searchParameter)
    }

    fun showSearch(searchParameter: Map<String, String>) {
        setHintIfExists(searchParameter)
        _searchParameterLiveData.value = HashMap(searchParameter)
        if (searchParameter.hasQuery()) {
            onQueryUpdated(searchParameter[SearchApiConst.Q].orEmpty())
            _activeKeywordLiveData.value = activeKeyword
        } else if (searchParameter.isMps()) {
            val keywordList = mutableListOf<SearchBarKeyword>()
            if (searchParameter.hasQuery1()) {
                keywordList.add(
                    SearchBarKeyword(keyword = searchParameter[SearchApiConst.Q1].orEmpty())
                )
            }
            if (searchParameter.hasQuery2()) {
                keywordList.add(
                    SearchBarKeyword(keywordList.size, searchParameter[SearchApiConst.Q2].orEmpty())
                )
            }
            if (searchParameter.hasQuery3()) {
                keywordList.add(
                    SearchBarKeyword(keywordList.size, searchParameter[SearchApiConst.Q3].orEmpty())
                )
            }
            _searchBarKeywords.value = keywordList.toList()
            activeKeyword = SearchBarKeyword(keywordList.size)
            _activeKeywordLiveData.value = activeKeyword
        }
    }

    private fun setHintIfExists(searchParameter: Map<String, String>) {
        val hint = searchParameter[SearchApiConst.HINT].orEmpty()
        val placeholder = searchParameter[SearchApiConst.PLACEHOLDER].orEmpty()
        val hintText = when {
            hint.isNotBlank() -> hint
            placeholder.isNotBlank() -> placeholder
            else -> ""
        }
        val currentState = _searchBarStateLiveData.value ?: SearchBarState()
        _searchBarStateLiveData.value = currentState.copy(
            hasHintOrPlaceHolder = hintText.isNotBlank(),
            hintText = hintText,
        )
    }

    fun onQueryUpdated(query: String) {
        if (query == activeKeyword.keyword) return
        val keyword = activeKeyword.copy(keyword = query)
        val searchParameter = activeSearchParameter
        val keywords = _searchBarKeywords.value ?: emptyList()
        val currentIndex = keywords.indexOfFirst { it.position == keyword.position }
        val newSearchParameter = when {
            currentIndex != -1 -> {
                val newList = keywords.filter { it.position != keyword.position }
                val updatedList = if (keyword.keyword.isNotBlank()) (newList + keyword) else newList
                val sortedKeywords = updatedList.sortedBy { it.position }
                _searchBarKeywords.value = sortedKeywords
                searchParameter.setSearchQueries(sortedKeywords.map { it.keyword })
            }
            keywords.isEmpty() && keyword.keyword.isBlank() -> {
                searchParameter.clearSearchQuery()
            }
            keywords.isEmpty() && keyword.keyword.isNotBlank() -> {
                searchParameter.setSearchQuery(keyword.keyword)
            }
            else -> searchParameter
        }
        _searchParameterLiveData.value = newSearchParameter
        activeKeyword = keyword
    }

    fun onKeywordAdded(query: String?) {
        if (!query.isNullOrBlank()) {
            val currentKeywords = _searchBarKeywords.value ?: emptyList()
            val cleanedQuery = activeKeyword.keyword.trim()
            val hasMaxKeywords = currentKeywords.size > 2
            val hasSameKeyword = currentKeywords.any { cleanedQuery == it.keyword }
            if (hasMaxKeywords || hasSameKeyword) return
            val addedKeyword = if (coachMarkLocalCache.shouldShowAddedKeywordCoachMark()) {
                coachMarkLocalCache.markShowAddedKeywordCoachMark()
                activeKeyword.copy(
                    keyword = cleanedQuery,
                    shouldShowCoachMark = true,
                )
            } else activeKeyword.copy(keyword = cleanedQuery)
            val keywords = currentKeywords + addedKeyword
            val sortedKeywords = keywords.sortWithNewIndex()
            val newKeyword = SearchBarKeyword(
                position = sortedKeywords.size,
            )
            _searchBarKeywords.value = sortedKeywords
            activeKeyword = newKeyword
            _activeKeywordLiveData.value = newKeyword

            _searchParameterLiveData.value = activeSearchParameter.setSearchQueries(
                sortedKeywords.map { it.keyword }
            )

            updateSearchBarState()
        }
    }

    fun onKeywordSelected(keyword: SearchBarKeyword) {
        if (!keyword.isSelected) {
            activeKeyword = keyword.copy(
                isSelected = true
            )
            _activeKeywordLiveData.value = activeKeyword
            _searchBarKeywords.value = _searchBarKeywords.value.orEmpty().map {
                if (it.position == keyword.position) activeKeyword else it.copy(isSelected = false)
            }
        } else {
            activeKeyword = SearchBarKeyword(position = _searchBarKeywords.value.orEmpty().size)
            _activeKeywordLiveData.value = activeKeyword
            _searchBarKeywords.value = _searchBarKeywords.value.orEmpty().map {
                it.copy(isSelected = false)
            }
        }
        _searchParameterLiveData.value = activeSearchParameter
        updateSearchBarState()
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
            _activeKeywordLiveData.value = activeKeyword
            activeKeyword.keyword
        } else activeKeyword.keyword
        _searchBarKeywords.value = sortedKeywords

        val searchParameter = if (sortedKeywords.isNotEmpty()) {
            activeSearchParameter.setSearchQueries(sortedKeywords.map { it.keyword })
        } else if (query.isNotBlank()) {
            activeSearchParameter.setSearchQuery(query)
        } else {
            activeSearchParameter.clearSearchQuery()
        }

        _searchParameterLiveData.value = searchParameter
        updateSearchBarState()
    }

    private fun updateSearchBarState() {
        val currentState = currentSearchBarState
        if (!currentState.isMpsEnabled) return
        val searchBarKeywordSize = _searchBarKeywords.value?.size.orZero()
        val isActiveKeywordNotInKeywordList = _searchBarKeywords.value.orEmpty()
            .none { it.position == activeKeyword.position }
        val shouldEnableAddButton = searchBarKeywordSize < 3 && isActiveKeywordNotInKeywordList
        val allowKeyboardDismiss = searchBarKeywordSize == 0
        val shouldDisplayMpsPlaceHolder = searchBarKeywordSize != 0
        val shouldShowMpsCoachMark = coachMarkLocalCache.shouldShowPlusIconCoachMark()
        val isMpsAnimationEnabled = coachMarkLocalCache.shouldShowAddedKeywordCoachMark()
        val mpsState = currentState.copy(
            isMpsAnimationEnabled = isMpsAnimationEnabled,
            shouldShowCoachMark = shouldShowMpsCoachMark,
            isAddButtonEnabled = shouldEnableAddButton,
            isKeyboardDismissEnabled = allowKeyboardDismiss,
            shouldDisplayMpsPlaceHolder = shouldDisplayMpsPlaceHolder,
        )
        _searchBarStateLiveData.value = mpsState
    }

    fun enableMps() {
        val shouldShowMpsCoachMark = coachMarkLocalCache.shouldShowPlusIconCoachMark()
        val isMpsAnimationEnabled = coachMarkLocalCache.shouldShowAddedKeywordCoachMark()
        val searchBarKeywordSize = _searchBarKeywords.value?.size.orZero()
        val shouldEnableAddButton = searchBarKeywordSize < 3
        val allowKeyboardDismiss = searchBarKeywordSize == 0
        val newState = currentSearchBarState.copy(
            isMpsEnabled = true,
            isMpsAnimationEnabled = isMpsAnimationEnabled,
            shouldShowCoachMark = shouldShowMpsCoachMark,
            isAddButtonEnabled = shouldEnableAddButton,
            isKeyboardDismissEnabled = allowKeyboardDismiss,
        )
        _searchBarStateLiveData.value = newState
    }

    fun disableMps() {
        _searchBarStateLiveData.value = currentSearchBarState.copy(
            isMpsEnabled = false,
            isMpsAnimationEnabled = false,
            shouldShowCoachMark = false,
            isAddButtonEnabled = false,
            isKeyboardDismissEnabled = true,
            shouldDisplayMpsPlaceHolder = false,
        )
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
        _activeKeywordLiveData.value = activeKeyword
        _searchParameterLiveData.value =
            searchParameter.setSearchQueries(newKeywords.map { it.keyword })
        _searchBarKeywords.value = newKeywords
    }

    fun markCoachMarkIconPlusAlreadyDisplayed() {
        this.isCoachMarkIconPlusAlreadyDisplayed = true
        coachMarkLocalCache.markShowPlusIconCoachMark()
    }

    fun markCoachMarkKeywordAddedAlreadyDisplayed() {
        this.isCoachMarkKeywordAddedAlreadyDisplayed = true
    }

    fun getSubmitSearchParameter(): Map<String, String> {
        val searchParameter = HashMap(activeSearchParameter)
        val currentKeywords = _searchBarKeywords.value ?: emptyList()
        val isActiveKeywordNotBlank = activeKeyword.keyword.isNotBlank()
        val isActiveKeywordNotInKeywordList = activeKeyword !in currentKeywords
        val isKeywordListNotFull = currentKeywords.size < 3
        val isActiveKeywordNeedToBeAdded = isActiveKeywordNotBlank
            && isActiveKeywordNotInKeywordList
            && isKeywordListNotFull
        val keywords = if (isActiveKeywordNeedToBeAdded) {
            currentKeywords + activeKeyword
        } else {
            currentKeywords
        }
        return if (keywords.size == 1) {
            searchParameter.setSearchQuery(keywords.first().keyword)
        } else {
            searchParameter.setSearchQueries(keywords.map { it.keyword })
        }
    }

    companion object {
        private const val SEARCH_PARAMETER_DELAY_MS: Long = 200
    }
}
