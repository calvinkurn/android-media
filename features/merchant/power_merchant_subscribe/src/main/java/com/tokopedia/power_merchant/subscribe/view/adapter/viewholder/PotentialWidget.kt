package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.view.adapter.PotentialAdapter
import com.tokopedia.power_merchant.subscribe.view.model.PotentialItemUiModel
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPotentialUiModel
import kotlinx.android.synthetic.main.widget_pm_potential.view.*

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class PotentialWidget(itemView: View) : AbstractViewHolder<WidgetPotentialUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_potential
        private const val OLD_POWER_MERCHANT_CHARGING = "1%"
    }

    private val benefitList by getBenefitList()

    override fun bind(element: WidgetPotentialUiModel) {
        with(itemView) {
            rvPmPotential.layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            rvPmPotential.adapter = PotentialAdapter(benefitList)

            tvPmPotentialDescription.text = context.getString(R.string.pm_registration_potential_description, Constant.POWER_MERCHANT_CHARGING, OLD_POWER_MERCHANT_CHARGING).parseAsHtml()
        }
    }

    private fun getBenefitList(): Lazy<List<PotentialItemUiModel>> {
        return lazy {
            listOf(
                    PotentialItemUiModel(
                            imgUrl = PMConstant.Images.PM_POTENTIAL_BENEFIT_01,
                            description = getString(R.string.pm_potential_benefit_01)
                    ),
                    PotentialItemUiModel(
                            imgUrl = PMConstant.Images.PM_POTENTIAL_BENEFIT_02,
                            description = getString(R.string.pm_potential_benefit_02)
                    ),
                    PotentialItemUiModel(
                            imgUrl = PMConstant.Images.PM_POTENTIAL_BENEFIT_03,
                            description = getString(R.string.pm_potential_benefit_03)
                    )
            )
        }
    }
}