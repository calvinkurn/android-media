package com.tokopedia.product.addedit.detail.presentation.dialog

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.InputPriceUtil
import com.tokopedia.product.addedit.common.util.StringValidationUtil.filterDigit
import com.tokopedia.product.addedit.common.util.getText
import com.tokopedia.product.addedit.common.util.setHtmlMessage
import com.tokopedia.product.addedit.common.util.setText
import com.tokopedia.product.addedit.databinding.BottomsheetPriceSuggestionLayoutBinding
import com.tokopedia.product.addedit.detail.presentation.adapter.SimilarProductAdapter
import com.tokopedia.product.addedit.detail.presentation.model.PriceSuggestion
import com.tokopedia.product.addedit.detail.presentation.viewholder.SimilarProductViewHolder
import com.tokopedia.product.addedit.tracking.ProductEditMainTracking
import com.tokopedia.unifycomponents.BottomSheetUnify

class PriceSuggestionBottomSheet : BottomSheetUnify(), SimilarProductViewHolder.ClickListener {

    interface Listener {
        fun onPriceSuggestionInfoCtaClick()
        fun onPriceTextInputChanged(priceInput: String)
        fun onSaveButtonClick(priceInput: String)
    }

    companion object {

        private const val TAG = "PriceSuggestionBottomSheet"
        private const val BUNDLE_KEY_IS_EDITING = "IS_EDITING"
        private const val BUNDLE_KEY_PRODUCT_ID = "PRODUCT_ID"
        private const val BUNDLE_KEY_PRICE_INPUT = "PRICE_INPUT"
        private const val BUNDLE_KEY_PRICE_SUGGESTION = "PRICE_SUGGESTION"

        @JvmStatic
        fun createInstance(isEditing: Boolean, productId: String, priceInput: String, priceSuggestion: PriceSuggestion): PriceSuggestionBottomSheet {
            return PriceSuggestionBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(BUNDLE_KEY_IS_EDITING, isEditing)
                    putString(BUNDLE_KEY_PRODUCT_ID, productId)
                    putString(BUNDLE_KEY_PRICE_INPUT, priceInput)
                    putParcelable(BUNDLE_KEY_PRICE_SUGGESTION, priceSuggestion)
                }
            }
        }
    }

    private var binding: BottomsheetPriceSuggestionLayoutBinding? = null

    private var adapter: SimilarProductAdapter? = null

    private var listener: Listener? = null

    private val isEditing: Boolean by lazy {
        arguments?.getBoolean(BUNDLE_KEY_IS_EDITING) ?: false
    }

    private val productId: String by lazy {
        arguments?.getString(BUNDLE_KEY_PRODUCT_ID).orEmpty()
    }

    private val priceInput: String by lazy {
        arguments?.getString(BUNDLE_KEY_PRICE_INPUT).orEmpty()
    }

    private val priceSuggestion: PriceSuggestion by lazy {
        arguments?.getParcelable(BUNDLE_KEY_PRICE_SUGGESTION) ?: PriceSuggestion()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupBottomSheet()
        val viewBinding = BottomsheetPriceSuggestionLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setChild(viewBinding.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet() {
        setTitle(getString(R.string.label_price_recommendation))
        isFullpage = true
//        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        clearContentPadding = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(binding)
        renderData(priceSuggestion, binding)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupViews(binding: BottomsheetPriceSuggestionLayoutBinding?) {

        binding?.root?.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        // setup recyclerview
        adapter = SimilarProductAdapter(this)
        binding?.rvSimilarProducts?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(
                    it.context
            )
        }
        // setup click listeners
        binding?.iuCtaInformation?.setOnClickListener {
            ProductEditMainTracking.sendClickPriceSuggestionPopUpAboutPriceSuggestionEvent(isEditing)
            listener?.onPriceSuggestionInfoCtaClick()
        }
        binding?.tfuProductPrice?.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // clean any kind of number formatting here
                val productPriceInput = charSequence?.toString()?.filterDigit()
                productPriceInput?.let {
                    // do the validation first
                    listener?.onPriceTextInputChanged(it)
                    binding.tfuProductPrice.editText.let { editText ->
                        InputPriceUtil.applyPriceFormatToInputField(editText, it, start, charSequence.length, count, this)
                        val updatedPrice = binding.tfuProductPrice.getText().toDoubleOrZero().getCurrencyFormatted()
                        ProductEditMainTracking.sendClickPriceSuggestionPopUpEditPriceEvent(
                                isEditing = isEditing,
                                productId = productId,
                                currentPrice = priceInput,
                                suggestedPrice = binding.tpgBestPrice.text.toString(),
                                priceRange = binding.tpgPriceSuggestionRange.text.toString(),
                                updatedPrice = updatedPrice
                        )
                    }
                }
            }
        })
        binding?.root?.setOnClickListener {
            this.context?.run {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
        binding?.tpgCtaApply?.setOnClickListener {
            val suggestedPrice = priceSuggestion.suggestedPrice?.getCurrencyFormatted()
            suggestedPrice?.run { binding.tfuProductPrice.setText(this) }
            ProductEditMainTracking.sendClickPriceSuggestionPopUpApplyEvent(
                    isEditing = isEditing,
                    productId = productId,
                    currentPrice = priceInput,
                    suggestedPrice = binding.tpgBestPrice.text.toString(),
                    priceRange = binding.tpgPriceSuggestionRange.text.toString()
            )
        }
        binding?.buttonSave?.setOnClickListener {
            val productPriceInput = binding.tfuProductPrice.editText.text.toString()
            listener?.onSaveButtonClick(productPriceInput)
            ProductEditMainTracking.sendClickPriceSuggestionPopUpSaveEvent(
                    isEditing = isEditing,
                    productId = productId,
                    currentPrice = priceInput,
                    suggestedPrice = binding.tpgBestPrice.text.toString(),
                    priceRange = binding.tpgPriceSuggestionRange.text.toString(),
                    updatedPrice = binding.tfuProductPrice.getText()
            )
        }
    }

    private fun renderData(priceSuggestion: PriceSuggestion, binding: BottomsheetPriceSuggestionLayoutBinding?) {
        // render price input from the previous page
        if (priceInput.isNotBlank()) binding?.tfuProductPrice?.setText(priceInput)
        // render price suggestion e.g. Rp12.478.000
        binding?.tpgBestPrice?.text = priceSuggestion.suggestedPrice?.getCurrencyFormatted()
        // render price suggestion range e.g. Rp11.500.000 - Rp13.500.000
        val minLimit = priceSuggestion.suggestedPriceMin?.getCurrencyFormatted()
        val maxLimit = priceSuggestion.suggestedPriceMax?.getCurrencyFormatted()
        val priceSuggestionRangeText = getString(R.string.price_suggestion_range, minLimit, maxLimit)
        binding?.tpgPriceSuggestionRange?.text = priceSuggestionRangeText
        // render competitive products layout
        val productSize = priceSuggestion.similarProducts.size
        if (productSize.isMoreThanZero()) {
            // render competitive product size e.g. 5 produk terkompetitif
            binding?.tpgProductCounter?.text = getString(R.string.label_competitive_product_size, productSize.toString())
            // render top 5 competitive products
            adapter?.setSimilarProducts(priceSuggestion.similarProducts)
        } else {
            binding?.competitiveProductsLayout?.gone()
        }
    }

    fun setClickListener(listener: Listener) {
        this.listener = listener
    }

    fun setPriceValidationResult(isError: Boolean, message: String) {
        binding?.tfuProductPrice?.apply {
            isInputError = isError
            setHtmlMessage(message)
        }
        binding?.buttonSave?.isEnabled = !isError
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isVisible) {
            show(fragmentManager, TAG)
        }
    }

    override fun onProductItemClickListener(adapterPosition: Int) {
        ProductEditMainTracking.sendClickPriceSuggestionPopUpSimilarProductEvent(
                isEditing = isEditing,
                row = adapterPosition.toString()
        )
    }
}