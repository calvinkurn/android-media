package com.tokopedia.feedcomponent.view.adapter.viewholder.posttag

import android.graphics.Paint
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModelNew
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

private const val RATING_FORMAT = 20.0

class ProductPostTagViewHolderNew(
    val mainView: View,
    val listener: DynamicPostViewHolder.DynamicPostListener
) : AbstractViewHolder<ProductPostTagViewModelNew>(mainView) {

    private lateinit var productLayout: FrameLayout
    private lateinit var productImage: ImageUnify
    private lateinit var productPrice: Typography
    private lateinit var productName: Typography
    private lateinit var productTag: Typography
    private lateinit var productNameSection: LinearLayout
    private lateinit var rating: Typography
    private lateinit var label: Label
    private lateinit var soldInfo: Typography
    private lateinit var freeShipping: ImageView
    private lateinit var divider: View
    private lateinit var star: IconUnify
    private lateinit var menuBtn: IconUnify
    private lateinit var card: CardUnify
    private val topAdsUrlHitter: TopAdsUrlHitter by lazy {
        TopAdsUrlHitter(mainView.context)
    }

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
        divider = itemView.findViewById(R.id.divider)
        star = itemView.findViewById(R.id.star)
        menuBtn = itemView.findViewById(R.id.menu)
        card = itemView.findViewById(R.id.container)
        label.showWithCondition(item.isDiscount)
        productTag.showWithCondition(item.isDiscount)
        if (item.isDiscount) {
            productTag.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text = item.originalPriceFmt
            }
            label.text = item.discountFmt
            productPrice.text = item.priceDiscountFmt

        } else {
            productPrice.text = item.priceFmt
        }
        freeShipping.showWithCondition(item.isFreeShipping)
        if (item.isFreeShipping) {
            freeShipping.loadImage(item.freeShippingURL)
        }
        productImage.setImageUrl(item.imgUrl)
        productName.text = item.text

        card.setOnClickListener {
            getItemClickNavigationListener(
                    listener,
                    item.positionInFeed,
                    item.product,
                    adapterPosition
            )
            topAdsUrlHitter.hitClickUrl(
                    this::class.java.simpleName,
                    item.adClickUrl,
                    "",
                    "",
                    "",
                    ""
            )
        }
        menuBtn.setOnClickListener {
            listener.onBottomSheetMenuClicked(item, mainView.context)
        }
        rating.text = String.format("%.1f", item.rating.toDouble() / RATING_FORMAT)
        val soldInfoText = getString(R.string.feed_common_terjual) + " " + item.totalSold.toString()
        soldInfo.text = soldInfoText
        star.showWithCondition(item.rating != 0)
        divider.showWithCondition(item.rating != 0 && item.totalSold != 0)
        rating.showWithCondition(item.rating != 0)
        soldInfo.showWithCondition(item.totalSold != 0)
    }

    private fun getItemClickNavigationListener(
        listener: DynamicPostViewHolder.DynamicPostListener,
        positionInFeed: Int,
        item: FeedXProduct, itemPosition: Int
    ): View.OnClickListener {
        return View.OnClickListener {
            listener.onPostTagItemBSClick(positionInFeed, item.appLink, item, itemPosition+1)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_producttag_list_new
    }
}