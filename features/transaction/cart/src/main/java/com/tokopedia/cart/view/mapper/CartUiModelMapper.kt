package com.tokopedia.cart.view.mapper

import android.content.Context
import com.tokopedia.cart.R
import com.tokopedia.cart.data.model.response.promo.*
import com.tokopedia.cart.data.model.response.shopgroupsimplified.*
import com.tokopedia.cart.domain.model.cartlist.SummaryTransactionUiModel
import com.tokopedia.cart.view.uimodel.*
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.BenefitSummaryInfo
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.SummariesItem
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.*
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.BenefitSummaryInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.Ticker
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

object CartUiModelMapper {

    private const val BUNDLE_NO_VARIANT_CONST = -1

    fun mapTickerAnnouncementUiModel(ticker: Ticker): TickerAnnouncementHolderData {
        return TickerAnnouncementHolderData(id = ticker.id, message = ticker.message)
    }

    fun mapChooseAddressUiModel(): CartChooseAddressHolderData {
        return CartChooseAddressHolderData()
    }

    fun mapCartEmptyUiModel(context: Context?): CartEmptyHolderData {
        return CartEmptyHolderData(
                title = context?.getString(R.string.checkout_module_keranjang_belanja_kosong_new)
                        ?: "",
                desc = context?.getString(R.string.checkout_empty_cart_sub_message_new) ?: "",
                imgUrl = CartConstant.CART_EMPTY_DEFAULT_IMG_URL,
                btnText = context?.getString(R.string.checkout_module_mulai_belanja) ?: ""
        )
    }

    fun mapCartEmptyWithPromoUiModel(context: Context?, lastApplyPromoData: LastApplyPromoData): CartEmptyHolderData {
        var title = context?.getString(R.string.cart_empty_with_promo_title) ?: ""
        var desc = context?.getString(R.string.cart_empty_with_promo_desc) ?: ""
        var imgUrl = CartConstant.CART_EMPTY_WITH_PROMO_IMG_URL
        val btnText = context?.getString(R.string.cart_empty_with_promo_btn) ?: ""

        if (lastApplyPromoData.additionalInfo.emptyCartInfo.message.isNotEmpty()) title = lastApplyPromoData.additionalInfo.emptyCartInfo.message
        if (lastApplyPromoData.additionalInfo.emptyCartInfo.detail.isNotEmpty()) desc = lastApplyPromoData.additionalInfo.emptyCartInfo.detail
        if (lastApplyPromoData.additionalInfo.emptyCartInfo.imageUrl.isNotEmpty()) imgUrl = lastApplyPromoData.additionalInfo.emptyCartInfo.imageUrl

        return CartEmptyHolderData(
                title = title,
                desc = desc,
                imgUrl = imgUrl,
                btnText = btnText
        )
    }

    fun mapTickerErrorUiModel(cartData: CartData): CartItemTickerErrorHolderData {
        var errorItemCount = 0
        cartData.unavailableSections.forEach { unavailableSection ->
            unavailableSection.unavailableGroups.forEach { unavailableGroup ->
                unavailableGroup.cartDetails.forEach { cartDetail ->
                    errorItemCount += cartDetail.products.size
                }
            }
        }

        return CartItemTickerErrorHolderData(
                errorProductCount = errorItemCount
        )
    }

