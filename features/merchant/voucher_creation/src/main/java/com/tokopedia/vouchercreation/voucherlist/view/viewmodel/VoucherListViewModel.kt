package com.tokopedia.vouchercreation.voucherlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.voucherlist.domain.model.VoucherListParam
import com.tokopedia.vouchercreation.voucherlist.domain.model.VoucherSort
import com.tokopedia.vouchercreation.voucherlist.domain.model.VoucherStatus
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.GetVoucherListUseCase
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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

    val voucherList: LiveData<Result<List<VoucherUiModel>>>
        get() = _voucherList

    private val _keywordLiveData = MutableLiveData<String>()

    private val activeVoucherRequestParam by lazy {
        VoucherListParam.createParam(status = VoucherStatus.ACTIVE)
    }

    private val _voucherList = MediatorLiveData<Result<List<VoucherUiModel>>>().apply {
        addSource(_keywordLiveData) { keyword ->
            searchVoucherByKeyword(keyword)
        }
    }

    fun getActiveVoucherList() {
        launchCatchError(block = {
//            getVoucherListUseCase.isActive = true

            getVoucherListUseCase.params = GetVoucherListUseCase.createRequestParam(activeVoucherRequestParam)
            _voucherList.value = Success(withContext(Dispatchers.IO) {
                getVoucherListUseCase.executeOnBackground()
            })
        }, onError = {
            _voucherList.value = Fail(it)
        })
    }

    fun getVoucherListHistory(@VoucherTypeConst type: Int?,
                              targetList: List<Int>?,
                              @VoucherSort sort: String?,
                              page: Int) {
        launchCatchError(block = {
            getVoucherListUseCase.params = GetVoucherListUseCase.createRequestParam(
                    VoucherListParam.createParam(
                            status = VoucherStatus.HISTORY,
                            type = type,
                            targetList = targetList,
                            sort = sort,
                            page = page)
            )
//            getVoucherListUseCase.isActive = false
            _voucherList.value = Success(withContext(Dispatchers.IO) {
                getVoucherListUseCase.executeOnBackground()
            })
        }, onError = {
            _voucherList.value = Fail(it)
        })
    }

    fun setSearchKeyword(keyword: String) {
        _keywordLiveData.value = keyword
    }

    private fun searchVoucherByKeyword(keyword: String) {
        launchCatchError(block = {
            _voucherList.value = Success(
                    (_voucherList.value as? Success)?.data?.filter {
                        it.name.contains(keyword, true) } ?: listOf())
        }, onError = {
            _voucherList.value = Fail(it)
        })
    }
}