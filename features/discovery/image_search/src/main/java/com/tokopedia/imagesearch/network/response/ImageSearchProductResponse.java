package com.tokopedia.imagesearch.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sachinbansal on 5/29/18.
 */

public class ImageSearchProductResponse {

    @SerializedName("image_search")
    @Expose
    private SearchProductResponse searchProductResponse;

    public SearchProductResponse getSearchProductResponse() {
        return searchProductResponse;
    }

    public void setSearchProductResponse(SearchProductResponse searchProductResponse) {
        this.searchProductResponse = searchProductResponse;
    }
}
