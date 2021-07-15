package com.tokopedia.entertainment.pdp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPTabEntity
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_event_pdp_tab_section.view.*


class WidgetEventPDPTabSection @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var recyclerView: RecyclerView

    init {
        initialView()
    }

    private fun initialView(){
        View.inflate(context, R.layout.widget_event_pdp_tab_section, this)
        tab_widget.customTabMode = TabLayout.MODE_SCROLLABLE
        tab_widget.customTabGravity = TabLayout.GRAVITY_FILL
        tab_widget.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.select()
                translateToPosition(tab.position)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }
        })
    }

    fun setDynamicTitle(titles: List<EventPDPTabEntity>) {
        tab_widget.tabLayout.removeAllTabs()
        for (title in titles){
            tab_widget.addNewTab(title.title)
        }
    }

    fun setScrolledSection(currentPosition: Int) {
        tab_widget.tabLayout.apply {
            if (currentPosition != 0)
                getTabAt(currentPosition-1)?.let {
                    it.select()
                }
        }
    }


    fun setRecycleView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    fun setScrolledMode(){
        val params = tab_widget.layoutParams
        params.height = context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl8)
        tab_widget.layoutParams = params
        tab_widget.customTabMode = TabLayout.MODE_SCROLLABLE
    }

    fun setNullMode(){
        val params = tab_widget.layoutParams
        params.height = context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0)
        tab_widget.layoutParams = params
    }

    private fun translateToPosition(itemPosition: Int) {
        val position = if(itemPosition == 0) itemPosition else itemPosition + 1
        (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
    }
}