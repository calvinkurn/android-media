package com.tokopedia.topads.dashboard.data.model.insightkey


import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("count")
        val count: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("negative")
        val negative: List<Negative> = listOf(),
        @SerializedName("bid")
        val bid: List<Bid> = listOf(),
        @SerializedName("keyword")
        val keyword: List<KeywordData> = listOf()
)
data class KeywordData(
        @SerializedName("data")
        val `data`: List<X1> = listOf(),
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("mutation_data")
        val mutationData: MutationData = MutationData()
)

data class Negative(
        @SerializedName("data")
        val `data`: List<X1> = listOf(),
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("mutation_data")
        val mutationData: MutationData = MutationData()
)

data class Bid(
        @SerializedName("data")
        val `data`: List<X1> = listOf(),
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("mutation_data")
        val mutationData: MutationData = MutationData()
)