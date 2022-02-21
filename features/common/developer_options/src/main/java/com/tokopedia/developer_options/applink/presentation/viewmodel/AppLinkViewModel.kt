package com.tokopedia.developer_options.applink.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.developer_options.applink.domain.usecase.GetAppLinkListUseCase
import com.tokopedia.developer_options.applink.presentation.uimodel.AppLinkUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success


class AppLinkViewModel(
    coroutineDispatchers: CoroutineDispatchers,
    private val getAppLinkListUseCase: GetAppLinkListUseCase
) : BaseViewModel(coroutineDispatchers.main) {

    private val _appLinkItemList = MutableLiveData<Result<List<AppLinkUiModel>>>()
    val appLinkItemList: LiveData<Result<List<AppLinkUiModel>>>
        get() = _appLinkItemList

    fun getAppLinkItemList() {
        launchCatchError(block = {
            _appLinkItemList.value = Success(getAppLinkListUseCase.execute())
        }, onError = {
            _appLinkItemList.value = Fail(it)
        })
    }
}