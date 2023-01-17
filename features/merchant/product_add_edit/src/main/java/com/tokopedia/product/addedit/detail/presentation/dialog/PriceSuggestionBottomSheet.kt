package com.tokopedia.product.addedit.detail.presentation.dialog

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.INFORMATION_ICON
import com.tokopedia.product.addedit.common.util.InputPriceUtil
import com.tokopedia.product.addedit.common.util.StringValidationUtil.filterDigit
import com.tokopedia.product.addedit.common.util.getText
import com.tokopedia.product.addedit.common.util.setHtmlMessage
import com.tokopedia.product.addedit.common.util.setText
import com.tokopedia.product.addedit.databinding.BottomsheetPriceSuggestionLayoutBinding
import com.tokopedia.product.addedit.detail.presentation.adapter.SimilarProductAdapter
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants
import com.tokopedia.product.addedit.detail.presentation.model.PriceSuggestion
import com.tokopedia.product.addedit.detail.presentation.viewholder.SimilarProductViewHolder
import com.tokopedia.product.addedit.tracking.ProductEditMainTracking
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

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
        isFullpage = false
        clearContentPadding = true
        isKeyboardOverlap = false
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(binding)
        renderData(priceSuggestion, binding)
        setupListeners(binding)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupViews(binding: BottomsheetPriceSuggestionLayoutBinding?) {
        // setup recyclerview
        adapter = SimilarProductAdapter(this)
        binding?.rvSimilarProducts?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(
                    it.context
            )
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun setupListeners(binding: BottomsheetPriceSuggestionLayoutBinding?) {
        binding?.apply {
            setupRootView(this)
            setupProductPriceField(this)
            setupCtaInformation(this)
            setupCtaApply(this)
            setupSaveButton(this)
        }
    }

    private fun setupRootView(binding: BottomsheetPriceSuggestionLayoutBinding) {
        binding.root.setOnClickListener {
            this.context?.run {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
                binding.aboutPriceRecommendationLayout.show()
            }
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun setupProductPriceField(binding: BottomsheetPriceSuggestionLayoutBinding) {
        binding.tfuProductPrice.editText.apply {
            observeProductPriceChangeWithDebounce()
                    .debounce(AddEditProductDetailConstants.DEBOUNCE_DELAY_MILLIS)
                    .filterNot {it.isBlank() }
                    .distinctUntilChanged()
                    .onEach {
                        ProductEditMainTracking.sendClickPriceSuggestionPopUpEditPriceEvent(
                                isEditing = isEditing,
                                productId = productId,
                                currentPrice = priceInput.filterDigit().toDoubleOrZero().getCurrencyFormatted(),
                                suggestedPrice = binding.tpgBestPrice.text.toString(),
                                priceRange = binding.tpgPriceSuggestionRange.text.toString(),
                                updatedPrice = it
                        )
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
            // hide "tentang rekomendasi harga" layout when the keyboard is visible
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) binding.aboutPriceRecommendationLayout.hide()
                else binding.aboutPriceRecommendationLayout.show()
            }
            setOnClickListener { binding.aboutPriceRecommendationLayout.hide() }
        }
        binding.tfuProductPrice.setKeyImeChangeCallback {
            binding.aboutPriceRecommendationLayout.show()
        }
    }

    private fun setupCtaInformation(binding: BottomsheetPriceSuggestionLayoutBinding) {
        binding.iuCtaInformation.setImageUrl(INFORMATION_ICON)
        binding.iuCtaInformation.setOnClickListener {
            ProductEditMainTracking.sendClickPriceSuggestionPopUpAboutPriceSuggestionEvent(isEditing)
            listener?.onPriceSuggestionInfoCtaClick()
        }
    }

    private fun setupCtaApply(binding: BottomsheetPriceSuggestionLayoutBinding) {
        binding.tpgCtaApply.setOnClickListener {
            val suggestedPrice = priceSuggestion.suggestedPrice?.getCurrencyFormatted()
            suggestedPrice?.run { binding.tfuProductPrice.setText(this) }
            ProductEditMainTracking.sendClickPriceSuggestionPopUpApplyEvent(
                    isEditing = isEditing,
                    productId = productId,
                    currentPrice = priceInput.filterDigit().toDoubleOrZero().getCurrencyFormatted(),
                    suggestedPrice = binding.tpgBestPrice.text.toString(),
                    priceRange = binding.tpgPriceSuggestionRange.text.toString()
            )
        }
    }

    private fun setupSaveButton(binding: BottomsheetPriceSuggestionLayoutBinding) {
        binding.buttonSave.setOnClickListener {
            val productPriceInput = binding.tfuProductPrice.editText.text.toString()
            listener?.onSaveButtonClick(productPriceInput)
            ProductEditMainTracking.sendClickPriceSuggestionPopUpSaveEvent(
                    isEditing = isEditing,
                    productId = productId,
                    currentPrice = priceInput.filterDigit().toDoubleOrZero().getCurrencyFormatted(),
                    suggestedPrice = binding.tpgBestPrice.text.toString(),
                    priceRange = binding.tpgPriceSuggestionRange.text.toString(),
                    updatedPrice = binding.tfuProductPrice.getText()
            )
        }
    }

    private fun renderData(priceSuggestion: PriceSuggestion, binding: BottomsheetPriceSuggestionLayoutBinding?) {
        // render price suggestion e.g. Rp12.478.000
        binding?.tpgBestPrice?.text = priceSuggestion.suggestedPrice?.getCurrencyFormatted()
        // render price suggestion range e.g. Rp11.500.000 - Rp13.500.000
        val minLimit = priceSuggestion.suggestedPriceMin?.getCurrencyFormatted()
        val maxLimit = priceSuggestion.suggestedPriceMax?.getCurrencyFormatted()
        val priceSuggestionRangeText = getString(R.string.price_suggestion_range, minLimit, maxLimit)
        binding?.tpgPriceSuggestionRange?.text = priceSuggestionRangeText
        // render price input from the previous page - render the last so the tracker works properly
        if (priceInput.isNotBlank()) binding?.tfuProductPrice?.setText(priceInput)
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
        if (!isVisible && !isAdded) {
            show(fragmentManager, TAG)
        }
    }

    @ExperimentalCoroutinesApi
    private fun EditText.observeProductPriceChangeWithDebounce(): Flow<String> {
        return callbackFlow {
            val listener = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    trySend(s.toString())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // clean any kind of number formatting here
                    val productPriceInput = s?.toString()?.filterDigit()
                    productPriceInput?.let {
                        // do the validation first
                        listener?.onPriceTextInputChanged(it)
                        binding?.tfuProductPrice?.editText?.let { editText ->
                            InputPriceUtil.applyPriceFormatToInputField(editText, it, start, s.length, count, this)
                        }
                    }
                }
            }
            addTextChangedListener(listener)
            awaitClose { removeTextChangedListener(listener) }
        }
    }

    override fun onProductItemClickListener(adapterPosition: Int) {
        ProductEditMainTracking.sendClickPriceSuggestionPopUpSimilarProductEvent(
                isEditing = isEditing,
                row = adapterPosition.toString()
        )
    }
}