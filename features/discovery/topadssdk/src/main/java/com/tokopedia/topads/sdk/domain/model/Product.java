package com.tokopedia.topads.sdk.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by errysuprayogi on 3/27/17.
 */

public class Product implements Parcelable {

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_WISHLIST = "wishlist";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_URI = "uri";
    private static final String KEY_RELATIVE_URI = "relative_uri";
    private static final String KEY_PRICE_FORMAT = "price_format";
    private static final String KEY_COUNT_TALK_FORMAT = "count_talk_format";
    private static final String KEY_COUNT_REVIEW_FORMAT = "count_review_format";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_PRODUCT_PREORDER = "product_preorder";
    private static final String KEY_PRODUCT_WHOLESALE = "product_wholesale";
    private static final String KEY_FREERETURN = "free_feturn";
    private static final String KEY_PRODUCT_CASHBACK = "product_cashback";
    private static final String KEY_PRODUCT_CASHBACK_RATE = "product_cashback_rate";
    private static final String KEY_PRODUCT_NEW_LABEL = "product_new_label";
    private static final String KEY_PRODUCT_RATE_FORMAT = "product_rating_format";
    private static final String KEY_PRODUCT_RATE = "product_rating";
    private static final String KEY_WHOLESALE_PRICE = "wholesale_price";
    private static final String KEY_LABELS = "labels";
    private static final String KEY_TOP_LABEL = "top_label";
    private static final String KEY_BOTTOM_LABEL = "bottom_label";
    private static final String KEY_APPLINKS = "applinks";
    private static final String KEY_IMAGE_PRODUCT = "image_product";
    private static final String KEY_CAMPAIGN = "campaign";
    private static final String KEY_LABEL_GROUP = "label_group";
    private static final String KEY_FREE_ONGKIR = "free_ongkir";
    private static final String KEY_CATEGORY_BREADCRUMB = "category_breadcrumb";
    private static final String KEY_PRODUCT_ITEM_SOLD_PAYMENT_VERIFIED = "product_item_sold_payment_verified";
    private static final String KEY_PRODUCT_MINIMUM_ORDER = "product_minimum_order";
    private static final String KEY_HEADLINE_PRODUCT_RATING_AVERAGE = "rating_average";

    @SerializedName(KEY_ID)
    @Expose
    private String id = "";
    private String adRefKey = "";
    private String adId = "";
    @SerializedName(KEY_NAME)
    @Expose
    private String name = "";

    @SerializedName(KEY_WISHLIST)
    @Expose
    private boolean wishlist = false;

    @SerializedName(KEY_IMAGE)
    @Expose
    private ProductImage image = new ProductImage();

    @SerializedName(KEY_URI)
    @Expose
    private String uri = "";

    @SerializedName(KEY_RELATIVE_URI)
    @Expose
    private String relativeUri = "";

    @SerializedName(KEY_PRICE_FORMAT)
    @Expose
    private String priceFormat = "";

    @SerializedName(KEY_COUNT_TALK_FORMAT)
    @Expose
    private String countTalkFormat = "";

    @SerializedName(KEY_COUNT_REVIEW_FORMAT)
    private String countReviewFormat = "0";

    @SerializedName(KEY_CATEGORY)
    @Expose
    private Category category = new Category();

    @SerializedName(KEY_PRODUCT_PREORDER)
    @Expose
    private boolean productPreorder = false;

    @SerializedName(KEY_PRODUCT_WHOLESALE)
    @Expose
    private boolean productWholesale = false;

    @SerializedName(KEY_FREERETURN)
    @Expose
    private String freeReturn = "";

    @SerializedName(KEY_PRODUCT_CASHBACK)
    @Expose
    private boolean productCashback = false;

    @SerializedName(KEY_PRODUCT_CASHBACK_RATE)
    @Expose
    private String productCashbackRate = "";

    @SerializedName(KEY_PRODUCT_NEW_LABEL)
    @Expose
    private boolean productNewLabel = false;

    @SerializedName(KEY_PRODUCT_RATE_FORMAT)
    @Expose
    private String productRatingFormat = "";

    @SerializedName(KEY_PRODUCT_RATE)
    @Expose
    private int productRating = 0;

    @SerializedName(KEY_APPLINKS)
    @Expose
    private String applinks = "";

    @SerializedName(KEY_WHOLESALE_PRICE)
    @Expose
    private List<WholesalePrice> wholesalePrice = new ArrayList<>();

    @SerializedName(KEY_LABELS)
    @Expose
    private List<Label> labels = new ArrayList<>();

    @SerializedName(KEY_TOP_LABEL)
    @Expose
    private List<String> topLabels = new ArrayList<>();

    @SerializedName(KEY_BOTTOM_LABEL)
    @Expose
    private List<String> bottomLabels = new ArrayList<>();

