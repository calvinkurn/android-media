
package com.tokopedia.core.database.model.category;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientNumber implements Parcelable {

    @SerializedName("help")
    @Expose
    private String help;
    @SerializedName("is_shown")
    @Expose
    private boolean isShown;
    @SerializedName("placeholder")
    @Expose
    private String placeholder;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("operator_style")
    @Expose
    private String operatorStyle;

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOperatorStyle() {
        return operatorStyle;
    }

    public void setOperatorStyle(String operatorStyle) {
        this.operatorStyle = operatorStyle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.help);
        dest.writeByte(this.isShown ? (byte) 1 : (byte) 0);
        dest.writeString(this.placeholder);
        dest.writeString(this.text);
        dest.writeString(this.operatorStyle);
    }

    public ClientNumber() {
    }

    protected ClientNumber(Parcel in) {
        this.help = in.readString();
        this.isShown = in.readByte() != 0;
        this.placeholder = in.readString();
        this.text = in.readString();
        this.operatorStyle = in.readString();
    }

    public static final Creator<ClientNumber> CREATOR = new Creator<ClientNumber>() {
        @Override
        public ClientNumber createFromParcel(Parcel source) {
            return new ClientNumber(source);
        }

        @Override
        public ClientNumber[] newArray(int size) {
            return new ClientNumber[size];
        }
    };
}
