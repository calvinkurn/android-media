package com.tokopedia.mvc.presentation.bottomsheet.changequota

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.mvc.databinding.SmvcBottomsheetChangeQuotaBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.presentation.bottomsheet.ExpenseEstimationBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.changequota.model.UpdateQuotaUiState
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetChangeQuotaFormBinding
import com.tokopedia.mvc.databinding.SmvcBottomsheetChangeQuotaShimmerBinding
import com.tokopedia.mvc.presentation.bottomsheet.changequota.model.UpdateQuotaModel
import com.tokopedia.mvc.presentation.bottomsheet.changequota.model.UpdateQuotaEffect
import com.tokopedia.mvc.util.constant.ChangeQuotaConstant.APPLY_ALL_PERIOD_COUPON
import com.tokopedia.mvc.util.constant.ChangeQuotaConstant.APPLY_ONLY_PERIOD_COUPON
import com.tokopedia.mvc.util.constant.ChangeQuotaConstant.NOT_YET_APPLY_PERIOD_COUPON
import com.tokopedia.mvc.util.tracker.ChangeQuotaVoucherTracker
import javax.inject.Inject

class ChangeQuotaBottomSheet : BottomSheetUnify() {

    companion object {
        private const val ARG_ID_VOUCHER = "ARG_ID_VOUCHER"
        private const val TITLE_BOTTOM_SHEET = "ARG_TITLE"
        private const val CLEAR_OPTIONS = -1
        private val TAG = ChangeQuotaBottomSheet::class.java.simpleName

        @JvmStatic
        fun newInstance(
            title: String,
            idVoucher: Long
        ): ChangeQuotaBottomSheet {
            val args = Bundle()
            args.putString(TITLE_BOTTOM_SHEET, title)
            args.putLong(ARG_ID_VOUCHER, idVoucher)
            val bottomSheet = ChangeQuotaBottomSheet().apply {
                arguments = args
            }
            return bottomSheet
        }
    }

    private val idVoucher by lazy {
        arguments?.getLong(ARG_ID_VOUCHER).orZero()
    }
    private val title by lazy { arguments?.getString(TITLE_BOTTOM_SHEET).orEmpty() }

    private var binding by autoClearedNullable<SmvcBottomsheetChangeQuotaBinding>()

    private var onSuccessListener: (String) -> Unit =
        {}
    private var onFailedListener: (String) -> Unit =
        {}

    @Inject
    lateinit var tracker: ChangeQuotaVoucherTracker

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy {
        viewModelProvider.get(
            ChangeQuotaBottomSheetViewModel::class.java
        )
    }

    private var formLayout: SmvcBottomsheetChangeQuotaFormBinding? = null
    private var loadingLayout: SmvcBottomsheetChangeQuotaShimmerBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    private fun setupDependencyInjection() {
        DaggerMerchantVoucherCreationComponent.builder()
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

    fun setOnSuccessUpdateQuotaListener(onSuccess: (String) -> Unit) {
        this.onSuccessListener = onSuccess
    }

    fun setOnFailedQuotaListener(onFailed: (String) -> Unit) {
        this.onFailedListener = onFailed
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding =
            SmvcBottomsheetChangeQuotaBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        formLayout = binding?.layoutFormChangeQuota
        loadingLayout = binding?.shimmer
        setTitle(title)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservableUiEffect()
        setupView()
    }

    fun setupView() {
        viewModel.getVoucherDetail(idVoucher)
        setViewListener()
    }

    private fun setViewListener() {
        setOnDismissListener()
        binding?.run {
            formLayout?.textFieldDiscountQuota?.editText?.afterTextChanged {
                viewModel.isValidInput(it.toLongOrZero())
            }

            formLayout?.labelSpendingEstimation?.apply {
                iconInfo?.setOnClickListener {
                    ExpenseEstimationBottomSheet.newInstance().show(childFragmentManager)
                }
            }

            formLayout?.btnMvcSave?.run {
                setOnClickListener {
                    this@ChangeQuotaBottomSheet.isCancelable = false
                    viewModel.changeQuota(
                        formLayout?.textFieldDiscountQuota?.editText?.text.toString().toIntOrZero()
                    )
                    isLoading = true
                    clearDismissListener()
                    tracker.sendClickOkEvent(
                        createLabelTracker(
                            idVoucher,
                            viewModel.getVoucherStatus()
                        )
                    )
                }
            }

            formLayout?.btnMvcReset?.run {
                setOnClickListener {
                    // TODO: uncomment this when BE ready to implement, radiosMultipleCoupon always gone for now
                    //setOptionsPickerOnClickRestartButton()
                    viewModel.restartVoucher()
                    tracker.sendClickResetEvent(
                        createLabelTracker(
                            idVoucher,
                            viewModel.getVoucherStatus()
                        )
                    )
                }
            }
        }
    }

    // TODO: uncomment this when BE ready to implement, radiosMultipleCoupon always gone for now
    /*private fun setOptionsPickerOnClickRestartButton(){
        clearClickOptionsListener()
        formLayout?.radiosMultipleCoupon?.clearCheck()
        setClickOptionsListener()
    }

    private fun sendTrackerOptions(optionsId: Int){
        if (optionsId != CLEAR_OPTIONS) {
            tracker.sendClickOptionsEvent(
                createLabelTrackerOptions(
                    idVoucher,
                    viewModel.getVoucherStatus(),
                    getOptionsName(optionsId)
                )
            )
        }
    }

    private fun setClickOptionsListener(){
        formLayout?.radiosMultipleCoupon?.setOnCheckedChangeListener { _, optionsId ->
            viewModel.setOptionsApplyPeriodCoupon(getPositionOptions(optionsId))
            sendTrackerOptions(optionsId)
        }
    }

    private fun clearClickOptionsListener(){
        formLayout?.radiosMultipleCoupon?.setOnCheckedChangeListener(null)
    }

    private fun getOptionsName(optionsId: Int): String {
        return when (optionsId) {
            R.id.radio_just_one_coupon -> getString(R.string.smvc_just_for_this_coupon)
            R.id.radio_apply_all_coupon -> getString(R.string.smvc_this_coupon_and_other)
            else -> getString(R.string.smvc_just_for_this_coupon)
        }
    }

    private fun createLabelTrackerOptions(
        voucherId: Long,
        voucherStatus: String,
        optionsName: String
    ): String {
        return getString(
            R.string.smvc_tracker_change_quota_lable_options,
            voucherId.toString(),
            voucherStatus,
            optionsName
        )
    }

    private fun getPositionOptions(optionsId: Int): Int {
        return when (optionsId) {
            R.id.radio_just_one_coupon -> APPLY_ONLY_PERIOD_COUPON
            R.id.radio_apply_all_coupon -> APPLY_ALL_PERIOD_COUPON
            else -> NOT_YET_APPLY_PERIOD_COUPON
        }
    }*/

    private fun setOnDismissListener() {
        this.setOnDismissListener {
            tracker.sendClickCloseEvent(createLabelTracker(idVoucher, viewModel.getVoucherStatus()))
        }
    }

    private fun clearDismissListener() {
        this.setOnDismissListener { }
    }

    private fun setObservableUiEffect() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.inputQuotaValidation.collect {
                handlingUiState(it)
            }
        }

