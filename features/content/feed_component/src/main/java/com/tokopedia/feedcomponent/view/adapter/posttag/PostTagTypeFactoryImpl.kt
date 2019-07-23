package com.tokopedia.feedcomponent.view.adapter.posttag

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.posttag.CtaPostTagViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.posttag.ProductPostTagViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.posttag.CtaPostTagViewModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModel

/**
 * @author by yoasfs on 2019-07-18
 */
class PostTagTypeFactoryImpl(val listener: DynamicPostViewHolder.DynamicPostListener)
    : BaseAdapterTypeFactory(), PostTagTypeFactory {

    override fun type(productPostTagViewModel: ProductPostTagViewModel): Int {
        return ProductPostTagViewHolder.LAYOUT
    }

    override fun type(ctaPostTagViewModel: CtaPostTagViewModel): Int {
        return CtaPostTagViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        if (type == ProductPostTagViewHolder.LAYOUT) {
            viewHolder = ProductPostTagViewHolder(parent, listener)
        } else if (type == CtaPostTagViewHolder.LAYOUT) {
            viewHolder = CtaPostTagViewHolder(parent, listener)
        } else {
            viewHolder = super.createViewHolder(parent, type)
        }
        return viewHolder
    }
}