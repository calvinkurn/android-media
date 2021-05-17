package com.tokopedia.feedcomponent.view.adapter.viewholder.posttag

import android.graphics.Paint
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.data.pojo.track.Tracking
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModelNew
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class ProductPostTagViewHolderNew(val mainView: View,
                                  val listener: DynamicPostViewHolder.DynamicPostListener)
    : AbstractViewHolder<ProductPostTagViewModelNew>(mainView) {

    private lateinit var productLayout: FrameLayout
    private lateinit var productImage: ImageUnify
    private lateinit var productPrice: Typography
    private lateinit var productName: Typography
    private lateinit var productTag: Typography
    private lateinit var productNameSection: LinearLayout
    private lateinit var rating: Typography
    private lateinit var label: Label
    private lateinit var soldInfo: Typography
    private lateinit var freeShipping: ImageUnify

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
        label.showWithCondition(item.isDiscount)
        productTag.showWithCondition(item.isDiscount)
        if (item.isDiscount) {
            productTag.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text = item.originalPriceFmt
            }
            label.text = item.discountFmt
        }
        freeShipping.showWithCondition(item.isFreeShipping)
        if (item.isFreeShipping) {
            freeShipping.setImageUrl(item.freeShippingURL)
        }
        productImage.setImageUrl(item.imgUrl)
        productName.text = item.text
        productNameSection.setOnClickListener(
            getItemClickNavigationListener(
                listener,
                item.positionInFeed,
                item.product,
                adapterPosition
            )
        )
        rating.text = item.rating.toString()
        val soldInfoText = getString(R.string.feed_common_terjual) + " " + item.totalSold.toString()
        soldInfo.text = soldInfoText

        productLayout.setOnClickListener(
            getItemClickNavigationListener(
                listener,
                item.positionInFeed,
                item.product,
                adapterPosition
            )
        )
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
    }
}