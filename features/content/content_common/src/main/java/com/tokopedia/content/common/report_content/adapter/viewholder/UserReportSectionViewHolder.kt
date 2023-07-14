package com.tokopedia.content.common.report_content.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.content.common.R
import com.tokopedia.content.common.report_content.model.PlayUserReportSectionType
import com.tokopedia.content.common.report_content.model.PlayUserReportSection
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by astidhiyaa on 16/12/21
 */
class UserReportSectionViewHolder(itemView: View) : BaseViewHolder(itemView) {
    private val tvTitle = itemView.findViewById<Typography>(R.id.tv_user_report_section)

    fun bind(item: PlayUserReportSection) {
        tvTitle.apply {
            text = if(item.isUrl) MethodChecker.fromHtml(getString(item.title)) else getString(item.title)

            setWeight(
                if(item.type == PlayUserReportSectionType.Header){
                    Typography.BOLD
                }else{
                    Typography.REGULAR
                }
            )
            setType(
                if(item.type == PlayUserReportSectionType.Header){
                    Typography.BODY_1
                }else{
                    Typography.BODY_3
                }
            )
        }
        itemView.setOnClickListener { item.onClick(item) }
    }
}
