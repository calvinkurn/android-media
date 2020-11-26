package com.tokopedia.deals.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.tokopedia.deals.R
import com.tokopedia.deals.home.listener.DealsCategoryListener
import com.tokopedia.deals.home.ui.adapter.DealsCategoryAdapter
import com.tokopedia.deals.home.ui.dataview.DealsCategoryDataView
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.unifycomponents.BottomSheetUnify


class DealsCategoryBottomSheet(dealsCategoryListener: DealsCategoryListener) : BottomSheetUnify() {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = true
    }

    private val dealsCategoryAdapter = DealsCategoryAdapter(dealsCategoryListener)
    private lateinit var dealsCategoryList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dealsCategoryList = RecyclerView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        setChild(dealsCategoryList)
        setTitle(getString(R.string.deals_select_category))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dealsCategoryList.apply {
            adapter = dealsCategoryAdapter
            layoutManager = GridLayoutManager(context, DEALS_CATEGORY_SPAN_COUNT)
            addItemDecoration(
                SpacingItemDecoration(getDimens(R.dimen.deals_dp_20),getDimens(R.dimen.deals_dp_30))
            )
        }
    }

    fun showDealsCategories(dealsCategories: List<DealsCategoryDataView>) {
        dealsCategoryAdapter.dealsCategories = dealsCategories.toMutableList()
    }

    companion object {
        private const val DEALS_CATEGORY_SPAN_COUNT = 5
    }
}