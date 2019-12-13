package com.tokopedia.purchase_platform.features.cart.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.tokopedia.checkout.view.common.TickerAnnouncementActionListener
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.TickerAnnouncementHolderData
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartDigitalProduct
import com.tokopedia.purchase_platform.common.data.model.response.macro_insurance.InsuranceCartShops
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoActionListener
import com.tokopedia.purchase_platform.common.feature.promo_global.PromoGlobalViewHolder
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionHolderData
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.CartPromoSuggestionViewHolder
import com.tokopedia.purchase_platform.common.feature.seller_cashback.ShipmentSellerCashbackModel
import com.tokopedia.purchase_platform.common.feature.seller_cashback.ShipmentSellerCashbackViewHolder
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.ShopGroupAvailableData
import com.tokopedia.purchase_platform.features.cart.view.ActionListener
import com.tokopedia.purchase_platform.features.cart.view.InsuranceItemActionListener
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartEmptyViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartLoadingViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartRecentViewViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartRecommendationViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartSectionHeaderViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartSelectAllViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartShopViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartTickerErrorViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartWishlistViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.DisabledCartItemViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.DisabledItemHeaderViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.DisabledShopViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.InsuranceCartShopViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewholder.TickerAnnouncementViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartEmptyHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemTickerErrorHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartLoadingHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecentViewHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecommendationItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartSectionHeaderHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartShopHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartWishlistHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.DisabledCartItemHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.DisabledItemHeaderHolderData
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.DisabledShopHolderData

import java.util.ArrayList

import javax.inject.Inject

import rx.subscriptions.CompositeSubscription

import com.tokopedia.transaction.insurance.utils.PAGE_TYPE_CART

/**
 * @author anggaprasetiyo on 18/01/18.
 */

