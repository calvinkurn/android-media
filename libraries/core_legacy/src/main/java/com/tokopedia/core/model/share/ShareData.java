package com.tokopedia.core.model.share;

import android.app.Activity;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;


/**
 * Created by Angga.Prasetiyo on 11/12/2015.
 */
public class ShareData implements Parcelable {
    public static final String TAG = ShareData.class.getSimpleName();
    public static final String CATALOG_TYPE = "Catalog";
    public static final String SHOP_TYPE = "Shop";
    public static final String PRODUCT_TYPE = "Product";
    public static final String CATEGORY_TYPE = "Directory";
    public static final String DISCOVERY_TYPE = "Discovery";
    public static final String HOTLIST_TYPE = "Hotlist";
    public static final String RIDE_TYPE = "Ride";
    public static final String PROMO_TYPE = "Promo";

    public static final String ARG_UTM_MEDIUM = "Share";
    private static final String DEFAULT_EMPTY_FIELD = "";
    public static final String APP_SHARE_TYPE = "App";
    public static final String REFERRAL_TYPE = "Referral";
    public static final String ARG_UTM_SOURCE = "Android";
    public static final String FEED_TYPE = "feed";
    public static final String GROUPCHAT_TYPE = "tokopedia_play";
    public static final String INDI_CHALLENGE_TYPE = "tokopedia_challenge";

    private String type = "";
    private String name;
    private String price;
    private String uri;
    private String description;
    private String imgUri;
    private String textContent;
    private String source;
    private String id = "";
    private String shareUrl;
    private String pathSticker;
    private String ogUrl;
    private String ogTitle;
    private String ogDescription;
    private String ogImageUrl;
    private String deepLink;


    public ShareData() {
    }

