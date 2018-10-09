
package com.tokopedia.topads.dashboard.data.model.request;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSuggestionBody {

    @SerializedName("suggestion_type")
    @Expose
    private String suggestionType;
    @SerializedName("data_type")
    @Expose
    private String dataType;
    @SerializedName("shop_id")
    @Expose
    private long shopId;
    @SerializedName("ids")
    @Expose
    private List<Long> ids = new ArrayList<>();
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("rounding")
    @Expose
    private boolean rounding;

    public String getSuggestionType() {
        return suggestionType;
    }

    public void setSuggestionType(String suggestionType) {
        this.suggestionType = suggestionType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public void addId(Long id){
        ids.add(id);
    }

    public void addId(String id){
        addId(Long.valueOf(id));
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isRounding() {
        return rounding;
    }

    public void setRounding(boolean rounding) {
        this.rounding = rounding;
    }

}
