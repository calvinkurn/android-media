package com.tokopedia.promocheckout.list.view.adapter

import androidx.core.content.ContextCompat
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.widget.TimerCheckoutWidget
import com.tokopedia.promocheckout.widget.TimerPromoCheckout
import kotlinx.android.synthetic.main.include_period_tnc_promo.view.*
import kotlinx.android.synthetic.main.item_list_promo_checkout.view.*

class PromoCheckoutListViewHolder(val view: View?, val listenerTrackingCoupon: ListenerTrackingCoupon) : AbstractViewHolder<PromoCheckoutListModel>(view) {

    override fun bind(element: PromoCheckoutListModel?) {
        listenerTrackingCoupon.onImpressionCoupon(element)
        var timerUsage = TimerPromoCheckout()
        ImageHandler.loadImageRounded2(view?.context, view?.imageBanner, element?.imageUrlMobile)
        view?.imagePeriod?.setImageResource(R.drawable.ic_promo_rp_new)
        view?.imageMinTrans?.setImageResource(R.drawable.ic_promo_time_new)
        view?.titlePeriod?.text = element?.usage?.text
        view?.titleMinTrans?.text = element?.minimumUsageLabel
        if (TextUtils.isEmpty(element?.minimumUsage)) {
            view?.textMinTrans?.visibility = View.GONE
            if (TextUtils.isEmpty(element?.minimumUsageLabel)) {
                view?.titleMinTrans?.visibility = View.GONE
                view?.imageMinTrans?.visibility = View.GONE
            } else {
                view?.titleMinTrans?.visibility = View.VISIBLE
                view?.imageMinTrans?.visibility = View.VISIBLE
            }
        } else {
            view?.titleMinTrans?.visibility = View.VISIBLE
            view?.imageMinTrans?.visibility = View.VISIBLE
            view?.textMinTrans?.visibility = View.VISIBLE
            view?.textMinTrans?.text = element?.minimumUsage
        }
        setDateUsage(element)
        view?.timerUsage?.visibility = View.GONE
        if ((element?.usage?.activeCountdown ?: 0 > 0 &&
                        element?.usage?.activeCountdown ?: 0 < TimerPromoCheckout.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_ONE_DAY)) {
            timerUsage?.listener = object : TimerPromoCheckout.Listener {
                override fun onTick(l: Long) {
                    element?.usage?.activeCountdown = l.toInt()
                }

                override fun onFinishTick() {
                    setTimerEnabled()
                }

            }
            setActiveTimerUsage(timerUsage, element?.usage?.activeCountdown?.toLong() ?: 0)
        } else if ((element?.usage?.expiredCountdown ?: 0 > 0 &&
                        element?.usage?.expiredCountdown ?: 0 < TimerPromoCheckout.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_ONE_DAY)) {
            view?.timerUsage?.listener = object : TimerCheckoutWidget.Listener {
                override fun onTick(l: Long) {
                    element?.usage?.expiredCountdown = l.toInt()
                }

                override fun onFinishTick() {
                    onExpiryTimerEnded()
                }

            }
            setExpiryTimerUsage(element?.usage?.expiredCountdown?.toLong() ?: 0)
        }
        enableOrDisableImages(element)
    }

    private fun enableOrDisableImages(item: PromoCheckoutListModel?) {
        item?.let { item ->
            if (item.usage != null) {
                if (item.usage?.activeCountdown!! > 0 || item.usage?.expiredCountdown!! <= 0) {
                    setTimerDisabled()
                } else {
                    setTimerEnabled()
                }
            } else {
                setTimerDisabled()
            }
        }
    }

    private fun setTimerDisabled() {
        view?.imagePeriod?.setColorFilter(ContextCompat.getColor(view?.context, com.tokopedia.unifyprinciples.R.color.Unify_N100))
        view?.imageMinTrans?.setColorFilter(ContextCompat.getColor(view?.context, com.tokopedia.unifyprinciples.R.color.Unify_N100))
    }

    private fun setTimerEnabled() {
        view?.imagePeriod?.setColorFilter(ContextCompat.getColor(view?.context, com.tokopedia.unifyprinciples.R.color.Unify_G400))
        view?.imageMinTrans?.setColorFilter(ContextCompat.getColor(view?.context, com.tokopedia.unifyprinciples.R.color.Unify_G400))
    }

    fun setDateUsage(element: PromoCheckoutListModel?) {
        view?.titlePeriod?.visibility = View.VISIBLE
        view?.textPeriod?.visibility = View.VISIBLE
        view?.textPeriod?.text = element?.usage?.usageStr
    }

    private fun setActiveTimerUsage(timerUsage: TimerPromoCheckout, countDown: Long) {
        timerUsage?.cancel()
        timerUsage?.expiredTimer = countDown
        timerUsage?.start()
    }

    fun setExpiryTimerUsage(countDown: Long) {
        view?.timerUsage?.cancel()
        view?.timerUsage?.visibility = View.VISIBLE
        view?.titlePeriod?.visibility = View.GONE
        view?.textPeriod?.visibility = View.GONE
        view?.timerUsage?.expiredTimer = countDown
        view?.timerUsage?.start()
    }

    fun onExpiryTimerEnded() {
        view?.timerUsage?.cancel()
        view?.timerUsage?.visibility = View.GONE
        view?.titlePeriod?.visibility = View.VISIBLE
        view?.textPeriod?.visibility = View.VISIBLE
        setTimerDisabled()
    }

    interface ListenerTrackingCoupon {
        fun onImpressionCoupon(promoCheckoutListModel: PromoCheckoutListModel?)
    }

    companion object {
        val LAYOUT = R.layout.item_list_promo_checkout
    }
}
