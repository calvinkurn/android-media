package com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.data.model.GetOrderSnapshot
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotTypeData
import com.tokopedia.ordermanagement.snapshot.util.SnapshotConsts.CREATED_TIME
import com.tokopedia.ordermanagement.snapshot.util.SnapshotConsts.KONDISI_BARU
import com.tokopedia.ordermanagement.snapshot.util.SnapshotConsts.KONDISI_BEKAS
import com.tokopedia.ordermanagement.snapshot.util.SnapshotUtils
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotAdapter
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotImageViewPagerAdapter
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.PageControl
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by fwidjaja on 1/28/21.
 */
class SnapshotContentViewHolder(itemView: View, private val actionListener: SnapshotAdapter.ActionListener?) : SnapshotAdapter.BaseViewHolder<SnapshotTypeData>(itemView)  {

    companion object {
        private const val SLASHED_PRICE_WITHOUT_DISCOUNT_MARGIN_START = 16
        private const val SLASHED_PRICE_WITH_DISCOUNT_MARGIN_START = 8
        private const val SLASHED_PRICE_MARGIN_END = 16
        private const val SLASHED_PRICE_WITHOUT_DISCOUNT_MARGIN_TOP = 4
        private const val SLASHED_PRICE_WITH_DISCOUNT_MARGIN_TOP = 0
    }

    val ivHeader = itemView.findViewById<ImageView>(R.id.snapshot_main_img)
    val viewPager2 = itemView.findViewById<ViewPager2>(R.id.snapshot_header_view_pager)
    val indicator = itemView.findViewById<PageControl>(R.id.snapshot_page_indicator)
    val tickerInfo = itemView.findViewById<Ticker>(R.id.ticker_info)
    val productPrice = itemView.findViewById<Typography>(R.id.snapshot_main_price)
    val discLabel = itemView.findViewById<Label>(R.id.snapshot_label_disc)
    val productName = itemView.findViewById<Typography>(R.id.snapshot_product_name)
    val shopBadge = itemView.findViewById<ImageView>(R.id.snapshot_shop_badge)
    val clShop = itemView.findViewById<ConstraintLayout>(R.id.cl_shop)
    val hargaCoret = itemView.findViewById<Typography>(R.id.snapshot_harga_coret)
    val shopLogo = itemView.findViewById<ImageView>(R.id.snapshot_shop_logo)
    val kondisiLabel = itemView.findViewById<Typography>(R.id.snapshot_kondisi_label)
    val kondisiValue = itemView.findViewById<Typography>(R.id.snapshot_kondisi_value)
    val dividerKondisi = itemView.findViewById<View>(R.id.divider_kondisi)
    val beratLabel = itemView.findViewById<Typography>(R.id.snapshot_detail_berat_label)
    val beratValue = itemView.findViewById<Typography>(R.id.snapshot_berat_value)
    val dividerBerat = itemView.findViewById<View>(R.id.divider_berat)
    val shopName = itemView.findViewById<Typography>(R.id.snapshot_shop_name)
    val labelPo = itemView.findViewById<Typography>(R.id.snapshot_po_label)
    val valuePo = itemView.findViewById<Typography>(R.id.snapshot_po_value)
    val dividerPo = itemView.findViewById<View>(R.id.divider_po)
    val minOrderLabel = itemView.findViewById<Typography>(R.id.snapshot_min_order_label)
    val minOrderValue = itemView.findViewById<Typography>(R.id.snapshot_min_order_value)
    val dividerMinOrder = itemView.findViewById<View>(R.id.divider_min_order)
    val desc = itemView.findViewById<Typography>(R.id.snapshot_desc)
    val productBundlingInfoLayout = itemView.findViewById<ConstraintLayout>(R.id.snapshot_bundling_info)
    val productBundlingNameText = itemView.findViewById<Typography>(R.id.snapshot_bundling_name)
    val productBundlingIconImage = itemView.findViewById<ImageUnify>(R.id.snapshot_bundling_icon)

    init {
        measureScreenHeight()
    }

    @SuppressLint("SetTextI18n")
    override fun bind(item: SnapshotTypeData, position: Int) {
        if (item.dataObject is GetOrderSnapshot) {
            renderHeader(item.dataObject)
            renderInfo(item.dataObject)
            renderShop(item.dataObject)
            renderDesc(item.dataObject)
        }

    }

    private fun measureScreenHeight() = with(itemView) {
        val screenWidth = itemView.resources.displayMetrics.widthPixels
        ivHeader.layoutParams.height = screenWidth
        viewPager2.layoutParams.height = screenWidth
    }

