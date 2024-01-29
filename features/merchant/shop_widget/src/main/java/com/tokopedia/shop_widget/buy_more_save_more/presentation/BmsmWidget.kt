package com.tokopedia.shop_widget.buy_more_save_more.presentation

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingInfoByShopIdUiModel
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel.Product
import com.tokopedia.shop_widget.buy_more_save_more.presentation.fragment.BmsmWidgetTabFragment
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetDependencyProvider
import com.tokopedia.shop_widget.buy_more_save_more.util.BmsmWidgetColorThemeConfig
import com.tokopedia.shop_widget.buy_more_save_more.util.ColorType
import com.tokopedia.shop_widget.databinding.LayoutBmsmCustomViewBinding
import com.tokopedia.unifycomponents.R.*
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.Typography
import timber.log.Timber

class BmsmWidget : ConstraintLayout {

    var offerType: Int = 1
        set(value) {
            field = value
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initWithAttr(context, attributeSet)
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        initWithAttr(context, attributeSet)
    }

    private var binding: LayoutBmsmCustomViewBinding? = null

    private var onSuccessAtc: (String, String, AddToCartDataModel) -> Unit = { _, _, _ -> }
    private var onErrorAtc: (String) -> Unit = {}
    private var onNavigateToOlp: (String, String, String) -> Unit = {_, _, _ ->  }
    private var onProductClicked: (String, String, Product) -> Unit = {_, _, _ ->  }
    private var onWidgetVisible: (String) -> Unit = {}
    private var onTabSelected: (OfferingInfoByShopIdUiModel) -> Unit = {}
    private var tabTotalWidth = 0
    private var colorSchema: ShopPageColorSchema = ShopPageColorSchema()
    private var colorThemeConfiguration: BmsmWidgetColorThemeConfig = BmsmWidgetColorThemeConfig.DEFAULT
    private var patternColorType: ColorType = ColorType.LIGHT

    companion object {
        private const val ONE_TAB = 1
        private const val MARGIN_16_DP = 16f
    }

    init {
        binding = LayoutBmsmCustomViewBinding.inflate(LayoutInflater.from(context), this, true)
        disableTabSwipeBehavior()
    }

