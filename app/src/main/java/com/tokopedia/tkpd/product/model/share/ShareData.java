package com.tokopedia.tkpd.product.model.share;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

/**
 * Created by Angga.Prasetiyo on 11/12/2015.
 */
public class ShareData implements Parcelable {
    public static final String TAG = ShareData.class.getSimpleName();
    private String type;
    private String name;
    private String price;
    private String uri;
    private String description;
    private String imgUri;
    private Bitmap bitmap;
    private String textContent;

    public ShareData() {
    }

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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getTextContent() {
        if (getType() != null){
            return this.textContent;
        }
        return String.valueOf(Html.fromHtml("Jual " + name + " hanya " + price + ", lihat gambar klik " + uri));
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

    protected ShareData(Parcel in) {
        name = in.readString();
        price = in.readString();
        uri = in.readString();
        description = in.readString();
        imgUri = in.readString();
        bitmap = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        textContent = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(uri);
        dest.writeString(description);
        dest.writeString(imgUri);
        dest.writeValue(bitmap);
        dest.writeString(textContent);
        dest.writeString(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ShareData> CREATOR = new Parcelable.Creator<ShareData>() {
        @Override
        public ShareData createFromParcel(Parcel in) {
            return new ShareData(in);
        }

        @Override
        public ShareData[] newArray(int size) {
            return new ShareData[size];
        }
    };


    public static class Builder {
        private String name;
        private String price;
        private String uri;
        private String description;
        private String imgUri;
        private Bitmap bitmap;
        private String type;
        private String textContent;

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
            shareData.setBitmap(bitmap);
            shareData.setType(type);
            shareData.setTextContent(textContent);
            return shareData;
        }
    }
}
