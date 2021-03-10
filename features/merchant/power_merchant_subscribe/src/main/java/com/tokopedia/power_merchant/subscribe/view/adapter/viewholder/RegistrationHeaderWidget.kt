package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.constant.KYCStatusId
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.adapter.RegistrationTermAdapter
import com.tokopedia.power_merchant.subscribe.view.model.WidgetRegistrationHeaderUiModel
import kotlinx.android.synthetic.main.widget_pm_registration_header.view.*

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class RegistrationHeaderWidget(itemView: View) : AbstractViewHolder<WidgetRegistrationHeaderUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_registration_header
    }

    override fun bind(element: WidgetRegistrationHeaderUiModel) {
        setupView(element)
        setupTermsList(element)
    }

    private fun setupView(element: WidgetRegistrationHeaderUiModel) {
        with(itemView) {
            viewPmHeaderBackground.setBackgroundResource(R.drawable.bg_pm_registration_header)
            imgPmHeaderImage.loadImageDrawable(R.drawable.img_pm_registration_header)
            tvPmHeaderNewSellerLabel.visibility = if (element.shopInfo.isNewSeller) View.VISIBLE else View.GONE
            tickerPmHeader.visibility = if (element.shopInfo.kycStatusId == KYCStatusId.PENDING && element.shopInfo.isEligibleShopScore) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun setupTermsList(element: WidgetRegistrationHeaderUiModel) {
        with(itemView.rvPmRegistrationTerm) {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = RegistrationTermAdapter(element.terms)
        }
    }
}