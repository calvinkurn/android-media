package com.tokopedia.feedcomponent.view.adapter.viewholder.statistic

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticDetailType
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticDetailUiModel
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction

/**
 * Created by jegul on 2019-11-25
 */
class PostStatisticViewHolder(itemView: View, private val onSeeMoreDetail: (PostStatisticDetailType) -> Unit) : BaseViewHolder(itemView) {

    private val ivIcon by lazy { itemView.findViewById<ImageView>(R.id.iv_icon) }
    private val tvCount by lazy { itemView.findViewById<TextView>(R.id.tv_count) }
    private val tvSubtitle by lazy { itemView.findViewById<TextView>(R.id.tv_subtitle) }
    private val tvAction by lazy { itemView.findViewById<TextView>(R.id.tv_action) }

    private val context: Context
        get() = itemView.context


    fun bind(item: PostStatisticDetailUiModel) {
        ivIcon.setImageDrawable(
                MethodChecker.getDrawable(context, item.iconRes)
        )

        tvCount.text = item.amountString
        tvSubtitle.text = getString(item.subtitleRes)

        tvAction.shouldShowWithAction(item.shouldShowDetail) {
            tvAction.setOnClickListener { onSeeMoreDetail(item.type) }
        }
    }
}