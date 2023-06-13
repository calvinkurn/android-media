package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmMemberListResponse(
    @Expose
    @SerializedName("membershipGetUserCardMemberList" )
    val membershipGetUserCardMemberList : MembershipGetUserCardMemberList? = MembershipGetUserCardMemberList()
)

data class MembershipGetUserCardMemberList (
    @Expose
    @SerializedName("resultStatus")
    val resultStatus:ResultStatus?= ResultStatus(),
    @Expose
    @SerializedName("userCardMemberList")
    val userCardMemberList:UserCardMemberList?= UserCardMemberList(),
    @Expose
    @SerializedName("paging")
    val paging: MembershipPaging?= MembershipPaging()
)

data class UserCardMemberList (
    @Expose
    @SerializedName("sumUserCardMember")
    val sumUserCardMember:SumUserCardMember?= SumUserCardMember(),
    @Expose
    @SerializedName("userCardMember")
    val userCardMember:List<UserCardMember>? = null
)

data class SumUserCardMember (
    @Expose
    @SerializedName("card")
    val card:Card?=Card(),
    @Expose
    @SerializedName("sumUserCardMember")
    val sumUserCardMember:Int?= 0,
    @Expose
    @SerializedName("sumUserCardMemberStr")
    val sumUserCardMemberStr: String? = ""
)

data class UserCardMember (
    @Expose
    @SerializedName("id")
    val id:String? = "",
    @Expose
    @SerializedName("userID")
    val userID:Long?= 0,
    @Expose
    @SerializedName("referenceID")
    val referenceID:String?= "",
    @Expose
    @SerializedName("userInfo")
    val userInfo:UserInfo? = UserInfo()
)

data class UserInfo (
    @Expose
    @SerializedName("name")
    val name:String? = "",
    @Expose
    @SerializedName("email")
    val email:String? = "",
    @Expose
    @SerializedName("phoneNumber")
    val phoneNumber:String? = "",
    @Expose
    @SerializedName("profilePicture")
    val profilePicture : String? = ""
)

data class MembershipPaging(
    @Expose
    @SerializedName("hasNext")
    val hasNext:Boolean?=false
)

