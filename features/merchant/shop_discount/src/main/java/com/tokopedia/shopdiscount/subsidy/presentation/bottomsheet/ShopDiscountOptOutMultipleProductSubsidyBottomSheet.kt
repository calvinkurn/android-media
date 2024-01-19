package com.tokopedia.shopdiscount.subsidy.presentation.bottomsheet

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shopdiscount.databinding.LayoutBottomSheetShopDiscountChooseProductSubsidyToOptOutBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.subsidy.model.mapper.ShopDiscountProductSubsidyMapper
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountManageProductSubsidyUiModel
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountProductSubsidyUiModel
import com.tokopedia.shopdiscount.subsidy.presentation.adapter.ShopDiscountProductSubsidyAdapter
import com.tokopedia.shopdiscount.subsidy.presentation.adapter.viewholder.ShopDiscountSubsidyProductItemViewHolder
import com.tokopedia.shopdiscount.utils.tracker.ShopDiscountTracker
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject


class ShopDiscountOptOutMultipleProductSubsidyBottomSheet :
    BottomSheetUnify(),
    ShopDiscountSubsidyProductItemViewHolder.Listener {

    companion object {
        private const val DATA = "data"
        private const val DISMISS_FROM_BOTTOM_SHEET_CLOSE_BUTTON = "x"
        private const val DISMISS_FROM_OVERLAY = "empty"
        fun newInstance(
            data: ShopDiscountManageProductSubsidyUiModel,
        ): ShopDiscountOptOutMultipleProductSubsidyBottomSheet {
            return ShopDiscountOptOutMultipleProductSubsidyBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(DATA, data)
                }
            }
        }
    }

    private var viewBinding by autoClearedNullable<LayoutBottomSheetShopDiscountChooseProductSubsidyToOptOutBinding>()
    private var onDismissBottomSheetAfterFinishActionListener: ((String, List<String>, String) -> Unit)? =
        null
    private val rv: RecyclerView?
        get() = viewBinding?.rvSubsidyProductList
    private val checkboxSelectAllProduct: CheckboxUnify?
        get() = viewBinding?.checkboxSelectAllProduct
    private val textMultipleSelectProduct: Typography?
        get() = viewBinding?.textMultipleSelectProduct
    private val buttonOptOut: UnifyButton?
        get() = viewBinding?.buttonOptOut
    private var data: ShopDiscountManageProductSubsidyUiModel =
        ShopDiscountManageProductSubsidyUiModel()
    private val subsidyProductAdapter: ShopDiscountProductSubsidyAdapter by lazy {
        ShopDiscountProductSubsidyAdapter(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var tracker: ShopDiscountTracker
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        getArgumentsData()
    }

    @SuppressLint("DeprecatedMethod")
    private fun getArgumentsData() {
        arguments?.let {
            data = it.getParcelable(DATA) ?: ShopDiscountManageProductSubsidyUiModel()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupDependencyInjection() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheetTitleView()
        setupRecyclerView()
        setupCheckboxSelectAllProduct()
        setupTextMultipleSelectProduct()
        setProductSubsidyData()
        configButtonOptOut()
        sendImpressionOptOutMultipleProductSubsidyTracker()
    }

    private fun sendImpressionOptOutMultipleProductSubsidyTracker() {
        tracker.sendImpressionOptOutMultipleProductSubsidyEvent(
            data.entrySource.value,
            buttonOptOut?.isEnabled.orFalse(),
            buttonOptOut?.text.toString(),
            subsidyProductAdapter.itemCount
        )
    }

    fun setOnDismissBottomSheetAfterFinishActionListener(listener: (String, List<String>, String) -> Unit) {
        onDismissBottomSheetAfterFinishActionListener = listener
    }

    private fun setupTextMultipleSelectProduct() {
        textMultipleSelectProduct?.apply {
            text = if (subsidyProductAdapter.getTotalSelectedProduct() == Int.ZERO) {
                checkboxSelectAllProduct?.isChecked = false
                setWeight(Typography.BOLD)
                getString(R.string.sd_subsidy_multiple_product_opt_out_checkbox_default_label)
            } else {
                setWeight(Typography.REGULAR)
                checkboxSelectAllProduct?.isChecked = true
                val isIndeterminate =
                    subsidyProductAdapter.getTotalSelectedProduct() != subsidyProductAdapter.itemCount
                if (checkboxSelectAllProduct?.getIndeterminate() != isIndeterminate) {
                    checkboxSelectAllProduct?.setIndeterminate(isIndeterminate)
                }
                getString(
                    R.string.sd_subsidy_multiple_product_opt_out_checkbox_selected_label_format,
                    subsidyProductAdapter.getTotalSelectedProduct(),
                    subsidyProductAdapter.itemCount
                )
            }
        }
    }

    private fun setupCheckboxSelectAllProduct() {
        checkboxSelectAllProduct?.apply {
            setOnClickListener {
                subsidyProductAdapter.updateAllSelectedProduct(isChecked)
                setupTextMultipleSelectProduct()
                configButtonOptOut()
            }
        }
    }

    private fun setProductSubsidyData() {
        subsidyProductAdapter.setListProductSubsidy(ShopDiscountProductSubsidyMapper.map(
            data.getListProductAbleToOptOutData()
        ))
    }

    private fun configButtonOptOut() {
        buttonOptOut?.apply {
            if (subsidyProductAdapter.getTotalSelectedProduct() == Int.ZERO) {
                if (data.hasNonSubsidyProduct()) {
                    isEnabled = true
                    setOnClickListener {
                        sendClickCtaOptOutVariantTracker()
                        onDismissBottomSheetAfterFinishActionListener?.invoke(
                            data.mode,
                            data.getListProductParentId(),
                            ""
                        )
                        dismiss()
                    }
                    text = if (data.mode == ShopDiscountManageDiscountMode.UPDATE) {
                        "Ubah Produk Non-subsidi"
                    } else {
                        "Hapus Produk Non-subsidi"
                    }
                } else {
                    isEnabled = false
                    text = "Keluar dari Subsidi"
                }
            } else {
                isEnabled = true
                text = "Keluar dari Subsidi"
                setOnClickListener {
                    sendClickCtaOptOutVariantTracker()
                    addSelectedProductToOptOutList()
                    showBottomSheetOptOutReason(data)
                }
            }
        }
    }

    private fun sendClickCtaOptOutVariantTracker() {
        tracker.sendClickCtaOptOutVariantEvent(
            data.entrySource.value,
            subsidyProductAdapter.itemCount,
            subsidyProductAdapter.getSelectedProduct().size,
            buttonOptOut?.text.toString(),
            subsidyProductAdapter.getSelectedProduct().map { it.productDetailData.productId }
        )
    }

    private fun addSelectedProductToOptOutList() {
        subsidyProductAdapter.getSelectedProduct().forEach {
            data.addSelectedProductToOptOut(it.productDetailData)
        }
    }

    private fun showBottomSheetOptOutReason(data: ShopDiscountManageProductSubsidyUiModel) {
        val bottomSheet = ShopDiscountSubsidyOptOutReasonBottomSheet.newInstance(data)
        bottomSheet.setOnDismissBottomSheetAfterFinishActionListener { mode, listProductId, optOutSuccessMessage ->
            onDismissBottomSheetAfterFinishActionListener?.invoke(
                mode,
                listProductId,
                optOutSuccessMessage
            )
            dismiss()
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun setupBottomSheetTitleView() {
        bottomSheetTitle.isSingleLine = false
    }

    private fun setupRecyclerView() {
        rv?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = subsidyProductAdapter
            itemAnimator = null
        }
    }

    private fun setupBottomSheet() {
        viewBinding = LayoutBottomSheetShopDiscountChooseProductSubsidyToOptOutBinding.inflate(
            LayoutInflater.from(context)
        ).apply {
            setTitle(getBottomSheetTitle())
            setChild(this.root)
            setCloseClickListener {
                sendDismissOptOutMultipleProductBottomSheetTracker(DISMISS_FROM_BOTTOM_SHEET_CLOSE_BUTTON)
                dismiss()
            }
        }
    }

    private fun sendDismissOptOutMultipleProductBottomSheetTracker(dismissSource: String) {
        tracker.sendDismissOptOutMultipleProductBottomSheetEvent(
            data.entrySource.value,
            buttonOptOut?.isEnabled.orFalse(),
            buttonOptOut?.text.toString(),
            dismissSource
        )
    }

    override fun onCancel(dialog: DialogInterface) {
        sendDismissOptOutMultipleProductBottomSheetTracker(DISMISS_FROM_OVERLAY)
        super.onCancel(dialog)
    }

    private fun getBottomSheetTitle(): String {
        return when (data.mode) {
            ShopDiscountManageDiscountMode.UPDATE -> getString(R.string.sd_subsidy_multiple_product_opt_out_edit_mode_title)
            ShopDiscountManageDiscountMode.DELETE -> getString(R.string.sd_subsidy_multiple_product_opt_out_delete_mode_title)
            else -> {
                ""
            }
        }
    }

    override fun onClickCheckbox(isChecked: Boolean, uiModel: ShopDiscountProductSubsidyUiModel) {
        subsidyProductAdapter.setProductSelectedStatus(isChecked, uiModel)
        setupTextMultipleSelectProduct()
        configButtonOptOut()
    }

}
