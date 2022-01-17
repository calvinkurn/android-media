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
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.extension.onTextChanged
import com.tokopedia.vouchercreation.databinding.FragmentCouponSettingBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import com.tokopedia.vouchercreation.product.create.domain.entity.DiscountType
import com.tokopedia.vouchercreation.product.create.domain.entity.MinimumPurchaseType
import com.tokopedia.vouchercreation.product.create.view.viewmodel.CouponSettingViewModel
import timber.log.Timber
import javax.inject.Inject


class CouponSettingFragment : BaseDaggerFragment() {

    companion object {
        private const val SCREEN_NAME = "Coupon Setting Page"
        private const val EMPTY_STRING = ""
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
    }

    private fun observeInputValidationResult() {
        viewModel.areInputValid.observe(viewLifecycleOwner, { validationResult ->
            binding.btnSave.isEnabled = validationResult
            Timber.d("Validation result is $validationResult")
        })
    }

    private fun observeCouponTypeChange() {
        viewModel.couponType.observe(viewLifecycleOwner, { couponType ->
            binding.btnSave.isEnabled = false
            /*if (couponType == CouponType.CASHBACK) {
                clearCashbackData()
            } else {
                clearFreeShippingData()
            }*/
        })
    }

    private fun setupViews() {
        binding.textAreaMinimumDiscount.textAreaInput.inputType = InputType.TYPE_CLASS_NUMBER
        binding.textAreaQuota.textAreaInput.inputType = InputType.TYPE_CLASS_NUMBER
        binding.textAreaMinimumPurchase.textAreaInput.inputType = InputType.TYPE_CLASS_NUMBER

        binding.textAreaQuotaFreeShipping.textAreaInput.inputType = InputType.TYPE_CLASS_NUMBER
        binding.textAreaFreeShippingDiscountAmount.textAreaInput.inputType = InputType.TYPE_CLASS_NUMBER
        binding.textAreaFreeShippingMinimumPurchase.textAreaInput.inputType = InputType.TYPE_CLASS_NUMBER
    }

    private fun setupTextAreaListener() {
        with(binding) {
            textAreaMinimumDiscount.textAreaInput.onTextChanged { text ->
                val isValidInput =
                    viewModel.isValidCashbackDiscountAmount(text.trim().toIntSafely())
                if (isValidInput) {
                    clearErrorMessage(binding.textAreaMinimumDiscount)
                } else {
                    showErrorMessage(
                        binding.textAreaMinimumDiscount,
                        getString(R.string.error_message_invalid_min_purchase)
                    )
                }
                validateInput()
            }
            textAreaMinimumPurchase.textAreaInput.onTextChanged { text ->
                val discountAmount = binding.textAreaMinimumDiscount.textAreaInput.text.toString().trim().toIntSafely()
                val isValidInput =
                    viewModel.isValidCashbackMinimumPurchase(text.trim().toIntSafely(), discountAmount)
                if (isValidInput) {
                    clearErrorMessage(binding.textAreaMinimumPurchase)
                } else {
                    showErrorMessage(
                        binding.textAreaMinimumPurchase,
                        getString(R.string.error_message_invalid_cashback_minimum_purchase)
                    )
                }
                validateInput()
            }
            textAreaQuota.textAreaInput.onTextChanged { text ->
                val isValidInput =
                    viewModel.isValidCashbackQuota(text.trim().toIntSafely())
                if (isValidInput) {
                    clearErrorMessage(binding.textAreaQuota)
                } else {
                    showErrorMessage(
                        binding.textAreaQuota,
                        getString(R.string.error_message_invalid_free_shipping_quota)
                    )
                }
                validateInput()
            }
            textAreaFreeShippingDiscountAmount.textAreaInput.onTextChanged { text ->
                val isValidInput = viewModel.isValidFreeShippingDiscountAmount(text.trim().toIntSafely())
                if (isValidInput) {
                    clearErrorMessage(binding.textAreaFreeShippingDiscountAmount)
                } else {
                    showErrorMessage(binding.textAreaFreeShippingDiscountAmount, getString(R.string.error_message_invalid_min_purchase))
                }
                validateInput()
            }
            textAreaFreeShippingMinimumPurchase.textAreaInput.onTextChanged { text ->
                val freeShippingDiscountAmount = binding.textAreaFreeShippingDiscountAmount.textAreaInput.text.toString().trim().toIntSafely()
                val isValidInput = viewModel.isValidFreeShippingMinimumPurchase(text.trim().toIntSafely(), freeShippingDiscountAmount)
                if (isValidInput) {
                    clearErrorMessage(binding.textAreaFreeShippingMinimumPurchase)
                } else {
                    showErrorMessage(binding.textAreaFreeShippingMinimumPurchase, getString(R.string.error_message_invalid_shipping_fee))
                }
                validateInput()
            }
            textAreaQuotaFreeShipping.textAreaInput.onTextChanged { text ->
                val isValidInput = viewModel.isValidFreeShippingQuota(text.trim().toIntSafely())
                if (isValidInput) {
                    clearErrorMessage(binding.textAreaQuotaFreeShipping)
                } else {
                    showErrorMessage(binding.textAreaQuotaFreeShipping, getString(R.string.error_message_invalid_free_shipping_quota))
                }
                validateInput()
            }
        }
    }

