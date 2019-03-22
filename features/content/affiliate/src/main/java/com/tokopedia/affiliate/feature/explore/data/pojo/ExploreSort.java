package com.tokopedia.affiliate.feature.explore.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by yfsx on 15/01/19.
 */
public class ExploreSort {
    @SerializedName("sort")
    private List<SortData> sorts;

    public List<SortData> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortData> sorts) {
        this.sorts = sorts;
    }

}