    fun mapAvailableShopUiModel(cartData: CartData): List<CartShopHolderData> {
        val cartShopHolderDataList = mutableListOf<CartShopHolderData>()
        cartData.availableSection.availableGroupGroups.forEach { availableGroup ->
            val productUiModelList = mutableListOf<CartItemHolderData>()
            availableGroup.cartDetails.forEach { cartDetail ->
                cartDetail.products.forEach { product ->
                    val productUiModel = mapProductUiModel(
                            cartData = cartData,
                            cartDetail = cartDetail,
                            product = product,
                            group = availableGroup,
                            unavailableSection = null
                    )
                    productUiModelList.add(productUiModel)
                }
            }
            val shopUiModel = CartShopHolderData().apply {
                this.productUiModelList = productUiModelList
                cartString = availableGroup.cartString
                shopId = availableGroup.shop.shopId
                shopName = availableGroup.shop.shopName
                isFulfillment = availableGroup.isFulfillment
                fulfillmentName = if (availableGroup.isFulfillment) cartData.tokoCabangInfo.message else availableGroup.shipmentInformation.shopLocation
                fulfillmentBadgeUrl = cartData.tokoCabangInfo.badgeUrl
                estimatedTimeArrival = availableGroup.shipmentInformation.estimation
                isShowPin = availableGroup.pinned.isPinned
                pinCoachmarkMessage = availableGroup.pinned.coachmarkMessage
                isTokoNow = availableGroup.shop.isTokoNow
                preOrderInfo = availableGroup.shipmentInformation.preorder.duration
                incidentInfo = availableGroup.shop.shopAlertMessage
                isFreeShippingExtra = availableGroup.shipmentInformation.freeShippingExtra.eligible
                freeShippingBadgeUrl = when {
                    availableGroup.shipmentInformation.freeShippingExtra.eligible -> {
                        availableGroup.shipmentInformation.freeShippingExtra.badgeUrl
                    }
                    availableGroup.shipmentInformation.freeShipping.eligible -> {
                        availableGroup.shipmentInformation.freeShipping.badgeUrl
                    }
                    else -> {
                        ""
                    }
                }
                maximumWeightWording = availableGroup.shop.maximumWeightWording
                maximumShippingWeight = availableGroup.shop.maximumShippingWeight
                if (availableGroup.checkboxState) {
                    isAllSelected = availableGroup.checkboxState
                    isPartialSelected = false
                } else {
                    isAllSelected = false
                    isPartialSelected = isPartialSelected(availableGroup)
                }
                isCollapsible = isTokoNow && cartData.availableSection.availableGroupGroups.size > 1 && productUiModelList.size > 1
                isCollapsed = isCollapsible
                isError = false
                promoCodes = availableGroup.promoCodes
                shopTypeInfo = availableGroup.shop.shopTypeInfo
            }
            cartShopHolderDataList.add(shopUiModel)
        }

        return cartShopHolderDataList
    }

    private fun isPartialSelected(availableGroup: AvailableGroup): Boolean {
        cartDetailLoop@ for (cartDetail in availableGroup.cartDetails) {
            productLoop@ for (product in cartDetail.products) {
                if (product.isCheckboxState) {
                    return true
                }
            }
        }

        return false
    }

