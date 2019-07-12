package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.MediaItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.item_multiple_media.view.*
import kotlinx.android.synthetic.main.layout_image_grid.view.*

/**
 * @author by yoasfs on 2019-07-01
 */
class FeedMultipleImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private val TYPE_EMPTY_NON_FEED = ""

    private val adapter: ImageAdapter by lazy {
        ImageAdapter(mutableListOf())
    }

    init {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.layout_image_grid, this)
        val gridLayoutManager = GridLayoutManager(context, 6)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return when(adapter.itemCount) {
                    1 -> 6
                    2 -> 3
                    3 -> 2
                    4 -> 3
                    5 -> if (position <2) 3 else 2
                    else -> 2
                }
            }
        }
        rv_media.layoutManager = gridLayoutManager
        rv_media.adapter = adapter
        rv_media.isNestedScrollingEnabled = false
    }

    fun bind(itemList: List<MediaItem>) {
        bind(itemList, TYPE_EMPTY_NON_FEED)
    }

    fun bind(itemList: List<MediaItem>, feedType: String) {
        adapter.updateItem(itemList, feedType)
        rv_media.addItemDecoration(ItemOffsetDecoration(context.resources.getDimensionPixelSize(R.dimen.dp_4), adapter.itemCount))
    }

    fun setOnFileClickListener(listener: OnFileClickListener){
        adapter.fileListener = listener
    }

    private class ImageAdapter(private var itemList: MutableList<MediaItem>,
                       var fileListener: OnFileClickListener? = null): RecyclerView.Adapter<ImageAdapter.Holder>() {

        private var feedType = ""
        init {
            setHasStableIds(true)
        }

        fun updateItem(itemList: List<MediaItem>, feedType: String){
            this.itemList.clear()
            this.itemList.addAll(itemList)
            this.feedType = feedType
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_multiple_media, parent, false))
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(itemList[position], feedType)
        }

        override fun getItemId(position: Int): Long {
            return with(itemList[position]){
                (thumbnail+type).hashCode().toLong()
            }
        }

        inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
            init {
                itemView.setOnClickListener { fileListener?.onClickItem(itemList[adapterPosition], adapterPosition) }
            }

            fun bind(item: MediaItem, feedType: String) {
                with(itemView){
                    val btnDeleteMargin = context.resources.getDimensionPixelSize(if (itemCount == 1) R.dimen.dp_16 else R.dimen.dp_8)
                    val layoutParams = delete.layoutParams as LayoutParams
                    layoutParams.setMargins(btnDeleteMargin, btnDeleteMargin, btnDeleteMargin, btnDeleteMargin)
                    delete.layoutParams = layoutParams

                    ImageHandler.LoadImage(itemImageView, item.thumbnail)
                    delete.setOnClickListener { removeItem(item, adapterPosition) }
                    delete.visibility = if (item.isSelected) View.GONE else View.VISIBLE
                    ic_play_vid.shouldShowWithAction(item.type == TYPE_VIDEO && !isSingleItemFromFeed(feedType)){}
                }
            }

            fun isSingleItemFromFeed(feedType: String):Boolean {
                return feedType.isNotEmpty() && itemList.size == 1
            }
        }

        private fun removeItem(media: MediaItem, position: Int) {
            itemList.removeAt(position)
            notifyDataSetChanged()
            fileListener?.onDeleteItem(media, position)
        }

        companion object{
            private const val TYPE_VIDEO = "video"
        }



    }

    interface OnFileClickListener{
        fun onDeleteItem(item: MediaItem, position: Int)
        fun onClickItem(item: MediaItem, position: Int)
    }
}