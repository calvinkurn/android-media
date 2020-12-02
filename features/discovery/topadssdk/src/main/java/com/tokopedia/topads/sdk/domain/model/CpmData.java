package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by errysuprayogi on 12/28/17.
 */

public class CpmData implements Parcelable {

    private static final String KEY_ID = "id";
    private static final String KEY_AD_REF_KEY = "ad_ref_key";
    private static final String KEY_REDIRECT = "redirect";
    private static final String KEY_AD_CLICK_URL = "ad_click_url";
    private static final String KEY_CPM = "headline";
    private static final String KEY_APPLINKS = "applinks";

    @SerializedName(KEY_ID)
    private String id;
    @SerializedName(KEY_AD_REF_KEY)
    private String adRefKey;
    @SerializedName(KEY_REDIRECT)
    private String redirect;
    @SerializedName(KEY_AD_CLICK_URL)
    private String adClickUrl;
    @SerializedName(KEY_CPM)
    private Cpm cpm;
    @SerializedName(KEY_APPLINKS)
    private String applinks;

    public CpmData() {
    }

    public CpmData(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ID)){
            setId(object.getString(KEY_ID));
        }
        if(!object.isNull(KEY_AD_REF_KEY)){
            setAdRefKey(object.getString(KEY_AD_REF_KEY));
        }
        if(!object.isNull(KEY_REDIRECT)){
            setRedirect(object.getString(KEY_REDIRECT));
        }
        if(!object.isNull(KEY_AD_CLICK_URL)){
            setAdClickUrl(object.getString(KEY_AD_CLICK_URL));
        }
        if(!object.isNull(KEY_CPM)){
            setCpm(new Cpm(object.getJSONObject(KEY_CPM)));
        }
        if(!object.isNull(KEY_APPLINKS)){
            setApplinks(object.getString(KEY_APPLINKS));
        }
    }

    protected CpmData(Parcel in) {
        id = in.readString();
        adRefKey = in.readString();
        redirect = in.readString();
        adClickUrl = in.readString();
        cpm = in.readParcelable(Cpm.class.getClassLoader());
        applinks = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(adRefKey);
        dest.writeString(redirect);
        dest.writeString(adClickUrl);
        dest.writeParcelable(cpm, flags);
        dest.writeString(applinks);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CpmData> CREATOR = new Creator<CpmData>() {
        @Override
        public CpmData createFromParcel(Parcel in) {
            return new CpmData(in);
        }

        @Override
        public CpmData[] newArray(int size) {
            return new CpmData[size];
        }
    };

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdRefKey() {
        return adRefKey;
    }

    public void setAdRefKey(String adRefKey) {
        this.adRefKey = adRefKey;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getAdClickUrl() {
        return adClickUrl;
    }

    public void setAdClickUrl(String adClickUrl) {
        this.adClickUrl = adClickUrl;
    }

    public Cpm getCpm() {
        return cpm;
    }

    public void setCpm(Cpm cpm) {
        this.cpm = cpm;
    }

}
