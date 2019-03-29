package com.tokopedia.product.manage.item.logistic.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextWatcher
import android.view.*
import android.widget.CompoundButton
import android.widget.TextView
import com.tokopedia.core.analytics.AppEventTracking
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.design.text.watcher.NumberTextWatcher
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_LOGISTIC
import com.tokopedia.product.manage.item.logistic.view.model.ProductLogistic
import com.tokopedia.product.manage.item.utils.ProductEditOptionMenuAdapter
import com.tokopedia.product.manage.item.utils.ProductEditOptionMenuBottomSheets
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_IS_FREE_RETURN
import com.tokopedia.product.manage.item.utils.ProductEditPreOrderTimeType
import com.tokopedia.product.manage.item.utils.ProductEditWeightType
import kotlinx.android.synthetic.main.fragment_product_edit_weightlogistic.*

class ProductEditWeightLogisticFragment : Fragment() {

    private var productLogistic = ProductLogistic()

    @ProductEditWeightType
    private var selectedWeightType: Int = ProductEditWeightType.GRAM

    @ProductEditPreOrderTimeType
    private var selectedPreOrderTimeType: Int = ProductEditPreOrderTimeType.DAY

    private val texViewMenu: TextView? by lazy { activity?.findViewById(R.id.texViewMenu) as? TextView }
    private val isFreeReturn by lazy { activity?.intent?.getBooleanExtra(EXTRA_IS_FREE_RETURN, false) ?: false }

