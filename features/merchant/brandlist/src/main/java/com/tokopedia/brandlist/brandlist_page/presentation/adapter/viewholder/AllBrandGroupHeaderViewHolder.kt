package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.adapter.BrandlistAlphabetHeaderAdapter
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.AllBrandGroupHeaderUiModel
import com.tokopedia.brandlist.common.Constant.DEFAULT_SELECTED_CHIPS
import java.text.NumberFormat
import java.util.*


class AllBrandGroupHeaderViewHolder(itemView: View) : AbstractViewHolder<AllBrandGroupHeaderUiModel>(itemView) {

    private lateinit var recyclerViewBrandHeader: RecyclerView
    private var tvTotalBrand: AppCompatTextView? = null
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: BrandlistAlphabetHeaderAdapter? = null
    private lateinit var allBrandGroupHeaderUiModel: AllBrandGroupHeaderUiModel

    init {
        initLayout(itemView)
    }

    private fun initLayout(view: View) {
        tvTotalBrand = itemView.findViewById(R.id.tv_total_brand)
        recyclerViewBrandHeader = itemView.findViewById(R.id.rv_groups_chip)

        layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewBrandHeader.layoutManager = layoutManager
        val animator = recyclerViewBrandHeader.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }

        recyclerViewBrandHeader.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                allBrandGroupHeaderUiModel.recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
                adapter?.recyclerViewState = allBrandGroupHeaderUiModel.recyclerViewState
            }
        })
    }

    override fun bind(element: AllBrandGroupHeaderUiModel) {
        val totalBrand: Int = element.totalBrands
        val headerList: MutableList<String> = getAlphabeticalShopFilter(totalBrand)

        this.allBrandGroupHeaderUiModel = element
        element.recyclerViewLastState?.let {
            if(element.selectedChip != DEFAULT_SELECTED_CHIPS){
                recyclerViewBrandHeader.layoutManager?.onRestoreInstanceState(it)
            }
        }

        adapter = BrandlistAlphabetHeaderAdapter(element.listener)
        adapter?.let {
            it.headerList = headerList
            it.selectedPosition = element.selectedChip
            it.lastTimeChipsClicked = element.lastTimeChipsClicked
            it.notifyDataSetChanged()
        }
        recyclerViewBrandHeader.adapter = adapter
    }

    private fun getAlphabeticalShopFilter(totalBrand: Int): MutableList<String> {
        var char: Char = 'A'
        var tempHeaderList = mutableListOf<String>()
        val totalBrandContent: String = NumberFormat.getNumberInstance(Locale.US)
                .format(totalBrand).toString().replace(",", ".") + " " + getString(R.string.brandlist_brand_label)
        tempHeaderList.add(totalBrandContent)
        tempHeaderList.add("Semua Brand")
        while (char <= 'Z') {
            tempHeaderList.add(char.toString())
            ++char
        }
        return tempHeaderList
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.brandlist_all_brand_group_header
    }
}