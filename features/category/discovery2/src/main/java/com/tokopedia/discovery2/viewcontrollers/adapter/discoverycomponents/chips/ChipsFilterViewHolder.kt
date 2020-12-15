package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.customview.SpaceItemDecoration
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.setMargin

class ChipsFilterViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private val chipsRecyclerView: RecyclerView = itemView.findViewById(R.id.bannerRecyclerView)
    private var chipsFilterRecycleAdapter: DiscoveryRecycleAdapter = DiscoveryRecycleAdapter(fragment, this)
    private lateinit var chipsFilterViewModel: ChipsFilterViewModel

    init {
        attachRecyclerView()
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        chipsFilterViewModel = discoveryBaseViewModel as ChipsFilterViewModel
        getSubComponent().inject(chipsFilterViewModel)
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        chipsFilterViewModel.getListDataLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            chipsFilterRecycleAdapter.setDataList(item)
            chipsFilterRecycleAdapter.notifyDataSetChanged()
        })
        chipsFilterViewModel.getSyncPageLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            if (item) {
                (fragment as DiscoveryFragment).reSync()
            }
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

    fun onChipSelected(id: String?) {
        chipsFilterViewModel.onChipSelected(id)
    }

    fun onChipUnSelected(id: String?) {
        chipsFilterViewModel.onChipUnSelected(id)
    }

}