package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.common.model.edit.ProductWholesaleViewModel
import com.tokopedia.product.edit.price.BaseProductEditFragment.Companion.EXTRA_PRICE
import com.tokopedia.product.edit.price.model.ProductPrice
import com.tokopedia.product.edit.util.ProductEditCurrencyType
import com.tokopedia.product.edit.util.ProductEditOptionMenuAdapter
import com.tokopedia.product.edit.util.ProductEditOptionMenuBottomSheets
import com.tokopedia.product.edit.view.activity.ProductAddWholesaleActivity
import com.tokopedia.product.edit.view.fragment.ProductAddWholesaleFragment.EXTRA_PRODUCT_WHOLESALE
import kotlinx.android.synthetic.main.fragment_product_edit_price.*

class ProductEditPriceFragment : Fragment() {

    private val texViewMenu: TextView by lazy { activity!!.findViewById(R.id.texViewMenu) as TextView }

    @ProductEditCurrencyType
    private var selectedCurrencyType: Int = ProductEditCurrencyType.RUPIAH

    private var productPrice = ProductPrice()

    private var wholesalePrice: ArrayList<ProductWholesaleViewModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if(activity!!.intent.hasExtra(EXTRA_PRICE)) {
            productPrice = activity!!.intent.getParcelableExtra(EXTRA_PRICE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDataPrice(productPrice)
        texViewMenu.text = getString(R.string.label_save)
        texViewMenu.setOnClickListener {
            setResult()
        }

        spinnerCounterInputViewPrice.spinnerTextView.editText.setOnClickListener({
            showBottomSheetsCurrency()
        })

        textAddMaksimumBuy.setOnClickListener({
            textInputLayoutMaksimumBuy.visibility = View.VISIBLE
            textAddMaksimumBuy.visibility = View.GONE
        })

        imageViewEdit.setOnClickListener {
            wholesalePrice.clear()
            setLabelViewWholesale(wholesalePrice)
        }

        labelViewWholesale.setOnClickListener {
            startActivityForResult(ProductAddWholesaleActivity.getIntent(context, wholesalePrice, selectedCurrencyType, spinnerCounterInputViewPrice.counterEditText.text.toString().replace(",", "").toDouble(), true, true), REQUEST_CODE_GET_WHOLESALE) }
    }

    private fun getCurrencyTypeTitle(type: Int): String {
        var resString = -1
        when (type) {
            ProductEditCurrencyType.RUPIAH -> resString = R.string.product_label_rupiah
            ProductEditCurrencyType.USD -> resString = R.string.product_label_usd
        }
        return getString(resString)
    }

    private fun showBottomSheetsCurrency() {
        val checkedBottomSheetMenu = ProductEditOptionMenuBottomSheets()
                .setMode(ProductEditOptionMenuAdapter.MODE_CHECKABLE)
                .setTitle(getString(R.string.product_label_currency))

        checkedBottomSheetMenu.setMenuItemSelected(object : ProductEditOptionMenuBottomSheets.OnMenuItemSelected {
            override fun onItemSelected(itemId: Int) {
                checkedBottomSheetMenu.dismiss()
                if (!isAdded) {
                    return
                }
                selectedCurrencyType = itemId
                spinnerCounterInputViewPrice.spinnerTextView.editText.setText(getCurrencyTypeTitle(selectedCurrencyType))
            }
        })

        checkedBottomSheetMenu.addItem(ProductEditCurrencyType.RUPIAH, getCurrencyTypeTitle(ProductEditCurrencyType.RUPIAH),
                selectedCurrencyType == ProductEditCurrencyType.RUPIAH)
        checkedBottomSheetMenu.addItem(ProductEditCurrencyType.USD, getCurrencyTypeTitle(ProductEditCurrencyType.USD),
                selectedCurrencyType == ProductEditCurrencyType.USD)

        checkedBottomSheetMenu.show(activity!!.supportFragmentManager, javaClass.simpleName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GET_WHOLESALE -> {
                    wholesalePrice = data!!.getParcelableArrayListExtra(EXTRA_PRODUCT_WHOLESALE)
                    setLabelViewWholesale(wholesalePrice)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showOrderMaxForm(){
        textInputLayoutMaksimumBuy.visibility = View.VISIBLE
        textAddMaksimumBuy.visibility = View.GONE
    }

    private fun setEnablePriceForm(isEnabled : Boolean){
        spinnerCounterInputViewPrice.isEnabled = isEnabled
        if(isEnabled)
            imageViewEdit.visibility = View.GONE
        else
            imageViewEdit.visibility = View.VISIBLE
    }

    private fun showDataPrice(productPrice: ProductPrice){
        selectedCurrencyType = productPrice.currencyType
        spinnerCounterInputViewPrice.spinnerTextView.editText.setText(getCurrencyTypeTitle(selectedCurrencyType))
        spinnerCounterInputViewPrice.counterEditText.setText(productPrice.price.toString())
        wholesalePrice = productPrice.wholesalePrice
        setLabelViewWholesale(wholesalePrice)
        if(productPrice.minOrder > 0)
            editTextMinOrder.setText(productPrice.minOrder.toString())
        editTextMaxOrder.setText(productPrice.maxOrder.toString())
        if(productPrice.maxOrder > 0) {
            editTextMaxOrder.setText(productPrice.maxOrder.toString())
            showOrderMaxForm()
        }
    }

    private fun setLabelViewWholesale(wholesaleList: ArrayList<ProductWholesaleViewModel>){
        if (wholesaleList.size == 0) {
            labelViewWholesale.setContent(getString(R.string.label_add))
            setEnablePriceForm(true)
        } else {
            labelViewWholesale.setContent(getString(R.string.product_count_wholesale, wholesaleList.size))
            setEnablePriceForm(false)
        }
    }

    private fun saveData(productPrice: ProductPrice): ProductPrice{
        productPrice.currencyType = selectedCurrencyType
        productPrice.price = spinnerCounterInputViewPrice.counterEditText.text.toString().replace(",", "").toDouble()
        productPrice.wholesalePrice = wholesalePrice
        productPrice.minOrder = editTextMinOrder.text.toInt()
        productPrice.maxOrder = editTextMaxOrder.text.toInt()
        return productPrice
    }

    private fun Editable.toInt(): Int{
        return toString().toInt()
    }

    private fun setResult(){
        val intent = Intent()
        intent.putExtra(EXTRA_PRICE, saveData(productPrice))
        activity!!.setResult(Activity.RESULT_OK, intent)
        activity!!.finish()
    }

    companion object {
        const val REQUEST_CODE_GET_WHOLESALE = 1
        fun createInstance() = ProductEditPriceFragment()
    }
}
