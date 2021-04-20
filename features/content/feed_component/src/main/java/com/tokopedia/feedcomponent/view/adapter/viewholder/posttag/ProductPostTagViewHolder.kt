package com.tokopedia.feedcomponent.view.adapter.viewholder.posttag

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.Size
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.common.ColorPojo
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.TagsItem
import com.tokopedia.feedcomponent.data.pojo.track.Tracking
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.feedcomponent.view.widget.RatingBarReview
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifyprinciples.Typography
import kotlin.math.min
import kotlin.math.roundToInt

private const val RAD_20f = 20f
private const val RAD_30f = 30f

class ProductPostTagViewHolder(val mainView: View,
                               val listener: DynamicPostViewHolder.DynamicPostListener,
                               private val screenWidth: Int)
    : AbstractViewHolder<ProductPostTagViewModel>(mainView) {

    private lateinit var productLayout: FrameLayout
    private lateinit var productImage: ImageView
    private lateinit var productPrice: Typography
    private lateinit var productName: Typography
    private lateinit var productTag: Typography
    private lateinit var btnBuy: FrameLayout
    private lateinit var textBtnBuy: Typography
    private lateinit var productNameSection: LinearLayout
    private lateinit var container: CardUnify
    private lateinit var widgetRating: RatingBarReview

    override fun bind(item: ProductPostTagViewModel) {
        productLayout = itemView.findViewById(R.id.productLayout)
        productImage = itemView.findViewById(R.id.productImage)
        productPrice = itemView.findViewById(R.id.productPrice)
        productTag = itemView.findViewById(R.id.productTag)
        btnBuy = itemView.findViewById(R.id.btnProductBuy)
        textBtnBuy = itemView.findViewById(R.id.textBtnProductBuy)
        productNameSection = itemView.findViewById(R.id.productNameSection)
        productName = itemView.findViewById(R.id.productName)
        widgetRating = itemView.findViewById(R.id.widgetRating)
        productImage.loadImageRounded(item.thumbnail, RAD_20f)
        productPrice.text = item.price

        val btnCtaPojo = item.postTagItemPojo.buttonCTA.firstOrNull()
        if (btnCtaPojo != null) {
            btnBuy.visible()

            val isCTADisabled = btnCtaPojo.isDisabled
            btnBuy.apply {
                isEnabled = !isCTADisabled
                setOnClickListener { onBuyButtonClicked(listener, item.positionInFeed, item.postTagItemPojo, item.authorType) }
            }
            textBtnBuy.apply {
                text =
                        if (!isCTADisabled) btnCtaPojo.text
                        else btnCtaPojo.textDisabled
                if (text.isEmpty()) text = getString(R.string.empty_product)
                setTextColor(ContextCompat.getColor(
                        context,
                        if (isCTADisabled) R.color.Unify_N200 else com.tokopedia.unifyprinciples.R.color.Unify_N0
                ))
            }
        } else btnBuy.gone()

        productLayout.setOnClickListener(
                getItemClickNavigationListener(listener, item.positionInFeed, item.postTagItemPojo, adapterPosition)
        )
        productName.text = item.text
        productNameSection.setOnClickListener(
                getItemClickNavigationListener(listener, item.positionInFeed, item.postTagItemPojo, adapterPosition))
        if (item.rating <= 0) {
            widgetRating.gone()
        } else {
            widgetRating.visible()
            widgetRating.updateRating((item.rating / RAD_20f).roundToInt())
        }
        if (item.feedType != DynamicPostViewHolder.SOURCE_DETAIL && item.needToResize) {
            container = itemView.findViewById(R.id.container)
            container.layoutParams.width = screenWidth * 3 / 4
        }

        if (item.tags.isNotEmpty()) {
            productTag.visible()
            renderTag(productTag, item.tags.first())
        } else {
            productTag.gone()
        }
    }

    private fun renderTag(textView: TextView, tag: TagsItem) {
        textView.text = tag.text
        if (tag.bgColor.hex.isEmpty() || tag.bgColor.opacity.isEmpty()) {
            tag.bgColor = getDefaultBackgroundColor()
        }
        if (tag.textColor.hex.isEmpty() || tag.textColor.opacity.isEmpty()) {
            tag.textColor = getDefaultTextColor()
        }
        val textColor = if(tag.textColor.hex.isEmpty()) getString(R.string.feed_color_pojo_dms_hex_black_value) else tag.textColor.hex
        textView.setTextColor(Color.parseColor(textColor))
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
        return ColorPojo(getString(R.string.feed_color_pojo_dms_hex_black_value), OPACITY_70)
    }

    private fun getDefaultTextColor(): ColorPojo {
        return ColorPojo(getString(R.string.feed_color_pojo_dms_hex_white_value), OPACITY_100)
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
        val LAYOUT = R.layout.item_producttag_list

        private const val OPACITY_70 = "0.7"
        private const val OPACITY_100 = "1"
    }
}