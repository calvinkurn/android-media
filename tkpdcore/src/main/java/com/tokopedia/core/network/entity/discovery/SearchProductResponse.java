package com.tokopedia.core.network.entity.discovery;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hangnadi on 10/5/17.
 */

@SuppressWarnings("all")
public class SearchProductResponse {

    @SerializedName("header")
    private Header header;
    @SerializedName("data")
    private Data data;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Header {
        @SerializedName("total_data")
        private int totalData;
        @SerializedName("total_data_text")
        private String totalDataText;
        @SerializedName("process_time")
        private double processTime;
        @SerializedName("additional_params")
        private String additionalParams;

        public int getTotalData() {
            return totalData;
        }

        public void setTotalData(int totalData) {
            this.totalData = totalData;
        }

        public String getTotalDataText() {
            return totalDataText;
        }

        public void setTotalDataText(String totalDataText) {
            this.totalDataText = totalDataText;
        }

        public double getProcessTime() {
            return processTime;
        }

        public void setProcessTime(double processTime) {
            this.processTime = processTime;
        }

        public String getAdditionalParams() {
            return additionalParams;
        }

        public void setAdditionalParams(String additionalParams) {
            this.additionalParams = additionalParams;
        }
    }

    public static class Data {
        @SerializedName("source")
        private String source;
        @SerializedName("share_url")
        private String shareUrl;
        @SerializedName("q")
        private String query;
        @SerializedName("suggestion_text")
        private SuggestionText suggestionText;
        @SerializedName("redirection")
        private Redirection redirection;
        @SerializedName("suggestions")
        private Suggestions suggestions;
        @SerializedName("suggestions_instead")
        private SuggestionsInstead suggestionsInstead;
        @SerializedName("catalogs")
        private List<Catalogs> catalogs;
        @SerializedName("products")
        private List<Products> products;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public SuggestionText getSuggestionText() {
            return suggestionText;
        }

        public void setSuggestionText(SuggestionText suggestionText) {
            this.suggestionText = suggestionText;
        }

        public Redirection getRedirection() {
            return redirection;
        }

        public void setRedirection(Redirection redirection) {
            this.redirection = redirection;
        }

        public Suggestions getSuggestions() {
            return suggestions;
        }

        public void setSuggestions(Suggestions suggestions) {
            this.suggestions = suggestions;
        }

        public SuggestionsInstead getSuggestionsInstead() {
            return suggestionsInstead;
        }

        public void setSuggestionsInstead(SuggestionsInstead suggestionsInstead) {
            this.suggestionsInstead = suggestionsInstead;
        }

        public List<Catalogs> getCatalogs() {
            return catalogs;
        }

        public void setCatalogs(List<Catalogs> catalogs) {
            this.catalogs = catalogs;
        }

        public List<Products> getProducts() {
            return products;
        }

        public void setProducts(List<Products> products) {
            this.products = products;
        }

        public static class SuggestionText {
            @SerializedName("text")
            private String text;
            @SerializedName("query")
            private String query;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getQuery() {
                return query;
            }

            public void setQuery(String query) {
                this.query = query;
            }
        }

        public static class Redirection {
            @SerializedName("redirect_url")
            private String redirectUrl;
            @SerializedName("department_id")
            private String departmentId;

            public String getRedirectUrl() {
                return redirectUrl;
            }

            public void setRedirectUrl(String redirectUrl) {
                this.redirectUrl = redirectUrl;
            }

            public String getDepartmentId() {
                return departmentId;
            }

            public void setDepartmentId(String departmentId) {
                this.departmentId = departmentId;
            }
        }

        public static class Suggestions {
            @SerializedName("suggestion")
            private String suggestion;
            @SerializedName("total_data")
            private int totalData;

            public String getSuggestion() {
                return suggestion;
            }

            public void setSuggestion(String suggestion) {
                this.suggestion = suggestion;
            }

            public int getTotalData() {
                return totalData;
            }

            public void setTotalData(int totalData) {
                this.totalData = totalData;
            }
        }

        public static class SuggestionsInstead {
            @SerializedName("suggestion_instead")
            private String suggestionInstead;
            @SerializedName("current_keyword")
            private String currentKeyword;
            @SerializedName("total_data")
            private int totalData;

            public String getSuggestionInstead() {
                return suggestionInstead;
            }

            public void setSuggestionInstead(String suggestionInstead) {
                this.suggestionInstead = suggestionInstead;
            }

            public String getCurrentKeyword() {
                return currentKeyword;
            }

            public void setCurrentKeyword(String currentKeyword) {
                this.currentKeyword = currentKeyword;
            }

            public int getTotalData() {
                return totalData;
            }

            public void setTotalData(int totalData) {
                this.totalData = totalData;
            }
        }

