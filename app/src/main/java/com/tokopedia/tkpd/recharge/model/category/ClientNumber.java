
package com.tokopedia.tkpd.recharge.model.category;

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
    private Boolean isShown;
    @SerializedName("placeholder")
    @Expose
    private String placeholder;
    @SerializedName("text")
    @Expose
    private String text;

    protected ClientNumber(Parcel in) {
        help = in.readString();
        byte isShownVal = in.readByte();
        isShown = isShownVal == 0x02 ? null : isShownVal != 0x00;
        placeholder = in.readString();
        text = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(help);
        if (isShown == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isShown ? 0x01 : 0x00));
        }
        dest.writeString(placeholder);
        dest.writeString(text);
    }

    @SuppressWarnings("unused")
    public static final Creator<ClientNumber> CREATOR = new Creator<ClientNumber>() {
        @Override
        public ClientNumber createFromParcel(Parcel in) {
            return new ClientNumber(in);
        }

        @Override
        public ClientNumber[] newArray(int size) {
            return new ClientNumber[size];
        }
    };

    /**
     * 
     * @return
     *     The help
     */
    public String getHelp() {
        return help;
    }

    /**
     * 
     * @param help
     *     The help
     */
    public void setHelp(String help) {
        this.help = help;
    }

    /**
     * 
     * @return
     *     The isShown
     */
    public Boolean getIsShown() {
        return isShown;
    }

    /**
     * 
     * @param isShown
     *     The is_shown
     */
    public void setIsShown(Boolean isShown) {
        this.isShown = isShown;
    }

    /**
     * 
     * @return
     *     The placeholder
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * 
     * @param placeholder
     *     The placeholder
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * 
     * @return
     *     The text
     */
    public String getText() {
        return text;
    }

    /**
     * 
     * @param text
     *     The text
     */
    public void setText(String text) {
        this.text = text;
    }

}
