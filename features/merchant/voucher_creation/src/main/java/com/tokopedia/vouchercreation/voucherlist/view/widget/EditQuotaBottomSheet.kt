package com.tokopedia.vouchercreation.voucherlist.view.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.viewmodel.EditQuotaViewModel
import kotlinx.android.synthetic.main.bottomsheet_mvc_edit_quota.*
import kotlinx.android.synthetic.main.bottomsheet_mvc_edit_quota.view.*
import kotlinx.android.synthetic.main.bottomsheet_mvc_edit_quota.view.tvMvcVoucherDescription
import kotlinx.android.synthetic.main.bottomsheet_mvc_edit_quota.view.tvMvcVoucherName
import kotlinx.android.synthetic.main.item_mvc_voucher_list.view.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 27/04/20
 */

class EditQuotaBottomSheet(parent: ViewGroup) : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(parent: ViewGroup,
                           voucher: VoucherUiModel) = EditQuotaBottomSheet(parent).apply {
            this.voucher = voucher
        }
    }

    init {
        val child = LayoutInflater.from(parent.context)
                .inflate(R.layout.bottomsheet_mvc_edit_quota, parent, false)

        setChild(child)
        setTitle(parent.context.getString(R.string.mvc_edit_quota))
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    private var voucher: VoucherUiModel? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(EditQuotaViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeLiveData()
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    private fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    private fun observeLiveData() {
        viewLifecycleOwner.observe(viewModel.editQuotaSuccessLiveData) { result ->
            when(result) {
                is Success -> {
                    dismiss()
                }
                is Fail -> {

                }
            }
        }
    }

    private fun setupView(view: View) = with(view) {
        setupBottomSheetChildNoMargin()
        voucher?.run {
            setImageVoucher(isPublic, type)

            KeyboardHandler.showSoftKeyboard(activity)

            val estimationAmount = quota * minimumAmt

            editMvcQuota?.textFieldInput?.run {
                addTextChangedListener(object : NumberTextWatcher(this@run){
                    override fun onNumberChanged(number: Double) {
                        super.onNumberChanged(number)
                        changeTickerValue(number.toInt() * discountAmtMax)
                    }
                })
                setText(CurrencyFormatHelper.removeCurrencyPrefix(quota.toString()))
                selectAll()
                requestFocus()
            }

            tvMvcVoucherName.text = name
            tvMvcVoucherDescription.text = String.format(context?.getString(R.string.mvc_cashback_formatted).toBlankOrString(), discountAmtFormatted)
            mvcTicker.run {
                title = context?.getString(R.string.mvc_estimation_title).toBlankOrString()
                description = context?.getString(R.string.mvc_estimation_description).toBlankOrString()
                nominal = CurrencyFormatHelper.convertToRupiah(estimationAmount.toString()).toBlankOrString()
            }

            setAction(context.getString(R.string.mvc_retry)) {

            }
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