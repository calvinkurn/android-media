package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.product.detail.data.util.CenterLayoutManager
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.databinding.WidgetProductDetailNavigationBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig

class ProductDetailNavigation : FrameLayout, NavigationListener {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet) {
        init()
    }

    constructor(context: Context, attrSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrSet,
        defStyleAttr
    ) {
        init()
    }

    companion object {
        private const val REMOTE_CONFIG_KEY_ENABLE_BLOCKING_TOUCH =
            "android_enable_blocking_touch_pdp_navbar"

        /**
         * ProductDetailNavigation will be render at front of recyclerview (stack)
         * use this function to calculate first visible item position by passing offsetY param.
         *
         * usually the calculation start from 0 (top of recyclerview)
         * instead we start form offsetY
         */
        fun calculateFirstVisibleItemPosition(
            recyclerView: RecyclerView, offsetY: Int = Int.ZERO
        ): Int {
            val layoutManager = recyclerView.layoutManager
            if (layoutManager !is CenterLayoutManager) return -1

            if (offsetY <= 0) {
                return layoutManager.findFirstVisibleItemPositions(null).firstOrNull() ?: -1
            }

            val position = layoutManager.findFirstVisibleItemPositions(null).firstOrNull() ?: -1
            val someItem = layoutManager.findViewByPosition(position)
            val rectItem = Rect()
            someItem?.getGlobalVisibleRect(rectItem)
            val rectRv = Rect()
            recyclerView.getGlobalVisibleRect(rectRv)
            val rectRvTop = rectRv.top
            return if ((rectItem.bottom - rectRvTop) <= offsetY) {
                evaluateViewPosition(layoutManager, offsetY, rectRvTop, position + 1)
            } else position
        }

        private fun evaluateViewPosition(
            layoutManager: CenterLayoutManager,
            offsetY: Int,
            rectRvTop: Int,
            position: Int
        ): Int {
            val itemView = layoutManager.findViewByPosition(position) ?: return position - 1
            if (itemView.height == Int.ZERO) {
                return evaluateViewPosition(layoutManager, offsetY, rectRvTop, position + 1)
            }
            val rectItem = Rect()
            itemView.getGlobalVisibleRect(rectItem)
            return if (rectItem.bottom - rectRvTop <= offsetY) {
                evaluateViewPosition(layoutManager, offsetY, rectRvTop, position + 1)
            } else position
        }
    }

    private var binding : WidgetProductDetailNavigationBinding? = null

    private var navigationTab : NavigationTab? = null
    private var backToTop : BackToTopButton? = null

    private val remoteConfig = FirebaseRemoteConfigImpl(context)
    private val enableBlockingTouch = getEnableBlockingTouch(remoteConfig)

    private var listener: DynamicProductDetailListener? = null

    private fun init() {
        WidgetProductDetailNavigationBinding.inflate(LayoutInflater.from(context)).also {
            binding = it
            navigationTab = it.pdpNavigationTab
            backToTop = it.pdpBackToTop
            addView(it.root)
        }
    }

    fun start(
        recyclerView: RecyclerView,
        items: List<NavigationTab.Item>,
        listener: DynamicProductDetailListener,
        offsetY: Int = 0,
    ) {
        this.listener = listener

        val config = getConfiguration(offsetY, items.firstOrNull())
        navigationTab?.start(recyclerView, items, enableBlockingTouch, this, config)
        backToTop?.start(recyclerView, enableBlockingTouch, this, config)
    }

    fun stop(recyclerView: RecyclerView) {
        navigationTab?.stop(recyclerView)
        backToTop?.stop(recyclerView)
    }

    fun updateItemPosition() {
        navigationTab?.updateItemPosition()
    }

    fun disableNavigationTabAutoShowHide() {
        navigationTab?.disableAutoShowHide()
    }

    private fun getConfiguration(offsetY: Int, firstItem: NavigationTab.Item?): Configuration {
        return if (firstItem?.componentName == ProductDetailConstant.MEDIA) {
            Configuration.Navbar4(offsetY)
        } else Configuration.Default(offsetY)
    }

    private fun getEnableBlockingTouch(remoteConfig: RemoteConfig): Boolean {
        return remoteConfig.getBoolean(
            REMOTE_CONFIG_KEY_ENABLE_BLOCKING_TOUCH,
            true
        )
    }

    override fun onImpressionNavigationTab(labels: List<String>) {
        listener?.onImpressProductDetailNavigation(labels)
    }

    override fun onImpressionBackToTop(label: String) {
        listener?.onImpressBackToTop(label)
    }

    override fun onClickNavigationTab(position: Int, label: String) {
        listener?.onClickProductDetailnavigation(position + 1, label)
        backToTop?.onClickTab()
    }

    override fun onClickBackToTop(position: Int, label: String) {
        listener?.onClickProductDetailnavigation(position, label)
        navigationTab?.onClickBackToTop()
    }

    sealed class Configuration {
        abstract val offsetY: Int

        class Default(
            override val offsetY: Int
        ) : Configuration()

        class Navbar4(
            override val offsetY: Int
        ) : Configuration()
    }
}
