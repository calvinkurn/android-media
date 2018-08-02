package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.TextView
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_STOCK
import com.tokopedia.product.edit.price.model.ProductStock
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_HAS_VARIANT
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_IS_STATUS_ADD
import kotlinx.android.synthetic.main.fragment_product_edit_stock.*

class ProductEditStockFragment : Fragment() {

    private var productStock = ProductStock()

    private val texViewMenu: TextView by lazy { activity!!.findViewById(R.id.texViewMenu) as TextView }
    private val hasVariant by lazy { activity!!.intent.getBooleanExtra(EXTRA_HAS_VARIANT, false) }
    private val isAddStatus by lazy { activity!!.intent.getBooleanExtra(EXTRA_IS_STATUS_ADD, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        productStock = activity!!.intent.getParcelableExtra(EXTRA_STOCK)
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

        texViewMenu.text = getString(R.string.label_save)
        texViewMenu.setOnClickListener {
            setResult()
        }
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
            decimalInputViewStock.text = DEFAULT_PARENT_STOCK.toString()
        } else {
            decimalInputViewStock.visibility = View.GONE
            textViewHelperStock.visibility = View.GONE
            decimalInputViewStock.text = DEFAULT_EMPTY_STOCK.toString()
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

    private fun saveData(productStock: ProductStock): ProductStock{
        productStock.isActive = !labelRadioButtonStockEmpty.isChecked
        if(labelRadioButtonStockLimited.isChecked) {
            if(decimalInputViewStock.text.toString().replace(",", "").toInt() > 0)
                productStock.stockCount = decimalInputViewStock.text.toString().replace(",", "").toInt()
            else
                decimalInputViewStock.setError("Jumlah Stok harus lebih dari 0, atau pilih Stock Kosong")
        } else {
            productStock.stockCount = 0
        }
        productStock.sku = editTextSku.text.toString()
        return productStock
    }

    private fun setResult(){
        val intent = Intent()
        intent.putExtra(EXTRA_STOCK, saveData(productStock))
        activity!!.setResult(Activity.RESULT_OK, intent)
        activity!!.finish()
    }

    companion object {
        const val DEFAULT_EMPTY_STOCK = 0
        const val DEFAULT_PARENT_STOCK = 1
        fun createInstance() = ProductEditStockFragment()
    }
}
