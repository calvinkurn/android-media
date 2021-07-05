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
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData

class OrderSummaryPageAdapter(private val analytics: OrderSummaryAnalytics,
                              private val productListener: OrderProductCard.OrderProductCardListener,
                              private val preferenceListener: OrderPreferenceCard.OrderPreferenceCardListener,
                              private val insuranceListener: OrderInsuranceCard.OrderInsuranceCardListener,
                              private val promoCardListener: OrderPromoCard.OrderPromoCardListener,
                              private val paymentCardListener: OrderTotalPaymentCard.OrderTotalPaymentCardListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var ticker: TickerData? = null
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

    val preferenceIndex: Int
        get() = products.size + 3
    val insuranceIndex: Int
        get() = products.size + 4
    val promoIndex: Int
        get() = products.size + 5
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
                return OrderShopCard(CardOrderShopBinding.inflate(inflater, parent, false), analytics)
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
        return products.size + 7
    }

    override fun getItemViewType(position: Int): Int {
        val productsCount = products.size
        val bottomPosition = position - productsCount
        return when {
            position == 0 -> OrderTickerCard.VIEW_TYPE
            position == 1 -> OrderOnboardingCard.VIEW_TYPE
            position == 2 -> OrderShopCard.VIEW_TYPE
            bottomPosition < 3 -> OrderProductCard.VIEW_TYPE
            bottomPosition == 3 -> OrderPreferenceCard.VIEW_TYPE
            bottomPosition == 4 -> OrderInsuranceCard.VIEW_TYPE
            bottomPosition == 5 -> OrderPromoCard.VIEW_TYPE
            bottomPosition == 6 -> OrderTotalPaymentCard.VIEW_TYPE
            else -> -1
        }
    }
}