    private fun setupChipsListener() {
        binding.chipCashback.chip_container.setOnClickListener {
            binding.chipCashback.chipType = ChipsUnify.TYPE_SELECTED
            binding.chipFreeShipping.chipType = ChipsUnify.TYPE_NORMAL
            adjustExpenseEstimationConstraint(binding.textAreaQuota.id)
            selectedCouponType = CouponType.CASHBACK
            viewModel.couponTypeChanged(selectedCouponType)
        }

        binding.chipFreeShipping.chip_container.setOnClickListener {
            binding.chipCashback.chipType = ChipsUnify.TYPE_NORMAL
            binding.chipFreeShipping.chipType = ChipsUnify.TYPE_SELECTED
            adjustExpenseEstimationConstraint(binding.textAreaQuotaFreeShipping.id)
            selectedCouponType = CouponType.FREE_SHIPPING
            viewModel.couponTypeChanged(selectedCouponType)
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

        binding.chipDiscountTypeNominal.chip_container.setOnClickListener {
            binding.chipDiscountTypeNominal.chipText = getString(R.string.in_rupiah)
            binding.chipDiscountTypeNominal.chipType = ChipsUnify.TYPE_SELECTED

            binding.chipDiscountTypePercentage.chipText = getString(R.string.mvc_create_tips_subtitle_percentage)
            binding.chipDiscountTypePercentage.chipType = ChipsUnify.TYPE_NORMAL

            selectedDiscountType = DiscountType.NOMINAL
            validateInput()
        }

        binding.chipDiscountTypePercentage.chip_container.setOnClickListener {
            binding.chipDiscountTypeNominal.chipText = getString(R.string.nominal)
            binding.chipDiscountTypeNominal.chipType = ChipsUnify.TYPE_NORMAL

            binding.chipDiscountTypePercentage.chipText = getString(R.string.in_percentage)
            binding.chipDiscountTypePercentage.chipType = ChipsUnify.TYPE_SELECTED

            selectedDiscountType = DiscountType.PERCENTAGE
            validateInput()
        }

        binding.chipMinimumPurchaseNominal.chip_container.setOnClickListener {
            binding.chipMinimumPurchaseNominal.chipText = getString(R.string.in_nominal)
            binding.chipMinimumPurchaseQuantity.chipText = getString(R.string.quantity)

            binding.chipMinimumPurchaseNominal.chipType = ChipsUnify.TYPE_SELECTED
            binding.chipMinimumPurchaseQuantity.chipType = ChipsUnify.TYPE_NORMAL
            binding.chipMinimumPurchaseNothing.chipType = ChipsUnify.TYPE_NORMAL
            selectedMinimumPurchaseType = MinimumPurchaseType.NOMINAL
            validateInput()
        }

        binding.chipMinimumPurchaseQuantity.chip_container.setOnClickListener {
            binding.chipMinimumPurchaseNominal.chipText = getString(R.string.nominal)
            binding.chipMinimumPurchaseQuantity.chipText = getString(R.string.in_quantity)

            binding.chipMinimumPurchaseNominal.chipType = ChipsUnify.TYPE_NORMAL
            binding.chipMinimumPurchaseQuantity.chipType = ChipsUnify.TYPE_SELECTED
            binding.chipMinimumPurchaseNothing.chipType = ChipsUnify.TYPE_NORMAL
            selectedMinimumPurchaseType = MinimumPurchaseType.QUANTITY
            validateInput()
        }

        binding.chipMinimumPurchaseNothing.chip_container.setOnClickListener {
            binding.chipMinimumPurchaseNominal.chipText = getString(R.string.nominal)
            binding.chipMinimumPurchaseQuantity.chipText = getString(R.string.quantity)

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


    private fun clearFreeShippingData() {
        binding.textAreaQuotaFreeShipping.textAreaInput.text = null
        binding.textAreaFreeShippingDiscountAmount.textAreaInput.text = null
        binding.textAreaFreeShippingMinimumPurchase.textAreaInput.text = null
    }

    private fun clearCashbackData() {
        binding.textAreaMinimumDiscount.textAreaInput.text = null
        binding.textAreaQuota.textAreaInput.text = null
        binding.textAreaMinimumPurchase.textAreaInput.text = null
    }

    private fun validateInput() {
        val cashbackDiscountAmount = binding.textAreaMinimumDiscount.textAreaInput.text.toString().trim().toIntSafely()
        val cashbackMinimumPurchase = binding.textAreaMinimumPurchase.textAreaInput.text.toString().trim().toIntSafely()
        val cashbackQuota = binding.textAreaQuota.textAreaInput.text.toString().trim().toIntSafely()

        val freeShippingDiscountAmount = binding.textAreaFreeShippingDiscountAmount.textAreaInput.text.toString().trim().toIntSafely()
        val freeShippingMinimumPurchase = binding.textAreaFreeShippingMinimumPurchase.textAreaInput.text.toString().trim().toIntSafely()
        val freeShippingQuota = binding.textAreaQuotaFreeShipping.textAreaInput.text.toString().trim().toIntSafely()

        viewModel.validateInput(
            selectedCouponType,
            selectedDiscountType,
            selectedMinimumPurchaseType,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )
    }

    private fun showErrorMessage(view: TextAreaUnify, errorMessage : String) {
        view.textAreaMessage = EMPTY_STRING
        view.isError = true
        view.textAreaWrapper.error = errorMessage
    }

    private fun clearErrorMessage(view: TextAreaUnify) {
        view.isError = false
        view.textAreaWrapper.error = EMPTY_STRING
    }

    private fun String.toIntSafely(): Int {
        return try {
            this.toInt()
        } catch (e: Exception) {
            0
        }
    }
}