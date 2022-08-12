package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productbundling

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.databinding.DiscoProductBundlingLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.common.widget.bundle.adapter.ProductBundleWidgetAdapter

class ProductBundlingViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val binding: DiscoProductBundlingLayoutBinding = DiscoProductBundlingLayoutBinding.bind(itemView)
    private var  mProductBundleRecycleAdapter: ProductBundleWidgetAdapter
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private lateinit var mProductBundlingViewModel: ProductBundlingViewModel

    init {
        with(binding) {
            productRv.layoutManager = linearLayoutManager
            mProductBundleRecycleAdapter = ProductBundleWidgetAdapter()
            productRv.adapter = mProductBundleRecycleAdapter
        }
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mProductBundlingViewModel = discoveryBaseViewModel as ProductBundlingViewModel
        getSubComponent().inject(mProductBundlingViewModel)
        binding.productRv.show()
        binding.viewEmptyState.hide()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mProductBundlingViewModel.getBundledProductDataList().observe(it,{ bundledProductList ->
                binding.productRv.show()
                mProductBundleRecycleAdapter.updateDataSet(bundledProductList)
            })
            mProductBundlingViewModel.showErrorState().observe(it, { shouldShowErrorState ->
                if (shouldShowErrorState) handleErrorState()
            })
        }
    }

    private fun handleErrorState() {
        with(binding) {
            mProductBundleRecycleAdapter.notifyDataSetChanged()
            if (mProductBundlingViewModel.getProductList() == null) {
                viewEmptyState.run {
                    title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
                    description?.text = context?.getString(R.string.discovery_section_empty_state_description).orEmpty()
                    refreshBtn?.setOnClickListener {
                        reloadComponent()
                    }
                    viewEmptyState.show()
                }
            } else {
                viewEmptyState.hide()
            }

            binding.productRv.hide()
        }
    }

    private fun reloadComponent() {
        with(binding) {
            productRv.show()
            viewEmptyState.hide()
            mProductBundlingViewModel.fetchProductBundlingData()
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {

        }
    }

}