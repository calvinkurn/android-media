package com.tokopedia.events.domain.model.scanticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Schedule {

@SerializedName("show_data")
@Expose
private String showData;
@SerializedName("category_label")
@Expose
private String categoryLabel;

public String getShowData() {
return showData;
}

public void setShowData(String showData) {
this.showData = showData;
}

public String getCategoryLabel() {
return categoryLabel;
}

public void setCategoryLabel(String categoryLabel) {
this.categoryLabel = categoryLabel;
}

}