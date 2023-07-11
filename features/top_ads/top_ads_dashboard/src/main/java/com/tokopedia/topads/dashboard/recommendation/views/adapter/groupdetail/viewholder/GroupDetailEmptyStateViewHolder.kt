package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailEmptyStateUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.recommendation.EmptyStatePagerAdapter
import com.tokopedia.unifycomponents.PageControl
import timber.log.Timber

class GroupDetailEmptyStateViewHolder(view: View) :
    AbstractViewHolder<GroupDetailEmptyStateUiModel>(view) {

    private var emptyStateRecyclerView: RecyclerView = view.findViewById(R.id.emptyStateView)
    private var pageControlEmptyState: PageControl =
        view.findViewById(R.id.pageControlEmptyState)

    private val pagerAdapter by lazy {
        EmptyStatePagerAdapter()
    }

    private val layoutManager by lazy {
        return@lazy object : LinearLayoutManager(view.context, HORIZONTAL, false) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    override fun bind(element: GroupDetailEmptyStateUiModel) {
        pagerAdapter.emptyStatePages = element.statesList
        emptyStateRecyclerView.layoutManager = layoutManager
        emptyStateRecyclerView.adapter = pagerAdapter
        if (element.statesList.size > Int.ONE) {
            pageControlEmptyState.setIndicator(element.statesList.size)
            pageControlEmptyState.show()
        } else {
            pageControlEmptyState.hide()
        }

        emptyStateRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val currentPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (currentPosition != RecyclerView.NO_POSITION && element.statesList.size > 1) {
                    pageControlEmptyState.setCurrentIndicator(currentPosition)
                }
            }
        })
        try {
            PagerSnapHelper().attachToRecyclerView(emptyStateRecyclerView)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    companion object {
        val LAYOUT = R.layout.topads_insight_centre_empty_state_layout
    }
}
