package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewCreator
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.user.session.UserSession

class MerchantVoucherCarouselViewHolder(itemView: View, val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var recyclerView: RecyclerView = itemView.findViewById(R.id.disco_mvc_carousel_rv)
    private val shimmer: LoaderUnify = itemView.findViewById(R.id.shimmer_view)
    private var mHeaderView: FrameLayout = itemView.findViewById(R.id.header_view)
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var merchantVoucherCarouselViewModel: MerchantVoucherCarouselViewModel

    init {
        recyclerView.layoutManager = linearLayoutManager
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        recyclerView.adapter = mDiscoveryRecycleAdapter
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        merchantVoucherCarouselViewModel = discoveryBaseViewModel as MerchantVoucherCarouselViewModel
        getSubComponent().inject(merchantVoucherCarouselViewModel)
        if(UserSession(fragment.context).isLoggedIn) {
            shimmer.show()
            discoveryBaseViewModel.getLihatSemuaHeader()
            discoveryBaseViewModel.fetchCouponData()
        }else{
            handleErrorState()
        }
        handleCarouselPagination()
    }

    private fun handleCarouselPagination() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount: Int = linearLayoutManager.childCount
                val totalItemCount: Int = linearLayoutManager.itemCount
                val firstVisibleItemPosition: Int =
                    linearLayoutManager.findFirstVisibleItemPosition()
                if (!merchantVoucherCarouselViewModel.isLoadingData() && !merchantVoucherCarouselViewModel.isLastPage()) {
                    if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount) && firstVisibleItemPosition >= 0) {
                        merchantVoucherCarouselViewModel.fetchCarouselPaginatedCoupon()
                    }
                }
            }
        })
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let { lifecycle ->
            merchantVoucherCarouselViewModel.headerData.observe(lifecycle, { component->
                addCardHeader(component)
            })
            merchantVoucherCarouselViewModel.couponList.observe(lifecycle, { item ->
                shimmer.hide()
                recyclerView.show()
                mDiscoveryRecycleAdapter.setDataList(item)
            })
            merchantVoucherCarouselViewModel.loadError.observe(lifecycle ,{
                if(it)
                handleErrorState()
            })

        }
    }

    private fun handleErrorState() {
        if (mHeaderView.childCount > 0)
            mHeaderView.removeAllViews()
        recyclerView.hide()
        shimmer.hide()
    }

    private fun addCardHeader(componentsItem: ComponentsItem?) {
        mHeaderView.removeAllViews()
        checkHeaderVisibility(componentsItem)
    }

    private fun checkHeaderVisibility(componentsItem: ComponentsItem?) {
        componentsItem?.data?.firstOrNull()?.let {
            if (!it.title.isNullOrEmpty() || !it.subtitle.isNullOrEmpty()) {
                mHeaderView.addView(
                    CustomViewCreator.getCustomViewObject(itemView.context,
                    ComponentsList.LihatSemua, componentsItem, fragment))
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            merchantVoucherCarouselViewModel.couponList.removeObservers(it)
            merchantVoucherCarouselViewModel.loadError.removeObservers(it)
            merchantVoucherCarouselViewModel.headerData.removeObservers(it)
        }
    }
}
