package com.tokopedia.recommendation_widget_common.widget.viewtoview

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class ViewToViewItemTypeFactoryImpl(
    private val listener: ViewToViewItemListener,
) : BaseAdapterTypeFactory(), ViewToViewItemTypeFactory {
    private var isUseBigLayout: Boolean = false

    override fun useBigLayout(useBigLayout: Boolean) {
        isUseBigLayout = useBigLayout
    }

    override fun type(viewToView: ViewToViewItemData): Int {
        return if(isUseBigLayout) {
            ViewToViewItemViewHolder.Big.LAYOUT
        } else ViewToViewItemViewHolder.Regular.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ViewToViewItemViewHolder.Big.LAYOUT -> {
                ViewToViewItemViewHolder.Big(parent, listener)
            }
            ViewToViewItemViewHolder.Regular.LAYOUT -> {
                ViewToViewItemViewHolder.Big(parent, listener)
            }
            else -> {
                super.createViewHolder(parent, type)
            }
        }
    }
}
