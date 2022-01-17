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
import com.tokopedia.utils.lifecycle.autoCleared
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.extension.onTextChanged
import com.tokopedia.vouchercreation.databinding.FragmentCouponSettingBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import com.tokopedia.vouchercreation.product.create.domain.entity.DiscountType
import com.tokopedia.vouchercreation.product.create.domain.entity.MinimumPurchaseType
import com.tokopedia.vouchercreation.product.create.view.viewmodel.CouponSettingViewModel
import javax.inject.Inject


class CouponSettingFragment : BaseDaggerFragment() {

    companion object {
        private const val SCREEN_NAME = "Coupon Setting Page"
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
    }

    private fun observeInputValidationResult() {
        viewModel.areInputValid.observe(viewLifecycleOwner, { validationResult ->
            when (validationResult) {
                CouponSettingViewModel.ValidationResult.Cashback.DiscountTypeNotSelected -> TODO()
                CouponSettingViewModel.ValidationResult.Cashback.InvalidDiscountAmount -> TODO()
                CouponSettingViewModel.ValidationResult.Cashback.InvalidMinimumPurchase -> TODO()
                CouponSettingViewModel.ValidationResult.Cashback.InvalidQuota -> TODO()
                CouponSettingViewModel.ValidationResult.Cashback.MinimumPurchaseTypeNotSelected -> TODO()
                CouponSettingViewModel.ValidationResult.Cashback.Valid -> TODO()
                CouponSettingViewModel.ValidationResult.CouponTypeNotSelected -> TODO()
                CouponSettingViewModel.ValidationResult.FreeShipping.InvalidFreeShippingAmount -> TODO()
                CouponSettingViewModel.ValidationResult.FreeShipping.InvalidMinimumPurchase -> TODO()
                CouponSettingViewModel.ValidationResult.FreeShipping.InvalidQuota -> TODO()
                CouponSettingViewModel.ValidationResult.FreeShipping.Valid -> TODO()
                CouponSettingViewModel.ValidationResult.Valid -> TODO()
            }
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
            textAreaMinimumDiscount.textAreaInput.onTextChanged { validateInput() }
            textAreaQuota.textAreaInput.onTextChanged { validateInput() }
            textAreaMinimumPurchase.textAreaInput.onTextChanged { validateInput() }
            textAreaQuotaFreeShipping.textAreaInput.onTextChanged { validateInput() }
            textAreaFreeShippingDiscountAmount.textAreaInput.onTextChanged { validateInput() }
            textAreaFreeShippingMinimumPurchase.textAreaInput.onTextChanged { validateInput() }
        }
    }

    private fun setupChipsListener() {
        binding.chipCashback.chip_container.setOnClickListener {
            binding.chipCashback.chipType = ChipsUnify.TYPE_SELECTED
            binding.chipFreeShipping.chipType = ChipsUnify.TYPE_NORMAL
            adjustExpenseEstimationConstraint(binding.textAreaQuota.id)
            selectedCouponType = CouponType.CASHBACK
            validateInput()
        }

        binding.chipFreeShipping.chip_container.setOnClickListener {
            binding.chipCashback.chipType = ChipsUnify.TYPE_NORMAL
            binding.chipFreeShipping.chipType = ChipsUnify.TYPE_SELECTED
            adjustExpenseEstimationConstraint(binding.textAreaQuotaFreeShipping.id)
            selectedCouponType = CouponType.FREE_SHIPPING
            validateInput()
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
            binding.chipDiscountTypeNominal.chipType = ChipsUnify.TYPE_SELECTED
            binding.chipDiscountTypePercentage.chipType = ChipsUnify.TYPE_NORMAL
            selectedDiscountType = DiscountType.NOMINAL
            validateInput()
        }

        binding.chipDiscountTypePercentage.chip_container.setOnClickListener {
            binding.chipDiscountTypeNominal.chipType = ChipsUnify.TYPE_NORMAL
            binding.chipDiscountTypePercentage.chipType = ChipsUnify.TYPE_SELECTED
            selectedDiscountType = DiscountType.PERCENTAGE
            validateInput()
        }

        binding.chipMinimumPurchaseNominal.chip_container.setOnClickListener {
            binding.chipMinimumPurchaseNominal.chipType = ChipsUnify.TYPE_SELECTED
            binding.chipMinimumPurchaseQuantity.chipType = ChipsUnify.TYPE_NORMAL
            binding.chipMinimumPurchaseNothing.chipType = ChipsUnify.TYPE_NORMAL
            selectedMinimumPurchaseType = MinimumPurchaseType.NOMINAL
            validateInput()
        }

        binding.chipMinimumPurchaseQuantity.chip_container.setOnClickListener {
            binding.chipMinimumPurchaseNominal.chipType = ChipsUnify.TYPE_NORMAL
            binding.chipMinimumPurchaseQuantity.chipType = ChipsUnify.TYPE_SELECTED
            binding.chipMinimumPurchaseNothing.chipType = ChipsUnify.TYPE_NORMAL
            selectedMinimumPurchaseType = MinimumPurchaseType.QUANTITY
            validateInput()
        }

        binding.chipMinimumPurchaseNothing.chip_container.setOnClickListener {
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

    private fun validateInput() {
        val cashbackDiscountAmount = binding.textAreaMinimumDiscount.textAreaInput.text.toString().trim().toInt()
        val cashbackMinimumPurchase = binding.textAreaMinimumPurchase.textAreaInput.text.toString().trim().toInt()
        val cashbackQuota = binding.textAreaQuota.textAreaInput.text.toString().trim().toInt()

        val freeShippingDiscountAmount = binding.textAreaFreeShippingDiscountAmount.textAreaInput.text.toString().trim().toInt()
        val freeShippingMinimumPurchase = binding.textAreaFreeShippingMinimumPurchase.textAreaInput.text.toString().trim().toInt()
        val freeShippingQuota = binding.textAreaQuotaFreeShipping.textAreaInput.text.toString().trim().toInt()

        viewModel.validateInput(selectedCouponType, selectedDiscountType, selectedMinimumPurchaseType, cashbackDiscountAmount, cashbackMinimumPurchase, cashbackQuota, freeShippingDiscountAmount, freeShippingMinimumPurchase, freeShippingQuota)
    }
}