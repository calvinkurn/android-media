package com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotResponse
import com.tokopedia.ordermanagement.snapshot.util.SnapshotConsts.CREATED_TIME
import com.tokopedia.ordermanagement.snapshot.util.SnapshotUtils
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotAdapter
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotImageViewPagerAdapter
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by fwidjaja on 1/28/21.
 */
class SnapshotItemViewHolder(itemView: View) : SnapshotAdapter.BaseViewHolder<SnapshotResponse.Data.GetOrderSnapshot>(itemView)  {
    @SuppressLint("SetTextI18n")
    override fun bind(item: SnapshotResponse.Data.GetOrderSnapshot, position: Int) {
        // header
        val productImages = item.productImageSecondary
        if (productImages.isNotEmpty()) {
            val ivHeader = itemView.findViewById<ImageUnify>(R.id.snapshot_main_img)
            val viewPager2 = itemView.findViewById<ViewPager2>(R.id.snapshot_header_view_pager)
            val indicator = itemView.findViewById<PageControl>(R.id.snapshot_page_indicator)

            if (productImages.size > 1) {
                ivHeader.gone()
                viewPager2.visible()
                indicator.visible()
                val imgViewPagerAdapter = SnapshotImageViewPagerAdapter()

                val arrayListImg = arrayListOf<String>()
                productImages.forEach {
                    val productImg = it as SnapshotResponse.Data.GetOrderSnapshot.ProductImageSecondaryItem
                    arrayListImg.add(productImg.imageUrl)
                }
                imgViewPagerAdapter.listImg = arrayListImg
                viewPager2.adapter = imgViewPagerAdapter

            } else {
                viewPager2.gone()
                indicator.gone()
                ivHeader.visible()
                productImages.firstOrNull()?.imageUrl?.let { ivHeader.loadImageWithoutPlaceholder(it) }
            }
        }

        // info
        val tickerInfo = itemView.findViewById<Ticker>(R.id.ticker_info)
        SnapshotUtils.parseDate(item.orderDetail.createTime)?.let { itemView.context.getString(R.string.snapshot_ticker_info).replace(CREATED_TIME, it) }?.let { tickerInfo.setTextDescription(it) }

        val productPrice = itemView.findViewById<Typography>(R.id.snapshot_main_price)
        productPrice.text = "Rp${item.orderDetail.normalPrice}"

        val discLabel = itemView.findViewById<Typography>(R.id.snapshot_label_disc)
        discLabel.text = item.campaignData.campaign.discountPercentageText

        val hargaCoret = itemView.findViewById<Typography>(R.id.snapshot_harga_coret)
        hargaCoret.text = item.campaignData.campaign.originalPrice

        val productName = itemView.findViewById<Typography>(R.id.snapshot_product_name)
        productName.text = item.orderDetail.productName

        val shopLogo = itemView.findViewById<ImageUnify>(R.id.snapshot_shop_logo)
        ImageHandler.loadImageCircle2(itemView.context, shopLogo, item.shopImagePrimaryUrl)

        val drawable = when {
            item.isOs -> {
                androidx.core.content.ContextCompat.getDrawable(itemView.context, R.drawable.ic_snapshot_official_store_product)
            }
            item.isPm -> {
                androidx.core.content.ContextCompat.getDrawable(itemView.context, R.drawable.ic_snapshot_power_merchant)
            }
            else -> {
                null
            }
        }

        val shopBadge = itemView.findViewById<ImageUnify>(R.id.snapshot_shop_badge)
        if (drawable == null) shopBadge.gone()
        else {
            shopBadge.setImageDrawable(drawable)
            shopBadge.visible()
        }

        val shopName = itemView.findViewById<Typography>(R.id.snapshot_shop_name)
        shopName.text = item.shopSummary.shopName

        val condition = when {
            item.orderDetail.condition == 1 -> {
                itemView.context.getString(R.string.snapshot_condition_baru)
            }
            item.orderDetail.condition == 0 -> {
                itemView.context.getString(R.string.snapshot_condition_bekas)
            }
            else -> {
                null
            }
        }
        val kondisiLabel = itemView.findViewById<ImageUnify>(R.id.snapshot_kondisi_label)
        val kondisiValue = itemView.findViewById<ImageUnify>(R.id.snapshot_kondisi_value)
        val kondisiDivider = itemView.findViewById<View>(R.id.divider_kondisi)
        if (condition == null) {
            kondisiLabel.gone()
            kondisiValue.gone()
            kondisiDivider.gone()
        }

    }
}