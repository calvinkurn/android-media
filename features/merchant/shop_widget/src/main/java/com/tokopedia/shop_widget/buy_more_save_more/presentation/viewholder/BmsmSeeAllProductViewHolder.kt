package com.tokopedia.shop_widget.buy_more_save_more.presentation.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetItemEventListener
import com.tokopedia.shop_widget.buy_more_save_more.util.Constant
import com.tokopedia.shop_widget.databinding.ItemBmsmWidgetSeeAllBinding
import com.tokopedia.utils.view.binding.viewBinding

class BmsmSeeAllProductViewHolder(
    itemView: View,
    private val listener: BmsmWidgetItemEventListener,
    private val isOverrideTheme: Boolean
) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemBmsmWidgetSeeAllBinding? by viewBinding()

    fun bind(element: OfferingProductListUiModel.Product) {
        binding?.apply {
            val remainingProductCount = element.totalProduct - Constant.productListShownSize
            tpgRemainingProductCount.text = "$remainingProductCount+"
            cardSeeAll.apply {
                setCardHeightMatchParent()
                setOnClickListener {
                    listener.onNavigateToOlp()
                }
            }
            if (isOverrideTheme) configReimagine()
        }
    }

    private fun CardView.setCardHeightMatchParent() {
        val layoutParams = this.layoutParams
        layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        this.layoutParams = layoutParams
    }

    private fun configReimagine() {
        binding?.apply {
            cardSeeAll.setCardBackgroundColor(MethodChecker.getColor(itemView.context, R.color.dms_static_white))
            tpgRemainingProductCount.setTextColor(MethodChecker.getColor(itemView.context, R.color.dms_static_black))
            tpgOtherProductLabel.setTextColor(MethodChecker.getColor(itemView.context, R.color.dms_static_black))
        }
    }
}
