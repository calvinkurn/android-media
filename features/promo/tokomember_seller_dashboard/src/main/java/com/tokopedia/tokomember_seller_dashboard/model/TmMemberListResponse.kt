package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmMemberListResponse(
    @Expose
    @SerializedName("membershipGetUserCardMemberList" )
    val membershipGetUserCardMemberList : MembershipGetUserCardMemberList? = null
)

data class MembershipGetUserCardMemberList (
    @Expose
    @SerializedName("resultStatus")
    val resultStatus:ResultStatus?= null,
    @Expose
    @SerializedName("userCardMemberList")
    val userCardMemberList:UserCardMemberList?=null,
    @Expose
    @SerializedName("paging")
    val paging: MembershipPaging?=null
)

data class UserCardMemberList (
    @Expose
    @SerializedName("sumUserCardMember")
    val sumUserCardMember:SumUserCardMember?=null,
    @Expose
    @SerializedName("userCardMember")
    val userCardMember:List<UserCardMember>? = null
)

data class SumUserCardMember (
    @Expose
    @SerializedName("card")
    val card:Card?=null,
    @Expose
    @SerializedName("sumUserCardMember")
    val sumUserCardMember    : Int?    = null,
    @Expose
    @SerializedName("sumUserCardMemberStr")
    val sumUserCardMemberStr : String? = null
)

data class UserCardMember (
    @Expose
    @SerializedName("id")
    val id:String? = null,
    @Expose
    @SerializedName("userID")
    val userID:Int?= null,
    @Expose
    @SerializedName("referenceID")
    val referenceID:String?=null,
    @Expose
    @SerializedName("userInfo")
    val userInfo:UserInfo? = null
)

data class UserInfo (
    @Expose
    @SerializedName("name")
    val name:String? = null,
    @Expose
    @SerializedName("email")
    val email:String? = null,
    @Expose
    @SerializedName("phoneNumber")
    val phoneNumber:String? = null,
    @Expose
    @SerializedName("profilePicture")
    val profilePicture : String? = null
)

data class MembershipPaging(
    @Expose
    @SerializedName("hasNext")
    val hasNext:Boolean?=null
)

