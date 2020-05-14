package com.tokopedia.vouchercreation.voucherlist.view.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.android.synthetic.main.bottomsheet_mvc_edit_quota.*
import kotlinx.android.synthetic.main.bottomsheet_mvc_edit_quota.view.*

/**
 * Created By @ilhamsuaib on 27/04/20
 */

class EditQuotaBottomSheet(
        parent: ViewGroup,
        private val voucher: VoucherUiModel
) : BottomSheetUnify() {

    init {
        val child = LayoutInflater.from(parent.context)
                .inflate(R.layout.bottomsheet_mvc_edit_quota, parent, false)

        setChild(child)
        setTitle(parent.context.getString(R.string.mvc_edit_quota))
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    private fun setupView(view: View) = with(view) {
        imgMvcVoucher.loadImageDrawable(R.drawable.img_mvc_cashback_khusus)
        KeyboardHandler.showSoftKeyboard(activity)

        val dummyVoucherQuota = 100
        val dummyEstimationAmount = dummyVoucherQuota * voucher.minimumAmt

        editMvcQuota?.textFieldInput?.run {
            addTextChangedListener(object : NumberTextWatcher(this@run){
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    changeTickerValue(number.toInt() * voucher.minimumAmt)
                }
            })
            setText(CurrencyFormatHelper.removeCurrencyPrefix(dummyVoucherQuota.toString()))
            selectAll()
            requestFocus()
        }

        tvMvcVoucherName.text = voucher.name
        tvMvcVoucherDescription.text = voucher.discountAmtFormatted
        mvcTicker.run {
            title = "Estimasi Maks. Pengeluaran"
            description = "Dipotong dari transaksi selesai"
            nominal = CurrencyFormatHelper.convertToRupiah(dummyEstimationAmount.toString()).toBlankOrString()
        }

        setAction(context.getString(R.string.mvc_retry)) {

        }
    }

    private fun changeTickerValue(quotaNumber: Int) {
        context?.run {
            mvcTicker?.nominal = String.format(
                    getString(R.string.mvc_rp_value),
                    CurrencyFormatHelper.convertToRupiah(quotaNumber.toString()).toBlankOrString()).toBlankOrString()
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, EditQuotaBottomSheet::class.java.simpleName)
    }
}