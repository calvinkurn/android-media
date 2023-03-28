package com.tokopedia.feedplus.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedPostImageViewHolder
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedLikeModel
import com.tokopedia.feedplus.presentation.util.FeedDiffUtilCallback

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedPostAdapter(typeFactory: FeedAdapterTypeFactory) :
    BaseAdapter<FeedAdapterTypeFactory>(typeFactory) {

    fun onToggleClearView() {
        notifyDataSetChanged()
    }

    fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(FeedDiffUtilCallback(visitables, newList))

        visitables.clear()
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateLikeUnlikeData(rowNumber: Int, like: FeedLikeModel) {
        if (visitables.size > rowNumber) {
            when (val item = visitables[rowNumber]) {
                is FeedCardImageContentModel -> {
                    visitables[rowNumber] = item.copy(like = like)
                }
                // TODO Furqan add for other type of models
            }
        }
        notifyItemChanged(
            rowNumber,
            FeedPostImageViewHolder.IMAGE_POST_LIKED_UNLIKED
        )
    }

    // TODO : Later to use DiffUtil

}
