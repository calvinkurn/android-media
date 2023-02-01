package com.tokopedia.product.estimasiongkir.tracking

object SellyTracking {

    /**
     * You may update the value with data.
     * Suggestions:
     *  eventLabel - "product_id:{{product_id}};shop_district_id:{{shop_district_id}};buyer_district_id:{{buyer_district_id}};berat_satuan:{{berat_satuan}};"
     *	component - "comp:{component name};temp:{template name};elem:{element name};cpos:{component position}; //component level attribute"
     *	layout - "layout:{layout name};catName:{category name};catId:{category id}; //layout level attribute"
     *	productId - "{Product ID} //Product ID of product displayed on PDP"
     *	creativeName - "{price //ex:Rp0, kuota habis}"
     *	creativeSlot - "{this is integer}"
     *	itemId - "{type //ex:pengiriman instant, pengiriman terjadwal}"
     *	itemName - "{date //ex:hari ini, besok 10 Agu, Sabtu 11 Agu} - {time //ex:tiba dalam 2 jam, Tiba 12.00 - 14.00}"
     *	shopId - "{shop_id} //shop_id level hit"
     *	userId - "{user_id} //user_id level hit, pass null if non login"
     */
    fun impressScheduledDelivery(
        eventLabel: String,
        component: String,
        layout: String,
        productId: String,
        creativeName: String,
        creativeSlot: String,
        itemId: String,
        itemName: String,
        shopId: String,
        userId: String,
        buyerDistrictId: String,
        sellerDistrictId: String
    ) {
        val mapEvent = hashMapOf<String, Any>(
            "event" to "promoView",
            "eventAction" to "impression - scheduled delivery detail bottomsheet",
            "eventCategory" to "product detail page",
            "eventLabel" to "product_id:$productId;shop_district_id:$sellerDistrictId;buyer_district_id:$buyerDistrictId;berat_satuan:{{berat_satuan}};",
            "trackerId" to "40899",
            "businessUnit" to "product detail page",
            "component" to "",
            "currentSite" to "tokopediamarketplace",
            "layout" to "",
            "productId" to productId,
            "ecommerce" to hashMapOf(
                "promoView" to hashMapOf(
                    "promotions" to arrayListOf(
                        hashMapOf(
                            "creative_name" to creativeName,
                            "creative_slot" to creativeSlot,
                            "item_id" to itemId,
                            "item_name" to itemName
                        )
                    )
                )
            ),
            "shopId" to shopId,
            "userId" to userId
        )
    }
}
