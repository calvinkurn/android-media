package com.tokopedia.discovery.activity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kulomady on 12/21/16.
 */

public class FilterMapAtribut implements Parcelable {
    private HashMap<Integer, FilterMapValue> filtersMap;

    public FilterMapAtribut() {
        this.filtersMap = new HashMap<>();
    }

    public HashMap<Integer, FilterMapValue> getFiltersMap() {
        return filtersMap;
    }

    protected FilterMapAtribut(Parcel in) {
        final int size = in.readInt();
        this.filtersMap = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            final int key = in.readInt();
            final FilterMapValue value = in.readParcelable(FilterMapValue.class.getClassLoader());
            filtersMap.put(key, value);
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeValue(filtersMap);
        dest.writeInt(filtersMap.size());
        for (Map.Entry<Integer, FilterMapValue> entry : filtersMap.entrySet()) {
            dest.writeInt(entry.getKey());
            final FilterMapValue value = entry.getValue();
            dest.writeParcelable(value, flags);
        }
    }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FilterMapAtribut> CREATOR = new Parcelable.Creator<FilterMapAtribut>() {
        @Override
        public FilterMapAtribut createFromParcel(Parcel in) {
            return new FilterMapAtribut(in);
        }

        @Override
        public FilterMapAtribut[] newArray(int size) {
            return new FilterMapAtribut[size];
        }
    };


    public static class FilterMapValue implements Parcelable {

        private HashMap<String, String> value;

        public FilterMapValue() {
            value = new HashMap<>();
        }

        public HashMap<String, String> getValue() {
            return value;
        }

        public void setValue(HashMap<String, String> value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
//            dest.writeSerializable(this.value);
            dest.writeInt(value.size());
            for (Map.Entry<String, String> entry : value.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeString(entry.getValue());
            }
        }

        protected FilterMapValue(Parcel in) {
//            this.value = (HashMap<String, String>) in.readSerializable();
            int size = in.readInt();
            this.value = new HashMap<>(size);
            for (int i = 0; i < size; i++) {
                String key = in.readString();
                String value = in.readString();
                this.value.put(key, value);
            }

        }

        public static final Parcelable.Creator<FilterMapValue> CREATOR = new Parcelable.Creator<FilterMapValue>() {
            @Override
            public FilterMapValue createFromParcel(Parcel source) {
                return new FilterMapValue(source);
            }

            @Override
            public FilterMapValue[] newArray(int size) {
                return new FilterMapValue[size];
            }
        };
    }


}
