package com.tokopedia.gopay.kyc.presentation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gopay.kyc.domain.data.GoPayPlusBenefit
import com.tokopedia.gopay.kyc.presentation.viewholder.GoPayBenefitTitleViewHolder
import com.tokopedia.gopay.kyc.presentation.viewholder.GoPayBenefitItemViewHolder
import com.tokopedia.gopay.kyc.utils.ViewType

class GoPayKycBenefitFactory : BaseAdapterTypeFactory(), ViewType {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        when (type) {
            GoPayBenefitTitleViewHolder.LAYOUT_ID -> {
                return GoPayBenefitTitleViewHolder(parent)
            }
            GoPayBenefitItemViewHolder.LAYOUT_ID -> {
                return GoPayBenefitItemViewHolder(parent)
            }
            else -> {
                viewHolder = super.createViewHolder(parent, type)
            }
        }
        return viewHolder
    }

    override fun type(dataModel: GoPayPlusBenefit): Int {
        return GoPayBenefitItemViewHolder.LAYOUT_ID
    }

    override fun type(dataModel: EmptyModel): Int {
        return GoPayBenefitTitleViewHolder.LAYOUT_ID
    }

}