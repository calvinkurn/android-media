
package com.tokopedia.core.network.entity.replacement.opportunitycategorydata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FilterData {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("options")
    @Expose
    private List<OptionItem> optionItemList;
    @SerializedName("search")
    @Expose
    private SearchData searchData;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<OptionItem> getOptionItemList() {
        return optionItemList;
    }

    public void setOptionItemList(List<OptionItem> optionItemList) {
        this.optionItemList = optionItemList;
    }

    public SearchData getSearchData() {
        return searchData;
    }

    public void setSearchData(SearchData searchData) {
        this.searchData = searchData;
    }
}
