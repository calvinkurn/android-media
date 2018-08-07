package com.tokopedia.product.edit.price

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tkpd.library.utils.CommonUtils
import com.tokopedia.core.analytics.AppEventTracking
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.core.util.GlobalConfig
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.common.model.edit.ProductWholesaleViewModel
import com.tokopedia.product.edit.common.util.CurrencyIdrTextWatcher
import com.tokopedia.product.edit.common.util.CurrencyTypeDef
import com.tokopedia.product.edit.common.util.CurrencyUsdTextWatcher
import com.tokopedia.product.edit.price.model.ProductPrice
import com.tokopedia.product.edit.util.ProductEditOptionMenuAdapter
import com.tokopedia.product.edit.util.ProductEditOptionMenuBottomSheets
import com.tokopedia.product.edit.utils.ProductPriceRangeUtils
import com.tokopedia.product.edit.view.activity.ProductAddWholesaleActivity
import com.tokopedia.product.edit.view.dialog.ProductChangeVariantPriceDialogFragment
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_HAS_VARIANT
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_IS_GOLD_MERCHANT
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_IS_MOVE_TO_GM
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_IS_OFFICIAL_STORE
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_PRICE
import com.tokopedia.product.edit.view.fragment.ProductAddWholesaleFragment.EXTRA_PRODUCT_WHOLESALE
import kotlinx.android.synthetic.main.fragment_product_edit_price.*

class ProductEditPriceFragment : Fragment(), ProductChangeVariantPriceDialogFragment.OnProductChangeVariantPriceFragmentListener {

    private val texViewMenu: TextView? by lazy { activity?.findViewById(R.id.texViewMenu) as? TextView }

    @CurrencyTypeDef
    private var selectedCurrencyType: Int = CurrencyTypeDef.TYPE_IDR

    private var productPrice = ProductPrice()
    private val isOfficialStore by lazy { activity?.intent?.getBooleanExtra(EXTRA_IS_OFFICIAL_STORE, false) ?: false }
    private val hasVariant by lazy { activity?.intent?.getBooleanExtra(EXTRA_HAS_VARIANT, false) ?: false }
    private val isGoldMerchant by lazy { activity?.intent?.getBooleanExtra(EXTRA_IS_GOLD_MERCHANT, false) ?: false}

    private var wholesalePrice: ArrayList<ProductWholesaleViewModel> = ArrayList()

    private lateinit var idrTextWatcher: CurrencyIdrTextWatcher
    private lateinit var usdTextWatcher: CurrencyUsdTextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity?.run { if(intent.hasExtra(EXTRA_PRICE)) {
            productPrice = intent.getParcelableExtra(EXTRA_PRICE)
        } }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idrTextWatcher = object : CurrencyIdrTextWatcher(spinnerCounterInputViewPrice.counterEditText) {
            override fun onNumberChanged(number: Double) {
                isPriceValid()
            }
        }
        usdTextWatcher = object : CurrencyUsdTextWatcher(spinnerCounterInputViewPrice.counterEditText) {
            override fun onNumberChanged(number: Double) {
                isPriceValid()
            }
        }
        showDataPrice(productPrice)
        texViewMenu?.run {  text = getString(R.string.label_save)
            setOnClickListener {
                if(isDataValid()){
                    setResult(false)
                }
            }}

        spinnerCounterInputViewPrice.spinnerTextView.editText.setOnClickListener({
            showBottomSheetsCurrency()
        })

