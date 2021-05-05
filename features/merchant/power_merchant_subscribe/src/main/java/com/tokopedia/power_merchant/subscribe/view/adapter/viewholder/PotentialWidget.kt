package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.power_merchant.subscribe.R
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
    }

    private val benefitList by getBenefitList()

    override fun bind(element: WidgetPotentialUiModel) {
        with(itemView) {
            rvPmPotential.layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            rvPmPotential.adapter = PotentialAdapter(benefitList)

            tvPmPotentialDescription.text = getString(R.string.pm_registration_potential_description, PMConstant.POWER_MERCHANT_CHARGING)
        }
    }

    private fun getBenefitList(): Lazy<List<PotentialItemUiModel>> {
        return lazy {
            listOf(
                    PotentialItemUiModel(
                            resDrawableIcon = R.drawable.ic_pm_benefit_01,
                            description = getString(R.string.pm_potential_benefit_01)
                    ),
                    PotentialItemUiModel(
                            resDrawableIcon = R.drawable.ic_pm_benefit_02,
                            description = getString(R.string.pm_potential_benefit_02)
                    ),
                    PotentialItemUiModel(
                            resDrawableIcon = R.drawable.ic_pm_benefit_03,
                            description = getString(R.string.pm_potential_benefit_03)
                    )
            )
        }
    }
}