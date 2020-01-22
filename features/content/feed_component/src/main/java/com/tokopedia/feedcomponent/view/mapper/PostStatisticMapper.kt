package com.tokopedia.feedcomponent.view.mapper

import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.domain.model.statistic.FeedGetStatsPosts
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticDetailType
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticDetailUiModel
import com.tokopedia.kotlin.extensions.view.toCompactAmountString
import rx.functions.Func1

/**
 * Created by jegul on 2019-11-25
 */
class PostStatisticMapper(
        private val likeCount: Int,
        private val commentCount: Int
) : Func1<FeedGetStatsPosts, List<PostStatisticDetailUiModel>> {

    override fun call(t: FeedGetStatsPosts): List<PostStatisticDetailUiModel> = t.stats.firstOrNull().let {
        listOf(
                PostStatisticDetailUiModel(
                        PostStatisticDetailType.View,
                        R.drawable.ic_feed_see_darker_grey,
                        it?.view?.fmt.takeUnless(String?::isNullOrEmpty) ?: "0",
                        R.string.feed_post_statistic_seen_count
                ),
                PostStatisticDetailUiModel(
                        PostStatisticDetailType.Click,
                        R.drawable.ic_feed_click_darker_grey,
                        it?.click?.fmt.takeUnless(String?::isNullOrEmpty) ?: "0",
                        R.string.feed_post_statistic_click_count
                ),
                PostStatisticDetailUiModel(
                        PostStatisticDetailType.Like,
                        R.drawable.ic_thumb,
                        likeCount.toCompactAmountString(),
                        R.string.feed_post_statistic_like_count
                ),
                PostStatisticDetailUiModel(
                        PostStatisticDetailType.Comment,
                        R.drawable.ic_feed_comment,
                        commentCount.toCompactAmountString(),
                        R.string.feed_post_statistic_comment_count,
                        true
                )
        )
    }
}