package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.TextView
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.BaseProductEditFragment.Companion.EXTRA_LOGISTIC
import com.tokopedia.product.edit.price.model.ProductLogistic
import com.tokopedia.product.edit.util.ProductEditOptionMenuAdapter
import com.tokopedia.product.edit.util.ProductEditOptionMenuBottomSheets
import com.tokopedia.product.edit.util.ProductEditPreOrderTimeType
import com.tokopedia.product.edit.util.ProductEditWeightType
import kotlinx.android.synthetic.main.fragment_product_edit_weightlogistic.*

class ProductEditWeightLogisticFragment : Fragment() {

    private var productLogistic = ProductLogistic()

    @ProductEditWeightType
    private var selectedWeightType: Int = ProductEditWeightType.GRAM

    @ProductEditPreOrderTimeType
    private var selectedPreOrderTimeType: Int = ProductEditPreOrderTimeType.DAY

    private val texViewMenu: TextView by lazy { activity!!.findViewById(R.id.texViewMenu) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if(activity!!.intent.hasExtra(EXTRA_LOGISTIC)) {
            productLogistic = activity!!.intent.getParcelableExtra(EXTRA_LOGISTIC)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_weightlogistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataLogistic(productLogistic)
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

        texViewMenu.text = getString(R.string.label_save)
        texViewMenu.setOnClickListener {
            setResult()
        }
    }

    private fun setDataLogistic(productLogistic: ProductLogistic){
        selectedWeightType = productLogistic.weightType
        spinnerCounterInputViewWeight.spinnerTextView.editText.setText(getWeightTypeTitle(selectedWeightType))
        spinnerCounterInputViewWeight.counterEditText.setText(productLogistic.weight.toString())

        labelCheckboxInsurance.isChecked = productLogistic.insurance
        labelCheckboxFreeReturn.isChecked = productLogistic.freeReturn
        labelSwitchPreOrder.isChecked = productLogistic.preOrder
        if(labelSwitchPreOrder.isChecked)
            layoutProcessTime.visibility = View.VISIBLE
        else
            layoutProcessTime.visibility = View.GONE

        selectedPreOrderTimeType = productLogistic.processTimeType
        spinnerCounterInputViewProcessTime.spinnerTextView.editText.setText(getPreOrderTimeTypeTitle(selectedPreOrderTimeType))
        spinnerCounterInputViewProcessTime.counterEditText.setText(productLogistic.processTime.toString())
    }

    private fun saveData(productLogistic: ProductLogistic): ProductLogistic{
        productLogistic.weightType = selectedWeightType
        productLogistic.weight = spinnerCounterInputViewWeight.counterEditText.text.toString().replace(",", "").toInt()
        productLogistic.insurance = labelCheckboxInsurance.isChecked
        productLogistic.freeReturn = labelCheckboxFreeReturn.isChecked
        productLogistic.preOrder = labelSwitchPreOrder.isChecked
        if(labelSwitchPreOrder.isChecked){
            productLogistic.processTimeType = selectedPreOrderTimeType
            productLogistic.processTime = spinnerCounterInputViewProcessTime.counterEditText.text.toString().replace(",", "").toInt()
        } else {
            productLogistic.processTimeType = ProductEditPreOrderTimeType.DAY
            productLogistic.processTime = 0
        }
        return productLogistic
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

        checkedBottomSheetMenu.addItem(ProductEditWeightType.GRAM, getString(getWeightTypeTitle(ProductEditWeightType.GRAM)),
                selectedWeightType == ProductEditWeightType.GRAM)
        checkedBottomSheetMenu.addItem(ProductEditWeightType.KILOGRAM, getString(getWeightTypeTitle(ProductEditWeightType.KILOGRAM)),
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

    private fun setResult(){
        val intent = Intent()
        intent.putExtra(EXTRA_LOGISTIC, saveData(productLogistic))
        activity!!.setResult(Activity.RESULT_OK, intent)
        activity!!.finish()
    }

    companion object {

        fun getWeightTypeTitle(type: Int): Int {
            var resString = -1
            when (type) {
                ProductEditWeightType.GRAM -> resString = R.string.product_label_gram
                ProductEditWeightType.KILOGRAM -> resString = R.string.product_label_kilogram
            }
            return resString
        }

        fun createInstance() = ProductEditWeightLogisticFragment()
    }
}
