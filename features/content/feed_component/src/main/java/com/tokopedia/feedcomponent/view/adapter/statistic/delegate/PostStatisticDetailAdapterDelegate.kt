package com.tokopedia.feedcomponent.view.adapter.statistic.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapter_delegate.TypedAdapterDelegate
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.statistic.PostStatisticViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticDetailType
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticDetailUiModel
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticUiModel

/**
 * Created by jegul on 2019-11-22
 */
class PostStatisticDetailAdapterDelegate(private val onSeeMoreDetail: (PostStatisticDetailType) -> Unit) : TypedAdapterDelegate<PostStatisticDetailUiModel, PostStatisticUiModel, PostStatisticViewHolder>(R.layout.item_post_statistic) {

    override fun onBindViewHolder(item: PostStatisticDetailUiModel, holder: PostStatisticViewHolder) {
       holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PostStatisticViewHolder {
        return PostStatisticViewHolder(basicView, onSeeMoreDetail)
    }
}