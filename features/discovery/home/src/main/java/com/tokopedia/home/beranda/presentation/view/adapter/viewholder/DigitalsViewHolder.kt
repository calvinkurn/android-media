package com.tokopedia.home.beranda.presentation.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.TextView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.viewpager.WrapContentViewPager
import com.tokopedia.digital.widget.view.fragment.DigitalChannelFragment
import com.tokopedia.digital.widget.view.fragment.DigitalWidgetFragment
import com.tokopedia.digital.widget.view.listener.DigitalChannelFragmentInteraction
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel

/**
 * @author by errysuprayogi on 11/28/17.
 */

class DigitalsViewHolder(private val listener: HomeCategoryListener, private val fragmentManager: FragmentManager, itemView: View) : AbstractViewHolder<DigitalsViewModel>(itemView), DigitalChannelFragmentInteraction {
    private val viewPagerChannel: WrapContentViewPager
    private val viewPagerWidget: WrapContentViewPager
    private var digitalsHomePagerAdapter: DigitalsHomePagerAdapter? = null
    private var widgetHomePagerAdapter: DigitalsHomePagerAdapter? = null
    private val titleTextView: TextView
    private val seeMoreTextView: TextView
    private var isDigitalWidget: Boolean = false


    init {
        viewPagerChannel = itemView.findViewById(R.id.view_pager_channel)
        viewPagerWidget = itemView.findViewById(R.id.view_pager_widget)
        seeMoreTextView = itemView.findViewById(R.id.see_more)
        titleTextView = itemView.findViewById(R.id.title)
    }


    override fun bind(element: DigitalsViewModel) {
        if (isDigitalWidget) {
            viewPagerChannel.visibility = View.GONE
            viewPagerWidget.visibility = View.VISIBLE
            if (widgetHomePagerAdapter == null) {
                widgetHomePagerAdapter = DigitalsHomePagerAdapter(fragmentManager, DigitalWidgetFragment.newInstance())
                viewPagerWidget.adapter = widgetHomePagerAdapter
                viewPagerWidget.offscreenPageLimit = 1
            }
            viewPagerWidget.currentItem = 0
            widgetHomePagerAdapter!!.notifyDataSetChanged()
        } else {
            if (digitalsHomePagerAdapter == null) {
                digitalsHomePagerAdapter = DigitalsHomePagerAdapter(fragmentManager, DigitalChannelFragment.newInstance(this))
                viewPagerChannel.adapter = digitalsHomePagerAdapter
                viewPagerChannel.offscreenPageLimit = 1
            }
            viewPagerWidget.visibility = View.GONE
            viewPagerChannel.visibility = View.VISIBLE
            digitalsHomePagerAdapter!!.notifyDataSetChanged()
            viewPagerWidget.currentItem = 0
        }


        seeMoreTextView.setOnClickListener {
            RouteManager.route(itemView.context, APPLINK_DIGITAL_BROWSE_PAGE)
            listener.onDigitalMoreClicked(adapterPosition)
        }
    }

    override fun changeToDigitalWidget() {
        isDigitalWidget = true
        viewPagerChannel.visibility = View.GONE
        viewPagerWidget.visibility = View.VISIBLE
        if (widgetHomePagerAdapter == null) {
            widgetHomePagerAdapter = DigitalsHomePagerAdapter(fragmentManager, DigitalWidgetFragment.newInstance())
            viewPagerWidget.adapter = widgetHomePagerAdapter
        }
    }

    override fun updateHeaderText(stringRes: Int) {
        titleTextView.setText(stringRes)
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.layout_digitals
        private val APPLINK_DIGITAL_BROWSE_PAGE = "tokopedia://category-explore?type=2"
    }
}
