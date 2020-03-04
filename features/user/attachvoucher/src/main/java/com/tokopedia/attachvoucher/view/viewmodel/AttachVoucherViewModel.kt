package com.tokopedia.attachvoucher.view.viewmodel

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.attachcommon.data.VoucherPreview
import com.tokopedia.attachvoucher.data.GetVoucherResponse
import com.tokopedia.attachvoucher.data.Voucher
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import javax.inject.Inject

class AttachVoucherViewModel @Inject constructor(
        private val getVouchersUseCase: GetVoucherUseCase
) : ViewModel() {

    private var _filter: MutableLiveData<Int> = MutableLiveData()
    private var _vouchers: MutableLiveData<List<Voucher>> = MutableLiveData()
    private var _error: MutableLiveData<Throwable> = MutableLiveData()

    var shopId: String = ""
    val filter: LiveData<Int> get() = _filter
    val error: LiveData<Throwable> get() = _error
    var filteredVouchers: LiveData<List<Voucher>> = Transformations.map(_filter) {
        _vouchers.value?.filter { voucher ->
            (_filter.value == NO_FILTER || _filter.value == voucher.type)
        } ?: emptyList()
    }

    fun initializeArguments(arguments: Bundle?) {
        if (arguments == null) return
        shopId = arguments.getString(ApplinkConst.AttachVoucher.PARAM_SHOP_ID) ?: ""
    }

    fun toggleFilter(filterType: Int) {
        val currentFilter = _filter.value
        if (currentFilter == filterType) {
            _filter.value = NO_FILTER
        } else {
            _filter.value = filterType
        }
    }

    fun loadVouchers() {
        if (shopId.isEmpty()) return
        getVouchersUseCase.getVouchers(
                ::onSuccessGetVouchers,
                ::onErrorGetVouchers,
                shopId.toInt()
        )
    }

    fun getVoucherPreviewIntent(voucher: Voucher): Intent {
        val voucherPreview = VoucherPreview(
                "inbox",
                voucher.voucherId,
                voucher.tnc ?: "",
                voucher.voucherCode ?: "",
                voucher.voucherName ?: "",
                voucher.minimumSpend,
                voucher.validThru.toLong(),
                voucher.merchantVoucherBanner?.desktopUrl ?: "",
                voucher.merchantVoucherBanner?.mobileUrl ?: "",
                voucher.availableAmount.toIntOrZero(),
                voucher.amountType ?: -1,
                voucher.identifier,
                voucher.type ?: -1
        )
        val stringVoucherPreview = CommonUtil.toJson(voucherPreview)
        return Intent().apply {
            putExtra(ApplinkConst.AttachVoucher.PARAM_VOUCHER_PREVIEW, stringVoucherPreview)
        }
    }

    private fun onSuccessGetVouchers(getVoucherResponse: GetVoucherResponse) {
        _vouchers.value = getVoucherResponse.vouchers
        _filter.value = NO_FILTER
    }

    private fun onErrorGetVouchers(throwable: Throwable) {
        _error.value = throwable
    }

    companion object {
        const val NO_FILTER = -1
    }

}