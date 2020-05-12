package com.tokopedia.vouchercreation.voucherlist.view.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
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

        tvMvcVoucherName.text = voucher.name
        tvMvcVoucherDescription.text = voucher.discountAmtFormatted
        mvcTicker.run {
            title = "Estimasi Maks. Pengeluaran"
            description = "Dipotong dari transaksi selesai"
            nominal = "Rp3.290.000"
        }
        edtMvcQuota.requestFocus()

        setAction(context.getString(R.string.mvc_retry)) {

        }
    }

    fun show(fm: FragmentManager) {
        show(fm, EditQuotaBottomSheet::class.java.simpleName)
    }
}