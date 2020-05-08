package com.tokopedia.entertainment.pdp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tokopedia.entertainment.pdp.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_event_pdp_tab_section.view.*


class WidgetEventPDPTabSection @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var recyclerView: RecyclerView

    init {
        View.inflate(context, R.layout.widget_event_pdp_tab_section, this)
        tab_widget.customTabMode = TabLayout.MODE_SCROLLABLE
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
        params.height = context.resources.getDimensionPixelSize(R.dimen.layout_lvl0)
        tab_widget.layoutParams = params
    }

    private fun translateToPosition(itemPosition: Int) {
        (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(itemPosition+1, 0)
    }
}