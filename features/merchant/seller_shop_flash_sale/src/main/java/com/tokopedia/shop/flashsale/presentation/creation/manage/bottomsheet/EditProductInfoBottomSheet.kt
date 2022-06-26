package com.tokopedia.shop.flashsale.presentation.creation.manage.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomsheetEditProductInfoBinding
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
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

        setupWarehouseListObserver()
        setupHasWarehouseObserver()
        setupErrorThrowable()
        loadNextData()
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
            binding?.btnSaveNext?.setOnClickListener {
                loadNextData()
            }
        }
    }

    private fun loadNextData() {
        if (binding?.btnSaveNext?.isLoading == true) return

        val product = productList?.getOrNull(productIndex)
        binding?.ivProduct?.loadImage(product?.imageUrl?.img100Square)
        binding?.typographyProductName?.text = product?.productName
        binding?.typographyOriginalPrice?.text = product?.formattedPrice
        binding?.btnSaveNext?.isLoading = true
        viewModel.setWarehouseList(product?.warehouseList.orEmpty())
        productIndex++
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }
}