        viewModel.changeQuotaUiModel.observe(viewLifecycleOwner) { uiModel ->
            when (uiModel) {
                is UpdateQuotaEffect.SuccessToGetDetailVoucher -> {
                    loadingLayout?.root?.gone()
                    formLayout?.root?.visible()
                    handlingVoucherUi(uiModel.updateQuotaModel)
                }
                is UpdateQuotaEffect.FailToGetDetailVoucher -> {
                    dismiss()
                    onFailedListener(uiModel.throwable.message.orEmpty())
                }
                is UpdateQuotaEffect.SuccessToUpdate -> {
                    dismiss()
                    val message = setMessageSuccess(uiModel)
                    onSuccessListener(message)
                }
                is UpdateQuotaEffect.FailToUpdate -> {
                    dismiss()
                    val message = getString(R.string.smvc_failed_change_quota, uiModel.nameVoucher)
                    onFailedListener(message)
                }
            }
        }
    }

    private fun setMessageSuccess(successUpdate: UpdateQuotaEffect.SuccessToUpdate): String {
        return if (successUpdate.isApplyToAllPeriodCoupon) {
            getString(R.string.smvc_success_change_quota_multi_period, successUpdate.nameVoucher)
        } else {
            getString(R.string.smvc_success_change_quota_single, successUpdate.nameVoucher)
        }
    }

    private fun handlingVoucherUi(voucher: UpdateQuotaModel) {
        binding?.layoutFormChangeQuota?.run {
            val estimationSpending = viewModel.calculateEstimation(
                voucher.maxBenefit.orZero(),
                voucher.currentQuota.orZero()
            )
            labelSpendingEstimation.spendingEstimationText =
                estimationSpending.getCurrencyFormatted()
            textFieldDiscountQuota.editText.setText(voucher.currentQuota.orZero().toString())

            // TODO: uncomment this when BE ready to implement, radiosMultipleCoupon always gone for now
            /*if (voucher.isMultiPeriod.orFalse()) {
                radiosMultipleCoupon.visible()
            } else {
                radiosMultipleCoupon.gone()
            }*/
        }
    }

    private fun handlingUiState(state: UpdateQuotaUiState) {
        val messageError = if (state.isInputOnUnderMinimumReq) getString(
            R.string.smvc_error_min_quota,
            state.quotaReq
        ) else getString(R.string.smvc_error_max_quota, state.quotaReq)
        binding?.layoutFormChangeQuota?.run {
            labelSpendingEstimation.spendingEstimationText =
                state.estimationSpending.getCurrencyFormatted()
            textFieldDiscountQuota.isInputError = !state.isValidInput
            if (!state.isValidInput) {
                textFieldDiscountQuota.setMessage(messageError)
            } else {
                textFieldDiscountQuota.setMessage("")
            }
            btnMvcSave.isEnabled = state.isValidInput && state.isSelectedOptions
        }
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            show(it, TAG)
        }
    }


    private fun createLabelTracker(voucherId: Long, voucherStatus: String): String {
        return getString(
            R.string.smvc_tracker_change_quota_lable,
            voucherId.toString(),
            voucherStatus
        )
    }


}
