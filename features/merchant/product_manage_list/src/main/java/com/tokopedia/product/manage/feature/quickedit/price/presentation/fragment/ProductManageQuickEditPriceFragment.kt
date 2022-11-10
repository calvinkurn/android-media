package com.tokopedia.product.manage.feature.quickedit.price.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.common.feature.list.data.model.PriceUiModel
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MAXIMUM_PRICE_LENGTH
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MINIMUM_PRICE
import com.tokopedia.product.manage.databinding.FragmentQuickEditPriceBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.CurrencyIdrTextWatcher

class ProductManageQuickEditPriceFragment(private var onFinishedListener: OnFinishedListener? = null,
                                          private var product: ProductUiModel? = null) : BottomSheetUnify() {

    companion object {
        private const val KEY_CACHE_MANAGER_ID = "cache_manager_id"
        private const val KEY_PRODUCT = "product"
        private const val KEY_IS_MULTILOCATION = "is_multilocation"

        fun createInstance(context: Context,
                           product: ProductUiModel,
                           isMultiLocation: Boolean = false,
                           onFinishedListener: OnFinishedListener) : ProductManageQuickEditPriceFragment {
            return ProductManageQuickEditPriceFragment(onFinishedListener, product).apply {
                SaveInstanceCacheManager(context, KEY_CACHE_MANAGER_ID).apply {
                    put(KEY_IS_MULTILOCATION, isMultiLocation)
                }
            }
        }
    }

    private var isMultiLocation = false

    private var binding by autoClearedNullable<FragmentQuickEditPriceBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cacheManager: SaveInstanceCacheManager?
        if (savedInstanceState == null) {
            cacheManager = context?.let { SaveInstanceCacheManager(it, KEY_CACHE_MANAGER_ID) }
        } else {
            val cacheManagerId = savedInstanceState.getString(KEY_CACHE_MANAGER_ID).orEmpty()
            cacheManager = context?.let { SaveInstanceCacheManager(it, cacheManagerId) }
            product =
                cacheManager?.get<ProductUiModel>(KEY_PRODUCT, ProductUiModel::class.java, null)
        }
        isMultiLocation =
            cacheManager?.get(KEY_IS_MULTILOCATION, Boolean::class.java, false).orFalse()

        val view = View.inflate(context, R.layout.fragment_quick_edit_price,null)
        setChild(view)
        setTitle(getString(R.string.product_manage_menu_set_price))
        setStyle(DialogFragment.STYLE_NORMAL, com.tokopedia.product.manage.common.R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuickEditPriceBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMultilocationTicker()
        product?.minPrice?.price?.let { initView(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.put(KEY_PRODUCT, product)
        cacheManager?.put(KEY_IS_MULTILOCATION, isMultiLocation)
        outState.putString(KEY_CACHE_MANAGER_ID, cacheManager?.id.orEmpty())
    }

    private fun setupMultilocationTicker() {
        binding?.tickerProductManageEditPriceMultiloc?.showWithCondition(isMultiLocation)
    }

    private fun initView(currentPrice: String) {
        context?.let {
            binding?.quickEditPriceTextField?.prependText(it.resources.getString(R.string.product_manage_quick_edit_currency))
        }
        binding?.quickEditPriceTextField?.run {
            textFieldInput.filters = arrayOf(InputFilter.LengthFilter(MAXIMUM_PRICE_LENGTH))
            textFieldInput.setText(CurrencyFormatHelper.removeCurrencyPrefix(CurrencyFormatHelper.convertToRupiah(currentPrice)))
            setFirstIcon(com.tokopedia.unifyicon.R.drawable.ic_system_action_close_normal_24)
            setInputType(InputType.TYPE_CLASS_NUMBER)
            getFirstIcon().setOnClickListener {
                binding?.quickEditPriceTextField?.textFieldInput?.text?.clear()
            }
            textFieldInput.setOnEditorActionListener { _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    val editedPrice = PriceUiModel(textFieldInput.text.toString(), product?.minPrice?.priceFormatted)
                    product = product?.copy(minPrice = editedPrice)
                    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(textFieldInput.windowToken, 0)
                }
                true
            }
            val idrTextWatcher = CurrencyIdrTextWatcher(this.textFieldInput)
            textFieldInput.addTextChangedListener(idrTextWatcher)
            textFieldInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val input = textFieldInput.text.toString()
                    val price = CurrencyFormatHelper.convertRupiahToDouble(input)

                    if(price < MINIMUM_PRICE) {
                        showErrorPriceTooLow()
                    } else {
                        hideError()
                    }
                }

            })
            setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    activity.let {
                        KeyboardHandler.showSoftKeyboard(it)
                    }
                } else {
                    activity.let {
                        KeyboardHandler.hideSoftKeyboard(it)
                    }
                }
            }
        }
        binding?.quickEditPriceTextField?.requestFocus()
        binding?.quickEditPriceSaveButton?.setOnClickListener {
            isPriceValid()
            ProductManageTracking.eventEditPriceSave(product?.id.orEmpty())
        }
    }

    private fun isPriceTooLow(): Boolean {
        val currentPrice = product?.minPrice?.price.toDoubleOrZero()
        if (currentPrice < MINIMUM_PRICE) return true
        return false
    }

    private fun showErrorPriceTooLow() {
        binding?.quickEditPriceTextField?.setError(true)
        context?.getString(R.string.product_manage_quick_edit_min_price_error)?.let {
            binding?.quickEditPriceTextField?.setMessage(it)
        }
    }

    private fun hideError() {
        binding?.quickEditPriceTextField?.run {
            setError(false)
            setMessage("")
        }
    }

    private fun isPriceValid() {
        product = product?.copy(
            minPrice = product?.minPrice?.copy(
                price = CurrencyFormatHelper.convertRupiahToLong(
                    binding?.quickEditPriceTextField?.textFieldInput?.text?.toString().orEmpty()).toString()))
        when {
            isPriceTooLow() -> {
                showErrorPriceTooLow()
                return
            }
            else -> {
                product?.run {
                    onFinishedListener?.onFinishEditPrice(this)
                    super.dismiss()
                }
            }
        }
    }

    fun setOnFinishedListener(onFinishedListener: OnFinishedListener) {
        this.onFinishedListener = onFinishedListener
    }

    interface OnFinishedListener {
        fun onFinishEditPrice(product: ProductUiModel)
    }

}