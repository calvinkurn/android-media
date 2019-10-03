package com.tokopedia.globalnavwidget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.globalnavwidget.GlobalNavWidgetConstant.GLOBAL_NAV_SPAN_COUNT
import kotlinx.android.synthetic.main.global_nav_widget_layout.view.*

class GlobalNavWidget: BaseCustomView {

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
        View.inflate(context, R.layout.global_nav_widget_layout, this)
    }

    fun setData(globalNavWidgetModel: GlobalNavWidgetModel, globalNavWidgetListener: GlobalNavWidgetListener) {
        setTitle(globalNavWidgetModel.title)
        setBackground(globalNavWidgetModel.background)
        setContent(globalNavWidgetModel, globalNavWidgetListener)

        globalNavSeeAllButton?.setOnClickListener { _ ->
            globalNavWidgetListener.onClickSeeAll(globalNavWidgetModel)
        }
    }

    private fun setTitle(title: String) {
        globalNavTitle?.text = MethodChecker.fromHtml(title)
    }

    private fun setBackground(backgroundImgUrl: String) {
        globalNavContainerLayout?.let {
            ImageHandler.loadBackgroundImage(it, backgroundImgUrl)
        }
    }

    private fun setContent(
            globalNavWidgetModel: GlobalNavWidgetModel,
            globalNavWidgetListener: GlobalNavWidgetListener
    ) {
        if (globalNavWidgetModel.navTemplate == GlobalNavWidgetConstant.NAV_TEMPLATE_CARD) {
            hidePillContent()
            setCardContent(globalNavWidgetModel.itemList, globalNavWidgetListener)
        }
        else if (globalNavWidgetModel.navTemplate == GlobalNavWidgetConstant.NAV_TEMPLATE_PILL) {
            hideCardContent()
            setPillContent(globalNavWidgetModel.itemList, globalNavWidgetListener)
        }
    }

    private fun setPillContent(
            globalNavWidgetItemList: List<GlobalNavWidgetModel.Item>,
            globalNavWidgetListener: GlobalNavWidgetListener
    ) {
        globalNavPillRecyclerView?.visibility = View.VISIBLE
        globalNavPillRecyclerView?.adapter = createPillAdapter(globalNavWidgetItemList, globalNavWidgetListener)
        globalNavPillRecyclerView?.layoutManager = createPillRecyclerViewLayoutManager()
    }

    private fun createPillAdapter(
            globalNavWidgetItemList: List<GlobalNavWidgetModel.Item>,
            globalNavWidgetListener: GlobalNavWidgetListener
    ): RecyclerView.Adapter<*> {

        val pillsAdapter = GlobalNavWidgetPillAdapter(globalNavWidgetListener)
        pillsAdapter.setItemList(globalNavWidgetItemList)

        return pillsAdapter
    }

    private fun createPillRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, GLOBAL_NAV_SPAN_COUNT, GridLayoutManager.VERTICAL, false)
    }

    private fun hidePillContent() {
        globalNavPillRecyclerView?.visibility = View.GONE
    }

    private fun setCardContent(
            globalNavWidgetItemList: List<GlobalNavWidgetModel.Item>,
            globalNavWidgetListener: GlobalNavWidgetListener
    ) {
        globalNavCardRecyclerView?.visibility = View.VISIBLE
        globalNavCardRecyclerView?.adapter = createCardAdapter(globalNavWidgetItemList, globalNavWidgetListener)
        globalNavCardRecyclerView?.layoutManager = createCardRecyclerViewLayoutManager()
    }

    private fun createCardAdapter(
            globalNavWidgetItemList: List<GlobalNavWidgetModel.Item>,
            globalNavWidgetListener: GlobalNavWidgetListener
    ): RecyclerView.Adapter<*> {

        val pillsAdapter = GlobalNavWidgetCardAdapter(globalNavWidgetListener)
        pillsAdapter.setItemList(globalNavWidgetItemList)

        return pillsAdapter
    }

    private fun createCardRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
    }

    private fun hideCardContent() {
        globalNavCardRecyclerView?.visibility = View.GONE
    }
}