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

    var onboarding: OccMainOnboarding = OccMainOnboarding()
    var shop: OrderShop = OrderShop()
    var product: OrderProduct? = null
    var products: List<OrderProduct> = emptyList()
    var preference: OrderPreference = OrderPreference()
    var shipment: OrderShipment = OrderShipment()
    var payment: OrderPayment = OrderPayment()
    var insurance: InsuranceData? = null
    var promo: OrderPromo = OrderPromo()
    var total: OrderTotal = OrderTotal()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            OrderOnboardingCard.VIEW_TYPE -> {
                return OrderOnboardingCard(LayoutOccOnboardingNewBinding.inflate(inflater, parent, false))
            }
            OrderShopCard.VIEW_TYPE -> {
                return OrderShopCard(CardOrderShopBinding.inflate(inflater, parent, false), analytics)
            }
            OrderProductCard.VIEW_TYPE -> {
                return OrderProductCard(CardOrderProductBinding.inflate(inflater, parent, false), productListener, analytics)
            }
            NewOrderPreferenceCard.VIEW_TYPE -> {
                return NewOrderPreferenceCard(CardOrderPreferenceNewBinding.inflate(inflater, parent, false), preferenceListener, analytics)
            }
            OrderInsuranceCard.VIEW_TYPE -> {
                return OrderInsuranceCard(CardOrderInsuranceBinding.inflate(inflater, parent, false), insuranceListener, analytics)
            }
            OrderPromoCard.VIEW_TYPE -> {
                return OrderPromoCard(CardOrderPromoBinding.inflate(inflater, parent, false), promoCardListener, analytics)
            }
            OrderTotalPaymentCard.VIEW_TYPE -> {
                return OrderTotalPaymentCard(LayoutPaymentBinding.inflate(inflater, parent, false), paymentCardListener)
            }
            else -> throw UnknownError("missing view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OrderOnboardingCard -> {
                holder.bind(onboarding)
            }
            is OrderShopCard -> {
                holder.setShop(shop, product!!.freeOngkirImg, product!!.isFreeOngkirExtra)
            }
            is OrderProductCard -> {
                holder.setProduct(products[position - 2])
                holder.setShop(shop)
                holder.initView()
            }
            is NewOrderPreferenceCard -> {
                holder.setPreference(preference)
                holder.setShipment(shipment)
                holder.setPayment(payment)
            }
            is OrderInsuranceCard -> {
                holder.setupInsurance(insurance, product!!.productId.toString())
            }
            is OrderPromoCard -> {
                holder.setupButtonPromo(promo)
            }
            is OrderTotalPaymentCard -> {
                holder.setupPayment(total)
            }
        }
    }

    override fun getItemCount(): Int {
        return products.size + 6
    }

    override fun getItemViewType(position: Int): Int {
        val productsCount = products.size
        val bottomPosition = position - productsCount
        return when {
            position == 0 -> OrderOnboardingCard.VIEW_TYPE
            position == 1 -> OrderShopCard.VIEW_TYPE
            bottomPosition < 2 -> OrderProductCard.VIEW_TYPE
            bottomPosition == 2 -> NewOrderPreferenceCard.VIEW_TYPE
            bottomPosition == 3 -> OrderInsuranceCard.VIEW_TYPE
            bottomPosition == 4 -> OrderPromoCard.VIEW_TYPE
            bottomPosition == 5 -> OrderTotalPaymentCard.VIEW_TYPE
            else -> 0
        }
    }
}