package com.tokopedia.vouchercreation.product.list.view.fragment

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
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.setFragmentToUnifyBgColor
import com.tokopedia.vouchercreation.databinding.FragmentMvcManageProductBinding
import com.tokopedia.vouchercreation.product.create.data.response.ProductId
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.list.view.adapter.ProductListAdapter
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import com.tokopedia.vouchercreation.product.list.view.viewmodel.ManageProductViewModel
import javax.inject.Inject

class ManageProductFragment : BaseDaggerFragment(), ProductListAdapter.OnProductItemClickListener {

    companion object {

        private const val ZERO = 0
        private const val NO_BACKGROUND: Int = 0
        const val BUNDLE_KEY_IS_EDITING = "isEditing"
        const val BUNDLE_KEY_MAX_PRODUCT_LIMIT = "maxProductLimit"
        const val BUNDLE_KEY_COUPON_SETTINGS = "couponSettings"
        const val BUNDLE_KEY_SELECTED_PRODUCTS = "selectedProducts"
        const val BUNDLE_KEY_SELECTED_PRODUCT_IDS = "selectedProductIds"

        @JvmStatic
        fun createInstance(
                isEditing: Boolean,
                maxProductLimit: Int,
                couponSettings: CouponSettings?,
                selectedProducts: ArrayList<ProductUiModel>?,
                selectedProductIds: ArrayList<ProductId>?,
        ) = ManageProductFragment().apply {
            this.arguments = Bundle().apply {
                putBoolean(BUNDLE_KEY_IS_EDITING, isEditing)
                putInt(BUNDLE_KEY_MAX_PRODUCT_LIMIT, maxProductLimit)
                putParcelable(BUNDLE_KEY_COUPON_SETTINGS, couponSettings)
                putParcelableArrayList(BUNDLE_KEY_SELECTED_PRODUCTS, selectedProducts)
                putParcelableArrayList(BUNDLE_KEY_SELECTED_PRODUCT_IDS, selectedProductIds)
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
        setupView(binding)

        val isEditing = arguments?.getBoolean(BUNDLE_KEY_IS_EDITING, true) ?: true
//        val maxProductLimit = arguments?.getInt(BUNDLE_KEY_MAX_PRODUCT_LIMIT) ?: ZERO
//        viewModel.setMaxProductLimit(maxProductLimit)

//        val couponSettings = arguments?.getParcelable<CouponSettings>(BUNDLE_KEY_COUPON_SETTINGS)
//        viewModel.setCouponSettings(couponSettings)
//        val selectedProductIds = arguments?.getParcelableArrayList<ProductId>(BUNDLE_KEY_SELECTED_PRODUCT_IDS)
//        viewModel.setSelectedProductIds(selectedProductIds ?: ArrayList())
//        val shopId = userSession.shopId

        // render selected products from voucher creation process
//        val selectedProducts = arguments?.getParcelableArrayList<ProductUiModel>(BUNDLE_KEY_SELECTED_PRODUCTS)
//        selectedProducts?.run {
//            if (selectedProducts.isNotEmpty()) {
//                val updatedProductList = viewModel.updateProductUiModelsDisplayMode(isEditing, selectedProducts)
//                adapter?.setProductList(updatedProductList)
//            }
//            viewModel.setSetSelectedProducts(selectedProducts.toList())
//        }
        // TODO : if only product ids available => hit product list => impl lazy variant load => match variant selection manually

//        observeLiveData()
    }

    private fun setupView(binding: FragmentMvcManageProductBinding?) {
        setupSelectionBar(binding)
        setupProductListView(binding)
    }

    private fun setupProductListView(binding: FragmentMvcManageProductBinding?) {
        adapter = ProductListAdapter(this)
        binding?.rvProductList?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupSelectionBar(binding: FragmentMvcManageProductBinding?) {
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

    private fun observeLiveData() {
        viewModel.selectedProductListLiveData.observe(viewLifecycleOwner, { selectedProducts ->
            when {
                selectedProducts.isEmpty() -> {
                    binding?.tpgAddProduct?.isEnabled = true
                    viewModel.isSelectAllMode = false
                    binding?.cbuSelectAllProduct?.isChecked = false
                    val isIndeterminate = binding?.cbuSelectAllProduct?.getIndeterminate() ?: false
                    if (isIndeterminate) binding?.cbuSelectAllProduct?.setIndeterminate(false)
                    binding?.tpgSelectAll?.text = getString(R.string.mvc_select_all)
                    binding?.selectionBar?.setBackgroundResource(NO_BACKGROUND)
                }
                selectedProducts.size == viewModel.getMaxProductLimit() -> {
                    binding?.tpgAddProduct?.isEnabled = false
                }
                else -> {
                    binding?.tpgAddProduct?.isEnabled = true
                    val size = selectedProducts.size
                    binding?.tpgSelectAll?.text = "$size Produk dipilih"
                    binding?.selectionBar?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mvc_grey_f3f4f5))
                }
            }
            val maxProductLimit = viewModel.getMaxProductLimit()
            binding?.tpgSelectedProductCounter?.text = "Jumlah Produk (${selectedProducts.size}/$maxProductLimit)"
        })
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
}