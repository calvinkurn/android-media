package com.tokopedia.buyerorder.unifiedhistory.view.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.buyerorder.R
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem


/**
 * Created by fwidjaja on 29/06/20.
 */
class UohListFragment: BaseDaggerFragment() {
    companion object {
        @JvmStatic
        fun newInstance(): UohListFragment {
            return UohListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_uoh_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderQuickFilter()
    }

    private fun renderQuickFilter() {
        val listQuickFilter = arrayListOf<QuickFilterItem>()

        val filterItem1 = CustomViewQuickFilterItem()
        filterItem1.name = "X"
        listQuickFilter.add(filterItem1)

        val filterItem2 = CustomViewQuickFilterItem()
        filterItem2.name = "Semua Tanggal"

        val str = "Semua Tanggal"
        val spannableString = SpannableString(str)
        val d = resources.getDrawable(R.drawable.ic_unified_chevron_down)
        d.setBounds(0, 0, d.intrinsicWidth, d.intrinsicHeight)
        val span = ImageSpan(d, ImageSpan.ALIGN_BOTTOM)
        spannableString.setSpan(span, str.length, str.length + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        filterItem2.name = spannableString.toString()
        listQuickFilter.add(filterItem2)

        val filterItem3 = CustomViewQuickFilterItem()
        filterItem3.name = "Semua Status"
        listQuickFilter.add(filterItem3)

        val filterItem4 = CustomViewQuickFilterItem()
        filterItem4.name = "Semua Kategori"
        listQuickFilter.add(filterItem4)

        quick_filter?.renderFilter(listQuickFilter, 0)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}
}