    fun mapUnavailableShopUiModel(context: Context?, cartData: CartData): Pair<List<Any>, DisabledAccordionHolderData?> {
        val unavailableSectionList = mutableListOf<Any>()

        val disabledItemHeaderUiModel = mapDisabledItemHeaderUiModel(cartData)
        unavailableSectionList.add(disabledItemHeaderUiModel)

        var showAccordion = false
        if (cartData.unavailableSections.size > 1) {
            showAccordion = true
        }

        cartData.unavailableSections.forEach { unavailableSection ->
            val disabledReasonHolderData = mapDisabledReasonUiModel(unavailableSection)
            unavailableSectionList.add(disabledReasonHolderData)
            if (!showAccordion && unavailableSection.unavailableGroups.isNotEmpty()) {
                if (unavailableSection.unavailableGroups.size > 1) {
                    showAccordion = true
                } else {
                    loop@ for (unavailableGroup in unavailableSection.unavailableGroups) {
                        if (unavailableGroup.cartDetails.size > 1) {
                            showAccordion = true
                            break@loop
                        } else {
                            innerLoop@ for (cartDetail in unavailableGroup.cartDetails) {
                                if ((cartDetail.bundleDetail.bundleId.isBlank() || cartDetail.bundleDetail.bundleId == "0") && cartDetail.products.size > 1) {
                                    showAccordion = true
                                    break@loop
                                }
                            }
                        }
                    }
                }
            }
            unavailableSection.unavailableGroups.forEach { unavailableGroup ->
                val productUiModelList = mutableListOf<CartItemHolderData>()
                unavailableGroup.cartDetails.forEach { cartDetail ->
                    cartDetail.products.forEach { product ->
                        val productUiModel = mapProductUiModel(
                                cartData = cartData,
                                cartDetail = cartDetail,
                                product = product,
                                group = unavailableGroup,
                                unavailableSection = unavailableSection
                        )
                        productUiModelList.add(productUiModel)
                    }
                }
                val shopUiModel = CartShopHolderData().apply {
                    this.productUiModelList = productUiModelList
                    cartString = unavailableGroup.cartString
                    shopId = unavailableGroup.shop.shopId
                    shopName = unavailableGroup.shop.shopName
                    isFulfillment = unavailableGroup.isFulfillment
                    fulfillmentName = if (unavailableGroup.isFulfillment) cartData.tokoCabangInfo.message else unavailableGroup.shipmentInformation.shopLocation
                    fulfillmentBadgeUrl = cartData.tokoCabangInfo.badgeUrl
                    estimatedTimeArrival = unavailableGroup.shipmentInformation.estimation
                    isShowPin = false
                    pinCoachmarkMessage = ""
                    isTokoNow = unavailableGroup.shop.isTokoNow
                    preOrderInfo = unavailableGroup.shipmentInformation.preorder.duration
                    incidentInfo = unavailableGroup.shop.shopAlertMessage
                    isFreeShippingExtra = unavailableGroup.shipmentInformation.freeShippingExtra.eligible
                    freeShippingBadgeUrl = when {
                        unavailableGroup.shipmentInformation.freeShippingExtra.eligible -> {
                            unavailableGroup.shipmentInformation.freeShippingExtra.badgeUrl
                        }
                        unavailableGroup.shipmentInformation.freeShipping.eligible -> {
                            unavailableGroup.shipmentInformation.freeShipping.badgeUrl
                        }
                        else -> {
                            ""
                        }
                    }
                    maximumWeightWording = unavailableGroup.shop.maximumWeightWording
                    maximumShippingWeight = unavailableGroup.shop.maximumShippingWeight
                    isAllSelected = false
                    isPartialSelected = false
                    isCollapsible = isTokoNow && cartData.availableSection.availableGroupGroups.size > 1 && productUiModelList.size > 1
                    isCollapsed = isCollapsible
                    isError = true
                }
                unavailableSectionList.add(shopUiModel)
            }
        }

        if (showAccordion) {
            val accordionUiModel = mapDisabledAccordionUiModel(context, cartData)
            unavailableSectionList.add(accordionUiModel)

            return Pair(unavailableSectionList, accordionUiModel)
        }

        return Pair(unavailableSectionList, null)
    }

    private fun mapDisabledItemHeaderUiModel(cartData: CartData): DisabledItemHeaderHolderData {
        var errorItemCount = 0
        cartData.unavailableSections.forEach { unavailableSection ->
            unavailableSection.unavailableGroups.forEach { unavailableGroup ->
                unavailableGroup.cartDetails.forEach { cartDetail ->
                    errorItemCount += cartDetail.products.size
                }
            }
        }

        return DisabledItemHeaderHolderData(
                disabledItemCount = errorItemCount
        )
    }

    private fun mapDisabledReasonUiModel(unavailabeSection: UnavailableSection): DisabledReasonHolderData {
        return DisabledReasonHolderData().apply {
            title = unavailabeSection.title
            subTitle = unavailabeSection.unavailableDescription
        }
    }

    private fun mapDisabledAccordionUiModel(context: Context?, cartData: CartData): DisabledAccordionHolderData {
        return DisabledAccordionHolderData(
                isCollapsed = true,
                showLessWording = cartData.unavailableSectionAction.find {
                    return@find it.id == Action.ACTION_SHOWLESS
                }?.message ?: context?.getString(R.string.cart_default_wording_show_less) ?: "",
                showMoreWording = cartData.unavailableSectionAction.find {
                    return@find it.id == Action.ACTION_SHOWMORE
                }?.message ?: context?.getString(R.string.cart_default_wording_show_more) ?: ""
        )
    }

