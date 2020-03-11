package com.tokopedia.centralizedpromo.view.fragment.partialview

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralizedpromo.view.fragment.CoachMarkListener
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.centralizedpromo.view.viewholder.PromoCreationViewHolder
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.centralized_promo_partial_promo_creation.view.*
import kotlinx.android.synthetic.main.centralized_promo_partial_creation_shimmering.view.*

class PartialCentralizedPromoCreationView(
        view: View,
        adapterTypeFactory: CentralizedPromoAdapterTypeFactory,
        coachMarkListener: CoachMarkListener,
        shouldWaitForCoachMark: Boolean
) : BasePartialListView<PromoCreationListUiModel, CentralizedPromoAdapterTypeFactory, PromoCreationUiModel>(view, adapterTypeFactory, coachMarkListener, shouldWaitForCoachMark) {

    init {
        setupPromoRecommendation()
    }

    private fun setupPromoRecommendation() = with(view) {
        rvCentralizedPromoCreation.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@PartialCentralizedPromoCreationView.adapter
            isNestedScrollingEnabled = false
            addItemDecoration(PromoCreationViewHolder.ItemDecoration(context.dpToPx(4).toInt()))
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

    override fun onRecyclerViewItemEmpty() {}

    override fun bindSuccessData(data: PromoCreationListUiModel) {}
}