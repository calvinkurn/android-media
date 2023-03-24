package com.tokopedia.feedplus.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_CLEAR_MODE
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions.FEED_POST_LIKED_UNLIKED
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedLikeModel
import com.tokopedia.feedplus.presentation.util.FeedDiffUtilCallback

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedPostAdapter(typeFactory: FeedAdapterTypeFactory) :
    BaseAdapter<FeedAdapterTypeFactory>(typeFactory) {

    fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(FeedDiffUtilCallback(visitables, newList))

        visitables.clear()
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateLikeUnlikeData(rowNumber: Int, like: FeedLikeModel) {
        if ((list?.size ?: 0) > rowNumber) {
            when (val item = list[rowNumber]) {
                is FeedCardImageContentModel -> {
                    list[rowNumber] = item.copy(like = like)
                }
                // TODO Furqan add for other type of models
            }
        }
        notifyItemChanged(
            rowNumber,
            FEED_POST_LIKED_UNLIKED
        )
    }

    fun showClearView(position: Int) {
        if ((list?.size ?: 0) > position) {
            notifyItemChanged(position, FEED_POST_CLEAR_MODE)
        }
    }

    // TODO : Later to use DiffUtil
}
