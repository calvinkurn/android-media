package com.tokopedia.saldodetails.response.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GqlMerchantSaldoDetailsResponse implements Parcelable {

    @SerializedName("sp_getmerchantstatus")
    @Expose
    private Details data;
    public final static Parcelable.Creator<GqlMerchantSaldoDetailsResponse> CREATOR = new Creator<GqlMerchantSaldoDetailsResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GqlMerchantSaldoDetailsResponse createFromParcel(Parcel in) {
            return new GqlMerchantSaldoDetailsResponse(in);
        }

        public GqlMerchantSaldoDetailsResponse[] newArray(int size) {
            return (new GqlMerchantSaldoDetailsResponse[size]);
        }

    };

    protected GqlMerchantSaldoDetailsResponse(Parcel in) {
        this.data = ((Details) in.readValue((Details.class.getClassLoader())));
    }

    public GqlMerchantSaldoDetailsResponse() {
    }

    public Details getData() {
        return data;
    }

    public void setData(Details data) {
        this.data = data;
    }

    public GqlMerchantSaldoDetailsResponse withData(Details data) {
        this.data = data;
        return this;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }


    public class InfoList implements Parcelable {

        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("value")
        @Expose
        private String value;
        public final Parcelable.Creator<InfoList> CREATOR = new Creator<InfoList>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public InfoList createFromParcel(Parcel in) {
                return new InfoList(in);
            }

            public InfoList[] newArray(int size) {
                return (new InfoList[size]);
            }

        };

        protected InfoList(Parcel in) {
            this.label = ((String) in.readValue((String.class.getClassLoader())));
            this.value = ((String) in.readValue((String.class.getClassLoader())));
        }

        public InfoList() {
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public InfoList withLabel(String label) {
            this.label = label;
            return this;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public InfoList withValue(String value) {
            this.value = value;
            return this;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(label);
            dest.writeValue(value);
        }

        public int describeContents() {
            return 0;
        }

    }

    public static class AnchorList implements Parcelable {

        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("color")
        @Expose
        private String color;
        public final Parcelable.Creator<AnchorList> CREATOR = new Creator<AnchorList>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public AnchorList createFromParcel(Parcel in) {
                return new AnchorList(in);
            }

            public AnchorList[] newArray(int size) {
                return (new AnchorList[size]);
            }

        };

        protected AnchorList(Parcel in) {
            this.label = ((String) in.readValue((String.class.getClassLoader())));
            this.url = ((String) in.readValue((String.class.getClassLoader())));
            this.color = ((String) in.readValue((String.class.getClassLoader())));
        }

        public AnchorList() {
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public AnchorList withLabel(String label) {
            this.label = label;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public AnchorList withUrl(String url) {
            this.url = url;
            return this;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public AnchorList withColor(String color) {
            this.color = color;
            return this;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(label);
            dest.writeValue(url);
            dest.writeValue(color);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class Details implements Parcelable {

        @SerializedName("is_eligible")
        @Expose
        private boolean isEligible;
        @SerializedName("status")
        @Expose
        private int status;
        @SerializedName("is_enabled")
        @Expose
        private boolean isEnabled;
        @SerializedName("show_new_logo")
        @Expose
        private boolean showNewLogo;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("show_toggle")
        @Expose
        private boolean showToggle;
        @SerializedName("box_type")
        @Expose
        private String boxType;

        @SerializedName("description")
        @Expose
        private String description;

        @SerializedName("box_right_arrow")
        @Expose
        private boolean showRightArrow;

        @SerializedName("box_title")
        @Expose
        private String boxTitle;

        @SerializedName("box_desc")
        @Expose
        private String boxDesc;

        @SerializedName("box_show_popup")
        @Expose
        private boolean boxShowPopup;

        @SerializedName("popup_title")
        @Expose
        private String popupTitle;

        @SerializedName("popup_desc")
        @Expose
        private String popupDesc;

        @SerializedName("popup_button_text")
        @Expose
        private String popupButtonText;

        @SerializedName("infoList")
        @Expose
        private List<InfoList> infoList = null;
        @SerializedName("anchorList")
        @Expose
        private List<AnchorList> anchorList = null;
        public final Parcelable.Creator<Details> CREATOR = new Creator<Details>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Details createFromParcel(Parcel in) {
                return new Details(in);
            }

            public Details[] newArray(int size) {
                return (new Details[size]);
            }

        };

        protected Details(Parcel in) {
            this.isEligible = ((boolean) in.readValue((boolean.class.getClassLoader())));
            this.status = ((int) in.readValue((Integer.class.getClassLoader())));
            this.isEnabled = ((boolean) in.readValue((boolean.class.getClassLoader())));
            this.showNewLogo = ((boolean) in.readValue((boolean.class.getClassLoader())));
            this.title = ((String) in.readValue((String.class.getClassLoader())));
            this.showToggle = ((boolean) in.readValue((boolean.class.getClassLoader())));
            this.boxType = ((String) in.readValue((String.class.getClassLoader())));
            this.description = ((String) in.readValue((String.class.getClassLoader())));
            this.showRightArrow = ((boolean) in.readValue((boolean.class.getClassLoader())));
            this.boxTitle = ((String) in.readValue((String.class.getClassLoader())));
            this.boxDesc = ((String) in.readValue((String.class.getClassLoader())));
            this.boxShowPopup = ((boolean) in.readValue((boolean.class.getClassLoader())));
            this.popupTitle = ((String) in.readValue((String.class.getClassLoader())));
            this.popupDesc = ((String) in.readValue((String.class.getClassLoader())));
            this.popupButtonText = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(this.infoList, (com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse.InfoList.class.getClassLoader()));
            in.readList(this.anchorList, (com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse.AnchorList.class.getClassLoader()));
        }

        public Details() {
        }

        public boolean isIsEligible() {
            return isEligible;
        }

        public void setIsEligible(boolean isEligible) {
            this.isEligible = isEligible;
        }

        public Details withIsEligible(boolean isEligible) {
            this.isEligible = isEligible;
            return this;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Details withStatus(int status) {
            this.status = status;
            return this;
        }

        public boolean isIsEnabled() {
            return isEnabled;
        }

        public void setIsEnabled(boolean isEnabled) {
            this.isEnabled = isEnabled;
        }

        public Details withIsEnabled(boolean isEnabled) {
            this.isEnabled = isEnabled;
            return this;
        }

        public boolean isShowNewLogo() {
            return showNewLogo;
        }

        public void setShowNewLogo(boolean showNewLogo) {
            this.showNewLogo = showNewLogo;
        }

        public Details withShowNewLogo(boolean showNewLogo) {
            this.showNewLogo = showNewLogo;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Details withTitle(String title) {
            this.title = title;
            return this;
        }

        public boolean isShowToggle() {
            return showToggle;
        }

        public void setShowToggle(boolean showToggle) {
            this.showToggle = showToggle;
        }

        public Details withShowToggle(boolean showToggle) {
            this.showToggle = showToggle;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Details withDescription(String description) {
            this.description = description;
            return this;
        }

        public List<InfoList> getInfoList() {
            return infoList;
        }

        public void setInfoList(List<InfoList> infoList) {
            this.infoList = infoList;
        }

        public Details withInfoList(List<InfoList> infoList) {
            this.infoList = infoList;
            return this;
        }

        public List<AnchorList> getAnchorList() {
            return anchorList;
        }

        public void setAnchorList(List<AnchorList> anchorList) {
            this.anchorList = anchorList;
        }

        public Details withAnchorList(List<AnchorList> anchorList) {
            this.anchorList = anchorList;
            return this;
        }

        public String getBoxType() {
            return boxType;
        }

        public void setBoxType(String boxType) {
            this.boxType = boxType;
        }

        public boolean isShowRightArrow() {
            return showRightArrow;
        }

        public void setShowRightArrow(boolean showRightArrow) {
            this.showRightArrow = showRightArrow;
        }

        public String getBoxTitle() {
            return boxTitle;
        }

        public void setBoxTitle(String boxTitle) {
            this.boxTitle = boxTitle;
        }

        public String getBoxDesc() {
            return boxDesc;
        }

        public void setBoxDesc(String boxDesc) {
            this.boxDesc = boxDesc;
        }

        public boolean isBoxShowPopup() {
            return boxShowPopup;
        }

        public void setBoxShowPopup(boolean boxShowPopup) {
            this.boxShowPopup = boxShowPopup;
        }

        public String getPopupTitle() {
            return popupTitle;
        }

        public void setPopupTitle(String popupTitle) {
            this.popupTitle = popupTitle;
        }

        public String getPopupDesc() {
            return popupDesc;
        }

        public void setPopupDesc(String popupDesc) {
            this.popupDesc = popupDesc;
        }

        public String getPopupButtonText() {
            return popupButtonText;
        }

        public void setPopupButtonText(String popupButtonText) {
            this.popupButtonText = popupButtonText;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(isEligible);
            dest.writeValue(status);
            dest.writeValue(isEnabled);
            dest.writeValue(showNewLogo);
            dest.writeValue(title);
            dest.writeValue(showToggle);
            dest.writeValue(boxType);
            dest.writeValue(description);
            dest.writeValue(showRightArrow);
            dest.writeValue(boxTitle);
            dest.writeValue(boxDesc);
            dest.writeValue(boxShowPopup);
            dest.writeValue(popupTitle);
            dest.writeValue(popupDesc);
            dest.writeValue(popupButtonText);
            dest.writeList(infoList);
            dest.writeList(anchorList);
        }

        public int describeContents() {
            return 0;
        }

    }
}
