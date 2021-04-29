package com.tokopedia.feedcomponent.view.adapter.viewholder.posttag

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.pojo.common.ColorPojo
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.TagsItem
import com.tokopedia.feedcomponent.data.pojo.track.Tracking
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModelNew
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

private const val RAD_20f = 20f
private const val RAD_30f = 30f

class ProductPostTagViewHolderNew(val mainView: View,
                                  val listener: DynamicPostViewHolder.DynamicPostListener)
    : AbstractViewHolder<ProductPostTagViewModelNew>(mainView) {

    private lateinit var productLayout: FrameLayout
    private lateinit var productImage: ImageView
    private lateinit var productPrice: Typography
    private lateinit var productName: Typography
    private lateinit var productTag: Typography
    private lateinit var productNameSection: LinearLayout
    private lateinit var rating: Typography
    private lateinit var label: Label
    private lateinit var soldInfo: Typography
    private lateinit var freeShipping:ImageUnify

    override fun bind(item: ProductPostTagViewModelNew) {
        productLayout = itemView.findViewById(R.id.productLayout)
        productImage = itemView.findViewById(R.id.productImage)
        productPrice = itemView.findViewById(R.id.productPrice)
        productTag = itemView.findViewById(R.id.productTag)
        productNameSection = itemView.findViewById(R.id.productNameSection)
        productName = itemView.findViewById(R.id.productName)
        rating = itemView.findViewById(R.id.rating)
        label = itemView.findViewById(R.id.discountLabel)
        soldInfo = itemView.findViewById(R.id.soldInfo)
        freeShipping = itemView.findViewById(R.id.freeShipping)
        productPrice.text = item.priceFmt

        productLayout.setOnClickListener(
                getItemClickNavigationListener(listener, item.positionInFeed, item.product, adapterPosition)
        )
        if(item.isDiscount) {
            label.visible()
            productTag.visible()
            productTag.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text = item.originalPriceFmt
            }
            label.text = item.discountFmt
        }else{
            label.gone()
            productTag.gone()
        }
        if(item.isFreeShipping){
            freeShipping.visible()
            freeShipping.loadImage(item.freeShippingURL)
        }else{
            freeShipping.gone()
        }
        productImage.loadImage(item.imgUrl)
        productName.text = item.text
        productNameSection.setOnClickListener(
                getItemClickNavigationListener(listener, item.positionInFeed, item.product, adapterPosition))
        rating.text = item.rating.toString()
        soldInfo.text = item.totalSold.toString()

    }

    private fun renderTag(textView: TextView, tag: TagsItem) {
        textView.text = tag.text
        if (tag.bgColor.hex.isEmpty() || tag.bgColor.opacity.isEmpty()) {
            tag.bgColor = getDefaultBackgroundColor()
        }
        if (tag.textColor.hex.isEmpty() || tag.textColor.opacity.isEmpty()) {
            tag.textColor = getDefaultTextColor()
        }
        textView.setTextColor(Color.parseColor(tag.textColor.hex))
        textView.background = renderDrawable(tag.bgColor.hex, OPACITY_70)
    }

    private fun renderDrawable(hex: String, opacity: String): Drawable {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadii = floatArrayOf(RAD_30f, RAD_30f, RAD_30f, RAD_30f, RAD_30f, RAD_30f, RAD_30f, RAD_30f)
        drawable.setColor(Color.parseColor(hex))
        drawable.alpha = calculateBackgroundAlpha(opacity)
        return drawable
    }

    private fun calculateBackgroundAlpha(opacityString: String): Int {
        val floatValue = opacityString.toFloat()
        return (floatValue * 255).toInt()
    }

    private fun getDefaultBackgroundColor(): ColorPojo {
        return ColorPojo(HEX_BLACK, OPACITY_70)
    }

    private fun getDefaultTextColor(): ColorPojo {
        return ColorPojo(HEX_WHITE, OPACITY_100)
    }

    private fun getItemClickNavigationListener(listener: DynamicPostViewHolder.DynamicPostListener,
                                               positionInFeed: Int,
                                               item: FeedXProduct, itemPosition: Int)
            : View.OnClickListener {
        return View.OnClickListener {
            listener.onPostTagItemBSClick(positionInFeed, item.appLink, item, itemPosition)
            //   listener.onAffiliateTrackClicked(mappingTracking(item.tracking), true)
        }
    }

    private fun mappingTracking(trackListPojo: List<Tracking>): MutableList<TrackingViewModel> {
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

    private fun onBuyButtonClicked(listener: DynamicPostViewHolder.DynamicPostListener, positionInFeed: Int, itemPojo: PostTagItem, authorType: String) {
        listener.onPostTagItemBuyClicked(positionInFeed, itemPojo, authorType)
        listener.onAffiliateTrackClicked(mappingTracking(itemPojo.tracking), true)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_producttag_list_new

        private const val HEX_BLACK = "#000"
        private const val HEX_WHITE = "#fff"
        private const val OPACITY_70 = "0.7"
        private const val OPACITY_100 = "1"
    }
}