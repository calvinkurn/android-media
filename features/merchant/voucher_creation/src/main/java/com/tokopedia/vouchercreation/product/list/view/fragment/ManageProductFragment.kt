package com.tokopedia.vouchercreation.product.list.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.setFragmentToUnifyBgColor
import com.tokopedia.vouchercreation.databinding.FragmentMvcManageProductBinding
import com.tokopedia.vouchercreation.product.create.data.response.ProductId
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.view.activity.CreateCouponProductActivity
import com.tokopedia.vouchercreation.product.list.view.activity.AddProductActivity
import com.tokopedia.vouchercreation.product.list.view.activity.ManageProductActivity
import com.tokopedia.vouchercreation.product.list.view.adapter.ProductListAdapter
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import com.tokopedia.vouchercreation.product.list.view.viewmodel.ManageProductViewModel
import com.tokopedia.vouchercreation.product.update.UpdateCouponActivity
import javax.inject.Inject

class ManageProductFragment : BaseDaggerFragment(),
        ProductListAdapter.OnProductItemClickListener,
        ManageProductActivity.OnBackPressedListener {

    companion object {

        private const val ZERO = 0
        private const val NO_BACKGROUND: Int = 0
        const val BUNDLE_KEY_IS_VIEWING = "isViewing"
        const val BUNDLE_KEY_IS_EDITING = "isEditing"
        const val BUNDLE_KEY_MAX_PRODUCT_LIMIT = "maxProductLimit"
        const val BUNDLE_KEY_COUPON_SETTINGS = "couponSettings"
        const val BUNDLE_KEY_SELECTED_PRODUCTS = "selectedProducts"
        const val BUNDLE_KEY_SELECTED_PRODUCT_IDS = "selectedProductIds"
        const val BUNDLE_KEY_SELECTED_WAREHOUSE_ID = "selectedWarehouseId"

        @JvmStatic
        fun createInstance(
                isViewing: Boolean,
                isEditing: Boolean,
                maxProductLimit: Int,
                couponSettings: CouponSettings?,
                selectedProducts: ArrayList<ProductUiModel>?,
                selectedProductIds: ArrayList<ProductId>?,
                selectedWarehouseId: String?,
        ) = ManageProductFragment().apply {
            this.arguments = Bundle().apply {
                putBoolean(BUNDLE_KEY_IS_VIEWING, isViewing)
                putBoolean(BUNDLE_KEY_IS_EDITING, isEditing)
                putInt(BUNDLE_KEY_MAX_PRODUCT_LIMIT, maxProductLimit)
                putParcelable(BUNDLE_KEY_COUPON_SETTINGS, couponSettings)
                putParcelableArrayList(BUNDLE_KEY_SELECTED_PRODUCTS, selectedProducts)
                putParcelableArrayList(BUNDLE_KEY_SELECTED_PRODUCT_IDS, selectedProductIds)
                putString(BUNDLE_KEY_SELECTED_WAREHOUSE_ID,selectedWarehouseId)
            }
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(ManageProductViewModel::class.java)
    }

    private var adapter: ProductListAdapter? = null

    private var binding: FragmentMvcManageProductBinding? = null

    override fun getScreenName(): String {
        return "manage product"
    }

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val viewBinding = FragmentMvcManageProductBinding.inflate(inflater, container, false)
        binding = viewBinding
        return viewBinding.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentToUnifyBgColor()

        val isViewing = arguments?.getBoolean(BUNDLE_KEY_IS_VIEWING, false) ?: false
        val isEditing = arguments?.getBoolean(BUNDLE_KEY_IS_EDITING, false) ?: false
        val maxProductLimit = arguments?.getInt(BUNDLE_KEY_MAX_PRODUCT_LIMIT) ?: ZERO
        viewModel.setIsViewing(isViewing)
        viewModel.setIsEditing(isEditing)
        viewModel.setMaxProductLimit(maxProductLimit)

        setupViews(binding)
        observeLiveData()

        val shopId = userSession.shopId

        val couponSettings = arguments?.getParcelable<CouponSettings>(BUNDLE_KEY_COUPON_SETTINGS)
        viewModel.setCouponSettings(couponSettings)

        val selectedProducts = arguments?.getParcelableArrayList<ProductUiModel>(BUNDLE_KEY_SELECTED_PRODUCTS)
        val selectedProductIds = arguments?.getParcelableArrayList<ProductId>(BUNDLE_KEY_SELECTED_PRODUCT_IDS)

        if (!selectedProductIds.isNullOrEmpty()) {
            viewModel.setSelectedProductIds(selectedProductIds)
            val selectedParentProductIds = viewModel.getSelectedParentProductIds()
            viewModel.getProductList(
                    pageSize = viewModel.getMaxProductLimit(),
                    shopId = shopId,
                    selectedProductIds = selectedParentProductIds
            )
        } else {
            if (!selectedProducts.isNullOrEmpty()) {
                viewModel.setSetSelectedProducts(selectedProducts.toList())
                val updatedProductList = viewModel.updateProductUiModelsDisplayMode(isViewing, isEditing, selectedProducts)
                adapter?.setProductList(resetProductUiModelState(updatedProductList))
            }
        }
    }

    private fun setupViews(binding: FragmentMvcManageProductBinding?) {
        binding?.duTop?.isVisible = viewModel.getIsEditing()
        setupDeleteProductButton(binding)
        setupSelectionBar(binding)
        setupProductListView(binding)
        setupAddProductButton(binding)
    }

    private fun setupDeleteProductButton(binding: FragmentMvcManageProductBinding?) {
        binding?.tpgDeleteProduct?.setOnClickListener {
            adapter?.deleteSelectedProducts()
        }
    }

    private fun setupProductListView(binding: FragmentMvcManageProductBinding?) {
        adapter = ProductListAdapter(this)
        binding?.rvProductList?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupSelectionBar(binding: FragmentMvcManageProductBinding?) {
        binding?.selectionBar?.isVisible = viewModel.getIsEditing()
        binding?.cbuSelectAllProduct?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // ignore checkbox click when single selection mode
                val isIndeterminate = binding.cbuSelectAllProduct.getIndeterminate()
                if (isIndeterminate && !viewModel.isSelectAllMode) return@setOnCheckedChangeListener
                adapter?.updateAllProductSelections(isChecked)
                viewModel.setSetSelectedProducts(adapter?.getSelectedProducts() ?: listOf())
            } else {
                binding.cbuSelectAllProduct.setIndeterminate(false)
                adapter?.updateAllProductSelections(isChecked)
                viewModel.setSetSelectedProducts(listOf())
            }
        }
    }

    private fun setupAddProductButton(binding: FragmentMvcManageProductBinding?) {
        binding?.tpgAddProduct?.isVisible = viewModel.getIsEditing()
        binding?.tpgAddProduct?.setOnClickListener {
            navigateToAddProductPage()
        }
        binding?.buttonAddProduct?.setOnClickListener {
            navigateToAddProductPage()
        }
    }

    private fun observeLiveData() {
        viewModel.selectedProductListLiveData.observe(viewLifecycleOwner, { selectedProducts ->
            when {
                selectedProducts.isEmpty() -> {
                    binding?.tpgAddProduct?.isEnabled = true
                    binding?.tpgAddProduct?.setTextColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_G500))
                    viewModel.isSelectAllMode = false
                    binding?.cbuSelectAllProduct?.isChecked = false
                    val isIndeterminate = binding?.cbuSelectAllProduct?.getIndeterminate() ?: false
                    if (isIndeterminate) binding?.cbuSelectAllProduct?.setIndeterminate(false)
                    binding?.tpgSelectAll?.text = getString(R.string.mvc_select_all)
                    binding?.selectionBar?.setBackgroundResource(NO_BACKGROUND)
                }
                selectedProducts.size == viewModel.getMaxProductLimit() -> {
                    binding?.tpgAddProduct?.isEnabled = false
                    binding?.tpgAddProduct?.setTextColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
                }
                else -> {
                    binding?.tpgAddProduct?.isEnabled = true
                    binding?.tpgAddProduct?.setTextColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_G500))
                    val size = selectedProducts.size
                    binding?.tpgSelectAll?.text = "$size Produk dipilih"
                    binding?.selectionBar?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mvc_grey_f3f4f5))
                }
            }
            val maxProductLimit = viewModel.getMaxProductLimit()
            binding?.tpgSelectedProductCounter?.text = "Jumlah Produk (${selectedProducts.size}/$maxProductLimit)"
        })
        viewModel.getProductListResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val productData = result.data.productList.data
                    val productUiModels = viewModel.mapProductDataToProductUiModel(
                            isViewing = viewModel.getIsViewing(),
                            isEditing = viewModel.getIsEditing(),
                            productDataList = productData)
                    viewModel.setProductUiModels(productUiModels)
                    if (productUiModels.isNotEmpty()) {
                        binding?.selectionBar?.isVisible = viewModel.getIsEditing()
                        binding?.emptyProductsLayout?.hide()
                        viewModel.getCouponSettings()?.run {
                            viewModel.validateProductList(
                                    benefitType = viewModel.getBenefitType(this),
                                    couponType = viewModel.getCouponType(this),
                                    benefitIdr = viewModel.getBenefitIdr(this),
                                    benefitMax = viewModel.getBenefitMax(this),
                                    benefitPercent = viewModel.getBenefitPercent(this),
                                    minPurchase = viewModel.getMinimumPurchase(this),
                                    productIds = viewModel.getIdsFromProductList(productUiModels)
                            )
                        }
                    } else {
                        binding?.selectionBar?.hide()
                        binding?.emptyProductsLayout?.show()
                    }
                }
                is Fail -> {
                    // TODO : handle negative case
                }
            }
        })
        viewModel.validateVoucherResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val validationResults = result.data.response.voucherValidationData.validationPartial
                    val productList = viewModel.getProductUiModels()
                    // add variants and error message (if any)
                    val updatedProductList = viewModel.applyValidationResult(
                            productList = productList,
                            validationResults = validationResults
                    )
                    // set product variant selection
                    val selectedProductIds = viewModel.getSelectedProductIds()
                    val finalProductList = viewModel.setVariantSelection(updatedProductList, selectedProductIds)

                    val newAddedProducts = arguments?.getParcelableArrayList<ProductUiModel>(BUNDLE_KEY_SELECTED_PRODUCTS)?.toList()
                    finalProductList.addAll(newAddedProducts?: listOf())
                    viewModel.setSetSelectedProducts(finalProductList)
                    adapter?.setProductList(finalProductList)
                }
                is Fail -> {
                    // TODO : handle negative case
                }
            }
        })
    }

    private fun navigateToAddProductPage() {
        val couponSettings = viewModel.getCouponSettings()
        val maxProductLimit = viewModel.getMaxProductLimit()
        val addProductIntent = Intent(requireContext(), AddProductActivity::class.java).apply {
            putExtras(Bundle().apply {
                val selectedWarehouseId = arguments?.getString(BUNDLE_KEY_SELECTED_WAREHOUSE_ID)
                putString(BUNDLE_KEY_SELECTED_WAREHOUSE_ID, selectedWarehouseId)
                putInt(UpdateCouponActivity.BUNDLE_KEY_MAX_PRODUCT_LIMIT, maxProductLimit)
                putParcelable(UpdateCouponActivity.BUNDLE_KEY_COUPON_SETTINGS, couponSettings)
                val selectedProducts = arrayListOf<ProductUiModel>()
                selectedProducts.addAll(adapter?.getSelectedProducts() ?: listOf())
                putParcelableArrayList(BUNDLE_KEY_SELECTED_PRODUCTS, selectedProducts)
            })
        }
        activity?.startActivityForResult(addProductIntent, CreateCouponProductActivity.REQUEST_CODE_ADD_PRODUCT)
    }

    override fun onProductCheckBoxClicked(isSelected: Boolean) {
        viewModel.isSelectAllMode = false
        if (isSelected) {
            val isIndeterminate = binding?.cbuSelectAllProduct?.getIndeterminate() ?: false
            if (!isIndeterminate) binding?.cbuSelectAllProduct?.setIndeterminate(true)
            val isChecked = binding?.cbuSelectAllProduct?.isChecked ?: false
            if (!isChecked) binding?.cbuSelectAllProduct?.isChecked = true
        }
        viewModel.setSetSelectedProducts(adapter?.getSelectedProducts() ?: listOf())
    }

    override fun onBackPressed() {
        val selectedProducts = adapter?.getSelectedProducts() ?: listOf()
        val extraSelectedProducts = ArrayList<ProductUiModel>()
        extraSelectedProducts.addAll(selectedProducts)
        val resultIntent = Intent().apply {
            putParcelableArrayListExtra(BUNDLE_KEY_SELECTED_PRODUCTS, extraSelectedProducts)
        }
        this.activity?.setResult(Activity.RESULT_OK, resultIntent)
        this.activity?.finish()
    }

    fun resetProductUiModelState(selectedProducts: List<ProductUiModel>): List<ProductUiModel> {
        return viewModel.resetProductUiModelState(selectedProducts)
    }

    fun addProducts(selectedProducts: List<ProductUiModel>) {
        adapter?.addProducts(selectedProducts)
    }
}