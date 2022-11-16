package com.tokopedia.people.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.library.baseadapter.BaseAdapter
import com.tokopedia.people.databinding.UpItemUserFeedBinding
import com.tokopedia.people.model.Post
import com.tokopedia.people.model.UserFeedPostsModel

class UserFeedPostsBaseAdapter(
    callback: AdapterCallback,
    val feedPostsWidgetCallback: FeedPostsCallback,
    val onLoadMore: (cursor: String) -> Unit,
) : BaseAdapter<Post>(callback) {

    var cursor: String = ""

    inner class ViewHolder(private val view: UpItemUserFeedBinding) : BaseVH(view.root) {

        override fun bindView(item: Post?, position: Int) {
            if (item == null || item.media.isEmpty()) return
            val firstItem = item.media.first()

            view.imageContent.apply {
                cornerRadius = 0
                setImageUrl(item.media.first().coverURL)
                setOnClickListener {
                    feedPostsWidgetCallback.onFeedPostsClick(
                        item.appLink,
                        item.id,
                        firstItem.coverURL,
                        position,
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
    }

    override fun getItemViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int,
    ): BaseVH {
        return ViewHolder(
            UpItemUserFeedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (position == (itemCount - 1) && !isLastPage) onLoadMore(cursor)
    }

    fun onSuccess(data: UserFeedPostsModel) {
        data.feedXProfileGetProfilePosts.posts.let { loadCompleted(it, data) }
        isLastPage = !data.feedXProfileGetProfilePosts.pagination.hasNext
        cursor = data.feedXProfileGetProfilePosts.pagination.cursor
    }

    fun onError() {
        loadCompletedWithError()
    }

    interface FeedPostsCallback {
        fun onFeedPostsClick(appLink: String, itemID: String, imageUrl: String, position: Int, mediaType: String)
    }

    companion object {
        private const val MEDIA_TYPE_IMAGE = "image"
        private const val MEDIA_TYPE_VIDEO = "video"
    }
}
