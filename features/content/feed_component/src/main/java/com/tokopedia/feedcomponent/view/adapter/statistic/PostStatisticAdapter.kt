package com.tokopedia.feedcomponent.view.adapter.statistic

import com.tokopedia.feedcomponent.helper.BaseDiffUtilAdapter
import com.tokopedia.feedcomponent.view.adapter.statistic.delegate.PostStatisticAdapterDelegate
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticUiModel

/**
 * Created by jegul on 2019-11-22
 */
class PostStatisticAdapter : BaseDiffUtilAdapter<PostStatisticUiModel>() {

    init {
        delegatesManager.addDelegate(PostStatisticAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: PostStatisticUiModel, newItem: PostStatisticUiModel): Boolean {
        return oldItem.iconRes == newItem.iconRes
    }

    override fun areContentsTheSame(oldItem: PostStatisticUiModel, newItem: PostStatisticUiModel): Boolean {
        return oldItem == newItem
    }
}