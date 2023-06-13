package com.tokopedia.pms.howtopay.data.model

object StoreList {
    private val storeArrayList = ArrayList<StoreData>()


    fun getStoreList(): ArrayList<StoreData> {
        if (storeArrayList.isEmpty()) {
            storeArrayList.add(StoreData("Alfamart",
                    "https://images.tokopedia.net/img/toppay/payment-logo/alfamart.png",
                    "Rp2.500"))
            storeArrayList.add(StoreData("Indomaret",
                    "https://images.tokopedia.net/img/toppay/payment-logo/indomaret.png",
                    "Rp2.500"))
            storeArrayList.add(StoreData("Mitra Tokopedia",
                    "https://images.tokopedia.net/img/toppay/sprites/mitra.png",
                    "Rp2.000"))
            storeArrayList.add(StoreData("BRILink",
                    "https://images.tokopedia.net/img/toppay/sprites/brilink.png",
                    "Rp3.000"))
            storeArrayList.add(StoreData("POS Indonesia",
                    "https://images.tokopedia.net/img/toppay/payment-logo/pospay.png",
                    "Rp3.000"))
            storeArrayList.add(StoreData("JNE",
                    "https://images.tokopedia.net/img/toppay/payment-logo/jne-retail.png",
                    "Rp2.000"))
            storeArrayList.add(StoreData("Circle K",
                    "https://images.tokopedia.net/img/toppay/payment-logo/circle-k.png",
                    "Rp2.000"))
            storeArrayList.add(StoreData("Gerai Tokopedia",
                    "https://images.tokopedia.net/img/toppay/payment-logo/tokopediacenter.png",
                    "Gratis"))
            storeArrayList.add(StoreData("FamilyMart",
                    "https://images.tokopedia.net/img/toppay/payment-logo/familymart.png",
                    "Rp2.000"))
            storeArrayList.add(StoreData("Kioson",
                    "https://images.tokopedia.net/img/toppay/payment-logo/kios-on.png",
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