    private fun mapProductUiModel(cartData: CartData,
                                  cartDetail: CartDetail,
                                  product: Product,
                                  group: Any,
                                  unavailableSection: UnavailableSection?): CartItemHolderData {
        return CartItemHolderData().apply {
            when (group) {
                is AvailableGroup -> {
                    isTokoNow = group.shop.isTokoNow
                    shopId = group.shop.shopId
                    shopTypeInfoData = group.shop.shopTypeInfo
                    shopName = group.shop.shopName
                    cartString = group.cartString
                    isFulfillment = group.isFulfillment
                    shouldValidateWeight = group.shop.maximumShippingWeight > 0.0 && group.shop.maximumWeightWording.isNotEmpty()
                }
                is UnavailableGroup -> {
                    isTokoNow = group.shop.isTokoNow
                    shopId = group.shop.shopId
                    shopTypeInfoData = group.shop.shopTypeInfo
                    shopName = group.shop.shopName
                    cartString = group.cartString
                }
            }
            if (unavailableSection != null) {
                errorType = unavailableSection.title
                selectedUnavailableActionId = unavailableSection.selectedUnavailableActionId
                selectedUnavailableActionLink = product.selectedUnavailableActionLink
                actionsData = unavailableSection.actions
                isError = true
            } else {
                actionsData = cartData.availableSection.actions
                isError = false
            }
            isSelected = product.isCheckboxState
            productName = product.productName
            productImage = product.productImage.imageSrc100Square
            productId = product.productId
            productInformation = product.productInformation
            productAlertMessage = product.productAlertMessage
            productPrice = product.productPrice
            productOriginalPrice = product.productOriginalPrice
            productInitialPriceBeforeDrop = product.initialPrice
            productSlashPriceLabel = product.slashPriceLabel
            productQtyLeft = product.productWarningMessage
            variant = if (product.variantDescriptionDetail.variantNames.isNotEmpty()) product.variantDescriptionDetail.variantNames.joinToString(", ") else ""
            wholesalePriceData = product.wholesalePrice.asReversed()
            isPreOrder = product.isPreorder == 1
            isWishlisted = product.isWishlisted
            cartId = product.cartId
            isCod = product.isCod
            productWeight = product.productWeight
            productCashBack = product.productCashback
            notes = product.productNotes
            originalNotes = notes
            maxNotesLength = cartData.maxCharNote
            isBundlingItem = cartDetail.bundleDetail.bundleId.isNotBlankOrZero()
            if (isBundlingItem) {
                parentId = if (product.parentId.isBlank() || product.parentId == "0") product.productId + cartDetail.bundleDetail.bundleId else product.parentId
                isMultipleBundleProduct = cartDetail.products.size > 1
                minOrder = cartDetail.bundleDetail.bundleMinOrder
                maxOrder = if (cartDetail.bundleDetail.bundleQuota > BUNDLE_NO_VARIANT_CONST) {
                    min(cartDetail.bundleDetail.bundleMaxOrder, cartDetail.bundleDetail.bundleQuota)
                } else {
                    cartDetail.bundleDetail.bundleMaxOrder
                }
                quantity = getBundleProductQuantity(cartDetail, product)
                bundleId = cartDetail.bundleDetail.bundleId
                bundleType = cartDetail.bundleDetail.bundleType
                bundleGroupId = cartDetail.bundleDetail.bundleGroupId
                val tmpBundleQuantity = when {
                    minOrder > cartDetail.bundleDetail.bundleQty -> minOrder
                    maxOrder < cartDetail.bundleDetail.bundleQty -> maxOrder
                    else -> cartDetail.bundleDetail.bundleQty
                }
                bundleQuantity = tmpBundleQuantity
                originalBundleQuantity = tmpBundleQuantity
                bundleLabelQuantity = product.productMinOrder
                bundleTitle = cartDetail.bundleDetail.bundleName
                bundlePrice = cartDetail.bundleDetail.bundlePrice
                bundleSlashPriceLabel = cartDetail.bundleDetail.slashPriceLabel
                bundleOriginalPrice = cartDetail.bundleDetail.bundleOriginalPrice
                editBundleApplink = cartDetail.bundleDetail.editBundleApplink
                bundleIconUrl = cartDetail.bundleDetail.bundleIconUrl
                bundlingItemPosition = if (cartDetail.products.firstOrNull()?.productId == productId) {
                    CartItemHolderData.BUNDLING_ITEM_HEADER
                } else if (cartDetail.products.lastOrNull()?.productId == productId) {
                    CartItemHolderData.BUNDLING_ITEM_FOOTER
                } else {
                    CartItemHolderData.BUNDLING_ITEM_DEFAULT
                }
            } else {
                parentId = if (product.parentId.isBlank() || product.parentId == "0") product.productId else product.parentId
                bundleId = "0"
                minOrder = product.productMinOrder
                maxOrder = if (product.productSwitchInvenage == 0) {
                    product.productMaxOrder
                } else {
                    min(product.productMaxOrder, product.productInvenageValue)
                }
                quantity = if (product.productSwitchInvenage == 0) {
                    product.productQuantity
                } else {
                    min(product.productQuantity, product.productInvenageValue)
                }
            }
            originalQty = quantity
            categoryId = product.categoryId
            trackerAttribution = product.productTrackerData.attribution
            trackerListName = product.productTrackerData.trackerListName
            category = product.category
            val promoAnalyticsData = getPromoAnalitics(cartData.promo.lastApplyPromo.lastApplyPromoData, product.productId)
            promoCodes = promoAnalyticsData.first
            promoDetails = promoAnalyticsData.second
            isFreeShippingExtra = product.freeShippingExtra.eligible
            isFreeShipping = product.freeShipping.eligible
            campaignId = product.campaignId
            warehouseId = product.warehouseId
        }
    }

