package com.tokopedia.digital_deals.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class CatalogViewModel implements Parcelable
{

private Integer digitalCategoryId;
private Integer digitalProductId;
private String digitalProductCode;
public final static Parcelable.Creator<CatalogViewModel> CREATOR = new Creator<CatalogViewModel>() {


@SuppressWarnings({
"unchecked"
})
public CatalogViewModel createFromParcel(Parcel in) {
return new CatalogViewModel(in);
}

public CatalogViewModel[] newArray(int size) {
return (new CatalogViewModel[size]);
}

}
;

protected CatalogViewModel(Parcel in) {
this.digitalCategoryId = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.digitalProductId = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.digitalProductCode = ((String) in.readValue((String.class.getClassLoader())));
}

public CatalogViewModel() {
}

public Integer getDigitalCategoryId() {
return digitalCategoryId;
}

public void setDigitalCategoryId(Integer digitalCategoryId) {
this.digitalCategoryId = digitalCategoryId;
}

public Integer getDigitalProductId() {
return digitalProductId;
}

public void setDigitalProductId(Integer digitalProductId) {
this.digitalProductId = digitalProductId;
}

public String getDigitalProductCode() {
return digitalProductCode;
}

public void setDigitalProductCode(String digitalProductCode) {
this.digitalProductCode = digitalProductCode;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(digitalCategoryId);
dest.writeValue(digitalProductId);
dest.writeValue(digitalProductCode);
}

public int describeContents() {
return 0;
}

}