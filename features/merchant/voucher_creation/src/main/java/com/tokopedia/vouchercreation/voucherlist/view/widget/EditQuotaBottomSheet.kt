package com.tokopedia.vouchercreation.voucherlist.view.widget

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.viewmodel.EditQuotaViewModel
import kotlinx.android.synthetic.main.bottomsheet_mvc_edit_quota.*
import kotlinx.android.synthetic.main.bottomsheet_mvc_edit_quota.view.*
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

        private const val MIN_QUOTA = 1
        private const val MAX_QUOTA = 999

        private const val MAX_TEXTFIELD_LENGTH = 3
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

    private var minQuota = MIN_QUOTA

    private val minQuotaErrorMessage
        get() = String.format(context?.getString(R.string.mvc_quota_min).toBlankOrString(), minQuota)
    private val maxQuotaErrorMessage = String.format(context?.getString(R.string.mvc_quota_max).toBlankOrString(), MAX_QUOTA)

    private var onSuccessUpdateVoucher: () -> Unit = {}

    private var onFailUpdateVoucher: (String) -> Unit = {}

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
                    onSuccessUpdateVoucher()
                }
                is Fail -> {
                    onFailUpdateVoucher(result.throwable.message.toBlankOrString())
                }
            }
            btnMvcSaveQuota?.run {
                isLoading = false
                isClickable = true
            }
            dismiss()
        }
    }

    private fun setupView(view: View) = with(view) {
        setupBottomSheetChildNoMargin()
        voucher?.run voucher@ {
            setImageVoucher(isPublic, type)

            KeyboardHandler.showSoftKeyboard(activity)

            val estimationAmount = quota * minimumAmt

            if (status == VoucherStatusConst.ONGOING) {
                minQuota = quota
            }

            if (status == VoucherStatusConst.NOT_STARTED) {
                removeTicker()
            }

            editMvcQuota?.textFieldInput?.run et@ {
                addTextChangedListener(object : NumberTextWatcher(this@et){
                    override fun onNumberChanged(number: Double) {
                        super.onNumberChanged(number)
                        changeTickerValue(number.toInt() * discountAmtMax)
                        onChangeQuotaFieldValue(number.toInt())
                    }
                })
                setText(CurrencyFormatHelper.removeCurrencyPrefix(quota.toString()))
                selectAll()
                requestFocus()
                filters = arrayOf(InputFilter.LengthFilter(MAX_TEXTFIELD_LENGTH))
            }

            btnMvcSaveQuota?.run {
                setOnClickListener {
                    val textEditQuota = this@with.editMvcQuota?.textFieldInput?.text?.toString()?.toInt()
                    if (textEditQuota != quota) {
                        viewModel.changeQuotaValue(this@voucher.id, this@with.editMvcQuota?.textFieldInput?.text?.toString()?.toInt() ?: quota)
                        isLoading = true
                        isClickable = false
                    } else {
                        dismiss()
                    }
                }
            }

            tvMvcVoucherName.text = name
            tvMvcVoucherDescription.text = String.format(context?.getString(R.string.mvc_discount_formatted).toBlankOrString(), typeFormatted, discountAmtFormatted)
            mvcTicker.run {
                title = context?.getString(R.string.mvc_estimation_title).toBlankOrString()
                description = context?.getString(R.string.mvc_estimation_description).toBlankOrString()
                nominal = CurrencyFormatHelper.convertToRupiah(estimationAmount.toString()).toBlankOrString()
            }

            setAction(context.getString(R.string.mvc_retry)) {
                editMvcQuota?.textFieldInput?.setText(CurrencyFormatHelper.removeCurrencyPrefix(quota.toString()))
            }
        }
    }

    private fun setImageVoucher(isPublic: Boolean, @VoucherTypeConst voucherType: Int) {
        try {
            view?.imgMvcVoucher?.run {
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

    private fun onChangeQuotaFieldValue(quota: Int) {
        when {
            quota < minQuota -> {
                editMvcQuota?.setError(true)
                editMvcQuota?.setMessage(minQuotaErrorMessage)
                btnMvcSaveQuota?.isEnabled = false
            }
            quota > MAX_QUOTA -> {
                editMvcQuota?.setError(true)
                editMvcQuota?.setMessage(maxQuotaErrorMessage)
                btnMvcSaveQuota?.isEnabled = false
            }
            else -> {
                editMvcQuota?.setError(false)
                editMvcQuota?.setMessage("")
                btnMvcSaveQuota?.isEnabled = true
            }
        }
    }

    private fun removeTicker() {
        view?.run {
            icMvcTips?.gone()
            tvMvcTips?.gone()
            vMvcVerLine1?.gone()
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

    fun setOnSuccessUpdateVoucher(action: () -> Unit): EditQuotaBottomSheet {
        onSuccessUpdateVoucher = action
        return this
    }

    fun setOnFailUpdateVoucher(action: (String) -> Unit): EditQuotaBottomSheet {
        onFailUpdateVoucher = action
        return this
    }
}