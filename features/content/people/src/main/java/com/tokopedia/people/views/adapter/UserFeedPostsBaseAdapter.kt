package com.tokopedia.people.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            if (item == null) return

            view.imageContent.apply {
                cornerRadius = 0
                setImageUrl(item.media.first().coverURL)
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
        fun onFeedPostsClick(appLink: String)
    }
}
