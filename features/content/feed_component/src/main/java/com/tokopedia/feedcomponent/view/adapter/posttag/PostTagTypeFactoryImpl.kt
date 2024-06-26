package com.tokopedia.feedcomponent.view.adapter.posttag

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.posttag.CtaPostTagViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.posttag.ProductPostTagViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.posttag.ProductPostTagViewHolderNew
import com.tokopedia.feedcomponent.view.viewmodel.posttag.CtaPostTagModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew

/**
 * @author by yoasfs on 2019-07-18
 */
class PostTagTypeFactoryImpl(val listener: DynamicPostViewHolder.DynamicPostListener, val screenWidth: Int)
    : BaseAdapterTypeFactory(), PostTagTypeFactory {

    override fun type(productPostTagViewModel: ProductPostTagModel): Int {
        return ProductPostTagViewHolder.LAYOUT
    }

    override fun type(productPostTagViewModelNew: ProductPostTagModelNew): Int {
        return ProductPostTagViewHolderNew.LAYOUT
    }

    override fun type(ctaPostTagViewModel: CtaPostTagModel): Int {
        return CtaPostTagViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProductPostTagViewHolder.LAYOUT -> {
                ProductPostTagViewHolder(parent, listener, screenWidth)
            }
            CtaPostTagViewHolder.LAYOUT -> {
                CtaPostTagViewHolder(parent, listener)
            }
            else -> {
                super.createViewHolder(parent, type)
            }
        }
    }
}
