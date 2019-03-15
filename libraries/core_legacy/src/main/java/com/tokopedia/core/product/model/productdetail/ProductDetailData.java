package com.tokopedia.core.product.model.productdetail;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.network.entity.variant.Campaign;
import com.tokopedia.core.product.model.productdetail.discussion.LatestTalkViewModel;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 28/10/2015.
 */
@Deprecated
public class ProductDetailData implements Parcelable {
    private static final String TAG = ProductDetailData.class.getSimpleName();

    @SerializedName("info")
    @Expose
    private ProductInfo info;
    @SerializedName("statistic")
    @Expose
    private ProductStatistic statistic;
    @SerializedName("shop_info")
    @Expose
    private ProductShopInfo shopInfo;
    @SerializedName("wholesale_price")
    @Expose
    private List<ProductWholesalePrice> wholesalePrice = new ArrayList<ProductWholesalePrice>();
    @SerializedName("breadcrumb")
    @Expose
    private List<ProductBreadcrumb> breadcrumb = new ArrayList<ProductBreadcrumb>();
    @SerializedName("rating")
    @Expose
    private ProductRating rating;
    @SerializedName("preorder")
    @Expose
    private ProductPreOrder preOrder;
    @SerializedName("cashback")
    @Expose
    private ProductCashback cashBack;
    @SerializedName("product_images")
    @Expose
    private List<ProductImage> productImages = new ArrayList<ProductImage>();
    @SerializedName("campaign")
    private Campaign campaign2;

    public long getServerTimeUnix() {
        return serverTimeUnix;
    }

    public void setServerTimeUnix(long serverTimeUnix) {
        this.serverTimeUnix = serverTimeUnix;
    }

    public static Creator<ProductDetailData> getCREATOR() {
        return CREATOR;
    }

    @SerializedName("server_time_unix")
    @Expose
    private long serverTimeUnix;
    @SerializedName("is_big_promo")
    @Expose
    private boolean isBigPromo;
    @SerializedName("checkout_type")
    @Expose
    private String checkoutType;

    /**
     * this is not supposed to be here
     * because it is ViewModel
     * but because this pojo used in the view
     */
    private LatestTalkViewModel latestTalkViewModel;
    private List<Review> reviewList;
    private Campaign campaign;

    public ProductDetailData() {
    }


    public ProductInfo getInfo() {
        return info;
    }

    public void setInfo(ProductInfo info) {
        this.info = info;
    }

    public ProductStatistic getStatistic() {
        return statistic;
    }

    public void setStatistic(ProductStatistic statistic) {
        this.statistic = statistic;
    }

