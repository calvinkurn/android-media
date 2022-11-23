package com.tokopedia.people.views.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.people.databinding.UpItemUserFeedBinding
import com.tokopedia.people.views.adapter.UserFeedPostsBaseAdapter
import com.tokopedia.people.views.uimodel.content.PostUiModel

/**
 * created by fachrizalmrsln on 22/11/22
 **/
class FeedPostsViewHolder(
    private val view: UpItemUserFeedBinding,
    private val callback: UserFeedPostsBaseAdapter.FeedPostsCallback,
) :
    RecyclerView.ViewHolder(view.root) {

    fun bindView(item: PostUiModel?) {
        if (item == null || item.media.isEmpty()) return
        val firstItem = item.media.first()

        callback.onImpressFeedPostData(item, bindingAdapterPosition)
        view.imageContent.apply {
            cornerRadius = 0
            setImageUrl(item.media.first().coverURL)
            setOnClickListener {
                callback.onFeedPostsClick(
                    item.appLink,
                    item.id,
                    firstItem.coverURL,
                    bindingAdapterPosition,
                    firstItem.type,
                )
            }
        }
        when (firstItem.type) {
            MEDIA_TYPE_VIDEO -> {
                view.iconType.visible()
                view.iconType.setImage(IconUnify.PLAY)
            }
            MEDIA_TYPE_IMAGE -> {
                view.iconType.showWithCondition(item.media.size > 1)
                view.iconType.setImage(IconUnify.SELECT_MULTIPLE)
            }
            else -> view.iconType.gone()
        }
    }


    companion object {
        private const val MEDIA_TYPE_IMAGE = "image"
        private const val MEDIA_TYPE_VIDEO = "video"

        fun create(parent: ViewGroup, callback: UserFeedPostsBaseAdapter.FeedPostsCallback) =
            FeedPostsViewHolder(
                UpItemUserFeedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                callback,
            )
    }

}
