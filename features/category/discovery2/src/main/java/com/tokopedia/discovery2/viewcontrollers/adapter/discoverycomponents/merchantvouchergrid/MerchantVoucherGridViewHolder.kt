package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.applink.RouteManager
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
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding

class MerchantVoucherGridViewHolder(
    itemView: View,
    val fragment: Fragment
): AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    companion object {
        private const val SCROLL_UP_DIRECTION = 1
        private const val GRID_SPAN_COUNT = 2
    }

    private val binding: MerchantVoucherGridLayoutBinding?
        by viewBinding()

    private val mAdapter: DiscoveryRecycleAdapter
        by lazy {
            DiscoveryRecycleAdapter(fragment)
        }

    private val mLayoutManager: GridLayoutManager
        by lazy {
            GridLayoutManager(
                itemView.context,
                GRID_SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false
            )
        }

    private val userSession: UserSessionInterface
        by lazy {
            UserSession(fragment.context)
        }

    private var viewModel: MerchantVoucherGridViewModel? = null

    init {
        binding?.setupRecyclerView()
        handlePagination()
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as? MerchantVoucherGridViewModel

        viewModel?.apply {
            getSubComponent().inject(this)

            if (!userSession.isLoggedIn) return

            loadFirstPageCoupon()
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        if (lifecycleOwner == null) return

        viewModel?.apply {
            observeCouponList(lifecycleOwner)
            observeSeeMore(lifecycleOwner)
            observeNoMorePages(lifecycleOwner)
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        if (lifecycleOwner == null) return

        viewModel?.apply {
            couponList.removeObservers(lifecycleOwner)
            seeMore.removeObservers(lifecycleOwner)
            noMorePages.removeObservers(lifecycleOwner)
        }
    }

    private fun handlePagination() {
        getParentFragment()?.onMerchantVoucherScrolledCallback = { parentRecyclerView ->
            val isAtTheBottomOfThePage = !parentRecyclerView.canScrollVertically(SCROLL_UP_DIRECTION)
            viewModel?.loadMore(isAtTheBottomOfThePage)
        }
    }

    private fun getParentFragment(): DiscoveryFragment? = fragment as? DiscoveryFragment

    private fun MerchantVoucherGridViewModel.observeCouponList(
        lifecycleOwner: LifecycleOwner
    ) {
        binding?.apply {
            couponList.observe(lifecycleOwner) { result ->
                when (result) {
                    is Success -> showWidget(result.data)
                    is Fail -> hideWidget()
                }
            }
        }
    }

    private fun MerchantVoucherGridViewModel.observeNoMorePages(
        lifecycleOwner: LifecycleOwner
    ) {
        noMorePages.observe(lifecycleOwner) {
            getParentFragment()?.onMerchantVoucherScrolledCallback = null
        }
    }

    private fun MerchantVoucherGridViewModel.observeSeeMore(
        lifecycleOwner: LifecycleOwner
    ) {
        seeMore.observe(lifecycleOwner) { redirection ->
            binding?.renderSeeMoreButton(redirection)
        }
    }

    private fun MerchantVoucherGridLayoutBinding.showWidget(items: ArrayList<ComponentsItem>?) {
        merchantVoucherRv.show()
        mAdapter.setDataList(items)
    }

    private fun MerchantVoucherGridLayoutBinding.hideWidget() {
        merchantVoucherRv.hide()
        seeMoreBtn.hide()
        seeMoreBtnSpace.hide()
    }

    private fun MerchantVoucherGridLayoutBinding.renderSeeMoreButton(redirection: Redirection) {
        seeMoreBtn.apply {
            show()

            text = redirection.ctaText

            setOnClickListener {
                if (!redirection.applink.isNullOrBlank()) {
                    RouteManager.route(itemView.context, redirection.applink)
                }
            }
        }

        seeMoreBtnSpace.show()
    }

    private fun MerchantVoucherGridLayoutBinding.setupRecyclerView() {
        merchantVoucherRv.adapter = mAdapter
        merchantVoucherRv.layoutManager = mLayoutManager
    }
}
