package com.tokopedia.feedcomponent.view.adapter.viewholder.posttag

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.common.ColorPojo
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItemTag
import com.tokopedia.feedcomponent.data.pojo.track.Tracking
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.feedcomponent.view.widget.RatingBarReview
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.visible
import kotlin.math.roundToInt

/**
 * @author by yoasfs on 2019-07-18
 */

class ProductPostTagViewHolder(val mainView: View, val listener: DynamicPostViewHolder.DynamicPostListener)
    : AbstractViewHolder<ProductPostTagViewModel>(mainView) {

    private lateinit var productLayout: CardView
    private lateinit var productImage: ImageView
    private lateinit var productPrice: TextView
    private lateinit var productName: TextView
    private lateinit var productTag: TextView
    private lateinit var btnBuy: FrameLayout
    private lateinit var textBtnBuy: TextView
    private lateinit var productNameSection: LinearLayout
    private lateinit var container: CardView
    private lateinit var widgetRating: RatingBarReview

    private val RAD_10f = 10f
    private val RAD_20f = 20f
    private val RAD_30f = 30f

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
        productImage.loadImageRounded(item.thumbnail, RAD_10f)
        productPrice.text = item.price

        val btnCtaPojo = item.postTagItemPojo.buttonCTA.firstOrNull()
        if (btnCtaPojo != null) {
            btnBuy.visible()

            val isCTADisabled = btnCtaPojo.isDisabled
            btnBuy.apply {
                isEnabled = !isCTADisabled
                setOnClickListener { onBuyButtonClicked(listener, item.postTagItemPojo) }
            }
            textBtnBuy.apply {
                text =
                        if (!isCTADisabled) btnCtaPojo.text
                        else btnCtaPojo.textDisabled
                if (text.isEmpty()) text = getString(R.string.empty_product)
                setTextColor(ContextCompat.getColor(
                        context,
                        if (isCTADisabled) R.color.Neutral_N200 else R.color.Neutral_N0
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
            container.viewTreeObserver.addOnGlobalLayoutListener(getGlobalLayoutListener())
        }

        if (item.tags.isNotEmpty()) {
            productTag.visible()
            renderTag(productTag, item.tags.first())
        } else {
            productTag.gone()
        }
    }


    private fun getGlobalLayoutListener(): ViewTreeObserver.OnGlobalLayoutListener {
        return object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val viewTreeObserver = container.viewTreeObserver
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    @Suppress("DEPRECATION")
                    viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
                val displayMetrics = DisplayMetrics()
                (itemView.context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.let {
                    it.defaultDisplay.getMetrics(displayMetrics)
                    container.layoutParams.width = (displayMetrics.widthPixels * VALUE_CARD_SIZE).toInt()
                    container.requestLayout()
                }
            }
        }
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
        drawable.cornerRadii = floatArrayOf(RAD_30f, RAD_30f, RAD_30f, RAD_30f, RAD_30f, RAD_30f, RAD_30f, RAD_30f)
        drawable.setColor(Color.parseColor(hex))
        drawable.alpha = calculateBackgroundAlpha(opacity)
        return drawable
    }

    private fun calculateBackgroundAlpha(opacityString: String): Int {
        val floatValue = opacityString.toFloat()
        return (floatValue * 100).toInt()
    }

    private fun getDefaultBackgroundColor(): ColorPojo {
        return ColorPojo(HEX_BLACK, OPACITY_70)
    }

    private fun getDefaultTextColor(): ColorPojo {
        return ColorPojo(HEX_WHITE, OPACITY_100)
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

    private fun onBuyButtonClicked(listener: DynamicPostViewHolder.DynamicPostListener, itemPojo: PostTagItem) {
        listener.onPostTagItemBuyClicked(itemPojo)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_producttag_list

        private const val VALUE_CARD_SIZE = 0.75
        private const val HEX_BLACK = "#000"
        private const val HEX_WHITE = "#fff"
        private const val OPACITY_70 = "0.7"
        private const val OPACITY_100 = "1"
    }
}