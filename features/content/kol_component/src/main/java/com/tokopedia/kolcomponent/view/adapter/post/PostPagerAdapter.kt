package com.tokopedia.kolcomponent.view.adapter.post

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kolcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.kolcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.kolcomponent.view.viewmodel.post.image.ImagePostViewModel

/**
 * @author by milhamj on 9/21/18.
 */
class PostPagerAdapter : PagerAdapter() {

    private val itemList: MutableList<Any> = ArrayList()

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = itemList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val element: Any = itemList[position]
        var viewHolder: BasePostViewHolder<Any>? = null

        if (element is ImagePostViewModel) {
            viewHolder = ImagePostViewHolder() as BasePostViewHolder<Any>
        }

        val view = viewHolder!!.inflate(container, element, position)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any) = PagerAdapter.POSITION_NONE

    fun setList(imageList: MutableList<Any>) {
        this.itemList.clear()
        this.itemList.addAll(imageList)
        notifyDataSetChanged()
    }

    interface ContentClickListener {
        fun onContentClick(position: Int)
    }
}