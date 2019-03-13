package com.tokopedia.navigation.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author errysuprayogi on 13,March,2019
 */
public class RecomendationEntity {

    @SerializedName("data")
    private DataRecomendation data;

    public DataRecomendation getData() {
        return data;
    }

    public void setData(DataRecomendation data) {
        this.data = data;
    }

    public static class DataRecomendation {

        @SerializedName("productRecommendationWidget")
        private ProductRecommendationWidget productRecommendationWidget;

        public ProductRecommendationWidget getProductRecommendationWidget() {
            return productRecommendationWidget;
        }

        public void setProductRecommendationWidget(ProductRecommendationWidget productRecommendationWidget) {
            this.productRecommendationWidget = productRecommendationWidget;
        }

        public static class ProductRecommendationWidget {
            @SerializedName("data")
            private List<Data> data;

            public List<Data> getData() {
                return data;
            }

            public void setData(List<Data> data) {
                this.data = data;
            }

            public static class Data {
                @SerializedName("tID")
                private String tID;
                @SerializedName("source")
                private String source;
                @SerializedName("title")
                private String title;
                @SerializedName("foreignTitle")
                private String foreignTitle;
                @SerializedName("widgetUrl")
                private String widgetUrl;
                @SerializedName("recommendation")
                private List<Recommendation> recommendation;

                public String getTID() {
                    return tID;
                }

                public void setTID(String tID) {
                    this.tID = tID;
                }

                public String getSource() {
                    return source;
                }

                public void setSource(String source) {
                    this.source = source;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getForeignTitle() {
                    return foreignTitle;
                }

                public void setForeignTitle(String foreignTitle) {
                    this.foreignTitle = foreignTitle;
                }

                public String getWidgetUrl() {
                    return widgetUrl;
                }

                public void setWidgetUrl(String widgetUrl) {
                    this.widgetUrl = widgetUrl;
                }

                public List<Recommendation> getRecommendation() {
                    return recommendation;
                }

                public void setRecommendation(List<Recommendation> recommendation) {
                    this.recommendation = recommendation;
                }

                public static class Recommendation {

                    @SerializedName("id")
                    private int id;
                    @SerializedName("name")
                    private String name;
                    @SerializedName("categoryBreadcrumbs")
                    private String categoryBreadcrumbs;
                    @SerializedName("url")
                    private String url;
                    @SerializedName("appUrl")
                    private String appUrl;
                    @SerializedName("clickUrl")
                    private String clickUrl;
                    @SerializedName("wishlistUrl")
                    private String wishlistUrl;
                    @SerializedName("trackerImageUrl")
                    private String trackerImageUrl;
                    @SerializedName("imageUrl")
                    private String imageUrl;
                    @SerializedName("price")
                    private String price;
                    @SerializedName("priceInt")
                    private int priceInt;
                    @SerializedName("shop")
                    private Shop shop;
                    @SerializedName("departmentId")
                    private int departmentId;
                    @SerializedName("rating")
                    private int rating;
                    @SerializedName("countReview")
                    private int countReview;
                    @SerializedName("recommendationType")
                    private String recommendationType;
                    @SerializedName("stock")
                    private int stock;
                    @SerializedName("isTopads")
                    private boolean isTopads;
                    @SerializedName("labels")
                    private List<Labels> labels;
                    @SerializedName("badges")
                    private List<Badges> badges;
                    @SerializedName("wholesalePrice")
                    private List<?> wholesalePrice;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getCategoryBreadcrumbs() {
                        return categoryBreadcrumbs;
                    }

                    public void setCategoryBreadcrumbs(String categoryBreadcrumbs) {
                        this.categoryBreadcrumbs = categoryBreadcrumbs;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    public String getAppUrl() {
                        return appUrl;
                    }

                    public void setAppUrl(String appUrl) {
                        this.appUrl = appUrl;
                    }

                    public String getClickUrl() {
                        return clickUrl;
                    }

                    public void setClickUrl(String clickUrl) {
                        this.clickUrl = clickUrl;
                    }

                    public String getWishlistUrl() {
                        return wishlistUrl;
                    }

                    public void setWishlistUrl(String wishlistUrl) {
                        this.wishlistUrl = wishlistUrl;
                    }

                    public String getTrackerImageUrl() {
                        return trackerImageUrl;
                    }

                    public void setTrackerImageUrl(String trackerImageUrl) {
                        this.trackerImageUrl = trackerImageUrl;
                    }

                    public String getImageUrl() {
                        return imageUrl;
                    }

                    public void setImageUrl(String imageUrl) {
                        this.imageUrl = imageUrl;
                    }

                    public String getPrice() {
                        return price;
                    }

                    public void setPrice(String price) {
                        this.price = price;
                    }

                    public int getPriceInt() {
                        return priceInt;
                    }

                    public void setPriceInt(int priceInt) {
                        this.priceInt = priceInt;
                    }

                    public Shop getShop() {
                        return shop;
                    }

                    public void setShop(Shop shop) {
                        this.shop = shop;
                    }

                    public int getDepartmentId() {
                        return departmentId;
                    }

                    public void setDepartmentId(int departmentId) {
                        this.departmentId = departmentId;
                    }

                    public int getRating() {
                        return rating;
                    }

                    public void setRating(int rating) {
                        this.rating = rating;
                    }

                    public int getCountReview() {
                        return countReview;
                    }

                    public void setCountReview(int countReview) {
                        this.countReview = countReview;
                    }

                    public String getRecommendationType() {
                        return recommendationType;
                    }

                    public void setRecommendationType(String recommendationType) {
                        this.recommendationType = recommendationType;
                    }

                    public int getStock() {
                        return stock;
                    }

                    public void setStock(int stock) {
                        this.stock = stock;
                    }

                    public boolean isIsTopads() {
                        return isTopads;
                    }

                    public void setIsTopads(boolean isTopads) {
                        this.isTopads = isTopads;
                    }

                    public List<Labels> getLabels() {
                        return labels;
                    }

                    public void setLabels(List<Labels> labels) {
                        this.labels = labels;
                    }

                    public List<Badges> getBadges() {
                        return badges;
                    }

                    public void setBadges(List<Badges> badges) {
                        this.badges = badges;
                    }

                    public List<?> getWholesalePrice() {
                        return wholesalePrice;
                    }

                    public void setWholesalePrice(List<?> wholesalePrice) {
                        this.wholesalePrice = wholesalePrice;
                    }

                    public static class Shop {

                        @SerializedName("id")
                        private int id;
                        @SerializedName("name")
                        private String name;

                        public int getId() {
                            return id;
                        }

                        public void setId(int id) {
                            this.id = id;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }
                    }

                    public static class Labels {

                        @SerializedName("title")
                        private String title;
                        @SerializedName("color")
                        private String color;

                        public String getTitle() {
                            return title;
                        }

                        public void setTitle(String title) {
                            this.title = title;
                        }

                        public String getColor() {
                            return color;
                        }

                        public void setColor(String color) {
                            this.color = color;
                        }
                    }

                    public static class Badges {

                        @SerializedName("title")
                        private String title;
                        @SerializedName("imageUrl")
                        private String imageUrl;

                        public String getTitle() {
                            return title;
                        }

                        public void setTitle(String title) {
                            this.title = title;
                        }

                        public String getImageUrl() {
                            return imageUrl;
                        }

                        public void setImageUrl(String imageUrl) {
                            this.imageUrl = imageUrl;
                        }
                    }
                }
            }
        }
    }
}
