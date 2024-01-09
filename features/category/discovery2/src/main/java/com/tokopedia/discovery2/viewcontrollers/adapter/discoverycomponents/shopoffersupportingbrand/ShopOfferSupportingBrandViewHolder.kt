package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopoffersupportingbrand

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.databinding.DiscoverySupportingBrandLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.CarouselProductCardItemDecorator
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
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

            loadPageBrand()
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
                    is Success -> {
                        trackImpression(result.data)
                        showWidget(result.data)
                    }

                    is Fail -> hideWidget()
                }
            }
        }
    }

    private fun trackImpression(components: List<ComponentsItem>) {
        if (components.find { it.name == ComponentNames.ShopOfferSupportingBrandItem.componentName } == null) return

        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
            ?.trackSupportingBrandImpression(components)
    }

    private fun DiscoverySupportingBrandLayoutBinding.showWidget(items: ArrayList<ComponentsItem>) {
        supportingBrandRV.show()
        supportingBrandAdapter.setDataList(items)
    }

    private fun DiscoverySupportingBrandLayoutBinding.hideWidget() {
        supportingBrandRV.hide()
    }

    private fun DiscoverySupportingBrandLayoutBinding.setupRecyclerView() {
        supportingBrandAdapter.setHasStableIds(true)
        supportingBrandRV.apply {
            adapter = supportingBrandAdapter
            layoutManager = supportingBrandLayoutManager
        }
        addItemDecorator()
        addScrollListener()
    }

    private fun DiscoverySupportingBrandLayoutBinding.addItemDecorator() {
        if (supportingBrandRV.itemDecorationCount.isMoreThanZero()) {
            supportingBrandRV.removeAllItemDecoration()
        }

        supportingBrandRV.addItemDecoration(CarouselProductCardItemDecorator())
    }

    private fun DiscoverySupportingBrandLayoutBinding.addScrollListener() {
        if (viewModel?.hasHeader() == false) return
        supportingBrandRV.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount: Int = supportingBrandLayoutManager.childCount
                    val totalItemCount: Int = supportingBrandLayoutManager.itemCount
                    val firstVisibleItemPosition: Int =
                        supportingBrandLayoutManager.findFirstVisibleItemPosition()
                    viewModel?.let { mShopOfferSupportingBrandViewModel ->
                        if (!mShopOfferSupportingBrandViewModel.isLoading && mShopOfferSupportingBrandViewModel.hasNextPage()) {
                            if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount) && firstVisibleItemPosition >= 0) {
                                mShopOfferSupportingBrandViewModel.loadMore()
                            }
                        }
                    }
                }
            }
        )
    }
}
