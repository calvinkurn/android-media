package com.tokopedia.promocheckout.list.view.adapter

import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.list.model.listcoupon.PromoCheckoutListModel
import com.tokopedia.promocheckout.widget.TimerCheckoutWidget
import kotlinx.android.synthetic.main.include_period_tnc_promo.view.*
import kotlinx.android.synthetic.main.item_list_promo_checkout.view.*

class PromoCheckoutListViewHolder(val view: View?, val listenerTrackingCoupon: ListenerTrackingCoupon) : AbstractViewHolder<PromoCheckoutListModel>(view) {

    override fun bind(element: PromoCheckoutListModel?) {
        listenerTrackingCoupon.onImpressionCoupon(element)
        ImageHandler.loadImageRounded2(view?.context, view?.imageBanner, element?.imageUrlMobile)
        view?.titlePeriod?.text = element?.usage?.text
        view?.titleMinTrans?.text = element?.minimumUsageLabel
        if(TextUtils.isEmpty(element?.minimumUsage)) {
            view?.textMinTrans?.visibility = View.GONE
            if(TextUtils.isEmpty(element?.minimumUsageLabel)){
                view?.titleMinTrans?.visibility = View.GONE
                view?.imageMinTrans?.visibility = View.GONE
            }else{
                view?.titleMinTrans?.visibility = View.VISIBLE
                view?.imageMinTrans?.visibility = View.VISIBLE
            }
        } else {
            view?.titleMinTrans?.visibility = View.VISIBLE
            view?.imageMinTrans?.visibility = View.VISIBLE
            view?.textMinTrans?.visibility = View.VISIBLE
            view?.textMinTrans?.text = element?.minimumUsage
        }
        if ((element?.usage?.activeCountdown ?: 0 > 0 &&
                        element?.usage?.activeCountdown ?: 0 < TimerCheckoutWidget.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_ONE_DAY)) {
            view?.timerUsage?.listener = object : TimerCheckoutWidget.Listener{
                override fun onTick(l: Long) {
                    element?.usage?.activeCountdown = l.toInt()
                }

                override fun onFinishTick() {

                }

            }
            setTimerUsage(element?.usage?.activeCountdown?.toLong() ?: 0)
        } else if ((element?.usage?.expiredCountdown ?: 0 > 0 &&
                        element?.usage?.expiredCountdown ?: 0 < TimerCheckoutWidget.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_ONE_DAY)) {
            view?.timerUsage?.listener = object : TimerCheckoutWidget.Listener{
                override fun onTick(l: Long) {
                    element?.usage?.expiredCountdown = l.toInt()
                }

                override fun onFinishTick() {

                }

            }
            setTimerUsage(element?.usage?.expiredCountdown?.toLong() ?: 0)
        }else{
            setDateUsage(element)
        }
    }

    fun setDateUsage(element: PromoCheckoutListModel?) {
        view?.timerUsage?.visibility = View.GONE
        view?.titlePeriod?.visibility = View.VISIBLE
        view?.textPeriod?.visibility = View.VISIBLE
        view?.textPeriod?.text = element?.usage?.usageStr
    }

    fun setTimerUsage(countDown: Long) {
        view?.timerUsage?.cancel()
        view?.timerUsage?.visibility = View.VISIBLE
        view?.titlePeriod?.visibility = View.GONE
        view?.textPeriod?.visibility = View.GONE
        view?.timerUsage?.expiredTimer = countDown
        view?.timerUsage?.start()
    }

    interface ListenerTrackingCoupon{
        fun onImpressionCoupon(promoCheckoutListModel: PromoCheckoutListModel?)
    }

    companion object {
        val LAYOUT = R.layout.item_list_promo_checkout
    }
}
