package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Redirection
import com.tokopedia.discovery2.databinding.MerchantVoucherGridLayoutBinding
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession

@RequiresApi(Build.VERSION_CODES.M)
class MerchantVoucherGridViewHolder(
    itemView: View,
    val fragment: Fragment
): AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private var recyclerView: RecyclerView = itemView.findViewById(R.id.merchant_voucher_rv)

    private val binding = MerchantVoucherGridLayoutBinding.bind(itemView)

    private var adapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)

    private var viewModel: MerchantVoucherGridViewModel? = null
    private val mLayoutManager = GridLayoutManager(
        itemView.context,
        2,
        GridLayoutManager.VERTICAL,
        false
    )

    init {
        recyclerView.adapter = adapter
        recyclerView.apply {
            layoutManager = mLayoutManager
        }
        handlePagination()
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as? MerchantVoucherGridViewModel

        viewModel?.let {
            getSubComponent().inject(it)
        }

        if (UserSession(fragment.context).isLoggedIn) {
            addShimmer()
            viewModel?.loadFirstPageCoupon()
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let { lifeCycle ->
            viewModel?.getSyncPageLiveData()?.observe(lifeCycle) { needReSync ->
                if (needReSync) (fragment as? DiscoveryFragment)?.reSync()
            }

            viewModel?.apply {
                couponList.observe(lifeCycle) { result ->
                    when (result) {
                        is Success -> renderMerchantVouchers(result.data)
                        is Fail -> handleErrorState()
                    }
                }
            }

            viewModel?.seeMore?.observe(lifeCycle) { redirection ->
                renderSeeMoreButton(redirection)
            }
        }
    }

    private fun renderSeeMoreButton(redirection: Redirection?) {
        binding.seeMoreBtn.showIfWithBlock(redirection != null) {
            text = redirection?.ctaText

            setOnClickListener {
                RouteManager.route(itemView.context, redirection?.applink)
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let { viewModel?.couponList?.removeObservers(it) }
        lifecycleOwner?.let { viewModel?.seeMore?.removeObservers(it) }
    }

    private fun addShimmer() {
        val shimmerComponent = ComponentsItem(
            name = ComponentNames.Shimmer.componentName,
            shimmerHeight = SHIMMER_HEIGHT
        )

        adapter.setDataList(arrayListOf(shimmerComponent, shimmerComponent))
    }

    private fun renderMerchantVouchers(items: ArrayList<ComponentsItem>?) {
        recyclerView.show()
        adapter.setDataList(items)
        adapter.notifyDataSetChanged()
    }

    private fun handleErrorState() {
        recyclerView.hide()
        binding.seeMoreBtn.hide()
    }

    private fun handlePagination() {
        (fragment as? DiscoveryFragment)?.onMerchantVoucherScrolledCallback = {
            val totalItemCount: Int = mLayoutManager.itemCount
            viewModel?.let { viewModel ->
                if (!viewModel.isLoading && !it.canScrollVertically(SCROLL_DOWN_DIRECTION)) {
                    viewModel.isLoading = true
                    viewModel.loadMore()
                }
            }
        }
    }

    companion object {
        const val SHIMMER_HEIGHT = 300
        private const val SCROLL_DOWN_DIRECTION = 1
    }
}
