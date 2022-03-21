package com.tokopedia.shop_widget.mvc_locked_to_product.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.usecase.MvcLockedToProductGetSortListUseCase
import com.tokopedia.shop_widget.mvc_locked_to_product.util.MvcLockedToProductBottomSheetMapper
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductSortUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class MvcLockedToProductSortListBottomSheetViewModel @Inject constructor(
    private val mvcLockedToProductGetSortListUseCase: MvcLockedToProductGetSortListUseCase,
    private val dispatcherProvider: CoroutineDispatchers
) : BaseViewModel(dispatcherProvider.main) {

    val sortListLiveData: LiveData<Result<List<MvcLockedToProductSortUiModel>>>
        get() = _sortListLiveData
    private val _sortListLiveData = MutableLiveData<Result<List<MvcLockedToProductSortUiModel>>>()

    fun getSortListData(selectedSortData: MvcLockedToProductSortUiModel) {
        launchCatchError(dispatcherProvider.io, block = {
            mvcLockedToProductGetSortListUseCase.setParams()
            val response = mvcLockedToProductGetSortListUseCase.executeOnBackground()
            val uiModel = MvcLockedToProductBottomSheetMapper.mapToSortListUiModel(
                response,
                selectedSortData
            )
            _sortListLiveData.postValue(Success(uiModel))
        }) {
            _sortListLiveData.postValue(Fail(it))
        }
    }

}