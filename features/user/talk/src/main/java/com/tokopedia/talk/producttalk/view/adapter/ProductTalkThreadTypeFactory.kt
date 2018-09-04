package com.tokopedia.talk.producttalk.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkThreadViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkViewModel

/**
 * @author by Steven
 */
interface ProductTalkThreadTypeFactory{


    fun type(viewModel: ProductTalkViewModel): Int

    fun type(viewModel: TalkThreadViewModel):Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}