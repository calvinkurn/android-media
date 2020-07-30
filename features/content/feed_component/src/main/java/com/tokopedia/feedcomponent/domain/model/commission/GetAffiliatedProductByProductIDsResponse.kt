package com.tokopedia.feedcomponent.domain.model.commission

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-11-23
 */
data class GetAffiliatedProductByProductIDsResponse(

        @SerializedName("affiliatedProductByProductIDs")
        val affiliatedProductByProductIDs: AffiliatedProductByProductIDs
)