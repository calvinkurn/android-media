package com.tokopedia.product.manage.item.stock.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tokopedia.core.analytics.AppEventTracking
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_HAS_VARIANT
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_IS_STATUS_ADD
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_STOCK
import com.tokopedia.product.manage.item.stock.view.model.ProductStock
import kotlinx.android.synthetic.main.fragment_product_edit_stock.*
import kotlinx.android.synthetic.main.widget_label_switch_product.view.*
import timber.log.Timber

class ProductEditStockFragment : Fragment() {

    private var productStock = ProductStock()

    private val texViewMenu: TextView? by lazy { activity?.findViewById(R.id.texViewMenu) as? TextView }
    private val hasVariant by lazy {
        activity?.intent?.getBooleanExtra(EXTRA_HAS_VARIANT, false) ?: false
    }
    private val isAddStatus by lazy {
        activity?.intent?.getBooleanExtra(EXTRA_IS_STATUS_ADD, false) ?: false
    }

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
        if (isAddStatus) {
            labelSwitchStock.disableSwitch()
        }

        if (hasVariant) {
            decimalInputViewStock.text = MIN_STOCK_VARIANT
            decimalInputViewStock.visibility = View.GONE
            textViewHelperStock.visibility = View.VISIBLE
        } else {
            decimalInputViewStock.visibility = View.VISIBLE
            textViewHelperStock.visibility = View.GONE
        }

        labelSwitchStock.setListenerValue(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            setStockLabel(isChecked)
        })


        texViewMenu?.run {
            text = getString(R.string.label_save)
            setOnClickListener {
                if (isTotalStockValid()) {
                    setResult()
                } else {
                    decimalInputViewStock.requestFocus()
                    UnifyTracking.eventAddProductError(activity, AppEventTracking.AddProduct.FIELDS_MANDATORY_STOCK_STATUS)
                }
            }
        }

        decimalInputViewStock.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                isTotalStockValid()
            }
        })
    }

    private fun setStockLabel(isChecked: Boolean) {
        if (isChecked) {
            labelSwitchStock.title = getString(R.string.label_always_active)
        } else {
            labelSwitchStock.title = getString(R.string.label_always_nonactive)
        }
    }

    private fun String.removeCommaToInt() = toString().replace(",", "").toInt()

    private fun getTotalStock() = decimalInputViewStock.doubleValue.toInt()

    private fun isTotalStockValid(): Boolean {
        // When add product, product stock cannot be 0
        val minStock = if (isAddStatus) {
            MIN_STOCK_IS_ADDED
        } else {
            MIN_STOCK
        }

        if (getTotalStock() < minStock.removeCommaToInt()) {
            if (isAddStatus) {
                decimalInputViewStock.setError(getString(R.string.global_product_stock_error_minimum))
            }
            return false
        } else if (getTotalStock() > MAX_STOCK.removeCommaToInt()) {
            decimalInputViewStock.setError(getString(R.string.global_product_stock_error_maximum, MAX_STOCK))
            return false
        }

        decimalInputViewStock.setError(null)
        return true
    }

    private fun setDataStock(productStock: ProductStock) {
        decimalInputViewStock.text = if (productStock.stockCount > 0)
            productStock.stockCount.toString()
        else
            MIN_STOCK

        labelSwitchStock.isChecked = productStock.isActive
        setStockLabel(productStock.isActive)
        editTextSku.setText(productStock.sku)
    }

    private fun saveData(productStock: ProductStock) = productStock.apply {
        isActive = labelSwitchStock.isChecked
        stockCount = getTotalStock()
        sku = editTextSku.text.toString()
    }

    private fun setResult() {
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
        const val MIN_STOCK_VARIANT = "1"
        const val MIN_STOCK = "0"
        const val MIN_STOCK_IS_ADDED = "1"
        const val MAX_STOCK = "999,999"
        fun createInstance() = ProductEditStockFragment()
    }
}
