package com.tokopedia.accountprofile.changephonenumber.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.accountprofile.changephonenumber.data.GetWarningDataModel
import com.tokopedia.accountprofile.changephonenumber.usecase.GetWarningUseCase
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import javax.inject.Inject

class ChangePhoneNumberViewModel @Inject constructor(
    private val getWarningUseCase: GetWarningUseCase,
    private val coroutineDispatchers: CoroutineDispatchers
) : BaseViewModel(coroutineDispatchers.io) {
    
    private val _getWarning = MutableLiveData<Result<GetWarningDataModel>>()
    val getWarning: LiveData<Result<GetWarningDataModel>>
        get() = _getWarning

    fun getWarning() {
        launchCatchError(coroutineContext, {
            val response = getWarningUseCase.executeOnBackground().getData()

            withContext(coroutineDispatchers.main) {
                _getWarning.postValue(Success(response))
            }
        }, {
            _getWarning.postValue(Fail(it))
        })
    }

    private fun Map<Type, RestResponse?>.getData(): GetWarningDataModel {
        return this[GetWarningDataModel::class.java]?.getData() as GetWarningDataModel
    }
}
