package com.tokopedia.feedcomponent.data.pojo.feed

import com.google.gson.annotations.SerializedName

data class Content (

    @SerializedName("cardpost")
    var cardpost: Cardpost = Cardpost(),
    @SerializedName("cardbanner")
    var cardbanner: Cardbanner = Cardbanner(),
    @SerializedName("cardrecom")
    var cardRecommendation: CardRecommendation = CardRecommendation()

)