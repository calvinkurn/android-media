package com.tokopedia.vouchercreation.product.update.quota

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.NumberTextWatcher
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.NumberConstant
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.extension.digitsOnlyInt
import com.tokopedia.vouchercreation.common.extension.splitByThousand
import com.tokopedia.vouchercreation.databinding.BottomsheetUpdateCouponQuotaBinding
import com.tokopedia.vouchercreation.product.create.view.bottomsheet.ExpenseEstimationBottomSheet
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import javax.inject.Inject

class UpdateCouponQuotaBottomSheet : BottomSheetUnify() {
    companion object {
        private const val BUNDLE_KEY_VOUCHER = "voucher"
        private const val ERROR_MESSAGE = "Error edit voucher quota"

        @JvmStatic
        fun newInstance(voucher: VoucherUiModel): UpdateCouponQuotaBottomSheet {
            val args = Bundle()
            args.putParcelable(BUNDLE_KEY_VOUCHER, voucher)

            val bottomSheet = UpdateCouponQuotaBottomSheet().apply {
                arguments = args
            }
            return bottomSheet
        }

    }

    private var nullableBinding: BottomsheetUpdateCouponQuotaBinding? = null
    private val binding: BottomsheetUpdateCouponQuotaBinding
        get() = requireNotNull(nullableBinding)

