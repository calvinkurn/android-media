package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeTypeFactory;

/**
 * @author anggaprasetiyo on 11/12/17.
 */

public class HeaderViewModel implements Parcelable, Visitable<HomeTypeFactory> {
    public static final int TYPE_TOKOCASH_ONLY = 1;
    public static final int TYPE_TOKOCASH_WITH_TOKOPOINT = 2;

    private HomeHeaderWalletAction homeHeaderWalletActionData;
    private TokoPointDrawerData tokoPointDrawerData;
    private int type;

    public HomeHeaderWalletAction getHomeHeaderWalletActionData() {
        return homeHeaderWalletActionData;
    }

    public void setHomeHeaderWalletActionData(HomeHeaderWalletAction homeHeaderWalletActionData) {
        this.homeHeaderWalletActionData = homeHeaderWalletActionData;
    }

    public TokoPointDrawerData getTokoPointDrawerData() {
        return tokoPointDrawerData;
    }

    public void setTokoPointDrawerData(TokoPointDrawerData tokoPointDrawerData) {
        this.tokoPointDrawerData = tokoPointDrawerData;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.homeHeaderWalletActionData, flags);
        dest.writeParcelable(this.tokoPointDrawerData, flags);
        dest.writeInt(this.type);
    }

    public HeaderViewModel() {
    }

    protected HeaderViewModel(Parcel in) {
        this.homeHeaderWalletActionData = in.readParcelable(HomeHeaderWalletAction.class.getClassLoader());
        this.tokoPointDrawerData = in.readParcelable(TokoPointDrawerData.class.getClassLoader());
        this.type = in.readInt();
    }

    public static final Creator<HeaderViewModel> CREATOR = new Creator<HeaderViewModel>() {
        @Override
        public HeaderViewModel createFromParcel(Parcel source) {
            return new HeaderViewModel(source);
        }

        @Override
        public HeaderViewModel[] newArray(int size) {
            return new HeaderViewModel[size];
        }
    };
}
