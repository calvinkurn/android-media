package com.tokopedia.product.manage.list.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import com.tokopedia.design.text.CounterInputView
import com.tokopedia.product.manage.item.common.util.CurrencyIdrTextWatcher
import com.tokopedia.product.manage.item.common.util.CurrencyTypeDef
import com.tokopedia.product.manage.item.utils.ProductPriceRangeUtils
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.utils.ProductManageTracking
import kotlinx.android.synthetic.main.fragment_dialog_product_manage_edit_price.view.*
import java.util.*

class ProductManageEditPriceDialogFragment : DialogFragment() {

    private var productId: String? = ""
    private var productPrice: String? = ""
    private var productCurrencyId: Int = 0
    private var isGoldMerchant: Boolean = false
    private var isOfficialStore: Boolean = false
    private var listenerDialogEditPrice: ListenerDialogEditPrice? = null

    lateinit var counterInputView: CounterInputView

    interface ListenerDialogEditPrice {
        fun onSubmitEditPrice(productId: String, price: String, currencyId: String, currencyText: String)
    }

    companion object {

        private const val PRODUCT_ID = "product_id"
        private const val PRODUCT_PRICE = "product_price"
        private const val PRODUCT_CURRENCY_ID = "product_currency_id"
        private const val IS_OFFICIAL_STORE = "isOfficialStore"
        private const val CURRENCY_ID = "1"
        private const val CURRENCY_TEXT = "Rp"

        val IS_GOLD_MERCHANT = "isGoldMerchant"

        fun createInstance(productId: String, productPrice: String,
                           productCurrencyId: Int, isGoldMerchant: Boolean, isOfficialStore: Boolean) = ProductManageEditPriceDialogFragment().also {
            it.arguments = Bundle().apply {
                putString(PRODUCT_ID, productId)
                putString(PRODUCT_PRICE, productPrice)
                putInt(PRODUCT_CURRENCY_ID, productCurrencyId)
                putBoolean(IS_GOLD_MERCHANT, isGoldMerchant)
                putBoolean(IS_OFFICIAL_STORE, isOfficialStore)
            }
        }
    }

    fun setListenerDialogEditPrice(listenerDialogEditPrice: ListenerDialogEditPrice) {
        this.listenerDialogEditPrice = listenerDialogEditPrice
    }

    private fun initData() {
        arguments?.run {
            productId = getString(PRODUCT_ID)
            productPrice = getString(PRODUCT_PRICE)
            productCurrencyId = getInt(PRODUCT_CURRENCY_ID)
            isGoldMerchant = getBoolean(IS_GOLD_MERCHANT)
            isOfficialStore = getBoolean(IS_OFFICIAL_STORE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.let {
            val root = RelativeLayout(activity)
            root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(root)
            it.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return dialog
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.product.manage.list.R.layout.fragment_dialog_product_manage_edit_price, container, false)
        counterInputView = view.counter_input_view
        view.string_picker_dialog_confirm.setOnClickListener {
            if (isPriceValid() && listenerDialogEditPrice != null) {
                ProductManageTracking.eventProductManageOverflowMenu(getString(com.tokopedia.product.manage.list.R.string.product_manage_menu_set_price)
                        + " - " + view.string_picker_dialog_confirm.text)
                listenerDialogEditPrice?.onSubmitEditPrice(productId ?: "",
                        formatDecimal(counterInputView.doubleValue), CURRENCY_ID, CURRENCY_TEXT)
                dismiss()
            } else {
                view.counter_input_view.requestFocus()
            }
        }

        view.string_picker_dialog_cancel.setOnClickListener {
            dismiss()
            ProductManageTracking.eventProductManageOverflowMenu(getString(com.tokopedia.product.manage.list.R.string.product_manage_menu_set_price)
                    + " - " + view.string_picker_dialog_confirm.text)
        }


        val idrTextWatcher = object : CurrencyIdrTextWatcher(counterInputView.editText) {
            override fun onNumberChanged(number: Double) {
                isPriceValid()
            }
        }

        view.counter_input_view.addTextChangedListener(idrTextWatcher)
        productPrice?.toDouble()?.let { view.counter_input_view.setValue(it) }

        return view
    }

    private fun isPriceValid(): Boolean {
        val priceValue = counterInputView.doubleValue
        val currencyType = CurrencyTypeDef.TYPE_IDR
        if (!ProductPriceRangeUtils.isPriceValid(priceValue, currencyType, isOfficialStore)) {
            counterInputView.setError(
                    counterInputView.context.getString(com.tokopedia.product.manage.list.R.string.product_manage_error_product_price_not_valid,
                            ProductPriceRangeUtils.getMinPriceString(currencyType, isOfficialStore),
                            ProductPriceRangeUtils.getMaxPriceString(currencyType, isOfficialStore)))
            return false
        }
        counterInputView.setError(null)
        return true
    }

    private fun formatDecimal(productPrice: Double): String {
        return if (productPrice == productPrice.toLong().toDouble())
            String.format(Locale.US, "%d", productPrice.toLong())
        else
            String.format("%s", productPrice)
    }
}