package com.tokopedia.search.result.presentation.view.adapter.viewholder.shop

import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintSet
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import kotlinx.android.synthetic.main.search_result_shop_header_layout.view.*

class ShopHeaderViewHolder(
        itemView: View,
        val bannerAdsListener: BannerAdsListener?
): AbstractViewHolder<ShopHeaderViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_result_shop_header_layout
    }

    private val context = itemView.context

    init {
        itemView.adsBannerView?.setTopAdsBannerClickListener { position, applink, data ->
            bannerAdsListener?.onBannerAdsClicked(position, applink, data)
        }

        itemView.adsBannerView?.setTopAdsImpressionListener(createTopAdsItemImpressionListener())
    }

    private fun createTopAdsItemImpressionListener(): TopAdsItemImpressionListener {
        return object : TopAdsItemImpressionListener() {
            override fun onImpressionHeadlineAdsItem(position: Int, data: CpmData) {
                bannerAdsListener?.onBannerAdsImpressionListener(position, data)
            }
        }
    }

    override fun bind(shopHeaderViewModel: ShopHeaderViewModel?) {
        if(shopHeaderViewModel == null) return

        initCpmModel(shopHeaderViewModel)
        initTextViewShopTotalCount(shopHeaderViewModel)
    }

    private fun initCpmModel(shopHeaderViewModel: ShopHeaderViewModel) {
        itemView.adsBannerView?.shouldShowWithAction(shopHeaderViewModel.cpmModel.data.size > 0) {
            itemView.adsBannerView?.displayAds(shopHeaderViewModel.cpmModel)
        }
    }

    private fun initTextViewShopTotalCount(shopHeaderViewModel: ShopHeaderViewModel) {
        val isShopCountTextVisible = shopHeaderViewModel.totalShopCount > 0

        initTextViewShopCountQuery(shopHeaderViewModel, isShopCountTextVisible)
        initTextViewShopCountFoundIn(isShopCountTextVisible)
        initTextViewShopCount(shopHeaderViewModel, isShopCountTextVisible)
        initTextViewShopCountShop(isShopCountTextVisible)
    }

    private fun initTextViewShopCountQuery(shopHeaderViewModel: ShopHeaderViewModel, isShopCountTextVisible: Boolean) {
        itemView.textViewShopCountQuery?.let { textViewShopCountQuery ->
            textViewShopCountQuery.shouldShowWithAction(isShopCountTextVisible) {
                setTextViewShopCountQueryText(textViewShopCountQuery, shopHeaderViewModel)
                setTextViewShopCountMargins(textViewShopCountQuery)
            }
        }
    }

    private fun setTextViewShopCountQueryText(textViewShopCountQuery: TextView, shopHeaderViewModel: ShopHeaderViewModel) {
        textViewShopCountQuery.text = getQueryWithQuotes(shopHeaderViewModel.query)
    }

    private fun getQueryWithQuotes(query: String): String {
        return "\"$query\""
    }

    private fun initTextViewShopCountFoundIn(isShopCountTextVisible: Boolean) {
        itemView.textViewShopCountFoundIn?.let { textViewShopCountFoundIn ->
            textViewShopCountFoundIn.shouldShowWithAction(isShopCountTextVisible) {
                setTextViewShopCountMargins(textViewShopCountFoundIn)
            }
        }
    }

    private fun initTextViewShopCount(shopHeaderViewModel: ShopHeaderViewModel, isShopCountTextVisible: Boolean) {
        itemView.textViewShopCount?.let { textViewShopCount ->
            textViewShopCount.shouldShowWithAction(isShopCountTextVisible) {
                setTextViewShopCountText(textViewShopCount, shopHeaderViewModel)
                setTextViewShopCountMargins(textViewShopCount)
            }
        }
    }

    private fun setTextViewShopCountText(textViewShopCount: TextView, shopHeaderViewModel: ShopHeaderViewModel) {
        textViewShopCount.text = shopHeaderViewModel.totalShopCount.toString()
    }

    private fun initTextViewShopCountShop(isShopCountTextVisible: Boolean) {
        itemView.textViewShopCountShop.let { textViewShopCountShop ->
            textViewShopCountShop.shouldShowWithAction(isShopCountTextVisible) {
                setTextViewShopCountMargins(textViewShopCountShop)
            }
        }
    }

    private fun setTextViewShopCountMargins(textViewShopCount: View) {
        itemView.constraintLayoutShopHeader?.let {
            val constraintSet = ConstraintSet()

            constraintSet.clone(it)

            val marginPixel = context.resources.getDimensionPixelSize(getTextViewShopCountMarginTop())
            constraintSet.setMargin(textViewShopCount.id, ConstraintSet.TOP, marginPixel)

            constraintSet.applyTo(it)
        }
    }

    @DimenRes
    private fun getTextViewShopCountMarginTop(): Int {
        return if (isAdsBannerViewVisible()) R.dimen.dp_0 else R.dimen.dp_16
    }

    private fun isAdsBannerViewVisible(): Boolean {
        return itemView.adsBannerView != null
                && itemView.adsBannerView.isVisible
                && itemView.adsBannerView.childCount > 0
    }
}