package com.tokopedia.home.beranda.domain.model;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tkpd.library.utils.CurrencyFormatHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class DynamicHomeChannel {
    @Expose
    @SerializedName("channels")
    private List<Channels> channels;

    public List<Channels> getChannels() {
        return channels;
    }

    public void setChannels(List<Channels> channels) {
        this.channels = channels;
    }

    public class Channels {
        public static final String LAYOUT_HERO = "hero_4_image";
        public static final String LAYOUT_3_IMAGE = "3_image";
        public static final String LAYOUT_SPRINT = "sprint_3_image";
        public static final String LAYOUT_SPRINT_LEGO = "sprint_lego";
        public static final String LAYOUT_ORGANIC = "organic";
        public static final String LAYOUT_6_IMAGE = "6_image";
        public static final String LAYOUT_LEGO_3_IMAGE = "lego_3_image";
        public static final String LAYOUT_SPRINT_CAROUSEL = "sprint_carousel";
        public static final String LAYOUT_DIGITAL_WIDGET = "digital_widget";
        public static final String LAYOUT_TOPADS = "topads";
        public static final String LAYOUT_SPOTLIGHT = "spotlight";

        @Expose
        @SerializedName("id")
        private String id;

        @Expose
        @SerializedName("layout")
        private String layout;

        @Expose
        @SerializedName("name")
        private String name;

        @Expose
        @SerializedName("grids")
        private Grid[] grids;

        @Expose
        @SerializedName("hero")
        private Hero[] hero;

        @Expose
        @SerializedName("type")
        private String type;

        @Expose
        @SerializedName("header")
        private Header header;
        @SerializedName("promoName")
        private String promoName;
        @SerializedName("homeAttribution")
        private String homeAttribution;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLayout() {
            return layout;
        }

        public void setLayout(String layout) {
            this.layout = layout;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Grid[] getGrids() {
            return grids;
        }

        public void setGrids(Grid[] grids) {
            this.grids = grids;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Header getHeader() {
            return header;
        }

        public void setHeader(Header header) {
            this.header = header;
        }

        public Hero[] getHero() {
            return hero;
        }

        public void setHero(Hero[] hero) {
            this.hero = hero;
        }

        public Map<String, Object> getEnhanceImpressionSprintSaleHomePage(int position) {
            List<Object> list = convertProductEnhanceSprintSaleDataLayer(getGrids());
            return DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "homepage",
                    "eventAction", "sprint sale impression",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "impressions", DataLayer.listOf(
                                    list.toArray(new Object[list.size()])

                            )),
                    "attribution", getHomeAttribution(position + 1, "")
            );
        }

        private List<Object> convertProductEnhanceSprintSaleDataLayer(Grid[] grids) {
            List<Object> list = new ArrayList<>();

            if (grids != null) {
                for (int i = 0; i < grids.length; i++) {
                    Grid grid = grids[i];
                    list.add(
                            DataLayer.mapOf(
                                    "name", grid.getName(),
                                    "id", grid.getId(),
                                    "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(
                                            grid.getPrice()
                                    )),
                                    "brand", "none / other",
                                    "category", "none / other",
                                    "variant", "none / other",
                                    "list", "/ - p1 - sprint sale",
                                    "position", String.valueOf(i + 1)
                            )
                    );
                }
            }
            return list;
        }

        public List<Object> convertProductEnhanceSprintSaleCarouselDataLayer() {
            List<Object> list = new ArrayList<>();

            if (getGrids() != null) {
                for (int i = 0; i < getGrids().length; i++) {
                    Grid grid = getGrids()[i];
                    list.add(
                            DataLayer.mapOf(
                                    "id", grid.getId(),
                                    "name", "/ - p2 - sprint sale banner",
                                    "position", String.valueOf(i + 1),
                                    "creative", grid.getName(),
                                    "creative_url", grid.getImageUrl()
                            )
                    );
                }
            }
            return list;
        }

        public Map<String, Object> getEnhanceClickSprintSaleLegoHomePage(int position, String countDown) {
            return DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "homepage",
                    "eventAction", "sprint sale click lego",
                    "eventLabel", countDown,
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/ - p1 - sprint sale lego"),
                                    "products", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "name", getGrids()[position].getName(),
                                                    "id", getGrids()[position].getId(),
                                                    "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(
                                                            getGrids()[position].getPrice()
                                                    )),
                                                    "list", "/ - p1 - sprint sale lego",
                                                    "position", String.valueOf(position + 1),
                                                    "dimension38", getHomeAttribution(position + 1, getGrids()[position].getId())
                                            )
                                    )
                            )
                    ),
                    "attribution", getHomeAttribution(position + 1, getGrids()[position].getId())
            );
        }

        public Map<String, Object> getEnhanceClickSprintSaleHomePage(int position, String countDown) {
            return DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "homepage",
                    "eventAction", "sprint sale click",
                    "eventLabel", countDown,
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/ - p1 - sprint sale"),
                                    "products", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "name", getGrids()[position].getName(),
                                                    "id", getGrids()[position].getId(),
                                                    "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(
                                                            getGrids()[position].getPrice()
                                                    )),
                                                    "list", "/ - p1 - sprint sale",
                                                    "position", String.valueOf(position + 1),
                                                    "dimension38", getHomeAttribution(position + 1, getGrids()[position].getId())
                                            )
                                    )
                            )
                    ),
                    "attribution", getHomeAttribution(position + 1, getGrids()[position].getId())
            );
        }

        public Map<String, Object> getEnhanceClickSprintSaleCarouselHomePage(int position, String countDown, String label) {
            return DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage",
                    "eventAction", "sprint sale banner click",
                    "eventLabel", String.format("%s - %s", countDown, label),
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", getGrids()[position].getId(),
                                                    "name", "/ - p2 - sprint sale banner",
                                                    "position", String.valueOf(position + 1),
                                                    "creative", getGrids()[position].getName(),
                                                    "creative_url", getGrids()[position].getImageUrl()
                                            )
                                    )
                            )
                    ),
                    "attribution", getHomeAttribution(position + 1, getGrids()[position].getId())
            );
        }

        public Map<String, Object> getEnhanceImpressionLegoAndCuratedHomePage(
                List<Object> legoAndCuratedList) {
            return DataLayer.mapOf(
                    "event", "promoView",
                    "eventCategory", "homepage",
                    "eventAction", "home banner impression",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            legoAndCuratedList.toArray(new Object[legoAndCuratedList.size()])
                                    )
                            )
                    )
            );
        }

        public List<Object> convertProductEnhanceSprintSaleCarouselDataLayerForCombination() {
            List<Object> list = new ArrayList<>();

            if (getGrids() != null) {
                for (int i = 0; i < getGrids().length; i++) {
                    Grid grid = getGrids()[i];
                    list.add(
                            DataLayer.mapOf(
                                    "id", grid.getId(),
                                    "name", "/ - p2 - sprint sale banner",
                                    "creative", grid.getName(),
                                    "position", String.valueOf(i + 1)
                                    )
                    );
                }
            }
            return list;
        }

        public List<Object> convertPromoEnhanceLegoBannerDataLayerForCombination() {
            List<Object> list = new ArrayList<>();

            if (getGrids() != null) {
                for (int i = 0; i < getGrids().length; i++) {
                    Grid grid = grids[i];
                    list.add(
                            DataLayer.mapOf(
                                    "id", grid.getId(),
                                    "name", getPromoName(),
                                    "creative", grid.getAttribution(),
                                    "position", String.valueOf(i + 1)
                            )
                    );
                }
            }
            return list;
        }

        public List<Object> convertPromoEnhanceDynamicChannelDataLayerForCombination() {
            List<Object> list = new ArrayList<>();
            if (getHero() != null) {
                list.add(DataLayer.mapOf(
                        "id", getHero()[0].getId(),
                        "name", getPromoName(),
                        "creative", getPromoName(),
                        "position", String.valueOf(1)
                ));
            }

            if (getGrids() != null) {
                for (int i = 0; i < getGrids().length; i++) {
                    Grid grid = getGrids()[i];
                    list.add(
                            DataLayer.mapOf(
                                    "id", grid.getId(),
                                    "name", getPromoName(),
                                    "creative", getPromoName(),
                                    "position", String.valueOf(i + 2)
                            )
                    );
                }
            }
            return list;
        }

        private List<Object> convertPromoEnhanceLegoBannerDataLayer(Grid[] grids, String promoName) {
            List<Object> list = new ArrayList<>();

            if (grids != null) {
                for (int i = 0; i < grids.length; i++) {
                    Grid grid = grids[i];
                    list.add(
                            DataLayer.mapOf(
                                    "id", grid.getId(),
                                    "name", promoName,
                                    "creative", grid.getAttribution(),
                                    "creative_url", grid.getImageUrl(),
                                    "position", String.valueOf(i + 1)
                            )
                    );
                }
            }
            return list;
        }

        public Map<String, Object> getEnhanceImpressionDynamicChannelHomePage(int position) {
            List<Object> list = convertPromoEnhanceDynamicChannelDataLayer(getHero(), getGrids(), getPromoName());
            return DataLayer.mapOf(
                    "event", "promoView",
                    "eventCategory", "homepage",
                    "eventAction", "curated list banner impression",
                    "eventLabel", getHeader().getName(),
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            list.toArray(new Object[list.size()])
                                    )
                            )
                    ),
                    "attribution", getHomeAttribution(position + 1, getHeader().getName())
            );
        }

        public Map<String, Object> getEnhanceImpressionDynamicSprintLegoHomePage(int position) {
            List<Object> list = convertPromoEnhanceDynamicSprintLegoDataLayer(position, getHero(), getGrids(), getPromoName());
            return DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "homepage",
                    "eventAction", "sprint sale impression",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "curencyCode", "IDR",
                            "impressions", DataLayer.listOf(
                                    list.toArray(new Object[list.size()])
                            )
                    ),
                    "attribution", getHomeAttribution(position + 1, getHeader().getName())
            );
        }

        private List<Object> convertPromoEnhanceDynamicChannelDataLayer(Hero[] hero, Grid[] grids, String promoName) {
            List<Object> list = new ArrayList<>();
            if (hero != null) {
                list.add(DataLayer.mapOf(
                        "id", hero[0].getId(),
                        "name", promoName,
                        "creative", hero[0].getAttribution(),
                        "position", String.valueOf(1)
                ));
            }

            if (grids != null) {
                for (int i = 0; i < grids.length; i++) {
                    Grid grid = grids[i];
                    list.add(
                            DataLayer.mapOf(
                                    "id", grid.getId(),
                                    "name", promoName,
                                    "creative", grid.getAttribution(),
                                    "position", String.valueOf(i + 2)
                            )
                    );
                }
            }
            return list;
        }
        private List<Object> convertPromoEnhanceDynamicSprintLegoDataLayer(int position, Hero[] hero, Grid[] grids, String promoName) {
            List<Object> list = new ArrayList<>();
            if (hero != null) {
                list.add(DataLayer.mapOf(
                        "name", hero[0].getName(),
                        "id", hero[0].getId(),
                        "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(
                                hero[0].getPrice()
                        )),
                        "list", "/ - p1 - sprint sale lego",
                        "position", String.valueOf(position + 1),
                        "dimension38", getHomeAttribution(position + 1, hero[0].getId())
                ));
            }

            if (grids != null) {
                for (int i = 0; i < grids.length; i++) {
                    Grid grid = grids[i];
                    list.add(
                            DataLayer.mapOf(
                                    "id", grid.getId(),
                                    "name", promoName,
                                    "list", grid.getAttribution(),
                                    "position", String.valueOf(i + 2)
                            )
                    );
                }
            }
            return list;
        }

        public Map<String, Object> getEnhanceClickDynamicChannelHomePage(Hero hero, int position) {
            return DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage",
                    "eventAction", "curated list banner click",
                    "eventLabel", String.format("%s - %s", getHeader().getName(), getHeader().getApplink()),
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", hero.getId(),
                                                    "name", getPromoName(),
                                                    "creative", hero.getAttribution(),
                                                    "position", String.valueOf(position)
                                            )
                                    )
                            )
                    ),
                    "attribution", getHomeAttribution(position, hero.getAttribution())
            );
        }

        public Map<String, Object> getEnhanceClickDynamicChannelHomePage(Grid grid, int position) {
            return DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage",
                    "eventAction", "curated list banner click",
                    "eventLabel", String.format("%s - %s", getHeader().getName(), getHeader().getApplink()),
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", grid.getId(),
                                                    "name", getPromoName(),
                                                    "creative", grid.getAttribution(),
                                                    "position", String.valueOf(position)
                                            )
                                    )
                            )
                    ),
                    "attribution", getHomeAttribution(position, grid.getAttribution())
            );
        }

        public Map<String, Object> getEnhanceClickLegoBannerHomePage(Grid grid, int position) {
            return DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage",
                    "eventAction", "lego banner click",
                    "eventLabel", grid.getName(),
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", grid.getId(),
                                                    "name", getPromoName(),
                                                    "creative", grid.getAttribution(),
                                                    "creative_url", grid.getImageUrl(),
                                                    "position", String.valueOf(position)
                                            )
                                    )
                            )
                    ),
                    "attribution", getHomeAttribution(position, grid.getAttribution())
            );
        }

        public Map<String, Object> getEnhanceClickThreeLegoBannerHomePage(Grid grid, int position) {
            return DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage",
                    "eventAction", "lego banner 3 image click",
                    "eventLabel", grid.getName(),
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", grid.getId(),
                                                    "name", getPromoName(),
                                                    "creative", grid.getAttribution(),
                                                    "creative_url", grid.getImageUrl(),
                                                    "position", String.valueOf(position)
                                            )
                                    )
                            )
                    ),
                    "attribution", getHomeAttribution(position, grid.getAttribution())
            );
        }

        public void setPromoName(String promoName) {
            this.promoName = promoName;
        }

        public String getPromoName() {
            return promoName;
        }

        public String getHomeAttribution(int position, String creativeName) {
            if (homeAttribution != null)
                return homeAttribution.replace("$1", Integer.toString(position)).replace("$2", (creativeName != null) ? creativeName : "");
            return "";
        }

        public void setHomeAttribution(String homeAttribution) {
            this.homeAttribution = homeAttribution;
        }

        public String getHomeAttribution() {
            return homeAttribution;
        }
    }

    public class Hero {
        @Expose
        @SerializedName("id")
        private String id;

        @Expose
        @SerializedName("imageUrl")
        private String imageUrl;

        @Expose
        @SerializedName("name")
        private String name;

        @Expose
        @SerializedName("applink")
        private String applink;

        @Expose
        @SerializedName("url")
        private String url;

        @Expose
        @SerializedName("price")
        private String price;

        @Expose
        @SerializedName("attribution")
        private String attribution;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getApplink() {
            return applink;
        }

        public void setApplink(String applink) {
            this.applink = applink;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAttribution() {
            return attribution;
        }

        public void setAttribution(String attribution) {
            this.attribution = attribution;
        }
    }

    public class Grid {
        @Expose
        @SerializedName("id")
        private String id;

        @Expose
        @SerializedName("price")
        private String price;

        @Expose
        @SerializedName("imageUrl")
        private String imageUrl;

        @Expose
        @SerializedName("name")
        private String name;

        @Expose
        @SerializedName("applink")
        private String applink;

        @Expose
        @SerializedName("url")
        private String url;

        @Expose
        @SerializedName("discount")
        private String discount;

        @Expose
        @SerializedName("slashedPrice")
        private String slashedPrice;

        @Expose
        @SerializedName("label")
        private String label;

        @Expose
        @SerializedName("soldPercentage")
        private int soldPercentage;

        @Expose
        @SerializedName("attribution")
        private String attribution;

        @Expose
        @SerializedName("impression")
        private String impression;

        @Expose
        @SerializedName("cashback")
        private String cashback;

        @Expose
        @SerializedName("productClickUrl")
        private String productClickUrl;

        public String getProductClickUrl() {
            return productClickUrl;
        }

        public void setProductClickUrl(String productClickUrl) {
            this.productClickUrl = productClickUrl;
        }

        public String getImpression() {
            return impression;
        }

        public void setImpression(String impression) {
            this.impression = impression;
        }

        public String getCashback() {
            return cashback;
        }

        public void setCashback(String cashback) {
            this.cashback = cashback;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getSoldPercentage() {
            return soldPercentage;
        }

        public void setSoldPercentage(int soldPercentage) {
            this.soldPercentage = soldPercentage;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getApplink() {
            return applink;
        }

        public void setApplink(String applink) {
            this.applink = applink;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getSlashedPrice() {
            return slashedPrice;
        }

        public void setSlashedPrice(String slashedPrice) {
            this.slashedPrice = slashedPrice;
        }

        public String getAttribution() {
            return attribution;
        }

        public void setAttribution(String attribution) {
            this.attribution = attribution;
        }
    }

    public class Header {
        @Expose
        @SerializedName("id")
        private String id;

        @Expose
        @SerializedName("name")
        private String name;

        @Expose
        @SerializedName("expiredTime")
        private String expiredTime;

        @Expose
        @SerializedName("serverTime")
        private long serverTimeUnix;

        @Expose
        @SerializedName("applink")
        private String applink;

        @Expose
        @SerializedName("url")
        private String url;

        @Expose
        @SerializedName("backColor")
        private String backColor;

        @Expose
        @SerializedName("backImage")
        private String backImage;

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

        public String getExpiredTime() {
            return expiredTime;
        }

        public void setExpiredTime(String expiredTime) {
            this.expiredTime = expiredTime;
        }

        public long getServerTimeUnix() {
            return serverTimeUnix;
        }

        public void setServerTimeUnix(long serverTimeUnix) {
            this.serverTimeUnix = serverTimeUnix;
        }

        public String getApplink() {
            return applink;
        }

        public void setApplink(String applink) {
            this.applink = applink;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getBackColor() {
            return backColor;
        }

        public void setBackColor(String backColor) {
            this.backColor = backColor;
        }

        public String getBackImage() {
            return backImage;
        }

        public void setBackImage(String backImage) {
            this.backImage = backImage;
        }
    }
}
