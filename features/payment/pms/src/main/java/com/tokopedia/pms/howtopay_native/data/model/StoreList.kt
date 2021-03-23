package com.tokopedia.pms.howtopay_native.data.model

object StoreList {
    private val storeArrayList = ArrayList<StoreData>()


    fun getStoreList(): ArrayList<StoreData> {
        if (storeArrayList.isEmpty()) {
            storeArrayList.add(StoreData("Alfamart",
                    "https://ecs7.tokopedia.net/img/toppay/payment-logo/alfamart.png",
                    "Rp2.500"))
            storeArrayList.add(StoreData("Indomaret",
                    "https://ecs7.tokopedia.net/img/toppay/payment-logo/indomaret.png",
                    "Rp2.500"))
            storeArrayList.add(StoreData("Mitra Tokopedia",
                    "https://ecs7.tokopedia.net/img/toppay/sprites/mitra.png",
                    "Rp2.000"))
            storeArrayList.add(StoreData("BRILink",
                    "https://ecs7.tokopedia.net/img/toppay/sprites/brilink.png",
                    "Rp3.000"))
            storeArrayList.add(StoreData("POS Indonesia",
                    "https://ecs7.tokopedia.net/img/toppay/payment-logo/pospay.png",
                    "Rp3.000"))
            storeArrayList.add(StoreData("JNE",
                    "https://ecs7.tokopedia.net/img/toppay/payment-logo/jne-retail.png",
                    "Rp2.000"))
            storeArrayList.add(StoreData("Circle K",
                    "https://ecs7.tokopedia.net/img/toppay/payment-logo/circle-k.png",
                    "Rp2.000"))
            storeArrayList.add(StoreData("Gerai Tokopedia",
                    "https://ecs7.tokopedia.net/img/toppay/payment-logo/tokopediacenter.png",
                    "Gratis"))
            storeArrayList.add(StoreData("FamilyMart",
                    "https://ecs7.tokopedia.net/img/toppay/payment-logo/familymart.png",
                    "Rp2.000"))
            storeArrayList.add(StoreData("Kioson",
                    "https://ecs7.tokopedia.net/img/toppay/payment-logo/kios-on.png",
                    "Rp2.500"))
        }
        return storeArrayList
    }

}


data class StoreData(
        val storeName: String,
        val storeLogo: String,
        val storeFee: String
)