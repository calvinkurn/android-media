package com.tokopedia.centralizedpromo.view.fragment.partialview

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralizedpromo.view.fragment.CoachMarkListener
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoListUiModel
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoUiModel
import com.tokopedia.centralizedpromo.view.viewholder.OnGoingPromoViewHolder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.CentralizedPromoPartialOnGoingPromoBinding
import com.tokopedia.sellerhome.databinding.FragmentCentralizedPromoBinding

class PartialCentralizedPromoOnGoingPromoView(
    private val refreshButtonClickListener: RefreshButtonClickListener,
    binding: FragmentCentralizedPromoBinding,
    adapterTypeFactory: CentralizedPromoAdapterTypeFactory,
    coachMarkListener: CoachMarkListener,
    showCoachMark: Boolean
) : BasePartialListView<OnGoingPromoListUiModel, CentralizedPromoAdapterTypeFactory, OnGoingPromoUiModel>(
    binding,
    adapterTypeFactory,
    coachMarkListener,
    showCoachMark
) {

    private val ongoingPromoBinding = CentralizedPromoPartialOnGoingPromoBinding.bind(binding.root)

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

    override fun bindSuccessData(data: OnGoingPromoListUiModel) = with(ongoingPromoBinding) {
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
                this@PartialCentralizedPromoOnGoingPromoView.adapter.apply { setHasStableIds(true) }
            addItemDecoration(
                OnGoingPromoViewHolder.ItemDecoration(
                    resources.getDimension(R.dimen.sah_dimen_10dp).toInt()
                )
            )
        }
    }

    interface RefreshButtonClickListener {
        fun onRefreshButtonClicked()
    }
}