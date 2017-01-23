//package com.tokopedia.core.payment.model;
//
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author by Angga.Prasetiyo on 20/05/2016.
// */
//public class ParamParcel implements Parcelable {
//
//    private List<String> keys = new ArrayList<>();
//    private List<String> values = new ArrayList<>();
//
//    public ParamParcel() {
//    }
//
//    public void put(String key, String value) {
//        this.keys.add(key);
//        this.values.add(value);
//    }
//
//    public Map<String, String> getMap() {
//        Map<String, String> map = new HashMap<>();
//        for (int i = 0; i < keys.size(); i++) {
//            map.put(keys.get(i), values.get(i));
//        }
//        return map;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeStringList(this.keys);
//        dest.writeStringList(this.values);
//    }
//
//    protected ParamParcel(Parcel in) {
//        this.keys = in.createStringArrayList();
//        this.values = in.createStringArrayList();
//    }
//
//    public static final Creator<ParamParcel> CREATOR = new Creator<ParamParcel>() {
//        @Override
//        public ParamParcel createFromParcel(Parcel source) {
//            return new ParamParcel(source);
//        }
//
//        @Override
//        public ParamParcel[] newArray(int size) {
//            return new ParamParcel[size];
//        }
//    };
//}
