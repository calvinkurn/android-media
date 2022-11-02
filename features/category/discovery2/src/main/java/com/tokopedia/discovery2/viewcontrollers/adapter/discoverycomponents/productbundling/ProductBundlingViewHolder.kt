package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productbundling

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.databinding.DiscoProductBundlingLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.common.widget.bundle.adapter.ProductBundleWidgetAdapter
import com.tokopedia.shop.common.widget.bundle.enum.BundleTypes
import com.tokopedia.shop.common.widget.bundle.listener.ProductBundleListener
import com.tokopedia.shop.common.widget.bundle.model.BundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ProductBundlingViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val binding: DiscoProductBundlingLayoutBinding = DiscoProductBundlingLayoutBinding.bind(itemView)
    private var productBundleList: ArrayList<BundleUiModel>? = null
    private var mProductBundleRecycleAdapter: ProductBundleWidgetAdapter
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private var mProductBundlingViewModel: ProductBundlingViewModel? = null

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
        mProductBundlingViewModel?.let { viewModel ->
            getSubComponent().inject(viewModel)
        }
        binding.productRv.show()
        binding.viewErrorState.hide()
        trackCarouselImpression()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mProductBundlingViewModel?.getBundledProductDataList()?.observe(it) { bundledProductList ->
                mProductBundleRecycleAdapter.updateDataSet(bundledProductList)
                productBundleList = bundledProductList
            }
            mProductBundlingViewModel?.getEmptyBundleData()?.observe(it) { isBundledDataEmpty ->
                if (isBundledDataEmpty) {
                    binding.productRv.hide()
                } else {
                    binding.productRv.show()
                }
            }
            mProductBundlingViewModel?.showErrorState()?.observe(it) { shouldShowErrorState ->
                if (shouldShowErrorState) handleErrorState()
            }
        }
    }

    private fun trackCarouselImpression() {
        binding.productRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val totalItemCount: Int = linearLayoutManager.itemCount
                    val lastVisibleItemPosition: Int = linearLayoutManager.findLastVisibleItemPosition()
                    mProductBundlingViewModel?.let { viewModel ->
                        if (viewModel.hasScrolled && (viewModel.lastSentPosition <= totalItemCount - 1) && (lastVisibleItemPosition > viewModel.lastSentPosition || lastVisibleItemPosition == totalItemCount - 1)) {
                            viewModel.components.let {
                                productBundleList?.let { bundledProductList ->
                                    (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                                        .trackEventProductBundlingCarouselImpression(it, bundledProductList, totalItemCount, viewModel.lastSentPosition, lastVisibleItemPosition)
                                    viewModel.lastSentPosition = lastVisibleItemPosition + 1
                                }
                            }
                        }
                    }
                }
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    mProductBundlingViewModel?.hasScrolled = true
                }
            }
        })
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
            mProductBundlingViewModel?.resetComponent()
            mProductBundlingViewModel?.fetchProductBundlingData()
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mProductBundlingViewModel?.getBundledProductDataList()?.removeObservers(it)
            mProductBundlingViewModel?.getEmptyBundleData()?.removeObservers(it)
            mProductBundlingViewModel?.showErrorState()?.removeObservers(it)
        }
    }

    private fun productBundleCallback() = object : ProductBundleListener {
        override fun onBundleProductClicked(bundleType: BundleTypes, bundle: BundleUiModel, selectedMultipleBundle: BundleDetailUiModel, selectedProduct: BundleProductUiModel, productItemPosition: Int) {
            if (selectedProduct.productAppLink.isNotEmpty()) {
                itemView.context?.let { context ->
                    RouteManager.route(context, selectedProduct.productAppLink)
                }
            }
        }

        override fun addMultipleBundleToCart(selectedMultipleBundle: BundleDetailUiModel, productDetails: List<BundleProductUiModel>) {
            if (selectedMultipleBundle.bundleId.isNotEmpty()) {
                itemView.context?.let { context ->
                    RouteManager.route(context, context.getString(R.string.product_bundling_atc_applink, productDetails.firstOrNull()?.productId, selectedMultipleBundle.bundleId))
                }
            }
            mProductBundlingViewModel?.components?.let {
                (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                    .trackEventProductBundlingAtcClick(it, selectedMultipleBundle)
            }
        }

        override fun addSingleBundleToCart(selectedBundle: BundleDetailUiModel, bundleProducts: BundleProductUiModel) {
            if (selectedBundle.bundleId.isNotEmpty()) {
                itemView.context?.let { context ->
                    if (selectedBundle.selectedBundleApplink.isNotEmpty()) {
                        RouteManager.route(context, selectedBundle.selectedBundleApplink)
                    } else {
                        RouteManager.route(context, context.getString(R.string.product_bundling_atc_applink, bundleProducts.productId, selectedBundle.selectedBundleId))
                    }
                }
            }
            mProductBundlingViewModel?.components?.let {
                (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                    .trackEventProductBundlingAtcClick(it, selectedBundle)
            }
        }

        override fun onTrackSingleVariantChange(selectedProduct: BundleProductUiModel, selectedSingleBundle: BundleDetailUiModel, bundleName: String) {
            mProductBundlingViewModel?.components?.let {
                (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                    .trackEventClickProductBundlingChipSelection(it, selectedProduct, selectedSingleBundle)
            }
        }

        override fun impressionProductBundleSingle(selectedSingleBundle: BundleDetailUiModel, selectedProduct: BundleProductUiModel, bundleName: String, bundlePosition: Int) {
            mProductBundlingViewModel?.components?.let {
                (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                    .trackEventProductBundlingViewImpression(it, selectedSingleBundle, bundlePosition)
            }

        }

        override fun impressionProductBundleMultiple(selectedMultipleBundle: BundleDetailUiModel, bundlePosition: Int) {
            mProductBundlingViewModel?.components?.let {
                (fragment as DiscoveryFragment).getDiscoveryAnalytics()
                    .trackEventProductBundlingViewImpression(it, selectedMultipleBundle, bundlePosition)
            }
        }

        override fun impressionProductItemBundleMultiple(selectedProduct: BundleProductUiModel, selectedMultipleBundle: BundleDetailUiModel, productItemPosition: Int) {

        }

    }

}
