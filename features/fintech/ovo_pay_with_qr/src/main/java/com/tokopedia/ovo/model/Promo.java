package com.tokopedia.ovo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Promo {

@SerializedName("banner")
@Expose
private String banner;
@SerializedName("name")
@Expose
private String name;
@SerializedName("start_date")
@Expose
private String startDate;
@SerializedName("end_date")
@Expose
private String endDate;
@SerializedName("descriptions")
@Expose
private List<Object> descriptions = null;

public String getBanner() {
return banner;
}

public void setBanner(String banner) {
this.banner = banner;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getStartDate() {
return startDate;
}

public void setStartDate(String startDate) {
this.startDate = startDate;
}

public String getEndDate() {
return endDate;
}

public void setEndDate(String endDate) {
this.endDate = endDate;
}

public List<Object> getDescriptions() {
return descriptions;
}

public void setDescriptions(List<Object> descriptions) {
this.descriptions = descriptions;
}

}