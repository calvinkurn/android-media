package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopoffersupportingbrand

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.databinding.DiscoverySupportingBrandLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.CarouselProductCardItemDecorator
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.home_component.util.removeAllItemDecoration
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding

class ShopOfferSupportingBrandViewHolder(
    itemView: View,
    val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val binding: DiscoverySupportingBrandLayoutBinding?
        by viewBinding()

    private val supportingBrandAdapter: DiscoveryRecycleAdapter
        by lazy {
            DiscoveryRecycleAdapter(fragment)
        }

    private val supportingBrandLayoutManager: LinearLayoutManager
        by lazy {
            LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }

    private var viewModel: ShopOfferSupportingBrandViewModel? = null

    init {
        binding?.setupRecyclerView()
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as? ShopOfferSupportingBrandViewModel

        viewModel?.apply {
            getSubComponent().inject(this)

            loadFirstPageBrand()
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        if (lifecycleOwner == null) return

        viewModel?.run {
            observeBrandList(lifecycleOwner)
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        if (lifecycleOwner == null) return

        viewModel?.observeBrandList(lifecycleOwner)
    }

    private fun ShopOfferSupportingBrandViewModel.observeBrandList(
        lifecycleOwner: LifecycleOwner
    ) {
        binding?.apply {
            brands.observe(lifecycleOwner) { result ->
                when (result) {
                    is Success -> showWidget(result.data)
                    is Fail -> hideWidget()
                }
            }
        }
    }

    private fun DiscoverySupportingBrandLayoutBinding.showWidget(items: ArrayList<ComponentsItem>) {
        localLoad.hide()
        supportingBrandRV.show()
        supportingBrandAdapter.setDataList(items)
    }

    private fun DiscoverySupportingBrandLayoutBinding.hideWidget() {
        supportingBrandRV.hide()
        showLocalLoad()
    }

    private fun DiscoverySupportingBrandLayoutBinding.setupRecyclerView() {
        supportingBrandAdapter.setHasStableIds(true)
        supportingBrandRV.apply {
            adapter = supportingBrandAdapter
            layoutManager = supportingBrandLayoutManager
        }
        addItemDecorator()
    }

    private fun DiscoverySupportingBrandLayoutBinding.addItemDecorator() {
        if (supportingBrandRV.itemDecorationCount.isMoreThanZero()) {
            supportingBrandRV.removeAllItemDecoration()
        }

        supportingBrandRV.addItemDecoration(CarouselProductCardItemDecorator())
    }

    private fun DiscoverySupportingBrandLayoutBinding.showLocalLoad() {
        localLoad.run {
            title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
            description?.text =
                context?.getString(R.string.discovery_section_empty_state_description).orEmpty()

            refreshBtn?.setOnClickListener {
                progressState = !progressState
                reloadComponent()
            }

            show()
        }
        supportingBrandRV.hide()
    }

    private fun DiscoverySupportingBrandLayoutBinding.reloadComponent() {
        supportingBrandRV.show()
        localLoad.hide()
        viewModel?.apply {
            resetComponent()
            loadFirstPageBrand()
        }
    }
}
