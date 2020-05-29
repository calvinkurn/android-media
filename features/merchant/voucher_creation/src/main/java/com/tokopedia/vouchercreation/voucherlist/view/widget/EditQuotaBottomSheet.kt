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
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.android.synthetic.main.bottomsheet_mvc_edit_quota.*
import kotlinx.android.synthetic.main.bottomsheet_mvc_edit_quota.view.*
import kotlinx.android.synthetic.main.bottomsheet_mvc_edit_quota.view.tvMvcVoucherDescription
import kotlinx.android.synthetic.main.bottomsheet_mvc_edit_quota.view.tvMvcVoucherName
import kotlinx.android.synthetic.main.item_mvc_voucher_list.view.*
import timber.log.Timber

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
        setupBottomSheetChildNoMargin()
        setImageVoucher(voucher.isPublic, voucher.type)

        KeyboardHandler.showSoftKeyboard(activity)

        val estimationAmount = voucher.quota * voucher.minimumAmt

        editMvcQuota?.textFieldInput?.run {
            addTextChangedListener(object : NumberTextWatcher(this@run){
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    changeTickerValue(number.toInt() * voucher.discountAmtMax)
                }
            })
            setText(CurrencyFormatHelper.removeCurrencyPrefix(voucher.quota.toString()))
            selectAll()
            requestFocus()
        }

        tvMvcVoucherName.text = voucher.name
        tvMvcVoucherDescription.text = String.format(context?.getString(R.string.mvc_cashback_formatted).toBlankOrString(), voucher.discountAmtFormatted)
        mvcTicker.run {
            title = context?.getString(R.string.mvc_estimation_title).toBlankOrString()
            description = context?.getString(R.string.mvc_estimation_description).toBlankOrString()
            nominal = CurrencyFormatHelper.convertToRupiah(estimationAmount.toString()).toBlankOrString()
        }

        setAction(context.getString(R.string.mvc_retry)) {

        }
    }

    private fun setImageVoucher(isPublic: Boolean, @VoucherTypeConst voucherType: Int) {
        try {
            view?.imgMvcVoucherType?.run {
                val drawableRes = when {
                    isPublic && (voucherType == VoucherTypeConst.CASHBACK || voucherType == VoucherTypeConst.DISCOUNT) -> R.drawable.ic_mvc_cashback_publik
                    !isPublic && (voucherType == VoucherTypeConst.CASHBACK || voucherType == VoucherTypeConst.DISCOUNT) -> R.drawable.ic_mvc_cashback_khusus
                    isPublic && (voucherType == VoucherTypeConst.FREE_ONGKIR) -> R.drawable.ic_mvc_ongkir_publik
                    !isPublic && (voucherType == VoucherTypeConst.FREE_ONGKIR) -> R.drawable.ic_mvc_ongkir_khusus
                    else -> R.drawable.ic_mvc_cashback_publik
                }
                loadImageDrawable(drawableRes)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun changeTickerValue(quotaNumber: Int) {
        context?.run {
            mvcTicker?.nominal = String.format(
                    getString(R.string.mvc_rp_value),
                    CurrencyFormatHelper.convertToRupiah(quotaNumber.toString()).toBlankOrString()).toBlankOrString()
        }
    }

    private fun View.setupBottomSheetChildNoMargin() {
        val initialPaddingTop = paddingTop
        val initialPaddingBottom = paddingBottom
        val initialPaddingLeft = paddingLeft
        val initialPaddingRight = paddingRight
        setPadding(0,initialPaddingTop,0,initialPaddingBottom)
        bottomSheetHeader.setPadding(initialPaddingLeft, 0, initialPaddingRight, 0)
    }

    fun show(fm: FragmentManager) {
        show(fm, EditQuotaBottomSheet::class.java.simpleName)
    }
}