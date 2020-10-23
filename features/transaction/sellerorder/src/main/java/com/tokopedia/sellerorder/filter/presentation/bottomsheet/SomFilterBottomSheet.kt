package com.tokopedia.sellerorder.filter.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.filter.presentation.adapter.FilterItemDecoration
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterListener
import com.tokopedia.unifycomponents.BottomSheetUnify

class SomFilterBottomSheet(private val mActivity: FragmentActivity?) : BottomSheetUnify(),
        SomFilterListener.Sort, SomFilterListener.StatusOrder, SomFilterListener.TypeOrder,
        SomFilterListener.Courier, SomFilterListener.Label, SomFilterListener.Deadline {

    private var rvSomFilter: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = View.inflate(mActivity, R.layout.bottomsheet_som_filter_list, null)
        rvSomFilter = view.findViewById(R.id.rvSomFilter)
        setChild(view)
        setTitle(TITLE_FILTER)
        showKnob = true
        isDragable = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setChipsLayoutManager(list: List<RecyclerView?>) {
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(mActivity)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()

        list.forEach { recyclerView ->
            recyclerView?.apply {
                addItemDecoration(FilterItemDecoration())
                layoutManager = chipsLayoutManager
                ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR)
            }
        }
    }

    open fun showDialog() {
        mActivity?.supportFragmentManager?.run {
            show(this, TITLE_FILTER)
        }
    }

    override fun onChipsSortClicked(param: Int, position: Int, chipType: String) {
        TODO("Not yet implemented")
    }

    override fun onChipsStatusClicked(ids: List<Int>, position: Int, chipType: String) {
        TODO("Not yet implemented")
    }

    override fun onChipsTypeOrderClicked(id: Int, position: Int, chipType: String) {
        TODO("Not yet implemented")
    }

    override fun onChipsCourierClicked(id: Int, position: Int, chipType: String) {
        TODO("Not yet implemented")
    }

    override fun onChipsLabelClicked(param: Int, position: Int, chipType: String) {
        TODO("Not yet implemented")
    }

    override fun onChipsDeadlineClicked(param: Int, position: Int, chipType: String) {
        TODO("Not yet implemented")
    }

    companion object {
        const val TITLE_FILTER = "Filter"
    }
}