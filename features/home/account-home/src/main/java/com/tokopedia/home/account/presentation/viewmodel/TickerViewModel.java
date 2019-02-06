package com.tokopedia.home.account.presentation.viewmodel;

import android.os.Parcel;

import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory;
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 16/11/18.
 */
public class TickerViewModel implements ParcelableViewModel<AccountTypeFactory> {
    private ArrayList<String> listMessage;

    public TickerViewModel(ArrayList<String> listMessage) {
        this.listMessage = listMessage;
    }

    protected TickerViewModel(Parcel in) {
        listMessage = in.createStringArrayList();
    }

    public static final Creator<TickerViewModel> CREATOR = new Creator<TickerViewModel>() {
        @Override
        public TickerViewModel createFromParcel(Parcel in) {
            return new TickerViewModel(in);
        }

        @Override
        public TickerViewModel[] newArray(int size) {
            return new TickerViewModel[size];
        }
    };

    public ArrayList<String> getListMessage() {
        return listMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(listMessage);
    }

    @Override
    public int type(AccountTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
