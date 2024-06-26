package com.tokopedia.thankyou_native.presentation.adapter.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.presentation.adapter.factory.GyroRecommendationFactory
import com.tokopedia.tokomember.model.BottomSheetContentItem

data class GyroTokomemberItem(
    var id : Long = 0L,
    @SerializedName("url")
    val url : String?="",
    @SerializedName("appLink")
    val urlApp : String?="",
    @SerializedName("title")
    val title : String?="",
    @SerializedName("description")
    val description : String?="",
    @SerializedName("isOpenBottomSheet")
    val isOpenBottomSheet : Boolean= false,
    @SerializedName("usecase")
    val usecase: String? = "",
    @SerializedName("isShown")
    val isShown: Boolean? = false,
    @SerializedName("imageURL")
    val imageURL: String? = "",
    @SerializedName("membership_type")
    val membershipType : Int = 0,
    @SerializedName("membershipCardId")
    val membershipCardId : String = "",
    var successRegister : Boolean = false,
    var failRegister : Boolean = false,
    val listOfBottomSheetContent: BottomSheetContentItem = BottomSheetContentItem()
) : Visitable<GyroRecommendationFactory>, GyroModel() {

    override fun type(typeFactory: GyroRecommendationFactory): Int {
        return typeFactory.type(this)
    }
}

data class TokomemberModel(
    val listOfTokomemberItem: List<GyroTokomemberItem> = arrayListOf(),
)

const val TOKOMEMBER_WAITING_WIDGET = 0
const val TOKOMEMBER_SUCCESS_WIDGET = 1
const val TOKOMEMBER_SUCCESS_BOTTOMSHEET = 0
