package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.Chip
import com.tokopedia.topads.dashboard.view.adapter.beranda.AdPlacementRvAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify

class RingkasanBottomSheet(private val itemClick: (Chip) -> Unit) : BottomSheetUnify() {

    private lateinit var rvAdPlacement: RecyclerView
    private lateinit var adapter: AdPlacementRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setUpChildView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setUpChildView() {
        val view = View.inflate(context, childLayout, null)
        setChild(view)
        rvAdPlacement = view.findViewById(R.id.rvSelectAdsTypeTopAdsInsight)

        view.run {
            setPadding(
                paddingLeft, paddingTop, paddingRight,
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            )
        }
        isDragable = true
        isHideable = true
        showKnob = true
        showCloseIcon = false
        setTitle(resources.getString(R.string.topads_dash_ad_placement))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AdPlacementRvAdapter.createInstance(itemClick)
        adapter.addItems(listOf(Chip("ank"), Chip("okay"), Chip("oakd"), Chip("okdied")))
        rvAdPlacement.layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        rvAdPlacement.adapter = adapter
    }

    companion object {
        private val childLayout = R.layout.topads_insight_select_ads_type_bottomsheet
        fun createInstance(itemClick: (Chip) -> Unit) = RingkasanBottomSheet(itemClick)
    }
}