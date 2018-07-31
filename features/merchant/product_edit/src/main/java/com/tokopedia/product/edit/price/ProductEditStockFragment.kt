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
import kotlinx.android.synthetic.main.fragment_product_edit_stock.*

class ProductEditStockFragment : Fragment() {

    private var productStock = ProductStock()

    private val texViewMenu: TextView by lazy { activity!!.findViewById(R.id.texViewMenu) as TextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if(activity!!.intent.hasExtra(EXTRA_STOCK)) {
            productStock = activity!!.intent.getParcelableExtra(EXTRA_STOCK)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_stock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataStock(productStock)
        labelRadioButtonStockLimited.setOnClickListener {
            if(!labelRadioButtonStockLimited.isChecked){
                labelRadioButtonStockLimited.isChecked = !labelRadioButtonStockLimited.isChecked
                labelRadioButtonStockAvailable.isChecked = !labelRadioButtonStockLimited.isChecked
            }
            setVisibleStockTextInputLayout()
        }

        labelRadioButtonStockAvailable.setOnClickListener {
            if(!labelRadioButtonStockAvailable.isChecked){
                labelRadioButtonStockAvailable.isChecked = !labelRadioButtonStockAvailable.isChecked
                labelRadioButtonStockLimited.isChecked = !labelRadioButtonStockAvailable.isChecked
            }
            setVisibleStockTextInputLayout()
        }

        texViewMenu.text = getString(R.string.label_save)
        texViewMenu.setOnClickListener {
            setResult()
        }
    }

    private fun setVisibleStockTextInputLayout(){
        if(labelRadioButtonStockLimited.isChecked){
            decimalInputViewStock.visibility = View.VISIBLE
            textViewHelperStock.visibility = View.VISIBLE
        } else {
            decimalInputViewStock.visibility = View.GONE
            textViewHelperStock.visibility = View.GONE
        }
    }

    private fun setDataStock(productStock: ProductStock){
        labelRadioButtonStockLimited.isChecked = productStock.isActive
        labelRadioButtonStockAvailable.isChecked = !productStock.isActive
        setVisibleStockTextInputLayout()
        decimalInputViewStock.text = productStock.stockCount.toString()
        editTextSku.setText(productStock.sku)
    }

    private fun saveData(productStock: ProductStock): ProductStock{
        productStock.isActive = labelRadioButtonStockLimited.isChecked
        if(productStock.isActive) {
            productStock.stockCount = decimalInputViewStock.text.toString().replace(",", "").toInt()
        }else {
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

        fun createInstance() = ProductEditStockFragment()
    }
}
