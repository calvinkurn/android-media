package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.bodyTypoMap
import com.tokopedia.affiliate.headerTypoMap
import com.tokopedia.affiliate.interfaces.AffiliateInfoClickInterfaces
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateCommissionItemModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class AffiliateCommissionDetailsItemVH(
    itemView: View,
    private val affiliateInfoClickInterfaces: AffiliateInfoClickInterfaces?
) :
    AbstractViewHolder<AffiliateCommissionItemModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.affiliate_commison_detail_item
        val TYPE_BOLD = "bold"
        val TYPE_REGULER = "regular"

    }

    override fun bind(element: AffiliateCommissionItemModel?) {
        val header = itemView.findViewById<Typography>(R.id.transaction_komisi_header)
        val infoIcon = itemView.findViewById<IconUnify>(R.id.transaction_komisi_icon)
        val valueTv = itemView.findViewById<Typography>(R.id.transaction_komisi)



        element?.data?.textType?.let { type ->
            when (type) {
                TYPE_BOLD -> {
                    element.data?.textSize?.let { size ->
                        headerTypoMap[size]?.let { it ->
                            header.setType(it)
                            valueTv.setType(it)
                        }
                    }
                    header.setWeight(Typography.BOLD)
                    valueTv.setWeight(Typography.BOLD)
                    header.setTextColor(
                        MethodChecker.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN950
                        )
                    )
                    valueTv.setTextColor(
                        MethodChecker.getColor(
                            itemView.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_NN950
                        )
                    )
                }
                TYPE_REGULER -> {
                    element.data?.textSize?.let { size ->
                        bodyTypoMap[size]?.let {
                            header.setType(it)
                            valueTv.setType(it)
                        }
                    }
                    header.setWeight(Typography.REGULAR)
                    valueTv.setWeight(Typography.REGULAR)
                }
            }
        }
        element?.data?.detailTitle?.let { title ->
            header.text = title
        }
        element?.data?.detailDescription?.let { desc ->
            if (desc.isEmpty())
                valueTv.hide()
            else {
                valueTv.show()
                valueTv.text = desc
            }
        }
        element?.data?.detailTooltip?.let {
            if (it.isEmpty())
                infoIcon.hide()
            else {
                infoIcon.show()
            }
        }
        infoIcon.setOnClickListener {
            affiliateInfoClickInterfaces?.onInfoClick(element?.data?.detailTitle,element?.data?.detailTooltip)
        }
    }

}
