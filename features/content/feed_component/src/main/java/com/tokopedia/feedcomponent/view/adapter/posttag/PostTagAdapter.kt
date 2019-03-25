package com.tokopedia.feedcomponent.view.adapter.posttag

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItemTag
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.kotlin.extensions.view.loadImageRounded

/**
 * @author by yfsx on 22/03/19.
 */
class PostTagAdapter(private val itemList: List<PostTagItem>,
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
        val item: PostTagItem = itemList.get(position)
        holder.bind(item, layoutType, listener, positionInFeed)
    }

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private lateinit var productLayout: CardView
        private lateinit var productImage: ImageView
        private lateinit var productPrice: TextView
        private lateinit var productName: TextView
        private lateinit var productTag: TextView
        private lateinit var productTagBackground: RelativeLayout

        fun bind(item: PostTagItem, layoutType: String,
                 listener: DynamicPostViewHolder.DynamicPostListener,
                 positionInFeed: Int) {
            productLayout = itemView.findViewById(R.id.productLayout)
            productImage = itemView.findViewById(R.id.productImage)
            productPrice = itemView.findViewById(R.id.productPrice)
            productTag = itemView.findViewById(R.id.productTag)
            productTagBackground = itemView.findViewById(R.id.productTagBackground)
            productImage.loadImageRounded(item.thumbnail)
            productPrice.text = item.price
            if (item.tags.isNotEmpty()) {
                productTagBackground.visibility = View.VISIBLE
                renderTag(productTag, item.tags.get(0))
            } else {
                productTagBackground.visibility = View.GONE
            }
            productLayout.setOnClickListener({
                listener.onPostTagItemClick(positionInFeed, item.applink)
            })

            if (layoutType.equals(TYPE_LIST)) {
                productName = itemView.findViewById(R.id.productName)
                productName.text = item.text
            }
        }

        private fun renderTag(textView: TextView, tag: PostTagItemTag) {
            textView.text = tag.text
            textView.setTextColor(Color.parseColor(tag.textColor.hex))
            textView.background = renderDrawable(tag.bgColor.hex, tag.bgColor.opacity)
        }

        private fun renderDrawable(hex: String, opacity: String): Drawable {
            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.cornerRadii = floatArrayOf(10f, 10f, 10f ,10f ,0f, 0f, 0f, 0f)
            drawable.setColor(Color.parseColor(hex))
            drawable.alpha = calculateBackgroundAlpha(opacity)
            return drawable
        }

        private fun calculateBackgroundAlpha(opacityString: String) : Int {
            val floatValue = opacityString.toFloat()
            return (floatValue*100).toInt()
        }
    }
}