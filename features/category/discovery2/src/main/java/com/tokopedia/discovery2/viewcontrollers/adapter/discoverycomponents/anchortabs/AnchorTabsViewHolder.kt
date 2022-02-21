package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs

import android.util.DisplayMetrics
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder


class AnchorTabsViewHolder(itemView: View, val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {
    private val anchorRV: RecyclerView = itemView.findViewById(R.id.anchor_rv)
    private var linearLayoutManager: LinearLayoutManager =
        LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter

    private var smoothScroller: SmoothScroller = object : LinearSmoothScroller(itemView.context) {
        override fun getHorizontalSnapPreference(): Int {
            return SNAP_TO_START
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return if (displayMetrics != null) {
//                Todo:: convert to constant
                return 150f/displayMetrics.densityDpi
            } else
                super.calculateSpeedPerPixel(displayMetrics)

        }

    }

    init {
        anchorRV.layoutManager = linearLayoutManager
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
        mDiscoveryRecycleAdapter.setHasStableIds(false)
        anchorRV.adapter = mDiscoveryRecycleAdapter
    }

    private lateinit var viewModel: AnchorTabsViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as AnchorTabsViewModel
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel.getCarouselItemsListData().observe(it, { item ->
                mDiscoveryRecycleAdapter.addDataList(item)
                anchorRV.post {
                    if (viewModel.selectedSectionPos < viewModel.getListSize()){
                        if(viewModel.wasSectionDeleted()){
                            linearLayoutManager.scrollToPositionWithOffset(viewModel.selectedSectionPos,0)
                        }else {
                            smoothScroller.targetPosition = viewModel.selectedSectionPos
                            linearLayoutManager.startSmoothScroll(smoothScroller)
                        }
                    }
                }
            })
            viewModel.getUpdatePositionsLD().observe(it, { (oldPos, newPos) ->
                if (oldPos < viewModel.getListSize())
                    mDiscoveryRecycleAdapter.notifyItemChanged(oldPos)

                if (newPos < viewModel.getListSize())
                    mDiscoveryRecycleAdapter.notifyItemChanged(newPos)

                anchorRV.post {
                    if (newPos < viewModel.getListSize()) {
                        smoothScroller.targetPosition = newPos
                        linearLayoutManager.startSmoothScroll(smoothScroller)
                    }
                }
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel.getCarouselItemsListData().removeObservers(it)
            viewModel.getUpdatePositionsLD().removeObservers(it)
        }
    }

}