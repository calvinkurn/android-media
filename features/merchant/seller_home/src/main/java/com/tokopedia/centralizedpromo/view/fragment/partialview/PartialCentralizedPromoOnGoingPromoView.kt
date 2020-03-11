package com.tokopedia.centralizedpromo.view.fragment.partialview

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralizedpromo.view.fragment.CoachMarkListener
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoListUiModel
import com.tokopedia.centralizedpromo.view.model.OnGoingPromoUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_on_going_promo.view.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_on_going_promo_error.view.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_on_going_promo_shimmering.view.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_on_going_promo_success.view.*

class PartialCentralizedPromoOnGoingPromoView(
        private val refreshButtonClickListener: RefreshButtonClickListener,
        view: View,
        adapterTypeFactory: CentralizedPromoAdapterTypeFactory,
        coachMarkListener: CoachMarkListener,
        shouldWaitForCoachMark: Boolean
) : BasePartialListView<OnGoingPromoListUiModel, CentralizedPromoAdapterTypeFactory, OnGoingPromoUiModel>(view, adapterTypeFactory, coachMarkListener, shouldWaitForCoachMark) {

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
        tvOnGoingPromo.text = data.title
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

    override fun getSuccessView(): ConstraintLayout? = view.layoutCentralizedPromoOnGoingPromoSuccess

    private fun setupOnGoingPromo() = with(view) {
        rvCentralizedPromoOnGoingPromo.apply {
            adapter = this@PartialCentralizedPromoOnGoingPromoView.adapter
        }
    }

    interface RefreshButtonClickListener {
        fun onRefreshButtonClicked()
    }
}