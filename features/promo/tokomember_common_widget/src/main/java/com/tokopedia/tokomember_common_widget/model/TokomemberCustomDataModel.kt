package com.tokopedia.tokomember_common_widget.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TokomemberShopCardModel(
    var shopName: String = "",
    var shopMemberName: String = "",
    var shopType: Int = 0,
    var startDate: String = "",
    var endData: String = "",
    var numberOfLevel : Int = 0,
    var fontColor : String ="",
    var backgroundColor: String ="",
    var backgroundImgUrl:String="",
    var shopIconUrl:String = ""
):Parcelable

data class TokomemberProgramCardModel(
    var programStatus: Int = 0,
    var programStartDate: String = "",
    var programStartTime: String = "",
    var programEndTime: String = "",
    var programEndDate: String = "",
    var programMember: String = "",
    var programTransaction: String = ""
)