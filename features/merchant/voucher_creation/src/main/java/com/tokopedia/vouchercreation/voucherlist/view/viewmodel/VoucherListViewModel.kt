package com.tokopedia.vouchercreation.voucherlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.voucherlist.domain.GetVoucherListUseCase
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListViewModel @Inject constructor(
        private val getVoucherListUseCase: GetVoucherListUseCase,
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _voucherList: MutableLiveData<Result<List<VoucherUiModel>>> = MutableLiveData()
    val voucherList: LiveData<Result<List<VoucherUiModel>>>
        get() = _voucherList

    fun getVoucherList() {
        launchCatchError(block = {
            delay(3000L)
            _voucherList.value = Success(withContext(Dispatchers.IO) {
                getVoucherListUseCase.executeOnBackground()
            })
        }, onError = {
            _voucherList.value = Fail(it)
        })
    }
}