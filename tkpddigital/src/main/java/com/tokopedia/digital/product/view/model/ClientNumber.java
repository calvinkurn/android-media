package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class ClientNumber implements Parcelable {

    public static final String TYPE_INPUT_TEL = "tel";
    public static final String TYPE_INPUT_NUMERIC = "numeric";
    public static final String TYPE_INPUT_ALPHANUMERIC = "tel";
    public static final String DEFAULT_TYPE_CONTRACT = "client_number";

    private String name;
    private String type;
    private String text;
    private String help;
    private String placeholder;
    private String _default;
    private List<Validation> validation = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String get_default() {
        return _default;
    }

    public void set_default(String _default) {
        this._default = _default;
    }

    public List<Validation> getValidation() {
        return validation;
    }

    public void setValidation(List<Validation> validation) {
        this.validation = validation;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.text);
        dest.writeString(this.help);
        dest.writeString(this.placeholder);
        dest.writeString(this._default);
        dest.writeList(this.validation);
    }

    public ClientNumber() {
    }

    protected ClientNumber(Parcel in) {
        this.name = in.readString();
        this.type = in.readString();
        this.text = in.readString();
        this.help = in.readString();
        this.placeholder = in.readString();
        this._default = in.readString();
        this.validation = new ArrayList<Validation>();
        in.readList(this.validation, Validation.class.getClassLoader());
    }

    public static final Parcelable.Creator<ClientNumber> CREATOR = new Parcelable.Creator<ClientNumber>() {
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
