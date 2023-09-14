package com.tokopedia.autocompletecomponent.searchbar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
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
import com.tokopedia.discovery.common.utils.MpsLocalCache
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

class SearchBarViewModel @Inject constructor(
    private val coachMarkLocalCache: CoachMarkLocalCache,
    private val mpsLocalCache: MpsLocalCache,
    dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.main) {
    private val _searchParameterLiveData: MutableLiveData<Map<String, String>> = MutableLiveData(
        mapOf()
    )

    val searchParameterLiveData: LiveData<Map<String, String>>
        get() = _searchParameterLiveData

    val searchParameterLiveDataWithDelay: LiveData<Map<String, String>> = _searchParameterLiveData.asFlow()
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

    private val _searchBarKeywordErrorEvent : SingleLiveEvent<SearchBarKeywordError> = SingleLiveEvent()
    val searchBarKeywordErrorEvent: LiveData<SearchBarKeywordError>
        get() = _searchBarKeywordErrorEvent

    fun restoreSearchParameter(
        searchParameter: Map<String, String>,
        searchBarKeyword: SearchBarKeyword,
    ) {
        val searchBarKeywords = mutableListOf<SearchBarKeyword>()
        if (searchParameter.hasQuery1()) {
            searchBarKeywords.add(
                SearchBarKeyword(
                    keyword = searchParameter[SearchApiConst.Q1].orEmpty(),
                )
            )
        }
        if (searchParameter.hasQuery2()) {
            searchBarKeywords.add(
                SearchBarKeyword(
                    position = 1,
                    keyword = searchParameter[SearchApiConst.Q2].orEmpty(),
                )
            )
        }
        if (searchParameter.hasQuery3()) {
            searchBarKeywords.add(
                SearchBarKeyword(
                    position = 2,
                    keyword = searchParameter[SearchApiConst.Q3].orEmpty(),
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
            updateSearchBarState()
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
        val keyword = activeKeyword.copy(
            keyword = query,
            isSelected = activeKeyword.isSelected && query.isNotEmpty(),
        )
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
            val cleanedQuery = activeKeyword.keyword.trim()
            updateSearchQuery(cleanedQuery)
        } else {
            _searchBarKeywordErrorEvent.value = SearchBarKeywordError.Empty
        }
    }

    fun onKeywordAdd(selectedSuggestion: BaseSuggestionDataView) {
        updateSearchQuery(selectedSuggestion.title)
    }

    private fun updateSearchQuery(query: String?){
        if (!query.isNullOrBlank()) {
            val currentKeywords = _searchBarKeywords.value ?: emptyList()
            val lowerCaseQuery = query.lowercase()
            val hasMaxKeywords = currentKeywords.size > 2
            val hasSameKeyword = currentKeywords.any { lowerCaseQuery == it.keyword.lowercase() }
            if (hasSameKeyword) _searchBarKeywordErrorEvent.value = SearchBarKeywordError.Duplicate
            if (hasMaxKeywords || hasSameKeyword) return
            val addedKeyword = if (coachMarkLocalCache.shouldShowAddedKeywordCoachMark()) {
                coachMarkLocalCache.markShowAddedKeywordCoachMark()
                activeKeyword.copy(
                    keyword = query,
                    shouldShowCoachMark = true,
                )
            } else activeKeyword.copy(keyword = query)
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
        } else {
            _searchBarKeywordErrorEvent.value = SearchBarKeywordError.Empty
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
            if (hasDuplicateKeyword(activeKeyword.keyword)) {
                _searchBarKeywordErrorEvent.value = SearchBarKeywordError.Duplicate
                return
            }
            
            activeKeyword = SearchBarKeyword(position = _searchBarKeywords.value.orEmpty().size)
            _activeKeywordLiveData.value = activeKeyword
            _searchBarKeywords.value = _searchBarKeywords.value.orEmpty().map {
                it.copy(isSelected = false)
            }
        }
        _searchParameterLiveData.value = activeSearchParameter
        updateSearchBarState()
    }

    private fun hasDuplicateKeyword(query: String) : Boolean {
        val cleanedQuery = query.trim().lowercase()
        val currentKeywords = _searchBarKeywords.value ?: emptyList()
        val otherKeywords = currentKeywords - activeKeyword
        return otherKeywords.any { cleanedQuery == it.keyword.lowercase() }
    }

    fun onApplySuggestionToSelectedKeyword(
        suggestionText: String,
        keyword: SearchBarKeyword,
    ) {
        if(!keyword.isSelected) return
        if (hasDuplicateKeyword(suggestionText)) {
            _searchBarKeywordErrorEvent.value = SearchBarKeywordError.Duplicate
            return
        }

        activeKeyword = SearchBarKeyword(position = _searchBarKeywords.value.orEmpty().size)
        _activeKeywordLiveData.value = activeKeyword
        _searchBarKeywords.value = _searchBarKeywords.value.orEmpty().map {
            val keywordText = if (it.hasSameKeywordAndPosition(keyword)) suggestionText else it.keyword
            it.copy(
                keyword = keywordText,
                isSelected = false,
            )
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
        activeKeyword = if (keyword.position == activeKeyword.position) {
            SearchBarKeyword(position = sortedKeywords.size)
        } else {
            activeKeyword.copy(position = sortedKeywords.size)
        }
        _activeKeywordLiveData.value = activeKeyword
        val query = activeKeyword.keyword
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
        val searchBarKeywordSize = _searchBarKeywords.value?.size.orZero()
        val isActiveKeywordNotInKeywordList = _searchBarKeywords.value.orEmpty()
            .none { it.position == activeKeyword.position }
        val shouldEnableAddButton = searchBarKeywordSize < 3 && isActiveKeywordNotInKeywordList
        val allowKeyboardDismiss = searchBarKeywordSize == 0
        val shouldDisplayMpsPlaceHolder = searchBarKeywordSize != 0
        val shouldShowMpsCoachMark = coachMarkLocalCache.shouldShowPlusIconCoachMark()
        val mpsState = currentState.copy(
            isMpsAnimationEnabled = false,
            shouldShowCoachMark = shouldShowMpsCoachMark,
            isAddButtonEnabled = shouldEnableAddButton,
            isKeyboardDismissEnabled = allowKeyboardDismiss,
            shouldDisplayMpsPlaceHolder = shouldDisplayMpsPlaceHolder,
        )
        _searchBarStateLiveData.value = mpsState
    }

    fun showMps() {
        val shouldShowMpsCoachMark = coachMarkLocalCache.shouldShowPlusIconCoachMark()
        val searchBarKeywordSize = _searchBarKeywords.value?.size.orZero()
        val shouldEnableAddButton = searchBarKeywordSize < 3
        val isMpsAnimationEnabled = mpsLocalCache.shouldAnimatePlusIcon() && shouldEnableAddButton
        val allowKeyboardDismiss = searchBarKeywordSize == 0
        val newState = currentSearchBarState.copy(
            isMpsAnimationEnabled = isMpsAnimationEnabled,
            shouldShowCoachMark = shouldShowMpsCoachMark,
            isAddButtonEnabled = shouldEnableAddButton,
            isKeyboardDismissEnabled = allowKeyboardDismiss,
        )
        _searchBarStateLiveData.value = newState
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
        updateSearchBarState()
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
        val cleanActiveKeywordValue = activeKeyword.keyword.trim()
        val isActiveKeywordNotInKeywordList = currentKeywords.none { it.keyword == cleanActiveKeywordValue }
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

    fun isMps(): Boolean {
        val currentKeywords = _searchBarKeywords.value ?: emptyList()
        return currentKeywords.isNotEmpty()
    }

    companion object {
        private const val SEARCH_PARAMETER_DELAY_MS: Long = 200
    }
}
