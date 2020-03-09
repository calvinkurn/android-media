package com.tokopedia.centralized_promo.view.fragment.partialview

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.centralized_promo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralized_promo.view.fragment.CoachMarkListener
import com.tokopedia.centralized_promo.view.model.OnGoingPromoListUiModel
import com.tokopedia.centralized_promo.view.model.OnGoingPromoUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_on_going_promo.view.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_on_going_promo_error.view.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_on_going_promo_shimmering.view.*
import kotlinx.android.synthetic.main.sah_partial_centralized_promo_on_going_promo_success.view.*

class PartialCentralizedPromoOnGoingPromoView(
        private val view: View,
        private val refreshButtonClickListener: RefreshButtonClickListener,
        adapterTypeFactory: CentralizedPromoAdapterTypeFactory,
        coachMarkListener: CoachMarkListener,
        shouldWaitForCoachMark: Boolean
) : PartialView<OnGoingPromoListUiModel, CentralizedPromoAdapterTypeFactory, OnGoingPromoUiModel>(adapterTypeFactory, coachMarkListener, shouldWaitForCoachMark) {

    init {
        setupOnGoingPromo()
    }

    override fun renderData(data: OnGoingPromoListUiModel) {
        if (data.errorMessage.isBlank()) {
            with(view) {
                if (data.promotions.isNotEmpty()) {
                    tvOnGoingPromo.text = data.title
                    adapter.setElements(data.promotions)
                } else {
                    layoutCentralizedPromoOnGoingPromoSuccess.hide()
                    layoutCentralizedPromoOnGoingPromoShimmering.hide()
                    layoutCentralizedPromoOnGoingPromoError?.hide()
                    localLoadOnGoingPromo?.progressState = false
                    onGoingPromoLayoutDivider.hide()

                    if (shouldWaitForCoachMark) {
                        shouldWaitForCoachMark = false
                        coachMarkListener.onViewReadyForCoachMark()
                    }
                }
            }
        } else {
            renderError(MessageErrorException(data.errorMessage))
        }
    }

    override fun renderError(cause: Throwable) {
        with(view) {
            shouldWaitForCoachMark = false
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
    }

    override fun onRefresh() {
        with(view) {
            show()
            layoutCentralizedPromoOnGoingPromoSuccess.hide()
            layoutCentralizedPromoOnGoingPromoError?.hide()
            layoutCentralizedPromoOnGoingPromoShimmering.show()
            onGoingPromoLayoutDivider.show()
        }
    }

    override fun onRecyclerViewResultDispatched() {
        with(view) {
            layoutCentralizedPromoOnGoingPromoSuccess.show()
            onGoingPromoLayoutDivider.show()
            layoutCentralizedPromoOnGoingPromoShimmering.hide()
            localLoadOnGoingPromo?.progressState = false
            layoutCentralizedPromoOnGoingPromoError?.hide()
        }
    }

    private fun setupOnGoingPromo() {
        with(view) {
            rvCentralizedPromoOnGoingPromo.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = this@PartialCentralizedPromoOnGoingPromoView.adapter
            }
        }
    }

    interface RefreshButtonClickListener {
        fun onRefreshButtonClicked()
    }
}