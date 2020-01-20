package com.tokopedia.imagesearch.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.filter.common.data.DataValue;
import com.tokopedia.filter.common.data.DynamicFilterModel;

/**
 * Created by sachinbansal on 5/29/18.
 */

public class ImageSearchProductResponse {

    @SerializedName("image_search")
    @Expose
    private SearchProductResponse searchProductResponse;

    @SerializedName("filter_sort_product")
    @Expose
    private DynamicFilterModel dynamicFilterModel;

    public SearchProductResponse getSearchProductResponse() {
        return searchProductResponse;
    }

    public void setSearchProductResponse(SearchProductResponse searchProductResponse) {
        this.searchProductResponse = searchProductResponse;
    }

    public DynamicFilterModel getDynamicFilterModel() {
        return dynamicFilterModel;
    }
}
