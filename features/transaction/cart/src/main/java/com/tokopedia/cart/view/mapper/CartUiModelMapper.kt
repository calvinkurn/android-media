package com.tokopedia.cart.view.mapper

import android.content.Context
import com.tokopedia.cart.R
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.promo.MessageGlobalPromo
import com.tokopedia.cart.data.model.response.promo.MessageVoucherOrders
import com.tokopedia.cart.data.model.response.promo.PromoAdditionalInfo
import com.tokopedia.cart.data.model.response.promo.PromoEmptyCartInfo
import com.tokopedia.cart.data.model.response.promo.PromoErrorDetail
import com.tokopedia.cart.data.model.response.promo.PromoMessageInfo
import com.tokopedia.cart.data.model.response.promo.VoucherOrders
import com.tokopedia.cart.data.model.response.shopgroupsimplified.AddOn
import com.tokopedia.cart.data.model.response.shopgroupsimplified.AvailableGroup
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartDetail
import com.tokopedia.cart.data.model.response.shopgroupsimplified.GiftingAddOn
import com.tokopedia.cart.data.model.response.shopgroupsimplified.GroupShopCart
import com.tokopedia.cart.data.model.response.shopgroupsimplified.Product
import com.tokopedia.cart.data.model.response.shopgroupsimplified.ProductLabel
import com.tokopedia.cart.data.model.response.shopgroupsimplified.Shop
import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopShipment
import com.tokopedia.cart.data.model.response.shopgroupsimplified.UnavailableGroup
import com.tokopedia.cart.data.model.response.shopgroupsimplified.UnavailableSection
import com.tokopedia.cart.view.helper.DateHelper
import com.tokopedia.cart.view.uimodel.CartAddOnData
import com.tokopedia.cart.view.uimodel.CartAddOnProductData
import com.tokopedia.cart.view.uimodel.CartAddOnWidgetData
import com.tokopedia.cart.view.uimodel.CartBmGmTickerData
import com.tokopedia.cart.view.uimodel.CartDetailInfo
import com.tokopedia.cart.view.uimodel.CartEmptyHolderData
import com.tokopedia.cart.view.uimodel.CartGroupBmGmHolderData
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartProductBenefitData
import com.tokopedia.cart.view.uimodel.CartProductLabelData
import com.tokopedia.cart.view.uimodel.CartPurchaseBenefitData
import com.tokopedia.cart.view.uimodel.CartShopBottomHolderData
import com.tokopedia.cart.view.uimodel.CartShopCoachmarkPlusData
import com.tokopedia.cart.view.uimodel.CartShopGroupTickerData
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.cart.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cart.view.uimodel.DisabledItemHeaderHolderData
import com.tokopedia.cart.view.uimodel.DisabledReasonHolderData
import com.tokopedia.cart.view.uimodel.HexColor
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.purchase_platform.common.constant.BmGmConstant.CART_BMGM_STATE_TICKER_ACTIVE
import com.tokopedia.purchase_platform.common.constant.BmGmConstant.CART_DETAIL_TYPE_BMGM
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.EpharmacyConsultationInfoResponse
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.BenefitSummaryInfo
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.SummariesItem
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.BebasOngkirInfo
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.UsageSummaries
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyBebasOngkirInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyEmptyCartInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyErrorDetailUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUsageSummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.BenefitSummaryInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.Ticker
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlin.math.min

object CartUiModelMapper {

    private const val BUNDLE_NO_VARIANT_CONST = -1

    fun mapTickerAnnouncementUiModel(ticker: Ticker): TickerAnnouncementHolderData {
        return TickerAnnouncementHolderData(
            id = ticker.id,
            title = ticker.title,
            message = ticker.message
        )
    }

    fun mapCartEmptyUiModel(context: Context?): CartEmptyHolderData {
        return CartEmptyHolderData(
            title = context?.getString(R.string.checkout_module_keranjang_belanja_kosong_new)
                ?: "",
            desc = context?.getString(R.string.checkout_empty_cart_sub_message_new) ?: "",
            imgUrl = CartConstant.CART_EMPTY_NEW_DEFAULT_IMG_URL,
            btnText = context?.getString(R.string.checkout_module_mulai_belanja) ?: ""
        )
    }

