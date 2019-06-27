package com.tokopedia.feedcomponent.view.adapter.posttag

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.common.ColorPojo
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItemTag
import com.tokopedia.feedcomponent.data.pojo.track.Tracking
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder.Companion.SOURCE_DETAIL
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder.Companion.SOURCE_FEEDS
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import android.util.DisplayMetrics
import android.view.*



/**
 * @author by yfsx on 22/03/19.
 */
class PostTagAdapter(private val itemList: List<PostTagItem>,
                     private val listener: DynamicPostViewHolder.DynamicPostListener,
                     private val positionInFeed: Int,
                     private val feedType: String)
    : RecyclerView.Adapter<PostTagAdapter.Holder>() {

    companion object {
        private val TYPE_LIST = "TYPE_LIST"
        private val TYPE_GRID = "TYPE_GRID"
    }

    private var layoutType = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var layoutRes = 0
        when(feedType) {
            SOURCE_FEEDS, SOURCE_DETAIL -> {
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
        holder.bind(item, layoutType, listener, positionInFeed, position, feedType, itemList.size != 1)
    }

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private lateinit var productLayout: CardView
        private lateinit var productImage: ImageView
        private lateinit var productPrice: TextView
        private lateinit var productName: TextView
        private lateinit var productTag: TextView
        private lateinit var btnBuy: ButtonCompat
        private lateinit var productNameSection: LinearLayout
        private lateinit var productTagBackground: RelativeLayout
        private lateinit var container: CardView

        fun bind(item: PostTagItem, layoutType: String,
                 listener: DynamicPostViewHolder.DynamicPostListener,
                 positionInFeed: Int,
                 itemPosition: Int,
                 feedType: String,
                 needToResize: Boolean) {
            productLayout = itemView.findViewById(R.id.productLayout)
            productImage = itemView.findViewById(R.id.productImage)
            productPrice = itemView.findViewById(R.id.productPrice)
            productTag = itemView.findViewById(R.id.productTag)
            productTagBackground = itemView.findViewById(R.id.productTagBackground)
            productImage.loadImageRounded(item.thumbnail, 8f)
            productPrice.text = item.price
            if (item.tags.isNotEmpty()) {
                productTagBackground.visibility = View.VISIBLE
                renderTag(productTag, item.tags.get(0))
            } else {
                productTagBackground.visibility = View.GONE
            }
            productLayout.setOnClickListener(
                getItemClickNavigationListener(listener, positionInFeed, item, itemPosition)
            )

            if (layoutType.equals(TYPE_LIST)) {
                btnBuy = itemView.findViewById(R.id.btnProductBuy)
                productNameSection = itemView.findViewById(R.id.productNameSection)
                productName = itemView.findViewById(R.id.productName)
                productName.text = item.text
                productNameSection.setOnClickListener(
                        getItemClickNavigationListener(listener, positionInFeed, item, itemPosition))

                if (feedType.equals(SOURCE_FEEDS) && needToResize) {
                    container = itemView.findViewById(R.id.container)
                    container.viewTreeObserver.addOnGlobalLayoutListener(
                            object : ViewTreeObserver.OnGlobalLayoutListener {
                                override fun onGlobalLayout() {
                                    val viewTreeObserver = container.viewTreeObserver
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                                    } else {
                                        @Suppress("DEPRECATION")
                                        viewTreeObserver.removeGlobalOnLayoutListener(this)
                                    }
                                    val displayMetrics = DisplayMetrics()
                                    val windowmanager = itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                                    windowmanager.getDefaultDisplay().getMetrics(displayMetrics)
                                    container.layoutParams.width = (displayMetrics.widthPixels * 0.75).toInt()
                                    container.requestLayout()
                                }
                            }
                    )
                }
            }

        }

        private fun getItemClickNavigationListener(listener: DynamicPostViewHolder.DynamicPostListener,
                                                   positionInFeed: Int,
                                                   item: PostTagItem, itemPosition: Int)
                : View.OnClickListener {
             return View.OnClickListener {
                 listener.onPostTagItemClick(positionInFeed, item.applink, item, itemPosition)
                 listener.onAffiliateTrackClicked(mappingTracking(item.tracking), true)
             }
        }

        private fun mappingTracking(trackListPojo : List<Tracking>): MutableList<TrackingViewModel> {
            val trackList = ArrayList<TrackingViewModel>()
            for (trackPojo: Tracking in trackListPojo) {
                trackList.add(TrackingViewModel(
                        trackPojo.clickURL,
                        trackPojo.viewURL,
                        trackPojo.type,
                        trackPojo.source
                ))
            }
            return trackList
        }

        private fun renderTag(textView: TextView, tag: PostTagItemTag) {
            textView.text = tag.text
            if (tag.bgColor.hex.isEmpty() || tag.bgColor.opacity.isEmpty()) {
                tag.bgColor = getDefaultBackgroundColor()
            }
            if (tag.textColor.hex.isEmpty() || tag.textColor.opacity.isEmpty()) {
                tag.textColor = getDefaultTextColor()
            }
            textView.setTextColor(Color.parseColor(tag.textColor.hex))
            textView.background = renderDrawable(tag.bgColor.hex, tag.bgColor.opacity)
        }

        private fun renderDrawable(hex: String, opacity: String): Drawable {
            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.cornerRadii = floatArrayOf(30f, 30f, 30f ,30f , 30f, 30f, 30f, 30f)
            drawable.setColor(Color.parseColor(hex))
            drawable.alpha = calculateBackgroundAlpha(opacity)
            return drawable
        }

        private fun calculateBackgroundAlpha(opacityString: String) : Int {
            val floatValue = opacityString.toFloat()
            return (floatValue*100).toInt()
        }

        private fun getDefaultBackgroundColor() : ColorPojo {
            return ColorPojo("#000", "0.7")
        }

        private fun getDefaultTextColor() : ColorPojo {
            return ColorPojo("#fff", "1")
        }
    }
}