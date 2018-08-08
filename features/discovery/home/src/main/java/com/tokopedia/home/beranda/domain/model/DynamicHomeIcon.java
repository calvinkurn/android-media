package com.tokopedia.home.beranda.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by henrypriyono on 30/01/18.
 */

public class DynamicHomeIcon {

    @Expose
    @SerializedName("useCaseIcon")
    private List<UseCaseIcon> useCaseIcon;

    @Expose
    @SerializedName("dynamicIcon")
    private List<DynamicIcon> dynamicIcon;

    public List<UseCaseIcon> getUseCaseIcon() {
        return useCaseIcon;
    }

    public void setUseCaseIcon(List<UseCaseIcon> useCaseIcon) {
        this.useCaseIcon = useCaseIcon;
    }

    public List<DynamicIcon> getDynamicIcon() {
        return dynamicIcon;
    }

    public void setDynamicIcon(List<DynamicIcon> dynamicIcon) {
        this.dynamicIcon = dynamicIcon;
    }

    public class DynamicIcon {
        @Expose
        @SerializedName("id")
        private String id;

        @Expose
        @SerializedName("applinks")
        private String applinks;

        @Expose
        @SerializedName("imageUrl")
        private String imageUrl;

        @Expose
        @SerializedName("name")
        private String name;

        @Expose
        @SerializedName("url")
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApplinks() {
            return applinks;
        }

        public void setApplinks(String applinks) {
            this.applinks = applinks;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
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
    }

    public class UseCaseIcon {
        @Expose
        @SerializedName("id")
        private String id;

        @Expose
        @SerializedName("applinks")
        private String applinks;

        @Expose
        @SerializedName("imageUrl")
        private String imageUrl;

        @Expose
        @SerializedName("name")
        private String name;

        @Expose
        @SerializedName("url")
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApplinks() {
            return applinks;
        }

        public void setApplinks(String applinks) {
            this.applinks = applinks;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
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
    }
}
