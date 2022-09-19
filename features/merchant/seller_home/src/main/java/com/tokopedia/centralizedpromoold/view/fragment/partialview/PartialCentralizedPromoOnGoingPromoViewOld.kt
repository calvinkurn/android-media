package com.tokopedia.centralizedpromoold.view.fragment.partialview

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.centralizedpromoold.view.adapter.CentralizedPromoAdapterTypeFactoryOld
import com.tokopedia.centralizedpromoold.view.fragment.CoachMarkListener
import com.tokopedia.centralizedpromoold.view.model.OnGoingPromoListUiModelOld
import com.tokopedia.centralizedpromoold.view.model.OnGoingPromoUiModelOld
import com.tokopedia.centralizedpromoold.view.viewholder.OnGoingPromoViewHolderOld
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.CentralizedPromoPartialOnGoingPromoOldBinding
import com.tokopedia.sellerhome.databinding.FragmentCentralizedPromoOldBinding

class PartialCentralizedPromoOnGoingPromoViewOld(
        private val refreshButtonClickListener: RefreshButtonClickListener,
        binding: FragmentCentralizedPromoOldBinding,
        adapterTypeFactory: CentralizedPromoAdapterTypeFactoryOld,
        coachMarkListener: CoachMarkListener,
        showCoachMark: Boolean
) : BasePartialListViewOld<OnGoingPromoListUiModelOld, CentralizedPromoAdapterTypeFactoryOld, OnGoingPromoUiModelOld>(
    binding,
    adapterTypeFactory,
    coachMarkListener,
    showCoachMark
) {

    private val ongoingPromoBinding = CentralizedPromoPartialOnGoingPromoOldBinding.bind(binding.root)

    init {
        setupOnGoingPromo()
    }

    override fun renderError(cause: Throwable) {
        with(ongoingPromoBinding) {
            root.show()
            layoutOnGoingPromoSuccess.layoutCentralizedPromoOnGoingPromoSuccess.hide()
            layoutOnGoingPromoShimmer.layoutCentralizedPromoOnGoingPromoShimmering.hide()
            layoutOnGoingPromoError.layoutCentralizedPromoOnGoingPromoError.show()
            onGoingPromoLayoutDivider.show()

            layoutOnGoingPromoError.localLoadOnGoingPromo.progressState = false
            layoutOnGoingPromoError.tvCentralizedPromoOnGoingTitleError.text =
                root.context.getString(R.string.sah_label_promo_and_ads)
            layoutOnGoingPromoError.localLoadOnGoingPromo.description?.text =
                root.context.getString(R.string.sah_label_on_going_promotion_retry)
            layoutOnGoingPromoError.localLoadOnGoingPromo.refreshBtn?.setOnClickListener {
                layoutOnGoingPromoError.localLoadOnGoingPromo.progressState = true
                refreshButtonClickListener.onRefreshButtonClicked()
            }

            val errorMessage =
                if (cause is MessageErrorException && !cause.message.isNullOrBlank()) {
                    cause.message
                } else {
                    root.context.getString(R.string.sah_label_on_going_promotion_error)
                }

            layoutOnGoingPromoError.localLoadOnGoingPromo.title?.text = errorMessage
        }
        super.renderError(cause)
    }

    override fun renderLoading() = with(ongoingPromoBinding) {
        root.show()
        layoutOnGoingPromoSuccess.layoutCentralizedPromoOnGoingPromoSuccess.hide()
        layoutOnGoingPromoError.layoutCentralizedPromoOnGoingPromoError.hide()
        layoutOnGoingPromoShimmer.layoutCentralizedPromoOnGoingPromoShimmering.show()
        onGoingPromoLayoutDivider.show()
    }

    override fun bindSuccessData(data: OnGoingPromoListUiModelOld) = with(ongoingPromoBinding) {
        layoutOnGoingPromoSuccess.tvOnGoingPromo.text =
            root.context.getString(R.string.sah_label_promo_and_ads)
    }

    override fun onRecyclerViewItemEmpty() = with(ongoingPromoBinding) {
        layoutOnGoingPromoSuccess.layoutCentralizedPromoOnGoingPromoSuccess.hide()
        layoutOnGoingPromoShimmer.layoutCentralizedPromoOnGoingPromoShimmering.hide()
        layoutOnGoingPromoError.layoutCentralizedPromoOnGoingPromoError.hide()
        onGoingPromoLayoutDivider.hide()
        layoutOnGoingPromoError.localLoadOnGoingPromo.progressState = false
    }

    override fun onRecyclerViewResultDispatched() = with(ongoingPromoBinding) {
        layoutOnGoingPromoSuccess.layoutCentralizedPromoOnGoingPromoSuccess.show()
        onGoingPromoLayoutDivider.show()
        layoutOnGoingPromoShimmer.layoutCentralizedPromoOnGoingPromoShimmering.hide()
        layoutOnGoingPromoError.layoutCentralizedPromoOnGoingPromoError.hide()
        layoutOnGoingPromoError.localLoadOnGoingPromo.progressState = false
    }

    override fun shouldShowCoachMark(): Boolean =
        showCoachMark && ongoingPromoBinding.layoutOnGoingPromoSuccess.layoutCentralizedPromoOnGoingPromoSuccess.isShown

    override fun getCoachMarkItem() = with(ongoingPromoBinding) {
        layoutOnGoingPromoSuccess.layoutCentralizedPromoOnGoingPromoSuccess.setBackgroundColor(
            root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N0)
        )
        CoachMarkItem(
            layoutOnGoingPromoSuccess.layoutCentralizedPromoOnGoingPromoSuccess,
            root.context.getString(R.string.sh_coachmark_title_on_going_promo),
            root.context.getString(R.string.sh_coachmark_desc_on_going_promo)
        )
    }

    private fun setupOnGoingPromo() = with(ongoingPromoBinding) {
        layoutOnGoingPromoSuccess.rvCentralizedPromoOnGoingPromo.apply {
            adapter =
                this@PartialCentralizedPromoOnGoingPromoViewOld.adapter.apply { setHasStableIds(true) }
            addItemDecoration(
                OnGoingPromoViewHolderOld.ItemDecoration(
                    resources.getDimension(R.dimen.sah_dimen_10dp).toInt()
                )
            )
        }
    }

    interface RefreshButtonClickListener {
        fun onRefreshButtonClicked()
    }
}