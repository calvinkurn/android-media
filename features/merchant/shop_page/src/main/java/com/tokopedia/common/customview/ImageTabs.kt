package com.tokopedia.common.customview

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.common.ColorPallete
import com.tokopedia.common.setRetainTextColor
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.shop.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ImageUnify.Companion.TYPE_CIRCLE
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.Typography

class ImageTabs(val tabsUnify: TabsUnify) {
    var tabType = TYPE_CIRCLE
    var selectedIndex = 0
    private var listener: ImageTabsListener? = null
    private var dataList: List<ImageTabData>? = null
    private var colorPallete: ColorPallete? = null
    private var totalTabViewWidth = 0

    interface ImageTabsListener {
        fun onImageTabSelected(index: Int, data: ImageTabData?)
    }

    init {
        tabsUnify.tabLayout.isTabIndicatorFullWidth = false
        tabsUnify.tabLayout.setBackgroundColor(Color.TRANSPARENT)
        tabsUnify.tabLayout.tabRippleColor = ColorStateList.valueOf(Color.TRANSPARENT)
        val centeredTabIndicator =
            ContextCompat.getDrawable(
                tabsUnify.context,
                R.drawable.shape_showcase_tab_indicator_color
            )
        tabsUnify.tabLayout.setSelectedTabIndicator(centeredTabIndicator)

        tabsUnify.whiteShadeLeft.visibility = View.GONE
        tabsUnify.whiteShadeRight.visibility = View.GONE

        tabsUnify.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedIndex = tab?.position ?: 0
                listener?.onImageTabSelected(selectedIndex, getData()?.getOrNull(selectedIndex))

                val text = tab?.customView?.findViewById<Typography>(R.id.text1)
                text?.apply {
                    setWeight(Typography.BOLD)
                    invalidate()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val text = tab?.customView?.findViewById<Typography>(R.id.text1)
                text?.apply {
                    setWeight(Typography.REGULAR)
                    invalidate()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // noop
            }
        })
    }

    fun setColor(colorPallete: ColorPallete) {
        this.colorPallete = colorPallete
    }

    fun getData(): List<ImageTabData>? {
        return dataList
    }

    fun setData(dataListInput: List<ImageTabData>?) {
        val curDataList = this.dataList
        if (!dataHasChanged(curDataList, dataListInput)) {
            return
        }
        // reset selected.
        selectedIndex = 0

        this.dataList = dataListInput
        if (dataListInput == null) {
            tabsUnify.visibility = View.GONE
            return
        } else {
            tabsUnify.visibility = View.VISIBLE
        }
        tabsUnify.tabLayout.removeAllTabs()
        repeat(dataListInput.size) {
            tabsUnify.tabLayout.newTab().let {
                tabsUnify.tabLayout.addTab(it, false)
            }
        }

        for (i in 0 until tabsUnify.tabLayout.tabCount) {
            val tab = tabsUnify.tabLayout.getTabAt(i)
            val tabView =
                LayoutInflater.from(tabsUnify.context).inflate(R.layout.layout_tab_image, tabsUnify, false)

            tabView.findViewById<ImageUnify>(R.id.icon).type = tabType
            tab?.customView = tabView
            (tabView.findViewById<ImageUnify>(R.id.icon)).setImageUrl(dataListInput[i].imageUrl)
            val tv = (tabView.findViewById<TextView>(R.id.text1))
            tv.text = dataListInput[i].name
            tv.setRetainTextColor(colorPallete, ColorPallete.ColorType.PRIMARY_TEXT)
            tabView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            totalTabViewWidth+= (tabView.measuredWidth + 16f.dpToPx() + 16f.dpToPx()).toInt()
        }
        tabsUnify.post {
            tabsUnify.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val screenWidth = DeviceScreenInfo.getScreenWidth(tabsUnify.context) - 16f.dpToPx() - 16f.dpToPx()
            if(totalTabViewWidth < screenWidth) {
                tabsUnify.customTabMode = TabLayout.MODE_FIXED
                tabsUnify.customTabGravity = TabLayout.GRAVITY_FILL
            } else {
                tabsUnify.customTabMode = TabLayout.MODE_SCROLLABLE
                tabsUnify.customTabGravity = TabLayout.GRAVITY_FILL
            }
            tabsUnify.tabLayout.getTabAt(selectedIndex)?.select()
            totalTabViewWidth = 0
        }
    }

    fun dataHasChanged(
        curDataList: List<ImageTabData>?,
        dataListInput: List<ImageTabData>?
    ): Boolean {
        if (curDataList == null) return true
        val etalaseList = dataListInput ?: return true
        if (curDataList.size != etalaseList.size) {
            return true
        } else {
            for ((i, etalase) in etalaseList.withIndex()) {
                if (etalase.id != curDataList[i].id) {
                    return true
                }
            }
            return false
        }
    }

    /**
     * TYPE_RECT or TYPE_CIRCLE
     */
    fun setTypeTab(type: Int) {
        val prevType = tabType
        this.tabType = type
        if (prevType != type) {
            for (i in 0 until tabsUnify.tabLayout.tabCount) {
                val tab = tabsUnify.tabLayout.getTabAt(i)
                tab?.customView?.findViewById<ImageUnify>(R.id.icon)?.type = tabType
            }
        }
    }

    fun setListener(listener: ImageTabsListener) {
        this@ImageTabs.listener = listener
    }

}

data class ImageTabData(
    val id: String,
    val name: String,
    val imageUrl: String
)
