
package com.tokopedia.topads.keyword.data.model.cloud.request.keywordadd;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddKeywordRequest {

    @SerializedName("data")
    @Expose
    private List<KeywordAddRequestDatum> data = null;

    public List<KeywordAddRequestDatum> getData() {
        return data;
    }

    public void setData(List<KeywordAddRequestDatum> data) {
        this.data = data;
    }

}
