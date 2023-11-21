package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopoffersupportingbrand

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.databinding.DiscoverySupportingBrandLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
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

    private val userSession: UserSessionInterface
        by lazy {
            UserSession(fragment.context)
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
        supportingBrandRV.show()
        supportingBrandAdapter.setDataList(items)
    }

    private fun DiscoverySupportingBrandLayoutBinding.hideWidget() {
        supportingBrandRV.hide()
    }

    private fun DiscoverySupportingBrandLayoutBinding.setupRecyclerView() {
        supportingBrandRV.apply {
            adapter = supportingBrandAdapter
            layoutManager = supportingBrandLayoutManager
        }
    }
}
