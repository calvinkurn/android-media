package com.tokopedia.feedplus.browse.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.feedplus.browse.presentation.model.FeedSearchResultUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class FeedSearchResultViewModel @Inject constructor(

): ViewModel() {
    private val _resultUiState = MutableLiveData<FeedSearchResultUiState>()
    val resultState: LiveData<FeedSearchResultUiState> get() = _resultUiState

    private val _resultData = MutableLiveData<SearchTempDataModel>()
    val resultData: LiveData<SearchTempDataModel> get() = _resultData

    private val _keyword = MutableLiveData<String>()
    val keyword: LiveData<String> get() = _keyword

    fun setKeyword(keyword: String) {
        _keyword.value = keyword
    }

    // Todo: will be replace to real fetcher
    private var fetchCounter = 0
    fun fetchData() {
        viewModelScope.launch {
            delay(4000)

            val stateRandom = Random.nextInt(0, 4)
            val uiState = when (stateRandom) {
                0 -> {FeedSearchResultUiState.InternalError}
                1 -> {FeedSearchResultUiState.NoConnection}
                2 -> {FeedSearchResultUiState.NotFound}
                else -> {FeedSearchResultUiState.Restricted}
            }

            updateResultState(uiState)
        }
    }

    fun getDataResult() {
        val data = mutableListOf<SearchTempDataModel.CardDetail>()
        for (i in 0..15) {
            data.add(
                SearchTempDataModel.CardDetail(
                    "Title - ${keyword.value} $i",
                    "https://picsum.photos/${768 + i}/${432 + i}"
                )
            )
        }

        _resultData.value = SearchTempDataModel(data, FeedSearchResultUiState.Success)
    }

    private fun updateResultState(state: FeedSearchResultUiState) {
        _resultUiState.value = state
    }
}

// Temp data model
data class SearchTempDataModel (
    val resultList: List<CardDetail>?,
    val uiState: FeedSearchResultUiState
) {
    data class CardDetail(
        val title: String,
        val imgUrl: String
    )
}
