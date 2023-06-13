package com.tokopedia.feedcomponent.view.adapter.post

import androidx.viewpager.widget.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.MultimediaGridViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.MultimediaGridModel
import com.tokopedia.feedcomponent.view.viewmodel.post.image.ImagePostModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentModel
import com.tokopedia.feedcomponent.view.viewmodel.post.video.VideoModel
import com.tokopedia.feedcomponent.view.viewmodel.post.youtube.YoutubeModel
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.feedcomponent.view.widget.WrapContentViewPager

/**
 * @author by milhamj on 9/21/18.
 */
class PostPagerAdapter(private val imagePostListener: ImagePostViewHolder.ImagePostListener,
                       private val youtubePostListener: YoutubeViewHolder.YoutubePostListener,
                       private val pollOptionListener: PollAdapter.PollOptionListener,
                       private val gridItemListener: GridPostAdapter.GridItemListener,
                       private val videoViewListener: VideoViewHolder.VideoViewListener,
                       private val feedMultipleImageViewListener: FeedMultipleImageView.FeedMultipleImageViewListener,
                       private val feedType: String)
    : PagerAdapter() {

    private val itemList: MutableList<BasePostModel> = ArrayList()
    private var currentPosition = -1

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = itemList.size

    @Suppress("UNCHECKED_CAST")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val element: BasePostModel = itemList[position]
        val viewHolder: BasePostViewHolder<BasePostModel> = when (element) {
            is ImagePostModel -> ImagePostViewHolder(imagePostListener) as BasePostViewHolder<BasePostModel>
            is YoutubeModel -> YoutubeViewHolder(youtubePostListener) as BasePostViewHolder<BasePostModel>
            is PollContentModel -> PollViewHolder(pollOptionListener) as BasePostViewHolder<BasePostModel>
            is GridPostModel -> GridPostViewHolder(gridItemListener) as BasePostViewHolder<BasePostModel>
            is VideoModel -> VideoViewHolder(videoViewListener) as BasePostViewHolder<BasePostModel>
            is MultimediaGridModel -> MultimediaGridViewHolder(feedMultipleImageViewListener, feedType) as BasePostViewHolder<BasePostModel>
            else -> throw IllegalStateException(this.javaClass.simpleName
                    .plus(" doesn't support view model of this type: ")
                    .plus(element.javaClass.simpleName))
        }
        val view = viewHolder.inflate(container, element, position)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        if (position != currentPosition) {
            val view = `object` as View?
            val pager = container as WrapContentViewPager?
            if (view != null) {
                currentPosition = position
                pager?.measureCurrentView(view)
            }
        }
    }

    override fun getItemPosition(`object`: Any) = PagerAdapter.POSITION_NONE

    fun setList(imageList: MutableList<BasePostModel>) {
        this.itemList.clear()
        this.itemList.addAll(imageList)
        notifyDataSetChanged()
    }
}
