package com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewholder

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils.FORMAT_YYYY_MM_DD
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BROAD_POSITIVE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BROAD_TYPE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.EXACT_POSITIVE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.SPECIFIC_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.AD_TYPE_PRODUCT_ADS
import com.tokopedia.topads.common.data.internal.ParamObject.AD_TYPE_SHOP_ADS
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.TopAdsDeletedAdsResponse
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewmodel.DeletedGroupItemsItemModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

private const val FORMAT_DD_MMM_YYYY = "dd MMM yyyy"

class DeletedGroupItemsItemViewHolder(
    val view: View,
) : DeletedGroupItemsViewHolder<DeletedGroupItemsItemModel>(view) {

    private val deletedProductImg: ImageUnify = view.findViewById(R.id.deletedProductImg)
    private val deletedProductName: Typography = view.findViewById(R.id.deletedProductName)
    private val date: Typography = view.findViewById(R.id.date)
    private val tampilCount: Typography = view.findViewById(R.id.tampilCount)
    private val pengeluaranCount: Typography = view.findViewById(R.id.pengeluaranCount)
    private val klikCount: Typography = view.findViewById(R.id.klikCount)
    private val pendapatanCount: Typography = view.findViewById(R.id.pendapatanCount)
    private val persentaseKlikCount: Typography = view.findViewById(R.id.persentaseKlikCount)
    private val produkTerjualCount: Typography = view.findViewById(R.id.produkTerjualCount)
    private val keywordTag: Typography = view.findViewById(R.id.keywordTag)
    private val keywordTypeLabel: Label = view.findViewById(R.id.keywordTypeLabel)
    private val iconRp: ImageUnify = view.findViewById(R.id.iconRp)
    private val keywordPriceBid: Typography = view.findViewById(R.id.keywordPriceBid)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_deleted_group_card
    }

    override fun bind(item: DeletedGroupItemsItemModel) {
        when (item.topAdsDeletedAdsItem.adType) {
            AD_TYPE_SHOP_ADS -> loadForHeadlineTab(item.topAdsDeletedAdsItem)
            AD_TYPE_PRODUCT_ADS -> loadForProductTab(item.topAdsDeletedAdsItem)
            else -> loadForProductTab(item.topAdsDeletedAdsItem)
        }
        date.text = DateFormatUtils.formatDate(
            FORMAT_YYYY_MM_DD,
            FORMAT_DD_MMM_YYYY,
            item.topAdsDeletedAdsItem.adDeletedTime
        )
        tampilCount.text = item.topAdsDeletedAdsItem.statTotalImpression
        pengeluaranCount.text = item.topAdsDeletedAdsItem.statTotalSpent
        klikCount.text = item.topAdsDeletedAdsItem.statAvgClick
        pendapatanCount.text = item.topAdsDeletedAdsItem.statTotalGrossProfit
        persentaseKlikCount.text = item.topAdsDeletedAdsItem.statTotalCtr
        produkTerjualCount.text = item.topAdsDeletedAdsItem.statTotalConversion

    }

    private fun loadForProductTab(topAdsDeletedAdsItem: TopAdsDeletedAdsResponse.TopadsDeletedAds.TopAdsDeletedAdsItem) {
        keywordTag.hide()
        keywordTypeLabel.hide()
        iconRp.hide()
        keywordPriceBid.hide()
        loadImage(topAdsDeletedAdsItem.productImageUri)
        deletedProductName.text = topAdsDeletedAdsItem.adTitle
        deletedProductName.show()
        deletedProductImg.show()
    }

    private fun loadForHeadlineTab(topAdsDeletedAdsItem: TopAdsDeletedAdsResponse.TopadsDeletedAds.TopAdsDeletedAdsItem) {
        deletedProductName.hide()
        deletedProductImg.hide()
        setKeyword(topAdsDeletedAdsItem.keywordTag)
        setLabel(topAdsDeletedAdsItem.searchType)
        setKeywordPriceBid(topAdsDeletedAdsItem.priceBid)

    }

    private fun setKeywordPriceBid(priceBid: String) {
        keywordPriceBid.text = getPriceBid(priceBid)
        iconRp.show()
        keywordPriceBid.show()
    }

    private fun setKeyword(keyword: String) {
        keywordTag.text = keyword
        keywordTag.show()
    }

    private fun setLabel(searchType: Int) {
        val label =
            if (searchType == BROAD_POSITIVE) BROAD_TYPE else if (searchType == EXACT_POSITIVE) SPECIFIC_TYPE else ""
        if (label.isNotEmpty()) {
            keywordTypeLabel.setLabel(label)
            keywordTypeLabel.show()
        } else {
            keywordTypeLabel.hide()
        }
    }

    private fun getPriceBid(priceBid: String): CharSequence? {
        val spannableString = SpannableString(priceBid)
        val colorSpan = ForegroundColorSpan(
            ContextCompat.getColor(
                view.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950
            )
        )
        spannableString.setSpan(colorSpan, 0, priceBid.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return SpannableStringBuilder().append(spannableString)
            .append(view.context.getString(com.tokopedia.topads.common.R.string.topads_common_headline_klik))

    }

    private fun loadImage(productImageUri: String) {
        if (productImageUri.isNotEmpty()) {
            deletedProductImg.loadImage(productImageUri)
        } else {
            deletedProductImg.loadImage(R.drawable.product_image_empty)
        }
    }
}
