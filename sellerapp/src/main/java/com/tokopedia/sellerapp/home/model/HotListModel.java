package com.tokopedia.sellerapp.home.model;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 28/10/2015.
 */
@Parcel
public class HotListModel extends RecyclerViewItem {
    @SerializedName("title")
    String hotListName;
    @SerializedName("price_start")
    String hotListPrice;
    @SerializedName("image_url")
    String hotListImage;
    @SerializedName("image_url_600")
    String hotListBiggerImage;
    @SerializedName("url")
    String hotListProductUrl;
    String hotListKey;

    public HotListModel(){
        setType(TkpdState.RecyclerView.VIEW_STANDARD);
    }

    public String getHotListImage() {
        return hotListImage;
    }

    public void setHotListImage(String hotListImage) {
        this.hotListImage = hotListImage;
    }

    public String getHotListPrice() {
        return hotListPrice;
    }

    public void setHotListPrice(String hotListPrice) {
        this.hotListPrice = hotListPrice;
    }

    public String getHotListName() {
        return hotListName;
    }

    public void setHotListName(String hotListName) {
        this.hotListName = hotListName;
    }

    public String getHotListBiggerImage() {
        return hotListBiggerImage;
    }

    public void setHotListBiggerImage(String hotListBiggerImage) {
        this.hotListBiggerImage = hotListBiggerImage;
    }

    public String getHotListProductUrl() {
        return hotListProductUrl;
    }

    public void setHotListProductUrl(String hotListProductUrl) {
        this.hotListProductUrl = hotListProductUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HotListModel that = (HotListModel) o;

        if (hotListName != null ? !hotListName.equals(that.hotListName) : that.hotListName != null)
            return false;
        if (hotListPrice != null ? !hotListPrice.equals(that.hotListPrice) : that.hotListPrice != null)
            return false;
        if (hotListImage != null ? !hotListImage.equals(that.hotListImage) : that.hotListImage != null)
            return false;
        if (hotListBiggerImage != null ? !hotListBiggerImage.equals(that.hotListBiggerImage) : that.hotListBiggerImage != null)
            return false;
        if (hotListProductUrl != null ? !hotListProductUrl.equals(that.hotListProductUrl) : that.hotListProductUrl != null)
            return false;
        return !(hotListKey != null ? !hotListKey.equals(that.hotListKey) : that.hotListKey != null);

    }

    @Override
    public int hashCode() {
        int result = hotListName != null ? hotListName.hashCode() : 0;
        result = 31 * result + (hotListPrice != null ? hotListPrice.hashCode() : 0);
        result = 31 * result + (hotListImage != null ? hotListImage.hashCode() : 0);
        result = 31 * result + (hotListBiggerImage != null ? hotListBiggerImage.hashCode() : 0);
        result = 31 * result + (hotListProductUrl != null ? hotListProductUrl.hashCode() : 0);
        result = 31 * result + (hotListKey != null ? hotListKey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HotListModel{" +
                "hotListName='" + hotListName + '\'' +
                ", hotListPrice='" + hotListPrice + '\'' +
                ", hotListImage='" + hotListImage + '\'' +
                ", hotListBiggerImage='" + hotListBiggerImage + '\'' +
                ", hotListProductUrl='" + hotListProductUrl + '\'' +
                ", hotListKey='" + hotListKey + '\'' +
                '}';
    }
}
