package com.tokopedia.core.product.model.share;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

/**
 * Created by Angga.Prasetiyo on 11/12/2015.
 */
public class ShareData implements Parcelable {
    public static final String TAG = ShareData.class.getSimpleName();
    public static final String CATALOG_TYPE = "Catalog";
    public static final String SHOP_TYPE = "Shop";
    public static final String PRODUCT_TYPE = "Product";
    public static final String DISCOVERY_TYPE = "Discovery";
    public static final String HOTLIST_TYPE = "Hotlist";
    private static final String ARG_UTM_MEDIUM = "Android%20Share%20Button";

    private String type;
    private String name;
    private String price;
    private String uri;
    private String description;
    private String imgUri;
    private String textContent;
    private String source;

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
        return description;
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

    public String getTextContent() {
        if (getType() != null){
            return (this.textContent != null) ? (this.textContent + "\n" + renderShareUri()) : renderShareUri();
        }
        return String.valueOf(Html.fromHtml("Jual " + name + " hanya " + price + ", lihat gambar klik " + uri + "\n"));
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

    public String renderShareUri() {
        String campaign = "Product";
        if (getType() != null)
            campaign = getType();

        String renderedUrl;
        if (getUri().contains("?")) {
            Uri uri = Uri.parse(String.format("%s&utm_source=%s&utm_campaign=%s&utm_medium=%s",
                    getUri(), getSource(), campaign, ARG_UTM_MEDIUM));
            renderedUrl = uri.toString();
        } else {
            Uri uri = Uri.parse(String.format("%s?utm_source=%s&utm_campaign=%s&utm_medium=%s",
                    getUri(), getSource(), campaign, ARG_UTM_MEDIUM));
            renderedUrl = uri.toString();
        }
        return renderedUrl;
    }

    public static class Builder {
        private String name;
        private String price;
        private String uri;
        private String description;
        private String imgUri;
        private Bitmap bitmap;
        private String type;
        private String textContent;
        private String source;

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

        public Builder setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
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

        public Builder but() {
            return aShareData().setName(name).setPrice(price).setUri(uri).setDescription(description).setImgUri(imgUri).setBitmap(bitmap);
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
            return shareData;
        }
    }
}
