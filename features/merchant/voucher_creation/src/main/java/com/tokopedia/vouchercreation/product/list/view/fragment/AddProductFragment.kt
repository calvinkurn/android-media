package com.tokopedia.vouchercreation.product.list.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.databinding.FragmentMvcAddProductBinding
import com.tokopedia.vouchercreation.product.list.view.adapter.ProductListAdapter
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import com.tokopedia.vouchercreation.product.list.view.model.ProductVariant
import com.tokopedia.vouchercreation.product.list.view.viewholder.ProductItemVariantViewHolder
import com.tokopedia.vouchercreation.product.list.view.viewholder.ProductItemViewHolder
import com.tokopedia.vouchercreation.product.list.view.viewmodel.AddProductViewModel
import javax.inject.Inject

class AddProductFragment : BaseDaggerFragment(), ProductItemViewHolder.OnProductItemClickListener, ProductItemVariantViewHolder.OnVariantItemClickListener {

    companion object {
        @JvmStatic
        fun createInstance() = AddProductFragment()
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(AddProductViewModel::class.java)
    }

    private var binding: FragmentMvcAddProductBinding? = null
    private var adapter: ProductListAdapter? = null

    override fun getScreenName(): String {
        return getString(R.string.add_product)
    }

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = FragmentMvcAddProductBinding.inflate(inflater, container, false)
        binding = viewBinding
        return viewBinding.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        observeLiveData()
        val shopId = userSession.shopId
        viewModel.getProductList(shopId)
        viewModel.getSellerLocations(shopId)
        viewModel.getProductsMetaData()
        viewModel.getShopShowCases(shopId)
    }

    private fun setupView(binding: FragmentMvcAddProductBinding?) {
        setupSearchBar(binding)
        setupProductListFilter(binding)
        setupSelectionBar(binding)
        setupProductListView(binding)
    }

    private fun setupSearchBar(binding: FragmentMvcAddProductBinding?) {
        binding?.sbuProductList
    }

    private fun setupProductListFilter(binding: FragmentMvcAddProductBinding?) {
        binding?.sfProductList
    }

    private fun setupMaxLimitTicker(binding: FragmentMvcAddProductBinding?) {
        binding?.tickerMaxProductWording
    }

    private fun setupSelectionBar(binding: FragmentMvcAddProductBinding?) {
        binding?.selectionBar
    }

    private fun setupProductListView(binding: FragmentMvcAddProductBinding?) {
        adapter = ProductListAdapter(this, this)
        binding?.rvProductList?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeLiveData() {

        viewModel.productListResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val productData = result.data.productList.data
                    val productUiModels = viewModel.mapProductDataToProductUiModel(productData)
                    adapter?.setProductList(productUiModels)
                    // TODO : handle empty product list
                }
                is Fail -> {
                    // TODO : handle negative case
                }
            }
        })

        viewModel.getProductVariantsResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val x = result.data.product.variant.products
                }
                is Fail -> {
                    // TODO : handle negative case
                }
            }
        })

        viewModel.getSellerLocationsResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val wareHouseLocations = result.data.ShopLocGetWarehouseByShopIDs.warehouses
                }
                is Fail -> {
                    // TODO : handle negative case
                }
            }
        })

        viewModel.getShowCasesByIdResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val shopShowCases = result.data.shopShowcasesByShopID.result
                }
                is Fail -> {
                    // TODO : handle negative case
                }
            }
        })


    }

    override fun onProductCheckBoxClicked(isSelected: Boolean, productUiModel: ProductUiModel) {
        if (isSelected) viewModel.addSelectedProduct(productUiModel.id)
        else viewModel.removeSelectedProduct(productUiModel.id)
    }

    override fun onVariantAccordionClicked(isVariantEmpty: Boolean, productId: String) {
        viewModel.getProductVariants(isVariantEmpty, productId)
    }

    override fun onVariantCheckBoxClicked(isSelected: Boolean, productVariant: ProductVariant) {
        if (isSelected) viewModel.removeSelectedProduct(productVariant.variantId)
        else viewModel.removeSelectedProduct(productVariant.variantId)
    }
}