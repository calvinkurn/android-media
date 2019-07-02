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
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import android.util.DisplayMetrics
import android.view.*
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder.Companion.SOURCE_DETAIL
import com.tokopedia.feedcomponent.view.widget.RatingBarReview


/**
 * @author by yfsx on 22/03/19.
 */
class PostTagAdapter(private val itemList: List<PostTagItem>,
                     private val listener: DynamicPostViewHolder.DynamicPostListener,
                     private val positionInFeed: Int,
                     private val feedType: String)
    : RecyclerView.Adapter<PostTagAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_producttag_list, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item: PostTagItem = itemList.get(position)
        holder.bind(item, listener, positionInFeed, position, feedType, itemList.size != 1)
    }

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val VALUE_CARD_SIZE = 0.75

        private lateinit var productLayout: CardView
        private lateinit var productImage: ImageView
        private lateinit var productPrice: TextView
        private lateinit var productName: TextView
        private lateinit var productTag: TextView
        private lateinit var btnBuy: ButtonCompat
        private lateinit var productNameSection: LinearLayout
        private lateinit var productTagBackground: RelativeLayout
        private lateinit var container: CardView
        private lateinit var widgetRating: RatingBarReview

        fun bind(item: PostTagItem,
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
            btnBuy = itemView.findViewById(R.id.btnProductBuy)
            productNameSection = itemView.findViewById(R.id.productNameSection)
            productName = itemView.findViewById(R.id.productName)
            widgetRating = itemView.findViewById(R.id.widgetRating)
            productImage.loadImageRounded(item.thumbnail, 10f)
            productPrice.text = item.price
            productTag.visibility = View.GONE
            productTagBackground.visibility = View.GONE
            btnBuy.visibility = View.GONE
            productLayout.setOnClickListener(
                getItemClickNavigationListener(listener, positionInFeed, item, itemPosition)
            )
            productName.text = item.text
            productNameSection.setOnClickListener(
                    getItemClickNavigationListener(listener, positionInFeed, item, itemPosition))
            if (item.rating == 0) {
                widgetRating.visibility = View.GONE
            } else {
                widgetRating.rating = (item.rating / 20).toInt()
            }
            if (!feedType.equals(SOURCE_DETAIL) && needToResize) {
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
                                container.layoutParams.width = (displayMetrics.widthPixels * VALUE_CARD_SIZE).toInt()
                                container.requestLayout()
                            }
                        }
                )
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
    }
}