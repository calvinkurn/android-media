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


class DealsCategoryBottomSheet : BottomSheetUnify() {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = true
    }

    private lateinit var dealsCategoryList: RecyclerView
    private var dealsCategories: ArrayList<DealsCategoryDataView>? = null
    private var dealsCategoryListener: DealsCategoryListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dealsCategories = it.getParcelableArrayList(DEALS_CATEGORIES_EXTRA) ?: arrayListOf()
        }
    }
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
            dealsCategoryListener?.let { dealsCategoryListener ->
                dealsCategories?.let { dealsCategories ->
                    val dealsCategoryAdapter = DealsCategoryAdapter(dealsCategoryListener)
                    dealsCategoryAdapter.dealsCategories = dealsCategories.toMutableList()
                    adapter = dealsCategoryAdapter
                    layoutManager = GridLayoutManager(context, DEALS_CATEGORY_SPAN_COUNT)
                    addItemDecoration(
                        SpacingItemDecoration(
                            getDimens(R.dimen.deals_dp_20),
                            getDimens(R.dimen.deals_dp_30)
                        )
                    )
                }
            }
        }
    }

    fun setListener(dealsCategoryListener: DealsCategoryListener) {
        this.dealsCategoryListener = dealsCategoryListener
    }

    companion object {
        private const val DEALS_CATEGORY_SPAN_COUNT = 5
        private const val DEALS_CATEGORIES_EXTRA = "DEALS_CATEGORIES_EXTRA"
        fun newInstance(dealsCategories: ArrayList<DealsCategoryDataView>): DealsCategoryBottomSheet {
            val bottomSheet = DealsCategoryBottomSheet()
            val bundle = Bundle()
            bundle.putParcelableArrayList(DEALS_CATEGORIES_EXTRA, dealsCategories)
            bottomSheet.arguments = bundle
            return bottomSheet
        }
    }
}