    private fun getBundleProductQuantity(cartDetail: CartDetail, product: Product): Int {
        if (cartDetail.bundleDetail.bundleQty > 0) {
            val tmpQty = product.productQuantity / cartDetail.bundleDetail.bundleQty
            if (tmpQty > 0) {
                return tmpQty
            } else {
                return 1
            }
        } else {
            return 1
        }
    }

    private fun getPromoAnalitics(lastApplyPromoData: LastApplyPromoData, productId: String): Pair<String, String> {
        var promoCodes = ""
        var promoDetails = ""
        if (lastApplyPromoData.trackingDetails.isNotEmpty()) {
            for (trackingDetail in lastApplyPromoData.trackingDetails) {
                if (productId == trackingDetail.productId) {
                    promoCodes = trackingDetail.promoCodesTracking
                    promoDetails = trackingDetail.promoDetailsTracking
                }
            }
        }

        return Pair(promoCodes, promoDetails)
    }

    fun mapLastApplySimplified(lastApplyPromoData: LastApplyPromoData): LastApplyUiModel {
        return LastApplyUiModel(
                codes = lastApplyPromoData.codes,
                voucherOrders = mapListVoucherOrders(lastApplyPromoData.listVoucherOrders),
                additionalInfo = mapAdditionalInfo(lastApplyPromoData.additionalInfo),
                message = mapMessageGlobalPromo(lastApplyPromoData.message),
                benefitSummaryInfo = mapBenefitSummaryInfo(lastApplyPromoData.benefitSummaryInfo)
        )
    }

    private fun mapBenefitSummaryInfo(benefitSummaryInfo: BenefitSummaryInfo): BenefitSummaryInfoUiModel {
        return BenefitSummaryInfoUiModel().apply {
            finalBenefitAmountStr = benefitSummaryInfo.finalBenefitAmountStr
            finalBenefitAmount = benefitSummaryInfo.finalBenefitAmount
            finalBenefitText = benefitSummaryInfo.finalBenefitText
            summaries = mapSummariesItemUiModel(benefitSummaryInfo.summaries)
        }
    }

