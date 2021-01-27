package com.tokopedia.globalnavwidget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.globalnavwidget.GlobalNavWidgetConstant.GLOBAL_NAV_SPAN_COUNT
import com.tokopedia.globalnavwidget.GlobalNavWidgetConstant.NAV_TEMPLATE_PILL
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.global_nav_widget_layout.view.*

class GlobalNavWidget: BaseCustomView {

    private val backgroundGradientColorList = intArrayOf(-0x51a, -0x1a0a01, -0x140011, -0x1511)

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
        if (globalNavWidgetModel.itemList.size == 1) {
            hideGlobalNavListContainer()
            handleSingleGlobalNav(globalNavWidgetModel, globalNavWidgetListener)
        }
        else {
            hideSingleGlobalNavCard()
            handleGlobalNav(globalNavWidgetModel, globalNavWidgetListener)
        }
    }

    private fun setBackground(backgroundImgUrl: String) {
        if (backgroundImgUrl.isEmpty()) {
            setBackgroundRandom()
        }
        else {
            setBackgroundFromModel(backgroundImgUrl)
        }
    }

    private fun setBackgroundRandom() {
        val backgroundIndex = (backgroundGradientColorList.indices).shuffled().first()

        val gd = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0), backgroundGradientColorList[backgroundIndex]))
        gd.cornerRadius = 0f

        globalNavContainerLayout?.background = gd
    }

    private fun setBackgroundFromModel(backgroundImgUrl: String) {
        globalNavContainerLayout?.let {
            ImageHandler.loadBackgroundImage(it, backgroundImgUrl)
        }
    }

    private fun hideGlobalNavListContainer() {
        globalNavTitleLayout?.visibility = View.GONE
        globalNavCardRecyclerView?.visibility = View.GONE
        globalNavPillRecyclerView?.visibility = View.GONE
    }

    private fun handleSingleGlobalNav(globalNavWidgetModel: GlobalNavWidgetModel, globalNavWidgetListener: GlobalNavWidgetListener) {
        val item = globalNavWidgetModel.itemList.firstOrNull() ?: return

        setBackgroundRandom()
        setSingleGlobalNavTitle(item)
        setSingleGlobalNavListener(globalNavWidgetModel, globalNavWidgetListener)

        if (globalNavWidgetModel.navTemplate == NAV_TEMPLATE_PILL) setSinglePillContent(item)
        else setSingleCardContent(item)
    }

    private fun setSingleGlobalNavTitle(item: GlobalNavWidgetModel.Item) {
        singleGlobalNavTitle?.shouldShowWithAction(item.name.isNotEmpty()) {
            it.text = item.name
        }
    }

    private fun setSingleGlobalNavListener(globalNavWidgetModel: GlobalNavWidgetModel, globalNavWidgetListener: GlobalNavWidgetListener) {
        val item = globalNavWidgetModel.itemList.firstOrNull() ?: return
        singleGlobalNavCard?.setOnClickListener {
            globalNavWidgetListener.onClickItem(item)
        }
    }

    private fun setSinglePillContent(item: GlobalNavWidgetModel.Item) {
        setSinglePillTypography()
        setSingleGlobalNavIconPill(item)
        hideContentForPill()
    }

    private fun setSinglePillTypography() {
        singleGlobalNavTitle?.setType(Typography.HEADING_5)
    }

    private fun setSingleGlobalNavIconPill(item: GlobalNavWidgetModel.Item) {
        if (item.logoUrl.isNotEmpty()) singleGlobalNavIcon?.setSingleGlobalNavIconImageWithUrl(item.logoUrl, singleGlobalNavImage)
        else singleGlobalNavIcon?.setSingleGlobalNavIconImageWithUrl(item.imageUrl, singleGlobalNavImage)
    }

    private fun AppCompatImageView.setSingleGlobalNavIconImageWithUrl(url: String, hiddenView: AppCompatImageView?) {
        shouldShowWithAction(url.isNotEmpty()) {
            ImageHandler.loadImageFitCenter(context, it, url)
            hiddenView?.visibility = View.GONE
        }
    }

    private fun hideContentForPill() {
        singleGlobalNavCategory?.visibility = View.GONE
        singleGlobalNavSubtitleInfoLayout?.visibility = View.GONE
    }

    private fun setSingleCardContent(item: GlobalNavWidgetModel.Item) {
        setSingleCardTypography()
        setSingleGlobalNavIconCard(item)
        setSingleGlobalNavCategory(item)
        setSingleGlobalNavSubtitleInfo(item)
    }

    private fun setSingleCardTypography() {
        singleGlobalNavTitle?.setType(Typography.BODY_3)
        singleGlobalNavTitle?.setWeight(Typography.BOLD)
    }

    private fun setSingleGlobalNavIconCard(item: GlobalNavWidgetModel.Item) {
        if (item.imageUrl.isNotEmpty()) singleGlobalNavImage?.setSingleGlobalNavIconImageWithUrl(item.imageUrl, singleGlobalNavIcon)
        else singleGlobalNavIcon?.setSingleGlobalNavIconImageWithUrl(item.logoUrl, singleGlobalNavImage)
    }

    private fun setSingleGlobalNavCategory(item: GlobalNavWidgetModel.Item) {
        singleGlobalNavCategory?.shouldShowWithAction(item.categoryName.isNotEmpty()) {
            it.text = item.categoryName
        }
    }

    private fun setSingleGlobalNavSubtitleInfo(item: GlobalNavWidgetModel.Item) {
        if (isShowSubtitleAndInfo(item)) showSingleGlobalNavSubtitleInfoLayout(item)
        else hideSubtitleInfoLayout()
    }

    private fun isShowSubtitleAndInfo(item: GlobalNavWidgetModel.Item): Boolean {
        return item.subtitle.isNotEmpty() && item.info.isNotEmpty()
    }

    private fun showSingleGlobalNavSubtitleInfoLayout(item: GlobalNavWidgetModel.Item) {
        setSingleGlobalNavSubtitle(item)
        setSingleGlobalNavInfo(item)
    }

    private fun setSingleGlobalNavSubtitle(item: GlobalNavWidgetModel.Item) {
        singleGlobalNavSubtitle?.shouldShowWithAction(item.subtitle.isNotEmpty()) {
            it.text = item.subtitle
        }
    }

    private fun setSingleGlobalNavInfo(item: GlobalNavWidgetModel.Item) {
        singleGlobalNavInfo?.shouldShowWithAction(item.info.isNotEmpty()) {
            it.text = item.info
        }
    }

    private fun hideSubtitleInfoLayout() {
        singleGlobalNavSubtitleInfoLayout?.visibility = View.GONE
    }

    private fun hideSingleGlobalNavCard() {
        singleGlobalNavCard?.visibility = View.GONE
    }

    private fun handleGlobalNav(globalNavWidgetModel: GlobalNavWidgetModel, globalNavWidgetListener: GlobalNavWidgetListener) {
        setBackground(globalNavWidgetModel.background)
        setGlobalNavTitle(globalNavWidgetModel.title)
        setSeeAllButtonListener(globalNavWidgetModel, globalNavWidgetListener)
        setGlobalNavContent(globalNavWidgetModel, globalNavWidgetListener)
    }

    private fun setGlobalNavTitle(title: String) {
        globalNavTitle?.text = MethodChecker.fromHtml(title)
    }

    private fun setSeeAllButtonListener(globalNavWidgetModel: GlobalNavWidgetModel, globalNavWidgetListener: GlobalNavWidgetListener) {
        val shouldShowSeeAllButton = globalNavWidgetModel.clickSeeAllApplink.isNotEmpty()

        globalNavSeeAllButton?.shouldShowWithAction(shouldShowSeeAllButton) { globalNavSeeAllButton ->
            globalNavSeeAllButton.setOnClickListener {
                globalNavWidgetListener.onClickSeeAll(globalNavWidgetModel)
            }
        }
    }

    private fun setGlobalNavContent(
            globalNavWidgetModel: GlobalNavWidgetModel,
            globalNavWidgetListener: GlobalNavWidgetListener
    ) {
        when(globalNavWidgetModel.navTemplate) {
            GlobalNavWidgetConstant.NAV_TEMPLATE_CARD -> handleGlobalNavCardTemplate(globalNavWidgetModel, globalNavWidgetListener)
            else -> handleGlobalNavDefaultTemplate(globalNavWidgetModel, globalNavWidgetListener)
        }
    }

    private fun handleGlobalNavCardTemplate(
            globalNavWidgetModel: GlobalNavWidgetModel,
            globalNavWidgetListener: GlobalNavWidgetListener
    ) {
        hideListPillContent()
        setListCardContent(globalNavWidgetModel.itemList, globalNavWidgetListener)
    }

    private fun hideListPillContent() {
        globalNavPillRecyclerView?.visibility = View.GONE
    }

    private fun setListCardContent(
            globalNavWidgetItemList: List<GlobalNavWidgetModel.Item>,
            globalNavWidgetListener: GlobalNavWidgetListener
    ) {
        globalNavCardRecyclerView?.visibility = View.VISIBLE
        globalNavCardRecyclerView?.adapter = createCardAdapter(globalNavWidgetItemList, globalNavWidgetListener)
        globalNavCardRecyclerView?.layoutManager = createCardRecyclerViewLayoutManager()

        if (globalNavCardRecyclerView?.itemDecorationCount == 0) {
            globalNavCardRecyclerView?.addItemDecoration(createGlobalNavWidgetCardItemDecoration())
        }
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

    private fun createGlobalNavWidgetCardItemDecoration(): RecyclerView.ItemDecoration {
        return GlobalNavWidgetCardItemDecoration(
                context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_12),
                context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
        )
    }

    private fun handleGlobalNavDefaultTemplate(
            globalNavWidgetModel: GlobalNavWidgetModel,
            globalNavWidgetListener: GlobalNavWidgetListener
    ) {
        hideListCardContent()
        setListPillContent(globalNavWidgetModel.itemList, globalNavWidgetListener)
    }

    private fun hideListCardContent() {
        globalNavCardRecyclerView?.visibility = View.GONE
    }

    private fun setListPillContent(
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