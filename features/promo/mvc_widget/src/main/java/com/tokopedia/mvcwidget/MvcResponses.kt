package com.tokopedia.mvcwidget

import androidx.annotation.StringDef
import com.google.gson.annotations.SerializedName
import com.tokopedia.mvcwidget.FollowWidgetType.Companion.DEFAULT
import com.tokopedia.mvcwidget.FollowWidgetType.Companion.FIRST_FOLLOW
import com.tokopedia.mvcwidget.FollowWidgetType.Companion.MEMBERSHIP_CLOSE
import com.tokopedia.mvcwidget.FollowWidgetType.Companion.MEMBERSHIP_OPEN

data class TokopointsCatalogMVCListResponse(
        @SerializedName("tokopointsCatalogMVCList") val data: TokopointsCatalogMVCList? = null
)

data class TokopointsCatalogMVCList(
        @SerializedName("followWidget") val followWidget: FollowWidget? = null,
        @SerializedName("resultStatus") val resultStatus: ResultStatus?,
        @SerializedName("shopName") val shopName: String?,
        @SerializedName("toasterSuccessMessage") val toasterSuccessMessage: String?,
        @SerializedName("catalogList") val catalogList: List<CatalogList?>?,
)

data class ResultStatus(
        @SerializedName("code") val code: String?,
        @SerializedName("message") val message: List<String?>?,
        @SerializedName("status") val status: String?,
        @SerializedName("reason") val reason: String?
)

data class CatalogList(
        @SerializedName("id") val code: String?,
        @SerializedName("slug") val slug: String?,
        @SerializedName("baseCode") val baseCode: String?,
        @SerializedName("promoID") val promoID: String?,
        @SerializedName("title") val title: String?,
        @SerializedName("catalogType") val catalogType: Int,
        @SerializedName("promoType") val promoType: String?,
        @SerializedName("maximumBenefitAmount") val maximumBenefitAmount: String?,
        @SerializedName("minimumUsageAmount") val minimumUsageAmount: String?,
        @SerializedName("minimumUsageLabel") val minimumUsageLabel: String?,
        @SerializedName("expiredDate") val expiredDate: String?,
        @SerializedName("expiredLabel") val expiredLabel: String?,
        @SerializedName("quotaLeft") val quotaLeft: String?,
        @SerializedName("quotaLeftLabel") val quotaLeftLabel: String?,
        @SerializedName("tagImageURLs") val tagImageURLs: List<String?>?,
)

data class FollowWidget(
        @SerializedName("isShown") val isShown: Boolean?,
        @FollowWidgetType @SerializedName("type") val type: String?,
        @SerializedName("content") val content: String?,
        @SerializedName("contentDetails") val contentDetails: String?,
        @SerializedName("membershipMinimumTransaction") val membershipMinimumTransaction: Int?,
        @SerializedName("membershipMinimumTransactionLabel") val membershipMinimumTransactionLabel: String?,
        @SerializedName("iconURL") val iconURL: String?,
        @SerializedName("membershipCardID") val membershipCardID: String?,
        @SerializedName("membershipHowTo") val membershipHowTo: List<MembershipHowTo?>?,
        @SerializedName("isCollapsed") var isCollapsed : Boolean = false
        ) : MvcListItem

data class MembershipHowTo(
        @SerializedName("imageURL") val imageURL: String?,
        @SerializedName("description") val description: String?
)

data class TokopointsCatalogMVCSummaryResponse(
        @SerializedName("tokopointsCatalogMVCSummary") val data: TokopointsCatalogMVCSummary? = null
)

data class TokopointsCatalogMVCSummary(
        @SerializedName("resultStatus") val resultStatus: ResultStatus?,
        @SerializedName("isShown") val isShown: Boolean?,
        @SerializedName("counterTotal") val counterTotal: Int?,
        @SerializedName("animatedInfos") val animatedInfos: List<AnimatedInfos>?,
        )

data class MembershipRegisterResponse(
        @SerializedName("membershipRegister") val data: MembershipRegister? = null
)

data class MembershipRegister(
        @SerializedName("resultStatus") val resultStatus: ResultStatus?,
        @SerializedName("infoMessage") val infoMessage: InfoMessage?,
)

data class InfoMessage(
        @SerializedName("imageURL") val imageURL: String?,
        @SerializedName("title") val title: String?,
        @SerializedName("subtitle") val subtitle: String?,
        @SerializedName("cta") val cta: Cta?,
)

data class Cta(
        @SerializedName("text") val text: String?,
        @SerializedName("url") val url: String?,
        @SerializedName("appLink") val appLink: String?
)

data class FollowShopResponse(
        @SerializedName("followShop") val followShop: FollowShop? = null
)

data class FollowShop(
        @SerializedName("success") val success: Boolean?
)

data class AnimatedInfos(
        @SerializedName("title") val title: String?,
        @SerializedName("subTitle") val subTitle: String?,
        @SerializedName("iconURL") val iconURL: String?
)

@Retention(AnnotationRetention.SOURCE)
@StringDef(FIRST_FOLLOW, MEMBERSHIP_OPEN, MEMBERSHIP_CLOSE, DEFAULT)
annotation class FollowWidgetType {

    companion object {
        const val FIRST_FOLLOW = "first_follow"
        const val MEMBERSHIP_OPEN = "membership_open"
        const val MEMBERSHIP_CLOSE = "membership_close"
        const val DEFAULT = "default"
    }
}