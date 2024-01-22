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
import com.tokopedia.shop_widget.databinding.LayoutBmsmCustomViewBinding
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

    private var onSuccessAtc: (AddToCartDataModel) -> Unit = {}
    private var onErrorAtc: (String) -> Unit = {}
    private var onNavigateToOlp: (String) -> Unit = {}
    private var onProductClicked: (Product) -> Unit = {}
    private var onWidgetVisible: () -> Unit = {}
    private var tabTotalWidth = 0

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
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ) {
        setupTabs(provider, offerList, isOverrideTheme, colorSchema)
    }

    private fun setupTabs(
        provider: BmsmWidgetDependencyProvider,
        offerList: List<OfferingInfoByShopIdUiModel>,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ) {
        binding?.apply {
            val fragments = createFragments(offerList, isOverrideTheme, colorSchema)
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
                    tab.select(
                        isOverrideTheme,
                        colorSchema)
                }  else {
                    tab.unselect(
                        isOverrideTheme,
                        colorSchema
                    )
                }

                tab.view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
                val tabWidth =
                    (tab.view.measuredWidth + MARGIN_16_DP.dpToPx() + MARGIN_16_DP.dpToPx()).toInt()
                tabTotalWidth += tabWidth
            }
            handleTabChange(tabBmsmWidget, isOverrideTheme, colorSchema)
            applyTabRuleWidth(offerList, tabBmsmWidget, isOverrideTheme)
        }
    }

    private fun handleTabChange(
        tabsUnify: TabsUnify,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ) {
        tabsUnify.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.select(isOverrideTheme, colorSchema)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab.unselect(isOverrideTheme, colorSchema)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

    private fun applyTabRuleWidth(
        tabs: List<OfferingInfoByShopIdUiModel>,
        tabsUnify: TabsUnify,
        isOverrideTheme: Boolean
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

    private fun TabLayout.Tab?.select(isOverrideTheme: Boolean, colorSchema: ShopPageColorSchema) {
        val tabTitle = this?.customView?.findViewById<Typography>(R.id.tpgTabTitle)
        val textColor = if (isOverrideTheme && colorSchema.listColorSchema.isNotEmpty()) {
            colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        } else {
            ContextCompat.getColor(context, R.color.dms_static_black)
        }
        tabTitle?.apply {
            typeface = Typography.getFontType(context, true, Typography.DISPLAY_3)
            setTextColor(textColor)
            invalidate()
        }
    }

    private fun TabLayout.Tab?.unselect(isOverrideTheme: Boolean, colorSchema: ShopPageColorSchema) {
        val tabTitle = this?.customView?.findViewById<Typography>(R.id.tpgTabTitle)
        val textColor = if (isOverrideTheme && colorSchema.listColorSchema.isNotEmpty())  {
            colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.DISABLED_TEXT_COLOR)
        } else {
            ContextCompat.getColor(context, R.color.dms_static_black)
        }

        tabTitle?.apply {
            typeface = Typography.getFontType(context, false, Typography.DISPLAY_3)
            setTextColor(textColor)
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
        offerList: List<OfferingInfoByShopIdUiModel>,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ): List<Pair<String, Fragment>> {
        val pages = mutableListOf<Pair<String, Fragment>>()

        offerList.forEachIndexed { _, currentTab ->
            val fragment = BmsmWidgetTabFragment.newInstance(
                data = currentTab,
                isOverrideTheme = isOverrideTheme,
                offerTypeId = offerType,
                colorSchema
            )

            fragment.apply {
                setOnSuccessAtcListener {
                    onSuccessAtc.invoke(it)
                }
                setOnErrorAtcListener {
                    onErrorAtc.invoke(it)
                }
                setOnNavigateToOlpListener {
                    onNavigateToOlp.invoke(it)
                }
                setOnProductCardClicked {
                    onProductClicked.invoke(it)
                }
                setOnWidgetVisible {
                    onWidgetVisible.invoke()
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

    fun setOnSuccessAtcListener(onSuccessAtc: (AddToCartDataModel) -> Unit) {
        this.onSuccessAtc = onSuccessAtc
    }

    fun setOnErrorAtcListener(onErrorAtc: (String) -> Unit) {
        this.onErrorAtc = onErrorAtc
    }

    fun setOnNavigateToOlpListener(onNavigateToOlp: (String) -> Unit) {
        this.onNavigateToOlp = onNavigateToOlp
    }

    fun setOnProductCardClicked(onProductClicked: (Product) -> Unit) {
        this.onProductClicked = onProductClicked
    }

    fun setOnWidgetVisible(onWidgetVisible: () -> Unit) {
        this.onWidgetVisible = onWidgetVisible
    }
}
