package com.tokopedia.shop.flashsale.presentation.creation.manage.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomsheetEditProductInfoBinding
import com.tokopedia.shop.flashsale.common.extension.*
import com.tokopedia.shop.flashsale.common.preference.SharedPreferenceDataStore
import com.tokopedia.shop.flashsale.common.util.DiscountUtil
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductErrorType
import com.tokopedia.shop.flashsale.domain.entity.enums.ProductInputValidationResult
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.WarehouseUiModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel.EditProductInfoViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class EditProductInfoBottomSheet: BottomSheetUnify() {

    companion object {
        private const val KEY_PRODUCTS = "PRODUCTS"
        private const val TAG = "EditProductInfoBottomSheet"
        private const val PRODUCT_REMAINING_VISIBILITY_THRESHOLD = 1
        private const val MAX_LENGTH_NUMBER_INPUT = 11
        private const val MAX_LENGTH_PERCENT_INPUT = 2
        private const val EMPTY_INITIAL_VALUE = ""

        fun newInstance(productList: List<SellerCampaignProductList.Product>): EditProductInfoBottomSheet {
            val fragment = EditProductInfoBottomSheet()
            fragment.arguments = Bundle().apply {
                putParcelableArrayList(KEY_PRODUCTS, ArrayList(productList))
            }
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: EditProductInfoViewModel
    @Inject
    lateinit var sharedPreference: SharedPreferenceDataStore
    private var binding by autoClearedNullable<SsfsBottomsheetEditProductInfoBinding>()
    private var warehouseBottomSheet: WarehouseBottomSheet? = null
    private var productIndex = Int.ZERO
    private var isDataFirstLoaded = true
    private var shouldLoadNextData = false
    private var onEditProductSuccessListener: () -> Unit = {}

    private val productList: ArrayList<SellerCampaignProductList.Product>? by lazy {
        arguments?.getParcelableArrayList(KEY_PRODUCTS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsBottomsheetEditProductInfoBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTitle(getString(R.string.editproduct_bs_title))
        setupContent()
        super.onViewCreated(view, savedInstanceState)

        setupProductObserver()
        setupWarehouseListObserver()
        setupIsShopMultiloc()
        setupErrorThrowableObserver()
        setupIsLoadingObserver()
        setupEditProductResultObserver()
        setupCampaignIsValidObserver()
        setupCampaignPriceInputObserver()
        handleCoachMark()
        loadNextData()
    }

    private fun setupCampaignPriceInputObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.campaignPrice.collect {
                if (!isDataFirstLoaded) binding?.tfCampaignPrice?.text = it.toString()
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.campaignPricePercent.collect {
                if (!isDataFirstLoaded) binding?.tfCampaignPricePercent?.text = it.toString()
            }
        }
    }

    @FlowPreview
    private fun setupCampaignIsValidObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isValid.collectLatest {
                val isSuccess = it.errorList.isEmpty()
                clearValidationResult()
                if (isDataFirstLoaded) {
                    // do not display validation on first loading product data
                    isDataFirstLoaded = false
                } else {
                    displayValidationResult(it)
                }

                binding?.btnSaveNext?.isEnabled = isSuccess
                binding?.btnSave?.isEnabled = isSuccess
            }
        }
    }

    private fun setupIsLoadingObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding?.btnSave?.isLoading = it
            binding?.btnSaveNext?.isLoading = it
            binding?.layoutInput?.setLayoutEnabled(!it)
        }
    }

    private fun setupDependencyInjection() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setupProductObserver() {
        viewModel.product.observe(viewLifecycleOwner) {
            binding?.apply {
                ivProduct.loadImage(it.imageUrl.img200)
                typographyProductName.text = it.productName
                typographyOriginalPrice.text = it.formattedPrice
                updateProductEditCounter()
                isDataFirstLoaded = true
            }
        }
    }

    private fun setupErrorThrowableObserver() {
        viewModel.errorThrowable.observe(viewLifecycleOwner) {
            val errorMessage = ErrorHandler.getErrorMessage(context ?: return@observe, it)
            displayError(errorMessage)
        }
    }

    private fun setupEditProductResultObserver() {
        viewModel.editProductResult.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                onEditDataSuccess()
            } else {
                displayError(it.errorMessage)
            }
        }
    }

    private fun setupWarehouseListObserver() {
        viewModel.warehouseList.observe(viewLifecycleOwner) { warehouseList ->
            warehouseList.firstOrNull { it.isSelected }?.let { selectedWarehouse ->
                viewModel.productInputData.apply {
                    warehouseId = selectedWarehouse.id
                    originalStock = selectedWarehouse.stock
                }
                binding?.spinnerShopLocation?.text = selectedWarehouse.name
                binding?.tfStock?.setMessage(getString(R.string.editproduct_stock_total_text, selectedWarehouse.stock))
            }

            warehouseBottomSheet = WarehouseBottomSheet.newInstance(warehouseList).apply {
                setOnSubmitListener(::onSubmitWarehouse)
            }
        }
    }

    private fun setupIsShopMultiloc() {
        viewModel.isShopMultiloc.observe(viewLifecycleOwner) {
            binding?.spinnerShopLocation?.isVisible = it
        }
    }

    private fun setupContent() {
        binding?.run {
            setupShopLocationSpinner(this)
            setupSaveButtons(this)
            setupTextInputViews(this)
            setFieldsInputMode(this)
        }
    }

    private fun setupShopLocationSpinner(binding: SsfsBottomsheetEditProductInfoBinding) {
        binding.spinnerShopLocation.setOnClickListener {
            warehouseBottomSheet?.show(childFragmentManager)
        }
    }

    private fun setFieldsInputMode(binding: SsfsBottomsheetEditProductInfoBinding) {
        with(binding) {
            tfCampaignPrice.textField.setModeToNumberDelimitedInput(MAX_LENGTH_NUMBER_INPUT)
            tfCampaignPricePercent.textField.setModeToNumberDelimitedInput(MAX_LENGTH_PERCENT_INPUT)
            tfStock.setModeToNumberDelimitedInput(MAX_LENGTH_NUMBER_INPUT)
            tfMaxSold.setModeToNumberDelimitedInput(MAX_LENGTH_NUMBER_INPUT)
        }
    }

    private fun setupTextInputViews(binding: SsfsBottomsheetEditProductInfoBinding) {
        with(binding) {
            switchPrice.setOnCheckedChangeListener { _, isChecked ->
                tfCampaignPrice.enabledEditing = !isChecked
                tfCampaignPricePercent.enabledEditing = isChecked
                viewModel.isUsingPercentage = isChecked
            }
            tfCampaignPrice.textField?.editText?.afterTextChanged {
                if (switchPrice.isChecked || isDataFirstLoaded) return@afterTextChanged
                else viewModel.setCampaignPrice(it.filterDigit())
            }
            tfCampaignPricePercent.textField?.editText?.afterTextChanged {
                if (!switchPrice.isChecked || isDataFirstLoaded) return@afterTextChanged
                else viewModel.setCampaignPricePercent(it.filterDigit())
            }
            tfStock.editText.afterTextChanged {
                viewModel.setCampaignStock(it.filterDigit())
            }
            tfMaxSold.editText.afterTextChanged {
                viewModel.setCampaignMaxOrder(it.filterDigit())
            }
        }
    }

    private fun setupSaveButtons(binding: SsfsBottomsheetEditProductInfoBinding) {
        binding.btnSaveNext.setOnClickListener {
            if (binding.btnSaveNext.isLoading) return@setOnClickListener
            val shouldLoadNextData = productIndex.inc() < productList?.size.orZero()
            submitInput(shouldLoadNextData)
        }
        binding.btnSave.setOnClickListener {
            if (binding.btnSaveNext.isLoading) return@setOnClickListener
            submitInput(shouldLoadNextData = false)
        }
    }

    private fun submitInput(shouldLoadNextData: Boolean) {
        this.shouldLoadNextData = shouldLoadNextData
        binding?.run {
            productIndex++
            viewModel.editProduct()
        }
    }

    private fun loadNextData() {
        val product = productList?.getOrNull(productIndex)
        viewModel.setProduct(product ?: return)
        resetInputData()
    }

    private fun displayValidationResult(validationResult: ProductInputValidationResult) {
        validationResult.errorList.forEach { errorType ->
            when(errorType) {
                ManageProductErrorType.EMPTY_PRICE -> {
                    displayPriceEmpty()
                }
                ManageProductErrorType.MAX_DISCOUNT_PRICE -> {
                    displayValidationPrice(validationResult, isMaxError = true)
                }
                ManageProductErrorType.MIN_DISCOUNT_PRICE -> {
                    displayValidationPrice(validationResult, isMinError = true)
                }
                ManageProductErrorType.MAX_STOCK -> {
                    displayValidationStock(validationResult, isMaxError = true)
                }
                ManageProductErrorType.MIN_STOCK -> {
                    displayValidationStock(validationResult, isMinError = true)
                }
                ManageProductErrorType.MAX_ORDER -> {
                    displayValidationOrder(validationResult, isMaxError = true)
                }
                ManageProductErrorType.MIN_ORDER -> {
                    displayValidationOrder(validationResult, isMinError = true)
                }
                else -> {}
            }
        }
    }

    private fun displayPriceEmpty() {
        binding?.apply {
            val usingPercentInput = switchPrice.isChecked.orFalse()
            val priceField = if (usingPercentInput) {
                tfCampaignPricePercent.textField
            } else {
                tfCampaignPrice.textField
            }
            priceField?.isInputError = true
            priceField?.setMessage(getString(R.string.editproduct_required_text))
        }
    }

    private fun displayValidationOrder(
        validationResult: ProductInputValidationResult,
        isMinError: Boolean = false,
        isMaxError: Boolean = false
    ) {
        val orderField = binding?.tfMaxSold
        orderField?.isInputError = true

        when {
            isMaxError -> {
                orderField?.setMessage(getString(R.string.editproduct_order_exceed_stock_error))
            }
            isMinError -> {
                orderField?.setMessage(getString(R.string.editproduct_min_prefix) + validationResult.minOrder)
            }
        }
    }

    private fun displayValidationPrice(
        validationResult: ProductInputValidationResult,
        isMinError: Boolean = false,
        isMaxError: Boolean = false
    ) {
        val usingPercentInput = binding?.switchPrice?.isChecked.orFalse()
        val priceField: TextFieldUnify2?
        val maxErrorValue: String
        val minErrorValue: String

        if (usingPercentInput) {
            priceField = binding?.tfCampaignPricePercent?.textField
            maxErrorValue = getString(R.string.editproduct_min_percent_text, validationResult.minPricePercent)
            minErrorValue = getString(R.string.editproduct_max_percent_text, validationResult.maxPricePercent)
        } else {
            priceField = binding?.tfCampaignPrice?.textField
            maxErrorValue = getString(R.string.editproduct_max_prefix) + validationResult.maxPrice.getCurrencyFormatted()
            minErrorValue = getString(R.string.editproduct_min_prefix) + validationResult.minPrice.getCurrencyFormatted()
        }

        priceField?.isInputError = true

        when {
            isMaxError -> {
                priceField?.setMessage(maxErrorValue)
            }
            isMinError -> {
                priceField?.setMessage(minErrorValue)
            }
        }
    }

    private fun displayValidationStock(
        validationResult: ProductInputValidationResult,
        isMinError: Boolean = false,
        isMaxError: Boolean = false
    ) {
        val stockField = binding?.tfStock
        stockField?.isInputError = true

        when {
            isMaxError -> {
                stockField?.setMessage(getString(R.string.editproduct_max_prefix) + validationResult.maxStock)
            }
            isMinError -> {
                stockField?.setMessage(getString(R.string.editproduct_min_prefix) + validationResult.minStock)
            }
        }
    }

    private fun clearValidationResult() {
        binding?.apply {
            tfCampaignPrice.textField?.isInputError = false
            tfCampaignPricePercent.textField?.isInputError = false
            tfStock.isInputError = false
            tfMaxSold.isInputError = false
            tfCampaignPrice.textField?.setMessage("")
            tfCampaignPricePercent.textField?.setMessage("")
            tfStock.setMessage(getString(R.string.editproduct_stock_total_text, viewModel.productInputData.originalStock))
            tfMaxSold.setMessage(getString(R.string.editproduct_input_max_transaction_message))
        }
    }

    private fun resetInputData() {
        val productInput = viewModel.productInputData
        val originalPrice = productInput.productMapData.originalPrice.toInt()
        val discountedPrice = productInput.price.orZero().toStringOrInitialValue(EMPTY_INITIAL_VALUE)
        val discountedPricePercent = productInput.price?.let {
            DiscountUtil.getDiscountPercentThresholded(it, originalPrice)
        }
        val customStock = productInput.stock.orZero().toStringOrInitialValue(EMPTY_INITIAL_VALUE)
        val maxOrder = productInput.maxOrder.orZero().toLong().toStringOrInitialValue(EMPTY_INITIAL_VALUE)

        viewModel.setCampaignPrice(discountedPrice)
        viewModel.setCampaignStock(customStock)
        viewModel.setCampaignMaxOrder(maxOrder)
        binding?.apply {
            tfCampaignPrice.text = discountedPrice
            tfCampaignPricePercent.text = discountedPricePercent?.toString() ?: EMPTY_INITIAL_VALUE
            tfStock.editText.setText(customStock)
            tfMaxSold.editText.setText(maxOrder)
        }
    }

    private fun displayError(errorMessage: String) {
        binding?.tickerError?.setHtmlDescription(errorMessage)
        binding?.tickerError?.show()
    }

    private fun onSubmitWarehouse(warehouseList: List<WarehouseUiModel>) {
        viewModel.setWarehouseList(warehouseList)
    }

    private fun onEditDataSuccess() {
        if (shouldLoadNextData) {
            loadNextData()
        } else {
            dismiss()
            onEditProductSuccessListener()
        }
    }

    private fun updateProductEditCounter() {
        val productCount = productList?.size.orZero()
        val counter = productCount - productIndex
        binding?.typographyProductRemaining?.apply {
            isVisible = productCount > PRODUCT_REMAINING_VISIBILITY_THRESHOLD
            text = getString(R.string.editproduct_counter_text, counter)
        }
    }

    private fun Long.toStringOrInitialValue(initialValue: String): String {
        return if (isMoreThanZero()) {
            toString()
        } else {
            initialValue
        }
    }

    private fun handleCoachMark() {
        val shouldShowCoachMark = !sharedPreference.isEditProductCoachMarkDismissed()
        if (shouldShowCoachMark) {
            showCoachMark()
        }
    }

    private fun showCoachMark() {
        val coachMark = CoachMark2(requireActivity())
        coachMark.showCoachMark(populateCoachMarkItems(), null)
        coachMark.onFinishListener = {
            sharedPreference.markEditProductCoachMarkComplete()
        }
        coachMark.onDismissListener = {
            sharedPreference.markEditProductCoachMarkComplete()
        }
    }

    private fun populateCoachMarkItems(): java.util.ArrayList<CoachMark2Item> {
        val firstAnchorView = binding?.layoutCampaignPrice ?: return arrayListOf()
        val secondAnchorView = binding?.tfStock ?: return arrayListOf()
        val thirdAnchorView = binding?.btnSaveNext ?: return arrayListOf()

        return arrayListOf(
            CoachMark2Item(
                firstAnchorView,
                getString(R.string.editproduct_first_coachmark_content),
                "",
                CoachMark2.POSITION_BOTTOM
            ),
            CoachMark2Item(
                secondAnchorView,
                getString(R.string.editproduct_second_coachmark_content),
                "",
                CoachMark2.POSITION_TOP
            ),
            CoachMark2Item(
                thirdAnchorView,
                getString(R.string.editproduct_third_coachmark_content),
                "",
                CoachMark2.POSITION_TOP
            )
        )
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    fun setOnEditProductSuccessListener(listener: () -> Unit) {
        onEditProductSuccessListener = listener
    }
}
