package com.tokopedia.digital.widget.view.model.category;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/14/17.
 */

public class ClientNumber implements Parcelable {

    private String help;
    private boolean isShown;
    private String placeholder;
    private String text;
    private String operatorStyle;

    public ClientNumber() {
    }

    protected ClientNumber(Parcel in) {
        help = in.readString();
        isShown = in.readByte() != 0;
        placeholder = in.readString();
        text = in.readString();
        operatorStyle = in.readString();
    }

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
        dest.writeString(help);
        dest.writeByte((byte) (isShown ? 1 : 0));
        dest.writeString(placeholder);
        dest.writeString(text);
        dest.writeString(operatorStyle);
    }
}
