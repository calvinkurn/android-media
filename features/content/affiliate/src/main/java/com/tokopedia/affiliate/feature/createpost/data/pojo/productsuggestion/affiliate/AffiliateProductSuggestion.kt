package com.tokopedia.affiliate.feature.createpost.data.pojo.productsuggestion.affiliate


import com.google.gson.annotations.SerializedName

data class AffiliateProductSuggestion(
    @SerializedName("affiliateParticularSections")
    val affiliateParticularSections: AffiliateParticularSections = AffiliateParticularSections()
)