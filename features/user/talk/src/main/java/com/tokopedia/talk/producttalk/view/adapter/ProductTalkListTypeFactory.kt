package com.tokopedia.talk.producttalk.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.producttalk.view.data.ChatBannerUiModel
import com.tokopedia.talk.producttalk.view.viewmodel.EmptyProductTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.LoadProductTalkThreadViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkTitleViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkThreadViewModel

/**
 * @author by Steven
 */
interface ProductTalkListTypeFactory{

    fun type(viewModel: ProductTalkTitleViewModel):Int
    fun type(viewModel: TalkThreadViewModel):Int
    fun type(viewModel: LoadProductTalkThreadViewModel):Int
    fun type(emptyProductTalkViewModel: EmptyProductTalkViewModel): Int
    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
    fun type(chatBannerUiModel: ChatBannerUiModel): Int

}