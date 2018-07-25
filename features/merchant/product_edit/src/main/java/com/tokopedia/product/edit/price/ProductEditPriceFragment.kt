package com.tokopedia.product.edit.price

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.util.ProductEditCurrencyType
import com.tokopedia.product.edit.util.ProductEditOptionMenuAdapter
import com.tokopedia.product.edit.util.ProductEditOptionMenuBottomSheets
import kotlinx.android.synthetic.main.fragment_product_edit_price.*

class ProductEditPriceFragment : Fragment() {

    @ProductEditCurrencyType
    private var selectedCurrencyType: Int = ProductEditCurrencyType.RUPIAH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerCounterInputViewPrice.spinnerTextView.editText.setText(getCurrencyTypeTitle(selectedCurrencyType))

        spinnerCounterInputViewPrice.spinnerTextView.editText.setOnClickListener({
            showBottomSheetsCurrency()
        })

        textAddMaksimumBuy.setOnClickListener({
            textInputLayoutMaksimumBuy.visibility = View.VISIBLE
            textAddMaksimumBuy.visibility = View.GONE
        })
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

    companion object {

        fun createInstance(): Fragment {
            return ProductEditPriceFragment()
        }
    }
}
