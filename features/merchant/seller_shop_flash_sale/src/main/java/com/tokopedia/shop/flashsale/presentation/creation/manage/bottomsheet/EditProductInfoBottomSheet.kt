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
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductErrorType
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
    private var productIndex = Int.ZERO
    private val productList: ArrayList<SellerCampaignProductList.Product>? by lazy {
        arguments?.getParcelableArrayList(KEY_PRODUCTS)
    }
    private var productInput = SellerCampaignProductList.ProductMapData()

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
        setupErrorThrowable()
        loadNextData()
    }

    private fun setupProductObserver() {
        viewModel.product.observe(viewLifecycleOwner) {
            binding?.apply {
                ivProduct.loadImage(it.imageUrl.img200)
                typographyProductName.text = it.productName
                typographyOriginalPrice.text = it.formattedPrice
            }
        }
    }

    private fun setupProductMapDataObserver() {
        viewModel.productMapData.observe(viewLifecycleOwner) {
            binding?.tfStock?.setMessage("Total stok ${it.originalStock}")
            productInput = it
        }
    }

    private fun setupValidationResultObserver() {
        viewModel.validationResult.observe(viewLifecycleOwner) { errorType ->
            binding?.apply {
                when(errorType) {
                    ManageProductErrorType.MAX_DISCOUNT_PRICE -> tfCampaignPrice.textField?.isInputError = true
                    ManageProductErrorType.MAX_DISCOUNT_PRICE_AND_OTHER -> tfCampaignPrice.textField?.isInputError = true
                    ManageProductErrorType.MIN_DISCOUNT_PRICE -> tfCampaignPrice.textField?.isInputError = true
                    ManageProductErrorType.MIN_DISCOUNT_PRICE_AND_OTHER -> tfCampaignPrice.textField?.isInputError = true
                    ManageProductErrorType.MAX_STOCK -> tfStock.isInputError = true
                    ManageProductErrorType.MAX_STOCK_AND_OTHER -> tfStock.isInputError = true
                    ManageProductErrorType.MIN_STOCK -> tfStock.isInputError = true
                    ManageProductErrorType.MIN_STOCK_AND_OTHER -> tfStock.isInputError = true
                    ManageProductErrorType.MAX_ORDER -> tfMaxSold.isInputError = true
                    else -> {
                        tfCampaignPrice.textField?.isInputError = false
                        tfStock.isInputError = false
                        tfMaxSold.isInputError = false
                    }
                }
            }
        }
    }

    private fun setupErrorThrowable() {
        viewModel.errorThrowable.observe(viewLifecycleOwner) {
            val errorMessage = ErrorHandler.getErrorMessage(context ?: return@observe, it)
            binding?.tickerError?.setHtmlDescription(errorMessage)
            binding?.tickerError?.show()
        }
    }

    private fun setupWarehouseListObserver() {
        viewModel.warehouseList.observe(viewLifecycleOwner) {
            binding?.btnSaveNext?.isLoading = false
            warehouseBottomSheet = WarehouseBottomSheet.newInstance(ArrayList(it))
        }
    }

    private fun setupHasWarehouseObserver() {
        viewModel.hasWarehouse.observe(viewLifecycleOwner) {
            binding?.spinnerShopLocation?.isVisible = it
        }
    }

    private fun setupDependencyInjection() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
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
                loadNextData()
            }
            tfStock.editText.afterTextChanged {
                productInput.customStock = it.toLongOrZero()
                viewModel.setProductInput(productInput)
            }
            tfMaxSold.editText.afterTextChanged {
                productInput.maxOrder = it.toIntSafely()
                viewModel.setProductInput(productInput)
            }
            tfCampaignPrice.textField?.editText?.afterTextChanged {
                productInput.discountedPrice = it.toLongOrZero()
                viewModel.setProductInput(productInput)
            }
        }
    }

    private fun loadNextData() {
        if (binding?.btnSaveNext?.isLoading == true) return

        val product = productList?.getOrNull(productIndex)
        binding?.btnSaveNext?.isLoading = true
        viewModel.setProduct(product ?: return)
        productIndex++
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }
}
