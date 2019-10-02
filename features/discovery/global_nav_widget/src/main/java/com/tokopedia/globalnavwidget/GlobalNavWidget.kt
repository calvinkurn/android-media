package com.tokopedia.globalnavwidget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.design.base.BaseCustomView
import java.util.*

class GlobalNavWidget: BaseCustomView {

    companion object {
        private const val GLOBAL_NAV_SPAN_COUNT = 5
    }

    private val backgroundGradientColorList = intArrayOf(-0x51a, -0x1a0a01, -0x140011, -0x1511)

    private var globalNavContainerLayout: LinearLayout? = null
    private var globalNavTitle: TextView? = null
    private var globalNavSeeAllButton: TextView? = null
    private var globalNavRecyclerView: RecyclerView? = null

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val view = View.inflate(context, R.layout.widget_global_nav, this)

        globalNavContainerLayout = view.findViewById(R.id.globalNavContainerLayout)
        globalNavTitle = view.findViewById(R.id.globalNavTitle)
        globalNavSeeAllButton = view.findViewById(R.id.globalNavSeeAllButton)
        globalNavRecyclerView = view.findViewById(R.id.globalNavRecyclerView)

        setupBackground()
    }

    private fun setupBackground() {
        val backgroundIndex = Random().nextInt(backgroundGradientColorList.size)

        val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(Color.WHITE, backgroundGradientColorList[backgroundIndex])
        )

        gradientDrawable.cornerRadius = 0f

        globalNavContainerLayout?.background = gradientDrawable
    }

    fun setData(globalNavWidgetModel: GlobalNavWidgetModel, globalNavWidgetListener: GlobalNavWidgetListener) {
        globalNavTitle?.text = globalNavWidgetModel.title

        val globalNavWidgetAdapter = createGlobalNavWidgetAdapter(globalNavWidgetListener)
        globalNavRecyclerView?.adapter = globalNavWidgetAdapter

        val gridLayoutManager = createGlobalNavRecyclerViewLayoutManager()
        globalNavRecyclerView?.layoutManager = gridLayoutManager

        globalNavWidgetAdapter.setItemList(globalNavWidgetModel.itemList)

        globalNavSeeAllButton?.setOnClickListener { _ ->
            globalNavWidgetListener.onClickSeeAll(globalNavWidgetModel)
        }
    }

    private fun createGlobalNavWidgetAdapter(globalNavWidgetListener: GlobalNavWidgetListener): GlobalNavWidgetAdapter {
        return GlobalNavWidgetAdapter(globalNavWidgetListener)
    }

    private fun createGlobalNavRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(
                context,
                GLOBAL_NAV_SPAN_COUNT,
                GridLayoutManager.VERTICAL,
                false)
    }
}