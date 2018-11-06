package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

/**
 * @author okasurya on 7/17/18.
 */
public class TokopediaPayBSModel implements Parcelable {
    private String title;
    private String body;
    private String buttonText;
    private String buttonRedirectionUrl;

    protected TokopediaPayBSModel(Parcel in) {
        title = in.readString();
        body = in.readString();
        buttonText = in.readString();
        buttonRedirectionUrl = in.readString();
    }

    public TokopediaPayBSModel() {
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getButtonRedirectionUrl() {
        return buttonRedirectionUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public void setButtonRedirectionUrl(String buttonRedirectionUrl) {
        this.buttonRedirectionUrl = buttonRedirectionUrl;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(buttonText);
        dest.writeString(buttonRedirectionUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TokopediaPayBSModel> CREATOR = new Creator<TokopediaPayBSModel>() {
        @Override
        public TokopediaPayBSModel createFromParcel(Parcel in) {
            return new TokopediaPayBSModel(in);
        }

        @Override
        public TokopediaPayBSModel[] newArray(int size) {
            return new TokopediaPayBSModel[size];
        }
    };

    public boolean isInvalid() {
        return title == null
                || title.trim().isEmpty()
                || body == null
                || body.trim().isEmpty();
    }
}