    private val voucher by lazy { arguments?.getParcelable(BUNDLE_KEY_VOUCHER) as? VoucherUiModel }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(UpdateCouponQuotaViewModel::class.java) }


    private var onUpdateQuotaSuccess: () -> Unit = {}
    private var onUpdateQuotaError: (String) -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    private fun setupDependencyInjection() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        nullableBinding = BottomsheetUpdateCouponQuotaBinding.inflate(inflater, container, false)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        isKeyboardOverlap = false
        clearContentPadding = true
        setChild(binding.root)
        setTitle(getString(R.string.mvc_edit_quota).toBlankOrString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeUpdateQuotaResult()
        observeValidationResult()
        observeMaxExpenseEstimation()
        super.onViewCreated(view, savedInstanceState)
        KeyboardHandler.showSoftKeyboard(requireActivity())
        setupView()
    }

    private fun observeUpdateQuotaResult() {
        viewModel.updateQuotaResult.observe(viewLifecycleOwner) { result ->
            binding.btnSave.isLoading = false
            binding.btnSave.isClickable = true

            when (result) {
                is Success -> {
                    onUpdateQuotaSuccess()
                }
                is Fail -> {
                    onUpdateQuotaError(result.throwable.message.toBlankOrString())
                    MvcErrorHandler.logToCrashlytics(result.throwable, ERROR_MESSAGE)
                }
            }
            dismiss()
        }
    }

    private fun observeValidationResult() {
        viewModel.validInput.observe(viewLifecycleOwner) { result ->
            when (result) {
                is UpdateCouponQuotaViewModel.QuotaState.BelowMinQuota -> {
                    binding.textFieldQuota.setError(true)
                    val minQuotaErrorMessage =
                        String.format(
                            getString(R.string.placeholder_minimum_quota).toBlankOrString(),
                            result.minQuota
                        )
                    binding.textFieldQuota.setMessage(minQuotaErrorMessage)
                    binding.btnSave.isEnabled = false
                }
                is UpdateCouponQuotaViewModel.QuotaState.ExceedMaxAllowedQuota -> {
                    binding.textFieldQuota.setError(true)
                    val maxQuotaErrorMessage =
                        String.format(
                            getString(R.string.placeholder_maximum_quota).toBlankOrString(),
                            result.maxQuota
                        )
                    binding.textFieldQuota.setMessage(maxQuotaErrorMessage)
                    binding.btnSave.isEnabled = false
                }
                is UpdateCouponQuotaViewModel.QuotaState.Valid -> {
                    binding.textFieldQuota.setError(false)
                    binding.textFieldQuota.setMessage("")
                    binding.btnSave.isEnabled = true
                }
            }
        }
    }

    private fun observeMaxExpenseEstimation() {
        viewModel.maxExpenseEstimation.observe(viewLifecycleOwner, { maxExpense ->
            if (maxExpense > NumberConstant.ZERO) {
                binding.tpgExpenseAmount.text = String.format(
                    getString(R.string.placeholder_rupiah),
                    maxExpense.splitByThousand()
                )
            } else {
                binding.tpgExpenseAmount.text = getString(R.string.hyphen)
            }
        })
    }

    private fun setupView() {
        val voucher = voucher ?: return

        displayCouponImage(voucher.isPublic, voucher.type)
        displayCouponInformation(voucher)
        setupQuotaTextField(voucher.quota, voucher.discountAmtMax)
        handleTickerVisibility(voucher.status)

        binding.imgExpenseEstimationDescription.setOnClickListener {
            displayExpenseEstimationDescription()
        }

        binding.btnSave.setOnClickListener {
            binding.btnSave.isLoading = true
            binding.btnSave.isClickable = false

            val newQuota = binding.textFieldQuota.textFieldInput.text.toString().digitsOnlyInt()
            if (newQuota != voucher.quota) {
                viewModel.updateQuota(voucher.id, newQuota)
            } else {
                dismiss()
            }
        }

        binding.tpgRetry.setOnClickListener { resetQuota(voucher.quota) }
    }


    private fun setupQuotaTextField(quota: Int, maxDiscountAmount: Int) {
        val textWatcher = object : NumberTextWatcher(binding.textFieldQuota.textFieldInput) {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                viewModel.calculateMaxExpenseEstimation(maxDiscountAmount, number.toInt())
                viewModel.validateInput(number.toInt(), quota)
            }
        }

        with(binding.textFieldQuota.textFieldInput) {
            addTextChangedListener(textWatcher)
            setText(quota.toString())
            selectAll()
            requestFocus()
        }
    }

    private fun handleTickerVisibility(voucherStatus: Int) {
        if (voucherStatus == VoucherStatusConst.NOT_STARTED) {
            removeTicker()
        }

    }

    private fun displayCouponInformation(voucher: VoucherUiModel) {
        binding.tpgCouponName.text = voucher.name
        binding.tpgCouponDescription.text = String.format(
            getString(R.string.mvc_discount_formatted).toBlankOrString(),
            voucher.typeFormatted,
            voucher.discountAmtFormatted
        )
    }


    private fun resetQuota(quota: Int) {
        binding.textFieldQuota.textFieldInput.setText(quota.toString())
    }

    private fun displayCouponImage(isPublic: Boolean, @VoucherTypeConst voucherType: Int) {
        val drawableRes = when {
            isPublic && (voucherType == VoucherTypeConst.CASHBACK || voucherType == VoucherTypeConst.DISCOUNT) -> R.drawable.ic_mvc_cashback_publik
            !isPublic && (voucherType == VoucherTypeConst.CASHBACK || voucherType == VoucherTypeConst.DISCOUNT) -> R.drawable.ic_mvc_cashback_khusus
            isPublic && (voucherType == VoucherTypeConst.FREE_ONGKIR) -> R.drawable.ic_mvc_ongkir_publik
            !isPublic && (voucherType == VoucherTypeConst.FREE_ONGKIR) -> R.drawable.ic_mvc_ongkir_khusus
            else -> R.drawable.ic_mvc_cashback_publik
        }
        binding.imgCoupon.loadImageDrawable(drawableRes)
    }

    private fun removeTicker() {
        binding.imgTips.gone()
        binding.tpgTips.gone()
        binding.divider.gone()
    }

    fun setOnUpdateQuotaSuccess(action: () -> Unit) {
        onUpdateQuotaSuccess = action
    }

    fun setOnUpdateQuotaError(action: (String) -> Unit) {
        onUpdateQuotaError = action
    }

    private fun displayExpenseEstimationDescription() {
        if (!isAdded) return
        val bottomSheet = ExpenseEstimationBottomSheet.newInstance()
        bottomSheet.show(childFragmentManager)
    }

}