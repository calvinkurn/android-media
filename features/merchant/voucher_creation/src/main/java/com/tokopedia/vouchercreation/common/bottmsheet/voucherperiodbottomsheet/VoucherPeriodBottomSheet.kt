package com.tokopedia.vouchercreation.common.bottmsheet.voucherperiodbottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.android.synthetic.main.bottomsheet_mvc_voucher_edit_period.view.*

/**
 * Created By @ilhamsuaib on 29/04/20
 */

class VoucherPeriodBottomSheet(
        parent: ViewGroup,
        private val voucher: VoucherUiModel
) : BottomSheetUnify() {

    private var onSaveListener: () -> Unit = {}

    init {
        val child = LayoutInflater.from(parent.context)
                .inflate(R.layout.bottomsheet_mvc_voucher_edit_period, parent, false)

        setTitle(parent.context.getString(R.string.mvc_edit_shown_period))
        setChild(child)
        setupView(child)
        setAction(parent.context.getString(R.string.mvc_retry)) {

        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    private fun setupView(view: View) = with(view) {
        imgMvcVoucher.loadImageDrawable(R.drawable.img_mvc_cashback_khusus)
        tvMvcVoucherName.text = voucher.name
        tvMvcVoucherDescription.text = voucher.discountAmtFormatted

        edtMvcStartDate.setOnClickListener {
            showDatePicker {

            }
        }
        edtMvcEndDate.setOnClickListener {
            showDatePicker {

            }
        }

        btnMvcSavePeriod.setOnClickListener {
            onSaveListener()
            dismiss()
        }
    }

    private fun showDatePicker(function: () -> Unit) {

    }

    fun setOnSaveClickListener(callback: () -> Unit): VoucherPeriodBottomSheet {
        this.onSaveListener = callback
        return this
    }

    fun show(fm: FragmentManager) {
        showNow(fm, this::class.java.simpleName)
    }
}