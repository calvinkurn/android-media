package com.tokopedia.topads.dashboard.data.model.insightkey


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class KeywordInsightDataMain(
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
) : Serializable

data class KeywordData(
        @SerializedName("data")
        val `data`: HashMap<String, ValueKey>? = HashMap(),
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("mutation_data")
        val mutationData: MutationData = MutationData()
) : Serializable

data class Negative(
        @SerializedName("data")
        val `data`: HashMap<String, ValueKey>? = HashMap(),
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("mutation_data")
        val mutationData: MutationData = MutationData()
) : Serializable

data class Bid(
        @SerializedName("data")
        val `data`: HashMap<String, ValueKey>? = HashMap(),
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("mutation_data")
        val mutationData: MutationData = MutationData()
) : Serializable