    protected ShareData(Parcel in) {
        type = in.readString();
        name = in.readString();
        price = in.readString();
        uri = in.readString();
        description = in.readString();
        imgUri = in.readString();
        textContent = in.readString();
        source = in.readString();
        id = in.readString();
        shareUrl = in.readString();
        pathSticker = in.readString();
        ogUrl = in.readString();
        ogTitle = in.readString();
        ogDescription = in.readString();
        ogImageUrl = in.readString();
        deepLink = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(uri);
        dest.writeString(description);
        dest.writeString(imgUri);
        dest.writeString(textContent);
        dest.writeString(source);
        dest.writeString(id);
        dest.writeString(shareUrl);
        dest.writeString(pathSticker);
        dest.writeString(ogUrl);
        dest.writeString(ogTitle);
        dest.writeString(ogDescription);
        dest.writeString(ogImageUrl);
        dest.writeString(deepLink);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShareData> CREATOR = new Creator<ShareData>() {
        @Override
        public ShareData createFromParcel(Parcel in) {
            return new ShareData(in);
        }

        @Override
        public ShareData[] newArray(int size) {
            return new ShareData[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDescription() {
        if (TextUtils.isEmpty(description)) {
            return DEFAULT_EMPTY_FIELD;
        } else {
            return String.valueOf(MethodChecker.fromHtml(description));
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getTextContent(Activity activity) {
        if (getType() != null) {
            return (this.textContent != null) ? (this.textContent + "\n" + renderShareUri()) : renderShareUri();
        }
        return String.valueOf(MethodChecker.fromHtml("Jual " + name + " hanya " + price + ", lihat gambar klik " + uri + "\n"));
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getshareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String renderShareUri() {
        if (getUri() == null) {
            return "";
        }
        String campaign = getCampaignName();

        String renderedUrl;
        if (!getType().equalsIgnoreCase(RIDE_TYPE)) {
            if (getUri().contains("?")) {
                Uri uri = Uri.parse(String.format("%s&utm_source=%s&utm_medium=%s&utm_campaign=%s",
                        getUri(), ARG_UTM_SOURCE, ARG_UTM_MEDIUM, campaign));
                renderedUrl = uri.toString();
            } else {
                Uri uri = Uri.parse(String.format("%s?utm_source=%s&utm_medium=%s&utm_campaign=%s",
                        getUri(), ARG_UTM_SOURCE, ARG_UTM_MEDIUM, campaign));
                renderedUrl = uri.toString();
            }
        } else {
            renderedUrl = getUri();
        }
        return renderedUrl;
    }

    public String getCampaignName() {
        String campaign = "Product Share";
        if (getType() != null)
            campaign = getType() + "%20Share";
        return campaign;
    }

    public String getTextContentForBranch(String shortUrl) {
        if (getType() != null) {
            return (this.textContent != null) ? (this.textContent + "\n" + shortUrl) : shortUrl;
        }
        return String.valueOf(MethodChecker.fromHtml("Jual " + name + " hanya " + price + ", lihat gambar klik " + uri + "\n"));
    }

    public String[] getSplittedDescription(String splitWith) {
        if (description.contains(splitWith))
            return description.split(splitWith);
        else
            return new String[0];
    }

    public String getPathSticker() {
        return pathSticker;
    }

    public void setPathSticker(String pathSticker) {
        this.pathSticker = pathSticker;
    }

    public String getOgUrl() {
        return ogUrl;
    }

    public void setOgUrl(String ogUrl) {
        this.ogUrl = ogUrl;
    }

    public String getOgTitle() {
        return ogTitle;
    }

    public void setOgTitle(String ogTitle) {
        this.ogTitle = ogTitle;
    }

    public String getOgDescription() {
        return ogDescription;
    }

    public void setOgDescription(String ogDescription) {
        this.ogDescription = ogDescription;
    }

    public String getOgImageUrl() {
        return ogImageUrl;
    }

    public void setOgImageUrl(String ogImageUrl) {
        this.ogImageUrl = ogImageUrl;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public static class Builder {
        private String name;
        private String price;
        private String uri;
        private String description;
        private String imgUri;
        private String type;
        private String textContent;
        private String source;
        private String id;
        private String shareUrl;
        private String pathSticker;
        private String ogUrl;
        private String ogTitle;
        private String ogDescription;
        private String ogImageUrl;
        private String deepLink;

        private Builder() {
        }

        public static Builder aShareData() {
            return new Builder();
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPrice(String price) {
            this.price = price;
            return this;
        }

        public Builder setUri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setImgUri(String imgUri) {
            this.imgUri = imgUri;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setTextContent(String textContent) {
            this.textContent = textContent;
            return this;
        }

        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
            return this;
        }
        public Builder setPathSticker(String pathSticker) {
            this.pathSticker = pathSticker;
            return this;
        }

        public Builder setOgUrl(String ogUrl) {
            this.ogUrl = ogUrl;
            return this;
        }

        public Builder setOgTitle(String ogTitle) {
            this.ogTitle = ogTitle;
            return this;
        }

        public Builder setOgDescription(String ogDescription) {
            this.ogDescription = ogDescription;
            return this;
        }

        public Builder setOgImageUrl(String ogImageUrl) {
            this.ogImageUrl = ogImageUrl;
            return this;
        }

        public Builder setDeepLink(String deepLink) {
            this.deepLink = deepLink;
            return this;
        }

        public Builder but() {
            return aShareData().setName(name).setPrice(price).setUri(uri).setDescription(description).setImgUri(imgUri).setShareUrl(shareUrl);
        }

        public ShareData build() {
            ShareData shareData = new ShareData();
            shareData.setName(name);
            shareData.setPrice(price);
            shareData.setUri(uri);
            shareData.setDescription(description);
            shareData.setImgUri(imgUri);
            shareData.setType(type);
            shareData.setTextContent(textContent);
            shareData.setSource(source);
            shareData.setId(id);
            shareData.setShareUrl(shareUrl);
            shareData.setPathSticker(pathSticker);
            shareData.setOgUrl(ogUrl);
            shareData.setOgTitle(ogTitle);
            shareData.setOgDescription(ogDescription);
            shareData.setOgImageUrl(ogImageUrl);
            shareData.setDeepLink(deepLink);
            return shareData;
        }

    }
}