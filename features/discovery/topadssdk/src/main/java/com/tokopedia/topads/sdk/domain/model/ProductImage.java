package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author by errysuprayogi on 4/3/17.
 */
public class ProductImage implements Parcelable {

    private static final String KEY_M_URL = "m_url";
    private static final String KEY_S_URL = "s_url";
    private static final String KEY_XS_URL = "xs_url";
    private static final String KEY_M_ECS = "m_ecs";
    private static final String KEY_S_ECS = "s_ecs";
    private static final String KEY_XS_ECS = "xs_ecs";

    @SerializedName(KEY_M_URL)
    private String m_url = "";
    @SerializedName(KEY_S_URL)
    private String s_url = "";
    @SerializedName(KEY_XS_URL)
    private String xs_url = "";
    @SerializedName(KEY_M_ECS)
    private String m_ecs = "";
    @SerializedName(KEY_S_ECS)
    private String s_ecs = "";
    @SerializedName(KEY_XS_ECS)
    private String xs_ecs = "";
    private boolean impressed = false;

    public ProductImage() {
    }

    public ProductImage(JSONObject object) throws JSONException {
        if (!object.isNull(KEY_M_URL)) {
            setM_url(object.getString(KEY_M_URL));
        } else if (!object.isNull(KEY_S_URL)) {
            setM_url(object.getString(KEY_S_URL));
        }
        if (!object.isNull(KEY_S_URL)) {
            setS_url(object.getString(KEY_S_URL));
        }
        if (!object.isNull(KEY_XS_URL)) {
            setXs_url(object.getString(KEY_XS_URL));
        }
        if (!object.isNull(KEY_M_ECS)) {
            setM_ecs(object.getString(KEY_M_ECS));
        } else if (!object.isNull(KEY_S_ECS)) {
            setM_ecs(object.getString(KEY_S_ECS));
        }
        if (!object.isNull(KEY_S_ECS)) {
            setS_ecs(object.getString(KEY_S_ECS));
        }
        if (!object.isNull(KEY_XS_ECS)) {
            setXs_ecs(object.getString(KEY_XS_ECS));
        }
    }

    protected ProductImage(Parcel in) {
        m_url = in.readString();
        s_url = in.readString();
        xs_url = in.readString();
        m_ecs = in.readString();
        s_ecs = in.readString();
        xs_ecs = in.readString();
        impressed = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_url);
        dest.writeString(s_url);
        dest.writeString(xs_url);
        dest.writeString(m_ecs);
        dest.writeString(s_ecs);
        dest.writeString(xs_ecs);
        dest.writeByte((byte) (impressed ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductImage> CREATOR = new Creator<ProductImage>() {
        @Override
        public ProductImage createFromParcel(Parcel in) {
            return new ProductImage(in);
        }

        @Override
        public ProductImage[] newArray(int size) {
            return new ProductImage[size];
        }
    };

    public boolean isImpressed() {
        return impressed;
    }

    public void setImpressed(boolean impressed) {
        this.impressed = impressed;
    }

    public String getM_url() {
        return m_url;
    }

    public void setM_url(String m_url) {
        this.m_url = m_url;
    }

    public String getS_url() {
        return s_url;
    }

    public void setS_url(String s_url) {
        this.s_url = s_url;
    }

    public String getXs_url() {
        return xs_url;
    }

    public void setXs_url(String xs_url) {
        this.xs_url = xs_url;
    }

    public String getM_ecs() {
        return m_ecs;
    }

    public void setM_ecs(String m_ecs) {
        this.m_ecs = m_ecs;
    }

    public String getS_ecs() {
        return s_ecs;
    }

    public void setS_ecs(String s_ecs) {
        this.s_ecs = s_ecs;
    }

    public String getXs_ecs() {
        return xs_ecs;
    }

    public void setXs_ecs(String xs_ecs) {
        this.xs_ecs = xs_ecs;
    }
}
