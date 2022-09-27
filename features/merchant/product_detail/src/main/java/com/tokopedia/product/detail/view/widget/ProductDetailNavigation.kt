package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.detail.data.util.CenterLayoutManager
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.databinding.WidgetProductDetailNavigationBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig

class ProductDetailNavigation(
    context: Context, attributeSet: AttributeSet
) : FrameLayout(context, attributeSet), NavigationListener {

    companion object {
        const val NAVIGATION_THRESHOLD_MEDIA_PERCENTAGE = 0.75
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
            return if ((rectItem.bottom - rectRv.top) <= offsetY) {
                checkViewHeight(layoutManager, position + 1)
            } else position
        }

        private fun checkViewHeight(layoutManager: CenterLayoutManager, position: Int): Int {
            val itemView = layoutManager.findViewByPosition(position)
            val height = itemView?.height.orZero()
            return if (height == 0) {
                checkViewHeight(layoutManager, position + 1)
            } else position
        }
    }

    private val binding = WidgetProductDetailNavigationBinding.inflate(LayoutInflater.from(context))
    private val view = binding.root

    private val navigationTab = binding.pdpNavigationTab
    private val backToTop = binding.pdpBackToTop

    private val remoteConfig = FirebaseRemoteConfigImpl(context)
    private val enableBlockingTouch = getEnableBlockingTouch(remoteConfig)

    private var listener: DynamicProductDetailListener? = null

    init {
        addView(view)
    }

    fun start(
        recyclerView: RecyclerView,
        items: List<NavigationTab.Item>,
        listener: DynamicProductDetailListener,
        offsetY: Int = 0,
    ) {
        this.listener = listener

        val config = getConfiguration(offsetY, items.firstOrNull())
        navigationTab.start(recyclerView, items, enableBlockingTouch, this, config)
        backToTop.start(recyclerView, enableBlockingTouch, this, config)
    }

    fun stop(recyclerView: RecyclerView) {
        navigationTab.stop(recyclerView)
        backToTop.stop(recyclerView)
    }

    fun updateItemPosition() {
        navigationTab.updateItemPosition()
    }

    private fun getConfiguration(offsetY: Int, firstItem: NavigationTab.Item?): Configuration {
        return if (firstItem?.componentName == ProductDetailConstant.MEDIA) {
            Configuration.Navbar4(offsetY)
        } else Configuration.Default(offsetY)
    }

    private fun getEnableBlockingTouch(remoteConfig: RemoteConfig): Boolean {
        return remoteConfig.getBoolean(
            REMOTE_CONFIG_KEY_ENABLE_BLOCKING_TOUCH, true
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
        backToTop.onClickTab()
    }

    override fun onClickBackToTop(position: Int, label: String) {
        listener?.onClickProductDetailnavigation(position, label)
        navigationTab.onClickBackToTop()
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