    private fun initWithAttr(context: Context, attributeSet: AttributeSet) {
        val attributeArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.BmsmWidget)
        try {
            offerType = attributeArray.getInt(R.styleable.BmsmWidget_offer_type, 1)
        } catch (t: Throwable) {
            Timber.d(t.localizedMessage)
        } finally {
            attributeArray.recycle()
        }
    }

    fun setupWidget(
        provider: BmsmWidgetDependencyProvider,
        offerList: List<OfferingInfoByShopIdUiModel>,
        colorSchema: ShopPageColorSchema,
        colorThemeConfiguration: BmsmWidgetColorThemeConfig,
        patternColorType: ColorType
    ) {
        this.colorSchema = colorSchema
        this.patternColorType = patternColorType
        this.colorThemeConfiguration = colorThemeConfiguration
        setupTabs(provider, offerList)
    }

    private fun setupTabs(
        provider: BmsmWidgetDependencyProvider,
        offerList: List<OfferingInfoByShopIdUiModel>
    ) {
        binding?.apply {
            val fragments = createFragments(offerList)
            val pagerAdapter = TabPagerAdapter(
                provider.bmsmWidgetHostFragmentManager,
                provider.bmsmWidgetHostLifecycle,
                fragments
            )
            vpBmsmWidget.adapter = pagerAdapter
            tabBmsmWidget.apply {
                tabLayout.apply {
                    isTabIndicatorFullWidth = false
                    setBackgroundColor(Color.TRANSPARENT)
                    val centeredTabIndicator = ContextCompat.getDrawable(
                        context,
                        R.drawable.bmsm_tab_indicator_underline
                    )
                    setSelectedTabIndicator(centeredTabIndicator)
                    setPadding(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO)
                    setSelectedTabIndicatorHeight(4)
                }
                whiteShadeLeft.gone()
                whiteShadeRight.gone()
            }
            TabsUnifyMediator(tabBmsmWidget, vpBmsmWidget) { tab, currentPosition ->
                val tabView = LayoutInflater.from(tabBmsmWidget.context)
                    .inflate(R.layout.item_viewpager_bmsm_navigation_tab, tabBmsmWidget, false)
                tab.customView = tabView

                val tabTitle: Typography? = tabView.findViewById(R.id.tpgTabTitle)
                tabTitle?.text = fragments[currentPosition].first

                if (currentPosition == Int.ZERO) {
                    tab.selectTab()
                }  else {
                    tab.unselect()
                }

                tab.view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
                val tabWidth =
                    (tab.view.measuredWidth + MARGIN_16_DP.dpToPx() + MARGIN_16_DP.dpToPx()).toInt()
                tabTotalWidth += tabWidth
                handleTabChange(tabBmsmWidget, offerList[currentPosition])
                applyTabRuleWidth(offerList, tabBmsmWidget)
            }
        }
    }

    private fun handleTabChange(
        tabsUnify: TabsUnify,
        offering: OfferingInfoByShopIdUiModel
    ) {
        tabsUnify.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                onTabSelected.invoke(offering)
                tab?.selectTab()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab.unselect()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

    private fun applyTabRuleWidth(
        tabs: List<OfferingInfoByShopIdUiModel>,
        tabsUnify: TabsUnify
    ) {
        tabsUnify.post {
            when {
                tabs.isEmpty() -> tabsUnify.gone()
                tabs.size == ONE_TAB -> tabsUnify.gone()
                else -> {
                    tabsUnify.visible()
                    val screenWidth =
                        DeviceScreenInfo.getScreenWidth(tabsUnify.context) - MARGIN_16_DP.dpToPx() - MARGIN_16_DP.dpToPx()
                    if (tabTotalWidth < screenWidth) {
                        tabsUnify.customTabMode = TabLayout.MODE_FIXED
                        tabsUnify.customTabGravity = TabLayout.GRAVITY_FILL
                    } else {
                        tabsUnify.customTabMode = TabLayout.MODE_SCROLLABLE
                        tabsUnify.customTabGravity = TabLayout.GRAVITY_FILL
                    }
                }
            }
        }
    }

    private fun TabLayout.Tab?.selectTab() {
        val tabTitle = this?.customView?.findViewById<Typography>(R.id.tpgTabTitle)
        tabTitle?.apply {
            typeface = Typography.getFontType(context, true, Typography.DISPLAY_3)
            setTextColor(getActiveTabTextColor())
            invalidate()
        }
    }

    private fun TabLayout.Tab?.unselect() {
        val tabTitle = this?.customView?.findViewById<Typography>(R.id.tpgTabTitle)
        tabTitle?.apply {
            typeface = Typography.getFontType(context, false, Typography.DISPLAY_3)
            setTextColor(getInActiveTabTextColor())
            invalidate()
        }
    }

    private class TabPagerAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        private val fragments: List<Pair<String, Fragment>>
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position].second
    }

    private fun createFragments(
        offerList: List<OfferingInfoByShopIdUiModel>
    ): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()

        offerList.forEachIndexed { _, currentTab ->
            val fragment = BmsmWidgetTabFragment.newInstance(
                data = currentTab,
                offerTypeId = offerType,
                colorThemeConfiguration = colorThemeConfiguration,
                patternColorType = patternColorType
            )

            fragment.apply {
                setOnSuccessAtcListener { offerId, offerType, product ->
                    onSuccessAtc.invoke(
                        offerId,
                        offerType,
                        product
                    )
                }
                setOnErrorAtcListener {
                    onErrorAtc.invoke(it)
                }
                setOnNavigateToOlpListener { offerId, offerType, product ->
                    onNavigateToOlp.invoke(
                        offerId,
                        offerType,
                        product
                    )
                }
                setOnProductCardClicked { offerId, offerType, product ->
                    onProductClicked.invoke(
                        offerId,
                        offerType,
                        product
                    )
                }
                setOnWidgetVisible { offerId ->
                    onWidgetVisible.invoke(offerId)
                }
            }

            val displayedTabName = currentTab.offerName
            pages.add(Pair(displayedTabName, fragment))
        }
        return pages
    }

    private fun disableTabSwipeBehavior() {
        binding?.vpBmsmWidget?.isUserInputEnabled = false
    }

    fun setOnSuccessAtcListener(onSuccessAtc: (String, String, AddToCartDataModel) -> Unit) {
        this.onSuccessAtc = onSuccessAtc
    }

    fun setOnErrorAtcListener(onErrorAtc: (String) -> Unit) {
        this.onErrorAtc = onErrorAtc
    }

    fun setOnNavigateToOlpListener(onNavigateToOlp: (String, String, String) -> Unit) {
        this.onNavigateToOlp = onNavigateToOlp
    }

    fun setOnProductCardClicked(onProductClicked: (String, String, Product) -> Unit) {
        this.onProductClicked = onProductClicked
    }

    fun setOnWidgetVisible(onWidgetVisible: (String) -> Unit) {
        this.onWidgetVisible = onWidgetVisible
    }

    fun setOnTabSelected(onTabSelected: (OfferingInfoByShopIdUiModel) -> Unit) {
        this.onTabSelected = onTabSelected
    }

    private fun getActiveTabTextColor(): Int{
        val textColor = when (colorThemeConfiguration) {
            BmsmWidgetColorThemeConfig.FESTIVITY -> ContextCompat.getColor(context, R.color.dms_static_white)
            BmsmWidgetColorThemeConfig.REIMAGINE -> {
                if (colorSchema.listColorSchema.isNotEmpty()) {
                    colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
                } else {
                    ContextCompat.getColor(context, color.Unify_NN950)
                }
            }
            BmsmWidgetColorThemeConfig.DEFAULT -> ContextCompat.getColor(context, R.color.dms_static_black)
        }
        return textColor
    }

    private fun getInActiveTabTextColor(): Int{
        val textColor = when (colorThemeConfiguration) {
            BmsmWidgetColorThemeConfig.FESTIVITY -> ContextCompat.getColor(context, R.color.dms_static_white)
            BmsmWidgetColorThemeConfig.REIMAGINE -> {
                if (colorSchema.listColorSchema.isNotEmpty()) {
                    colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS)
                } else {
                    ContextCompat.getColor(context, color.Unify_NN950)
                }
            }
            BmsmWidgetColorThemeConfig.DEFAULT -> ContextCompat.getColor(context, R.color.dms_static_black)
        }
        return textColor
    }
}
