package com.tokopedia.digital.widget.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author rizkyfadillah on 10/2/2017.
 */

public class ResponseNumberList {

    @SerializedName("data")
    @Expose
    private List<ResponseFavoriteNumber> favoriteNumbers;

    public List<ResponseFavoriteNumber> getFavoriteNumbers() {
        return favoriteNumbers;
    }
}
