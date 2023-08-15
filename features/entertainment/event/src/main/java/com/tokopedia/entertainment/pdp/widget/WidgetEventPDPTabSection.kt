package com.tokopedia.entertainment.pdp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tokopedia.entertainment.databinding.WidgetEventPdpTabSectionBinding
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPTabEntity
import com.tokopedia.unifycomponents.BaseCustomView

class WidgetEventPDPTabSection @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var recyclerView: RecyclerView

    private val binding = WidgetEventPdpTabSectionBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )
    init {
        initialView()
    }

    private fun initialView(){
        with(binding) {
            tabWidget.customTabMode = TabLayout.MODE_SCROLLABLE
            tabWidget.customTabGravity = TabLayout.GRAVITY_FILL
            tabWidget.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
    }

    fun setDynamicTitle(titles: List<EventPDPTabEntity>) {
        with(binding) {
            tabWidget.tabLayout.removeAllTabs()
            for (title in titles) {
                tabWidget.addNewTab(title.title)
            }
        }
    }

    fun setScrolledSection(currentPosition: Int) {
        binding.tabWidget.tabLayout.apply {
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
        with(binding) {
            val params = tabWidget.layoutParams
            params.height =
                context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl8)
            tabWidget.layoutParams = params
            tabWidget.customTabMode = TabLayout.MODE_SCROLLABLE
        }
    }

    fun setNullMode(){
        with(binding) {
            val params = tabWidget.layoutParams
            params.height =
                context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0)
            binding.tabWidget.layoutParams = params
        }
    }

    private fun translateToPosition(itemPosition: Int) {
        val position = if(itemPosition == 0) itemPosition else itemPosition + 1
        (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
    }
}
