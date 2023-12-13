package com.tokopedia.tokopedianow.annotation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.annotation.domain.mapper.AllAnnotationMapper.mapToAnnotationUiModel
import com.tokopedia.tokopedianow.annotation.domain.usecase.GetAllAnnotationPageUseCase
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.AnnotationUiModel
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper.mapToWarehouseIds
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoNowAllAnnotationViewModel @Inject constructor(
    private val getAllAnnotationPageUseCase: GetAllAnnotationPageUseCase,
    private val addressData: TokoNowLocalAddress,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _headerTitle = MutableLiveData<Result<String>>()
    private val _annotationList = MutableLiveData<Result<List<AnnotationUiModel>>>()

    val headerTitle: LiveData<Result<String>>
        get() = _headerTitle
    val annotationList: LiveData<Result<List<AnnotationUiModel>>>
        get() = _annotationList

    fun getFirstPage(
        categoryId: String,
        annotationType: String
    ) {
        launchCatchError(block = {
            val warehouseIds = mapToWarehouseIds(addressData.getAddressData())
            val response = getAllAnnotationPageUseCase.execute(
                categoryId = categoryId,
                warehouseIds = warehouseIds,
                annotationType = annotationType,
            )
            _headerTitle.postValue(Success(response.annotationHeader.title))
            _annotationList.postValue(Success(response.mapToAnnotationUiModel()))
        }) {
            _headerTitle.postValue(Fail(it))
            _annotationList.postValue(Fail(it))
        }
    }
}
