package com.tokopedia.review.feature.reputationhistory.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R
import com.tokopedia.review.feature.reputationhistory.view.helper.DateUtilHelper
import com.tokopedia.unifyprinciples.Typography

class SellerReputationEmptyViewHolder(view: View, private val fragment: Fragment?) :
    AbstractViewHolder<EmptyModel>(view) {

    companion object {
        val LAYOUT = R.layout.deisgn_retry_reputation
    }

    private var dateUtilHelper: DateUtilHelper? = null

    private val imgReputationRetry = itemView.findViewById<ImageView>(R.id.img_reputation_retry)
    private val tvReputationInfo = itemView.findViewById<Typography>(R.id.good_job_reputation_retry)
    private val tvDescriptionReputationRetry =
        itemView.findViewById<Typography>(R.id.description_reputation_retry)
    private val containerReputationChangeDate =
        itemView.findViewById<LinearLayout>(R.id.reputation_container_change_date)

    override fun bind(element: EmptyModel?) {
        with(itemView) {
            imgReputationRetry.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_penalti_reputasi_zero
                )
            )
            tvReputationInfo.text =
                context.getString(R.string.reputation_history_label_congrats_no_penalty)
            tvDescriptionReputationRetry.text =
                context.getString(R.string.reputation_history_label_improve_selling_get_badge)

            containerReputationChangeDate.setOnClickListener {
                if (fragment != null) {
                    dateUtilHelper = DateUtilHelper()
                    dateUtilHelper?.onClick(fragment)
                }
            }
        }
    }
}