    private lateinit var weightTextWatcher: TextWatcher
    private lateinit var processTimeTextWatcher: TextWatcher


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity?.let {
            productLogistic = it.intent.getParcelableExtra(EXTRA_LOGISTIC)
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_PRODUCT_LOGISTIC)) {
                productLogistic = savedInstanceState.getParcelable(SAVED_PRODUCT_LOGISTIC)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_weightlogistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataLogistic(productLogistic)
        spinnerCounterInputViewWeight.spinnerTextView.editText.setOnClickListener({ showBottomSheetsWeight() })
        spinnerCounterInputViewProcessTime.spinnerTextView.editText.setOnClickListener({ showBottomSheetsPreOrder() })

        weightTextWatcher = object : NumberTextWatcher(spinnerCounterInputViewWeight.counterEditText, getString(R.string.product_default_counter_text)) {
            override fun onNumberChanged(number: Double) {
                if (isWeightValid()) {
                    spinnerCounterInputViewWeight.setCounterError(null)
                }
            }
        }

        processTimeTextWatcher = object : NumberTextWatcher(spinnerCounterInputViewProcessTime.counterEditText, getString(R.string.product_default_counter_text)) {
            override fun onNumberChanged(number: Double) {
                if (isPreOrderValid()) {
                    spinnerCounterInputViewProcessTime.setCounterError(null)
                }
            }
        }

        spinnerCounterInputViewWeight.addTextChangedListener(weightTextWatcher)
        spinnerCounterInputViewProcessTime.addTextChangedListener(processTimeTextWatcher)

        labelSwitchPreOrder.setListenerValue(CompoundButton.OnCheckedChangeListener {
            compoundButton: CompoundButton, b: Boolean -> populatePreorder()
        })

        texViewMenu?.run {  text = getString(R.string.label_save)
            setOnClickListener {
                when {
                    !isWeightValid() -> {
                        spinnerCounterInputViewWeight.requestFocus()
                        UnifyTracking.eventAddProductError(activity, AppEventTracking.AddProduct.FIELDS_MANDATORY_WEIGHT)
                    }
                    !isPreOrderValid() -> {
                        spinnerCounterInputViewProcessTime.requestFocus()
                        UnifyTracking.eventAddProductError(activity, AppEventTracking.AddProduct.FIELDS_OPTIONAL_PREORDER)
                    }
                    else -> setResult()
                }
            }}
    }

    private fun populatePreorder() {
        layoutProcessTime.visibility = if(labelSwitchPreOrder.isChecked) View.VISIBLE else View.GONE
    }

    private fun setDataLogistic(productLogistic: ProductLogistic){
        selectedWeightType = productLogistic.weightType
        spinnerCounterInputViewWeight.spinnerTextView.editText.setText(getWeightTypeTitle(selectedWeightType))
        spinnerCounterInputViewWeight.counterEditText.setText(productLogistic.weight.toString())

        labelCheckboxInsurance.isChecked = productLogistic.insurance
        labelCheckboxFreeReturn.isChecked = productLogistic.freeReturn
        labelSwitchPreOrder.isChecked = productLogistic.preOrder
        layoutProcessTime.visibility = if(labelSwitchPreOrder.isChecked) View.VISIBLE else View.GONE

        if(isFreeReturn) {
            labelCheckboxFreeReturn.visibility = View.VISIBLE
            lineDividerFreeReturn.visibility = View.VISIBLE
        }
        else {
            labelCheckboxFreeReturn.visibility = View.GONE
            lineDividerFreeReturn.visibility = View.GONE
        }

        selectedPreOrderTimeType = productLogistic.processTimeType
        spinnerCounterInputViewProcessTime.spinnerTextView.editText.setText(getPreOrderTimeTypeTitle(selectedPreOrderTimeType))
        spinnerCounterInputViewProcessTime.counterEditText.setText(productLogistic.processTime.toString())
    }

    private fun getWeight() = spinnerCounterInputViewWeight.counterValue

    private fun getPreOrder() = spinnerCounterInputViewProcessTime.counterValue

    private fun isWeightValid(): Boolean {
        val minWeight = MIN_WEIGHT
        var maxWeight = MAX_WEIGHT_GRAM
        if (selectedWeightType == ProductEditWeightType.KILOGRAM) {
            maxWeight = MAX_WEIGHT_KG
        }
        if (minWeight.removeCommaToInt() > getWeight() || getWeight() > maxWeight.removeCommaToInt()) {
            spinnerCounterInputViewWeight.setCounterError(getString(R.string.product_error_product_weight_not_valid, minWeight, maxWeight))
            return false
        }
        spinnerCounterInputViewWeight.setCounterError(null)
        return true
    }

    private fun isPreOrderValid(): Boolean {
        if(labelSwitchPreOrder.isChecked){
            val minPreOrder = MIN_PRE_ORDER
            var maxPreOrder = MAX_PRE_ORDER_DAY
            if (selectedPreOrderTimeType == ProductEditPreOrderTimeType.WEEK) {
                maxPreOrder = MAX_PRE_ORDER_WEEK
            }
            if (minPreOrder.removeCommaToInt() > getPreOrder() || getPreOrder() > maxPreOrder.removeCommaToInt()) {
                spinnerCounterInputViewProcessTime.setCounterError(getString(R.string.product_error_product_pre_order_not_valid, minPreOrder, maxPreOrder))
                return false
            }
            spinnerCounterInputViewProcessTime.setCounterError(null)
        }
        return true
    }

    private fun String.removeCommaToInt() = toString().replace(",", "").toInt()

    private fun getPreOrderTimeTypeTitle(type: Int) = getString(
        when (type) {
            ProductEditPreOrderTimeType.DAY ->  R.string.product_label_day
            ProductEditPreOrderTimeType.WEEK ->  R.string.product_label_week
            ProductEditPreOrderTimeType.MONTH ->  R.string.product_label_month
            else -> R.string.product_label_day
        })


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
                spinnerCounterInputViewWeight.removeTextChangedListener(weightTextWatcher)
                spinnerCounterInputViewWeight.spinnerTextView.editText.setText(getWeightTypeTitle(selectedWeightType))
                spinnerCounterInputViewWeight.counterValue = getString(R.string.product_default_counter_text).toDouble()
                spinnerCounterInputViewWeight.counterEditText.setSelection(spinnerCounterInputViewWeight.counterEditText.text.length)
                spinnerCounterInputViewWeight.addTextChangedListener(weightTextWatcher)
                spinnerCounterInputViewWeight.setCounterError(null)
            }
        })

        checkedBottomSheetMenu.addItem(ProductEditWeightType.GRAM, getString(getWeightTypeTitle(ProductEditWeightType.GRAM)),
                selectedWeightType == ProductEditWeightType.GRAM)
        checkedBottomSheetMenu.addItem(ProductEditWeightType.KILOGRAM, getString(getWeightTypeTitle(ProductEditWeightType.KILOGRAM)),
                selectedWeightType == ProductEditWeightType.KILOGRAM)

        activity?.run {
            checkedBottomSheetMenu.show(this.supportFragmentManager, javaClass.simpleName)
        }
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
                spinnerCounterInputViewProcessTime.removeTextChangedListener(processTimeTextWatcher)
                spinnerCounterInputViewProcessTime.spinnerTextView.editText.setText(getPreOrderTimeTypeTitle(selectedPreOrderTimeType))
                spinnerCounterInputViewProcessTime.counterValue = getString(R.string.product_default_counter_text).toDouble()
                spinnerCounterInputViewProcessTime.counterEditText.setSelection(spinnerCounterInputViewProcessTime.counterEditText.text.length)
                spinnerCounterInputViewProcessTime.addTextChangedListener(processTimeTextWatcher)
                spinnerCounterInputViewProcessTime.setCounterError(null)
            }
        })

        checkedBottomSheetMenu.addItem(ProductEditPreOrderTimeType.DAY, getPreOrderTimeTypeTitle(ProductEditPreOrderTimeType.DAY),
                selectedPreOrderTimeType == ProductEditPreOrderTimeType.DAY)
        checkedBottomSheetMenu.addItem(ProductEditPreOrderTimeType.WEEK, getPreOrderTimeTypeTitle(ProductEditPreOrderTimeType.WEEK),
                selectedPreOrderTimeType == ProductEditPreOrderTimeType.WEEK)

        checkedBottomSheetMenu.show(activity!!.supportFragmentManager, javaClass.simpleName)
    }

    private fun saveData(productLogistic: ProductLogistic) =  productLogistic.apply {
            weightType = selectedWeightType
            weight = getWeight().toInt()
            insurance = labelCheckboxInsurance.isChecked
            freeReturn = labelCheckboxFreeReturn.isChecked
            preOrder = labelSwitchPreOrder.isChecked
            if(labelSwitchPreOrder.isChecked){
                processTimeType = selectedPreOrderTimeType
                processTime = getPreOrder().toInt()
            } else {
                processTimeType = ProductEditPreOrderTimeType.DAY
                processTime = 0
            }
        }


    private fun setResult(){
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply { putExtra(EXTRA_LOGISTIC, saveData(productLogistic)) })
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_PRODUCT_LOGISTIC, saveData(productLogistic))
    }

    companion object {
        const val SAVED_PRODUCT_LOGISTIC = "SAVED_PRODUCT_LOGISTIC"
        const val MIN_WEIGHT = "1"
        const val MAX_WEIGHT_GRAM = "500,000"
        const val MAX_WEIGHT_KG = "500"

        const val MIN_PRE_ORDER = "1"
        const val MAX_PRE_ORDER_DAY = "90"
        const val MAX_PRE_ORDER_WEEK = "13"

        fun getWeightTypeTitle(type: Int) =
            when (type) {
                ProductEditWeightType.GRAM ->  R.string.product_label_gram
                ProductEditWeightType.KILOGRAM ->  R.string.product_label_kilogram
                else -> -1
            }

        fun createInstance() = ProductEditWeightLogisticFragment()
    }
}