    public ProductShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ProductShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    public List<ProductWholesalePrice> getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(List<ProductWholesalePrice> wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public List<ProductBreadcrumb> getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(List<ProductBreadcrumb> breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    public ProductRating getRating() {
        return rating;
    }

    public void setCashBack(ProductCashback cashBack) {
        this.cashBack = cashBack;
    }

    public ProductCashback getCashBack() {
        return cashBack;
    }

    public ProductPreOrder getPreOrder() {
        return preOrder;
    }

    public void setPreOrder(ProductPreOrder preOrder) {
        this.preOrder = preOrder;
    }

    public void setRating(ProductRating rating) {
        this.rating = rating;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public void setLatestTalkViewModel(LatestTalkViewModel latestTalkViewModel) {
        this.latestTalkViewModel = latestTalkViewModel;
    }

    public LatestTalkViewModel getLatestTalkViewModel() {
        return latestTalkViewModel;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Campaign getCampaign2() {
        return campaign2;
    }

    public void setCampaign2(Campaign campaign2) {
        this.campaign2 = campaign2;
    }

    public boolean isBigPromo() {
        return isBigPromo;
    }

    public String getCheckoutType() {
        return checkoutType;
    }

    public void setBigPromo(boolean bigPromo) {
        isBigPromo = bigPromo;
    }

    public void setCheckoutType(String checkoutType) {
        this.checkoutType = checkoutType;
    }

    public static class Builder {
        private ProductInfo info;
        private ProductStatistic statistic;
        private ProductShopInfo shopInfo;
        private List<ProductWholesalePrice> wholesalePrice = new ArrayList<ProductWholesalePrice>();
        private List<ProductBreadcrumb> breadcrumb = new ArrayList<ProductBreadcrumb>();
        private ProductRating rating;
        private List<ProductImage> productImages = new ArrayList<ProductImage>();
        private ProductCashback cashBack;
        private long serverTimeUnix;
        private boolean isBigPromo;
        private String checkoutType;

        private Builder() {
        }

        public static Builder aProductInfoData() {
            return new Builder();
        }

        public Builder setInfo(ProductInfo info) {
            this.info = info;
            return this;
        }

        public Builder setStatistic(ProductStatistic statistic) {
            this.statistic = statistic;
            return this;
        }

        public Builder setShopInfo(ProductShopInfo shopInfo) {
            this.shopInfo = shopInfo;
            return this;
        }

        public Builder setWholesalePrice(List<ProductWholesalePrice> wholesalePrice) {
            this.wholesalePrice = wholesalePrice;
            return this;
        }

        public Builder setBreadcrumb(List<ProductBreadcrumb> breadcrumb) {
            this.breadcrumb = breadcrumb;
            return this;
        }

        public Builder setRating(ProductRating rating) {
            this.rating = rating;
            return this;
        }

        public Builder setProductImages(List<ProductImage> productImages) {
            this.productImages = productImages;
            return this;
        }

        public Builder setCashBack(ProductCashback cashBack) {
            this.cashBack = cashBack;
            return this;
        }

        public Builder setServerTimeUnix(long serverTimeUnix) {
            this.serverTimeUnix = serverTimeUnix;
            return this;
        }

        public Builder setIsBigPromo(boolean isBigPromo) {
            this.isBigPromo = isBigPromo;
            return this;
        }

        public Builder setCheckoutType(String checkoutType) {
            this.checkoutType = checkoutType;
            return this;
        }

        public Builder but() {
            return aProductInfoData().setInfo(info).setStatistic(statistic).setShopInfo(shopInfo).setWholesalePrice(wholesalePrice).setBreadcrumb(breadcrumb).setRating(rating).setProductImages(productImages).setCashBack(cashBack).setServerTimeUnix(serverTimeUnix).setIsBigPromo(isBigPromo).setCheckoutType(checkoutType);
        }

        public ProductDetailData build() {
            ProductDetailData productDetailData = new ProductDetailData();
            productDetailData.setInfo(info);
            productDetailData.setStatistic(statistic);
            productDetailData.setShopInfo(shopInfo);
            productDetailData.setWholesalePrice(wholesalePrice);
            productDetailData.setBreadcrumb(breadcrumb);
            productDetailData.setRating(rating);
            productDetailData.setProductImages(productImages);
            productDetailData.setCashBack(cashBack);
            productDetailData.setServerTimeUnix(serverTimeUnix);
            productDetailData.setBigPromo(isBigPromo);
            productDetailData.setCheckoutType(checkoutType);
            return productDetailData;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.info, flags);
        dest.writeParcelable(this.statistic, flags);
        dest.writeParcelable(this.shopInfo, flags);
        dest.writeTypedList(this.wholesalePrice);
        dest.writeTypedList(this.breadcrumb);
        dest.writeParcelable(this.rating, flags);
        dest.writeParcelable(this.preOrder, flags);
        dest.writeParcelable(this.cashBack, flags);
        dest.writeTypedList(this.productImages);
        dest.writeParcelable(this.latestTalkViewModel, flags);
        dest.writeTypedList(this.reviewList);
        dest.writeParcelable(this.campaign, flags);
        dest.writeLong(this.serverTimeUnix);
        dest.writeByte(this.isBigPromo ? (byte) 1 : (byte) 0);
        dest.writeString(this.checkoutType);
    }

    protected ProductDetailData(Parcel in) {
        this.info = in.readParcelable(ProductInfo.class.getClassLoader());
        this.statistic = in.readParcelable(ProductStatistic.class.getClassLoader());
        this.shopInfo = in.readParcelable(ProductShopInfo.class.getClassLoader());
        this.wholesalePrice = in.createTypedArrayList(ProductWholesalePrice.CREATOR);
        this.breadcrumb = in.createTypedArrayList(ProductBreadcrumb.CREATOR);
        this.rating = in.readParcelable(ProductRating.class.getClassLoader());
        this.preOrder = in.readParcelable(ProductPreOrder.class.getClassLoader());
        this.cashBack = in.readParcelable(ProductCashback.class.getClassLoader());
        this.productImages = in.createTypedArrayList(ProductImage.CREATOR);
        this.latestTalkViewModel = in.readParcelable(LatestTalkViewModel.class.getClassLoader());
        this.reviewList = in.createTypedArrayList(Review.CREATOR);
        this.campaign = in.readParcelable(Campaign.class.getClassLoader());
        this.serverTimeUnix = in.readLong();
        this.isBigPromo = in.readByte() == 1;
        this.checkoutType = in.readString();
    }

    public static final Creator<ProductDetailData> CREATOR = new Creator<ProductDetailData>() {
        @Override
        public ProductDetailData createFromParcel(Parcel source) {
            return new ProductDetailData(source);
        }

        @Override
        public ProductDetailData[] newArray(int size) {
            return new ProductDetailData[size];
        }
    };

    public String getEnhanceCategoryFormatted() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < getBreadcrumb().size(); i++) {
            list.add(getBreadcrumb().get(i).getDepartmentName());
        }
        return TextUtils.join("/", list);
    }

    public String getEnhanceUrl(String url) {
        Uri uri = Uri.parse(url);
        return uri.getLastPathSegment();
    }

    public String getEnhanceShopType() {
        if (getShopInfo().getShopIsOfficial() == 1) {
            return "official_store";
        } else if (getShopInfo().getShopIsGold() == 1) {
            return "gold_merchant";
        } else {
            return "regular";
        }
    }
}
