package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
            })
            viewModel.getUpdatePositionsLD().observe(it, { (oldPos, newPos) ->
                mDiscoveryRecycleAdapter.notifyItemChanged(oldPos)
                mDiscoveryRecycleAdapter.notifyItemChanged(newPos)
                anchorRV.post {
                    anchorRV.smoothScrollToPosition(newPos)
//                    linearLayoutManager.scrollToPositionWithOffset(newPos,0)
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