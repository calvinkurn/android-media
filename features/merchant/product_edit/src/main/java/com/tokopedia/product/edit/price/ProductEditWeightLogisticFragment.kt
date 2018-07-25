package com.tokopedia.product.edit.price

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.util.ProductEditOptionMenuAdapter
import com.tokopedia.product.edit.util.ProductEditOptionMenuBottomSheets
import com.tokopedia.product.edit.util.ProductEditPreOrderTimeType
import com.tokopedia.product.edit.util.ProductEditWeightType
import kotlinx.android.synthetic.main.fragment_product_edit_weightlogistic.*

class ProductEditWeightLogisticFragment : Fragment() {

    @ProductEditWeightType
    private var selectedWeightType: Int = ProductEditWeightType.GRAM

    @ProductEditPreOrderTimeType
    private var selectedPreOrderTimeType: Int = ProductEditPreOrderTimeType.DAY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_weightlogistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerCounterInputViewWeight.spinnerTextView.editText.setText(getWeightTypeTitle(selectedWeightType))
        spinnerCounterInputViewProcessTime.spinnerTextView.editText.setText(getPreOrderTimeTypeTitle(selectedPreOrderTimeType))

        spinnerCounterInputViewWeight.spinnerTextView.editText.setOnClickListener({
            showBottomSheetsWeight()
        })
        spinnerCounterInputViewProcessTime.spinnerTextView.editText.setOnClickListener({
            showBottomSheetsPreOrder()
        })

        labelSwitchPreOrder.setOnClickListener {
            labelSwitchPreOrder.isChecked = !labelSwitchPreOrder.isChecked
            if(labelSwitchPreOrder.isChecked)
                layoutProcessTime.visibility = View.VISIBLE
            else
                layoutProcessTime.visibility = View.GONE
        }
    }

    private fun getWeightTypeTitle(type: Int): String {
        var resString = -1
        when (type) {
            ProductEditWeightType.GRAM -> resString = R.string.product_label_gram
            ProductEditWeightType.KILOGRAM -> resString = R.string.product_label_kilogram
        }
        return getString(resString)
    }

    private fun getPreOrderTimeTypeTitle(type: Int): String {
        var resString = -1
        when (type) {
            ProductEditPreOrderTimeType.DAY -> resString = R.string.product_label_day
            ProductEditPreOrderTimeType.WEEK -> resString = R.string.product_label_week
        }
        return getString(resString)
    }

    private fun showBottomSheetsWeight() {
        val checkedBottomSheetMenu = ProductEditOptionMenuBottomSheets()
                .setMode(ProductEditOptionMenuAdapter.MODE_CHECKABLE)
                .setTitle(getString(R.string.product_label_product_weight))

        checkedBottomSheetMenu.setMenuItemSelected(object : ProductEditOptionMenuBottomSheets.OnMenuItemSelected {
            override fun onItemSelected(itemId: Int) {
                checkedBottomSheetMenu.dismiss()
                if (!isAdded) {
                    return
                }
                selectedWeightType = itemId
                spinnerCounterInputViewWeight.spinnerTextView.editText.setText(getWeightTypeTitle(selectedWeightType))
            }
        })

        checkedBottomSheetMenu.addItem(ProductEditWeightType.GRAM, getWeightTypeTitle(ProductEditWeightType.GRAM),
                selectedWeightType == ProductEditWeightType.GRAM)
        checkedBottomSheetMenu.addItem(ProductEditWeightType.KILOGRAM, getWeightTypeTitle(ProductEditWeightType.KILOGRAM),
                selectedWeightType == ProductEditWeightType.KILOGRAM)

        checkedBottomSheetMenu.show(activity!!.supportFragmentManager, javaClass.simpleName)
    }

    private fun showBottomSheetsPreOrder() {
        val checkedBottomSheetMenu = ProductEditOptionMenuBottomSheets()
                .setMode(ProductEditOptionMenuAdapter.MODE_CHECKABLE)
                .setTitle(getString(R.string.product_label_process_time))

        checkedBottomSheetMenu.setMenuItemSelected(object : ProductEditOptionMenuBottomSheets.OnMenuItemSelected {
            override fun onItemSelected(itemId: Int) {
                checkedBottomSheetMenu.dismiss()
                if (!isAdded) {
                    return
                }
                selectedPreOrderTimeType = itemId
                spinnerCounterInputViewProcessTime.spinnerTextView.editText.setText(getPreOrderTimeTypeTitle(selectedPreOrderTimeType))
            }
        })

        checkedBottomSheetMenu.addItem(ProductEditPreOrderTimeType.DAY, getPreOrderTimeTypeTitle(ProductEditPreOrderTimeType.DAY),
                selectedPreOrderTimeType == ProductEditPreOrderTimeType.DAY)
        checkedBottomSheetMenu.addItem(ProductEditPreOrderTimeType.WEEK, getPreOrderTimeTypeTitle(ProductEditPreOrderTimeType.WEEK),
                selectedPreOrderTimeType == ProductEditPreOrderTimeType.WEEK)

        checkedBottomSheetMenu.show(activity!!.supportFragmentManager, javaClass.simpleName)
    }

    companion object {

        fun createInstance(): Fragment {
            return ProductEditWeightLogisticFragment()
        }
    }
}
