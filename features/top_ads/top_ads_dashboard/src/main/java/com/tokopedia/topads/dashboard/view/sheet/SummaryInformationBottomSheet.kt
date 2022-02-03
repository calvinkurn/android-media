package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.Information
import com.tokopedia.topads.dashboard.view.adapter.beranda.InformationRvAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify

class SummaryInformationBottomSheet() : BottomSheetUnify() {

    private lateinit var recyclerView: RecyclerView
    private val adapter by lazy { InformationRvAdapter.createInstance(getInformationList()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setUpChildView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setUpChildView() {
        val view = View.inflate(context, childLayout, null)
        setChild(view)
        recyclerView = view.findViewById(R.id.rvSelectAdsTypeTopAdsInsight)

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
        setTitle(resources.getString(R.string.topads_dashboard_information_title))
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
    }

    private fun getInformationList(): List<Information> {
        resources.apply {
            return listOf(
                Information(
                    getString(com.tokopedia.topads.common.R.string.topads_common_tampil),
                    getString(R.string.tampil_sub_title)
                ),
                Information(
                    getString(com.tokopedia.topads.common.R.string.topads_common_klik),
                    getString(R.string.klik_sub_title)
                ),
                Information(
                    getString(R.string.label_top_ads_sold),
                    getString(R.string.total_terjual_sub_title)
                ),
                Information(
                    getString(com.tokopedia.topads.common.R.string.topads_common_pendapatan),
                    getString(R.string.pendapatan_sub_title)
                ),
                Information(
                    getString(com.tokopedia.topads.common.R.string.topads_common_pengeluaran),
                    getString(R.string.pengeluaran_sub_title)
                ),
                Information(
                    getString(R.string.topads_dashboard_efektivitas_iklan),
                    getString(R.string.efektivitas_iklan_sub_title)
                )
            )
        }
    }

    companion object {
        private val childLayout = R.layout.topads_single_recyclerview_layout
        fun createInstance() = SummaryInformationBottomSheet()
    }
}