package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.MediaItem
import kotlinx.android.synthetic.main.layout_image_grid.view.*

/**
 * @author by yoasfs on 2019-07-01
 */
class FeedMultipleImageView: BaseCustomView {

    private lateinit var gridLayoutManager: GridLayoutManager


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.layout_image_grid, this)
    }

    fun bind(itemList: List<MediaItem>) {
        gridLayoutManager = GridLayoutManager(context, 6)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                when(itemList.size) {
                    1 -> return 6
                    2 -> return 3
                    3 -> return 2
                    4 -> return 3
                    5 -> if (position <2) return 3 else return 2
                    else -> return 2
                }
            }
        }
        rv_media.layoutManager = gridLayoutManager
        rv_media.adapter = ImageAdapter(itemList)
    }

    class ImageAdapter(private var itemList: List<MediaItem>): RecyclerView.Adapter<ImageAdapter.Holder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_multiple_media, parent, false))
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(itemList.get(position))
        }

        class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
            private lateinit var itemImageView: ImageView
             fun bind(item: MediaItem) {
                 itemImageView = itemView.findViewById(R.id.itemImageView)
                 ImageHandler.LoadImage(itemImageView, item.thumbnail)
             }

        }

    }
}