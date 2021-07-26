package com.tokopedia.centralizedpromo.view.fragment.partialview

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralizedpromo.view.fragment.CoachMarkListener
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoListUiModel
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoUiModel
import com.tokopedia.centralizedpromo.view.viewholder.OnGoingPromoViewHolder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R
import kotlinx.android.synthetic.main.centralized_promo_partial_on_going_promo.view.*
import kotlinx.android.synthetic.main.centralized_promo_partial_on_going_promo_error.view.*
import kotlinx.android.synthetic.main.centralized_promo_partial_on_going_promo_shimmering.view.*
import kotlinx.android.synthetic.main.centralized_promo_partial_on_going_promo_success.view.*

class PartialCentralizedPromoOnGoingPromoView(
        private val refreshButtonClickListener: RefreshButtonClickListener,
        view: View,
        adapterTypeFactory: CentralizedPromoAdapterTypeFactory,
        coachMarkListener: CoachMarkListener,
        showCoachMark: Boolean
) : BasePartialListView<OnGoingPromoListUiModel, CentralizedPromoAdapterTypeFactory, OnGoingPromoUiModel>(view, adapterTypeFactory, coachMarkListener, showCoachMark) {

    init {
        setupOnGoingPromo()
    }

    override fun renderError(cause: Throwable) {
        with(view) {
            show()
            layoutCentralizedPromoOnGoingPromoSuccess.hide()
            layoutCentralizedPromoOnGoingPromoShimmering.hide()
            if (stubLayoutCentralizedPromoOnGoingPromoError?.parent != null) {
                stubLayoutCentralizedPromoOnGoingPromoError.inflate()
            }
            layoutCentralizedPromoOnGoingPromoError?.show()
            onGoingPromoLayoutDivider.show()

            localLoadOnGoingPromo.progressState = false
            tvCentralizedPromoOnGoingTitleError.text = context.getString(R.string.sah_label_promo_and_ads)
            localLoadOnGoingPromo.description?.text = context.getString(R.string.sah_label_on_going_promotion_retry)
            localLoadOnGoingPromo.refreshBtn?.setOnClickListener {
                localLoadOnGoingPromo.progressState = true
                refreshButtonClickListener.onRefreshButtonClicked()
            }

            val errorMessage = if (cause is MessageErrorException && !cause.message.isNullOrBlank()) {
                cause.message
            } else {
                context.getString(R.string.sah_label_on_going_promotion_error)
            }

            localLoadOnGoingPromo.title?.text = errorMessage
        }
        super.renderError(cause)
    }

    override fun renderLoading() = with(view) {
        show()
        layoutCentralizedPromoOnGoingPromoSuccess.hide()
        layoutCentralizedPromoOnGoingPromoError?.hide()
        layoutCentralizedPromoOnGoingPromoShimmering.show()
        onGoingPromoLayoutDivider.show()
    }

    override fun bindSuccessData(data: OnGoingPromoListUiModel) = with(view) {
        tvOnGoingPromo.text = context.getString(R.string.sah_label_promo_and_ads)
    }

    override fun onRecyclerViewItemEmpty() = with(view) {
        layoutCentralizedPromoOnGoingPromoSuccess.hide()
        layoutCentralizedPromoOnGoingPromoShimmering.hide()
        layoutCentralizedPromoOnGoingPromoError?.hide()
        onGoingPromoLayoutDivider.hide()
        localLoadOnGoingPromo?.progressState = false
    }

    override fun onRecyclerViewResultDispatched() = with(view) {
        layoutCentralizedPromoOnGoingPromoSuccess.show()
        onGoingPromoLayoutDivider.show()
        layoutCentralizedPromoOnGoingPromoShimmering.hide()
        layoutCentralizedPromoOnGoingPromoError?.hide()
        localLoadOnGoingPromo?.progressState = false
    }

    override fun shouldShowCoachMark(): Boolean = showCoachMark && view.layoutCentralizedPromoOnGoingPromoSuccess.isShown

    override fun getCoachMarkItem() = with(view) {
        layoutCentralizedPromoOnGoingPromoSuccess.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        CoachMarkItem(layoutCentralizedPromoOnGoingPromoSuccess,
                context.getString(R.string.sh_coachmark_title_on_going_promo),
                context.getString(R.string.sh_coachmark_desc_on_going_promo))
    }

    private fun setupOnGoingPromo() = with(view) {
        rvCentralizedPromoOnGoingPromo.apply {
            adapter = this@PartialCentralizedPromoOnGoingPromoView.adapter.apply { setHasStableIds(true) }
            addItemDecoration(OnGoingPromoViewHolder.ItemDecoration(resources.getDimension(R.dimen.sah_dimen_10dp).toInt()))
        }
    }

    interface RefreshButtonClickListener {
        fun onRefreshButtonClicked()
    }
}