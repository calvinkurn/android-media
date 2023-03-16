package com.tokopedia.feedcomponent.view.adapter.posttag

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.posttag.CtaPostTagModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew

/**
 * @author by yoasfs on 2019-07-18
 */

interface PostTagTypeFactory: AdapterTypeFactory {
    fun type(ctaPostTagViewModel: CtaPostTagModel): Int
    fun type(productPostTagViewModel: ProductPostTagModel): Int
    fun type(productPostTagViewModelNew: ProductPostTagModelNew):Int

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}