    private fun mapSummariesItemUiModel(summariesItemList: List<SummariesItem>): List<SummariesItemUiModel> {
        val summariesItemUiModelList = ArrayList<SummariesItemUiModel>()
        summariesItemList.forEach { summariesItem ->
            val summariesItemUiModel = SummariesItemUiModel().apply {
                amount = summariesItem.amount
                sectionName = summariesItem.sectionName
                description = summariesItem.description
                sectionDescription = summariesItem.sectionDescription
                type = summariesItem.type
                amountStr = summariesItem.amountStr
            }
            summariesItemUiModelList.add(summariesItemUiModel)
        }

        return summariesItemUiModelList
    }

    private fun mapListVoucherOrders(listVoucherOrdersItem: List<VoucherOrders>): List<LastApplyVoucherOrdersItemUiModel> {
        val listVoucherOrders: ArrayList<LastApplyVoucherOrdersItemUiModel> = arrayListOf()
        listVoucherOrdersItem.forEach {
            listVoucherOrders.add(mapVoucherOrders(it))
        }
        return listVoucherOrders
    }

    private fun mapVoucherOrders(voucherOrders: VoucherOrders): LastApplyVoucherOrdersItemUiModel {
        return LastApplyVoucherOrdersItemUiModel(
                code = voucherOrders.code,
                uniqueId = voucherOrders.uniqueId,
                message = mapMessage(voucherOrders.message)
        )
    }

    private fun mapMessage(message: MessageVoucherOrders): LastApplyMessageUiModel {
        return LastApplyMessageUiModel(
                color = message.color,
                state = message.state,
                text = message.text)
    }

    private fun mapMessageGlobalPromo(message: MessageGlobalPromo): LastApplyMessageUiModel {
        return LastApplyMessageUiModel(
                color = message.color,
                state = message.state,
                text = message.text)
    }

    private fun mapAdditionalInfo(promoAdditionalInfo: PromoAdditionalInfo): LastApplyAdditionalInfoUiModel {
        return LastApplyAdditionalInfoUiModel(
                messageInfo = mapMessageInfo(promoAdditionalInfo.messageInfo),
                errorDetail = mapErrorDetail(promoAdditionalInfo.errorDetail),
                emptyCartInfo = mapEmptyCartInfo(promoAdditionalInfo.emptyCartInfo)
        )
    }

    private fun mapMessageInfo(promoMessageInfo: PromoMessageInfo): LastApplyMessageInfoUiModel {
        return LastApplyMessageInfoUiModel(
                detail = promoMessageInfo.detail,
                message = promoMessageInfo.message)
    }

    private fun mapErrorDetail(promoErrorDetail: PromoErrorDetail): LastApplyErrorDetailUiModel {
        return LastApplyErrorDetailUiModel(
                message = promoErrorDetail.message)
    }

    private fun mapEmptyCartInfo(promoEmptyCartInfo: PromoEmptyCartInfo): LastApplyEmptyCartInfoUiModel {
        return LastApplyEmptyCartInfoUiModel(
                imgUrl = promoEmptyCartInfo.imageUrl,
                message = promoEmptyCartInfo.message,
                detail = promoEmptyCartInfo.detail
        )
    }

    fun mapSummaryTransactionUiModel(cartData: CartData): SummaryTransactionUiModel {
        return SummaryTransactionUiModel().apply {
            totalWording = cartData.shoppingSummary.totalWording
            discountTotalWording = cartData.shoppingSummary.discountTotalWording
            paymentTotalWording = cartData.shoppingSummary.paymentTotalWording
            promoWording = cartData.shoppingSummary.promoWording
            sellerCashbackWording = cartData.shoppingSummary.sellerCashbackWording
        }
    }

    fun mapPromoSummaryUiModel(promoSummary: PromoSummary): PromoSummaryData {
        return PromoSummaryData(
                title = promoSummary.title,
                details = promoSummary.details.map {
                    PromoSummaryDetailData(
                            description = it.description,
                            type = it.type,
                            amountStr = it.amountStr,
                            amount = it.amount,
                            currencyDetailStr = it.currencyDetailStr
                    )
                }.toMutableList()
        )
    }
}