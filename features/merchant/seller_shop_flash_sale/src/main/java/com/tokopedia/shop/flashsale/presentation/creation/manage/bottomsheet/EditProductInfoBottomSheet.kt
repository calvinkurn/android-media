package com.tokopedia.shop.flashsale.presentation.creation.manage.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomsheetEditProductInfoBinding
import com.tokopedia.shop.flashsale.common.extension.disableEnableControls
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductErrorType
import com.tokopedia.shop.flashsale.domain.entity.enums.ProductInputValidationResult
import com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel.EditProductInfoViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class EditProductInfoBottomSheet: BottomSheetUnify() {

    companion object {
        private const val KEY_PRODUCTS = "PRODUCTS"
        private const val TAG = "EditProductInfoBottomSheet"

        fun newInstance(productList: ArrayList<SellerCampaignProductList.Product>): EditProductInfoBottomSheet {
            val fragment = EditProductInfoBottomSheet()
            fragment.arguments = Bundle().apply {
                putParcelableArrayList(KEY_PRODUCTS, productList)
            }
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: EditProductInfoViewModel

    private var binding by autoClearedNullable<SsfsBottomsheetEditProductInfoBinding>()
    private var warehouseBottomSheet: WarehouseBottomSheet? = null
    private val productList: ArrayList<SellerCampaignProductList.Product>? by lazy {
        arguments?.getParcelableArrayList(KEY_PRODUCTS)
    }
    private var productInput = SellerCampaignProductList.ProductMapData()
    private var productIndex = Int.ZERO
    private var isDataFirstLoaded = true
    private var shouldLoadNextData = false

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
        setTitle("Ubah informasi produk")
        setupContent()
        super.onViewCreated(view, savedInstanceState)

        setupProductObserver()
        setupProductMapDataObserver()
        setupValidationResultObserver()
        setupWarehouseListObserver()
        setupHasWarehouseObserver()
        setupErrorThrowableObserver()
        setupIsLoadingObserver()
        setupEditProductResultObserver()
        loadNextData()
    }

    private fun setupIsLoadingObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding?.btnSave?.isLoading = it
            binding?.btnSaveNext?.isLoading = it
            disableEnableControls(!it, binding?.layoutInput)
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

    private fun setupProductMapDataObserver() {
        viewModel.productMapData.observe(viewLifecycleOwner) {
            productInput = it
            resetInputData()
        }
    }

    private fun setupValidationResultObserver() {
        viewModel.validationResult.observe(viewLifecycleOwner) { validationResult ->
            val isSuccess = validationResult.errorList.isEmpty()
            clearValidationResult()
            if (isDataFirstLoaded) {
                // do not display validation on first loading product data
                isDataFirstLoaded = false
            } else {
                displayValidationResult(validationResult)
                if (isSuccess) {
                    productIndex++
                    viewModel.editProduct(productInput.campaignId)
                }
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
        viewModel.warehouseList.observe(viewLifecycleOwner) {
            warehouseBottomSheet = WarehouseBottomSheet.newInstance(ArrayList(it))
        }
    }

    private fun setupHasWarehouseObserver() {
        viewModel.hasWarehouse.observe(viewLifecycleOwner) {
            binding?.spinnerShopLocation?.isVisible = it
        }
    }

    private fun setupContent() {
        binding?.run {
            spinnerShopLocation.setOnClickListener {
                warehouseBottomSheet?.show(childFragmentManager)
            }
            switchPrice.setOnCheckedChangeListener { _, isChecked ->
                tfCampaignPrice.enabledEditing = !isChecked
                tfCampaignPricePercent.enabledEditing = isChecked
            }
            btnSaveNext.setOnClickListener {
                if (btnSaveNext.isLoading) return@setOnClickListener
                val shouldLoadNextData = productIndex.inc() < productList?.size.orZero()
                submitInput(shouldLoadNextData)
            }
            btnSave.setOnClickListener {
                if (btnSaveNext.isLoading) return@setOnClickListener
                submitInput(shouldLoadNextData = false)
            }
        }
    }

    private fun submitInput(shouldLoadNextData: Boolean) {
        this.shouldLoadNextData = shouldLoadNextData
        binding?.run {
            productInput.customStock = tfStock.editText.text.toString().toLongOrZero()
            productInput.maxOrder = tfMaxSold.editText.text.toString().toIntSafely()
            productInput.discountedPrice = tfCampaignPrice.textField?.editText?.text.toString().toLongOrZero()
            viewModel.setProductInput(productInput)
        }
    }

    private fun loadNextData() {
        val product = productList?.getOrNull(productIndex)
        viewModel.setProduct(product ?: return)
    }

    private fun displayValidationResult(validationResult: ProductInputValidationResult) {
        validationResult.errorList.forEach { errorType ->
            when(errorType) {
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

    private fun displayValidationOrder(
        validationResult: ProductInputValidationResult,
        isMinError: Boolean = false,
        isMaxError: Boolean = false
    ) {
        val orderField = binding?.tfMaxSold
        orderField?.isInputError = true

        when {
            orderField?.editText?.text?.toString().orEmpty().isEmpty() -> {
                orderField?.setMessage("Wajib diisi")
            }
            isMaxError -> {
                orderField?.setMessage("Tidak boleh melebihi stok utama")
            }
            isMinError -> {
                orderField?.setMessage("Min. " + validationResult.minOrder)
            }
        }
    }

    private fun displayValidationPrice(
        validationResult: ProductInputValidationResult,
        isMinError: Boolean = false,
        isMaxError: Boolean = false
    ) {
        val priceField = binding?.tfCampaignPrice?.textField
        priceField?.isInputError = true

        when {
            priceField?.editText?.text?.toString().orEmpty().isEmpty() -> {
                priceField?.setMessage("Wajib diisi")
            }
            isMaxError -> {
                priceField?.setMessage("Maks. " + validationResult.maxPrice.getCurrencyFormatted())
            }
            isMinError -> {
                priceField?.setMessage("Min. " + validationResult.minPrice.getCurrencyFormatted())
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
            stockField?.editText?.text?.toString().orEmpty().isEmpty() -> {
                stockField?.setMessage("Wajib diisi")
            }
            isMaxError -> {
                stockField?.setMessage("Maks. " + validationResult.maxStock)
            }
            isMinError -> {
                stockField?.setMessage("Min. " + validationResult.minStock)
            }
        }
    }

    private fun clearValidationResult() {
        binding?.apply {
            tfCampaignPrice.textField?.isInputError = false
            tfStock.isInputError = false
            tfMaxSold.isInputError = false
            tfCampaignPrice.textField?.setMessage("")
            tfStock.setMessage("Total Stok: " + productInput.originalStock)
            tfMaxSold.setMessage("Batas maks. pembelian produk")
        }
    }

    private fun resetInputData() {
        binding?.apply {
            tfCampaignPrice.text = productInput.discountedPrice.toString()
            tfStock.editText.setText(productInput.customStock.toString())
            tfMaxSold.editText.setText(productInput.maxOrder.toString())
            tfStock.setMessage("Total stok ${productInput.originalStock}")
        }
    }

    private fun displayError(errorMessage: String) {
        binding?.tickerError?.setHtmlDescription(errorMessage)
        binding?.tickerError?.show()
    }

    private fun onEditDataSuccess() {
        if (shouldLoadNextData) {
            loadNextData()
        } else {
            dismiss()
        }
    }

    private fun updateProductEditCounter() {
        val counter = productList?.size.orZero() - productIndex
        binding?.typographyProductRemaining?.text = "Produk dengan info belum lengkap: $counter"
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }
}
