package com.tokopedia.core.myproduct.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 23/12/2015.
 */

@Deprecated
@Parcel
public class MyShopInfoModel {

    /**
     * this is for parcelable
     */
    public MyShopInfoModel(){}

    @SerializedName("message_error")
    @Expose
    String[] message_error;

    @SerializedName("status")
    @Expose
    String status;

    @SerializedName("data")
    @Expose
    Data data;

    @SerializedName("config")
    @Expose
    String config;

    @SerializedName("image")
    @Expose
    String server_process_time;

    public String[] getMessage_error() {
        return message_error;
    }

    public void setMessage_error(String[] message_error) {
        this.message_error = message_error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getServer_process_time() {
        return server_process_time;
    }

    public void setServer_process_time(String server_process_time) {
        this.server_process_time = server_process_time;
    }

    @Parcel
    public static class Data {
        /**
         * this is for parcelable
         */
        public Data(){}

        @SerializedName("image")
        @Expose
        Image image;

        @SerializedName("closed_detail")
        @Expose
        ClosedDetail closed_detail;

        @SerializedName("is_allow")
        @Expose
        String is_allow;

        @SerializedName("info")
        @Expose
        Info info;

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public ClosedDetail getClosed_detail() {
            return closed_detail;
        }

        public void setClosed_detail(ClosedDetail closed_detail) {
            this.closed_detail = closed_detail;
        }

        public String getIs_allow() {
            return is_allow;
        }

        public void setIs_allow(String is_allow) {
            this.is_allow = is_allow;
        }

        public Info getInfo() {
            return info;
        }

        public void setInfo(Info info) {
            this.info = info;
        }
    }

    @Parcel
    public static class Image {
        /**
         * this is for parcelable
         */
        public Image(){}

        @SerializedName("logo")
        @Expose
        String logo;

        @SerializedName("og_img")
        @Expose
        String og_img;

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getOg_img() {
            return og_img;
        }

        public void setOg_img(String og_img) {
            this.og_img = og_img;
        }
    }

    @Parcel
    public static class ClosedDetail{

        /**
         * this is for parcelable
         */
        public ClosedDetail(){}

        @SerializedName("reason")
        @Expose
        String reason;

        @SerializedName("note")
        @Expose
        String note;

        @SerializedName("until")
        @Expose
        String until;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getUntil() {
            return until;
        }

        public void setUntil(String until) {
            this.until = until;
        }
    }

    @Parcel
    public static class Info {

        /**
         * this is for parcelable
         */
        public Info(){}

        @SerializedName("shop_is_closed_reason")
        @Expose
        String shop_is_closed_reason;

        @SerializedName("shop_open_since")
        @Expose
        String shop_open_since;

        @SerializedName("shop_avatar")
        @Expose
        String shop_avatar;

        @SerializedName("shop_is_gold")
        @Expose
        String shop_is_gold;

        @SerializedName("shop_is_closed_note")
        @Expose
        String shop_is_closed_note;

        @SerializedName("shop_owner_id")
        @Expose
        String shop_owner_id;

        @SerializedName("shop_owner_last_login")
        @Expose
        String shop_owner_last_login;

        @SerializedName("shop_is_closed_until")
        @Expose
        String shop_is_closed_until;

        @SerializedName("shop_already_favorited")
        @Expose
        String shop_already_favorited;

        @SerializedName("shop_description")
        @Expose
        String shop_description;

        @SerializedName("shop_lucky")
        @Expose
        String shop_lucky;

        @SerializedName("shop_id")
        @Expose
        String shop_id;

        @SerializedName("shop_domain")
        @Expose
        String shop_domain;

        @SerializedName("shop_name")
        @Expose
        String shop_name;

        @SerializedName("shop_cover")
        @Expose
        String shop_cover;

        @SerializedName("shop_location")
        @Expose
        String shop_location;

        @SerializedName("shop_total_favorit")
        @Expose
        String shop_total_favorit;

        @SerializedName("shop_reputation")
        @Expose
        String shop_reputation;

        @SerializedName("shop_min_badge_score")
        @Expose
        String shop_min_badge_score;

        @SerializedName("shop_tagline")
        @Expose
        String shop_tagline;

        @SerializedName("shop_has_terms")
        @Expose
        String shop_has_terms;

        @SerializedName("shop_url")
        @Expose
        String shop_url;

        @SerializedName("shop_is_owner")
        @Expose
        String shop_is_owner;

        @SerializedName("shop_reputation_badge")
        @Expose
        String shop_reputation_badge;

        @SerializedName("shop_status")
        @Expose
        String shop_status;

        public String getShop_is_closed_reason() {
            return shop_is_closed_reason;
        }

        public void setShop_is_closed_reason(String shop_is_closed_reason) {
            this.shop_is_closed_reason = shop_is_closed_reason;
        }

        public String getShop_open_since() {
            return shop_open_since;
        }

        public void setShop_open_since(String shop_open_since) {
            this.shop_open_since = shop_open_since;
        }

        public String getShop_avatar() {
            return shop_avatar;
        }

        public void setShop_avatar(String shop_avatar) {
            this.shop_avatar = shop_avatar;
        }

        public String getShop_is_gold() {
            return shop_is_gold;
        }

        public void setShop_is_gold(String shop_is_gold) {
            this.shop_is_gold = shop_is_gold;
        }

        public String getShop_is_closed_note() {
            return shop_is_closed_note;
        }

        public void setShop_is_closed_note(String shop_is_closed_note) {
            this.shop_is_closed_note = shop_is_closed_note;
        }

        public String getShop_owner_id() {
            return shop_owner_id;
        }

        public void setShop_owner_id(String shop_owner_id) {
            this.shop_owner_id = shop_owner_id;
        }

        public String getShop_owner_last_login() {
            return shop_owner_last_login;
        }

        public void setShop_owner_last_login(String shop_owner_last_login) {
            this.shop_owner_last_login = shop_owner_last_login;
        }

        public String getShop_is_closed_until() {
            return shop_is_closed_until;
        }

        public void setShop_is_closed_until(String shop_is_closed_until) {
            this.shop_is_closed_until = shop_is_closed_until;
        }

        public String getShop_already_favorited() {
            return shop_already_favorited;
        }

        public void setShop_already_favorited(String shop_already_favorited) {
            this.shop_already_favorited = shop_already_favorited;
        }

        public String getShop_description() {
            return shop_description;
        }

        public void setShop_description(String shop_description) {
            this.shop_description = shop_description;
        }

        public String getShop_lucky() {
            return shop_lucky;
        }

        public void setShop_lucky(String shop_lucky) {
            this.shop_lucky = shop_lucky;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getShop_domain() {
            return shop_domain;
        }

        public void setShop_domain(String shop_domain) {
            this.shop_domain = shop_domain;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getShop_cover() {
            return shop_cover;
        }

        public void setShop_cover(String shop_cover) {
            this.shop_cover = shop_cover;
        }

        public String getShop_location() {
            return shop_location;
        }

        public void setShop_location(String shop_location) {
            this.shop_location = shop_location;
        }

        public String getShop_total_favorit() {
            return shop_total_favorit;
        }

        public void setShop_total_favorit(String shop_total_favorit) {
            this.shop_total_favorit = shop_total_favorit;
        }

        public String getShop_reputation() {
            return shop_reputation;
        }

        public void setShop_reputation(String shop_reputation) {
            this.shop_reputation = shop_reputation;
        }

        public String getShop_min_badge_score() {
            return shop_min_badge_score;
        }

        public void setShop_min_badge_score(String shop_min_badge_score) {
            this.shop_min_badge_score = shop_min_badge_score;
        }

        public String getShop_tagline() {
            return shop_tagline;
        }

        public void setShop_tagline(String shop_tagline) {
            this.shop_tagline = shop_tagline;
        }

        public String getShop_has_terms() {
            return shop_has_terms;
        }

        public void setShop_has_terms(String shop_has_terms) {
            this.shop_has_terms = shop_has_terms;
        }

        public String getShop_url() {
            return shop_url;
        }

        public void setShop_url(String shop_url) {
            this.shop_url = shop_url;
        }

        public String getShop_is_owner() {
            return shop_is_owner;
        }

        public void setShop_is_owner(String shop_is_owner) {
            this.shop_is_owner = shop_is_owner;
        }

        public String getShop_reputation_badge() {
            return shop_reputation_badge;
        }

        public void setShop_reputation_badge(String shop_reputation_badge) {
            this.shop_reputation_badge = shop_reputation_badge;
        }

        public String getShop_status() {
            return shop_status;
        }

        public void setShop_status(String shop_status) {
            this.shop_status = shop_status;
        }
    }
}
