package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchAllBrandGroupHeaderViewModel
import java.text.NumberFormat
import java.util.*

class BrandlistSearchGroupHeaderViewHolder(itemView: View) : AbstractViewHolder<BrandlistSearchAllBrandGroupHeaderViewModel>(itemView) {

    private lateinit var recyclerViewBrandHeader: RecyclerView
    private var tvTotalBrand: AppCompatTextView? = null
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: BrandlistSearchAlphabetHeaderAdapter? = null
    private lateinit var allBrandGroupHeaderViewModel: BrandlistSearchAllBrandGroupHeaderViewModel

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
                allBrandGroupHeaderViewModel.recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
                adapter?.recyclerViewState = allBrandGroupHeaderViewModel.recyclerViewState
            }
        })
    }

    override fun bind(element: BrandlistSearchAllBrandGroupHeaderViewModel) {
        val totalBrand: Int = element.totalBrands
        val headerList: MutableList<String> = getAlphabeticalShopFilter(totalBrand)

        this.allBrandGroupHeaderViewModel = element
        element.recyclerViewLastState?.let {
            recyclerViewBrandHeader.layoutManager?.onRestoreInstanceState(it)
        }

        adapter = BrandlistSearchAlphabetHeaderAdapter(element.listener)
        adapter?.headerList = headerList
        adapter?.selectedPosition = element.selectedChip
        adapter?.notifyDataSetChanged()
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