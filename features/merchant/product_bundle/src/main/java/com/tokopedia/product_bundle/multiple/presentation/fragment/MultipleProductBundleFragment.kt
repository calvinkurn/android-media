package com.tokopedia.product_bundle.multiple.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.common.di.ProductBundleComponentBuilder
import com.tokopedia.product_bundle.common.di.ProductBundleModule
import com.tokopedia.product_bundle.multiple.di.DaggerMultipleProductBundleComponent
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleDetailAdapter
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleDetailAdapter.ProductBundleDetailItemClickListener
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleMasterAdapter
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleMasterAdapter.ProductBundleMasterItemClickListener
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster
import com.tokopedia.product_bundle.viewmodel.ProductBundleViewModel
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class MultipleProductBundleFragment : BaseDaggerFragment(),
        ProductBundleMasterItemClickListener,
        ProductBundleDetailItemClickListener {

    companion object {
        @JvmStatic
        fun newInstance() =
                MultipleProductBundleFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(ProductBundleViewModel::class.java)
    }

    private var soldProductBundleTextView: Typography? = null

    // product bundle master components
    private var productBundleMasterView: RecyclerView? = null
    private var productBundleMasterAdapter: ProductBundleMasterAdapter? = null

    // product bundle detail components
    private var productBundleDetailView: RecyclerView? = null
    private var productBundleDetailAdapter: ProductBundleDetailAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multiple_product_bundle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        soldProductBundleTextView = view.findViewById(R.id.tv_sold_product_bundle)
        // setup product bundle master
        setupProductBundleMasterView(view)
        // setup product bundle detail
        setupProductBundleDetailView(view)
        // simulate get product info api call and conversion from bundle to product bundle master
        val productBundleMasters = viewModel.getProductBundleMasters()
        // render product bundle master chips
        productBundleMasterAdapter?.setProductBundleMasterList(productBundleMasters)
        // observe data from view model
        subscribeToProductBundleInfo()
    }

    override fun getScreenName(): String {
        return getString(R.string.product_bundle_page_title)
    }

    override fun initInjector() {
        DaggerMultipleProductBundleComponent.builder()
                .productBundleComponent(ProductBundleComponentBuilder.getComponent(requireContext().applicationContext as BaseMainApplication))
                .productBundleModule(ProductBundleModule())
                .build()
                .inject(this)
    }

    private fun setupProductBundleMasterView(view: View) {
        productBundleMasterView = view.findViewById(R.id.rv_product_bundle_master)
        productBundleMasterAdapter = ProductBundleMasterAdapter(this)
        productBundleMasterView?.let {
            it.adapter = productBundleMasterAdapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupProductBundleDetailView(view: View) {
        productBundleDetailView = view.findViewById(R.id.rv_product_bundle_detail)
        productBundleDetailAdapter = ProductBundleDetailAdapter(this)
        productBundleDetailView?.let {
            it.adapter = productBundleDetailAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun subscribeToProductBundleInfo() {
        viewModel.getBundleInfoResult.observe(viewLifecycleOwner, Observer { bundleInfo ->
            // render product bundle detail
            val productBundleItems = bundleInfo.bundleItems
            val productBundleDetails = viewModel.mapProductBundleItemsToProductBundleDetail(productBundleItems)
            productBundleDetailAdapter?.setProductBundleDetails(productBundleDetails)
            // render sold product bundle view
            val soldProductBundle = viewModel.getSoldProductBundle()
            soldProductBundleTextView?.text = String.format(getString(R.string.text_sold_product_bundle), soldProductBundle)
        })
    }

    override fun onProductBundleMasterItemClicked(adapterPosition: Int, productBundleMaster: ProductBundleMaster) {
        // update selected bundle state to view model
        viewModel.setSelectedProductBundleMaster(viewModel.getProductBundleMasters()[adapterPosition])
        // deselect the rest of selection except the selected one
        productBundleMasterAdapter?.deselectUnselectedItems(adapterPosition)
        // get product bundle detail
        val bundleId = productBundleMaster.bundleId
        viewModel.getBundleInfo(bundleId)
    }

    override fun onProductVariantSpinnerClicked(productBundleItem: BundleItem) {
        TODO("show product variant bottom sheet")
    }
}