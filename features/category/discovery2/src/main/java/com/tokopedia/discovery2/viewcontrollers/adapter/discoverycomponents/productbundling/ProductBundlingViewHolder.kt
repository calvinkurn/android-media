package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productbundling

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.databinding.DiscoProductBundlingLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.common.widget.bundle.adapter.ProductBundleWidgetAdapter
import com.tokopedia.shop.common.widget.bundle.enum.BundleTypes
import com.tokopedia.shop.common.widget.bundle.listener.ProductBundleListener
import com.tokopedia.shop.common.widget.bundle.model.BundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel

class ProductBundlingViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val binding: DiscoProductBundlingLayoutBinding = DiscoProductBundlingLayoutBinding.bind(itemView)
    private var mProductBundleRecycleAdapter: ProductBundleWidgetAdapter
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private lateinit var mProductBundlingViewModel: ProductBundlingViewModel

    init {
        with(binding) {
            productRv.layoutManager = linearLayoutManager
            mProductBundleRecycleAdapter = ProductBundleWidgetAdapter()
            productRv.adapter = mProductBundleRecycleAdapter
            mProductBundleRecycleAdapter.setListener(productBundleCallback())
        }
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mProductBundlingViewModel = discoveryBaseViewModel as ProductBundlingViewModel
        getSubComponent().inject(mProductBundlingViewModel)
        binding.productRv.show()
        binding.viewErrorState.hide()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mProductBundlingViewModel.getBundledProductDataList().observe(it, { bundledProductList ->
                mProductBundleRecycleAdapter.updateDataSet(bundledProductList)
            })
            mProductBundlingViewModel.getEmptyBundleData().observe(it, { isBundledDataEmpty ->
                if (isBundledDataEmpty) {
                    binding.productRv.hide()
                } else {
                    binding.productRv.show()
                }
            })
            mProductBundlingViewModel.showErrorState().observe(it, { shouldShowErrorState ->
                if (shouldShowErrorState) handleErrorState()
            })
        }
    }

    private fun handleErrorState() {
        with(binding) {
            mProductBundleRecycleAdapter.notifyDataSetChanged()
            viewErrorState.run {
                title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
                description?.text = context?.getString(R.string.discovery_section_empty_state_description).orEmpty()
                refreshBtn?.setOnClickListener {
                    reloadComponent()
                }
                viewErrorState.show()
            }
            binding.productRv.hide()
        }
    }

    private fun reloadComponent() {
        with(binding) {
            productRv.show()
            viewErrorState.hide()
            mProductBundlingViewModel.fetchProductBundlingData()
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {

        }
    }

    private fun productBundleCallback() = object : ProductBundleListener {
        override fun onBundleProductClicked(bundleType: BundleTypes, bundle: BundleUiModel, selectedMultipleBundle: BundleDetailUiModel, selectedProduct: BundleProductUiModel, productItemPosition: Int) {
            itemView.context?.let { context ->
                RouteManager.route(context,selectedProduct.productAppLink)
            }
        }

        override fun addMultipleBundleToCart(selectedMultipleBundle: BundleDetailUiModel, productDetails: List<BundleProductUiModel>) {
//            itemView.context?.let{ context ->
//                val intent = RouteManager.getIntent(
//                        context,
//                        selectedProduct.productAppLink,
//                        selectedMultipleBundle.bundleId
//                )
//                context.startActivity(intent)
//            }
        }

        override fun addSingleBundleToCart(selectedBundle: BundleDetailUiModel, bundleProducts: BundleProductUiModel) {
            itemView.context?.let { context ->
                val intent = RouteManager.getIntent(
                        context,
                        "tokopedia://product-bundle",
                        selectedBundle.bundleId
                )
                context.startActivity(intent)
            }
        }

        override fun onTrackSingleVariantChange(selectedProduct: BundleProductUiModel, selectedSingleBundle: BundleDetailUiModel, bundleName: String) {

        }

        override fun impressionProductBundleSingle(selectedSingleBundle: BundleDetailUiModel, selectedProduct: BundleProductUiModel, bundleName: String, bundlePosition: Int) {

        }

        override fun impressionProductBundleMultiple(selectedMultipleBundle: BundleDetailUiModel, bundlePosition: Int) {

        }

        override fun impressionProductItemBundleMultiple(selectedProduct: BundleProductUiModel, selectedMultipleBundle: BundleDetailUiModel, productItemPosition: Int) {

        }

    }

}