package com.tokopedia.centralizedpromo.view.fragment.partialview

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralizedpromo.view.fragment.CoachMarkListener
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R
import kotlinx.android.synthetic.main.centralized_promo_partial_creation_shimmering.view.*
import kotlinx.android.synthetic.main.centralized_promo_partial_promo_creation.view.*

class PartialCentralizedPromoCreationView(
        view: View,
        adapterTypeFactory: CentralizedPromoAdapterTypeFactory,
        coachMarkListener: CoachMarkListener,
        showCoachMark: Boolean
) : BasePartialListView<PromoCreationListUiModel, CentralizedPromoAdapterTypeFactory, PromoCreationUiModel>(view, adapterTypeFactory, coachMarkListener, showCoachMark) {

    companion object {
        const val SPAN_COUNT: Int = 2
    }

    init {
        setupPromoRecommendation()
    }

    private fun setupPromoRecommendation() = with(view) {
        rvCentralizedPromoCreation.apply {
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
            adapter = this@PartialCentralizedPromoCreationView.adapter.apply { setHasStableIds(true) }
            isNestedScrollingEnabled = false
        }
    }

    override fun renderLoading() = with(view) {
        partialSuccess.hide()
        rvCentralizedPromoCreation.hide()
        layoutCentralizedPromoCreationShimmering.show()
    }

    override fun onRecyclerViewResultDispatched() = with(view) {
        partialSuccess.show()
        rvCentralizedPromoCreation.show()
        layoutCentralizedPromoCreationShimmering.hide()
    }

    override fun shouldShowCoachMark(): Boolean = showCoachMark && view.partialSuccess.isShown

    override fun getCoachMarkItem() = with(view) {
        partialSuccess.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        CoachMarkItem(partialSuccess,
                context.getString(R.string.sh_coachmark_title_promo_creation),
                context.getString(R.string.sh_coachmark_desc_promo_creation))
    }

    override fun onRecyclerViewItemEmpty() {}

    override fun bindSuccessData(data: PromoCreationListUiModel) {}
}