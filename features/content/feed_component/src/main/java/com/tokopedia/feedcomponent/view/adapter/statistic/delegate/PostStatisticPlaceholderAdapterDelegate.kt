package com.tokopedia.feedcomponent.view.adapter.statistic.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.helper.TypedAdapterDelegate
import com.tokopedia.feedcomponent.view.adapter.viewholder.statistic.PostStatisticViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticPlaceholderUiModel
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticUiModel

/**
 * Created by jegul on 2019-11-25
 */
class PostStatisticPlaceholderAdapterDelegate : TypedAdapterDelegate<PostStatisticPlaceholderUiModel, PostStatisticUiModel, PostStatisticViewHolder>(R.layout.item_post_statistic) {

    override fun onBindViewHolder(item: PostStatisticPlaceholderUiModel, holder: PostStatisticViewHolder) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PostStatisticViewHolder {
        return PostStatisticViewHolder(basicView)
    }
}