    fun mapCartEmptyWithPromoUiModel(
        context: Context?,
        lastApplyPromoData: LastApplyPromoData
    ): CartEmptyHolderData {
        var title = context?.getString(R.string.cart_empty_with_promo_title) ?: ""
        var desc = context?.getString(R.string.cart_empty_with_promo_desc) ?: ""
        var imgUrl = CartConstant.CART_EMPTY_WITH_PROMO_IMG_URL
        val btnText = context?.getString(R.string.cart_empty_with_promo_btn) ?: ""

        if (lastApplyPromoData.additionalInfo.emptyCartInfo.message.isNotEmpty()) {
            title =
                lastApplyPromoData.additionalInfo.emptyCartInfo.message
        }
        if (lastApplyPromoData.additionalInfo.emptyCartInfo.detail.isNotEmpty()) {
            desc =
                lastApplyPromoData.additionalInfo.emptyCartInfo.detail
        }
        if (lastApplyPromoData.additionalInfo.emptyCartInfo.imageUrl.isNotEmpty()) {
            imgUrl =
                lastApplyPromoData.additionalInfo.emptyCartInfo.imageUrl
        }

        return CartEmptyHolderData(
            title = title,
            desc = desc,
            imgUrl = imgUrl,
            btnText = btnText
        )
    }

    fun mapAvailableGroupUiModel(cartData: CartData): List<Any> {
        val cartGroupHolderDataList = mutableListOf<Any>()
        val firstPlusAvailableGroupIndex =
            cartData.availableSection.availableGroupGroups.indexOfFirst {
                it.shipmentInformation.freeShippingGeneral.isBoTypePlus()
            }
        cartData.availableSection.availableGroupGroups.forEachIndexed { index, availableGroup ->
            val productUiModelList = mutableListOf<CartItemHolderData>()
            val groupShopCount = availableGroup.groupShopCartData.count()
            var cartGroupBmGmHolderData = CartGroupBmGmHolderData()
            availableGroup.groupShopCartData.forEachIndexed { shopIndex, availableShop ->
                val shopUiModel = mapGroupShop(availableShop.shop, availableShop.cartDetails)
                availableShop.cartDetails.forEachIndexed { cartDetailIndex, cartDetail ->
                    cartDetail.products.forEachIndexed { productIndex, product ->
                        val shouldShowBmGmBottomDivider =
                            productIndex == cartDetail.products.lastIndex &&
                                cartDetailIndex < availableShop.cartDetails.lastIndex &&
                                cartDetail.cartDetailInfo.cartDetailType == CART_DETAIL_TYPE_BMGM
                        val productUiModel = mapProductUiModel(
                            cartData = cartData,
                            cartDetail = cartDetail,
                            product = product,
                            group = availableGroup,
                            unavailableSection = null,
                            availableShop = availableShop,
                            shopData = shopUiModel,
                            shouldShowBmGmBottomDivider = shouldShowBmGmBottomDivider
                        ).apply {
                            isShopShown =
                                availableGroup.isUsingOWOCDesign() && cartDetailIndex == 0 && productIndex == 0
                        }
                        productUiModelList.add(productUiModel)
                    }

                    if (cartDetail.cartDetailInfo.cartDetailType == CART_DETAIL_TYPE_BMGM) {
                        cartGroupBmGmHolderData = mapGroupBmGmHolder(
                            availableGroup.cartString,
                            cartDetail,
                            availableShop.cartStringOrder
                        )
                    }
                }
                productUiModelList.lastOrNull()?.isFinalItem = shopIndex == groupShopCount - 1
            }
            val groupUiModel = CartGroupHolderData().apply {
                this.productUiModelList = productUiModelList
                cartString = availableGroup.cartString
                groupType = availableGroup.groupType
                uiGroupType = availableGroup.uiGroupType
                groupName = availableGroup.groupInformation.name
                groupBadge = availableGroup.groupInformation.badgeUrl
                groupAppLink = availableGroup.groupInformation.appLink
                isFulfillment = availableGroup.isFulfillment
                fulfillmentName = availableGroup.groupInformation.description
                fulfillmentBadgeUrl = availableGroup.groupInformation.descriptionBadgeUrl
                estimatedTimeArrival = availableGroup.shipmentInformation.estimation
                isShowPin = availableGroup.pinned.isPinned
                pinCoachmarkMessage = availableGroup.pinned.coachmarkMessage
                isTokoNow = availableGroup.groupShopCartData.getOrNull(0)?.shop?.isTokoNow ?: false
                preOrderInfo = availableGroup.shipmentInformation.preorder.duration
                incidentInfo =
                    availableGroup.groupShopCartData.getOrNull(0)?.shop?.shopAlertMessage ?: ""
                isFreeShippingExtra = availableGroup.shipmentInformation.freeShippingExtra.eligible
                freeShippingBadgeUrl =
                    availableGroup.shipmentInformation.freeShippingGeneral.badgeUrl
                isFreeShippingPlus =
                    availableGroup.shipmentInformation.freeShippingGeneral.isBoTypePlus()
                maximumWeightWording =
                    availableGroup.groupShopCartData.getOrNull(0)?.shop?.maximumWeightWording ?: ""
                maximumShippingWeight =
                    availableGroup.groupShopCartData.getOrNull(0)?.shop?.maximumShippingWeight
                        ?: 0.0
                if (availableGroup.checkboxState) {
                    isAllSelected = availableGroup.checkboxState
                    isPartialSelected = false
                } else {
                    isAllSelected = false
                    isPartialSelected = isPartialSelected(availableGroup)
                }
                isCollapsible =
                    isTokoNow && cartData.availableSection.availableGroupGroups.size > 1 && productUiModelList.size > 1
                isCollapsed = isCollapsible
                isError = false
                promoCodes = availableGroup.promoCodes
                shopShipments = mapShopShipment(
                    availableGroup.groupShopCartData.getOrNull(0)?.shop?.shopShipments
                        ?: emptyList()
                )
                districtId = availableGroup.warehouse.districtId
                postalCode = availableGroup.warehouse.postalCode
                longitude = availableGroup.warehouse.longitude
                latitude = availableGroup.warehouse.latitude
                boMetadata = availableGroup.boMetadata
                cartShopGroupTicker = CartShopGroupTickerData(
                    enableBoAffordability = availableGroup.shipmentInformation.enableBoAffordability,
                    enableCartAggregator = availableGroup.shipmentInformation.enableShopGroupTickerCartAggregator,
                    errorText = cartData.messages.errorBoAffordability
                )
                mapAddOnData(availableGroup.giftingAddOn, availableGroup.epharmacyConsultationInfo)
                warehouseId = availableGroup.warehouse.warehouseId.toLongOrZero()
                isPo = availableGroup.shipmentInformation.preorder.isPreorder
                val lastApplyData =
                    cartData.promo.lastApplyPromo.lastApplyPromoData.listVoucherOrders.firstOrNull {
                        it.cartStringGroup == cartString && it.shippingId > 0 &&
                            it.spId > 0 && it.type == "logistic"
                    }
                boCode = lastApplyData?.code ?: ""
                coachmarkPlus = CartShopCoachmarkPlusData(
                    isShown = cartData.coachmark.plus.isShown && (firstPlusAvailableGroupIndex == index),
                    title = cartData.coachmark.plus.title,
                    content = cartData.coachmark.plus.content
                )
                enablerLabel =
                    if (availableGroup.groupShopCartData.getOrNull(0)?.shop?.enabler?.showLabel == true) {
                        availableGroup.groupShopCartData.getOrNull(
                            0
                        )?.shop?.enabler?.labelName ?: ""
                    } else {
                        ""
                    }
                this.cartGroupBmGmHolderData = cartGroupBmGmHolderData
            }
            cartGroupHolderDataList.add(groupUiModel)
            if (!groupUiModel.isCollapsed) {
                cartGroupHolderDataList.addAll(productUiModelList)
            }
            cartGroupHolderDataList.add(CartShopBottomHolderData(groupUiModel))
        }

        return cartGroupHolderDataList
    }

