package com.tokopedia.core.session.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevenfredian on 6/2/16.
 */
public class LoginProviderModel implements Parcelable {
    /**
     * id : facebook
     * name : Facebook
     * url : https://accounts-alpha.tokopedia.com/fb-login
     * image : https://ecs1.tokopedia.net/img/icon/facebook_icon.png
     * color : #3A589B
     * imageResource : apk resources, 0 if bean is from server
     */

    @SerializedName("providers")
    @Expose
    private List<ProvidersBean> providers;

    @SerializedName("url_background_seller")
    @Expose
    private String urlBackground;

    public List<ProvidersBean> getProviders() {
        return providers;
    }

    public void setProviders(List<ProvidersBean> providers) {
        this.providers = providers;
    }

    public String getUrlBackground() {
        return urlBackground;
    }

    public void setUrlBackground(String urlBackground) {
        this.urlBackground = urlBackground;
    }

    public static class ProvidersBean {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("color")
        @Expose
        private String color;

        private int imageResource;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public void setImageResource(int imageResource) {
            this.imageResource = imageResource;
        }

        public int getImageResource() {
            return imageResource;
        }
    }

    public LoginProviderModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.providers);
        dest.writeString(this.urlBackground);
    }

    protected LoginProviderModel(Parcel in) {
        this.providers = new ArrayList<ProvidersBean>();
        in.readList(this.providers, ProvidersBean.class.getClassLoader());
        this.urlBackground = in.readString();
    }

    public static final Creator<LoginProviderModel> CREATOR = new Creator<LoginProviderModel>() {
        @Override
        public LoginProviderModel createFromParcel(Parcel source) {
            return new LoginProviderModel(source);
        }

        @Override
        public LoginProviderModel[] newArray(int size) {
            return new LoginProviderModel[size];
        }
    };
}
