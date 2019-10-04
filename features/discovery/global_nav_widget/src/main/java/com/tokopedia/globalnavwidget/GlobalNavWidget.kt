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
        setSeeAllButtonListener(globalNavWidgetModel, globalNavWidgetListener)
        setContent(globalNavWidgetModel, globalNavWidgetListener)
    }

    private fun setTitle(title: String) {
        globalNavTitle?.text = MethodChecker.fromHtml(title)
    }

    private fun setBackground(backgroundImgUrl: String) {
        globalNavContainerLayout?.let {
            ImageHandler.loadBackgroundImage(it, backgroundImgUrl)
        }
    }

    private fun setSeeAllButtonListener(globalNavWidgetModel: GlobalNavWidgetModel, globalNavWidgetListener: GlobalNavWidgetListener) {
        val shouldShowSeeAllButton = globalNavWidgetModel.clickSeeAllApplink.isNotEmpty()

        globalNavSeeAllButton?.shouldShowWithAction(shouldShowSeeAllButton) { globalNavSeeAllButton ->
            globalNavSeeAllButton.setOnClickListener {
                globalNavWidgetListener.onClickSeeAll(globalNavWidgetModel)
            }
        }
    }

    private fun setContent(
            globalNavWidgetModel: GlobalNavWidgetModel,
            globalNavWidgetListener: GlobalNavWidgetListener
    ) {
        when(globalNavWidgetModel.navTemplate) {
            GlobalNavWidgetConstant.NAV_TEMPLATE_CARD -> handleCardTemplate(globalNavWidgetModel, globalNavWidgetListener)
            else -> handleDefaultTemplate(globalNavWidgetModel, globalNavWidgetListener)
        }
    }

    private fun handleCardTemplate(
            globalNavWidgetModel: GlobalNavWidgetModel,
            globalNavWidgetListener: GlobalNavWidgetListener
    ) {
        hidePillContent()
        setCardContent(globalNavWidgetModel.itemList, globalNavWidgetListener)
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

    private fun handleDefaultTemplate(
            globalNavWidgetModel: GlobalNavWidgetModel,
            globalNavWidgetListener: GlobalNavWidgetListener
    ) {
        hideCardContent()
        setPillContent(globalNavWidgetModel.itemList, globalNavWidgetListener)
    }

    private fun hideCardContent() {
        globalNavCardRecyclerView?.visibility = View.GONE
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
}