    private fun CartGroupHolderData.mapAddOnData(
        giftingAddOn: GiftingAddOn,
        epharmacyConsultationInfo: EpharmacyConsultationInfoResponse
    ) {
        if (epharmacyConsultationInfo.tickerText.isNotBlank()) {
            addOnText = epharmacyConsultationInfo.tickerText
            addOnImgUrl = epharmacyConsultationInfo.iconUrl
            addOnId = ""
            addOnType = CartGroupHolderData.ADD_ON_EPHARMACY
        } else {
            addOnText = giftingAddOn.tickerText
            addOnImgUrl = giftingAddOn.iconUrl
            addOnId = if (giftingAddOn.addOnIds.isNotEmpty()) giftingAddOn.addOnIds.first() else ""
            addOnType = CartGroupHolderData.ADD_ON_GIFTING
        }
    }

    private fun mapShopShipment(shopShipments: List<ShopShipment>):
        List<com.tokopedia.logisticcart.shipping.model.ShopShipment> {
        return shopShipments.map { shipment ->
            com.tokopedia.logisticcart.shipping.model.ShopShipment(
                shipId = shipment.shipId,
                shipName = shipment.shipName,
                shipCode = shipment.shipCode,
                shipLogo = shipment.shipLogo,
                shipProds = shipment.shipProds.map {
                    com.tokopedia.logisticcart.shipping.model.ShipProd(
                        shipProdId = it.shipProdId,
                        shipProdName = it.shipProdName,
                        shipGroupName = it.shipGroupName,
                        shipGroupId = it.shipGroupId,
                        additionalFee = it.additionalFee,
                        minimumWeight = it.minimumWeight
                    )
                },
                isDropshipEnabled = shipment.isDropshipEnabled == 1
            )
        }
    }

    private fun isPartialSelected(availableGroup: AvailableGroup): Boolean {
        shopLoop@ for (groupShop in availableGroup.groupShopCartData) {
            cartDetailLoop@ for (cartDetail in groupShop.cartDetails) {
                productLoop@ for (product in cartDetail.products) {
                    if (product.isCheckboxState) {
                        return true
                    }
                }
            }
        }

        return false
    }

