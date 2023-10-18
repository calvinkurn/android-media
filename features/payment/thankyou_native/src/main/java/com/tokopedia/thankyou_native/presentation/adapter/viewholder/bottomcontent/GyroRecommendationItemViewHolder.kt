package com.tokopedia.thankyou_native.presentation.adapter.viewholder.bottomcontent

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankBottomContentGyroRecomBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendationWidgetModel
import com.tokopedia.thankyou_native.presentation.views.RegisterMemberShipListener
import com.tokopedia.utils.view.binding.viewBinding

class GyroRecommendationItemViewHolder(
    view: View?,
    private val registerMemberShipListener: RegisterMemberShipListener
): AbstractViewHolder<GyroRecommendationWidgetModel>(view) {

    private var binding: ThankBottomContentGyroRecomBinding? by viewBinding()

    companion object {
        val LAYOUT_ID = R.layout.thank_bottom_content_gyro_recom
    }

    override fun bind(data: GyroRecommendationWidgetModel?) {
        if (data == null) return

        val gyroRecomView = binding?.gyroRecomView
        if (!data.gyroRecommendation.gyroVisitable.isNullOrEmpty()) {
            gyroRecomView?.visible()
            gyroRecomView?.listener = registerMemberShipListener
            gyroRecomView?.addData(
                data.gyroRecommendation,
                data.thanksPageData,
                data.gyroRecommendationAnalytics,
            )
        } else {
            gyroRecomView?.gone()
        }
    }
}