class CartAdapter @Inject constructor(private val actionListener: ActionListener,
                                      private val promoActionListener: PromoActionListener,
                                      private val cartItemActionListener: CartItemAdapter.ActionListener,
                                      private val insuranceItemActionlistener: InsuranceItemActionListener,
                                      private val tickerAnnouncementActionListener: TickerAnnouncementActionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val cartDataList = ArrayList<Any>()
    private val compositeSubscription = CompositeSubscription()

    val insuranceCartShops = ArrayList<InsuranceCartShops>()
    private val insuranceRecommendationList = ArrayList<InsuranceCartShops>()
    private val insuranceCartList = ArrayList<InsuranceCartShops>()
    private var shipmentSellerCashbackModel: ShipmentSellerCashbackModel? = null
    private var cartEmptyHolderData: CartEmptyHolderData? = null
    private var cartLoadingHolderData: CartLoadingHolderData? = null
    private var cartWishlistAdapter: CartWishlistAdapter? = null
    private var cartRecentViewAdapter: CartRecentViewAdapter? = null
    private var sendInsuranceImpressionEvent = false

    var isInsuranceSelected: Boolean = false
        private set
    var selectedInsuranceProductId = ""
        private set
    var selectedInsuranceProductTitle = ""
        private set

    val allShopGroupDataList: List<CartShopHolderData>
        get() {
            val cartShopHolderDataFinalList = ArrayList<CartShopHolderData>()
            for (i in cartDataList.indices) {
                val `object` = cartDataList[i]
                if (`object` is CartShopHolderData) {
                    cartShopHolderDataFinalList.add(`object`)
                }
            }
            return cartShopHolderDataFinalList
        }

    val selectedCartItemData: List<CartItemData>
        get() {
            val cartItemDataList = ArrayList<CartItemData>()
            for (data in cartDataList) {
                if (data is CartShopHolderData) {
                    if ((data.isPartialSelected || data.isAllSelected) && data.shopGroupAvailableData.cartItemDataList != null) {
                        val cartItemHolderDataList = data.shopGroupAvailableData.cartItemDataList
                        cartItemHolderDataList?.let {
                            for (cartItemHolderData in it) {
                                if (cartItemHolderData.isSelected && !cartItemHolderData.cartItemData.isError) {
                                    cartItemDataList.add(cartItemHolderData.cartItemData)
                                }
                            }
                        }
                    }
                }
            }

            return cartItemDataList
        }

    val selectedCartShopHolderData: List<CartShopHolderData>
        get() {
            val cartShopHolderDataList = ArrayList<CartShopHolderData>()
            for (data in cartDataList) {
                if (data is CartShopHolderData) {
                    if ((data.isPartialSelected || data.isAllSelected) && data.shopGroupAvailableData.cartItemDataList != null) {
                        cartShopHolderDataList.add(data)
                    }
                }
            }

            return cartShopHolderDataList
        }

    val allCartItemData: List<CartItemData>
        get() {
            val cartItemDataList = ArrayList<CartItemData>()
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartShopHolderData -> if (data.shopGroupAvailableData.cartItemDataList != null) {
                        val cartItemHolderDataList = data.shopGroupAvailableData.cartItemDataList
                        cartItemHolderDataList?.let {
                            for ((cartItemData) in it) {
                                cartItemDataList.add(cartItemData)
                            }
                        }
                    }
                    is DisabledCartItemHolderData -> cartItemDataList.add(data.data)
                    is CartWishlistHolderData, is CartRecentViewHolderData, is CartRecommendationItemHolderData -> break@loop
                }
            }

            return cartItemDataList
        }

    val allAvailableCartItemData: List<CartItemData>
        get() {
            val cartItemDataList = ArrayList<CartItemData>()
            for (data in cartDataList) {
                if (data is CartShopHolderData) {
                    val cartItemHolderDataList = data.shopGroupAvailableData.cartItemDataList
                    cartItemHolderDataList?.let {
                        for ((cartItemData) in it) {
                            cartItemDataList.add(cartItemData)
                        }
                    }
                } else if (data is CartWishlistHolderData || data is CartRecentViewHolderData || data is CartRecommendationItemHolderData) {
                    break
                }
            }

            return cartItemDataList
        }

    val allDisabledCartItemData: List<CartItemData>
        get() {
            val cartItemDataList = ArrayList<CartItemData>()
            if (cartDataList != null) {
                for (data in cartDataList) {
                    if (data is DisabledCartItemHolderData) {
                        val (_, _, _, _, _, _, _, _, _, _, _, data1) = data
                        cartItemDataList.add(data1)
                    } else if (data is CartWishlistHolderData ||
                            data is CartRecentViewHolderData ||
                            data is CartRecommendationItemHolderData) {
                        break
                    }
                }
            }

            return cartItemDataList
        }

    val allCartItemProductId: List<String>
        get() {
            val productIdList = ArrayList<String>()
            if (cartDataList != null) {
                for (data in cartDataList) {
                    if (data is CartShopHolderData) {
                        if (data.shopGroupAvailableData.cartItemDataList != null) {
                            for ((cartItemData) in data.shopGroupAvailableData.cartItemDataList!!) {
                                productIdList.add(cartItemData.originData!!.productId)
                            }
                        }
                    } else if (data is DisabledCartItemHolderData) {
                        productIdList.add(data.productId)
                    }
                }
            }

            return productIdList
        }

    val allCartItemHolderData: List<CartItemHolderData>
        get() {
            val cartItemDataList = ArrayList<CartItemHolderData>()
            if (cartDataList != null) {
                for (data in cartDataList) {
                    if (data is CartShopHolderData) {
                        if (data.shopGroupAvailableData.cartItemDataList != null) {
                            cartItemDataList.addAll(data.shopGroupAvailableData.cartItemDataList!!)
                        }
                    }
                }
            }

            return cartItemDataList
        }

    val selectedRecommendedInsuranceList: List<InsuranceCartShops>
        get() {

            val insuranceCartShopsList = ArrayList<InsuranceCartShops>()
            for (insuranceCartShops in insuranceRecommendationList) {

                if (insuranceCartShops != null &&
                        insuranceCartShops.shopItemsList.size > 0 &&
                        insuranceCartShops.shopItemsList[0] != null &&
                        insuranceCartShops.shopItemsList[0].digitalProductList.size > 0 &&
                        insuranceCartShops.shopItemsList[0].digitalProductList[0] != null &&
                        insuranceCartShops.shopItemsList[0].digitalProductList[0].optIn) {

                    insuranceCartShopsList.add(insuranceCartShops)
                }
            }
            return insuranceCartShopsList
        }

    val isInsuranceCartProductUnSelected: ArrayList<InsuranceCartDigitalProduct>
        get() {

            isInsuranceSelected = true
            val insuranceCartDigitalProductArrayList = ArrayList<InsuranceCartDigitalProduct>()
            for (insuranceCartShops in insuranceCartList) {
                if (insuranceCartShops != null && !insuranceCartShops.shopItemsList.isEmpty()) {
                    for ((_, digitalProductList) in insuranceCartShops.shopItemsList) {
                        if (digitalProductList != null && !digitalProductList.isEmpty()) {
                            for (insuranceCartDigitalProduct in digitalProductList) {
                                selectedInsuranceProductTitle = insuranceCartDigitalProduct.productInfo.title
                                selectedInsuranceProductId = insuranceCartDigitalProduct.productId
                                if (!insuranceCartDigitalProduct.optIn) {
                                    isInsuranceSelected = false
                                    insuranceCartDigitalProductArrayList.add(insuranceCartDigitalProduct)
                                }
                            }
                        } else {
                            isInsuranceSelected = false
                        }
                    }
                } else {
                    isInsuranceSelected = false
                }

            }
            return insuranceCartDigitalProductArrayList
        }

    val promoData: PromoData?
        get() {
            for (i in cartDataList!!.indices) {
                if (cartDataList[i] is PromoData) {
                    return cartDataList[i] as PromoData
                }
            }
            return null
        }

    val promoStackingGlobalData: PromoStackingData?
        get() {
            for (i in cartDataList!!.indices) {
                if (cartDataList[i] is PromoStackingData) {
                    return cartDataList[i] as PromoStackingData
                }
            }
            return null
        }

    val allCartShopHolderData: List<CartShopHolderData>
        get() {
            val cartShopHolderDataList = ArrayList<CartShopHolderData>()
            for (i in cartDataList!!.indices) {
                val cartShopHolderData = getCartShopHolderDataByIndex(i)
                if (cartShopHolderData != null) {
                    cartShopHolderDataList.add(cartShopHolderData)
                }
            }

            return cartShopHolderDataList
        }

    val disabledItemHeaderPosition: Int
        get() {
            for (i in cartDataList!!.indices) {
                if (cartDataList[i] is DisabledItemHeaderHolderData) {
                    return i
                }
            }
            return 0
        }

    override fun getItemViewType(position: Int): Int {
        val `object` = cartDataList!![position]
        return if (`object` is CartShopHolderData) {
            CartShopViewHolder.TYPE_VIEW_ITEM_SHOP
        } else if (`object` is CartPromoSuggestionHolderData) {
            CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION
        } else if (`object` is PromoStackingData) {
            PromoGlobalViewHolder.TYPE_VIEW_PROMO
        } else if (`object` is CartItemTickerErrorHolderData) {
            CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR
        } else if (`object` is ShipmentSellerCashbackModel) {
            ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK
        } else if (`object` is CartEmptyHolderData) {
            CartEmptyViewHolder.LAYOUT
        } else if (`object` is CartRecentViewHolderData) {
            CartRecentViewViewHolder.LAYOUT
        } else if (`object` is CartWishlistHolderData) {
            CartWishlistViewHolder.LAYOUT
        } else if (`object` is CartSectionHeaderHolderData) {
            CartSectionHeaderViewHolder.LAYOUT
        } else if (`object` is CartRecommendationItemHolderData) {
            CartRecommendationViewHolder.LAYOUT
        } else if (`object` is CartLoadingHolderData) {
            CartLoadingViewHolder.LAYOUT
        } else if (`object` is TickerAnnouncementHolderData) {
            TickerAnnouncementViewHolder.LAYOUT
        } else if (`object` is Boolean) {
            CartSelectAllViewHolder.LAYOUT
        } else if (`object` is InsuranceCartShops) {
            InsuranceCartShopViewHolder.TYPE_VIEW_INSURANCE_CART_SHOP
        } else if (`object` is DisabledCartItemHolderData) {
            DisabledCartItemViewHolder.LAYOUT
        } else if (`object` is DisabledItemHeaderHolderData) {
            DisabledItemHeaderViewHolder.LAYOUT
        } else if (`object` is DisabledShopHolderData) {
            DisabledShopViewHolder.LAYOUT
        } else {
            super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(CartShopViewHolder.TYPE_VIEW_ITEM_SHOP, parent, false)
            return CartShopViewHolder(view, actionListener, cartItemActionListener, compositeSubscription)
        } else if (viewType == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION, parent, false)
            return CartPromoSuggestionViewHolder(view, promoActionListener)
        } else if (viewType == PromoGlobalViewHolder.TYPE_VIEW_PROMO) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(PromoGlobalViewHolder.TYPE_VIEW_PROMO, parent, false)
            return PromoGlobalViewHolder(view, promoActionListener)
        } else if (viewType == CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR, parent, false)
            return CartTickerErrorViewHolder(view, actionListener)
        } else if (viewType == ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK, parent, false)
            return ShipmentSellerCashbackViewHolder(view)
        } else if (viewType == CartEmptyViewHolder.LAYOUT) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(CartEmptyViewHolder.LAYOUT, parent, false)
            return CartEmptyViewHolder(view, actionListener)
        } else if (viewType == CartRecentViewViewHolder.LAYOUT) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(CartRecentViewViewHolder.LAYOUT, parent, false)
            return CartRecentViewViewHolder(view, actionListener)
        } else if (viewType == CartWishlistViewHolder.LAYOUT) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(CartWishlistViewHolder.LAYOUT, parent, false)
            return CartWishlistViewHolder(view, actionListener)
        } else if (viewType == CartSectionHeaderViewHolder.LAYOUT) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(CartSectionHeaderViewHolder.LAYOUT, parent, false)
            return CartSectionHeaderViewHolder(view, actionListener)
        } else if (viewType == CartRecommendationViewHolder.LAYOUT) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(CartRecommendationViewHolder.LAYOUT, parent, false)
            return CartRecommendationViewHolder(view, actionListener)
        } else if (viewType == CartLoadingViewHolder.LAYOUT) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(CartLoadingViewHolder.LAYOUT, parent, false)
            return CartLoadingViewHolder(view)
        } else if (viewType == CartSelectAllViewHolder.LAYOUT) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(CartSelectAllViewHolder.LAYOUT, parent, false)
            return CartSelectAllViewHolder(view, actionListener)
        } else if (viewType == TickerAnnouncementViewHolder.LAYOUT) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(TickerAnnouncementViewHolder.LAYOUT, parent, false)
            return TickerAnnouncementViewHolder(view, tickerAnnouncementActionListener)
        } else if (viewType == InsuranceCartShopViewHolder.TYPE_VIEW_INSURANCE_CART_SHOP) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(InsuranceCartShopViewHolder.TYPE_VIEW_INSURANCE_CART_SHOP, parent, false)
            return InsuranceCartShopViewHolder(view, insuranceItemActionlistener)
        } else if (viewType == DisabledCartItemViewHolder.LAYOUT) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(DisabledCartItemViewHolder.LAYOUT, parent, false)
            return DisabledCartItemViewHolder(view, actionListener)
        } else if (viewType == DisabledItemHeaderViewHolder.LAYOUT) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(DisabledItemHeaderViewHolder.LAYOUT, parent, false)
            return DisabledItemHeaderViewHolder(view, actionListener)
        } else if (viewType == DisabledShopViewHolder.LAYOUT) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(DisabledShopViewHolder.LAYOUT, parent, false)
            return DisabledShopViewHolder(view, actionListener)
        }

        throw RuntimeException("No view holder type found")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            val holderView = holder as CartShopViewHolder
            val data = cartDataList!![position] as CartShopHolderData
            holderView.bindData(data, position)
        } else if (viewType == CartPromoSuggestionViewHolder.TYPE_VIEW_PROMO_SUGGESTION) {
            val holderView = holder as CartPromoSuggestionViewHolder
            val data = cartDataList!![position] as CartPromoSuggestionHolderData
            holderView.bindData(data, position)
        } else if (viewType == PromoGlobalViewHolder.TYPE_VIEW_PROMO) {
            val holderView = holder as PromoGlobalViewHolder
            val data = cartDataList!![position] as PromoStackingData
            holderView.bindData(data, position)
        } else if (viewType == CartTickerErrorViewHolder.TYPE_VIEW_TICKER_CART_ERROR) {
            val holderView = holder as CartTickerErrorViewHolder
            val data = cartDataList!![position] as CartItemTickerErrorHolderData
            holderView.bindData(data, position)
        } else if (viewType == ShipmentSellerCashbackViewHolder.ITEM_VIEW_SELLER_CASHBACK) {
            val holderView = holder as ShipmentSellerCashbackViewHolder
            val data = cartDataList!![position] as ShipmentSellerCashbackModel
            holderView.bindViewHolder(data)
        } else if (viewType == CartEmptyViewHolder.LAYOUT) {
            val holderView = holder as CartEmptyViewHolder
            val data = cartDataList!![position] as CartEmptyHolderData
            holderView.bind(data)
        } else if (viewType == CartRecentViewViewHolder.LAYOUT) {
            val holderView = holder as CartRecentViewViewHolder
            val data = cartDataList!![position] as CartRecentViewHolderData
            holderView.bind(data)
            cartRecentViewAdapter = holderView.recentViewAdapter
        } else if (viewType == CartWishlistViewHolder.LAYOUT) {
            val holderView = holder as CartWishlistViewHolder
            val data = cartDataList!![position] as CartWishlistHolderData
            holderView.bind(data)
            cartWishlistAdapter = holderView.wishlistAdapter
        } else if (viewType == CartSectionHeaderViewHolder.LAYOUT) {
            val holderView = holder as CartSectionHeaderViewHolder
            val data = cartDataList!![position] as CartSectionHeaderHolderData
            holderView.bind(data)
        } else if (viewType == CartRecommendationViewHolder.LAYOUT) {
            val holderView = holder as CartRecommendationViewHolder
            val data = cartDataList!![position] as CartRecommendationItemHolderData
            holderView.bind(data)
        } else if (viewType == CartLoadingViewHolder.LAYOUT) {
            val holderView = holder as CartLoadingViewHolder
            val data = cartDataList!![position] as CartLoadingHolderData
            holderView.bind(data)
        } else if (viewType == TickerAnnouncementViewHolder.LAYOUT) {
            val holderView = holder as TickerAnnouncementViewHolder
            val cartTickerData = cartDataList!![position] as TickerAnnouncementHolderData
            holderView.bind(cartTickerData)
        } else if (viewType == CartSelectAllViewHolder.LAYOUT) {
            val holderView = holder as CartSelectAllViewHolder
            val isAllSelected = cartDataList!![position] as Boolean
            holderView.bind(isAllSelected)
        } else if (getItemViewType(position) == InsuranceCartShopViewHolder.TYPE_VIEW_INSURANCE_CART_SHOP) {
            val insuranceCartShopViewHolder = holder as InsuranceCartShopViewHolder
            val insuranceCartShops = cartDataList!![position] as InsuranceCartShops
            insuranceCartShopViewHolder.bindData(insuranceCartShops, position, PAGE_TYPE_CART)
        } else if (viewType == DisabledItemHeaderViewHolder.LAYOUT) {
            val holderView = holder as DisabledItemHeaderViewHolder
            val data = cartDataList!![position] as DisabledItemHeaderHolderData
            holderView.bind(data)
        } else if (viewType == DisabledShopViewHolder.LAYOUT) {
            val holderView = holder as DisabledShopViewHolder
            val data = cartDataList!![position] as DisabledShopHolderData
            holderView.bind(data)
        } else if (viewType == DisabledCartItemViewHolder.LAYOUT) {
            val holderView = holder as DisabledCartItemViewHolder
            val data = cartDataList!![position] as DisabledCartItemHolderData
            holderView.bind(data)
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is InsuranceCartShopViewHolder && !sendInsuranceImpressionEvent) {
            sendInsuranceImpressionEvent = true
            insuranceItemActionlistener.sendEventInsuranceImpression(holder.getProductTitle())
        }
    }


    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder.itemViewType == CartRecommendationViewHolder.LAYOUT) {
            (holder as CartRecommendationViewHolder).clearImage()
        }
    }

    override fun getItemCount(): Int {
        return cartDataList!!.size
    }

    fun unsubscribeSubscription() {
        compositeSubscription.unsubscribe()
    }

    fun addAvailableDataList(shopGroupAvailableDataList: List<ShopGroupAvailableData>) {
        for (shopGroupAvailableData in shopGroupAvailableDataList) {
            if (shopGroupAvailableData.cartItemDataList != null && shopGroupAvailableData.cartItemDataList!!.size > 0) {
                val cartShopHolderData = CartShopHolderData()
                cartShopHolderData.shopGroupAvailableData = shopGroupAvailableData
                if (shopGroupAvailableData.isError) {
                    cartShopHolderData.isAllSelected = false
                } else {
                    if (shopGroupAvailableData.isChecked) {
                        cartShopHolderData.isAllSelected = true
                    } else if (shopGroupAvailableData.cartItemDataList != null && shopGroupAvailableData.cartItemDataList!!.size > 1) {
                        for ((_, _, _, _, _, isSelected) in shopGroupAvailableData.cartItemDataList!!) {
                            if (isSelected) {
                                cartShopHolderData.isPartialSelected = true
                                break
                            }
                        }
                    }
                }
                cartShopHolderData.shopGroupAvailableData = shopGroupAvailableData
                cartDataList!!.add(cartShopHolderData)
            }
        }
    }

    fun addNotAvailableHeader(disabledItemHeaderHolderData: DisabledItemHeaderHolderData) {
        cartDataList!!.add(disabledItemHeaderHolderData)
    }

    fun addNotAvailableShop(disabledShopHolderData: DisabledShopHolderData) {
        cartDataList!!.add(disabledShopHolderData)
    }

    fun addNotAvailableProduct(disabledCartItemHolderData: DisabledCartItemHolderData) {
        cartDataList!!.add(disabledCartItemHolderData)
    }

    fun setAllShopSelected(selected: Boolean) {
        if (cartDataList != null) {
            for (i in cartDataList.indices) {
                if (cartDataList[i] is CartShopHolderData) {
                    val cartShopHolderData = cartDataList[i] as CartShopHolderData
                    if (cartShopHolderData.shopGroupAvailableData.cartItemDataList != null && cartShopHolderData.shopGroupAvailableData.cartItemDataList!!.size == 1) {
                        for ((cartItemData) in cartShopHolderData.shopGroupAvailableData.cartItemDataList!!) {
                            if (cartItemData.isError && cartItemData.isSingleChild) {
                                setShopSelected(i, false)
                            } else {
                                setShopSelected(i, selected)
                            }
                        }
                    } else {
                        setShopSelected(i, selected)
                    }
                }
            }
        }
    }

    fun setShopSelected(position: Int, selected: Boolean) {
        val `object` = cartDataList!![position]
        if (`object` is CartShopHolderData) {
            `object`.isAllSelected = selected
            for (cartItemHolderData in `object`.shopGroupAvailableData.cartItemDataList!!) {
                cartItemHolderData.isSelected = selected
            }
        }
    }

    fun setItemSelected(position: Int, parentPosition: Int, selected: Boolean): Boolean {
        var needToUpdateParent = false
        val `object` = cartDataList!![parentPosition]
        if (`object` is CartShopHolderData) {
            val shopAlreadySelected = `object`.isAllSelected || `object`.isPartialSelected
            var selectedCount = 0
            for (i in 0 until `object`.shopGroupAvailableData.cartItemDataList!!.size) {
                val cartItemHolderData = `object`.shopGroupAvailableData.cartItemDataList!![i]
                if (i == position) {
                    cartItemHolderData.isSelected = selected
                }

                if (cartItemHolderData.isSelected) {
                    selectedCount++
                }
            }

            if (selectedCount == 0) {
                `object`.isAllSelected = false
                `object`.isPartialSelected = false
                needToUpdateParent = shopAlreadySelected
            } else if (selectedCount > 0 && selectedCount < `object`.shopGroupAvailableData.cartItemDataList!!.size) {
                `object`.isAllSelected = false
                `object`.isPartialSelected = true
                needToUpdateParent = !shopAlreadySelected
            } else {
                `object`.isAllSelected = true
                `object`.isPartialSelected = false
                needToUpdateParent = !shopAlreadySelected
            }

            // Check does has cash back item
            var hasCashbackItem = false
            for ((cartItemData) in `object`.shopGroupAvailableData.cartItemDataList!!) {
                if (cartItemData.originData!!.isCashBack) {
                    hasCashbackItem = true
                    break
                }
            }
            // Check is it the last cash back item to uncheck & still contain non cash back item
            var isTheLastCashbackItem = true
            var hasUncheckedItem = false
            if (!selected && hasCashbackItem) {
                // Check does it still contain unchecked item
                for ((cartItemData, _, _, _, _, isSelected) in `object`.shopGroupAvailableData.cartItemDataList!!) {
                    if (isSelected && !cartItemData.originData!!.isCashBack) {
                        hasUncheckedItem = true
                        break
                    }
                }
                // Check is it the last cash back item to be unchecked
                for (j in 0 until `object`.shopGroupAvailableData.cartItemDataList!!.size) {
                    val (cartItemData, _, _, _, _, isSelected) = `object`.shopGroupAvailableData.cartItemDataList!![j]
                    if (j != position && isSelected && cartItemData.originData!!.isCashBack) {
                        isTheLastCashbackItem = false
                        break
                    }
                }
            }
            if (hasUncheckedItem && isTheLastCashbackItem) {
                needToUpdateParent = true
            }
        }

        return needToUpdateParent
    }

    fun increaseQuantity(position: Int, parentPosition: Int) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            (cartDataList!![parentPosition] as CartShopHolderData).shopGroupAvailableData
                    .cartItemDataList!![position].cartItemData.updatedData!!.increaseQuantity()
        }
        checkForShipmentForm()
    }

    fun decreaseQuantity(position: Int, parentPosition: Int) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            (cartDataList!![parentPosition] as CartShopHolderData).shopGroupAvailableData
                    .cartItemDataList!![position].cartItemData.updatedData!!.decreaseQuantity()
        }
        checkForShipmentForm()
    }

    fun resetQuantity(position: Int, parentPosition: Int) {
        if (getItemViewType(parentPosition) == CartShopViewHolder.TYPE_VIEW_ITEM_SHOP) {
            (cartDataList!![parentPosition] as CartShopHolderData).shopGroupAvailableData
                    .cartItemDataList!![position].cartItemData.updatedData!!.resetQuantity()
        }
        checkForShipmentForm()
    }

    fun notifyItems(position: Int) {
        val itemData = cartDataList!![position]
        val itemDataParentId = (itemData as CartItemHolderData).cartItemData.originData!!.parentId
        notifyItemChanged(position)
        for (`object` in cartDataList) {
            if (`object` is CartItemHolderData) {
                val parentId = `object`.cartItemData.originData!!.parentId
                if (parentId == itemDataParentId) {
                    notifyItemChanged(cartDataList.indexOf(`object`))
                }
            }
        }
    }

    fun addPromoSuggestion(cartPromoSuggestionHolderData: CartPromoSuggestionHolderData) {
        cartDataList!!.add(cartPromoSuggestionHolderData)
        checkForShipmentForm()
    }

    fun removeInsuranceDataItem(productIdList: List<Long>) {
        try {
            for (productId in productIdList) {
                var position = 0
                for (item in cartDataList!!) {
                    position++
                    if (item is InsuranceCartShops) {
                        for ((productId1) in item.shopItemsList) {
                            if (productId1 == productId) {
                                cartDataList.remove(item)
                                notifyItemRemoved(position)
                                break
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun addInsuranceDataList(insuranceCartShops: InsuranceCartShops?, isRecommendation: Boolean) {
        this.insuranceCartShops.clear()
        this.insuranceCartShops.add(insuranceCartShops)

        if (isRecommendation) {
            insuranceRecommendationList.clear()
            insuranceRecommendationList.add(insuranceCartShops)
        } else {
            insuranceCartList.clear()
            insuranceCartList.add(insuranceCartShops)
        }

        var insuranceIndex = 0

        for (item in cartDataList!!) {
            if (item is CartEmptyHolderData || item is CartShopHolderData) {
                insuranceIndex = cartDataList.indexOf(item)
            }
        }

        if (insuranceCartShops != null) {
            cartDataList.add(++insuranceIndex, insuranceCartShops)
        }

        notifyDataSetChanged()
    }

    fun resetData() {
        cartDataList!!.clear()
        notifyDataSetChanged()
        checkForShipmentForm()
    }

    fun updateItemPromoStackVoucher(promoStackingData: PromoStackingData) {
        for (i in cartDataList!!.indices) {
            val `object` = cartDataList[i]
            if (`object` is PromoStackingData) {
                cartDataList[i] = promoStackingData
                notifyItemChanged(i)
            } else if (`object` is CartPromoSuggestionHolderData) {
                `object`.isVisible = false
                notifyItemChanged(i)
            }
        }
    }

    fun addPromoStackingVoucherData(promoStackingData: PromoStackingData) {
        cartDataList!!.add(promoStackingData)
        checkForShipmentForm()
    }

    fun removePromoStackingVoucherData() {
        for (i in cartDataList!!.indices) {
            if (cartDataList[i] is PromoStackingData) {
                cartDataList.removeAt(i)
                notifyItemRemoved(i)
                break
            }
        }
    }

    fun addCartTickerError(cartItemTickerErrorHolderData: CartItemTickerErrorHolderData) {
        cartDataList!!.add(cartItemTickerErrorHolderData)
        checkForShipmentForm()
    }

    fun addCartEmptyData() {
        if (cartEmptyHolderData == null) {
            cartEmptyHolderData = CartEmptyHolderData()
        }
        cartDataList!!.add(cartEmptyHolderData)
    }

    fun addCartRecentViewData(cartSectionHeaderHolderData: CartSectionHeaderHolderData,
                              cartRecentViewHolderData: CartRecentViewHolderData) {
        var recentViewIndex = 0
        for (item in cartDataList!!) {
            if (item is CartEmptyHolderData ||
                    item is CartShopHolderData ||
                    item is ShipmentSellerCashbackModel ||
                    item is InsuranceCartShops ||
                    item is DisabledCartItemHolderData) {
                recentViewIndex = cartDataList.indexOf(item) // 3
            }
        }
        cartDataList.add(++recentViewIndex, cartSectionHeaderHolderData)
        cartDataList.add(++recentViewIndex, cartRecentViewHolderData)
        notifyDataSetChanged()
    }

    fun addCartWishlistData(cartSectionHeaderHolderData: CartSectionHeaderHolderData,
                            cartWishlistHolderData: CartWishlistHolderData) {
        var wishlistIndex = 0
        for (item in cartDataList!!) {
            if (item is CartEmptyHolderData ||
                    item is CartShopHolderData ||
                    item is ShipmentSellerCashbackModel ||
                    item is CartRecentViewHolderData ||
                    item is InsuranceCartShops ||
                    item is DisabledCartItemHolderData) {

                wishlistIndex = cartDataList.indexOf(item)
            }
        }
        cartDataList.add(++wishlistIndex, cartSectionHeaderHolderData)
        cartDataList.add(++wishlistIndex, cartWishlistHolderData)
        notifyDataSetChanged()
    }

    fun addCartRecommendationData(cartSectionHeaderHolderData: CartSectionHeaderHolderData?,
                                  cartRecommendationItemHolderDataList: List<CartRecommendationItemHolderData>) {
        var recommendationIndex = 0
        for (item in cartDataList!!) {
            if (item is CartEmptyHolderData ||
                    item is CartShopHolderData ||
                    item is ShipmentSellerCashbackModel ||
                    item is CartRecentViewHolderData ||
                    item is CartWishlistHolderData ||
                    item is CartRecommendationItemHolderData ||
                    item is InsuranceCartShops ||
                    item is DisabledCartItemHolderData) {
                recommendationIndex = cartDataList.indexOf(item)
            }
        }

        if (cartSectionHeaderHolderData != null) {
            cartDataList.add(++recommendationIndex, cartSectionHeaderHolderData)
        }

        cartDataList.addAll(++recommendationIndex, cartRecommendationItemHolderDataList)
        notifyDataSetChanged()
    }

    fun removeCartEmptyData() {
        cartDataList!!.remove(cartEmptyHolderData)
        notifyDataSetChanged()

        cartEmptyHolderData = null
    }

    fun checkForShipmentForm() {
        var canProcess = true
        var checkedCount = 0
        for (`object` in cartDataList!!) {
            if (`object` is CartShopHolderData) {
                if (!`object`.shopGroupAvailableData.isError) {
                    if (`object`.isAllSelected) {
                        checkedCount += `object`.shopGroupAvailableData.cartItemDataList!!.size
                    } else if (`object`.isPartialSelected) {
                        if (`object`.shopGroupAvailableData.cartItemDataList != null) {
                            for (cartItemHolderData in `object`.shopGroupAvailableData.cartItemDataList!!) {
                                if (cartItemHolderData.isSelected) {
                                    checkedCount++
                                    if (cartItemHolderData.getErrorFormItemValidationTypeValue() != CartItemHolderData.ERROR_EMPTY || cartItemHolderData.cartItemData.isError) {
                                        canProcess = false
                                        break
                                    }
                                }
                            }
                            if (!canProcess) {
                                break
                            }
                        }
                    }
                }
            }
        }

        if (canProcess && checkedCount > 0) {
            actionListener.onCartDataEnableToCheckout()
        } else {
            actionListener.onCartDataDisableToCheckout()
        }
    }

    fun updateShipmentSellerCashback(cashback: Double) {
        if (cashback > 0) {
            if (shipmentSellerCashbackModel == null || cartDataList!!.indexOf(shipmentSellerCashbackModel) == -1) {
                var index = 0
                for (item in cartDataList!!) {
                    if (item is CartShopHolderData) {
                        index = cartDataList.indexOf(item)
                    }
                }
                shipmentSellerCashbackModel = ShipmentSellerCashbackModel()
                shipmentSellerCashbackModel!!.isVisible = true
                shipmentSellerCashbackModel!!.sellerCashback = CurrencyFormatUtil.convertPriceValueToIdrFormat(cashback.toLong(), false)
                cartDataList.add(++index, shipmentSellerCashbackModel)
                notifyItemInserted(index)
            } else {
                shipmentSellerCashbackModel!!.isVisible = true
                shipmentSellerCashbackModel!!.sellerCashback = CurrencyFormatUtil.convertPriceValueToIdrFormat(cashback.toLong(), false)
                val index = cartDataList.indexOf(shipmentSellerCashbackModel)
                notifyItemChanged(index)
            }
        } else {
            if (shipmentSellerCashbackModel != null) {
                val index = cartDataList!!.indexOf(shipmentSellerCashbackModel)
                cartDataList.remove(shipmentSellerCashbackModel)
                notifyItemRemoved(index)
                shipmentSellerCashbackModel = null
            }
        }

        val index = cartDataList!!.indexOf(shipmentSellerCashbackModel)
        if (index != -1) {
            notifyItemChanged(index)
        }
    }

    fun notifyByProductId(productId: String, isWishlisted: Boolean) {
        for (i in cartDataList!!.indices) {
            val obj = cartDataList[i]
            if (obj is CartShopHolderData) {
                val cartShopHolderData = cartDataList[i] as CartShopHolderData
                if (cartShopHolderData.shopGroupAvailableData.cartItemDataList != null) {
                    for ((cartItemData) in cartShopHolderData.shopGroupAvailableData.cartItemDataList!!) {
                        if (cartItemData.originData!!.productId == productId) {
                            cartItemData.originData!!.isWishlisted = isWishlisted
                            notifyItemChanged(i)
                            break
                        }
                    }
                }
            } else if (obj is DisabledCartItemHolderData) {
                if (obj.productId == productId) {
                    obj.isWishlisted = isWishlisted
                    notifyItemChanged(i)
                    break
                }
            }
        }
    }

    fun getCartShopHolderDataByIndex(index: Int): CartShopHolderData? {
        return if (cartDataList!![index] is CartShopHolderData) {
            cartDataList[index] as CartShopHolderData
        } else null
    }

    fun notifyWishlist(productId: String, isWishlist: Boolean) {
        for (`object` in cartDataList!!) {
            if (`object` is CartWishlistHolderData) {
                val wishlist = `object`.wishList
                for (data in wishlist) {
                    if (data.id == productId) {
                        data.isWishlist = isWishlist
                        if (cartWishlistAdapter != null) {
                            cartWishlistAdapter!!.notifyItemChanged(wishlist.indexOf(data))
                        }
                        break
                    }
                }
                break
            }
        }
    }

    fun notifyRecentView(productId: String, isWishlist: Boolean) {
        for (`object` in cartDataList!!) {
            if (`object` is CartRecentViewHolderData) {
                val recentViews = `object`.recentViewList
                for (data in recentViews) {
                    if (data.id == productId) {
                        data.isWishlist = isWishlist
                        if (cartRecentViewAdapter != null) {
                            cartRecentViewAdapter!!.notifyItemChanged(recentViews.indexOf(data))
                        }
                        break
                    }
                }
                break
            }
        }
    }

    fun notifyRecommendation(productId: String, isWishlist: Boolean) {
        for (i in cartDataList!!.indices.reversed()) {
            val `object` = cartDataList[i]
            if (`object` is CartRecommendationItemHolderData) {
                if (`object`.recommendationItem.productId.toString() == productId) {
                    `object`.recommendationItem.isWishlist = isWishlist
                    notifyItemChanged(i)
                    break
                }
            }
        }
    }

    fun addCartLoadingData() {
        if (cartLoadingHolderData == null) {
            cartLoadingHolderData = CartLoadingHolderData()
        }
        cartDataList!!.add(cartLoadingHolderData)
        notifyItemInserted(cartDataList.indexOf(cartLoadingHolderData))
    }

    fun removeCartLoadingData() {
        val index = cartDataList!!.indexOf(cartLoadingHolderData)
        cartDataList.remove(cartLoadingHolderData)
        notifyItemRemoved(index)
    }

    fun removeCartItemById(cartIds: List<Int>, context: Context?) {
        // Store item first before remove item to prevent ConcurrentModificationException
        val toBeRemovedData = ArrayList<Any>()
        var disabledItemHeaderHolderData: DisabledItemHeaderHolderData? = null
        var cartItemTickerErrorHolderData: CartItemTickerErrorHolderData? = null
        for (i in cartDataList!!.indices) {
            val obj = cartDataList[i]
            if (obj is CartShopHolderData) {
                val cartItemHolderDataList = obj.shopGroupAvailableData.cartItemDataList
                val toBeRemovedCartItemHolderData = ArrayList<CartItemHolderData>()
                for (cartItemHolderData in cartItemHolderDataList!!) {
                    if (cartIds.contains(cartItemHolderData.cartItemData.originData!!.cartId)) {
                        toBeRemovedCartItemHolderData.add(cartItemHolderData)
                    }
                }
                for (cartItemHolderData in toBeRemovedCartItemHolderData) {
                    cartItemHolderDataList.remove(cartItemHolderData)
                }
                if (cartItemHolderDataList.size == 0) {
                    toBeRemovedData.add(obj)
                }
            } else if (obj is DisabledCartItemHolderData) {
                if (cartIds.contains(obj.cartId)) {
                    val before = cartDataList[i - 1]
                    var after: Any? = null
                    if (i + 1 < cartDataList.size) {
                        after = cartDataList[i + 1]
                    }
                    if (before is DisabledShopHolderData) {
                        if (after !is DisabledCartItemHolderData) {
                            toBeRemovedData.add(before)
                        }
                        toBeRemovedData.add(obj)
                    } else if (before is DisabledCartItemHolderData) {
                        if (after !is DisabledCartItemHolderData) {
                            before.showDivider = false
                        }
                        toBeRemovedData.add(obj)
                    }
                }
            } else if (obj is DisabledItemHeaderHolderData) {
                disabledItemHeaderHolderData = obj
            } else if (obj is CartItemTickerErrorHolderData) {
                cartItemTickerErrorHolderData = obj
            } else if (obj is CartRecentViewHolderData ||
                    obj is CartWishlistHolderData ||
                    obj is CartRecommendationItemHolderData) {
                break
            }
        }

        for (cartShopHolderData in toBeRemovedData) {
            cartDataList.remove(cartShopHolderData)
        }

        if (cartItemTickerErrorHolderData != null || disabledItemHeaderHolderData != null) {
            var errorItemCount = 0
            for (`object` in cartDataList) {
                if (`object` is CartShopHolderData) {
                    val cartItemHolderDataList = `object`.shopGroupAvailableData.cartItemDataList
                    for ((cartItemData) in cartItemHolderDataList!!) {
                        if (cartItemData.isError) {
                            errorItemCount++
                        }
                    }
                } else if (`object` is DisabledCartItemHolderData) {
                    errorItemCount++
                } else if (`object` is CartRecentViewHolderData ||
                        `object` is CartWishlistHolderData ||
                        `object` is CartRecommendationItemHolderData) {
                    break
                }
            }

            if (errorItemCount > 0) {
                if (context != null) {
                    if (cartItemTickerErrorHolderData != null) {
                        cartItemTickerErrorHolderData.cartTickerErrorData!!.errorInfo = String.format(context.getString(R.string.cart_error_message), errorItemCount)
                    }
                    if (disabledItemHeaderHolderData != null) {
                        disabledItemHeaderHolderData.disabledItemCount = errorItemCount
                    }
                }
            } else {
                cartDataList.remove(cartItemTickerErrorHolderData)
                cartDataList.remove(disabledItemHeaderHolderData)
            }
        }

        notifyDataSetChanged()
    }

    fun addCartTicker(tickerAnnouncementHolderData: TickerAnnouncementHolderData) {
        cartDataList!!.add(0, tickerAnnouncementHolderData)
    }
}
