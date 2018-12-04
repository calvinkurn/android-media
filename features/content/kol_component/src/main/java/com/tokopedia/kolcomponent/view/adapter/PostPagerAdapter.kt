package com.tokopedia.kolcomponent.view.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kolcomponent.view.viewholder.ImageViewHolder
import com.tokopedia.kolcomponent.view.viewmodel.ImageViewModel

/**
 * @author by milhamj on 9/21/18.
 */
class PostPagerAdapter : PagerAdapter() {

    val itemList: MutableList<Any> = ArrayList()

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = itemList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val element: Any = itemList[position]
        var view: View? = null

        if (element is ImageViewModel) {
            view = ImageViewHolder(container).inflate(element)
        }

        container.addView(view)
        return view!!
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any) = PagerAdapter.POSITION_NONE

    fun setList(imageList: ArrayList<Any>) {
        this.itemList.clear()
        this.itemList.addAll(imageList)
        notifyDataSetChanged()
    }

    interface ImageClickListener {
        fun onImageClick(position: Int)
    }
}