        public static class Products {
            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;
            @SerializedName("url")
            private String url;
            @SerializedName("image_url")
            private String imageUrl;
            @SerializedName("image_url_700")
            private String imageUrl700;
            @SerializedName("price")
            private String price;
            @SerializedName("shop")
            private Shop shop;
            @SerializedName("condition")
            private int condition;
            @SerializedName("department_id")
            private String departmentId;
            @SerializedName("rating")
            private String rating;
            @SerializedName("count_review")
            private String countReview;
            @SerializedName("original_price")
            private String originalPrice;
            @SerializedName("discount_percentage")
            private int discountPercentage;
            @SerializedName("wholesale_price")
            private List<WholesalePrice> wholesalePrice;
            @SerializedName("labels")
            private List<Labels> labels;
            @SerializedName("badges")
            private List<Badges> badges;
            @SerializedName("is_featured")
            private int isFeatured;

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

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public Shop getShop() {
                return shop;
            }

            public void setShop(Shop shop) {
                this.shop = shop;
            }

            public int getCondition() {
                return condition;
            }

            public void setCondition(int condition) {
                this.condition = condition;
            }

            public String getDepartmentId() {
                return departmentId;
            }

            public void setDepartmentId(String departmentId) {
                this.departmentId = departmentId;
            }

            public String getRating() {
                return rating;
            }

            public void setRating(String rating) {
                this.rating = rating;
            }

            public String getCountReview() {
                return countReview;
            }

            public void setCountReview(String countReview) {
                this.countReview = countReview;
            }

            public String getOriginalPrice() {
                return originalPrice;
            }

            public void setOriginalPrice(String originalPrice) {
                this.originalPrice = originalPrice;
            }

            public int getDiscountPercentage() {
                return discountPercentage;
            }

            public void setDiscountPercentage(int discountPercentage) {
                this.discountPercentage = discountPercentage;
            }

            public List<WholesalePrice> getWholesalePrice() {
                return wholesalePrice;
            }

            public void setWholesalePrice(List<WholesalePrice> wholesalePrice) {
                this.wholesalePrice = wholesalePrice;
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

            public int getIsFeatured() {
                return isFeatured;
            }

            public void setIsFeatured(int isFeatured) {
                this.isFeatured = isFeatured;
            }

            public static class Shop {
                @SerializedName("id")
                private String id;
                @SerializedName("name")
                private String name;
                @SerializedName("url")
                private String url;
                @SerializedName("is_gold")
                private boolean isGold;
                @SerializedName("location")
                private String location;
                @SerializedName("city")
                private String city;
                @SerializedName("reputation")
                private String reputation;
                @SerializedName("clover")
                private String clover;

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

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public boolean isIsGold() {
                    return isGold;
                }

                public void setIsGold(boolean isGold) {
                    this.isGold = isGold;
                }

                public String getLocation() {
                    return location;
                }

                public void setLocation(String location) {
                    this.location = location;
                }

                public String getCity() {
                    return city;
                }

                public void setCity(String city) {
                    this.city = city;
                }

                public String getReputation() {
                    return reputation;
                }

                public void setReputation(String reputation) {
                    this.reputation = reputation;
                }

                public String getClover() {
                    return clover;
                }

                public void setClover(String clover) {
                    this.clover = clover;
                }
            }

            public static class WholesalePrice {
                @SerializedName("quantity_min")
                private int quantityMin;
                @SerializedName("quantity_max")
                private int quantityMax;
                @SerializedName("price")
                private int price;

                public int getQuantityMin() {
                    return quantityMin;
                }

                public void setQuantityMin(int quantityMin) {
                    this.quantityMin = quantityMin;
                }

                public int getQuantityMax() {
                    return quantityMax;
                }

                public void setQuantityMax(int quantityMax) {
                    this.quantityMax = quantityMax;
                }

                public int getPrice() {
                    return price;
                }

                public void setPrice(int price) {
                    this.price = price;
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
                @SerializedName("image_url")
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

        public static class Catalogs {

            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;
            @SerializedName("price")
            private String price;
            @SerializedName("price_min")
            private String priceMin;
            @SerializedName("price_max")
            private String priceMax;
            @SerializedName("count_product")
            private int countProduct;
            @SerializedName("description")
            private String description;
            @SerializedName("image_url")
            private String imageUrl;
            @SerializedName("url")
            private String url;
            @SerializedName("department_id")
            private String departmentId;

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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getPriceMin() {
                return priceMin;
            }

            public void setPriceMin(String priceMin) {
                this.priceMin = priceMin;
            }

            public String getPriceMax() {
                return priceMax;
            }

            public void setPriceMax(String priceMax) {
                this.priceMax = priceMax;
            }

            public int getCountProduct() {
                return countProduct;
            }

            public void setCountProduct(int countProduct) {
                this.countProduct = countProduct;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getDepartmentId() {
                return departmentId;
            }

            public void setDepartmentId(String departmentId) {
                this.departmentId = departmentId;
            }
        }
    }
}
