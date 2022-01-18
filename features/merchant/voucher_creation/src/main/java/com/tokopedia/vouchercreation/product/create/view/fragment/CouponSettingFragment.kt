package com.tokopedia.vouchercreation.product.create.view.fragment

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.TextAreaUnify
import com.tokopedia.utils.lifecycle.autoCleared
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.LocaleConstant
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.extension.digitsOnlyInt
import com.tokopedia.vouchercreation.common.extension.splitByThousand
import com.tokopedia.vouchercreation.common.textwatcher.NumberThousandSeparatorTextWatcher
import com.tokopedia.vouchercreation.databinding.FragmentCouponSettingBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import com.tokopedia.vouchercreation.product.create.domain.entity.DiscountType
import com.tokopedia.vouchercreation.product.create.domain.entity.MinimumPurchaseType
import com.tokopedia.vouchercreation.product.create.view.viewmodel.CouponSettingViewModel
import java.text.DecimalFormat
import java.text.NumberFormat
import javax.inject.Inject


class CouponSettingFragment : BaseDaggerFragment() {

    companion object {
        private const val SCREEN_NAME = "Coupon Setting Page"
        private const val EMPTY_STRING = ""
        private const val ZERO = 0
        private const val NUMBER_FORMAT_PATTERN = "#,###,###"

        fun newInstance():  CouponSettingFragment {
            val args = Bundle()
            val fragment = CouponSettingFragment()
            fragment.arguments = args
            return fragment
        }

    }

