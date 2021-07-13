package com.tokopedia.oneclickcheckout.order.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.oneclickcheckout.databinding.CardOrderInsuranceBinding
import com.tokopedia.oneclickcheckout.databinding.CardOrderPreferenceBinding
import com.tokopedia.oneclickcheckout.databinding.CardOrderProductBinding
import com.tokopedia.oneclickcheckout.databinding.CardOrderPromoBinding
import com.tokopedia.oneclickcheckout.databinding.CardOrderShopBinding
import com.tokopedia.oneclickcheckout.databinding.CardOrderTickerBinding
import com.tokopedia.oneclickcheckout.databinding.LayoutOccOnboardingNewBinding
import com.tokopedia.oneclickcheckout.databinding.LayoutPaymentBinding
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.card.OrderInsuranceCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderOnboardingCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderPreferenceCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderProductCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderPromoCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderShopCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderTickerCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderTotalPaymentCard
import com.tokopedia.oneclickcheckout.order.view.model.OccOnboarding
import com.tokopedia.oneclickcheckout.order.view.model.OrderPayment
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfile
import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.oneclickcheckout.order.view.model.OrderTotal
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData

class OrderSummaryPageAdapter(private val analytics: OrderSummaryAnalytics,
                              private val shopListener: OrderShopCard.OrderShopCardListener,
                              private val productListener: OrderProductCard.OrderProductCardListener,
                              private val preferenceListener: OrderPreferenceCard.OrderPreferenceCardListener,
                              private val insuranceListener: OrderInsuranceCard.OrderInsuranceCardListener,
                              private val promoCardListener: OrderPromoCard.OrderPromoCardListener,
                              private val paymentCardListener: OrderTotalPaymentCard.OrderTotalPaymentCardListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var ticker: TickerData? = null
    var onboarding: OccOnboarding = OccOnboarding()
    var shop: OrderShop = OrderShop()
    var product: OrderProduct? = null
    var products: List<OrderProduct> = emptyList()
    var profile: OrderProfile = OrderProfile(enable = false)
    var shipment: OrderShipment = OrderShipment()
    var payment: OrderPayment = OrderPayment()
    var promo: OrderPromo = OrderPromo()
    var total: OrderTotal = OrderTotal()

    val tickerIndex = 0
    val onboardingIndex = 1
    val shopIndex = 2
    val productStartIndex = 3

    private val preferenceIndexAddition = 3
    val preferenceIndex: Int
        get() = products.size + preferenceIndexAddition

    private val insuranceIndexAddition = 4
    val insuranceIndex: Int
        get() = products.size + insuranceIndexAddition

    private val promoIndexAddition = 5
    val promoIndex: Int
        get() = products.size + promoIndexAddition

    private val totalPaymentIndexAddition = 6
    val totalPaymentIndex: Int
        get() = products.size + 6

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            OrderTickerCard.VIEW_TYPE -> {
                return OrderTickerCard(CardOrderTickerBinding.inflate(inflater, parent, false), object : OrderTickerCard.OrderTickerCardListener {
                    override fun onCloseTicker() {
                        ticker = null
                    }
                })
            }
            OrderOnboardingCard.VIEW_TYPE -> {
                return OrderOnboardingCard(LayoutOccOnboardingNewBinding.inflate(inflater, parent, false))
            }
            OrderShopCard.VIEW_TYPE -> {
                return OrderShopCard(CardOrderShopBinding.inflate(inflater, parent, false), shopListener, analytics)
            }
            OrderProductCard.VIEW_TYPE -> {
                return OrderProductCard(CardOrderProductBinding.inflate(inflater, parent, false), productListener, analytics)
            }
            OrderPreferenceCard.VIEW_TYPE -> {
                return OrderPreferenceCard(CardOrderPreferenceBinding.inflate(inflater, parent, false), preferenceListener, analytics)
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
            is OrderTickerCard -> {
                holder.bind(ticker)
            }
            is OrderOnboardingCard -> {
                holder.bind(onboarding)
            }
            is OrderShopCard -> {
                holder.setShop(shop)
            }
            is OrderProductCard -> {
                holder.setData(products[position - 3], shop, position - 3)
            }
            is OrderPreferenceCard -> {
                holder.setPreferenceData(profile, shipment, payment)
            }
            is OrderInsuranceCard -> {
                holder.setupInsurance(shipment, product!!.productId.toString())
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
        return totalPaymentIndex + 1
    }

    override fun getItemViewType(position: Int): Int {
        val bottomPosition = position - products.size
        return when {
            position == tickerIndex -> OrderTickerCard.VIEW_TYPE
            position == onboardingIndex -> OrderOnboardingCard.VIEW_TYPE
            position == shopIndex -> OrderShopCard.VIEW_TYPE
            bottomPosition < preferenceIndexAddition -> OrderProductCard.VIEW_TYPE
            bottomPosition == preferenceIndexAddition -> OrderPreferenceCard.VIEW_TYPE
            bottomPosition == insuranceIndexAddition -> OrderInsuranceCard.VIEW_TYPE
            bottomPosition == promoIndexAddition -> OrderPromoCard.VIEW_TYPE
            bottomPosition == totalPaymentIndexAddition -> OrderTotalPaymentCard.VIEW_TYPE
            else -> -1
        }
    }
}