    @SerializedName(KEY_IMAGE_PRODUCT)
    @Expose
    private ImageProduct imageProduct = new ImageProduct();

    @SerializedName(KEY_CAMPAIGN)
    @Expose
    private Campaign campaign = new Campaign();

    @SerializedName(KEY_LABEL_GROUP)
    @Expose
    private List<LabelGroup> labelGroupList = new ArrayList<>();

    @SerializedName(KEY_FREE_ONGKIR)
    @Expose
    private FreeOngkir freeOngkir = new FreeOngkir();

    @SerializedName(KEY_CATEGORY_BREADCRUMB)
    @Expose
    private String categoryBreadcrumb = "";

    @SerializedName(KEY_PRODUCT_ITEM_SOLD_PAYMENT_VERIFIED)
    @Expose
    private String countSold = "";

    @SerializedName(KEY_PRODUCT_MINIMUM_ORDER)
    @Expose
    private int productMinimumOrder = 0;

    @SerializedName(KEY_HEADLINE_PRODUCT_RATING_AVERAGE)
    @Expose
    private String headlineProductRatingAverage = "";

    private boolean topAds = false;

    private String recommendationType = "";

    private boolean loaded = false;

    public Product() {
    }

    public Product(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_ID)){
            setId(object.getString(KEY_ID));
        }
        if(!object.isNull(KEY_NAME)){
            setName(object.getString(KEY_NAME));
        }
        if(!object.isNull(KEY_WISHLIST)){
            setWishlist(object.getBoolean(KEY_WISHLIST));
        }
        if(!object.isNull(KEY_IMAGE)){
            setImage(new ProductImage(object.getJSONObject(KEY_IMAGE)));
        }
        if(!object.isNull(KEY_URI)){
            setUri(object.getString(KEY_URI));
        }
        if(!object.isNull(KEY_RELATIVE_URI)){
            setRelativeUri(object.getString(KEY_RELATIVE_URI));
        }
        if(!object.isNull(KEY_PRICE_FORMAT)){
            setPriceFormat(object.getString(KEY_PRICE_FORMAT));
        }
        if(!object.isNull(KEY_COUNT_TALK_FORMAT)){
            setCountTalkFormat(object.getString(KEY_COUNT_TALK_FORMAT));
        }
        if(!object.isNull(KEY_COUNT_REVIEW_FORMAT)){
            setCountReviewFormat(object.getString(KEY_COUNT_REVIEW_FORMAT));
        }
        if(!object.isNull(KEY_CATEGORY)){
            setCategory(new Category(object.getJSONObject(KEY_CATEGORY)));
        }
        if(!object.isNull(KEY_PRODUCT_PREORDER)){
            setProductPreorder(object.getBoolean(KEY_PRODUCT_PREORDER));
        }
        if(!object.isNull(KEY_PRODUCT_WHOLESALE)){
            setProductWholesale(object.getBoolean(KEY_PRODUCT_WHOLESALE));
        }
        if(!object.isNull(KEY_FREERETURN)){
            setFreeReturn(object.getString(KEY_FREERETURN));
        }
        if(!object.isNull(KEY_PRODUCT_CASHBACK)){
            setProductCashback(object.getBoolean(KEY_PRODUCT_CASHBACK));
        }
        if(!object.isNull(KEY_PRODUCT_NEW_LABEL)){
            setProductNewLabel(object.getBoolean(KEY_PRODUCT_NEW_LABEL));
        }
        if(!object.isNull(KEY_APPLINKS)){
            setApplinks(object.getString(KEY_APPLINKS));
        }
        if(!object.isNull(KEY_PRODUCT_CASHBACK_RATE)){
            setProductCashbackRate(object.getString(KEY_PRODUCT_CASHBACK_RATE));
        }
        if(!object.isNull(KEY_PRODUCT_RATE_FORMAT)) {
            setProductRatingFormat(object.getString(KEY_PRODUCT_RATE_FORMAT));
        }
        if(!object.isNull(KEY_PRODUCT_RATE)){
            setProductRating(object.getInt(KEY_PRODUCT_RATE));
        }
        if(!object.isNull(KEY_IMAGE_PRODUCT)){
            setImageProduct(new ImageProduct(object.getJSONObject(KEY_IMAGE_PRODUCT)));
        }
        if(!object.isNull(KEY_WHOLESALE_PRICE)){
            JSONArray wholesalePriceArray = object.getJSONArray(KEY_WHOLESALE_PRICE);
            for (int i = 0; i < wholesalePriceArray.length(); i++) {
                wholesalePrice.add(new WholesalePrice(wholesalePriceArray.getJSONObject(i)));
            }
        }
        if(!object.isNull(KEY_LABELS)) {
            JSONArray labelArray = object.getJSONArray(KEY_LABELS);
            for (int i = 0; i < labelArray.length(); i++) {
                labels.add(new Label(labelArray.getJSONObject(i)));
            }
        }
        if(!object.isNull(KEY_TOP_LABEL)) {
            JSONArray arr = object.getJSONArray(KEY_TOP_LABEL);
            for (int i = 0; i < arr.length(); i++) {
                topLabels.add(arr.getString(i));
            }
        }
        if(!object.isNull(KEY_BOTTOM_LABEL)) {
            JSONArray arr = object.getJSONArray(KEY_BOTTOM_LABEL);
            for (int i = 0; i < arr.length(); i++) {
                bottomLabels.add(arr.getString(i));
            }
        }
        if(!object.isNull(KEY_CAMPAIGN)) {
            setCampaign(new Campaign(object.getJSONObject(KEY_CAMPAIGN)));
        }
        if(!object.isNull(KEY_LABEL_GROUP)) {
            JSONArray arr = object.getJSONArray(KEY_LABEL_GROUP);
            for (int i = 0; i < arr.length(); i++) {
                labelGroupList.add(new LabelGroup(arr.getJSONObject(i)));
            }
        }
        if(!object.isNull(KEY_FREE_ONGKIR)) {
            setFreeOngkir(new FreeOngkir(object.getJSONObject(KEY_FREE_ONGKIR)));
        }
        if(!object.isNull(KEY_CATEGORY_BREADCRUMB)){
            setCategoryBreadcrumb(object.getString(KEY_CATEGORY_BREADCRUMB));
        }
        if(!object.isNull(KEY_PRODUCT_ITEM_SOLD_PAYMENT_VERIFIED)){
            setCountSold(object.getString(KEY_PRODUCT_ITEM_SOLD_PAYMENT_VERIFIED));
        }
        if(!object.isNull(KEY_PRODUCT_MINIMUM_ORDER)) {
            setProductMinimumOrder(object.getInt(KEY_PRODUCT_MINIMUM_ORDER));
        }
        if(!object.isNull(KEY_HEADLINE_PRODUCT_RATING_AVERAGE)) {
            setHeadlineProductRatingAverage(object.getString(KEY_HEADLINE_PRODUCT_RATING_AVERAGE));
        }
    }

    protected Product(Parcel in) {
        id = in.readString();
        adRefKey = in.readString();
        adId = in.readString();
        name = in.readString();
        wishlist = in.readByte() != 0;
        image = in.readParcelable(ProductImage.class.getClassLoader());
        uri = in.readString();
        relativeUri = in.readString();
        priceFormat = in.readString();
        countTalkFormat = in.readString();
        countReviewFormat = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        productPreorder = in.readByte() != 0;
        productWholesale = in.readByte() != 0;
        freeReturn = in.readString();
        productCashback = in.readByte() != 0;
        productCashbackRate = in.readString();
        productNewLabel = in.readByte() != 0;
        productRatingFormat = in.readString();
        productRating = in.readInt();
        applinks = in.readString();
        wholesalePrice = in.createTypedArrayList(WholesalePrice.CREATOR);
        labels = in.createTypedArrayList(Label.CREATOR);
        topLabels = in.createStringArrayList();
        bottomLabels = in.createStringArrayList();
        imageProduct = in.readParcelable(ImageProduct.class.getClassLoader());
        campaign = in.readParcelable(Campaign.class.getClassLoader());
        labelGroupList = in.createTypedArrayList(LabelGroup.CREATOR);
        freeOngkir = in.readParcelable(FreeOngkir.class.getClassLoader());
        categoryBreadcrumb = in.readString();
        countSold = in.readString();
        productMinimumOrder = in.readInt();
        headlineProductRatingAverage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(adRefKey);
        dest.writeString(adId);
        dest.writeString(name);
        dest.writeByte((byte) (wishlist ? 1 : 0));
        dest.writeParcelable(image, flags);
        dest.writeString(uri);
        dest.writeString(relativeUri);
        dest.writeString(priceFormat);
        dest.writeString(countTalkFormat);
        dest.writeString(countReviewFormat);
        dest.writeParcelable(category, flags);
        dest.writeByte((byte) (productPreorder ? 1 : 0));
        dest.writeByte((byte) (productWholesale ? 1 : 0));
        dest.writeString(freeReturn);
        dest.writeByte((byte) (productCashback ? 1 : 0));
        dest.writeString(productCashbackRate);
        dest.writeByte((byte) (productNewLabel ? 1 : 0));
        dest.writeString(productRatingFormat);
        dest.writeInt(productRating);
        dest.writeString(applinks);
        dest.writeTypedList(wholesalePrice);
        dest.writeTypedList(labels);
        dest.writeStringList(topLabels);
        dest.writeStringList(bottomLabels);
        dest.writeParcelable(imageProduct, flags);
        dest.writeParcelable(campaign, flags);
        dest.writeTypedList(labelGroupList);
        dest.writeParcelable(freeOngkir, flags);
        dest.writeString(categoryBreadcrumb);
        dest.writeString(countSold);
        dest.writeInt(productMinimumOrder);
        dest.writeString(headlineProductRatingAverage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public boolean isWishlist() {
        return wishlist;
    }

    public void setWishlist(boolean wishlist) {
        this.wishlist = wishlist;
    }

    public List<String> getTopLabels() {
        return topLabels;
    }

    public void setTopLabels(List<String> topLabels) {
        this.topLabels = topLabels;
    }

    public List<String> getBottomLabels() {
        return bottomLabels;
    }

    public void setBottomLabels(List<String> bottomLabels) {
        this.bottomLabels = bottomLabels;
    }

    public ImageProduct getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(ImageProduct imageProduct) {
        this.imageProduct = imageProduct;
    }

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

    public String getCategoryBreadcrumb() {
        return categoryBreadcrumb;
    }

    public void setCategoryBreadcrumb(String categoryBreadcrumb) {
        this.categoryBreadcrumb = categoryBreadcrumb;
    }

    public boolean isTopAds() {
        return topAds;
    }

    public void setTopAds(boolean topAds) {
        this.topAds = topAds;
    }

    public String getRecommendationType() {
        return recommendationType;
    }

    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
    }

    public ProductImage getImage() {
        return image;
    }

    public void setImage(ProductImage image) {
        this.image = image;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRelativeUri() {
        return relativeUri;
    }

    public void setRelativeUri(String relativeUri) {
        this.relativeUri = relativeUri;
    }

    public String getPriceFormat() {
        return priceFormat;
    }

    public void setPriceFormat(String priceFormat) {
        this.priceFormat = priceFormat;
    }

    public String getCountTalkFormat() {
        return countTalkFormat;
    }

    public void setCountTalkFormat(String countTalkFormat) {
        this.countTalkFormat = countTalkFormat;
    }

    public String getCountReviewFormat() {
        return countReviewFormat;
    }

    public boolean isProductNewLabel() {
        return productNewLabel;
    }

    public void setProductNewLabel(boolean productNewLabel) {
        this.productNewLabel = productNewLabel;
    }

    public void setCountReviewFormat(String countReviewFormat) {
        this.countReviewFormat = countReviewFormat;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isProductPreorder() {
        return productPreorder;
    }

    public void setProductPreorder(boolean productPreorder) {
        this.productPreorder = productPreorder;
    }

    public boolean isProductWholesale() {
        return productWholesale;
    }

    public void setProductWholesale(boolean productWholesale) {
        this.productWholesale = productWholesale;
    }

    public String getFreeReturn() {
        return freeReturn;
    }

    public void setFreeReturn(String freeReturn) {
        this.freeReturn = freeReturn;
    }

    public boolean isProductCashback() {
        return productCashback;
    }

    public void setProductCashback(boolean productCashback) {
        this.productCashback = productCashback;
    }

    public String getProductCashbackRate() {
        return productCashbackRate;
    }

    public void setProductCashbackRate(String productCashbackRate) {
        this.productCashbackRate = productCashbackRate;
    }

    public List<WholesalePrice> getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(List<WholesalePrice> wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public String getProductRatingFormat() {
        return productRatingFormat;
    }

    public void setProductRatingFormat(String productRatingFormat) {
        this.productRatingFormat = productRatingFormat;
    }

    public int getProductRating() {
        return productRating;
    }

    public void setProductRating(int productRating) {
        this.productRating = productRating;
    }

    public String getAdRefKey() {
        return adRefKey;
    }

    public void setAdRefKey(String adRefKey) {
        this.adRefKey = adRefKey;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setLabelGroupList(List<LabelGroup> labelGroupList) {
        this.labelGroupList = labelGroupList;
    }

    public List<LabelGroup> getLabelGroupList() {
        return this.labelGroupList;
    }

    public void setFreeOngkir(FreeOngkir freeOngkir) {
        this.freeOngkir = freeOngkir;
    }

    public FreeOngkir getFreeOngkir() {
        return this.freeOngkir;
    }

    public String getCountSold() {
        return this.countSold;
    }

    public void setCountSold(String countSold) {
        this.countSold = countSold;
    }

    public int getProductMinimumOrder() {
        return this.productMinimumOrder;
    }

    public void setProductMinimumOrder(int productMinimumOrder) {
        this.productMinimumOrder = productMinimumOrder;
    }

    public void setHeadlineProductRatingAverage(String headlineProductRatingAverage) {
        this.headlineProductRatingAverage = headlineProductRatingAverage;
    }

    public String getHeadlineProductRatingAverage() {
        return this.headlineProductRatingAverage;
    }
}
