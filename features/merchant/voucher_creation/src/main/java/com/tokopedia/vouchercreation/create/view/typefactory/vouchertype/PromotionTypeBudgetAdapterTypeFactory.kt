package com.tokopedia.vouchercreation.create.view.typefactory.vouchertype

import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.widget.PromotionTypeInputUiModel
import com.tokopedia.vouchercreation.create.view.viewholder.NextButtonViewHolder
import com.tokopedia.vouchercreation.create.view.viewholder.vouchertype.widget.PromotionTypeInputViewHolder

class PromotionTypeBudgetAdapterTypeFactory(private val fragment: Fragment,
                                            private val onNextStep: (VoucherImageType, Int, Int) -> Unit,
                                            private val onShouldChangeBannerValue: (VoucherImageType) -> Unit = {},
                                            private val getVoucherReviewUiModel: () -> VoucherReviewUiModel? = { null })
    : BaseAdapterTypeFactory(), PromotionTypeBudgetTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            PromotionTypeInputViewHolder.LAYOUT -> PromotionTypeInputViewHolder(parent, fragment, onNextStep, onShouldChangeBannerValue, getVoucherReviewUiModel)
            NextButtonViewHolder.LAYOUT -> NextButtonViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(promotionTypeInputUiModel: PromotionTypeInputUiModel): Int = PromotionTypeInputViewHolder.LAYOUT
}