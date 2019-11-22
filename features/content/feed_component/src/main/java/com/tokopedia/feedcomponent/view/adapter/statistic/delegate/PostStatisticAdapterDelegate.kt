package com.tokopedia.feedcomponent.view.adapter.statistic.delegate

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.helper.BaseViewHolder
import com.tokopedia.feedcomponent.helper.TypedAdapterDelegate
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticUiModel
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction

/**
 * Created by jegul on 2019-11-22
 */
class PostStatisticAdapterDelegate : TypedAdapterDelegate<PostStatisticUiModel, PostStatisticUiModel, PostStatisticAdapterDelegate.PostStatisticViewHolder>(R.layout.item_post_statistic) {

    override fun onBindViewHolder(item: PostStatisticUiModel, holder: PostStatisticViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PostStatisticViewHolder {
        return PostStatisticViewHolder(basicView)
    }

    inner class PostStatisticViewHolder(itemView: View) : BaseViewHolder(itemView) {

        private val ivIcon by lazy { itemView.findViewById<ImageView>(R.id.iv_icon) }
        private val tvCount by lazy { itemView.findViewById<TextView>(R.id.tv_count) }
        private val tvSubtitle by lazy { itemView.findViewById<TextView>(R.id.tv_subtitle) }
        private val tvAction by lazy { itemView.findViewById<TextView>(R.id.tv_action) }

        private val context: Context
            get() = itemView.context


        fun bind(item: PostStatisticUiModel) {
            ivIcon.setImageDrawable(
                    MethodChecker.getDrawable(context, item.iconRes)
            )

            tvCount.text = item.amountString
            tvSubtitle.text = getString(item.subtitleRes)

            tvAction.shouldShowWithAction(item.actionTitle != null) {
                tvAction.text = item.actionTitle
            }
        }
    }
}