    private fun renderHeader(dataObject: GetOrderSnapshot) {
        val productImages = dataObject.productImageSecondary
        if (productImages.isNotEmpty()) {
            if (productImages.size > 1) {
                ivHeader.gone()
                viewPager2.visible()
                indicator.apply {
                    visible()
                    setIndicator(productImages.size)
                    activeColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                    inactiveColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN600_68)
                }
                val imgViewPagerAdapter = SnapshotImageViewPagerAdapter()

                val arrayListImg = arrayListOf<String>()
                productImages.forEach {
                    val productImg = it
                    arrayListImg.add(productImg.imageUrl)
                }
                imgViewPagerAdapter.listImg = arrayListImg
                actionListener?.let { imgViewPagerAdapter.setActionListener(it) }
                viewPager2.apply {
                    adapter = imgViewPagerAdapter
                    setOnClickListener {
                        actionListener?.onSnapshotImgClicked(adapterPosition)
                    }
                    registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                            indicator.setCurrentIndicator(position)
                        }

                    })
                }

            } else {
                viewPager2.gone()
                indicator.gone()
                ivHeader.visible()
                ivHeader.setOnClickListener {
                    actionListener?.onSnapshotImgClicked(adapterPosition)
                }
                productImages.firstOrNull()?.imageUrl?.let {
                    ivHeader.loadImage(it)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderInfo(dataObject: GetOrderSnapshot) {
        SnapshotUtils.parseDate(dataObject.orderDetail.createTime)?.let {
            itemView.context.getString(R.string.snapshot_ticker_info).replace(CREATED_TIME, it) }?.let {
            tickerInfo.setHtmlDescription(it) }

        renderBundleInfo(dataObject)

        productPrice.text = dataObject.productAdditionalData.productPrice

        val discPercentage = dataObject.campaignData.campaign.discountPercentageText
        if (discPercentage.isEmpty()) {
            discLabel.gone()
        } else {
            discLabel.visible()
            discLabel.text = dataObject.campaignData.campaign.discountPercentageText
        }

        val originalPrice = dataObject.campaignData.campaign.originalPrice
        if (originalPrice.isEmpty()) {
            hargaCoret.gone()
        } else {
            hargaCoret.visible()
            hargaCoret.text = dataObject.campaignData.campaign.originalPrice
            hargaCoret.paintFlags = hargaCoret.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            hargaCoret.setMargin(
                left = if (discPercentage.isEmpty()) {
                    SLASHED_PRICE_WITHOUT_DISCOUNT_MARGIN_START.toPx()
                } else {
                    SLASHED_PRICE_WITH_DISCOUNT_MARGIN_START.toPx()
                },
                top = if (discPercentage.isEmpty()) {
                    SLASHED_PRICE_WITHOUT_DISCOUNT_MARGIN_TOP.toPx()
                } else {
                    SLASHED_PRICE_WITH_DISCOUNT_MARGIN_TOP.toPx()
                },
                right = SLASHED_PRICE_MARGIN_END.toPx(),
                bottom = Int.ZERO
            )
        }

        productName.text = MethodChecker.fromHtmlPreserveLineBreak(dataObject.orderDetail.productName)
    }

    @SuppressLint("SetTextI18n")
    private fun renderShop(dataObject: GetOrderSnapshot) {
        shopLogo.loadImageCircle(dataObject.shopImagePrimaryUrl)

        if (dataObject.shopSummary.badgeUrl.isBlank()) shopBadge.gone()
        else {
            shopBadge.loadImage(dataObject.shopSummary.badgeUrl)
            shopBadge.visible()
        }

        shopName.text = MethodChecker.fromHtml(dataObject.shopSummary.shopName)

        val condition = when {
            dataObject.orderDetail.condition == KONDISI_BARU -> {
                itemView.context.getString(R.string.snapshot_condition_baru)
            }
            dataObject.orderDetail.condition == KONDISI_BEKAS -> {
                itemView.context.getString(R.string.snapshot_condition_bekas)
            }
            else -> {
                null
            }
        }

        clShop?.setOnClickListener {
            actionListener?.onSnapshotShopClicked(dataObject.shopSummary.shopId)
        }

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

        val berat = dataObject.productWeightFormatted
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

        if (dataObject.preOrder) {
            if (dataObject.preOrderDuration.isNotEmpty()) {
                labelPo.visible()
                valuePo.visible()
                valuePo.text = dataObject.preOrderDuration
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

        val minOrder = dataObject.orderDetail.minOrder

        if (minOrder.toInt() > 0) {
            minOrderLabel.visible()
            minOrderValue.visible()
            minOrderValue.text = "$minOrder pcs"
            dividerMinOrder.visible()

        } else {
            minOrderLabel.gone()
            minOrderValue.gone()
            dividerMinOrder.gone()
        }
    }

    private fun renderDesc(dataObject: GetOrderSnapshot) {
        val productDesc = dataObject.orderDetail.productDesc
        if (productDesc.isNotEmpty()) {
            desc.visible()
            desc.text = MethodChecker.fromHtmlPreserveLineBreak(productDesc)
        } else {
            desc.gone()
        }
    }

    private fun renderBundleInfo(dataObject: GetOrderSnapshot) {
        productBundlingInfoLayout?.showWithCondition(dataObject.isBundleProduct)
        productBundlingIconImage?.setImageUrl(dataObject.bundleIcon)
        productBundlingNameText?.text = dataObject.bundleName
    }

}