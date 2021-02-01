package com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotResponse
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotTypeData
import com.tokopedia.ordermanagement.snapshot.util.SnapshotConsts.CREATED_TIME
import com.tokopedia.ordermanagement.snapshot.util.SnapshotUtils
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotAdapter
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotImageViewPagerAdapter
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by fwidjaja on 1/28/21.
 */
class SnapshotContentViewHolder(itemView: View) : SnapshotAdapter.BaseViewHolder<SnapshotTypeData>(itemView)  {
    @SuppressLint("SetTextI18n")
    override fun bind(item: SnapshotTypeData, position: Int) {
        if (item.dataObject is SnapshotResponse.Data.GetOrderSnapshot) {
            // header
            val productImages = item.dataObject.productImageSecondary
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
                        val productImg = it
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
            SnapshotUtils.parseDate(item.dataObject.orderDetail.createTime)?.let {
                itemView.context.getString(R.string.snapshot_ticker_info).replace(CREATED_TIME, it) }?.let {
                tickerInfo.setHtmlDescription(it) }

            val productPrice = itemView.findViewById<Typography>(R.id.snapshot_main_price)
            productPrice.text = "Rp${item.dataObject.orderDetail.normalPrice}"

            val discLabel = itemView.findViewById<Label>(R.id.snapshot_label_disc)
            discLabel.text = item.dataObject.campaignData.campaign.discountPercentageText

            val hargaCoret = itemView.findViewById<Typography>(R.id.snapshot_harga_coret)
            hargaCoret.text = item.dataObject.campaignData.campaign.originalPrice
            hargaCoret.paintFlags = hargaCoret.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG

            val productName = itemView.findViewById<Typography>(R.id.snapshot_product_name)
            productName.text = item.dataObject.orderDetail.productName

            val shopLogo = itemView.findViewById<ImageUnify>(R.id.snapshot_shop_logo)
            ImageHandler.loadImageCircle2(itemView.context, shopLogo, item.dataObject.shopImagePrimaryUrl)

            val drawable = when {
                item.dataObject.isOs -> {
                    androidx.core.content.ContextCompat.getDrawable(itemView.context, R.drawable.ic_snapshot_official_store_product)
                }
                item.dataObject.isPm -> {
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
            shopName.text = item.dataObject.shopSummary.shopName

            val condition = when {
                item.dataObject.orderDetail.condition == 1 -> {
                    itemView.context.getString(R.string.snapshot_condition_baru)
                }
                item.dataObject.orderDetail.condition == 0 -> {
                    itemView.context.getString(R.string.snapshot_condition_bekas)
                }
                else -> {
                    null
                }
            }
            val kondisiLabel = itemView.findViewById<Typography>(R.id.snapshot_kondisi_label)
            val kondisiValue = itemView.findViewById<Typography>(R.id.snapshot_kondisi_value)
            val dividerKondisi = itemView.findViewById<View>(R.id.divider_kondisi)

            if (condition == null) {
                kondisiLabel.gone()
                kondisiValue.gone()
                dividerKondisi.gone()

            } else {
                kondisiLabel.visible()
                kondisiValue.visible()
                kondisiValue.text = condition
                dividerKondisi.visible()
            }

            val berat = item.dataObject.productTotalWeightFormatted
            val beratLabel = itemView.findViewById<Typography>(R.id.snapshot_detail_berat_label)
            val beratValue = itemView.findViewById<Typography>(R.id.snapshot_berat_value)
            val dividerBerat = itemView.findViewById<View>(R.id.divider_berat)

            if (berat.isNotEmpty()) {
                beratLabel.visible()
                beratValue.visible()
                dividerBerat.visible()
                beratValue.text = berat

            } else {
                beratLabel.gone()
                beratValue.gone()
                dividerBerat.gone()
            }

            val labelPo = itemView.findViewById<Typography>(R.id.snapshot_po_label)
            val valuePo = itemView.findViewById<Typography>(R.id.snapshot_po_value)
            val dividerPo = itemView.findViewById<View>(R.id.divider_po)

            if (item.dataObject.preOrder) {
                if (item.dataObject.preOrderDuration.isNotEmpty()) {
                    labelPo.visible()
                    valuePo.visible()
                    valuePo.text = item.dataObject.preOrderDuration
                    dividerPo.visible()

                } else {
                    labelPo.gone()
                    valuePo.gone()
                    dividerPo.gone()
                }
            } else {
                labelPo.gone()
                valuePo.gone()
                dividerPo.gone()
            }

            val minOrderLabel = itemView.findViewById<Typography>(R.id.snapshot_min_order_label)
            val minOrderValue = itemView.findViewById<Typography>(R.id.snapshot_min_order_value)
            val dividerMinOrder = itemView.findViewById<View>(R.id.divider_min_order)
            val minOrder = item.dataObject.orderDetail.minOrder

            if (minOrder > 0) {
                minOrderLabel.visible()
                minOrderValue.visible()
                minOrderValue.text = "$minOrder pcs"
                dividerMinOrder.visible()

            } else {
                minOrderLabel.gone()
                minOrderValue.gone()
                dividerMinOrder.gone()
            }

            val desc = itemView.findViewById<Typography>(R.id.snapshot_desc)

            val productDesc = item.dataObject.orderDetail.productDesc
            if (productDesc.isNotEmpty()) {
                desc.visible()
                desc.text = item.dataObject.orderDetail.productDesc
            } else {
                desc.gone()
            }
        }
    }
}