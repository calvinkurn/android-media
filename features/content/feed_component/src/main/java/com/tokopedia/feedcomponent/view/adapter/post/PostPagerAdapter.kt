package com.tokopedia.feedcomponent.view.adapter.post

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.GridPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.image.ImagePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.youtube.YoutubeViewModel

/**
 * @author by milhamj on 9/21/18.
 */
class PostPagerAdapter : PagerAdapter() {

    private val itemList: MutableList<BasePostViewModel> = ArrayList()

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = itemList.size

    @Suppress("UNCHECKED_CAST")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val element: BasePostViewModel = itemList[position]
        val viewHolder: BasePostViewHolder<BasePostViewModel> = when (element) {
            is ImagePostViewModel -> ImagePostViewHolder() as BasePostViewHolder<BasePostViewModel>
            is GridPostViewModel -> GridPostViewHolder() as BasePostViewHolder<BasePostViewModel>
            is PollViewModel -> PollViewHolder() as BasePostViewHolder<BasePostViewModel>
            is YoutubeViewModel -> YoutubeViewHolder() as BasePostViewHolder<BasePostViewModel>
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

    override fun getItemPosition(`object`: Any) = PagerAdapter.POSITION_NONE

    fun setList(imageList: MutableList<BasePostViewModel>) {
        this.itemList.clear()
        this.itemList.addAll(imageList)
        notifyDataSetChanged()
    }

    interface ContentClickListener {
        fun onContentClick(position: Int)
    }
}