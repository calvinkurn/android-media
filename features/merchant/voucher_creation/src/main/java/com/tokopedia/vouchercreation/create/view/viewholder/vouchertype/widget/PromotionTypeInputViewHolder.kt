package com.tokopedia.vouchercreation.create.view.viewholder.vouchertype.widget

import android.os.Handler
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.adapter.vouchertype.PromotionTypeAdapter
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.fragment.vouchertype.CashbackVoucherCreateFragment
import com.tokopedia.vouchercreation.create.view.fragment.vouchertype.FreeDeliveryVoucherCreateFragment
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.widget.PromotionTypeInputUiModel
import kotlinx.android.synthetic.main.mvc_type_budget_promotion_widget.view.*

class PromotionTypeInputViewHolder(itemView: View,
                                   private val fragment: Fragment,
                                   private val onNextStep: (VoucherImageType, Int, Int) -> Unit,
                                   private val onShouldChangeBannerValue: (VoucherImageType) -> Unit,
                                   private val getVoucherReviewData: () -> VoucherReviewUiModel?)
    : AbstractViewHolder<PromotionTypeInputUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_type_budget_promotion_widget

        private const val FREE_DELIVERY_TYPE_FRAGMENT_KEY = 0
        private const val CASHBACK_TYPE_FRAGMENT_KEY = 1
    }

    private val freeDeliveryVoucherCreateFragment by lazy {
        FreeDeliveryVoucherCreateFragment.createInstance(onNextStep, onShouldChangeBannerValue, itemView.context, getVoucherReviewData)
    }

    private val cashbackVoucherCreateFragment by lazy {
        CashbackVoucherCreateFragment.createInstance(onNextStep, onShouldChangeBannerValue, itemView.context, getVoucherReviewData)
    }

    private val promotionTypeFragmentHashMap by lazy {
        LinkedHashMap<Int, BaseListFragment<*,*>>().apply {
            put(FREE_DELIVERY_TYPE_FRAGMENT_KEY, freeDeliveryVoucherCreateFragment)
            put(CASHBACK_TYPE_FRAGMENT_KEY, cashbackVoucherCreateFragment)
        }
    }

    private val promotionTypeAdapter by lazy {
        PromotionTypeAdapter(
                fragment,
                promotionTypeFragmentHashMap.values.toList())
    }

    override fun bind(element: PromotionTypeInputUiModel) {
        itemView.run {
            typeBudgetPromotionViewPager?.let { viewPager ->
                viewPager.isUserInputEnabled = false
                viewPager.adapter = promotionTypeAdapter
                typeBudgetPromotionContentSwitcher?.setOnCheckedChangeListener { _, isChecked ->
                    Handler().post {
                        viewPager.setCurrentItem(
                                if (isChecked) {
                                    CASHBACK_TYPE_FRAGMENT_KEY
                                } else {
                                    FREE_DELIVERY_TYPE_FRAGMENT_KEY
                                },
                                true
                        )
                    }
                }
                typeBudgetPromotionContentSwitcher?.isChecked = element.isChecked
            }
        }
    }
}