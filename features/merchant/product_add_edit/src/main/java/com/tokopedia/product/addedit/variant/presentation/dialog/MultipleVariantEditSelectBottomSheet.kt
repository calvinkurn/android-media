package com.tokopedia.product.addedit.variant.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tracking.ProductAddVariantDetailTracking
import com.tokopedia.product.addedit.tracking.ProductEditVariantDetailTracking
import com.tokopedia.product.addedit.variant.presentation.adapter.MultipleVariantEditSelectAdapter
import com.tokopedia.product.addedit.variant.presentation.model.MultipleVariantEditInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import kotlinx.android.synthetic.main.add_edit_product_multiple_variant_edit_select_bottom_sheet_content.view.*
import java.math.BigInteger

class MultipleVariantEditSelectBottomSheet(
        private val multipleVariantEditListener: MultipleVariantEditListener
): BottomSheetUnify(), MultipleVariantEditInputBottomSheet.MultipleVariantEditInputListener {

    companion object {
        const val TAG = "Tag Multiple Variant Edit Select"
    }

    private var contentView: View? = null
    private var selectAdapter: MultipleVariantEditSelectAdapter? = null
    private var enableEditSku = true
    private var enableEditPrice = true
    private var trackerShopId = ""
    private var trackerIsEditMode = false

    interface MultipleVariantEditListener {
        fun onMultipleEditFinished(multipleVariantEditInputModel: MultipleVariantEditInputModel)
        fun onMultipleEditInputValidatePrice(price: BigInteger): String
        fun onMultipleEditInputValidateStock(stock: BigInteger): String
    }

    init {
        selectAdapter = MultipleVariantEditSelectAdapter()
        setBehaviorAsKnob()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        removeContainerPadding()
        addMarginTitle()
    }

    override fun onMultipleEditInputFinished(multipleVariantEditInputModel: MultipleVariantEditInputModel) {
        selectAdapter?.getSelectedData()?.let {
            multipleVariantEditInputModel.selection = it
            multipleVariantEditListener.onMultipleEditFinished(multipleVariantEditInputModel)
        }
    }

    override fun onMultipleEditInputValidatePrice(price: BigInteger): String {
        return multipleVariantEditListener.onMultipleEditInputValidatePrice(price)
    }

    override fun onMultipleEditInputValidateStock(stock: BigInteger): String {
        return multipleVariantEditListener.onMultipleEditInputValidateStock(stock)
    }

    fun setData(items: VariantInputModel?) {
        items?.run {
            selectAdapter?.setData(this)
        }
    }

    fun setEnableEditSku(enabled: Boolean) {
        enableEditSku = enabled
    }

    fun setEnableEditPrice(enabled: Boolean) {
        enableEditPrice = enabled
    }

    fun setTrackerShopId(shopId: String) {
        trackerShopId = shopId
    }

    fun setTrackerIsEditMode(isEditMode: Boolean) {
        trackerIsEditMode = isEditMode
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , MultipleVariantEditInputBottomSheet.TAG)
        }
    }

    private fun setBehaviorAsKnob() {
        showCloseIcon = false
        showKnob = true
    }

    private fun removeContainerPadding() {
        val padding = resources.getDimensionPixelSize(R.dimen.tooltip_padding)
        val paddingTop = resources.getDimensionPixelSize(R.dimen.tooltip_close_margin)
        bottomSheetWrapper.setPadding(padding, paddingTop, padding, padding)
    }

    private fun addMarginTitle() {
        val topMargin = resources.getDimensionPixelSize(R.dimen.spacing_lvl3)
        val horizontalMargin = resources.getDimensionPixelSize(R.dimen.tooltip_close_margin)
        (bottomSheetTitle.layoutParams as RelativeLayout.LayoutParams).apply {
            setMargins(horizontalMargin, topMargin, horizontalMargin, 0)
            addRule(RelativeLayout.CENTER_VERTICAL)
        }
    }

    private fun initChildLayout() {
        setTitle(getString(R.string.label_variant_multiple_select_bottom_sheet_title))
        contentView = View.inflate(context,
                R.layout.add_edit_product_multiple_variant_edit_select_bottom_sheet_content, null)
        contentView?.recyclerViewVariantCheck?.apply {
            setHasFixedSize(true)
            adapter = selectAdapter
            layoutManager = LinearLayoutManager(context)
        }
        contentView?.buttonNext?.setOnClickListener {
            dismiss()
            val multipleVariantEditSelectBottomSheet =
                    MultipleVariantEditInputBottomSheet(enableEditSku, enableEditPrice, this)
            multipleVariantEditSelectBottomSheet.setTrackerShopId(trackerShopId)
            multipleVariantEditSelectBottomSheet.setTrackerIsEditMode(trackerIsEditMode)
            multipleVariantEditSelectBottomSheet.show(fragmentManager)
            sendTrackerContinueManageAllData()
        }
        contentView?.checkboxSelectAll?.setOnClickListener {
            val isSelected = (it as CheckboxUnify).isChecked
            selectAdapter?.setAllDataSelected(isSelected)
            sendTrackerSelectManageAllData()
        }
        setChild(contentView)
    }

    private fun sendTrackerContinueManageAllData() {
        if (trackerIsEditMode) {
            ProductEditVariantDetailTracking.continueManageAll(trackerShopId)
        } else {
            ProductAddVariantDetailTracking.continueManageAll(trackerShopId)
        }
    }

    private fun sendTrackerSelectManageAllData() {
        if (trackerIsEditMode) {
            ProductEditVariantDetailTracking.selectManageAll(trackerShopId)
        } else {
            ProductAddVariantDetailTracking.selectManageAll(trackerShopId)
        }
    }
}