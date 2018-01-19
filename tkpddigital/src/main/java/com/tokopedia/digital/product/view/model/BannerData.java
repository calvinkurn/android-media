package com.tokopedia.digital.product.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public class BannerData implements Parcelable {
    public static final String DEFAULT_TYPE_CONTRACT = "banner";
    public static final String OTHER_TYPE_CONTRACT = "other_banner";
    private String id;
    private String type;
    private int rechargeCmsbannerId;
    private String fileName;
    private String fileNameWebp;
    private String startDate;
    private String endDate;
    private String imgUrl;
    private int priority;
    private int status;
    private String title;
    private String subtitle;
    private String promocode;
    private String dataTitle;
    private String image;
    private String link;

    private boolean voucherCodeCopied;

    private BannerData(Builder builder) {
        setId(builder.id);
        setType(builder.type);
        setRechargeCmsbannerId(builder.rechargeCmsbannerId);
        setFileName(builder.fileName);
        setFileNameWebp(builder.fileNameWebp);
        setStartDate(builder.startDate);
        setEndDate(builder.endDate);
        setImgUrl(builder.imgUrl);
        setPriority(builder.priority);
        setStatus(builder.status);
        setTitle(builder.title);
        setSubtitle(builder.subtitle);
        setPromocode(builder.promocode);
        setDataTitle(builder.dataTitle);
        setImage(builder.image);
        setLink(builder.link);
        setVoucherCodeCopied(builder.voucherCodeCopied);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRechargeCmsbannerId() {
        return rechargeCmsbannerId;
    }

    public void setRechargeCmsbannerId(int rechargeCmsbannerId) {
        this.rechargeCmsbannerId = rechargeCmsbannerId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameWebp() {
        return fileNameWebp;
    }

    public void setFileNameWebp(String fileNameWebp) {
        this.fileNameWebp = fileNameWebp;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }

    public boolean isVoucherCodeCopied() {
        return voucherCodeCopied;
    }

    public void setVoucherCodeCopied(boolean voucherCodeCopied) {
        this.voucherCodeCopied = voucherCodeCopied;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public BannerData() {
    }


    public static final class Builder {
        private String id;
        private String type;
        private int rechargeCmsbannerId;
        private String fileName;
        private String fileNameWebp;
        private String startDate;
        private String endDate;
        private String imgUrl;
        private int priority;
        private int status;
        private String title;
        private String subtitle;
        private String promocode;
        private String dataTitle;
        private String image;
        private String link;
        private boolean voucherCodeCopied;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder rechargeCmsbannerId(int val) {
            rechargeCmsbannerId = val;
            return this;
        }

        public Builder fileName(String val) {
            fileName = val;
            return this;
        }

        public Builder fileNameWebp(String val) {
            fileNameWebp = val;
            return this;
        }

        public Builder startDate(String val) {
            startDate = val;
            return this;
        }

        public Builder endDate(String val) {
            endDate = val;
            return this;
        }

        public Builder imgUrl(String val) {
            imgUrl = val;
            return this;
        }

        public Builder priority(int val) {
            priority = val;
            return this;
        }

        public Builder status(int val) {
            status = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder subtitle(String val) {
            subtitle = val;
            return this;
        }

        public Builder promocode(String val) {
            promocode = val;
            return this;
        }

        public Builder dataTitle(String val) {
            dataTitle = val;
            return this;
        }

        public Builder image(String val) {
            image = val;
            return this;
        }

        public Builder link(String val) {
            link = val;
            return this;
        }

        public Builder voucherCodeCopied(boolean val) {
            voucherCodeCopied = val;
            return this;
        }

        public BannerData build() {
            return new BannerData(this);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeInt(this.rechargeCmsbannerId);
        dest.writeString(this.fileName);
        dest.writeString(this.fileNameWebp);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.imgUrl);
        dest.writeInt(this.priority);
        dest.writeInt(this.status);
        dest.writeString(this.title);
        dest.writeString(this.subtitle);
        dest.writeString(this.promocode);
        dest.writeString(this.dataTitle);
        dest.writeString(this.image);
        dest.writeString(this.link);
        dest.writeByte(this.voucherCodeCopied ? (byte) 1 : (byte) 0);
    }

    protected BannerData(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.rechargeCmsbannerId = in.readInt();
        this.fileName = in.readString();
        this.fileNameWebp = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.imgUrl = in.readString();
        this.priority = in.readInt();
        this.status = in.readInt();
        this.title = in.readString();
        this.subtitle = in.readString();
        this.promocode = in.readString();
        this.dataTitle = in.readString();
        this.image = in.readString();
        this.link = in.readString();
        this.voucherCodeCopied = in.readByte() != 0;
    }

    public static final Creator<BannerData> CREATOR = new Creator<BannerData>() {
        @Override
        public BannerData createFromParcel(Parcel source) {
            return new BannerData(source);
        }

        @Override
        public BannerData[] newArray(int size) {
            return new BannerData[size];
        }
    };


}
