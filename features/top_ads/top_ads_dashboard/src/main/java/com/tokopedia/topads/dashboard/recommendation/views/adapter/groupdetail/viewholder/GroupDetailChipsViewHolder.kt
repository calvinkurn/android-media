package com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.viewholder

import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.smoothSnapToPosition
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.common.decoration.ChipsInsightItemDecoration
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.ChipsData.chipsList
import com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips.GroupDetailChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.GroupDetailsChipsAdapter
import com.tokopedia.topads.dashboard.recommendation.views.fragments.findPositionOfSelected
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment

class GroupDetailChipsViewHolder(private val view: View, private val onChipClick: (Int) -> Unit) :
    AbstractViewHolder<GroupDetailChipsUiModel>(view) {

    private var adapter: GroupDetailsChipsAdapter? = null
    private val chipsRv: RecyclerView = itemView.findViewById(R.id.groupDetailChipsRv)

    override fun bind(element: GroupDetailChipsUiModel?) {
        adapter = GroupDetailsChipsAdapter(onChipsClick)
        chipsRv.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        chipsRv.addItemDecoration(ChipsInsightItemDecoration())
        chipsRv.adapter = adapter
    }

    private val onChipsClick: (Int) -> Unit = { position ->
        chipsRv.layoutManager?.startSmoothScroll(
            object : LinearSmoothScroller(view.context) {
                override fun getHorizontalSnapPreference(): Int {
                    return SNAP_TO_START
                }

                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                    return TopAdsProductIklanFragment.MILLISECONDS_PER_INCH / displayMetrics.densityDpi
                }
            }.apply { targetPosition = position }
        )
        onChipClick.invoke(position)
    }

    fun onViewAttachedToWindow() {
        adapter?.notifyDataSetChanged()
        chipsRv.smoothSnapToPosition(chipsList.findPositionOfSelected { it.isSelected })
    }

    companion object {
        val LAYOUT = R.layout.top_ads_group_detail_chips_item_layout
    }
}
