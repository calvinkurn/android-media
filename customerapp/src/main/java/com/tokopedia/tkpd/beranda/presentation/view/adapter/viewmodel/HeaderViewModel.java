package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.digital.tokocash.model.CashBackData;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeTypeFactory;

/**
 * @author anggaprasetiyo on 11/12/17.
 */

public class HeaderViewModel implements Parcelable, Visitable<HomeTypeFactory> {
    public static final int TYPE_TOKOCASH_ONLY = 1;
    public static final int TYPE_TOKOPINT_ONLY = 3;
    public static final int TYPE_EMPTY = 4;
    public static final int TYPE_TOKOCASH_WITH_TOKOPOINT = 2;

    private HomeHeaderWalletAction homeHeaderWalletActionData;
    private TokoPointDrawerData tokoPointDrawerData;
    private CashBackData cashBackData;
    private int type;
    private boolean pendingTokocashChecked;

    public void setPendingTokocashChecked(boolean pendingTokocashChecked) {
        this.pendingTokocashChecked = pendingTokocashChecked;
    }

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
        if (homeHeaderWalletActionData == null && tokoPointDrawerData != null
                && tokoPointDrawerData.getOffFlag() == 0) {
            return TYPE_TOKOPINT_ONLY;
        } else if (homeHeaderWalletActionData != null && tokoPointDrawerData == null) {
            return TYPE_TOKOCASH_ONLY;
        } else if (homeHeaderWalletActionData != null && tokoPointDrawerData != null
                && tokoPointDrawerData.getOffFlag() == 1) {
            return TYPE_TOKOCASH_ONLY;
        } else if (homeHeaderWalletActionData != null && tokoPointDrawerData != null
                && tokoPointDrawerData.getOffFlag() == 0) {
            return TYPE_TOKOCASH_WITH_TOKOPOINT;
        } else {
            return TYPE_EMPTY;
        }
    }

    public CashBackData getCashBackData() {
        return cashBackData;
    }

    public void setCashBackData(CashBackData cashBackData) {
        this.cashBackData = cashBackData;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public HeaderViewModel() {
    }

    public boolean isPendingTokocashChecked() {
        return pendingTokocashChecked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.homeHeaderWalletActionData, flags);
        dest.writeParcelable(this.tokoPointDrawerData, flags);
        dest.writeParcelable(this.cashBackData, flags);
        dest.writeInt(this.type);
        dest.writeByte(this.pendingTokocashChecked ? (byte) 1 : (byte) 0);
    }

    protected HeaderViewModel(Parcel in) {
        this.homeHeaderWalletActionData = in.readParcelable(HomeHeaderWalletAction.class.getClassLoader());
        this.tokoPointDrawerData = in.readParcelable(TokoPointDrawerData.class.getClassLoader());
        this.cashBackData = in.readParcelable(CashBackData.class.getClassLoader());
        this.type = in.readInt();
        this.pendingTokocashChecked = in.readByte() != 0;
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