    private var binding : FragmentCouponSettingBinding by autoCleared()
    private var selectedCouponType = CouponType.NONE
    private var selectedDiscountType = DiscountType.NONE
    private var selectedMinimumPurchaseType = MinimumPurchaseType.NONE

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CouponSettingViewModel::class.java) }

    override fun getScreenName() = SCREEN_NAME

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCouponSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupChipsListener()
        setupTextAreaListener()
        observeInputValidationResult()
        observeCouponTypeChange()
        observeMaxExpenseEstimation()
    }

    private fun observeMaxExpenseEstimation() {
        viewModel.maxExpenseEstimation.observe(viewLifecycleOwner, { maxExpense ->
            if (maxExpense > ZERO) {
                binding.tpgExpenseAmount.text = String.format(
                    getString(R.string.placeholder_rupiah),
                    maxExpense.splitByThousand()
                )
            } else {
                binding.tpgExpenseAmount.text = getString(R.string.hyphen)
            }
        })
    }

    private fun observeInputValidationResult() {
        viewModel.areInputValid.observe(viewLifecycleOwner, { validationResult ->
            binding.btnSave.isEnabled = validationResult
        })
    }

    private fun observeCouponTypeChange() {
        viewModel.couponType.observe(viewLifecycleOwner, { selectedCouponType ->
            binding.btnSave.isEnabled = false
            calculateMaxExpenseEstimation()
            if (selectedCouponType == CouponType.CASHBACK) {
                clearCashbackSelection()
            } else {
                clearFreeShippingSelection()
            }
        })
    }

    private fun setupViews() {
        binding.textAreaMinimumDiscount.textAreaInput.inputType = InputType.TYPE_CLASS_NUMBER
        binding.textAreaQuota.textAreaInput.inputType = InputType.TYPE_CLASS_NUMBER
        binding.textAreaMinimumPurchase.textAreaInput.inputType = InputType.TYPE_CLASS_NUMBER

        binding.textAreaFreeShippingQuota.textAreaInput.inputType = InputType.TYPE_CLASS_NUMBER
        binding.textAreaFreeShippingDiscountAmount.textAreaInput.inputType = InputType.TYPE_CLASS_NUMBER
        binding.textAreaFreeShippingMinimumPurchase.textAreaInput.inputType = InputType.TYPE_CLASS_NUMBER
    }

    private fun setupTextAreaListener() {
        val numberFormatter = NumberFormat.getInstance(LocaleConstant.INDONESIA) as DecimalFormat
        numberFormatter.applyPattern(NUMBER_FORMAT_PATTERN)
        setupTextAreaCashbackListener(numberFormatter)
        setupTextAreaFreeShippingListener(numberFormatter)
    }

    private fun setupTextAreaFreeShippingListener(numberFormatter : DecimalFormat) {
        with(binding) {

            textAreaMinimumDiscount.textAreaInput.addTextChangedListener(NumberThousandSeparatorTextWatcher(
                textAreaMinimumDiscount.textAreaInput,
                numberFormatter
            ) { number, formattedNumber ->

                textAreaMinimumDiscount.textAreaInput.setText(formattedNumber)
                textAreaMinimumDiscount.textAreaInput.setSelection(textAreaMinimumDiscount.textAreaInput.text?.length ?: 0)

                val isValidInput =
                    viewModel.isValidCashbackDiscountAmount(number)
                if (isValidInput) {
                    clearErrorMessage(textAreaMinimumDiscount, getString(R.string.error_message_minimum_discount))
                } else {
                    showErrorMessage(textAreaMinimumDiscount, getString(R.string.error_message_minimum_discount))
                }
                validateInput()
                calculateMaxExpenseEstimation()
            })

            textAreaMinimumPurchase.textAreaInput.addTextChangedListener(NumberThousandSeparatorTextWatcher(
                textAreaMinimumPurchase.textAreaInput,
                numberFormatter
            ) { number, formattedNumber ->

                textAreaMinimumPurchase.textAreaInput.setText(formattedNumber)
                textAreaMinimumPurchase.textAreaInput.setSelection(textAreaMinimumPurchase.textAreaInput.text?.length ?: 0)

                val discountAmount = textAreaMinimumDiscount.textAreaInput.text.toString().trim().digitsOnlyInt()
                val isValidInput =
                    viewModel.isValidCashbackMinimumPurchase(number, discountAmount, selectedMinimumPurchaseType)
                if (isValidInput) {
                    clearMinimalPurchaseErrorMessage(textAreaMinimumPurchase)
                } else {
                    showMinimalPurchaseErrorMessage(textAreaMinimumPurchase)
                }
                validateInput()
            })

            textAreaQuota.textAreaInput.addTextChangedListener(NumberThousandSeparatorTextWatcher(
                textAreaQuota.textAreaInput,
                numberFormatter
            ) { number, formattedNumber ->

                textAreaQuota.textAreaInput.setText(formattedNumber)
                textAreaQuota.textAreaInput.setSelection(textAreaQuota.textAreaInput.text?.length ?: 0)

                when (viewModel.isValidQuota(number)) {
                    CouponSettingViewModel.QuotaState.BelowAllowedQuotaAmount -> {
                        showErrorMessage(
                            textAreaQuota,
                            getString(R.string.error_message_quota_below_minimum)
                        )
                    }
                    CouponSettingViewModel.QuotaState.ExceedAllowedQuotaAmount -> {
                        showErrorMessage(
                            textAreaQuota,
                            getString(R.string.error_message_quota_exceed_maximum)
                        )
                    }
                    CouponSettingViewModel.QuotaState.ValidQuota -> {
                        clearErrorMessage(textAreaQuota,  EMPTY_STRING)
                    }
                }

                validateInput()
                calculateMaxExpenseEstimation()
            })
        }
    }
    private fun setupTextAreaCashbackListener(numberFormatter: DecimalFormat) {
        with(binding) {
            textAreaFreeShippingDiscountAmount.textAreaInput.addTextChangedListener(NumberThousandSeparatorTextWatcher(
                textAreaFreeShippingDiscountAmount.textAreaInput,
                numberFormatter
            ) { number, formattedNumber ->

                textAreaFreeShippingDiscountAmount.textAreaInput.setText(formattedNumber)
                textAreaFreeShippingDiscountAmount.textAreaInput.setSelection(textAreaFreeShippingDiscountAmount.textAreaInput.text?.length ?: 0)

                val isValidInput = viewModel.isValidFreeShippingDiscountAmount(number)
                if (isValidInput) {
                    clearErrorMessage(textAreaFreeShippingDiscountAmount, getString(R.string.error_message_minimum_discount))
                } else {
                    showErrorMessage(textAreaFreeShippingDiscountAmount, getString(R.string.error_message_minimum_discount))
                }
                validateInput()
                calculateMaxExpenseEstimation()
            })


            textAreaFreeShippingMinimumPurchase.textAreaInput.addTextChangedListener(NumberThousandSeparatorTextWatcher(
                textAreaFreeShippingMinimumPurchase.textAreaInput,
                numberFormatter
            ) { number, formattedNumber ->

                textAreaFreeShippingMinimumPurchase.textAreaInput.setText(formattedNumber)
                textAreaFreeShippingMinimumPurchase.textAreaInput.setSelection(textAreaFreeShippingMinimumPurchase.textAreaInput.text?.length ?: 0)

                val freeShippingDiscountAmount = textAreaFreeShippingDiscountAmount.textAreaInput.text.toString().trim().digitsOnlyInt()
                val isValidInput = viewModel.isValidFreeShippingMinimumPurchase(number, freeShippingDiscountAmount)
                if (isValidInput) {
                    clearMinimalPurchaseErrorMessage(textAreaFreeShippingMinimumPurchase)
                } else {
                    showErrorMessage(textAreaFreeShippingMinimumPurchase, getString(R.string.error_message_invalid_free_shipping_minimum_purchase))
                }
                validateInput()
            })

            textAreaFreeShippingQuota.textAreaInput.addTextChangedListener(NumberThousandSeparatorTextWatcher(
                textAreaFreeShippingQuota.textAreaInput,
                numberFormatter
            ) { number, formattedNumber ->

                textAreaFreeShippingQuota.textAreaInput.setText(formattedNumber)
                textAreaFreeShippingQuota.textAreaInput.setSelection(textAreaFreeShippingQuota.textAreaInput.text?.length ?: 0)

                when (viewModel.isValidQuota(number)) {
                    CouponSettingViewModel.QuotaState.BelowAllowedQuotaAmount -> {
                        showErrorMessage(
                            textAreaFreeShippingQuota,
                            getString(R.string.error_message_quota_below_minimum)
                        )
                    }
                    CouponSettingViewModel.QuotaState.ExceedAllowedQuotaAmount -> {
                        showErrorMessage(
                            textAreaFreeShippingQuota,
                            getString(R.string.error_message_quota_exceed_maximum)
                        )
                    }
                    CouponSettingViewModel.QuotaState.ValidQuota -> {
                        clearErrorMessage(textAreaFreeShippingQuota,  EMPTY_STRING)
                    }
                }

                validateInput()
                calculateMaxExpenseEstimation()
            })

            textAreaDiscountPercentage.textAreaInput.addTextChangedListener(NumberThousandSeparatorTextWatcher(
                textAreaDiscountPercentage.textAreaInput,
                numberFormatter
            ) { number, formattedNumber ->

                textAreaDiscountPercentage.textAreaInput.setText(formattedNumber)
                textAreaDiscountPercentage.textAreaInput.setSelection(textAreaDiscountPercentage.textAreaInput.text?.length ?: 0)

                when (viewModel.isValidCashbackPercentage(number)) {
                    CouponSettingViewModel.CashbackPercentageState.BelowAllowedMinimumPercentage -> {
                        showErrorMessage(textAreaDiscountPercentage, getString(R.string.error_message_discount_percentage_below_minimum))
                    }
                    CouponSettingViewModel.CashbackPercentageState.ExceedAllowedMaximumPercentage -> {
                        showErrorMessage(textAreaDiscountPercentage, getString(R.string.error_message_discount_percentage_exceed_maximum))
                    }
                    CouponSettingViewModel.CashbackPercentageState.ValidPercentage -> {
                        clearErrorMessage(textAreaDiscountPercentage, getString(R.string.error_message_discount_percentage_range))
                    }
                }

                validateInput()

            })

            textAreaMaximumDiscount.textAreaInput.addTextChangedListener(NumberThousandSeparatorTextWatcher(
                textAreaMaximumDiscount.textAreaInput,
                numberFormatter
            ) { number, formattedNumber ->

                textAreaMaximumDiscount.textAreaInput.setText(formattedNumber)
                textAreaMaximumDiscount.textAreaInput.setSelection(textAreaMaximumDiscount.textAreaInput.text?.length ?: 0)

                when (viewModel.isValidMaximumCashbackAmount(number)) {
                    CouponSettingViewModel.CashbackAmountState.BelowAllowedMinimumAmount -> {
                        showErrorMessage(textAreaMaximumDiscount, getString(R.string.error_message_cashback_amount_below_minimum))
                    }
                    CouponSettingViewModel.CashbackAmountState.ExceedAllowedMinimumAmount -> {
                        showErrorMessage(textAreaMaximumDiscount, getString(R.string.error_message_cashback_amount_exceed_maximum))
                    }
                    CouponSettingViewModel.CashbackAmountState.ValidAmount -> {
                        clearErrorMessage(textAreaMaximumDiscount, EMPTY_STRING)
                    }
                }

                validateInput()

            })
        }
    }

    private fun setupChipsListener() {
        binding.chipCashback.chip_container.setOnClickListener {
            binding.chipCashback.chipType = ChipsUnify.TYPE_SELECTED
            binding.chipFreeShipping.chipType = ChipsUnify.TYPE_NORMAL

            binding.chipDiscountTypeNominal.chipType = ChipsUnify.TYPE_SELECTED
            binding.chipMinimumPurchaseNominal.chipType = ChipsUnify.TYPE_SELECTED

            selectedCouponType = CouponType.CASHBACK
            selectedDiscountType = DiscountType.NOMINAL
            selectedMinimumPurchaseType = MinimumPurchaseType.NOMINAL

            viewModel.couponTypeChanged(selectedCouponType)

            showCashbackCouponTypeWidget()
        }

        binding.chipFreeShipping.chip_container.setOnClickListener {
            binding.chipCashback.chipType = ChipsUnify.TYPE_NORMAL
            binding.chipFreeShipping.chipType = ChipsUnify.TYPE_SELECTED

            selectedCouponType = CouponType.FREE_SHIPPING
            viewModel.couponTypeChanged(selectedCouponType)

            showFreeShippingCouponTypeWidget()
        }

        binding.chipCashback.selectedChangeListener = { isActive ->
            if (isActive) {
                binding.groupCashback.visible()
            } else {
                binding.groupCashback.gone()
            }
        }

        binding.chipFreeShipping.selectedChangeListener = { isActive ->
            if (isActive) {
                binding.groupFreeShipping.visible()
            } else {
                binding.groupFreeShipping.gone()
            }
        }

        binding.chipDiscountTypeNominal.selectedChangeListener = {
            binding.chipDiscountTypeNominal.chipText = getString(R.string.in_rupiah)
        }

        binding.chipMinimumPurchaseNominal.selectedChangeListener = {
            binding.chipMinimumPurchaseNominal.chipText = getString(R.string.in_nominal)
        }

        binding.chipDiscountTypeNominal.chip_container.setOnClickListener {
            binding.chipDiscountTypeNominal.chipText = getString(R.string.in_rupiah)
            binding.chipDiscountTypeNominal.chipType = ChipsUnify.TYPE_SELECTED

            binding.chipDiscountTypePercentage.chipText = getString(R.string.mvc_create_tips_subtitle_percentage)
            binding.chipDiscountTypePercentage.chipType = ChipsUnify.TYPE_NORMAL

            binding.textAreaDiscountPercentage.textAreaWrapper.suffixText = getString(R.string.percent)

            selectedDiscountType = DiscountType.NOMINAL

            showNominalDiscountTypeWidget()
            validateInput()
        }

        binding.chipDiscountTypePercentage.chip_container.setOnClickListener {
            binding.chipDiscountTypeNominal.chipText = getString(R.string.nominal)
            binding.chipDiscountTypeNominal.chipType = ChipsUnify.TYPE_NORMAL

            binding.chipDiscountTypePercentage.chipText = getString(R.string.in_percentage)
            binding.chipDiscountTypePercentage.chipType = ChipsUnify.TYPE_SELECTED

            selectedDiscountType = DiscountType.PERCENTAGE

            showPercentageDiscountTypeWidget()
            validateInput()
        }

        binding.chipMinimumPurchaseNominal.chip_container.setOnClickListener {
            binding.chipMinimumPurchaseNominal.chipText = getString(R.string.in_nominal)
            binding.chipMinimumPurchaseQuantity.chipText = getString(R.string.quantity)

            binding.groupCashbackMinimumPurchase.visible()

            binding.textAreaMinimumPurchase.textAreaWrapper.prefixText = getString(R.string.rupiah)
            binding.textAreaMinimumPurchase.textAreaWrapper.suffixText = EMPTY_STRING

            binding.textAreaMinimumPurchase.textAreaMessage = getString(R.string.error_message_invalid_cashback_minimum_purchase_nominal)

            binding.chipMinimumPurchaseNominal.chipType = ChipsUnify.TYPE_SELECTED
            binding.chipMinimumPurchaseQuantity.chipType = ChipsUnify.TYPE_NORMAL
            binding.chipMinimumPurchaseNothing.chipType = ChipsUnify.TYPE_NORMAL

            selectedMinimumPurchaseType = MinimumPurchaseType.NOMINAL

            validateInput()
        }

        binding.chipMinimumPurchaseQuantity.chip_container.setOnClickListener {
            binding.chipMinimumPurchaseNominal.chipText = getString(R.string.nominal)
            binding.chipMinimumPurchaseQuantity.chipText = getString(R.string.in_quantity)

            binding.groupCashbackMinimumPurchase.visible()

            binding.textAreaMinimumPurchase.textAreaWrapper.prefixText = EMPTY_STRING
            binding.textAreaMinimumPurchase.textAreaWrapper.suffixText = getString(R.string.pcs)

            binding.textAreaMinimumPurchase.textAreaMessage = getString(R.string.error_message_invalid_cashback_minimum_purchase_quantity)

            binding.chipMinimumPurchaseNominal.chipType = ChipsUnify.TYPE_NORMAL
            binding.chipMinimumPurchaseQuantity.chipType = ChipsUnify.TYPE_SELECTED
            binding.chipMinimumPurchaseNothing.chipType = ChipsUnify.TYPE_NORMAL

            selectedMinimumPurchaseType = MinimumPurchaseType.QUANTITY

            validateInput()
        }

        binding.chipMinimumPurchaseNothing.chip_container.setOnClickListener {
            binding.chipMinimumPurchaseNominal.chipText = getString(R.string.nominal)
            binding.chipMinimumPurchaseQuantity.chipText = getString(R.string.quantity)

            binding.groupCashbackMinimumPurchase.gone()

            binding.textAreaMinimumPurchase.textAreaWrapper.prefixText = EMPTY_STRING
            binding.textAreaMinimumPurchase.textAreaWrapper.suffixText = EMPTY_STRING
            binding.textAreaMinimumPurchase.textAreaMessage = EMPTY_STRING

            binding.chipMinimumPurchaseNominal.chipType = ChipsUnify.TYPE_NORMAL
            binding.chipMinimumPurchaseQuantity.chipType = ChipsUnify.TYPE_NORMAL
            binding.chipMinimumPurchaseNothing.chipType = ChipsUnify.TYPE_SELECTED

            selectedMinimumPurchaseType = MinimumPurchaseType.NOTHING

            validateInput()
        }
    }


    private fun adjustExpenseEstimationConstraint(@IdRes viewId : Int) {
        val layoutParams = binding.layoutExpenseEstimation.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.topToBottom = viewId
    }

    private fun adjustMinimumPurchaseConstraint(@IdRes viewId : Int) {
        val layoutParams = binding.tpgMinimumPurchase.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.topToBottom = viewId
    }


    private fun clearFreeShippingSelection() {
        with(binding) {
            textAreaFreeShippingQuota.textAreaInput.text = null
            textAreaFreeShippingDiscountAmount.textAreaInput.text = null
            textAreaFreeShippingMinimumPurchase.textAreaInput.text = null


            chipMinimumPurchaseQuantity.chipType = ChipsUnify.TYPE_NORMAL
            chipMinimumPurchaseNothing.chipType = ChipsUnify.TYPE_NORMAL
            chipMinimumPurchaseNominal.chipType = ChipsUnify.TYPE_NORMAL

            chipDiscountTypeNominal.chipType = ChipsUnify.TYPE_NORMAL
            chipDiscountTypePercentage.chipType = ChipsUnify.TYPE_NORMAL
        }

    }

    private fun clearCashbackSelection() {
        with(binding) {
            textAreaMinimumDiscount.textAreaInput.text = null
            textAreaQuota.textAreaInput.text = null
            textAreaMinimumPurchase.textAreaInput.text = null
        }
    }

    private fun validateInput() {
        val cashbackDiscountAmount = binding.textAreaMinimumDiscount.textAreaInput.text.toString().trim().digitsOnlyInt()
        val cashbackMinimumPurchase = binding.textAreaMinimumPurchase.textAreaInput.text.toString().trim().digitsOnlyInt()
        val cashbackQuota = binding.textAreaQuota.textAreaInput.text.toString().trim().digitsOnlyInt()

        val cashbackDiscountPercentage = binding.textAreaDiscountPercentage.textAreaInput.text.toString().trim().digitsOnlyInt()
        val cashbackMaxDiscount = binding.textAreaMaximumDiscount.textAreaInput.text.toString().trim().digitsOnlyInt()

        val freeShippingDiscountAmount = binding.textAreaFreeShippingDiscountAmount.textAreaInput.text.toString().trim().digitsOnlyInt()
        val freeShippingMinimumPurchase = binding.textAreaFreeShippingMinimumPurchase.textAreaInput.text.toString().trim().digitsOnlyInt()
        val freeShippingQuota = binding.textAreaFreeShippingQuota.textAreaInput.text.toString().trim().digitsOnlyInt()

        viewModel.validateInput(
            selectedCouponType,
            selectedDiscountType,
            selectedMinimumPurchaseType,
            cashbackDiscountPercentage,
            cashbackMaxDiscount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )
    }

    private fun calculateMaxExpenseEstimation() {
        val discountAmount = if (selectedCouponType == CouponType.CASHBACK) {
            binding.textAreaMinimumDiscount.textAreaInput.text.toString().trim().digitsOnlyInt()
        } else {
            binding.textAreaFreeShippingDiscountAmount.textAreaInput.text.toString().trim().digitsOnlyInt()
        }
        val voucherQuota = if (selectedCouponType == CouponType.CASHBACK) {
            binding.textAreaQuota.textAreaInput.text.toString().trim().digitsOnlyInt()
        } else {
            binding.textAreaFreeShippingQuota.textAreaInput.text.toString().trim().digitsOnlyInt()
        }
        viewModel.calculateMaxExpenseEstimation(discountAmount, voucherQuota)
    }

    private fun showErrorMessage(view: TextAreaUnify, errorMessage : String) {
        view.textAreaMessage = EMPTY_STRING
        view.isError = true
        view.textAreaWrapper.error = errorMessage
    }

    private fun clearErrorMessage(view: TextAreaUnify, errorMessage: String) {
        view.isError = false
        view.textAreaWrapper.error = EMPTY_STRING
        view.textAreaMessage = errorMessage
    }

    private fun showMinimalPurchaseErrorMessage(view: TextAreaUnify) {
        val errorMessage = viewModel.getMinimalPurchaseErrorMessage(selectedCouponType, selectedMinimumPurchaseType)
        view.textAreaMessage = EMPTY_STRING
        view.isError = true
        view.textAreaWrapper.error = errorMessage
    }

    private fun clearMinimalPurchaseErrorMessage(view: TextAreaUnify) {
        val errorMessage = viewModel.getMinimalPurchaseErrorMessage(selectedCouponType, selectedMinimumPurchaseType)
        view.isError = false
        view.textAreaWrapper.error = EMPTY_STRING
        view.textAreaMessage = errorMessage
    }

    private fun showCashbackCouponTypeWidget() {
        binding.groupCashbackMinimumPurchase.visible()
        binding.groupNominalDiscountType.visible()

        showNominalDiscountTypeWidget()

        adjustExpenseEstimationConstraint(binding.textAreaQuota.id)
    }

    private fun showFreeShippingCouponTypeWidget() {
        binding.groupCashbackMinimumPurchase.gone()
        binding.groupNominalDiscountType.gone()
        hideNominalDiscountTypeWidget()

        hidePercentageDiscountTypeWidget()

        adjustExpenseEstimationConstraint(binding.textAreaFreeShippingQuota.id)
    }

    private fun showNominalDiscountTypeWidget() {
        binding.groupNominalDiscountType.visible()
        binding.groupPercentageDiscountType.gone()
        adjustMinimumPurchaseConstraint(binding.textAreaMinimumDiscount.id)
    }

    private fun showPercentageDiscountTypeWidget() {
        binding.groupNominalDiscountType.gone()
        binding.groupPercentageDiscountType.visible()
        adjustMinimumPurchaseConstraint(binding.textAreaMaximumDiscount.id)
    }

    private fun hideNominalDiscountTypeWidget() {
        binding.groupNominalDiscountType.gone()
        binding.groupPercentageDiscountType.gone()
    }

    private fun hidePercentageDiscountTypeWidget() {
        binding.groupNominalDiscountType.gone()
        binding.groupPercentageDiscountType.gone()
    }

}