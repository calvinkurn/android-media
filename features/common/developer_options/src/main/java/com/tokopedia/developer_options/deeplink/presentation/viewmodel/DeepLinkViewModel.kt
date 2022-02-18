package com.tokopedia.developer_options.deeplink.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.developer_options.deeplink.domain.usecase.GetDeepLinkListUseCase
import com.tokopedia.developer_options.deeplink.presentation.uimodel.DeepLinkUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success


class DeepLinkViewModel(
    coroutineDispatchers: CoroutineDispatchers,
    private val getDeepLinkListUseCase: GetDeepLinkListUseCase
) : BaseViewModel(coroutineDispatchers.main) {

    private val _deepLinkItemList = MutableLiveData<Result<List<DeepLinkUiModel>>>()
    val deepLinkItemList: LiveData<Result<List<DeepLinkUiModel>>>
        get() = _deepLinkItemList

    fun getDeepLinkItemList() {
        launchCatchError(block = {
            _deepLinkItemList.value = Success(getDeepLinkListUseCase.execute())
        }, onError = {
            _deepLinkItemList.value = Fail(it)
        })
    }
}