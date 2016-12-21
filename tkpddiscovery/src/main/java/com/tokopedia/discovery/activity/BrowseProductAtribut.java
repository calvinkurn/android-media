package com.tokopedia.discovery.activity;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.discovery.model.DataValue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kulomady on 12/21/16.
 *         this only for hotfix later will be revamp
 */

public class BrowseProductAtribut implements Parcelable {
    private HashMap<Integer, DataValue> filterAttributMap;


    public BrowseProductAtribut() {
        filterAttributMap = new HashMap<>();
    }

    public HashMap<Integer, DataValue> getFilterAttributMap() {
        return filterAttributMap;
    }


    protected BrowseProductAtribut(Parcel in) {
        final int size = in.readInt();
        this.filterAttributMap = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            final int key = in.readInt();
            final DataValue value
                    = in.readParcelable(DataValue.class.getClassLoader());
            filterAttributMap.put(key, value);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(filterAttributMap.size());
        for (Map.Entry<Integer, DataValue> entry : filterAttributMap.entrySet()) {
            dest.writeInt(entry.getKey());
            final DataValue value = entry.getValue();
            dest.writeParcelable(value, flags);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BrowseProductAtribut> CREATOR
            = new Parcelable.Creator<BrowseProductAtribut>() {

        @Override
        public BrowseProductAtribut createFromParcel(Parcel in) {
            return new BrowseProductAtribut(in);
        }

        @Override
        public BrowseProductAtribut[] newArray(int size) {
            return new BrowseProductAtribut[size];
        }
    };


}