    fun mapUnavailableShopUiModel(
        context: Context?,
        cartData: CartData
    ): Pair<List<Any>, DisabledAccordionHolderData?> {
        val unavailableSectionList = mutableListOf<Any>()

        val disabledItemHeaderUiModel = mapDisabledItemHeaderUiModel(cartData)
        unavailableSectionList.add(disabledItemHeaderUiModel)

        var showAccordion = false

        val totalUnavailableProduct =
            cartData.unavailableSections.sumOf { unavailableSection -> unavailableSection.productsCount }

        if (totalUnavailableProduct > 3) {
            showAccordion = true
        }

        cartData.unavailableSections.forEachIndexed { sectionIndex, unavailableSection ->
            val disabledReasonHolderData = mapDisabledReasonUiModel(unavailableSection)
            unavailableSectionList.add(disabledReasonHolderData)
            unavailableSection.unavailableGroups.forEachIndexed { groupIndex, unavailableGroup ->
                val productUiModelList = mutableListOf<CartItemHolderData>()
                val shopUiModel = mapGroupShop(unavailableGroup.shop, unavailableGroup.cartDetails)
                unavailableGroup.cartDetails.forEach { cartDetail ->
                    cartDetail.products.forEach { product ->
                        val productUiModel = mapProductUiModel(
                            cartData = cartData,
                            cartDetail = cartDetail,
                            product = product,
                            group = unavailableGroup,
                            unavailableSection = unavailableSection,
                            availableShop = null,
                            shopData = shopUiModel,
                            shouldShowBmGmBottomDivider = false

                        )
                        productUiModelList.add(productUiModel)
                    }
                }
                productUiModelList.lastOrNull()?.apply {
                    isFinalItem = true
                    showErrorBottomDivider =
                        sectionIndex != cartData.unavailableSections.lastIndex ||
                        (sectionIndex == cartData.unavailableSections.lastIndex && groupIndex != unavailableSection.unavailableGroups.lastIndex)
                    shouldDivideHalfErrorBottomDivider =
                        showErrorBottomDivider && groupIndex != unavailableSection.unavailableGroups.lastIndex
                }
                val groupUiModel = CartGroupHolderData().apply {
                    this.productUiModelList = productUiModelList
                    cartString = unavailableGroup.cartString
                    groupName = unavailableGroup.shop.shopName
                    groupBadge = unavailableGroup.shop.shopTypeInfo.shopBadge
                    isFulfillment = unavailableGroup.isFulfillment
                    fulfillmentName =
                        if (unavailableGroup.isFulfillment) {
                            cartData.tokoCabangInfo.message
                        } else {
                            unavailableGroup.shipmentInformation.shopLocation
                        }
                    fulfillmentBadgeUrl = cartData.tokoCabangInfo.badgeUrl
                    estimatedTimeArrival = unavailableGroup.shipmentInformation.estimation
                    isShowPin = false
                    pinCoachmarkMessage = ""
                    isTokoNow = unavailableGroup.shop.isTokoNow
                    preOrderInfo = unavailableGroup.shipmentInformation.preorder.duration
                    incidentInfo = unavailableGroup.shop.shopAlertMessage
                    isFreeShippingExtra =
                        unavailableGroup.shipmentInformation.freeShippingExtra.eligible
                    freeShippingBadgeUrl =
                        unavailableGroup.shipmentInformation.freeShippingGeneral.badgeUrl
                    isFreeShippingPlus =
                        unavailableGroup.shipmentInformation.freeShippingGeneral.isBoTypePlus()
                    maximumWeightWording = unavailableGroup.shop.maximumWeightWording
                    maximumShippingWeight = unavailableGroup.shop.maximumShippingWeight
//                    shopTypeInfo = unavailableGroup.shop.shopTypeInfo
                    isAllSelected = false
                    isPartialSelected = false
                    isCollapsible = cartData.availableSection.availableGroupGroups.size > 1 &&
                        productUiModelList.size > 3
                    isCollapsed = isCollapsible
                    isError = true
                    warehouseId = unavailableGroup.warehouse.warehouseId.toLongOrZero()
                    isPo = unavailableGroup.shipmentInformation.preorder.isPreorder
                }
                unavailableSectionList.add(groupUiModel)
                unavailableSectionList.addAll(productUiModelList)
            }
        }

        if (showAccordion) {
            val accordionUiModel = mapDisabledAccordionUiModel(context)
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
            disabledItemCount = errorItemCount,
            isDividerShown = false
        )
    }

    private fun mapDisabledReasonUiModel(unavailabeSection: UnavailableSection): DisabledReasonHolderData {
        return DisabledReasonHolderData().apply {
            title = unavailabeSection.title
            subTitle = unavailabeSection.unavailableDescription
            productsCount = unavailabeSection.productsCount
            showOutOfCoverageTitle = unavailabeSection.unavailableSectionCta.message
            isShowOutOfCoverageAction = unavailabeSection.unavailableSectionCta.id != 0L
        }
    }

