package com.tokopedia.search.result.shop.presentation.viewholder

import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintSet
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.result.shop.presentation.model.ShopTotalCountViewModel
import kotlinx.android.synthetic.main.search_result_shop_total_count_layout.view.*

internal class ShopTotalCountViewHolder(itemView: View): AbstractViewHolder<ShopTotalCountViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_result_shop_total_count_layout
    }

    private val context = itemView.context
    private var isAdsBannerViewVisible = false

    override fun bind(shopTotalCountViewModel: ShopTotalCountViewModel?) {
        if (shopTotalCountViewModel == null) return

        isAdsBannerViewVisible = shopTotalCountViewModel.isAdsBannerVisible

        initTextViewShopTotalCount(shopTotalCountViewModel)
    }

    private fun initTextViewShopTotalCount(shopTotalCountViewModel: ShopTotalCountViewModel) {
        val isShopCountTextVisible = shopTotalCountViewModel.totalShopCount > 0

        initTextViewShopCountQuery(shopTotalCountViewModel, isShopCountTextVisible)
        initTextViewShopCountFoundIn(isShopCountTextVisible)
        initTextViewShopCount(shopTotalCountViewModel, isShopCountTextVisible)
        initTextViewShopCountShop(isShopCountTextVisible)
    }

    private fun initTextViewShopCountQuery(shopTotalCountViewModel: ShopTotalCountViewModel, isShopCountTextVisible: Boolean) {
        itemView.textViewShopCountQuery?.let { textViewShopCountQuery ->
            textViewShopCountQuery.shouldShowWithAction(isShopCountTextVisible) {
                setTextViewShopCountQueryText(textViewShopCountQuery, shopTotalCountViewModel)
                setTextViewShopCountMargins(textViewShopCountQuery)
            }
        }
    }

    private fun setTextViewShopCountQueryText(textViewShopCountQuery: TextView, shopTotalCountViewModel: ShopTotalCountViewModel) {
        textViewShopCountQuery.text = getQueryWithQuotes(shopTotalCountViewModel.query)
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

    private fun initTextViewShopCount(shopTotalCountViewModel: ShopTotalCountViewModel, isShopCountTextVisible: Boolean) {
        itemView.textViewShopCount?.let { textViewShopCount ->
            textViewShopCount.shouldShowWithAction(isShopCountTextVisible) {
                setTextViewShopCountText(textViewShopCount, shopTotalCountViewModel)
                setTextViewShopCountMargins(textViewShopCount)
            }
        }
    }

    private fun setTextViewShopCountText(textViewShopCount: TextView, shopTotalCountViewModel: ShopTotalCountViewModel) {
        textViewShopCount.text = shopTotalCountViewModel.totalShopCount.toString()
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
        return if (isAdsBannerViewVisible) com.tokopedia.design.R.dimen.dp_0 else com.tokopedia.design.R.dimen.dp_16
    }
}