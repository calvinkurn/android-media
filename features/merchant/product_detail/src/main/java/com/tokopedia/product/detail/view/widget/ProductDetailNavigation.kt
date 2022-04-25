package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.databinding.WidgetProductDetailNavigationBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class ProductDetailNavigation(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet), NavigationListener {
    private val binding = WidgetProductDetailNavigationBinding.inflate(LayoutInflater.from(context))
    private val view = binding.root

    private val navigationTab = binding.pdpNavigationTab
    private val backToTop = binding.pdpBackToTop

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
        navigationTab.start(recyclerView, items, this)
        backToTop.start(recyclerView, this)
    }

    fun stop(recyclerView: RecyclerView) {
        navigationTab.stop(recyclerView)
        backToTop.stop(recyclerView)
    }

    fun updateItemPosition() {
        navigationTab.updateItemPosition()
    }

    override fun onImpressionNavigationTab(labels: List<String>) {
        listener?.onImpressProductDetailNavigation(labels)
    }

    override fun onImpressionBackToTop(label: String) {
        listener?.onImpressBackToTop(label)
    }

    override fun onClickNavigationTab(position: Int, label: String) {
        listener?.onClickProductDetailnavigation(position, label)
        backToTop.onClickTab()
    }

    override fun onClickBackToTop(position: Int, label: String) {
        listener?.onClickProductDetailnavigation(position, label)
        navigationTab.onClickBackToTop()
    }
}