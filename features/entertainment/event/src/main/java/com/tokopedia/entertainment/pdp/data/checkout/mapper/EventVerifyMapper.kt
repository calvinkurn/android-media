package com.tokopedia.entertainment.pdp.data.checkout.mapper

import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.promocheckout.common.domain.model.event.*

object EventVerifyMapper {

    fun getVerifyBody(name: String, email: String, groupID: Int, packageID: Int, scheduleID: Int,
                      productID: Int, categoryId: Int, providerId: Int, quantity: Int, pricePerSeat: Int, totalPrice: Int, digitalProductID: Int,
                      entityPessanggers: List<EntityPassengerVerify>, promocode: String): EventVerifyBody {
        return EventVerifyBody(cartItems = listOf(CartItemVerify(
                configuration = ConfigurationVerify(
                        price = totalPrice,
                        subConfig = SubConfigVerify(
                                name = name
                        )
                ),
                metaData = MetaDataVerify(
                        entityCategoryId = categoryId,
                        entityCategoryName = "",
                        entityGroupId = groupID,
                        entityPackages = listOf(EntityPackageVerify(
                                packageId = packageID,
                                quantity = quantity,
                                description = "",
                                pricePerSeat = pricePerSeat,
                                sessionID = "",
                                productId = productID,
                                groupId = groupID,
                                scheduleId = scheduleID,
                                areaID = ""
                        )
                        ),
                        totalTicketCount = 1, //keep as one, backend need
                        entityProductId = productID,
                        entityProviderId = providerId,
                        entityScheduleId = scheduleID,
                        entityPassengers = entityPessanggers,
                        entityAddress = EntityAddressVerify(
                                name = "",
                                address = "",
                                city = "",
                                email = email,
                                mobile = "",
                                latitude = "",
                                longitude = ""
                        ),
                        citySearched = "",
                        entityEndTime = "",
                        entityStartTime = "",
                        taxPerQuantity = listOf(
                                TaxPerQuantity(
                                        entertainment = 0
                                ),
                                TaxPerQuantity(
                                        service = 0
                                )
                        ),
                        otherCharges = listOf(
                                OtherCharge(
                                        convFee = 0
                                )
                        ),
                        totalTaxAmount = 0,
                        totalOtherCharges = 0,
                        totalTicketPrice = totalPrice,
                        entityImage = "",
                        tncApproved = false
                ),
                quantity = 1, //keep as one, backend need
                productId = digitalProductID
        )
        ),
                promocode = promocode,
                deviceId = ""
        )
    }

    fun getEntityPessangerVerify(forms: List<Form>): List<EntityPassengerVerify> {
        val list: MutableList<EntityPassengerVerify> = mutableListOf()
        if (!forms.isNullOrEmpty()) {
            for (i in 0..forms.size - 1) {
                forms[i].apply {
                    val pessanger = EntityPassengerVerify(
                            id = id.toInt(), productId = productId.toInt(),
                            name = name, title = title, value = value, validatorRegex = validatorRegex,
                            elementType = elementType, required = required.toString(), errorMessage = errorMessage
                    )
                    list.add(pessanger)
                }
            }
        }
        return list
    }
}
