package com.tokopedia.tkpd.testgql.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TestGqlModel {
    @SerializedName("searchProduct")
    @Expose
    private SearchProduct searchProduct = new SearchProduct();

    @SerializedName("global_search_navigation")
    @Expose
    private GlobalNavModel globalNavModel = new GlobalNavModel();

    @SerializedName("searchInspirationCarousel")
    @Expose
    private SearchInspirationCarousel searchInspirationCarousel = new SearchInspirationCarousel();

    @SerializedName("searchInspirationWidget")
    @Expose
    private SearchInspirationCard searchInspirationCard = new SearchInspirationCard();

    public GlobalNavModel getGlobalNavModel() {
        return globalNavModel;
    }

    public SearchInspirationCarousel getSearchInspirationCarousel() {
        return searchInspirationCarousel;
    }

    public SearchInspirationCard getSearchInspirationCard() {
        return searchInspirationCard;
    }

    public static class SearchProduct {

        @SerializedName("query")
        @Expose
        private String query;
        @SerializedName("source")
        @Expose
        private String source;
        @SerializedName("errorMessage")
        @Expose
        private String errorMessage;
        @SerializedName("lite_url")
        @Expose
        private String liteUrl;
        @SerializedName("isFilter")
        @Expose
        private boolean isFilter;
        @SerializedName("isQuerySafe")
        @Expose
        private boolean isQuerySafe;
        @SerializedName("count")
        @Expose
        private int count;
        @SerializedName("response_code")
        @Expose
        private String responseCode;
        @SerializedName("keyword_process")
        @Expose
        private String keywordProcess;
        @SerializedName("count_text")
        @Expose
        private String countText;
        @SerializedName("additional_params")
        @Expose
        private String additionalParams;
        @SerializedName("autocomplete_applink")
        @Expose
        private String autocompleteApplink;
        @SerializedName("default_view")
        @Expose
        private int defaultView = 0;
        @SerializedName("redirection")
        @Expose
        private Redirection redirection = new Redirection();
        @SerializedName("suggestion")
        @Expose
        private Suggestion suggestion = new Suggestion();
        @SerializedName("ticker")
        @Expose
        private Ticker ticker = new Ticker();
        @SerializedName("related")
        @Expose
        private Related related = new Related();
        @SerializedName("products")
        @Expose
        private List<Product> products = new ArrayList<>();

        public String getQuery() {
            return query;
        }

        public String getSource() {
            return source;
        }

        public boolean isFilter() {
            return isFilter;
        }

        public String getErrorMessage() {
            return this.errorMessage;
        }

        public String getLiteUrl() {
            return this.liteUrl;
        }

        public boolean isQuerySafe() {
            return isQuerySafe;
        }

        public int getCount() {
            return count;
        }

        public String getResponseCode() {
            return responseCode;
        }

        public String getKeywordProcess() {
            return keywordProcess;
        }

        public String getCountText() {
            return countText;
        }

        public String getAdditionalParams() {
            return additionalParams;
        }

        public String getAutocompleteApplink() {
            return autocompleteApplink;
        }

        public int getDefaultView() {
            return defaultView;
        }

        public Redirection getRedirection() {
            return this.redirection;
        }

        public Ticker getTicker() {
            return ticker;
        }

        public Suggestion getSuggestion() {
            return suggestion;
        }

        public List<Product> getProducts() {
            return products;
        }

        public Related getRelated() {
            return related;
        }
    }

    public static class Redirection {

        @SerializedName("redirect_applink")
        @Expose
        private String redirectApplink;

        public String getRedirectApplink() {
            return this.redirectApplink;
        }
    }

    public static class Related {
        @SerializedName("related_keyword")
        @Expose
        private String relatedKeyword = "";

        @SerializedName("other_related")
        @Expose
        private List<OtherRelated> otherRelated = new ArrayList<>();

        public String getRelatedKeyword() {
            return relatedKeyword;
        }

        public List<OtherRelated> getOtherRelated() {
            return otherRelated;
        }
    }

    public static class OtherRelated {
        @SerializedName("keyword")
        @Expose
        private String keyword;

        @SerializedName("url")
        @Expose
        private String url;

        @SerializedName("applink")
        @Expose
        private String applink;

        @SerializedName("product")
        @Expose
        private List<OtherRelatedProduct> otherRelatedProductList;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getApplink() {
            return applink;
        }

        public List<OtherRelatedProduct> getOtherRelatedProductList() {
            return otherRelatedProductList;
        }
    }

    public static class OtherRelatedProduct {

        @SerializedName("id")
        @Expose
        private String id = "";

        @SerializedName("name")
        @Expose
        private String name = "";

        @SerializedName("price")
        @Expose
        private int price = 0;

        @SerializedName("image_url")
        @Expose
        private String imageUrl = "";

        @SerializedName("rating")
        @Expose
        private int rating;

        @SerializedName("count_review")
        @Expose
        private int countReview;

        @SerializedName("url")
        @Expose
        private String url;

        @SerializedName("applink")
        @Expose
        private String applink;

        @SerializedName("price_str")
        @Expose
        private String priceString;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public int getRating() {
            return rating;
        }

        public int getCountReview() {
            return countReview;
        }

        public String getUrl() {
            return url;
        }

        public String getApplink() {
            return applink;
        }

        public String getPriceString() {
            return priceString;
        }
    }

    public static class Catalog {

        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("price_raw")
        @Expose
        private String rawPrice;
        @SerializedName("price_min")
        @Expose
        private String minPrice;
        @SerializedName("price_max")
        @Expose
        private String maxPrice;
        @SerializedName("price_min_raw")
        @Expose
        private String rawMinPrice;
        @SerializedName("price_max_raw")
        @Expose
        private String rawMaxPrice;
        @SerializedName("count_product")
        @Expose
        private int count;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("department_id")
        @Expose
        private int departmentId;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public String getRawPrice() {
            return rawPrice;
        }

        public String getMinPrice() {
            return minPrice;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public String getRawMinPrice() {
            return rawMinPrice;
        }

        public String getRawMaxPrice() {
            return rawMaxPrice;
        }

        public int getCount() {
            return count;
        }

        public String getDescription() {
            return description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getUrl() {
            return url;
        }

        public int getDepartmentId() {
            return departmentId;
        }
    }

    public static class Ticker {

        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("query")
        @Expose
        private String query;
        @SerializedName("type_id")
        @Expose
        private int typeId;

        public String getText() {
            return text;
        }

        public String getQuery() {
            return query;
        }

        public int getTypeId() { return typeId; }
    }

    public static class Suggestion {

        @SerializedName("currentKeyword")
        @Expose
        private String currentKeyword = "";
        @SerializedName("suggestion")
        @Expose
        private String suggestion = "";
        @SerializedName("suggestionCount")
        @Expose
        private int suggestionCount = 0;
        @SerializedName("instead")
        @Expose
        private String instead = "";
        @SerializedName("insteadCount")
        @Expose
        private int insteadCount = 0;
        @SerializedName("text")
        @Expose
        private String text = "";
        @SerializedName("query")
        @Expose
        private String query = "";

        public String getCurrentKeyword() {
            return currentKeyword;
        }

        public String getSuggestion() {
            return suggestion;
        }

        public int getSuggestionCount() {
            return suggestionCount;
        }

        public String getInstead() {
            return instead;
        }

        public int getInsteadCount() {
            return insteadCount;
        }

        public String getText() {
            return text;
        }

        public String getQuery() {
            return query;
        }
    }

    public static class Product {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("warehouse_id_default")
        @Expose
        private String warehouseId = "";
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("childs")
        @Expose
        private Object childs = new Object();
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;
        @SerializedName("image_url_300")
        @Expose
        private String imageUrl300;
        @SerializedName("image_url_700")
        @Expose
        private String imageUrl700;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("price_range")
        @Expose
        private String priceRange;
        @SerializedName("top_label")
        @Expose
        private List<String> topLabel;
        @SerializedName("bottom_label")
        @Expose
        private List<String> bottomLabel;
        @SerializedName("whole_sale_price")
        @Expose
        private List<WholeSalePrice> wholeSalePrice = null;
        @SerializedName("courier_count")
        @Expose
        private int courierCount;
        @SerializedName("condition")
        @Expose
        private int condition;
        @SerializedName("category_id")
        @Expose
        private int categoryId;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("category_breadcrumb")
        @Expose
        private String categoryBreadcrumb;
        @SerializedName("department_id")
        @Expose
        private int departmentId;
        @SerializedName("department_name")
        @Expose
        private String departmentName;
        @SerializedName("labels")
        @Expose
        private List<Label> labels = null;
        @SerializedName("label_groups")
        @Expose
        private List<LabelGroup> labelGroups = new ArrayList<>();
        @SerializedName("badges")
        @Expose
        private List<Badge> badges = null;
        @SerializedName("is_featured")
        @Expose
        private int isFeatured;
        @SerializedName("rating_average")
        @Expose
        private String ratingAverage = "";
        @SerializedName("rating")
        @Expose
        private int rating;
        @SerializedName("count_review")
        @Expose
        private int countReview;
        @SerializedName("original_price")
        @Expose
        private String originalPrice;
        @SerializedName("discount_expired_time")
        @Expose
        private String discountExpiredTime;
        @SerializedName("discount_start_time")
        @Expose
        private String discountStartTime;
        @SerializedName("discount_percentage")
        @Expose
        private int discountPercentage;
        @SerializedName("sku")
        @Expose
        private String sku;
        @SerializedName("stock")
        @Expose
        private int stock;
        @SerializedName("ga_key")
        @Expose
        private String gaKey;
        @SerializedName("is_preorder")
        @Expose
        private boolean preorder;
        @SerializedName("wishlist")
        @Expose
        private boolean wishlist;
        @SerializedName("shop")
        @Expose
        private Shop shop = new Shop();
        @SerializedName("free_ongkir")
        @Expose
        private FreeOngkir freeOngkir = new FreeOngkir();
        @SerializedName("booster_list")
        @Expose
        private String boosterList = "";

        public String getId() {
            return id;
        }

        public String getWarehouseId() {
            return warehouseId;
        }

        public String getName() {
            return name;
        }

        public Object getChilds() {
            return childs;
        }

        public String getUrl() {
            return url;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getImageUrl300() {
            return imageUrl300;
        }

        public String getImageUrl700() {
            return imageUrl700;
        }

        public String getPrice() {
            return price;
        }

        public List<WholeSalePrice> getWholeSalePrice() {
            return wholeSalePrice;
        }

        public int getCourierCount() {
            return courierCount;
        }

        public int getCondition() {
            return condition;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public String getCategoryBreadcrumb() {
            return categoryBreadcrumb;
        }

        public int getDepartmentId() {
            return departmentId;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public List<Label> getLabels() {
            return labels;
        }

        public List<LabelGroup> getLabelGroupList() {
            return labelGroups;
        }

        public List<Badge> getBadges() {
            return badges;
        }

        public int getIsFeatured() {
            return isFeatured;
        }

        public String getRatingAverage() {
            return ratingAverage;
        }

        public int getRating() {
            return rating;
        }

        public int getCountReview() {
            return countReview;
        }

        public String getOriginalPrice() {
            return originalPrice;
        }

        public String getDiscountExpiredTime() {
            return discountExpiredTime;
        }

        public String getDiscountStartTime() {
            return discountStartTime;
        }

        public int getDiscountPercentage() {
            return discountPercentage;
        }

        public String getSku() {
            return sku;
        }

        public int getStock() {
            return stock;
        }

        public String getGaKey() {
            return gaKey;
        }

        public boolean isPreorder() {
            return preorder;
        }

        public boolean isWishlist() {
            return wishlist;
        }

        public String getPriceRange() {
            return priceRange;
        }

        public List<String> getTopLabel() {
            return topLabel;
        }

        public List<String> getBottomLabel() {
            return bottomLabel;
        }

        public Shop getShop() {
            return shop;
        }

        public FreeOngkir getFreeOngkir() {
            return freeOngkir;
        }

        public String getBoosterList() {
            return boosterList;
        }
    }

    public static class LabelGroup {
        @SerializedName("position")
        @Expose
        private String position;

        public String getPosition() {
            return position;
        }

        @SerializedName("type")
        @Expose
        private String type;

        public String getType() {
            return type;
        }

        @SerializedName("title")
        @Expose
        private String title;

        public String getTitle() {
            return title;
        }
    }

    public static class WholeSalePrice {
        @SerializedName("quantity_min")
        @Expose
        private int quantityMin;
        @SerializedName("quantity_max")
        @Expose
        private int quantityMax;
        @SerializedName("price")
        @Expose
        private String price;

        public int getQuantityMin() {
            return quantityMin;
        }

        public int getQuantityMax() {
            return quantityMax;
        }

        public String getPrice() {
            return price;
        }
    }

    public static class Label {
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("color")
        @Expose
        private String color;

        public String getTitle() {
            return title;
        }

        public String getColor() {
            return color;
        }
    }

    public static class Badge {
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;
        @SerializedName("show")
        @Expose
        private boolean isShown;

        public String getTitle() {
            return title;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public boolean isShown() {
            return isShown;
        }
    }

    public static class Shop {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("reputation")
        @Expose
        private String reputation;
        @SerializedName("clover")
        @Expose
        private String clover;
        @SerializedName("is_gold_shop")
        @Expose
        private boolean goldmerchant;
        @SerializedName("is_official")
        @Expose
        private boolean isOfficial;
        @SerializedName("is_power_badge")
        @Expose
        private boolean isPowerBadge;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getLocation() {
            return location;
        }

        public String getCity() {
            return city;
        }

        public String getReputation() {
            return reputation;
        }

        public String getClover() {
            return clover;
        }

        public boolean isGoldmerchant() {
            return goldmerchant;
        }

        public boolean isOfficial() {
            return isOfficial;
        }

        public boolean isPowerBadge() {
            return isPowerBadge;
        }
    }

    public static class FreeOngkir {
        @SerializedName("is_active")
        @Expose
        private boolean isActive;

        @SerializedName("img_url")
        @Expose
        private String imageUrl;

        public boolean isActive() {
            return isActive;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    public static class GlobalNavModel {
        @SerializedName("data")
        private GlobalNavData data = new GlobalNavData();

        public GlobalNavData getData() {
            return data;
        }
    }

    public static class GlobalNavData {
        @SerializedName("source")
        private String source = "";

        @SerializedName("title")
        private String title = "";

        @SerializedName("keyword")
        private String keyword = "";

        @SerializedName("nav_template")
        private String navTemplate = "";

        @SerializedName("background")
        private String background = "";

        @SerializedName("see_all_applink")
        private String seeAllApplink = "";

        @SerializedName("see_all_url")
        private String seeAllUrl = "";

        @SerializedName("show_topads")
        private boolean isShowTopAds = false;

        @SerializedName("list")
        private List<GlobalNavItem> globalNavItems = new ArrayList<>();

        public String getSource() {
            return source;
        }

        public String getTitle() {
            return title;
        }

        public String getKeyword() {
            return keyword;
        }

        public String getNavTemplate() {
            return navTemplate;
        }

        public String getBackground() {
            return background;
        }

        public String getSeeAllApplink() {
            return seeAllApplink;
        }

        public String getSeeAllUrl() {
            return seeAllUrl;
        }

        public boolean getIsShowTopAds() {
            return isShowTopAds;
        }

        public List<GlobalNavItem> getGlobalNavItems() {
            return globalNavItems;
        }
    }

    public static class GlobalNavItem {
        @SerializedName("category_name")
        private String categoryName = "";

        @SerializedName("name")
        private String name = "";

        @SerializedName("info")
        private String info = "";

        @SerializedName("image_url")
        private String imageUrl = "";

        @SerializedName("applink")
        private String applink = "";

        @SerializedName("url")
        private String url = "";

        @SerializedName("subtitle")
        private String subtitle = "";

        @SerializedName("strikethrough")
        private String strikethrough = "";

        @SerializedName("background_url")
        private String backgroundUrl = "";

        @SerializedName("logo_url")
        private String logoUrl = "";

        public String getCategoryName() {
            return categoryName;
        }

        public String getName() {
            return name;
        }

        public String getInfo() {
            return info;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getApplink() {
            return applink;
        }

        public String getUrl() {
            return url;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public String getStrikethrough() {
            return strikethrough;
        }

        public String getBackgroundUrl() {
            return backgroundUrl;
        }

        public String getLogoUrl() {
            return logoUrl;
        }
    }

    public static class SearchInspirationCarousel {
        @SerializedName("data")
        private List<InspirationCarouselData> data = new ArrayList<>();

        public List<InspirationCarouselData> getData() {
            return data;
        }
    }

    public static class InspirationCarouselData {
        @SerializedName("title")
        private String title = "";

        @SerializedName("type")
        private String type = "";

        @SerializedName("position")
        private int position = 0;

        @SerializedName("options")
        private List<InspirationCarouselOption> inspirationCarouselOptions = new ArrayList<>();

        public String getTitle() {
            return title;
        }

        public String getType() {
            return type;
        }

        public int getPosition() {
            return position;
        }

        public List<InspirationCarouselOption> getInspirationCarouselOptions() {
            return inspirationCarouselOptions;
        }
    }

    public static class InspirationCarouselOption {
        @SerializedName("title")
        private String title = "";

        @SerializedName("url")
        private String url = "";

        @SerializedName("applink")
        private String applink = "";

        @SerializedName("product")
        private List<InspirationCarouselProduct> inspirationCarouselProducts = new ArrayList<>();

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public String getApplink() {
            return applink;
        }

        public List<InspirationCarouselProduct> getInspirationCarouselProducts() {
            return inspirationCarouselProducts;
        }
    }

    public static class InspirationCarouselProduct {
        @SerializedName("id")
        private String id = "";

        @SerializedName("name")
        private String name = "";

        @SerializedName("price")
        private int price = 0;

        @SerializedName("image_url")
        private String imgUrl = "";

        @SerializedName("price_str")
        private String priceStr = "";

        @SerializedName("title")
        private String title = "";

        @SerializedName("rating")
        private int rating = 0;

        @SerializedName("count_review")
        private int countReview = 0;

        @SerializedName("url")
        private String url = "";

        @SerializedName("applink")
        private String applink = "";

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public String getTitle() {
            return title;
        }

        public int getRating() {
            return rating;
        }

        public int countReview() {
            return countReview;
        }

        public String getUrl() {
            return url;
        }

        public String getApplink() {
            return applink;
        }

        public String getPriceStr() {
            return priceStr;
        }
    }

    public static class SearchInspirationCard {
        @SerializedName("data")
        private List<InspirationCardData> data = new ArrayList<>();

        public List<InspirationCardData> getData() {
            return data;
        }
    }

    public static class InspirationCardData {
        @SerializedName("title")
        private String title = "";

        @SerializedName("type")
        private String type = "";

        @SerializedName("position")
        private int position = 0;

        @SerializedName("options")
        private List<InspirationCardOption> inspiratioWidgetOptions = new ArrayList<>();

        public String getTitle() {
            return title;
        }

        public String getType() {
            return type;
        }

        public int getPosition() {
            return position;
        }

        public List<InspirationCardOption> getInspiratioWidgetOptions() {
            return inspiratioWidgetOptions;
        }
    }

    public static class InspirationCardOption {
        @SerializedName("text")
        private String text = "";

        @SerializedName("img")
        private String img = "";

        @SerializedName("url")
        private String url = "";

        @SerializedName("color")
        private String color = "";

        @SerializedName("applink")
        private String applink = "";

        public String getText() {
            return text;
        }

        public String getImg() {
            return img;
        }

        public String getUrl() {
            return url;
        }

        public String getColor() {
            return color;
        }

        public String getApplink() {
            return applink;
        }
    }
}
