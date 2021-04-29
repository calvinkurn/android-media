package com.tokopedia.feedcomponent.view.adapter.posttag

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.posttag.CtaPostTagViewModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModel
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModelNew

/**
 * @author by yoasfs on 2019-07-18
 */

interface PostTagTypeFactory: AdapterTypeFactory {
    fun type(ctaPostTagViewModel: CtaPostTagViewModel): Int
    fun type(productPostTagViewModel: ProductPostTagViewModel): Int
    fun type(productPostTagViewModelNew: ProductPostTagViewModelNew):Int

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}