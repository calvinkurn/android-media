package com.tokopedia.centralizedpromoold.view.fragment.partialview

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.centralizedpromoold.view.adapter.CentralizedPromoAdapterTypeFactoryOld
import com.tokopedia.centralizedpromoold.view.fragment.CoachMarkListener
import com.tokopedia.centralizedpromoold.view.model.PromoCreationListUiModelOld
import com.tokopedia.centralizedpromoold.view.model.PromoCreationUiModelOld
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.CentralizedPromoPartialPromoCreationOldBinding
import com.tokopedia.sellerhome.databinding.FragmentCentralizedPromoOldBinding

class PartialCentralizedPromoCreationViewOld(
        binding: FragmentCentralizedPromoOldBinding,
        adapterTypeFactory: CentralizedPromoAdapterTypeFactoryOld,
        coachMarkListener: CoachMarkListener,
        showCoachMark: Boolean
) : BasePartialListViewOld<PromoCreationListUiModelOld, CentralizedPromoAdapterTypeFactoryOld, PromoCreationUiModelOld>(
    binding,
    adapterTypeFactory,
    coachMarkListener,
    showCoachMark
) {

    companion object {
        const val SPAN_COUNT: Int = 2
    }

    private val promoCreationBinding = CentralizedPromoPartialPromoCreationOldBinding.bind(binding.root)

    init {
        setupPromoRecommendation()
    }

    private fun setupPromoRecommendation() = with(promoCreationBinding) {
        rvCentralizedPromoCreation.apply {
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
            adapter =
                this@PartialCentralizedPromoCreationViewOld.adapter.apply { setHasStableIds(true) }
            isNestedScrollingEnabled = false
        }
    }

    override fun renderLoading() = with(promoCreationBinding) {
        partialSuccess.layoutCentralizedPromoCreationSuccess.hide()
        rvCentralizedPromoCreation.hide()
        centralizedPromoCreationShimmering.layoutCentralizedPromoCreationShimmering.show()
    }

    override fun onRecyclerViewResultDispatched() = with(promoCreationBinding) {
        partialSuccess.layoutCentralizedPromoCreationSuccess.show()
        rvCentralizedPromoCreation.show()
        centralizedPromoCreationShimmering.layoutCentralizedPromoCreationShimmering.hide()
    }

    override fun shouldShowCoachMark(): Boolean =
        showCoachMark && promoCreationBinding.partialSuccess.layoutCentralizedPromoCreationSuccess.isShown

    override fun getCoachMarkItem() = with(promoCreationBinding) {
        promoCreationBinding.partialSuccess.layoutCentralizedPromoCreationSuccess.setBackgroundColor(
            ContextCompat.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        )
        CoachMarkItem(
            promoCreationBinding.partialSuccess.layoutCentralizedPromoCreationSuccess,
            root.context.getString(R.string.sh_coachmark_title_promo_creation),
            root.context.getString(R.string.sh_coachmark_desc_promo_creation)
        )
    }

    override fun onRecyclerViewItemEmpty() {}

    override fun bindSuccessData(data: PromoCreationListUiModelOld) {}
}