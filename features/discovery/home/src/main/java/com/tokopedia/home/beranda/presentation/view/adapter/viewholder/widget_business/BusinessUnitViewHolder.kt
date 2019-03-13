package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business

import android.support.v4.app.FragmentManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.DigitalsHomePagerAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel
import com.tokopedia.home.beranda.presentation.view.fragment.TabBusinessFragment
import kotlinx.android.synthetic.main.layout_bu_widget.view.*

class BusinessUnitViewHolder(
        private val fm: FragmentManager,
        view: View
) :
        AbstractViewHolder<DigitalsViewModel>(view)
{

    private lateinit var adapter: DigitalsHomePagerAdapter

    override fun bind(element: DigitalsViewModel) {
        adapter = DigitalsHomePagerAdapter(fm, TabBusinessFragment())
        itemView.viewPager.adapter = adapter
        itemView.viewPager.offscreenPageLimit = 1
        itemView.viewPager.currentItem = 0
        adapter.notifyDataSetChanged()
    }

    companion object {
        val LAYOUT = R.layout.layout_bu_widget
    }
}
