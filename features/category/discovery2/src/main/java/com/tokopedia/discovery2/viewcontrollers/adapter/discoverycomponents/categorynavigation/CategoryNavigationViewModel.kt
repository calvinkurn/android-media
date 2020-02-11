package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorynavigation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.usecase.CategoryNavigationUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class CategoryNavigationViewModel(val application: Application, private val components: ComponentsItem) : DiscoveryBaseViewModel(), CoroutineScope {

    private val listData = MutableLiveData<Result<ArrayList<ComponentsItem>>>()
    private val title = MutableLiveData<Result<String>>()
    private val imageUrl = MutableLiveData<Result<String>>()

    @Inject
    lateinit var categoryNavigationUseCase: CategoryNavigationUseCase


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        initDaggerInject()
    }

    fun getCategoryNavigationData() {
        launchCatchError(
                block = {
                    withContext(Dispatchers.IO) {
                        val dataList = categoryNavigationUseCase.
                                getCategoryNavigationData(components.data?.getOrElse(0) { DataItem() }?.categoryDetailUrl
                                ?: "")
                        listData.postValue(Success(dataList))
                    }
                },
                onError = {
                    listData.value = Fail(it)
                }
        )
    }

    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((application.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    fun getListData():LiveData<Result<ArrayList<ComponentsItem>>> = listData

    fun getTitle(): LiveData<Result<String>> {
        title.postValue(Success(components.title ?: ""))
        return title
    }

    fun getImageUrl(): LiveData<Result<String>> {
        imageUrl.postValue(Success(components.data?.get(0)?.backgroundImageApps ?: ""))
        return imageUrl
    }


}