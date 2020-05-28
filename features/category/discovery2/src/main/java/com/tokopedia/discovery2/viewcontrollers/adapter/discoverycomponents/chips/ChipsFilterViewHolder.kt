package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.list.adapter.SpaceItemDecoration
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin

class ChipsFilterViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private val chipsRecyclerView: RecyclerView = itemView.findViewById(R.id.bannerRecyclerView)
    private var chipsFilterRecycleAdapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment)
    private lateinit var chipsFilterViewModel: ChipsFilterViewModel

    init {
        attachRecyclerView()
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        chipsFilterViewModel = discoveryBaseViewModel as ChipsFilterViewModel
        setUpObservers()
    }

    private fun setUpObservers() {
        chipsFilterViewModel.getListDataLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            chipsFilterRecycleAdapter.setDataList(item)
        })
    }

    private fun attachRecyclerView() {
        chipsRecyclerView.apply {
            adapter = chipsFilterRecycleAdapter
            val chipsLayoutManager = LinearLayoutManager(fragment.context, LinearLayoutManager.HORIZONTAL, false)
            layoutManager = chipsLayoutManager
            setMargin(resources.getDimensionPixelSize(R.dimen.dp_12),
                    resources.getDimensionPixelSize(R.dimen.dp_8),
                    resources.getDimensionPixelSize(R.dimen.dp_12),
                    resources.getDimensionPixelSize(R.dimen.dp_8))
            addItemDecoration(SpaceItemDecoration(context.resources.getDimensionPixelSize(R.dimen.dp_4), LinearLayoutManager.HORIZONTAL))
        }
    }

}