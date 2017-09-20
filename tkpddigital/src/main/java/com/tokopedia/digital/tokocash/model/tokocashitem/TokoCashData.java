
package com.tokopedia.digital.tokocash.model.tokocashitem;

import android.os.Parcel;

public class TokoCashData extends com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public TokoCashData() {
    }

    protected TokoCashData(Parcel in) {
        super(in);
    }

    public static final Creator<TokoCashData> CREATOR = new Creator<TokoCashData>() {
        @Override
        public TokoCashData createFromParcel(Parcel source) {
            return new TokoCashData(source);
        }

        @Override
        public TokoCashData[] newArray(int size) {
            return new TokoCashData[size];
        }
    };
}
