package com.tokopedia.topads.common.domain.model.createheadline


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class TopAdsManageHeadlineInput2(
    @SerializedName("operation")
        var operation: Operation = Operation(),
    @SerializedName("source")
        var source: String = ""
) {
    data class Operation(
            @SerializedName("action")
            var action: String = "",
            @SerializedName("group")
            var group: Group = Group()
    ) {
        data class Group(
            @SerializedName("id")
                var id: String = "",
            @SerializedName("keywordOperations")
                var keywordOperations: List<KeywordOperation> = ArrayList(),
            @SerializedName("name")
                var name: String = "",
            @SerializedName("shopID")
                var shopID: String = "",
            @SerializedName("status")
                var status: String = ""
        ) {
            @Parcelize
            data class KeywordOperation(
                    @SerializedName("action")
                    var action: String = "",
                    @SerializedName("keyword")
                    var keyword: Keyword = Keyword()
            ) : Parcelable {
                @Parcelize
                data class Keyword(
                        @SerializedName("priceBid")
                        var priceBid: Long = 0,
                        @SerializedName("status")
                        var status: String = "",
                        @SerializedName("tag")
                        var tag: String = "",
                        @SerializedName("type")
                        var type: String = ""
                ) : Parcelable
            }
        }
    }
}
