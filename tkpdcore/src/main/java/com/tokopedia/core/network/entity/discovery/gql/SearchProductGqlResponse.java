package com.tokopedia.core.network.entity.discovery.gql;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchProductGqlResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {

        @SerializedName("searchProduct")
        @Expose
        private SearchProduct searchProduct;

        public SearchProduct getSearchProduct() {
            return searchProduct;
        }
    }

    public static class SearchProduct {

        @SerializedName("query")
        @Expose
        private String query;
        @SerializedName("source")
        @Expose
        private String source;
        @SerializedName("shareUrl")
        @Expose
        private String shareUrl;
        @SerializedName("isFilter")
        @Expose
        private boolean isFilter;
        @SerializedName("count")
        @Expose
        private int count;
        @SerializedName("redirection")
        @Expose
        private Redirection redirection;
        @SerializedName("suggestion")
        @Expose
        private Suggestion suggestion;
        @SerializedName("products")
        @Expose
        private List<Product> products = null;
        @SerializedName("catalogs")
        @Expose
        private List<Catalog> catalogs = null;

        public String getQuery() {
            return query;
        }

        public String getSource() {
            return source;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public boolean isFilter() {
            return isFilter;
        }

        public int getCount() {
            return count;
        }

        public Redirection getRedirection() {
            return redirection;
        }

        public Suggestion getSuggestion() {
            return suggestion;
        }

        public List<Product> getProducts() {
            return products;
        }

        public List<Catalog> getCatalogs() {
            return catalogs;
        }
    }

    public static class Catalog {

        @SerializedName("id")
        @Expose
        private long id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("rawPrice")
        @Expose
        private long rawPrice;
        @SerializedName("minPrice")
        @Expose
        private String minPrice;
        @SerializedName("maxPrice")
        @Expose
        private String maxPrice;
        @SerializedName("rawMinPrice")
        @Expose
        private long rawMinPrice;
        @SerializedName("rawMaxPrice")
        @Expose
        private long rawMaxPrice;
        @SerializedName("count")
        @Expose
        private long count;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("imageUrl")
        @Expose
        private String imageUrl;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("departmentId")
        @Expose
        private long departmentId;

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public long getRawPrice() {
            return rawPrice;
        }

        public String getMinPrice() {
            return minPrice;
        }

        public String getMaxPrice() {
            return maxPrice;
        }

        public long getRawMinPrice() {
            return rawMinPrice;
        }

        public long getRawMaxPrice() {
            return rawMaxPrice;
        }

        public long getCount() {
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

        public long getDepartmentId() {
            return departmentId;
        }
    }

    public static class Suggestion {

        @SerializedName("currentKeyword")
        @Expose
        private String currentKeyword;
        @SerializedName("suggestion")
        @Expose
        private String suggestion;
        @SerializedName("suggestionCount")
        @Expose
        private long suggestionCount;
        @SerializedName("instead")
        @Expose
        private String instead;
        @SerializedName("insteadCount")
        @Expose
        private long insteadCount;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("query")
        @Expose
        private String query;

        public String getCurrentKeyword() {
            return currentKeyword;
        }

        public String getSuggestion() {
            return suggestion;
        }

        public long getSuggestionCount() {
            return suggestionCount;
        }

        public String getInstead() {
            return instead;
        }

        public long getInsteadCount() {
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
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("childs")
        @Expose
        private Object childs;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("imageUrl")
        @Expose
        private String imageUrl;
        @SerializedName("imageUrlLarge")
        @Expose
        private String imageUrlLarge;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("rating")
        @Expose
        private int rating;
        @SerializedName("countReview")
        @Expose
        private int countReview;
        @SerializedName("preorder")
        @Expose
        private boolean preorder;
        @SerializedName("cashback")
        @Expose
        private String cashback;
        @SerializedName("wishlist")
        @Expose
        private boolean wishlist;
        @SerializedName("gaKey")
        @Expose
        private String gaKey;
        @SerializedName("catId")
        @Expose
        private long catId;
        @SerializedName("shop")
        @Expose
        private Shop shop;

        public String getId() {
            return id;
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

        public String getImageUrlLarge() {
            return imageUrlLarge;
        }

        public String getPrice() {
            return price;
        }

        public int getRating() {
            return rating;
        }

        public int getCountReview() {
            return countReview;
        }

        public boolean isPreorder() {
            return preorder;
        }

        public String getCashback() {
            return cashback;
        }

        public boolean isWishlist() {
            return wishlist;
        }

        public String getGaKey() {
            return gaKey;
        }

        public long getCatId() {
            return catId;
        }

        public Shop getShop() {
            return shop;
        }
    }

    public static class Redirection {

        @SerializedName("redirectUrl")
        @Expose
        private String redirectUrl;
        @SerializedName("departmentId")
        @Expose
        private long departmentId;

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public long getDepartmentId() {
            return departmentId;
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
        @SerializedName("goldmerchant")
        @Expose
        private boolean goldmerchant;
        @SerializedName("official")
        @Expose
        private boolean official;

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
            return official;
        }
    }
}
