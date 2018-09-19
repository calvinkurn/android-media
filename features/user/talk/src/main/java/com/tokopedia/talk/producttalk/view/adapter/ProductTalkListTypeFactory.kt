package com.tokopedia.talk.producttalk.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.producttalk.view.viewmodel.*

/**
 * @author by Steven
 */
interface ProductTalkListTypeFactory{

    fun type(viewModel: ProductTalkTitleViewModel):Int
    fun type(viewModel: TalkThreadViewModel):Int
    fun type(viewModel: LoadProductTalkThreadViewModel):Int
    fun type(emptyProductTalkViewModel: EmptyProductTalkViewModel): Int
    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}