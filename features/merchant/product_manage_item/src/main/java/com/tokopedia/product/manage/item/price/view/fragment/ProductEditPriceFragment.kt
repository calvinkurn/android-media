package com.tokopedia.product.manage.item.price.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tkpd.library.utils.CommonUtils
import com.tokopedia.core.analytics.AppEventTracking
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.gm.resource.GMConstant
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.common.util.CurrencyIdrTextWatcher
import com.tokopedia.product.manage.item.common.util.CurrencyTypeDef
import com.tokopedia.product.manage.item.main.base.data.model.ProductWholesaleViewModel
import com.tokopedia.product.manage.item.price.model.ProductPrice
import com.tokopedia.product.manage.item.utils.ProductPriceRangeUtils
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_HAS_VARIANT
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_IS_GOLD_MERCHANT
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_IS_MOVE_TO_GM
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_IS_OFFICIAL_STORE
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_PRICE
import com.tokopedia.product.manage.item.variant.dialog.ProductChangeVariantPriceDialogFragment
import com.tokopedia.product.manage.item.wholesale.activity.ProductAddWholesaleActivity
import com.tokopedia.product.manage.item.wholesale.fragment.ProductAddWholesaleFragment.EXTRA_PRODUCT_WHOLESALE
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity?.run {
            if(intent.hasExtra(EXTRA_PRICE)) {
            productPrice = intent.getParcelableExtra(EXTRA_PRICE) }
        }
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_PRODUCT_PRICE)) {
                productPrice = savedInstanceState.getParcelable(SAVED_PRODUCT_PRICE)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_product_edit_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idrTextWatcher = object : CurrencyIdrTextWatcher(counterEditText.editText) {
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
                    .getIntent(context, wholesalePrice, selectedCurrencyType, counterEditText
                            .editText.text.toString().replace(",", "").toDouble(),
                            isOfficialStore, hasVariant), REQUEST_CODE_GET_WHOLESALE) }
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
        productPrice.currencyType = currencyType
        productPrice.price = currencyValue
        productPrice.wholesalePrice = ArrayList()
        showDataPrice(productPrice)
    }

    private fun showDataPrice(productPrice: ProductPrice){
        selectedCurrencyType = productPrice.currencyType
        setPriceTextChangedListener()
        counterEditText.setValue(productPrice.price)
        counterEditText.setError(null)
        wholesalePrice = productPrice.wholesalePrice
        setEditTextPriceState(wholesalePrice)
        setLabelViewWholesale(wholesalePrice)

        editTextMinOrder.text = if(productPrice.minOrder > 0)
            productPrice.minOrder.toString()
        else
            MIN_ORDER

        editTextMaxOrder.text = productPrice.maxOrder.toString()
        if(productPrice.maxOrder > 0) {
            editTextMaxOrder.text = productPrice.maxOrder.toString()
            showOrderMaxForm()
        }
    }

    private fun getPriceValue() = counterEditText.doubleValue

    private fun getMinOrderValue() = editTextMinOrder.doubleValue

    private fun getMaxOrderValue() = editTextMaxOrder.doubleValue

    private fun String.removeCommaToInt() = toString().replace(",", "").toInt()

    private fun isPriceValid(): Boolean {
        if (!ProductPriceRangeUtils.isPriceValid(getPriceValue(), selectedCurrencyType, isOfficialStore) || getPriceValue() == DEFAULT_PRICE) {
            counterEditText.setError(
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
        counterEditText.setError(null)
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

    private fun showDialogGoToGM(){
        val builder = AlertDialog.Builder(context!!,
                R.style.AppCompatAlertDialogStyle)
        val gm = getString(GMConstant.getGMTitleResource(context))
        builder.setTitle(getString(R.string.add_product_title_alert_dialog_dollar_dynamic, gm))
        builder.setMessage(getString(R.string.add_product_label_alert_save_as_draft_dollar_and_video,
                getString(R.string.product_add_label_alert_dialog_dollar, gm)))
        builder.setCancelable(true)
        builder.setPositiveButton(R.string.change) { dialog, _ ->
            dialog.cancel()
            setResult(true)
        }
        builder.setNegativeButton(R.string.close) { dialog, _ -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
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
        if (selectedCurrencyType == CurrencyTypeDef.TYPE_IDR) {
            counterEditText.addTextChangedListener(idrTextWatcher)
        } else {
            counterEditText.removeTextChangedListener(idrTextWatcher)
        }
    }

    private fun setLabelViewWholesale(wholesaleList: ArrayList<ProductWholesaleViewModel>){
        labelViewWholesale.setContent(if (wholesaleList.size == 0) getString(R.string.label_add) else getString(R.string.product_count_wholesale, wholesaleList.size))
    }

    private fun setEditTextPriceState(wholesaleList: ArrayList<ProductWholesaleViewModel>){
        if (wholesaleList.size == 0 && !hasVariant) {
            setEnablePriceForm(true)
            counterEditText.editText.setSelection(
                    counterEditText.editText.text.length)
        } else {
            setEnablePriceForm(false)
        }
    }

    private fun setEnablePriceForm(isEnabled : Boolean){
        counterEditText.isEnabled = isEnabled
        imageViewEdit.visibility = if(isEnabled) View.GONE else View.VISIBLE
    }

    private fun saveData(productPrice: ProductPrice) = productPrice.apply {
            currencyType = selectedCurrencyType
            price = getPriceValue()
            wholesalePrice = this@ProductEditPriceFragment.wholesalePrice
            minOrder = getMinOrderValue().toInt()
            maxOrder = getMaxOrderValue().toInt()
        }

    private fun isDataValid(): Boolean{
        if(!isPriceValid()){
            counterEditText.requestFocus()
            UnifyTracking.eventAddProductError(activity, AppEventTracking.AddProduct.FIELDS_MANDATORY_PRICE)
            return false
        }
        if(!isMinOrderValid()){
            editTextMinOrder.requestFocus()
            UnifyTracking.eventAddProductError(activity, AppEventTracking.AddProduct.FIELDS_MANDATORY_MIN_PURCHASE)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_PRODUCT_PRICE, saveData(productPrice))
    }

    companion object {
        const val SAVED_PRODUCT_PRICE = "SAVED_PRODUCT_PRICE"
        const val DEFAULT_PRICE = 0.0
        const val MIN_ORDER = "1"
        const val MAX_ORDER = "10,000"
        const val REQUEST_CODE_GET_WHOLESALE = 1
        fun createInstance() = ProductEditPriceFragment()
    }
}
