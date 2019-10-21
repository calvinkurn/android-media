package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

/**
 * Author devarafikry on 24/07/19
 */
public class AccountRecommendationTitleViewModel implements ParcelableViewModel<AccountTypeFactory> {

    private String title;

    public AccountRecommendationTitleViewModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
    }

    protected AccountRecommendationTitleViewModel(Parcel in) {
        this.title = in.readString();
    }

    public static final Creator<AccountRecommendationTitleViewModel> CREATOR = new Creator<AccountRecommendationTitleViewModel>() {
        @Override
        public AccountRecommendationTitleViewModel createFromParcel(Parcel source) {
            return new AccountRecommendationTitleViewModel(source);
        }

        @Override
        public AccountRecommendationTitleViewModel[] newArray(int size) {
            return new AccountRecommendationTitleViewModel[size];
        }
    };
}
