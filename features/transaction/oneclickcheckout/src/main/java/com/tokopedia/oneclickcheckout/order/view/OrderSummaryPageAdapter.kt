package com.tokopedia.oneclickcheckout.order.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.oneclickcheckout.databinding.*
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding
import com.tokopedia.oneclickcheckout.order.view.card.*
import com.tokopedia.oneclickcheckout.order.view.model.*

class OrderSummaryPageAdapter(private val analytics: OrderSummaryAnalytics,
                              private val productListener: OrderProductCard.OrderProductCardListener,
                              private val preferenceListener: NewOrderPreferenceCard.OrderPreferenceCardListener,
                              private val insuranceListener: OrderInsuranceCard.OrderInsuranceCardListener,
                              private val promoCardListener: OrderPromoCard.OrderPromoCardListener,
                              private val paymentCardListener: OrderTotalPaymentCard.OrderTotalPaymentCardListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onboarding: OccMainOnboarding? = null
    var shop: OrderShop? = null
    var product: OrderProduct? = null
    var preference: OrderPreference? = null
    var shipment: OrderShipment? = null
    var payment: OrderPayment? = null
    var insurance: InsuranceData? = null
    var promo: OrderPromo? = null
    var total: OrderTotal? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == OrderOnboardingCard.VIEW_TYPE) {
            return OrderOnboardingCard(LayoutOccOnboardingNewBinding.inflate(inflater, parent, false))
        }
        if (viewType == OrderShopCard.VIEW_TYPE) {
            return OrderShopCard(CardOrderShopBinding.inflate(inflater, parent, false), analytics)
        }
        if (viewType == OrderProductCard.VIEW_TYPE) {
            return OrderProductCard(CardOrderProductBinding.inflate(inflater, parent, false), productListener, analytics)
        }
        if (viewType == NewOrderPreferenceCard.VIEW_TYPE) {
            return NewOrderPreferenceCard(CardOrderPreferenceNewBinding.inflate(inflater, parent, false), preferenceListener, analytics)
        }
        if (viewType == OrderInsuranceCard.VIEW_TYPE) {
            return OrderInsuranceCard(CardOrderInsuranceBinding.inflate(inflater, parent, false), insuranceListener, analytics)
        }
        if (viewType == OrderPromoCard.VIEW_TYPE) {
            return OrderPromoCard(CardOrderPromoBinding.inflate(inflater, parent, false), promoCardListener, analytics)
        }
        if (viewType == OrderTotalPaymentCard.VIEW_TYPE) {
            return OrderTotalPaymentCard(LayoutPaymentBinding.inflate(inflater, parent, false), paymentCardListener)
        }
        throw UnknownError("missing view type $viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OrderOnboardingCard) {
            holder.bind(onboarding!!)
        }
        if (holder is OrderShopCard) {
            holder.setShop(shop!!, product!!.freeOngkirImg, product!!.isFreeOngkirExtra)
        }
        if (holder is OrderProductCard) {
            holder.setProduct(product!!)
            holder.setShop(shop!!)
            holder.initView()
        }
        if (holder is NewOrderPreferenceCard) {
            holder.setPreference(preference!!)
            if (shipment != null) {
                holder.setShipment(shipment)
            }
            if (payment != null) {
                holder.setPayment(payment!!)
            }
        }
        if (holder is OrderInsuranceCard) {
            holder.setupInsurance(insurance, product!!.productId.toString())
        }
        if (holder is OrderPromoCard) {
            holder.setupButtonPromo(promo!!)
        }
        if (holder is OrderTotalPaymentCard) {
            holder.setupPayment(total!!)
        }
    }

    override fun getItemCount(): Int {
        if (shop != null && total != null && insurance != null) return 7
        return 0
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return OrderOnboardingCard.VIEW_TYPE
        if (position == 1) return OrderShopCard.VIEW_TYPE
        if (position == 2) return OrderProductCard.VIEW_TYPE
        if (position == 3) return NewOrderPreferenceCard.VIEW_TYPE
        if (position == 4) return OrderInsuranceCard.VIEW_TYPE
        if (position == 5) return OrderPromoCard.VIEW_TYPE
        if (position == 6) return OrderTotalPaymentCard.VIEW_TYPE
        return 0
    }
}