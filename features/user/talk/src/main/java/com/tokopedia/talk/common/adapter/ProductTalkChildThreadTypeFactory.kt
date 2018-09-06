package com.tokopedia.talk.common.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.producttalk.view.viewmodel.LoadProductTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkItemViewModel

/**
 * @author by Steven
 */
interface ProductTalkChildThreadTypeFactory{

    fun type(loadMoreModel: LoadProductTalkViewModel):Int

    fun type(viewModel: ProductTalkItemViewModel):Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}