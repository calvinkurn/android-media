package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.adapter.BrandlistAlphabetHeaderAdapter
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.AllBrandGroupHeaderViewModel
import java.text.NumberFormat
import java.util.*


class AllBrandGroupHeaderViewHolder(itemView: View) : AbstractViewHolder<AllBrandGroupHeaderViewModel>(itemView) {

    private var tvTotalBrand: AppCompatTextView? = null
    private var recyclerViewBrandHeader: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: BrandlistAlphabetHeaderAdapter? = null
    private val context: Context

    init {
        context = itemView.context
        tvTotalBrand = itemView.findViewById(R.id.tv_total_brand)
        recyclerViewBrandHeader = itemView.findViewById(R.id.rv_groups_chip)
    }

    override fun bind(element: AllBrandGroupHeaderViewModel) {
        val headerList: MutableList<String> = getAlphabeticalShopFilter()
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        recyclerViewBrandHeader?.layoutManager = layoutManager
        adapter = BrandlistAlphabetHeaderAdapter(element.listener)
        adapter?.updateDataHeaderList(headerList)
        recyclerViewBrandHeader?.adapter = adapter

        tvTotalBrand?.text = element.groupHeaderText
//        element.totalBrands?.let {
//            val totalBrandContent: String = NumberFormat.getNumberInstance(Locale.US)
//                    .format(it).toString().replace(",", ".") + " " +
//                    getString(R.string.brandlist_brand_label)
//            tvTotalBrand?.text = totalBrandContent
//        }
    }

    private fun getAlphabeticalShopFilter(): MutableList<String> {
        var c: Char
        var tempHeaderList = mutableListOf<String>()
        c = 'A'
        tempHeaderList.add("Semua Brand")
        while (c <= 'Z') {
            println("getAlphabeticalShopFilter: $c")
            tempHeaderList.add(c.toString())
            ++c
        }
        return tempHeaderList
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.brandlist_all_brand_group_header
    }
}