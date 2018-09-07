package com.tokopedia.talk.common.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.common.viewmodel.LoadMoreCommentTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkItemViewModel

/**
 * @author by Steven
 */
interface ProductTalkChildThreadTypeFactory{

    fun type(viewModel: ProductTalkItemViewModel):Int

    fun type(loadMoreCommentTalkViewModel: LoadMoreCommentTalkViewModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}