package com.tokopedia.product.edit.price

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.edit.R
import kotlinx.android.synthetic.main.fragment_product_edit_stock.*

class ProductEditStockFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_stock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    }

    private fun setVisibleStockTextInputLayout(){
        if(labelRadioButtonStockLimited.isChecked){
            textInputLayoutStock.visibility = View.VISIBLE
        } else {
            textInputLayoutStock.visibility = View.GONE
        }
    }

    companion object {

        fun createInstance(): Fragment {
            return ProductEditStockFragment()
        }
    }
}
