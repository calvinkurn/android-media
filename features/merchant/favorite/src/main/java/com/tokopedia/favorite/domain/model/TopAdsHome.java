package com.tokopedia.favorite.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopAdsHome {

    @SerializedName("data")
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("id")
        private String id;
        @SerializedName("ad_ref_key")
        private String adRefKey;
        @SerializedName("redirect")
        private String redirect;
        @SerializedName("ad_click_url")
        private String adClickUrl;
        @SerializedName("headline")
        private Headline headline;
        @SerializedName("applinks")
        private String applinks;
        public boolean isSelected;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAdRefKey() {
            return adRefKey;
        }

        public String getRedirect() {
            return redirect;
        }

        public void setRedirect(String redirect) {
            this.redirect = redirect;
        }

        public String getAdClickUrl() {
            return adClickUrl;
        }

        public Headline getHeadline() {
            return headline;
        }

        public void setHeadline(Headline headline) {
            this.headline = headline;
        }

        public String getApplinks() {
            return applinks;
        }

        public void setApplinks(String applinks) {
            this.applinks = applinks;
        }

    }

    public static class Headline {

        @SerializedName("template_id")
        private String templateId;
        @SerializedName("name")
        private String name;
        @SerializedName("image")
        private Image image;
        @SerializedName("shop")
        private Shop shop;
        @SerializedName("button_text")
        private String buttonText;
        @SerializedName("promoted_text")
        private String promotedText;
        @SerializedName("description")
        private String description;
        @SerializedName("uri")
        private String uri;
        @SerializedName("badges")
        private List<?> badges;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public Shop getShop() {
            return shop;
        }

        public void setShop(Shop shop) {
            this.shop = shop;
        }

        public String getButtonText() {
            return buttonText;
        }

        public void setButtonText(String buttonText) {
            this.buttonText = buttonText;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public List<?> getBadges() {
            return badges;
        }

        public void setBadges(List<?> badges) {
            this.badges = badges;
        }

        public static class Image {

            @SerializedName("full_url")
            private String fullUrl;
            @SerializedName("full_ecs")
            private String fullEcs;

            public String getFullUrl() {
                return fullUrl;
            }

            public void setFullUrl(String fullUrl) {
                this.fullUrl = fullUrl;
            }
        }

    }

    public static class Shop {


        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("domain")
        private String domain;
        @SerializedName("tagline")
        private String tagline;
        @SerializedName("slogan")
        private String slogan;
        @SerializedName("location")
        private String location;
        @SerializedName("city")
        private String city;
        @SerializedName("gold_shop")
        private boolean goldShop;
        @SerializedName("gold_shop_badge")
        private boolean goldShopBadge;
        @SerializedName("shop_is_official")
        private boolean shopIsOfficial;
        @SerializedName("image_shop")
        private ImageShop imageShop;
        @SerializedName("product")
        private List<Product> product;

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

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getTagline() {
            return tagline;
        }

        public void setTagline(String tagline) {
            this.tagline = tagline;
        }

        public String getSlogan() {
            return slogan;
        }

        public void setSlogan(String slogan) {
            this.slogan = slogan;
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

        public boolean isGoldShop() {
            return goldShop;
        }

        public ImageShop getImageShop() {
            return imageShop;
        }

        public List<Product> getProduct() {
            return product;
        }

        public void setProduct(List<Product> product) {
            this.product = product;
        }

        public static class ImageShop {

            @SerializedName("cover")
            private String cover;
            @SerializedName("s_url")
            private String sUrl;
            @SerializedName("xs_url")
            private String xsUrl;
            @SerializedName("cover_ecs")
            private String coverEcs;
            @SerializedName("s_ecs")
            private String sEcs;
            @SerializedName("xs_ecs")
            private String xsEcs;

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getXsUrl() {
                return xsUrl;
            }

            public String getCoverEcs() {
                return coverEcs;
            }

            public String getSEcs() {
                return sEcs;
            }
        }

        public static class Product {

            @SerializedName("id")
            private String id;
            @SerializedName("name")
            private String name;
            @SerializedName("price_format")
            private String priceFormat;
            @SerializedName("applinks")
            private String applinks;
            @SerializedName("image_product")
            private Product.ImageProduct imageProduct;

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

            public String getApplinks() {
                return applinks;
            }

            public void setApplinks(String applinks) {
                this.applinks = applinks;
            }

            public static class ImageProduct {

                @SerializedName("product_id")
                private String productId;
                @SerializedName("product_name")
                private String productName;
                @SerializedName("image_url")
                private String imageUrl;
                @SerializedName("image_click_url")
                private String imageClickUrl;

                public String getProductId() {
                    return productId;
                }

                public void setProductId(String productId) {
                    this.productId = productId;
                }

                public String getProductName() {
                    return productName;
                }

                public void setProductName(String productName) {
                    this.productName = productName;
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
