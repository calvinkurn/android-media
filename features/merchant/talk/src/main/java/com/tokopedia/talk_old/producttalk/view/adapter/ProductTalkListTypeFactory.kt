package com.tokopedia.talk_old.producttalk.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk_old.producttalk.view.data.ChatBannerUiModel
import com.tokopedia.talk_old.producttalk.view.viewmodel.*

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