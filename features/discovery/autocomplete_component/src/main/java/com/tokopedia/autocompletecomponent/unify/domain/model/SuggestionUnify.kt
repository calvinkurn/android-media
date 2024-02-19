package com.tokopedia.autocompletecomponent.unify.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuggestionUnify(
    @SerializedName("applink")
    @Expose
    val applink: String = "",

    @SerializedName("cta")
    @Expose
    val cta: SuggestionUnifyCta = SuggestionUnifyCta(),

    @SerializedName("feature_id")
    @Expose
    val featureId: String = "",

    @SerializedName("feature_type")
    @Expose
    val featureType: String = "",

    @SerializedName("flags")
    @Expose
    val flags: List<SuggestionUnifyFlag> = listOf(),

    @SerializedName("image")
    @Expose
    val image: SuggestionUnifyImage = SuggestionUnifyImage(),

    @SerializedName("is_ads")
    @Expose
    val isAds: Boolean = false,

    @SerializedName("label")
    @Expose
    val label: SuggestionUnifyLabel = SuggestionUnifyLabel(),

    @SerializedName("product")
    @Expose
    val product: SuggestionUnifyProduct = SuggestionUnifyProduct(),

    @SerializedName("subtitle")
    @Expose
    val subtitle: SuggestionUnifyTitle = SuggestionUnifyTitle(),

    @SerializedName("suggestion_id")
    @Expose
    val suggestionId: String = "",

    @SerializedName("template")
    @Expose
    val template: String = "",

    @SerializedName("title")
    @Expose
    val title: SuggestionUnifyTitle = SuggestionUnifyTitle(),

    @SerializedName("tracking")
    @Expose
    val tracking: SuggestionUnifyTracking = SuggestionUnifyTracking(),

    @SerializedName("url")
    @Expose
    val url: String = ""
)
