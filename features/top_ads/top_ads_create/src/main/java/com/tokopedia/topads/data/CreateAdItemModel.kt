package com.tokopedia.topads.data


open class CreateAdItemModel : CreateAdModel() {
     var adImage: String = ""

     var productURI: String = ""

     var ad: CreateAdModel = CreateAdModel()

     var productID: String = ""

     var adStatusToogle: Int = 0

     var isEnoughDeposit: Boolean = false

     var groupID: String = ""

     var adTitle: String = ""

     var adSchedule: String = ""

     var adEndTime: String = ""

     var toggle: String = ""

     open var source: String = ""

     var adEndDate: String = ""

     var stickerID: String = ""

     var adStartTime: String = ""

     var adStartDate: String = ""

     var shopID: String = ""

}