package com.tokopedia.feedcomponent.view.adapter.posttag

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.MediaItem
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTag
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder

/**
 * @author by yfsx on 22/03/19.
 */
class PostTagAdapter(private val itemList: List<MediaItem>,
                     private val listener: DynamicPostViewHolder.DynamicPostListener,
                     private val positionInFeed: Int)
    : RecyclerView.Adapter<PostTagAdapter.Holder>() {

    companion object {
        private val TYPE_LIST = "TYPE_LIST"
        private val TYPE_GRID = "TYPE_GRID"
    }

    private var layoutType = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var layoutRes = 0
        when(itemList.size) {
            1 -> {
                layoutRes = R.layout.item_producttag_list
                layoutType = TYPE_LIST
            }
            else -> {
                layoutRes = R.layout.item_producttag_grid
                layoutType = TYPE_GRID
            }
        }
        return Holder(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item: MediaItem = itemList.get(position)
        holder.bind(item, layoutType, listener, positionInFeed)
    }

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private lateinit var productLayout: CardView
        private lateinit var productImage: ImageView
        private lateinit var productPrice: TextView
        private lateinit var productName: TextView

        fun bind(item: MediaItem, layoutType: String,
                 listener: DynamicPostViewHolder.DynamicPostListener,
                 positionInFeed: Int) {
            productLayout = itemView.findViewById(R.id.productLayout)
            productImage = itemView.findViewById(R.id.productImage)
            productPrice = itemView.findViewById(R.id.productPrice)
            ImageHandler.loadImageRounded2(itemView.context, productImage, item.thumbnail)
            productPrice.text = item.price
            productLayout.setOnClickListener({
                listener.onPostTagItemClick(positionInFeed, item.applink)
            })

            if (layoutType.equals(TYPE_LIST)) {
                productName = itemView.findViewById(R.id.productName)
                productName.text = item.text
            }
        }

    }
}