        editTextMinOrder.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(editable: Editable) {
                if (isMinOrderValid()) {
                    editTextMinOrder.setError(null)
                }
            }
        })

        editTextMaxOrder.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(editable: Editable) {
                if (isMaxOrderValid()) {
                    editTextMaxOrder.setError(null)
                }
            }
        })

        textAddMaksimumBuy.setOnClickListener{showOrderMaxForm()}

        imageViewEdit.setOnClickListener {showEditPriceDialog()}

        labelViewWholesale.setOnClickListener {
            startActivityForResult(ProductAddWholesaleActivity
                    .getIntent(context, wholesalePrice, selectedCurrencyType, spinnerCounterInputViewPrice
                            .counterEditText.text.toString().replace(",", "").toDouble(),
                            true, true), REQUEST_CODE_GET_WHOLESALE) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_GET_WHOLESALE -> {
                    wholesalePrice = data.getParcelableArrayListExtra(EXTRA_PRODUCT_WHOLESALE)
                    setEditTextPriceState(wholesalePrice)
                    setLabelViewWholesale(wholesalePrice)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onChangeAllPriceVariantSubmit(currencyType: Int, currencyValue: Double) {
        spinnerCounterInputViewPrice.counterValue = currencyValue
    }

    private fun showDataPrice(productPrice: ProductPrice){
        selectedCurrencyType = productPrice.currencyType
        spinnerCounterInputViewPrice.counterValue = productPrice.price
        setPriceTextChangedListener()
        wholesalePrice = productPrice.wholesalePrice
        setEditTextPriceState(wholesalePrice)
        setLabelViewWholesale(wholesalePrice)
        if(productPrice.minOrder > 0)
            editTextMinOrder.text = productPrice.minOrder.toString()
        else
            editTextMinOrder.text = MIN_ORDER
        editTextMaxOrder.text = productPrice.maxOrder.toString()
        if(productPrice.maxOrder > 0) {
            editTextMaxOrder.text = productPrice.maxOrder.toString()
            showOrderMaxForm()
        }
    }

    private fun getPriceValue() = spinnerCounterInputViewPrice.counterValue

    private fun getMinOrderValue() = editTextMinOrder.text.removeCommaToInt()

    private fun getMaxOrderValue() = editTextMaxOrder.text.removeCommaToInt()

    private fun String.removeCommaToInt() = toString().replace(",", "").toInt()

    private fun isPriceValid(): Boolean {
        if (!ProductPriceRangeUtils.isPriceValid(getPriceValue(), selectedCurrencyType, isOfficialStore) || getPriceValue() == DEFAULT_PRICE) {
            spinnerCounterInputViewPrice.setCounterError(
                            getString(R.string.product_error_product_price_not_valid,
                            ProductPriceRangeUtils.getMinPriceString(selectedCurrencyType, isOfficialStore),
                            ProductPriceRangeUtils.getMaxPriceString(selectedCurrencyType, isOfficialStore)))
            labelViewWholesale.visibility = View.GONE
            dividerLabelViewWholesale.visibility = View.GONE
            return false
        } else {
            labelViewWholesale.visibility = View.VISIBLE
            dividerLabelViewWholesale.visibility = View.VISIBLE
        }
        spinnerCounterInputViewPrice.setCounterError(null)
        return true
    }

    private fun isMinOrderValid(): Boolean {
        if (MIN_ORDER.removeCommaToInt() > getMinOrderValue() || getMinOrderValue() > MAX_ORDER.removeCommaToInt()) {
            editTextMinOrder.setError(getString(R.string.product_error_product_minimum_order_not_valid, MIN_ORDER, MAX_ORDER))
            return false
        }
        editTextMinOrder.setError(null)
        return true
    }

    private fun isMaxOrderValid(): Boolean {
        if(getMinOrderValue() > 0) {
            if (MIN_ORDER.removeCommaToInt() > getMinOrderValue() || getMinOrderValue() > MAX_ORDER.removeCommaToInt()) {
                editTextMaxOrder.setError(getString(R.string.product_error_product_minimum_order_not_valid, MIN_ORDER, MAX_ORDER))
                return false
            }
        }
        editTextMaxOrder.setError(null)
        return true
    }

    private fun getCurrencyTypeTitle(type: Int) =  getString(
            when (type) {
                CurrencyTypeDef.TYPE_IDR -> R.string.product_label_rupiah
                CurrencyTypeDef.TYPE_USD -> R.string.product_label_usd
                else -> -1
            })

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
                if(itemId != selectedCurrencyType){
                    if (!isGoldMerchant && itemId == CurrencyTypeDef.TYPE_USD) {
                        if (GlobalConfig.isSellerApp()) {
                            UnifyTracking.eventSwitchRpToDollarAddProduct()
                            setResult(true)
                        } else {
                            Snackbar.make(spinnerCounterInputViewPrice.rootView.findViewById(android.R.id.content), R.string.product_error_must_be_gold_merchant, Snackbar.LENGTH_LONG)
                                    .setActionTextColor(ContextCompat.getColor(context!!, R.color.green_400))
                                    .show()
                        }
                    } else {
                        selectedCurrencyType = itemId
                        setPriceTextChangedListener()
                    }
                    spinnerCounterInputViewPrice.counterValue = DEFAULT_PRICE
                    spinnerCounterInputViewPrice.counterEditText.setSelection(spinnerCounterInputViewPrice.counterEditText.text.length)
                    spinnerCounterInputViewPrice.setCounterError(null)
                }
            }
        })

        checkedBottomSheetMenu.addItem(CurrencyTypeDef.TYPE_IDR, getCurrencyTypeTitle(CurrencyTypeDef.TYPE_IDR),
                selectedCurrencyType == CurrencyTypeDef.TYPE_IDR)
        checkedBottomSheetMenu.addItem(CurrencyTypeDef.TYPE_USD, getCurrencyTypeTitle(CurrencyTypeDef.TYPE_USD),
                selectedCurrencyType == CurrencyTypeDef.TYPE_USD)

        checkedBottomSheetMenu.show(activity!!.supportFragmentManager, javaClass.simpleName)
    }

    private fun showEditPriceDialog() {
        if (wholesalePrice.size>0) {
            val builder = AlertDialog.Builder(context!!,
                    R.style.AppCompatAlertDialogStyle)
            builder.setTitle(R.string.product_title_confirmation_change_wholesale_price)
            builder.setMessage(R.string.product_confirmation_change_wholesale_price)
            builder.setCancelable(true)
            builder.setPositiveButton(R.string.change) { dialog, _ ->
                wholesalePrice.clear()
                setEditTextPriceState(wholesalePrice)
                setLabelViewWholesale(wholesalePrice)
                dialog.cancel()
                if (hasVariant) {
                    showEditPriceWhenHasVariantDialog()
                }
            }
            builder.setNegativeButton(R.string.close) { dialog, _ -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        } else if (hasVariant) {
            showEditPriceWhenHasVariantDialog()
        }
    }

    private fun showEditPriceWhenHasVariantDialog(){
        val dialogFragment = ProductChangeVariantPriceDialogFragment.newInstance(selectedCurrencyType,
                isGoldMerchant,
                getPriceValue(),
                isOfficialStore)
        dialogFragment.show(activity!!.supportFragmentManager,
                ProductChangeVariantPriceDialogFragment.TAG)
        dialogFragment.attachListener(this)
        dialogFragment.setOnDismissListener {
            Handler().post(Runnable {
                if (!isAdded || activity == null) {
                    return@Runnable
                }
                val view = activity!!.currentFocus
                if (view != null) {
                    CommonUtils.hideSoftKeyboard(view)
                    view.clearFocus()
                }
            })
        }
    }

    private fun showOrderMaxForm(){
        editTextMaxOrder.visibility = View.VISIBLE
        textAddMaksimumBuy.visibility = View.GONE
    }

    private fun setPriceTextChangedListener(){
        spinnerCounterInputViewPrice.spinnerTextView.editText.setText(getCurrencyTypeTitle(selectedCurrencyType))
        spinnerCounterInputViewPrice.removeTextChangedListener(idrTextWatcher)
        spinnerCounterInputViewPrice.removeTextChangedListener(usdTextWatcher)
        if (selectedCurrencyType == CurrencyTypeDef.TYPE_IDR) {
            spinnerCounterInputViewPrice.addTextChangedListener(idrTextWatcher)
        } else {
            spinnerCounterInputViewPrice.addTextChangedListener(usdTextWatcher)
        }
    }

    private fun setLabelViewWholesale(wholesaleList: ArrayList<ProductWholesaleViewModel>){
        labelViewWholesale.setContent(if (wholesaleList.size == 0) getString(R.string.label_add) else getString(R.string.product_count_wholesale, wholesaleList.size))
    }

    private fun setEditTextPriceState(wholesaleList: ArrayList<ProductWholesaleViewModel>){
        if (wholesaleList.size == 0 && !hasVariant) {
            setEnablePriceForm(true)
            spinnerCounterInputViewPrice.counterEditText.setSelection(spinnerCounterInputViewPrice.counterEditText.text.length)
        } else {
            setEnablePriceForm(false)
        }
    }

    private fun setEnablePriceForm(isEnabled : Boolean){
        spinnerCounterInputViewPrice.isEnabled = isEnabled
        imageViewEdit.visibility = if(isEnabled) View.GONE else View.VISIBLE
    }

    private fun saveData(productPrice: ProductPrice) = productPrice.apply {
            currencyType = selectedCurrencyType
            price = getPriceValue()
            wholesalePrice = this@ProductEditPriceFragment.wholesalePrice
            minOrder = getMinOrderValue()
            maxOrder = getMaxOrderValue()
        }

    private fun isDataValid(): Boolean{
        if(!isPriceValid()){
            spinnerCounterInputViewPrice.requestFocus()
            UnifyTracking.eventAddProductError(AppEventTracking.AddProduct.FIELDS_MANDATORY_PRICE)
            return false
        }
        if(!isMinOrderValid()){
            editTextMinOrder.requestFocus()
            UnifyTracking.eventAddProductError(AppEventTracking.AddProduct.FIELDS_MANDATORY_MIN_PURCHASE)
            return false
        }
        return true
    }

    private fun setResult(isMoveToGm: Boolean){
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_PRICE, saveData(productPrice))
                putExtra(EXTRA_IS_MOVE_TO_GM, isMoveToGm)
            })
            finish()
        }
    }

    companion object {
        const val DEFAULT_PRICE = 0.0
        const val MIN_ORDER = "1"
        const val MAX_ORDER = "10,000"
        const val REQUEST_CODE_GET_WHOLESALE = 1
        fun createInstance() = ProductEditPriceFragment()
    }
}
