package com.tokopedia.feedcomponent.view.adapter.statistic

import com.tokopedia.feedcomponent.helper.BaseDiffUtilAdapter
import com.tokopedia.feedcomponent.view.adapter.statistic.delegate.PostStatisticDetailAdapterDelegate
import com.tokopedia.feedcomponent.view.adapter.statistic.delegate.PostStatisticPlaceholderAdapterDelegate
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticDetailUiModel
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticPlaceholderUiModel
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticUiModel

/**
 * Created by jegul on 2019-11-22
 */
class PostStatisticAdapter : BaseDiffUtilAdapter<PostStatisticUiModel>() {

    init {
        delegatesManager
                .addDelegate(PostStatisticDetailAdapterDelegate())
                .addDelegate(PostStatisticPlaceholderAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: PostStatisticUiModel, newItem: PostStatisticUiModel): Boolean {
        return if (oldItem is PostStatisticDetailUiModel && newItem is PostStatisticDetailUiModel) oldItem.iconRes == newItem.iconRes
        else oldItem == PostStatisticPlaceholderUiModel && newItem == PostStatisticPlaceholderUiModel
    }

    override fun areContentsTheSame(oldItem: PostStatisticUiModel, newItem: PostStatisticUiModel): Boolean {
        return oldItem == newItem
    }
}