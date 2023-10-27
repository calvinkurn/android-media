package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Redirection
import com.tokopedia.discovery2.databinding.MerchantVoucherGridLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.user.session.UserSession

class MerchantVoucherGridViewHolder(
    itemView: View,
    val fragment: Fragment
): AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val binding = MerchantVoucherGridLayoutBinding.bind(itemView)

    private var adapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)

    private var viewModel: MerchantVoucherGridViewModel? = null

    init {
        binding.merchantVoucherRv.adapter = adapter
    }
    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as? MerchantVoucherGridViewModel

        viewModel?.let {
            getSubComponent().inject(it)
        }

        binding.merchantVoucherRv.layoutManager = GridLayoutManager(
            itemView.context,
            2,
            GridLayoutManager.VERTICAL,
            false
        )

        if (UserSession(fragment.context).isLoggedIn) {
            addShimmer()
            viewModel?.fetchCoupons()
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let { lifeCycle ->
            viewModel?.getSyncPageLiveData()?.observe(lifeCycle) { needReSync ->
                if (needReSync) (fragment as? DiscoveryFragment)?.reSync()
            }

            viewModel?.couponList?.observe(lifeCycle) {
                renderMerchantVouchers(it)
            }

            viewModel?.seeMore?.observe(lifeCycle) { redirection ->
                renderSeeMoreButton(redirection)
            }

            viewModel?.loadError?.observe(lifeCycle) {
                if (it) handleErrorState()
            }
        }
    }

    private fun renderSeeMoreButton(redirection: Redirection) {
        binding.seeMoreBtn.run {
            text = redirection.ctaText

            setOnClickListener {
                RouteManager.route(itemView.context, redirection.applink)
            }

            show()
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { viewModel?.couponList?.removeObservers(it) }
        lifecycleOwner?.let { viewModel?.seeMore?.removeObservers(it) }
        lifecycleOwner?.let { viewModel?.loadError?.removeObservers(it) }
    }

    private fun addShimmer() {
        val shimmerComponent = ComponentsItem(
            name = ComponentNames.Shimmer.componentName,
            shimmerHeight = SHIMMER_HEIGHT
        )

        adapter.setDataList(arrayListOf(shimmerComponent, shimmerComponent))
    }

    private fun renderMerchantVouchers(items: ArrayList<ComponentsItem>?) {
        binding.merchantVoucherRv.show()
        adapter.setDataList(items)
    }

    private fun handleErrorState() {
        binding.merchantVoucherRv.hide()
        binding.seeMoreBtn.hide()
    }

    companion object {
        private const val SHIMMER_HEIGHT = 300
    }
}
