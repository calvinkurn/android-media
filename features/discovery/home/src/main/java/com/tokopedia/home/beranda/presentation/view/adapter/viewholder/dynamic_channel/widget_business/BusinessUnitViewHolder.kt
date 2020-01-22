package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.DigitalsHomePagerAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitViewModel
import com.tokopedia.home.beranda.presentation.view.fragment.TabBusinessFragment
import kotlinx.android.synthetic.main.layout_bu_widget.view.*

class BusinessUnitViewHolder(
        private val listener: HomeCategoryListener,
        view: View
) :
        AbstractViewHolder<BusinessUnitViewModel>(view)
{

    private var adapter: DigitalsHomePagerAdapter? = null

    override fun bind(element: BusinessUnitViewModel) {
        if(adapter == null) {
            adapter = DigitalsHomePagerAdapter(listener.childFragmentManager, TabBusinessFragment.newInstance(element.position))
            itemView.viewPager.adapter = adapter
            itemView.viewPager.offscreenPageLimit = 1
            itemView.viewPager.currentItem = 0
        } else {
            adapter?.refreshData()
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_bu_widget
    }
}
