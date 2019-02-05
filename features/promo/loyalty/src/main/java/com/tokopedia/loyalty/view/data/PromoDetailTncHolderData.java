package com.tokopedia.loyalty.view.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author Aghny A. Putra on 26/03/18
 */

public class PromoDetailTncHolderData implements Parcelable {

    private List<String> termAndConditions;

    public List<String> getTermAndConditions() {
        return termAndConditions;
    }

    public void setTermAndConditions(List<String> termAndConditions) {
        this.termAndConditions = termAndConditions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.termAndConditions);
    }

    public PromoDetailTncHolderData() {
    }

    protected PromoDetailTncHolderData(Parcel in) {
        this.termAndConditions = in.createStringArrayList();
    }

    public static final Creator<PromoDetailTncHolderData> CREATOR = new Creator<PromoDetailTncHolderData>() {
        @Override
        public PromoDetailTncHolderData createFromParcel(Parcel source) {
            return new PromoDetailTncHolderData(source);
        }

        @Override
        public PromoDetailTncHolderData[] newArray(int size) {
            return new PromoDetailTncHolderData[size];
        }
    };
}