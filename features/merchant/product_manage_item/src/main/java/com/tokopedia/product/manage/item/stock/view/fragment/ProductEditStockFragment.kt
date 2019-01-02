package com.tokopedia.product.manage.item.stock.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextWatcher
import android.view.*
import android.widget.TextView
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_STOCK
import com.tokopedia.product.manage.item.stock.view.model.ProductStock
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_HAS_VARIANT
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_IS_STATUS_ADD
import kotlinx.android.synthetic.main.fragment_product_edit_stock.*
import android.text.Editable
import com.tokopedia.core.analytics.AppEventTracking
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.product.manage.item.utils.LabelRadioButton

class ProductEditStockFragment : Fragment() {

    private var productStock = ProductStock()

    private val texViewMenu: TextView? by lazy { activity?.findViewById(R.id.texViewMenu) as? TextView }
    private val hasVariant by lazy { activity?.intent?.getBooleanExtra(EXTRA_HAS_VARIANT, false) ?: false }
    private val isAddStatus by lazy { activity?.intent?.getBooleanExtra(EXTRA_IS_STATUS_ADD, false) ?: false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity?.let { productStock = it.intent.getParcelableExtra(EXTRA_STOCK) }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_PRODUCT_STOCK)) {
                productStock = savedInstanceState.getParcelable(SAVED_PRODUCT_STOCK)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_stock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataStock(productStock)
        labelRadioButtonStockLimited.setOnClickListener {
            setRadioButtonChosen(labelRadioButtonStockLimited)
            setVisibleStockTextInputLayout()
        }

        labelRadioButtonStockAvailable.setOnClickListener {
            setRadioButtonChosen(labelRadioButtonStockAvailable)
            setVisibleStockTextInputLayout()
        }

        labelRadioButtonStockEmpty.setOnClickListener {
            setRadioButtonChosen(labelRadioButtonStockEmpty)
            setVisibleStockTextInputLayout()
        }

        texViewMenu?.run {  text = getString(R.string.label_save)
            setOnClickListener {
                if(isTotalStockValid()){
                    setResult()
                } else {
                    decimalInputViewStock.requestFocus()
                    UnifyTracking.eventAddProductError(activity, AppEventTracking.AddProduct.FIELDS_MANDATORY_STOCK_STATUS)
                }
            }}

        decimalInputViewStock.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                isTotalStockValid()
            }
        })
    }

    private fun String.removeCommaToInt() = toString().replace(",", "").toInt()

    private fun getTotalStock() = decimalInputViewStock.doubleValue.toInt()

    private fun isTotalStockValid(): Boolean {
        if(labelRadioButtonStockLimited.isChecked) {
            if (MIN_STOCK.removeCommaToInt() > getTotalStock() || getTotalStock() > MAX_STOCK.removeCommaToInt()) {
                decimalInputViewStock.setError(getString(R.string.product_error_total_stock_not_valid, MIN_STOCK, MAX_STOCK))
                return false
            }
            decimalInputViewStock.setError(null)
        }
        return true
    }

    private fun setRadioButtonChosen(labelRadioButton: LabelRadioButton){
        labelRadioButtonStockAvailable.isChecked = false
        labelRadioButtonStockLimited.isChecked = false
        labelRadioButtonStockEmpty.isChecked = false
        labelRadioButton.isChecked = true
    }

    private fun setVisibleStockTextInputLayout(){
        if(labelRadioButtonStockLimited.isChecked){
            if (!hasVariant) {
                decimalInputViewStock.visibility = View.VISIBLE
                textViewHelperStock.visibility = View.VISIBLE
            }
            decimalInputViewStock.text = DEFAULT_PARENT_STOCK
        } else {
            decimalInputViewStock.visibility = View.GONE
            textViewHelperStock.visibility = View.GONE
            decimalInputViewStock.text = MIN_STOCK
        }
    }

    private fun setDataStock(productStock: ProductStock){
        labelRadioButtonStockEmpty.isChecked = !productStock.isActive
        if(productStock.stockCount > 0){
            labelRadioButtonStockLimited.isChecked = productStock.isActive
        } else {
            labelRadioButtonStockAvailable.isChecked = productStock.isActive
        }
        setVisibleStockTextInputLayout()
        decimalInputViewStock.text = productStock.stockCount.toString()
        editTextSku.setText(productStock.sku)
        if(isAddStatus){
            labelRadioButtonStockEmpty.visibility = View.GONE
        }
    }

    private fun saveData(productStock: ProductStock) = productStock.apply {
            isActive = !labelRadioButtonStockEmpty.isChecked
            if(labelRadioButtonStockLimited.isChecked) {
                if(getTotalStock() > 0)
                    stockCount = getTotalStock()
                else
                    decimalInputViewStock.setError("Jumlah Stok harus lebih dari 0, atau pilih Stock Kosong")
            } else {
                stockCount = 0
            }
            sku = editTextSku.text.toString()
        }

    private fun setResult(){
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply { putExtra(EXTRA_STOCK, saveData(productStock)) })
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_PRODUCT_STOCK, saveData(productStock))
    }

    companion object {
        const val SAVED_PRODUCT_STOCK = "SAVED_PRODUCT_STOCK"
        const val MIN_STOCK = "1"
        const val MAX_STOCK = "10,000"
        const val DEFAULT_PARENT_STOCK = "1"
        fun createInstance() = ProductEditStockFragment()
    }
}
