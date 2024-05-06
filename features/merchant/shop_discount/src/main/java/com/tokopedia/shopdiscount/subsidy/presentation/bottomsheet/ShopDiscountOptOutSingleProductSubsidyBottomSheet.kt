package com.tokopedia.shopdiscount.subsidy.presentation.bottomsheet

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.LayoutBottomSheetShopDiscountDisableEditDeleteSubsidyProductBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountManageProductSubsidyUiModel
import com.tokopedia.shopdiscount.utils.tracker.ShopDiscountTracker
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ShopDiscountOptOutSingleProductSubsidyBottomSheet : BottomSheetUnify() {

    companion object {
        private const val DATA = "data"
        private const val DISMISS_FROM_BOTTOM_SHEET_CLOSE_BUTTON = "x"
        private const val DISMISS_FROM_OVERLAY = "empty"

        fun newInstance(
            data: ShopDiscountManageProductSubsidyUiModel
        ): ShopDiscountOptOutSingleProductSubsidyBottomSheet {
            return ShopDiscountOptOutSingleProductSubsidyBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(DATA, data)
                }
            }
        }
    }

    private var viewBinding by autoClearedNullable<LayoutBottomSheetShopDiscountDisableEditDeleteSubsidyProductBinding>()
    private var onDismissBottomSheetAfterFinishActionListener: ((ShopDiscountManageProductSubsidyUiModel, String) -> Unit)? =
        null
    private val textDescription: Typography?
        get() = viewBinding?.textDescription
    private val buttonOptOutSubsidy: UnifyButton?
        get() = viewBinding?.buttonOptOutSubsidy

    private var data: ShopDiscountManageProductSubsidyUiModel =
        ShopDiscountManageProductSubsidyUiModel()

    @Inject
    lateinit var tracker: ShopDiscountTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        getArgumentsData()
    }

    @SuppressLint("DeprecatedMethod")
    private fun getArgumentsData() {
        data = arguments?.getParcelable(DATA) ?: ShopDiscountManageProductSubsidyUiModel()
    }

    private fun setupDependencyInjection() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextDescription()
        setupButtonOptOutSubsidy()
        sendImpressionOptOutSingleProductSubsidyTracker()
    }

    private fun sendImpressionOptOutSingleProductSubsidyTracker() {
        tracker.sendImpressionOptOutSingleProductSubsidyEvent(
            data.entrySource.value,
            buttonOptOutSubsidy?.isEnabled.orFalse(),
            buttonOptOutSubsidy?.text.toString()
        )
    }

    fun setOnDismissBottomSheetAfterFinishActionListener(listener: (ShopDiscountManageProductSubsidyUiModel, String) -> Unit) {
        onDismissBottomSheetAfterFinishActionListener = listener
    }

    private fun setupButtonOptOutSubsidy() {
        buttonOptOutSubsidy?.setOnClickListener {
            addCurrentProductDetailDataToOptOutList()
            showBottomSheetOptOutReason()
            sendClickOptOutSingleProductSubsidyTracker()
        }
    }

    private fun sendClickOptOutSingleProductSubsidyTracker() {
        tracker.sendClickOptOutSingleProductSubsidyEvent(
            data.entrySource.value,
            buttonOptOutSubsidy?.text.toString(),
            data.selectedProductToOptOut.firstOrNull()?.productId.orEmpty()
        )
    }

    private fun addCurrentProductDetailDataToOptOutList() {
        data.getListProductAbleToOptOutData().firstOrNull()?.let {
            data.addSelectedProductToOptOut(it)
        }
    }

    private fun showBottomSheetOptOutReason() {
        val bottomSheet = ShopDiscountSubsidyOptOutReasonBottomSheet.newInstance(data)
        bottomSheet.setOnDismissBottomSheetAfterFinishActionListener { data, optOutSuccessMessage ->
            onDismissBottomSheetAfterFinishActionListener?.invoke(data, optOutSuccessMessage)
            dismiss()
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun setupTextDescription() {
        textDescription?.text = when (data.mode) {
            ShopDiscountManageDiscountMode.UPDATE -> getString(R.string.sd_subsidy_disable_edit_product_desc)
            ShopDiscountManageDiscountMode.DELETE -> getString(R.string.sd_subsidy_disable_delete_product_desc)
            else -> ""
        }
    }

    private fun setupBottomSheet() {
        viewBinding = LayoutBottomSheetShopDiscountDisableEditDeleteSubsidyProductBinding.inflate(
            LayoutInflater.from(context)
        ).apply {
            val bottomSheetTitle = getBottomSheetTitle()
            setTitle(bottomSheetTitle)
            setChild(this.root)
            setCloseClickListener {
                sendDismissOptOutSingleProductBottomSheetTracker(DISMISS_FROM_BOTTOM_SHEET_CLOSE_BUTTON)
                dismiss()
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        sendDismissOptOutSingleProductBottomSheetTracker(DISMISS_FROM_OVERLAY)
        super.onCancel(dialog)
    }

    private fun sendDismissOptOutSingleProductBottomSheetTracker(dismissSource: String) {
        tracker.sendDismissOptOutSingleProductBottomSheetEvent(
            data.entrySource.value,
            buttonOptOutSubsidy?.isEnabled.orFalse(),
            buttonOptOutSubsidy?.text.toString(),
            dismissSource
        )
    }

    private fun getBottomSheetTitle(): String {
        return when (data.mode) {
            ShopDiscountManageDiscountMode.UPDATE -> getString(R.string.sd_subsidy_single_product_opt_out_edit_mode_title)
            ShopDiscountManageDiscountMode.DELETE -> getString(R.string.sd_subsidy_single_product_opt_out_delete_mode_title)
            else -> ""
        }
    }

}
