package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productbundlewidget.listener.ProductBundleWidgetListener
import com.tokopedia.productbundlewidget.model.GetBundleParamBuilder
import com.tokopedia.productbundlewidget.model.WidgetType
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowBundleUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowBundleWidgetBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowBundleWidgetViewHolder(
    itemView: View,
    private val productBundleWidgetListener: ProductBundleWidgetListener? = null,
    private val tokoNowBundleWidgetListener: TokoNowBundleWidgetListener? = null
) : AbstractViewHolder<TokoNowBundleUiModel>(itemView) {

    companion object {
        private const val PAGE_SOURCE = "now_homepage"

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_bundle_widget
    }

    private var binding: ItemTokopedianowBundleWidgetBinding? by viewBinding()

    override fun bind(element: TokoNowBundleUiModel) {
        binding?.apply {
            root.addOnImpressionListener(element) {
                if (element.bundleIds.isNotEmpty()) {
                    tokoNowBundleWidgetListener?.setWidgetId(element.id)

                    val param = GetBundleParamBuilder()
                        .setWidgetType(WidgetType.TYPE_1)
                        .setBundleId(element.bundleIds)
                        .setPageSource(PAGE_SOURCE)
                        .build()

                    root.getBundleData(param)
                    root.setTitleText(element.title)

                    if (productBundleWidgetListener != null) {
                        root.setListener(productBundleWidgetListener)
                    }
                }
            }
        }
    }

    interface TokoNowBundleWidgetListener {
        fun setWidgetId(id: String)
    }
}
