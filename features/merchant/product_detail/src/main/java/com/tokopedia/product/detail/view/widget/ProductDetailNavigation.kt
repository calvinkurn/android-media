package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.databinding.WidgetProductDetailNavigationBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig

class ProductDetailNavigation(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet), NavigationListener {

    companion object {
        private const val REMOTE_CONFIG_KEY_ENABLE_BLOCKING_TOUCH =
            "android_enable_blocking_touch_pdp_navbar"
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
        listener: DynamicProductDetailListener
    ) {
        this.listener = listener
        navigationTab.start(recyclerView, items, enableBlockingTouch, this)
        backToTop.start(recyclerView, enableBlockingTouch, this)
    }

    fun stop(recyclerView: RecyclerView) {
        navigationTab.stop(recyclerView)
        backToTop.stop(recyclerView)
    }

    fun updateItemPosition() {
        navigationTab.updateItemPosition()
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
        backToTop.onClickTab()
    }

    override fun onClickBackToTop(position: Int, label: String) {
        listener?.onClickProductDetailnavigation(position, label)
        navigationTab.onClickBackToTop()
    }
}