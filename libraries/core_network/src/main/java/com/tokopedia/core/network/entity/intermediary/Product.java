
package com.tokopedia.core.network.entity.intermediary;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class Product implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("department_id")
    @Expose
    private Integer departmentId;
    @SerializedName("condition")
    @Expose
    private Integer condition;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("image_url_700")
    @Expose
    private String imageUrl700;
    @SerializedName("badges")
    @Expose
    private List<Badge> badges = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("labels")
    @Expose
    private List<Label> labels = null;
    @SerializedName("shop")
    @Expose
    private Shop shop;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl700() {
        return imageUrl700;
    }

    public void setImageUrl700(String imageUrl700) {
        this.imageUrl700 = imageUrl700;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }



    protected Product(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        departmentId = in.readByte() == 0x00 ? null : in.readInt();
        condition = in.readByte() == 0x00 ? null : in.readInt();
        imageUrl = in.readString();
        imageUrl700 = in.readString();
        if (in.readByte() == 0x01) {
            badges = new ArrayList<Badge>();
            in.readList(badges, Badge.class.getClassLoader());
        } else {
            badges = null;
        }
        name = in.readString();
        price = in.readString();
        rating = in.readByte() == 0x00 ? null : in.readDouble();
        url = in.readString();
        if (in.readByte() == 0x01) {
            labels = new ArrayList<Label>();
            in.readList(labels, Label.class.getClassLoader());
        } else {
            labels = null;
        }
        shop = (Shop) in.readValue(Shop.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        if (departmentId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(departmentId);
        }
        if (condition == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(condition);
        }
        dest.writeString(imageUrl);
        dest.writeString(imageUrl700);
        if (badges == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(badges);
        }
        dest.writeString(name);
        dest.writeString(price);
        if (rating == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(rating);
        }
        dest.writeString(url);
        if (labels == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(labels);
        }
        dest.writeValue(shop);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}