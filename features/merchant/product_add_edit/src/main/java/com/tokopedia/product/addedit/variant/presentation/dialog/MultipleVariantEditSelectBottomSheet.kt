package com.tokopedia.product.addedit.variant.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tracking.ProductAddVariantDetailTracking
import com.tokopedia.product.addedit.tracking.ProductEditVariantDetailTracking
import com.tokopedia.product.addedit.variant.presentation.adapter.MultipleVariantEditSelectAdapter
import com.tokopedia.product.addedit.variant.presentation.model.MultipleVariantEditInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import java.math.BigInteger

class MultipleVariantEditSelectBottomSheet(
    private val multipleVariantEditListener: MultipleVariantEditListener? = null,
    private val couldShowMultiLocationTicker: Boolean = false
): BottomSheetUnify(), MultipleVariantEditListener,
        MultipleVariantEditSelectAdapter.OnSelectionsDataListener {

    companion object {
        const val TAG = "Tag Multiple Variant Edit Select"
    }

    private var contentView: View? = null
    private var checkboxSelectAll: CheckboxUnify? = null
    private var recyclerViewVariantCheck: RecyclerView? = null
    private var buttonNext: UnifyButton? = null

    private var selectAdapter: MultipleVariantEditSelectAdapter? = null
    private var enableEditSku = true
    private var enableEditPrice = true
    private var trackerShopId = ""
    private var trackerIsEditMode = false

    init {
        selectAdapter = MultipleVariantEditSelectAdapter()
        selectAdapter?.setOnSelectionsDataListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onMultipleEditInputFinished(multipleVariantEditInputModel: MultipleVariantEditInputModel) {
        selectAdapter?.getSelectedData()?.let {
            multipleVariantEditInputModel.selection = it
            multipleVariantEditListener?.onMultipleEditInputFinished(multipleVariantEditInputModel)
        }
    }

    override fun onMultipleEditInputValidatePrice(price: BigInteger): String {
        return multipleVariantEditListener?.onMultipleEditInputValidatePrice(price).orEmpty()
    }

    override fun onMultipleEditInputValidateStock(stock: BigInteger): String {
        return multipleVariantEditListener?.onMultipleEditInputValidateStock(stock).orEmpty()
    }

    override fun onMultipleEditInputValidateWeight(weight: BigInteger): String {
        return multipleVariantEditListener?.onMultipleEditInputValidateWeight(weight).orEmpty()
    }

    override fun onSelectionsDataChanged(selectedCount: Int) {
        val buttonNextText = getString(com.tokopedia.product.addedit.R.string.action_variant_next)
        contentView?.apply {
            if (selectedCount > 0) {
                val numberedText = "$buttonNextText ($selectedCount)"
                buttonNext?.text = numberedText
                buttonNext?.isEnabled = true
            } else {
                buttonNext?.text = buttonNextText
                buttonNext?.isEnabled = false
                checkboxSelectAll?.isSelected = false
            }
        }

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

    private fun initChildLayout() {
        setTitle(getString(R.string.label_variant_multiple_select_bottom_sheet_title))
        contentView = View.inflate(context,
                R.layout.add_edit_product_multiple_variant_edit_select_bottom_sheet_content, null)
        checkboxSelectAll = contentView?.findViewById(R.id.checkboxSelectAll)
        recyclerViewVariantCheck = contentView?.findViewById(R.id.recyclerViewVariantCheck)
        buttonNext = contentView?.findViewById(R.id.buttonNext)

        recyclerViewVariantCheck?.apply {
            setHasFixedSize(true)
            adapter = selectAdapter
            layoutManager = LinearLayoutManager(context)
        }
        buttonNext?.setOnClickListener {
            dismiss()
            val multipleVariantEditSelectBottomSheet =
                    MultipleVariantEditInputBottomSheet(enableEditSku, enableEditPrice, couldShowMultiLocationTicker, this)
            multipleVariantEditSelectBottomSheet.isKeyboardOverlap = false
            multipleVariantEditSelectBottomSheet.setTrackerShopId(trackerShopId)
            multipleVariantEditSelectBottomSheet.setTrackerIsEditMode(trackerIsEditMode)
            multipleVariantEditSelectBottomSheet.show(fragmentManager)
            sendTrackerContinueManageAllData()
        }
        checkboxSelectAll?.setOnClickListener {
            val isSelected = (it as CheckboxUnify).isChecked
            selectAdapter?.setAllDataSelected(isSelected)
            sendTrackerSelectManageAllData()
        }
        setChild(contentView)
        clearContentPadding = true
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