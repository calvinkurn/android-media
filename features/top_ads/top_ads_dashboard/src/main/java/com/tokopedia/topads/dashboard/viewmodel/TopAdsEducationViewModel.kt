package com.tokopedia.topads.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.dashboard.data.model.ListArticle
import com.tokopedia.topads.dashboard.data.raw.articlesJson
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject

class TopAdsEducationViewModel @Inject constructor() : BaseViewModel(Dispatchers.Main) {

    private val _articlesLiveData = MutableLiveData<Result<ListArticle>>()
    val articlesLiveData: LiveData<Result<ListArticle>> = _articlesLiveData

    fun fetchArticles() {
        launchCatchError(block =  {
            val data = Gson().fromJson(articlesJson, ListArticle::class.java)
            _articlesLiveData.value = Success(data)
        }, onError = {
            _articlesLiveData.value = Fail(it)
            Timber.d(it)
        })
    }
}