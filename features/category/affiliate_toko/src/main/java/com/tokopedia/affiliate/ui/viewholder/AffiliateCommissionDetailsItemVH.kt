package com.tokopedia.affiliate.ui.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliate.bodyTypoMap
import com.tokopedia.affiliate.headerTypoMap
import com.tokopedia.affiliate.interfaces.AffiliateInfoClickInterfaces
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateCommissionItemModel
import com.tokopedia.affiliate.utils.DateUtils
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
        const val TYPE_BOLD = "bold"
        const val TYPE_REGULER = "regular"
        const val TYPE_DATE_TIME = "datetime"

    }
    private val header: Typography = itemView.findViewById(R.id.transaction_komisi_header)
    private val infoIcon: IconUnify = itemView.findViewById(R.id.transaction_komisi_icon)
    private val valueTv: Typography = itemView.findViewById(R.id.transaction_komisi)
    override fun bind(element: AffiliateCommissionItemModel?) {
        setType(element)
        setTextType(element)
        setDetailTitle(element)
        setDetailDescription(element)
        setTooltilView(element)
        setClickLitener(element)
    }

    private fun setType(element: AffiliateCommissionItemModel?) {
        if(element?.data?.detailType == TYPE_DATE_TIME){
            element.data?.detailDescription?.let { date ->
                element.data?.detailDescription = DateUtils().formatDate(dateString = date)
            }
        }
    }

    private fun setClickLitener(element: AffiliateCommissionItemModel?) {
        infoIcon.setOnClickListener {
            affiliateInfoClickInterfaces?.onInfoClick(element?.data?.detailTitle,element?.data?.detailTooltip,element?.data?.advanceTooltip)
        }
    }

    private fun setTooltilView(element: AffiliateCommissionItemModel?) {
        if(element?.data?.detailTooltip?.isNotEmpty() == true || element?.data?.advanceTooltip?.isNotEmpty() == true){
            infoIcon.show()
        }else{
            infoIcon.hide()
        }
    }

    private fun setDetailDescription(element: AffiliateCommissionItemModel?) {
        element?.data?.detailDescription?.let { desc ->
            if (desc.isEmpty())
                valueTv.hide()
            else {
                valueTv.show()
                valueTv.text = desc
            }
        }
    }

    private fun setDetailTitle(element: AffiliateCommissionItemModel?) {
        element?.data?.detailTitle?.let { title ->
            header.text = title
        }
    }

    private fun setTextType(element: AffiliateCommissionItemModel?) {
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
    }

}
