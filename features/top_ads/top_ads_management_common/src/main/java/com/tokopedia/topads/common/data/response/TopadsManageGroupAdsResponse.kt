package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

data class TopadsManageGroupAdsResponse(
    @SerializedName("topadsManageGroupAds")
    val topAdsManageGroupAds : TopAdsManageGroupAds = TopAdsManageGroupAds()
){
    data class TopAdsManageGroupAds(
        @SerializedName("groupResponse")
        val groupResponse:GroupResponse = GroupResponse()
    )

    data class GroupResponse(
        @SerializedName("data")
        val data:TopAdsGroupsResponse = TopAdsGroupsResponse(),
        @SerializedName("errors")
        val errors:List<TopAdsDashboardError> = listOf()
    )

    data class TopAdsGroupsResponse(
        @SerializedName("id")
        val id:Int = 0,
        @SerializedName("resourceUrl")
        val resourceUrl:String = ""
    )

}

data class TopadsManageGroupAdsInput(
    @SerializedName("source")
    var source:String = "",
    @SerializedName("groupID")
    var groupId:String = "",
    @SerializedName("shopID")
    var shopId:String = "",
    @SerializedName("groupOperation")
    var groupOperation:GroupOperation = GroupOperation()
){
    data class GroupOperation(
        @SerializedName("action")
        var action:String = "",
        @SerializedName("group")
        var group:Group = Group()
    ){
        data class Group(
            @SerializedName("type")
            var type:String = "",
            @SerializedName("adOperations")
            var adOperations:List<GroupEditInput.Group.AdOperationsItem> = listOf()
        )
    }
}


