package com.tokopedia.entertainment.pdp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.usecase.EventProductDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventPDPFormViewModel@Inject constructor(private val dispatcher: CoroutineDispatcher,
                                               private val usecase: EventProductDetailUseCase) : BaseViewModel(dispatcher) {


    val errorMutable = MutableLiveData<String>()
    val error : LiveData<String>
        get() = errorMutable

    val mFormDataMutable = MutableLiveData<MutableList<Form>>()
    val mFormData : LiveData<MutableList<Form>>
        get() = mFormDataMutable

    fun getData(url: String, rawQueryPDP: String,rawQueryContent: String){
        val formData: MutableList<Form> = mutableListOf()
        launch {
            when(val data = usecase.executeUseCase(rawQueryPDP, rawQueryContent, false, url)){
                is Success -> {
                    data.data.eventProductDetailEntity.eventProductDetail.productDetailData.forms.forEach {
                        formData.add(it)
                    }
                    mFormDataMutable.value = formData
                }

                is Fail -> {
                    errorMutable.value = data.throwable.message
                }
            }
        }
    }
}