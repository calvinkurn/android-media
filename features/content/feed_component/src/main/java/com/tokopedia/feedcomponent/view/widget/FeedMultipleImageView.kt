package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.MediaItem
import com.tokopedia.feedcomponent.data.pojo.track.Tracking
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageRounded
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

    private val itemDecoration: RecyclerView.ItemDecoration

    init {
        itemDecoration = ItemOffsetDecoration(context.resources.getDimensionPixelSize(com.tokopedia.feedcomponent.R.dimen.feed_component_dp_4))
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.layout_image_grid, this)
        val gridLayoutManager = GridLayoutManager(context, 6)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.itemCount) {
                    1 -> 6
                    2 -> 3
                    3 -> 2
                    4 -> 3
                    5 -> if (position < 2) 3 else 2
                    else -> 2
                }
            }
        }
        rv_media.apply {
            layoutManager = gridLayoutManager
            adapter = this@FeedMultipleImageView.adapter
            isNestedScrollingEnabled = false
            setItemDecoration(itemDecoration)
        }
    }

    fun bind(itemList: List<MediaItem>) {
        bind(itemList, TYPE_EMPTY_NON_FEED)
    }

    fun bind(itemList: List<MediaItem>, feedType: String) {
        adapter.updateItem(itemList, feedType)
    }

    fun setOnFileClickListener(listener: OnFileClickListener) {
        adapter.fileListener = listener
    }

    fun setFeedMultipleImageViewListener(listener: FeedMultipleImageViewListener) {
        adapter.feedMultipleImageViewListener = listener
    }

    private class ImageAdapter(private var itemList: MutableList<MediaItem>,
                               var fileListener: OnFileClickListener? = null,
                               var feedMultipleImageViewListener: FeedMultipleImageViewListener? = null)
        : RecyclerView.Adapter<ImageAdapter.Holder>() {

        private var feedType = ""

        init {
            setHasStableIds(true)
        }

        fun updateItem(itemList: List<MediaItem>, feedType: String) {
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
            return with(itemList[position]) {
                (thumbnail + type).hashCode().toLong()
            }
        }

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            init {
                itemView.setOnClickListener {
                    val media = itemList[adapterPosition]
                    fileListener?.onClickItem(media, adapterPosition)
                    feedMultipleImageViewListener?.onMediaGridClick(media.positionInFeed, adapterPosition,
                            media.applink, itemCount == 1)
                    if (media.tracking.isNotEmpty()) {
                        feedMultipleImageViewListener?.onAffiliateTrackClicked(mapTrackingData(media.tracking), true)
                    }
                    if (media.type == TYPE_VIDEO) {
                        itemView.ic_play_vid.gone()
                    }
                }
            }

            fun bind(item: MediaItem, feedType: String) {
                with(itemView) {
                    val btnDeleteMargin = context.resources.getDimensionPixelSize(if (itemCount == 1) R.dimen.dp_16 else R.dimen.dp_8)
                    val layoutParams = delete.layoutParams as LayoutParams
                    layoutParams.setMargins(btnDeleteMargin, btnDeleteMargin, btnDeleteMargin, btnDeleteMargin)
                    delete.layoutParams = layoutParams

                    itemImageView.loadImageRounded(item.thumbnail, RAD_10f)
                    delete.setOnClickListener { removeItem(item, adapterPosition) }
                    delete.visibility = if (item.isSelected) View.GONE else View.VISIBLE
                    if (item.videos.isNotEmpty()) {
                        ic_play_vid.shouldShowWithAction(item.type == TYPE_VIDEO) {
                            val modLength = context.resources.getDimensionPixelSize(if (itemCount == 1) R.dimen.dp_72 else R.dimen.dp_36)
                            ic_play_vid.layoutParams.width = modLength
                            ic_play_vid.layoutParams.height = modLength
                        }
                    }
                    else ic_play_vid.gone()
                }

            }


            private fun mapTrackingData(trackList: List<Tracking>): List<TrackingViewModel> {
                return trackList.map {
                    TrackingViewModel(
                            it.clickURL,
                            it.viewURL,
                            it.type,
                            it.source
                    )
                }
            }

            fun isSingleItemFromFeed(feedType: String): Boolean {
                return feedType.isNotEmpty() && itemList.size == 1
            }
        }

        private fun removeItem(media: MediaItem, position: Int) {
            itemList.removeAt(position)
            notifyDataSetChanged()
            fileListener?.onDeleteItem(media, position)
        }

        companion object {
            private val RAD_10f = 10f
            private const val TYPE_VIDEO = "video"
        }
    }


    interface OnFileClickListener {
        fun onDeleteItem(item: MediaItem, position: Int)
        fun onClickItem(item: MediaItem, position: Int)
    }

    interface FeedMultipleImageViewListener {
        fun onMediaGridClick(positionInFeed: Int, contentPosition: Int,
                             redirectLink: String, isSingleItem: Boolean)

        fun onAffiliateTrackClicked(trackList: List<TrackingViewModel>, isClick: Boolean)
    }

    private fun RecyclerView.setItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
        for (itemDecorIndex in 0 until itemDecorationCount) {
            removeItemDecorationAt(itemDecorIndex)
        }
        addItemDecoration(itemDecoration)
    }
}