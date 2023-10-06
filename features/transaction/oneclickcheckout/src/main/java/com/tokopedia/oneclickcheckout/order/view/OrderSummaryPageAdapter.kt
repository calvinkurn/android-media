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
import com.tokopedia.purchase_platform.common.databinding.ItemUploadPrescriptionBinding
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionListener
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionViewHolder
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData

class OrderSummaryPageAdapter(
    private val analytics: OrderSummaryAnalytics,
    private val shopListener: OrderShopCard.OrderShopCardListener,
    private val productListener: OrderProductCard.OrderProductCardListener,
    private val preferenceListener: OrderPreferenceCard.OrderPreferenceCardListener,
    private val insuranceListener: OrderInsuranceCard.OrderInsuranceCardListener,
    private val promoCardListener: OrderPromoCard.OrderPromoCardListener,
    private val uploadPrescriptionListener: UploadPrescriptionListener,
    private val paymentCardListener: OrderTotalPaymentCard.OrderTotalPaymentCardListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        const val tickerIndex = 0
        const val onboardingIndex = 1
        const val shopIndex = 2
        const val productStartIndex = 3

        private const val uploadPrescriptionIndexAddition = 3
        private const val preferenceIndexAddition = 4
        private const val insuranceIndexAddition = 5
        private const val promoIndexAddition = 6
        private const val totalPaymentIndexAddition = 7
    }

    var ticker: TickerData? = null
    var onboarding: OccOnboarding = OccOnboarding()
    var shop: OrderShop = OrderShop()
    var products: List<OrderProduct> = emptyList()
    var profile: OrderProfile = OrderProfile(enable = false)
    var shipment: OrderShipment = OrderShipment()
    var payment: OrderPayment = OrderPayment()
    var promo: OrderPromo = OrderPromo()
    var uploadPrescription = UploadPrescriptionUiModel()
    var total: OrderTotal = OrderTotal()

    val uploadPrescriptionIndex: Int
        get() = products.size + uploadPrescriptionIndexAddition

    val preferenceIndex: Int
        get() = products.size + preferenceIndexAddition

    val insuranceIndex: Int
        get() = products.size + insuranceIndexAddition

    val promoIndex: Int
        get() = products.size + promoIndexAddition

    val totalPaymentIndex: Int
        get() = products.size + totalPaymentIndexAddition

    fun getFirstErrorIndex(): Int {
        if (shop.isError) {
            return shopIndex
        }
        for (index in products.indices) {
            if (products.getOrNull(index)?.isError == true) {
                return productStartIndex + index
            }
        }
        return 0
    }

    fun getAddOnProductServiceIndex(cartId: String): Int {
        for (index in products.indices) {
            if (products[index].cartId == cartId) {
                return productStartIndex + index
            }
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            OrderTickerCard.VIEW_TYPE -> {
                return OrderTickerCard(
                    CardOrderTickerBinding.inflate(inflater, parent, false),
                    object : OrderTickerCard.OrderTickerCardListener {
                        override fun onCloseTicker() {
                            ticker = null
                        }
                    }
                )
            }
            OrderOnboardingCard.VIEW_TYPE -> {
                return OrderOnboardingCard(LayoutOccOnboardingNewBinding.inflate(inflater, parent, false))
            }
            OrderShopCard.VIEW_TYPE -> {
                return OrderShopCard(CardOrderShopBinding.inflate(inflater, parent, false), shopListener, analytics)
            }
            OrderProductCard.VIEW_TYPE -> {
                return OrderProductCard(CardOrderProductBinding.inflate(inflater, parent, false), productListener, analytics, inflater)
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
            UploadPrescriptionViewHolder.ITEM_VIEW_UPLOAD -> {
                return UploadPrescriptionViewHolder(ItemUploadPrescriptionBinding.inflate(inflater, parent, false).root, uploadPrescriptionListener)
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
                holder.setData(products[position - productStartIndex], shop, position - productStartIndex)
            }
            is OrderPreferenceCard -> {
                holder.setPreferenceData(shop, profile, shipment, payment)
            }
            is OrderInsuranceCard -> {
                holder.setupInsurance(shipment, profile)
            }
            is OrderPromoCard -> {
                holder.setupButtonPromo(promo)
            }
            is OrderTotalPaymentCard -> {
                holder.setupPayment(total)
            }
            is UploadPrescriptionViewHolder -> {
                holder.bindViewHolder(uploadPrescription)
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
            bottomPosition < uploadPrescriptionIndexAddition -> OrderProductCard.VIEW_TYPE
            bottomPosition == uploadPrescriptionIndexAddition -> UploadPrescriptionViewHolder.ITEM_VIEW_UPLOAD
            bottomPosition == preferenceIndexAddition -> OrderPreferenceCard.VIEW_TYPE
            bottomPosition == insuranceIndexAddition -> OrderInsuranceCard.VIEW_TYPE
            bottomPosition == promoIndexAddition -> OrderPromoCard.VIEW_TYPE
            bottomPosition == totalPaymentIndexAddition -> OrderTotalPaymentCard.VIEW_TYPE
            else -> -1
        }
    }
}
