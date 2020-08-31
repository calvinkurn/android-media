package com.tokopedia.entertainment.pdp.data.checkout.mapper

import com.tokopedia.promocheckout.common.domain.model.event.*

object EventCheckoutMapper {
    fun mapToCart(cart: Cart): Cart {
        cart.apply {
            return Cart(
                    bankCode = bankCode,
                    error = error,
                    cartItems = mapCartItem(cartItems),
                    cashbackAmount = cashbackAmount,
                    count = count,
                    displayPrice = displayPrice,
                    grandTotal = grandTotal,
                    orderSubtitle = orderSubtitle,
                    orderTitle = orderTitle,
                    promocode = promocode,
                    promocodeCashback = promocodeCashback,
                    promocodeDiscount = promocodeDiscount,
                    promocodeFailureMessage = promocodeFailureMessage,
                    promocodeStatus = promocodeStatus,
                    promocodeSuccessMessage = promocodeSuccessMessage,
                    totalConvFee = totalConvFee,
                    totalPrice = totalPrice,
                    user = user
            )
        }
    }

    fun mapCartItem(list: List<CartItem>): List<CartItem> {
        val list = list.map {
            CartItem(
                    address = it.address,
                    appLink = it.appLink,
                    categoryId = it.categoryId.toInt(),
                    configuration = it.configuration,
                    discount = it.discount.toInt(),
                    discountedPrice = it.discountedPrice.toInt(),
                    displaySequence = it.displaySequence.toInt(),
                    fulfillmentServiceId = it.fulfillmentServiceId.toInt(),
                    imageUrl = it.imageUrl,
                    itemId = it.itemId,
                    metaData = mapMetaData(it.metaData),
                    mrp = it.mrp.toInt(),
                    price = it.price.toInt(),
                    productId = it.productId.toInt(),
                    product = it.product,
                    productName = it.productName,
                    quantity = it.quantity.toInt(),
                    title = it.title,
                    totalPrice = it.totalPrice.toInt(),
                    url = it.url,
                    verticalName = it.verticalName
            )
        }
        return list
    }

    fun mapMetaData(metadata: MetaData): MetaData {
        return metadata.run {
            MetaData(
                    citySearched = citySearched,
                    endDate = endDate,
                    entityAddress = entityAddress,
                    entityCategoryId = entityCategoryId.toInt(),
                    entityCategoryName = entityCategoryName,
                    entityEndTime = entityEndTime,
                    entityGroupId = entityGroupId.toInt(),
                    entityImage = entityImage,
                    entityProductId = entityProductId.toInt(),
                    entityProductName = entityProductName,
                    entityProviderId = entityProviderId.toInt(),
                    entityScheduleId = entityScheduleId.toInt(),
                    entityStartTime = entityStartTime,
                    integratorSp = integratorSp,
                    integratorTxnId = integratorTxnId,
                    minStartDate = minStartDate,
                    orderTraceId = orderTraceId,
                    otherCharges = otherCharges,
                    seoUrl = seoUrl,
                    startDate = startDate,
                    taxPerQuantity = taxPerQuantity,
                    tncApproved = tncApproved,
                    totalOtherCharges = totalOtherCharges.toInt(),
                    totalTaxAmount = totalTaxAmount.toInt(),
                    totalTicketPrice = totalTicketPrice.toInt(),
                    totalTicketCount = totalTicketCount.toInt(),
                    verticalId = verticalId.toInt(),
                    entityPassengers = entityPassengers,
                    entityPackages = entityPackages
            )
        }
    }
}