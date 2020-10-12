package com.tokopedia.globalnavwidget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.globalnavwidget.GlobalNavWidgetConstant.GLOBAL_NAV_SPAN_COUNT
import com.tokopedia.globalnavwidget.GlobalNavWidgetConstant.NAV_TEMPLATE_PILL
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.global_nav_widget_list_layout.view.*
import kotlinx.android.synthetic.main.single_global_nav_layout.view.*

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
            hideSingleGlobalNavContainer()
            handleGlobalNavList(globalNavWidgetModel, globalNavWidgetListener)
        }
    }

    private fun hideGlobalNavListContainer() {
        globalNavContainerLayout?.visibility = View.GONE
    }

    private fun handleSingleGlobalNav(globalNavWidgetModel: GlobalNavWidgetModel, globalNavWidgetListener: GlobalNavWidgetListener) {
        setRandomBackground()

        val item = globalNavWidgetModel.itemList.firstOrNull() ?: return
        bindTitle(item)
        bindListener(globalNavWidgetModel, globalNavWidgetListener)

        if (globalNavWidgetModel.navTemplate == NAV_TEMPLATE_PILL) setPillContent(item)
        else setCardContent(item)
    }

    private fun setRandomBackground() {
        val backgroundIndex = intArrayOf(
                ContainerUnify.RED,
                ContainerUnify.GREEN,
                ContainerUnify.BLUE,
                ContainerUnify.YELLOW,
                ContainerUnify.GREY
        ).indices.shuffled().first()
        singleGlobalNavContainer?.setContainerColor(backgroundIndex)
    }

    private fun bindTitle(item: GlobalNavWidgetModel.Item) {
        singleGlobalNavTitle?.shouldShowWithAction(item.name.isNotEmpty()) {
            it.text = item.name
        }
    }

    private fun bindListener(globalNavWidgetModel: GlobalNavWidgetModel, globalNavWidgetListener: GlobalNavWidgetListener) {
        val item = globalNavWidgetModel.itemList.firstOrNull() ?: return
        singleGlobalNavCard?.setOnClickListener {
            globalNavWidgetListener.onClickItem(item)
        }
    }

    private fun setPillContent(item: GlobalNavWidgetModel.Item) {
        setPillTypography()
        bindIconWithLogoUrl(item)
        hideContentForPill()
    }

    private fun setPillTypography() {
        singleGlobalNavTitle?.setType(Typography.HEADING_5)
    }

    private fun bindIconWithLogoUrl(item: GlobalNavWidgetModel.Item) {
        singleGlobalNavIcon?.shouldShowWithAction(item.logoUrl.isNotEmpty()) {
            ImageHandler.loadImageFitCenter(context, it, item.logoUrl)
            singleGlobalNavImage?.visibility = View.GONE
        }
    }

    private fun hideContentForPill() {
        singleGlobalNavCategory?.visibility = View.GONE
        singleGlobalNavSubtitleInfoLayout?.visibility = View.GONE
    }

    private fun setCardContent(item: GlobalNavWidgetModel.Item) {
        setCardTypography()
        bindIconCard(item)
        bindCategory(item)
        bindSubtitleInfo(item)
    }

    private fun setCardTypography() {
        singleGlobalNavTitle?.setType(Typography.BODY_3)
        singleGlobalNavTitle?.setWeight(Typography.BOLD)
    }

    private fun bindIconCard(item: GlobalNavWidgetModel.Item) {
        if (item.imageUrl.isNotEmpty()) bindIconWithImageUrl(item)
        else bindIconWithLogoUrl(item)
    }

    private fun bindIconWithImageUrl(item: GlobalNavWidgetModel.Item) {
        singleGlobalNavImage?.shouldShowWithAction(item.imageUrl.isNotEmpty()) {
            ImageHandler.loadImageFitCenter(context, it, item.imageUrl)
            singleGlobalNavIcon?.visibility = View.GONE
        }
    }

    private fun bindCategory(item: GlobalNavWidgetModel.Item) {
        singleGlobalNavCategory?.shouldShowWithAction(item.categoryName.isNotEmpty()) {
            it.text = item.categoryName
        }
    }

    private fun bindSubtitleInfo(item: GlobalNavWidgetModel.Item) {
        if (item.subtitle.isNotEmpty() && item.info.isNotEmpty()) {
            bindSubtitle(item)
            bindInfo(item)
        } else hideSubtitleInfoLayout()
    }

    private fun bindInfo(item: GlobalNavWidgetModel.Item) {
        singleGlobalNavInfo?.shouldShowWithAction(item.info.isNotEmpty()) {
            it.text = item.info
        }
    }

    private fun bindSubtitle(item: GlobalNavWidgetModel.Item) {
        singleGlobalNavSubtitle?.shouldShowWithAction(item.subtitle.isNotEmpty()) {
            it.text = item.subtitle
        }
    }

    private fun hideSubtitleInfoLayout() {
        singleGlobalNavSubtitleInfoLayout?.visibility = View.GONE
    }

    private fun hideSingleGlobalNavContainer() {
        singleGlobalNavContainer?.visibility = View.GONE
    }

    private fun handleGlobalNavList(globalNavWidgetModel: GlobalNavWidgetModel, globalNavWidgetListener: GlobalNavWidgetListener) {
        setTitle(globalNavWidgetModel.title)
        setBackground(globalNavWidgetModel.background)
        setSeeAllButtonListener(globalNavWidgetModel, globalNavWidgetListener)
        setContent(globalNavWidgetModel, globalNavWidgetListener)
    }

    private fun setTitle(title: String) {
        globalNavTitle?.text = MethodChecker.fromHtml(title)
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
                intArrayOf(Color.WHITE, backgroundGradientColorList[backgroundIndex]))
        gd.cornerRadius = 0f

        globalNavContainerLayout?.background = gd
    }

    private fun setBackgroundFromModel(backgroundImgUrl: String) {
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
                context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16),
                context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8)
        )
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