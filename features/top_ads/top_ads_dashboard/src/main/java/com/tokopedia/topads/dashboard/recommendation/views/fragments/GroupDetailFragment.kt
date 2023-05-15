package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.smoothSnapToPosition
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupInsightsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupPerformanceWidgetUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.ChipsData.chipsList
import com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips.GroupDetailChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.GroupDetailAdapter
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.GroupDetailsChipsAdapter
import com.tokopedia.topads.dashboard.recommendation.views.adapter.groupdetail.factory.GroupDetailAdapterFactoryImpl
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment

class GroupDetailFragment : TkpdBaseV4Fragment() {

    private var groupDetailsRecyclerView: RecyclerView? = null
    private var groupDetailChipsRv: RecyclerView? = null
    private var groupChipsLayout: View? = null
    private val groupDetailAdapter by lazy { GroupDetailAdapter(GroupDetailAdapterFactoryImpl()) }
    private var groupDetailsChipsAdapter: GroupDetailsChipsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(
            R.layout.fragment_group_detail_fragment, container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        setUpRecyclerView()
        setUpChipsRecyclerView()
    }


    private fun setUpChipsRecyclerView() {
        groupDetailsChipsAdapter = GroupDetailsChipsAdapter(onChipsClick)
        groupDetailChipsRv?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        groupDetailChipsRv?.adapter = groupDetailsChipsAdapter

    }

    private val onChipsClick: (Int) -> Unit = { position ->
        groupDetailChipsRv?.layoutManager?.startSmoothScroll(object :
            LinearSmoothScroller(context) {
            override fun getHorizontalSnapPreference(): Int {
                return SNAP_TO_START
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return TopAdsProductIklanFragment.MILLISECONDS_PER_INCH / displayMetrics.densityDpi
            }
        }.apply { targetPosition = position })
        groupDetailAdapter.updateItem()
    }

    private fun setUpRecyclerView() {
        groupDetailsRecyclerView?.layoutManager = LinearLayoutManager(context)
        groupDetailsRecyclerView?.adapter = groupDetailAdapter
        groupDetailAdapter.submitList(
            listOf(
                InsightTypeChipsUiModel(
                    listOf(
                        InsightTypeChipsItemUiModel("Iklan Produk"),
                        InsightTypeChipsItemUiModel("Cuci Gudang Agustus...")
                    )
                ),
                GroupPerformanceWidgetUiModel(),
                GroupDetailChipsUiModel(),
                GroupInsightsUiModel(),
                GroupInsightsUiModel(),
                GroupInsightsUiModel(),
                GroupInsightsUiModel(),
                GroupInsightsUiModel(),
                GroupInsightsUiModel(),
                GroupInsightsUiModel(),
                GroupInsightsUiModel(),
                GroupInsightsUiModel(),
                GroupInsightsUiModel(),
                GroupInsightsUiModel(),
                GroupInsightsUiModel(),
                GroupInsightsUiModel(),
                GroupInsightsUiModel(),
            )
        )
        groupDetailsRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position =
                    (recyclerView.layoutManager as LinearLayoutManager?)?.findFirstVisibleItemPosition()
                        ?: return

                if (dy > 0) {
                    if (position > 1) {
                        groupChipsLayout?.visibility = View.VISIBLE
                        groupDetailsChipsAdapter?.notifyDataSetChanged()
                        groupDetailChipsRv?.smoothSnapToPosition(chipsList.findPositionOfSelected { it.isSelected })
                    }
                } else {
                    if (position <= 1) {
                        groupChipsLayout?.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun initializeViews() {
        groupDetailsRecyclerView = view?.findViewById(R.id.groupDetailsRecyclerView)
        groupDetailChipsRv = view?.findViewById(R.id.groupDetailChipsRv)
        groupChipsLayout = view?.findViewById(R.id.groupChipsLayout)
    }

    companion object {
        fun createInstance() = GroupDetailFragment()
    }

    override fun getScreenName(): String = javaClass.name


}

fun <T> Iterable<T>.findPositionOfSelected(predicate: (T) -> Boolean): Int {
    for ((index, element) in this.withIndex()) if (predicate(element)) return index
    return Int.ZERO
}