    private fun mapDisabledAccordionUiModel(
        context: Context?
    ): DisabledAccordionHolderData {
        return DisabledAccordionHolderData(
            isCollapsed = true,
            showLessWording = context?.getString(R.string.cart_new_default_wording_show_less) ?: "",
            showMoreWording = context?.getString(R.string.cart_new_default_wording_show_more) ?: ""
        )
    }

    private fun mapProductUiModel(
        cartData: CartData,
        cartDetail: CartDetail,
        product: Product,
        group: Any,
        unavailableSection: UnavailableSection?,
        availableShop: GroupShopCart?,
        shopData: CartShopHolderData,
        shouldShowBmGmBottomDivider: Boolean
    ): CartItemHolderData {
        return CartItemHolderData().apply {
            when (group) {
                is AvailableGroup -> {
                    availableShop?.let { availableShop ->
                        isTokoNow = availableShop.shop.isTokoNow
                        cartString = group.cartString
                        cartStringOrder = availableShop.cartStringOrder
                        orderMetadata = availableShop.orderMetadata
                        isFulfillment = group.isFulfillment
                        shouldValidateWeight =
                            availableShop.shop.maximumShippingWeight > 0.0 && availableShop.shop.maximumWeightWording.isNotEmpty()
                    }
                }

                is UnavailableGroup -> {
                    isTokoNow = group.shop.isTokoNow
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
            identifier = generateIdentifier(
                availableShop?.cartStringOrder,
                bundleGroupId,
                cartDetail.cartDetailInfo.bmgmData.offerId,
                cartDetail.cartDetailInfo.bmgmData.offerTypeId
            )
            shopHolderData = shopData
            originWarehouseIds = product.originWarehouseIds
            needPrescription = product.ethicalDrug.needPrescription
            butuhResepText = product.ethicalDrug.text
            butuhResepIconUrl = product.ethicalDrug.iconUrl
            isSelected = product.isCheckboxState
            productName = product.productName
            productImage = product.productImage.imageSrc100Square
            productId = product.productId
            productInformation = product.productInformation
            productInformationWithIcon = product.productInformationWithIcon
            productTagInfo = product.productTagInfo
            productAlertMessage = product.productAlertMessage
            productPrice = product.productPrice
            productOriginalPrice = product.productOriginalPrice
            productInitialPriceBeforeDrop = product.initialPrice
            productSlashPriceLabel = product.slashPriceLabel
            productQtyLeft = product.productWarningMessage
            variant =
                if (product.variantDescriptionDetail.variantNames.isNotEmpty()) {
                    product.variantDescriptionDetail.variantNames.joinToString(
                        ", "
                    )
                } else {
                    ""
                }
            wholesalePriceData = product.wholesalePrice.asReversed()
            isPreOrder = product.isPreorder == 1
            isWishlisted = product.isWishlisted
            cartId = product.cartId
            isCod = product.isCod
            productWeight = product.productWeight
            productCashBack = product.productCashback
            notes = product.productNotes
            originalNotes = notes
            placeholderNote = cartData.placeholderNote
            maxNotesLength = cartData.maxCharNote
            isBundlingItem = cartDetail.bundleDetail.bundleId.isNotBlankOrZero()
            if (isBundlingItem) {
                parentId =
                    if (product.parentId.isBlank() || product.parentId == "0") {
                        product.productId + cartDetail.bundleDetail.bundleId
                    } else {
                        product.parentId
                    }
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
                    maxOrder < cartDetail.bundleDetail.bundleQty -> maxOrder
                    minOrder > cartDetail.bundleDetail.bundleQty -> minOrder
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
                bundleGrayscaleIconUrl = cartDetail.bundleDetail.bundleGrayscaleIconUrl
                bundlingItemPosition =
                    if (cartDetail.products.firstOrNull()?.productId == productId) {
                        CartItemHolderData.BUNDLING_ITEM_HEADER
                    } else if (cartDetail.products.lastOrNull()?.productId == productId) {
                        CartItemHolderData.BUNDLING_ITEM_FOOTER
                    } else {
                        CartItemHolderData.BUNDLING_ITEM_DEFAULT
                    }
            } else {
                parentId =
                    if (product.parentId.isBlank() || product.parentId == "0") product.productId else product.parentId
                bundleId = "0"
                minOrder = product.productMinOrder
                maxOrder = if (product.productSwitchInvenage == 0) {
                    product.productMaxOrder
                } else {
                    min(product.productMaxOrder, product.productInvenageValue)
                }
                val tmpQuantity = when {
                    maxOrder < product.productQuantity -> maxOrder
                    minOrder > product.productQuantity -> minOrder
                    else -> product.productQuantity
                }
                quantity = tmpQuantity
            }
            originalQty = quantity
            categoryId = product.categoryId
            trackerAttribution = product.productTrackerData.attribution
            trackerListName = product.productTrackerData.trackerListName
            category = product.category
            val promoAnalyticsData = getPromoAnalitics(
                cartData.promo.lastApplyPromo.lastApplyPromoData,
                product.productId
            )
            promoCodes = promoAnalyticsData.first
            promoDetails = promoAnalyticsData.second
            isFreeShippingExtra = product.freeShippingExtra.eligible
            isFreeShipping = product.freeShipping.eligible
            freeShippingName = product.freeShippingGeneral.boName
            campaignId = product.campaignId
            warehouseId = product.warehouseId
            bundleIds = product.bundleIds
            addOnsProduct = mapCartAddOnData(product.addOn)
            showBundlePrice = cartData.showBundlePrice
            cartBmGmTickerData = mapCartBmGmTickerData(cartDetail, shopData, productId)
            showBmGmBottomDivider = shouldShowBmGmBottomDivider
            cartProductLabelData = mapProductLabel(product.productLabel)
            productMetadata = product.productMetadata
            isEnableCartVariant = product.isEnableCartVariant
        }
    }

    private fun mapCartAddOnData(addOn: AddOn): CartAddOnData {
        val arrayListAddOnProduct = ArrayList<CartAddOnProductData>()
        addOn.addOnData.forEach {
            val cartAddOnProductData = CartAddOnProductData(
                id = it.addonId,
                uniqueId = it.uniqueId,
                status = it.status,
                type = it.type,
                price = it.price,
                fixedQuantity = it.fixedQuantity
            )
            arrayListAddOnProduct.add(cartAddOnProductData)
        }
        return CartAddOnData().apply {
            listData = arrayListAddOnProduct
            widget = CartAddOnWidgetData(
                title = addOn.addOnWidget.title,
                price = "(${
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    addOn.addOnWidget.price,
                    false
                ).removeDecimalSuffix()
                })",
                leftIconUrl = addOn.addOnWidget.leftIconUrl,
                rightIconUrl = addOn.addOnWidget.rightIconUrl
            )
        }
    }

    private fun getBundleProductQuantity(cartDetail: CartDetail, product: Product): Int {
        if (cartDetail.bundleDetail.bundleQty > 0) {
            val tmpQty = product.productQuantity / cartDetail.bundleDetail.bundleQty
            return if (tmpQty > 0) {
                tmpQty
            } else {
                1
            }
        } else {
            return 1
        }
    }

    private fun getPromoAnalitics(
        lastApplyPromoData: LastApplyPromoData,
        productId: String
    ): Pair<String, String> {
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
            benefitSummaryInfo = mapBenefitSummaryInfo(lastApplyPromoData.benefitSummaryInfo),
            userGroupMetadata = lastApplyPromoData.userGroupMetadata
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

    private fun mapListVoucherOrders(listVoucherOrdersItem: List<VoucherOrders>):
        List<LastApplyVoucherOrdersItemUiModel> {
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
            message = mapMessage(voucherOrders.message),
            cartStringGroup = voucherOrders.cartStringGroup
        )
    }

    private fun mapMessage(message: MessageVoucherOrders): LastApplyMessageUiModel {
        return LastApplyMessageUiModel(
            color = message.color,
            state = message.state,
            text = message.text
        )
    }

    private fun mapMessageGlobalPromo(message: MessageGlobalPromo): LastApplyMessageUiModel {
        return LastApplyMessageUiModel(
            color = message.color,
            state = message.state,
            text = message.text
        )
    }

    private fun mapAdditionalInfo(promoAdditionalInfo: PromoAdditionalInfo): LastApplyAdditionalInfoUiModel {
        return LastApplyAdditionalInfoUiModel(
            messageInfo = mapMessageInfo(promoAdditionalInfo.messageInfo),
            errorDetail = mapErrorDetail(promoAdditionalInfo.errorDetail),
            emptyCartInfo = mapEmptyCartInfo(promoAdditionalInfo.emptyCartInfo),
            usageSummaries = mapUsageSummaries(promoAdditionalInfo.usageSummaries),
            bebasOngkirInfo = mapBebasOngkirInfo(promoAdditionalInfo.bebasOngkirInfo)
        )
    }

    private fun mapMessageInfo(promoMessageInfo: PromoMessageInfo): LastApplyMessageInfoUiModel {
        return LastApplyMessageInfoUiModel(
            detail = promoMessageInfo.detail,
            message = promoMessageInfo.message
        )
    }

    private fun mapErrorDetail(promoErrorDetail: PromoErrorDetail): LastApplyErrorDetailUiModel {
        return LastApplyErrorDetailUiModel(
            message = promoErrorDetail.message
        )
    }

    private fun mapEmptyCartInfo(promoEmptyCartInfo: PromoEmptyCartInfo): LastApplyEmptyCartInfoUiModel {
        return LastApplyEmptyCartInfoUiModel(
            imgUrl = promoEmptyCartInfo.imageUrl,
            message = promoEmptyCartInfo.message,
            detail = promoEmptyCartInfo.detail
        )
    }

    private fun mapUsageSummaries(promoUsageSummaries: List<UsageSummaries>): List<LastApplyUsageSummariesUiModel> {
        return promoUsageSummaries.map {
            LastApplyUsageSummariesUiModel(
                description = it.desc,
                type = it.type,
                amountStr = it.amountStr,
                amount = it.amount,
                currencyDetailsStr = it.currencyDetailsStr
            )
        }
    }

    private fun mapBebasOngkirInfo(bebasOngkirInfo: BebasOngkirInfo): LastApplyBebasOngkirInfoUiModel {
        return LastApplyBebasOngkirInfoUiModel(
            isBoUnstackEnabled = bebasOngkirInfo.isBoUnstackEnabled,
            isUseBebasOngkirOnly = bebasOngkirInfo.isUseBebasOngkirOnly
        )
    }

    private fun mapGroupShop(shop: Shop, cartDetails: List<CartDetail>): CartShopHolderData {
        return CartShopHolderData().apply {
            shopId = shop.shopId
            shopName = shop.shopName
            shopTypeInfo = shop.shopTypeInfo
            isTokoNow = shop.isTokoNow
            incidentInfo = shop.shopAlertMessage
            maximumWeightWording = shop.maximumWeightWording
            shopShipments = mapShopShipment(shop.shopShipments)
            districtId = shop.districtId
            postalCode = shop.postalCode
            latitude = shop.latitude
            longitude = shop.longitude
            poDuration = cartDetails.getOrNull(0)
                ?.products?.getOrNull(0)?.productPreorder?.durationDay?.toString()
                ?: "0"
            enablerLabel = if (shop.enabler.showLabel) shop.enabler.labelName else ""
        }
    }

    private fun mapGroupBmGmHolder(
        cartString: String,
        cartDetail: CartDetail,
        cartStringOrder: String
    ): CartGroupBmGmHolderData {
        return CartGroupBmGmHolderData(
            hasBmGmOffer = true,
            discountBmGmAmount = cartDetail.cartDetailInfo.bmgmData.totalDiscount,
            offerId = cartDetail.cartDetailInfo.bmgmData.offerId,
            offerJsonData = cartDetail.cartDetailInfo.bmgmData.offerJsonData,
            cartBmGmGroupTickerCartString = "$cartString-${cartDetail.cartDetailInfo.bmgmData.offerId}",
            bundleId = cartDetail.bundleDetail.bundleId.toLongOrZero(),
            bundleGroupId = cartDetail.bundleDetail.bundleGroupId,
            cartStringOrder = cartStringOrder
        )
    }

    private fun checkNeedToShowTickerBmGm(cartDetail: CartDetail, productId: String): Boolean {
        val isFirstIndexProduct = if (cartDetail.products.isNotEmpty()) {
            cartDetail.products[0].productId == productId
        } else {
            false
        }
        return cartDetail.cartDetailInfo.cartDetailType == CART_DETAIL_TYPE_BMGM && isFirstIndexProduct
    }

    private fun checkNeedToShowBmGmDivider(cartDetail: CartDetail, productId: String): Boolean {
        val isLastIndexProduct = if (cartDetail.products.isNotEmpty()) {
            if (cartDetail.cartDetailInfo.bmgmData.isValidGiftPurchase()) {
                false
            } else {
                cartDetail.products.last().productId == productId
            }
        } else {
            true
        }
        return cartDetail.cartDetailInfo.cartDetailType == CART_DETAIL_TYPE_BMGM && !isLastIndexProduct
    }

    private fun mapCartBmGmTickerData(
        cartDetail: CartDetail,
        shopData: CartShopHolderData,
        productId: String
    ): CartBmGmTickerData {
        val isLastProduct = cartDetail.products.last().productId == productId
        return CartBmGmTickerData(
            bmGmCartInfoData = mapBmGmProductData(cartDetail, shopData, isLastProduct),
            isShowTickerBmGm = checkNeedToShowTickerBmGm(cartDetail, productId),
            stateTickerBmGm = CART_BMGM_STATE_TICKER_ACTIVE,
            isShowBmGmDivider = checkNeedToShowBmGmDivider(cartDetail, productId)
        )
    }

    private fun mapBmGmProductData(
        cartDetail: CartDetail,
        shopData: CartShopHolderData,
        isLastProduct: Boolean
    ): CartDetailInfo {
        if (cartDetail.cartDetailInfo.cartDetailType == CART_DETAIL_TYPE_BMGM) {
            val listTiersApplied = arrayListOf<CartDetailInfo.BmGmTierProductData>()
            val listProductTiersApplied =
                arrayListOf<CartDetailInfo.BmGmTierProductData.BmGmProductData>()
            cartDetail.cartDetailInfo.bmgmData.tierProductList.forEach { tierProduct ->
                tierProduct.listProduct.forEach { bmGmProduct ->
                    loop@ for (product in cartDetail.products) {
                        if (product.productId == bmGmProduct.productId) {
                            listProductTiersApplied.add(
                                CartDetailInfo.BmGmTierProductData.BmGmProductData(
                                    cartId = bmGmProduct.cartId,
                                    shopId = shopData.shopId,
                                    productId = bmGmProduct.productId,
                                    warehouseId = bmGmProduct.warehouseId,
                                    qty = bmGmProduct.quantity,
                                    finalPrice = bmGmProduct.priceBeforeBenefit,
                                    checkboxState = product.isCheckboxState
                                )
                            )
                            break@loop
                        }
                    }
                }

                val purchaseBenefitProducts = tierProduct.productsBenefit.map { product ->
                    CartProductBenefitData(
                        id = product.productId,
                        name = product.productName,
                        imageUrl = product.productImage,
                        qty = product.quantity,
                        originalPrice = product.originalPrice,
                        finalPrice = product.finalPrice
                    )
                }.toMutableList()

                val isShown =
                    cartDetail.cartDetailInfo.bmgmData.isGiftWithPurchase() && tierProduct.productsBenefit.isNotEmpty() && isLastProduct

                val purchaseBenefitData = CartPurchaseBenefitData(
                    isShown = isShown,
                    benefitWording = tierProduct.benefitWording,
                    actionWording = tierProduct.actionWording,
                    purchaseBenefitProducts = purchaseBenefitProducts
                )

                listTiersApplied.add(
                    CartDetailInfo.BmGmTierProductData(
                        tierId = tierProduct.tierId,
                        listProduct = listProductTiersApplied,
                        purchaseBenefitData = purchaseBenefitData
                    )
                )
            }
            return CartDetailInfo(
                cartDetailType = cartDetail.cartDetailInfo.cartDetailType,
                bmGmData = CartDetailInfo.BmGmData(
                    offerId = cartDetail.cartDetailInfo.bmgmData.offerId,
                    offerTypeId = cartDetail.cartDetailInfo.bmgmData.offerTypeId,
                    offerName = cartDetail.cartDetailInfo.bmgmData.offerName,
                    offerIcon = cartDetail.cartDetailInfo.bmgmData.offerIcon,
                    offerMessage = cartDetail.cartDetailInfo.bmgmData.offerMessage,
                    offerLandingPageLink = cartDetail.cartDetailInfo.bmgmData.offerLandingPageLink,
                    totalDiscount = cartDetail.cartDetailInfo.bmgmData.totalDiscount,
                    offerJsonData = cartDetail.cartDetailInfo.bmgmData.offerJsonData
                ),
                bmGmTierProductList = listTiersApplied
            )
        } else {
            return CartDetailInfo()
        }
    }

    private fun mapProductLabel(productLabel: ProductLabel): CartProductLabelData {
        val expiredTime = DateHelper.toDate(productLabel.labelDetail.timer.expiredTime)?.time ?: 0L
        val serverTime = DateHelper.toDate(productLabel.labelDetail.timer.serverTime)?.time ?: 0L
        val remainingTimeMillis = expiredTime - serverTime
        return CartProductLabelData(
            type = productLabel.labelType,
            localExpiredTimeMillis = System.currentTimeMillis() + remainingTimeMillis,
            imageLogoUrl = productLabel.labelDetail.assetLabel.imageAsset.imageLabel,
            iconUrl = productLabel.labelDetail.assetLabel.textAsset.squareIcon,
            text = productLabel.labelDetail.assetLabel.textAsset.label,
            textColor = HexColor(productLabel.labelDetail.assetLabel.textAsset.fontColor),
            backgroundStartColor = HexColor(productLabel.labelDetail.assetLabel.textAsset.backgroundStartColor),
            backgroundEndColor = HexColor(productLabel.labelDetail.assetLabel.textAsset.backgroundEndColor),
            lineColor = HexColor(productLabel.labelDetail.assetLabel.textAsset.lineColor),
            alwaysShowTimer = remainingTimeMillis.isMoreThanZero()
        )
    }

    private fun generateIdentifier(
        cartStringOrder: String?,
        bundleGroupId: String,
        offerId: Long,
        offerTypeId: Long
    ): String {
        return "$cartStringOrder|$bundleGroupId|$offerId|$offerTypeId"
    }
}
