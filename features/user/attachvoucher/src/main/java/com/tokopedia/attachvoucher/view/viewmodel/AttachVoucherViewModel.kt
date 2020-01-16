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
    var shopId: String = ""

    var filter: MutableLiveData<Int> = MutableLiveData()
    var vouchers: MutableLiveData<List<Voucher>> = MutableLiveData()
    var error: MutableLiveData<Throwable> = MutableLiveData()
    var filteredVouchers: LiveData<List<Voucher>> = Transformations.map(filter) {
        vouchers.value?.filter { voucher ->
            (filter.value == NO_FILTER || filter.value == voucher.type)
        } ?: emptyList()
    }

    fun initializeArguments(arguments: Bundle?) {
        if (arguments == null) return
        shopId = arguments.getString(ApplinkConst.AttachVoucher.PARAM_SHOP_ID) ?: ""
    }

    fun toggleFilter(filterType: Int) {
        val currentFilter = filter.value
        if (currentFilter == filterType) {
            filter.value = NO_FILTER
        } else {
            filter.value = filterType
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

    private fun onSuccessGetVouchers(getVoucherResponse: GetVoucherResponse) {
        vouchers.value = getVoucherResponse.vouchers
        filter.value = NO_FILTER
    }

    private fun onErrorGetVouchers(throwable: Throwable) {
        error.value = throwable
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

    companion object {
        const val NO_FILTER = -1
    }

}