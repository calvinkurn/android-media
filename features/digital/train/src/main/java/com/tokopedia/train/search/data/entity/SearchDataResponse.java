package com.tokopedia.train.search.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 07/06/18.
 */
public class SearchDataResponse {

    @SerializedName("kaiSearchSchedule")
    @Expose
    private SearchDataEntity searchSchedule;

    public SearchDataEntity getSearchSchedule() {
        return searchSchedule;
    }
}
