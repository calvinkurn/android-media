package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

class ContentCardViewHolder(itemView: View, private val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private var viewModel: ContentCardViewModel? = null

    companion object {
        private const val GRID_SPAN_COUNT = 2
    }

    private val discoveryAdapter: DiscoveryRecycleAdapter
        by lazy {
            DiscoveryRecycleAdapter(fragment)
        }

    private val gridLayoutManager: GridLayoutManager
        by lazy {
            GridLayoutManager(
                itemView.context,
                GRID_SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false
            )
        }

    private val contentCardRv = itemView.findViewById<RecyclerView>(R.id.rvContentCard)

    init {
        setupRecyclerView()
    }

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        viewModel = discoveryBaseViewModel as ContentCardViewModel
        viewModel?.checkForDarkMode(itemView.context)
        viewModel?.apply {
            getSubComponent().inject(this)

            loadContentCard()
        }
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let {
            viewModel?.contentCardList?.observe(it) { result ->
                when (result) {
                    is Success -> showWidget(result.data)
                    is Fail -> hideWidget()
                }
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            viewModel?.contentCardList?.removeObservers(it)
        }
    }

    private fun setupRecyclerView() {
        contentCardRv.apply {
            adapter = discoveryAdapter
            layoutManager = gridLayoutManager
        }
    }

    private fun showWidget(items: ArrayList<ComponentsItem>?) {
        contentCardRv.show()
        discoveryAdapter.setDataList(items)
    }

    private fun hideWidget() {
        contentCardRv.hide()
    }

}
