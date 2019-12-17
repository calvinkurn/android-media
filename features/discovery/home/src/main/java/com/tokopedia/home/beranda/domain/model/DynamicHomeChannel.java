package com.tokopedia.home.beranda.domain.model;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.kotlin.model.ImpressHolder;

import java.util.ArrayList;
import java.util.HashMap;
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

    public class Channels extends ImpressHolder {
        public static final String LAYOUT_HERO = "hero_4_image";
        public static final String LAYOUT_3_IMAGE = "3_image";
        public static final String LAYOUT_SPRINT = "sprint_3_image";
        public static final String LAYOUT_SPRINT_LEGO = "sprint_lego";
        public static final String LAYOUT_ORGANIC = "organic";
        public static final String LAYOUT_6_IMAGE = "6_image";
        public static final String LAYOUT_BANNER_GIF = "banner_image";
        public static final String LAYOUT_LEGO_3_IMAGE = "lego_3_image";
        public static final String LAYOUT_SPRINT_CAROUSEL = "sprint_carousel";
        public static final String LAYOUT_DIGITAL_WIDGET = "digital_widget";
        public static final String LAYOUT_BU_WIDGET = "bu_widget";
        public static final String LAYOUT_TOPADS = "topads";
        public static final String LAYOUT_SPOTLIGHT = "spotlight";
        public static final String LAYOUT_HOME_WIDGET = "home_widget";
        public static final String LAYOUT_BANNER_ORGANIC = "banner_organic";
        public static final String LAYOUT_BANNER_CAROUSEL = "banner_carousel";
        public static final String LAYOUT_REVIEW = "product_review";
        public static final String LAYOUT_PLAY_BANNER = "play_widget";

        public static final String channelId = "channelId";

        @Expose
        @SerializedName("id")
        private String id;

        @Expose
        @SerializedName("galaxy_attribution")
        private String galaxyAttribution;

        @Expose
        @SerializedName("persona")
        private String persona;

        @Expose
        @SerializedName("brand_id")
        private String brandId;

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
        @SerializedName("showPromoBadge")
        private Boolean showPromoBadge;

        @Expose
        @SerializedName("header")
        private Header header;
        @Expose
        @SerializedName("banner")
        private Banner banner;
        @SerializedName("promoName")
        private String promoName;
        @SerializedName("homeAttribution")
        private String homeAttribution;

        private int position;

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

        public void setPosition(int position) {
            this.position = position;
        }

        public Banner getBanner() {
            return banner;
        }

        public void setBanner(Banner banner) {
            this.banner = banner;
        }

        public Boolean getShowPromoBadge() {
            return showPromoBadge;
        }

        public String getGalaxyAttribution() {
            return galaxyAttribution;
        }

        public void setGalaxyAttribution(String galaxyAttribution) {
            this.galaxyAttribution = galaxyAttribution;
        }

        public String getPersona() {
            return persona;
        }

        public void setPersona(String persona) {
            this.persona = persona;
        }

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public void setShowPromoBadge(Boolean showPromoBadge) {
            this.showPromoBadge = showPromoBadge;
        }

        private List<Object> convertProductEnhanceProductMixDataLayer(Grid[] grids, String headerName, String type) {
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
                                    "list", "/ - p1 - dynamic channel mix - product - "+headerName+" - "+type,
                                    "position", String.valueOf(i + 1),
                                    "dimension83", grid.getFreeOngkir().isActive() ? "bebas ongkir" : "none/other"
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

        public Map<String, Object> getEnhanceClickSprintSaleLegoHomePage(int position) {
            return DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "homepage",
                    "eventAction", "click on lego product",
                    "eventLabel", getHeader().getName(),
                    channelId, id,
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/ - p1 - lego product - " + getHeader().getName()),
                                    "products", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "name", getGrids()[position].getName(),
                                                    "id", getGrids()[position].getId(),
                                                    "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(
                                                            getGrids()[position].getPrice()
                                                    )),
                                                    "list", "/ - p1 - lego product - " + getHeader().getName(),
                                                    "position", String.valueOf(position + 1)
                                            )
                                    )
                            )
                    ),
                    "attribution", getHomeAttribution(position + 1, getGrids()[position].getId())
            );
        }

        public Map<String, Object> getEnhanceClickSprintSaleHomePage(int position, String countDown, Boolean isFreeOngkir) {
            return DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "homepage",
                    "eventAction", "sprint sale click",
                    "eventLabel", countDown,
                    channelId, id,
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
                                                    "dimension38", getHomeAttribution(position + 1, getGrids()[position].getId()),
                                                    "dimension83", isFreeOngkir ? "bebas ongkir" : "none/other"
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
                    channelId, id,
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
                                    "creative_url", grid.getImageUrl(),
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
                                    "creative_url", grid.getImageUrl(),
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
                        "creative_url", getHero()[0].getImageUrl(),
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
                                    "creative_url", grid.getImageUrl(),
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
                    channelId, id,
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

        public Map<String, Object> getEnhanceImpressionDynamicSprintLegoHomePage() {
            List<Object> list = convertPromoEnhanceDynamicSprintLegoDataLayer(getGrids());
            return DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "homepage",
                    "eventAction", "impression on lego product",
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
        private List<Object> convertPromoEnhanceDynamicSprintLegoDataLayer(Grid[] grids) {
            List<Object> list = new ArrayList<>();

            if (grids != null) {
                for (int i = 0; i < grids.length; i++) {
                    Grid grid = grids[i];
                    list.add(
                            DataLayer.mapOf(
                                    "id", grid.getId(),
                                    "name", grid.getName(),
                                    "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(
                                            grid.getPrice()
                                    )),
                                    "brand", "none / other",
                                    "variant", "none / other",
                                    "list", "/ - p1 - lego product - " + getHeader().getName(),
                                    "position", String.valueOf(i + 1),
                                    "dimension83", grid.getFreeOngkir().isActive() ? "bebas ongkir" : "none/other",
                                    "dimension84", id
                            )
                    );
                }
            }
            return list;
        }

        private List<Object> convertPromoEnhanceBannerChannelMix() {
            List<Object> list = new ArrayList<>();

            /**
             * Banner always in position 1 because only 1 banner shown
             */
            list.add(
                    DataLayer.mapOf(
                            "id", getBanner().getId(),
                            "name", "/ - p1 - dynamic channel mix - banner - "+getHeader().name,
                            "creative", getBanner().getAttribution(),
                            "creative_url", getBanner().getImageUrl(),
                            "position", String.valueOf(1)
                    )
            );
            return list;
        }

        public Map<String, Object> getEnhanceClickDynamicChannelHomePage(Hero hero, int position) {
            return DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage",
                    "eventAction", "curated list banner click",
                    "eventLabel", String.format("%s - %s", getHeader().getName(), getHeader().getApplink()),
                    channelId, id,
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
                    channelId, id,
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
                    channelId, id,
                    "eventLabel", grid.getAttribution(),
                    channelId, id,
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
                    "eventLabel", grid.getAttribution(),
                    channelId, id,
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", grid.getId(),
                                                    "name", getPromoName(),
                                                    "creative", grid.getAttribution(),
                                                    "creative_url", grid.getImageUrl(),
                                                    "position", String.valueOf(position),
                                                    "attribution", getHomeAttribution(position, grid.getAttribution())
                                            )
                                    )
                            )
                    )
            );
        }

        public Map<String, Object> getEnhanceClickProductChannelMix(int gridPosition, boolean isFreeOngkir) {
            return DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "homepage",
                    "eventAction", "click on product dynamic channel mix",
                    "eventLabel", getHeader().name,
                    channelId, id,
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/ - p1 - dynamic channel mix - product - "+getHeader().name),
                                    "products", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "name", getGrids()[gridPosition].getName(),
                                                    "id", getGrids()[gridPosition].getId(),
                                                    "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(
                                                            getGrids()[gridPosition].getPrice()
                                                    )),
                                                    "brand", "none / other",
                                                    "category", "none / other",
                                                    "variant", "none / other",
                                                    "position", String.valueOf(gridPosition+1),
                                                    "attribution", getHomeAttribution(gridPosition + 1, getGrids()[gridPosition].getId()),
                                                    "dimension83", isFreeOngkir ? "bebas ongkir" : "none/other"
                                            )
                                    )
                            )
                    ));
        }

        public Map<String, Object> getEnhanceImpressionProductChannelMix() {
            String type = "";
            if (layout.equals(LAYOUT_BANNER_ORGANIC)) {
                type = "non carousel";
            } else if (layout.equals(LAYOUT_BANNER_CAROUSEL)) {
                type = "carousel";
            }
            List<Object> list = convertProductEnhanceProductMixDataLayer(getGrids(), getHeader().name, type);
            return DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "homepage",
                    "eventAction", "impression on product dynamic channel mix",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "impressions", DataLayer.listOf(
                                    list.toArray(new Object[list.size()])

                            )),
                    "channelId", id
            );
        }

        /**
         * Banner always in position 1 because only 1 banner shown
         */
        public Map<String, Object> getEnhanceClickBannerChannelMix() {
            return DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage",
                    "eventAction", "click on banner dynamic channel mix",
                    "eventLabel", getHeader().name,
                    channelId, id,
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", banner.getId(),
                                                    "name", "/ - p1 - dynamic channel mix - banner - "+getHeader().getName(),
                                                    "creative", banner.getAttribution(),
                                                    "creative_url", banner.getImageUrl(),
                                                    "position", String.valueOf(1)
                                            )
                                    )
                            )
                    )
            );
        }

        public Map<String, Object> getEnhanceClickBannerButtonChannelMix() {
            return DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage",
                    "eventAction", "click "+getBanner().getCta().getText()+" on dynamic channel mix",
                    channelId, id,
                    "eventLabel", getHeader().name,
                    "channelId", id,
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", banner.getId(),
                                                    "name", "/ - p1 - dynamic channel mix - banner - "+getHeader().getName(),
                                                    "creative", banner.getAttribution(),
                                                    "creative_url", banner.getImageUrl(),
                                                    "position", String.valueOf(position),
                                                    "promo_code", banner.getCta().couponCode
                                            )
                                    )
                            )
                    )
            );
        }

        public HashMap<String, Object> getEnhanceImpressionBannerChannelMix() {
            List<Object> list = convertPromoEnhanceBannerChannelMix();
            return (HashMap<String, Object>) DataLayer.mapOf(
                    "event", "promoView",
                    "eventCategory", "homepage",
                    "eventAction", "impression on banner dynamic channel mix",
                    "eventLabel", "",
                    "channelId", id,
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(
                                            list.toArray(new Object[list.size()])
                                    )
                            )

                    ),
                    "attribution", getHomeAttribution(1, getHeader().getName())
            );
        }

        public Map<String, Object> getEnhanceClickPlayBanner() {
            List<Object> list = convertPromoEnhancePlayBanner();
            return DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage-cmp",
                    "eventAction", "click on play dynamic banner",
                    "eventLabel", id + " - Play-CMP_OTHERS_indonesian-idol",
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            list.toArray(new Object[list.size()])
                                    )
                            )

                    )
            );
        }

        public Map<String, Object> getEnhanceImpressionPlayBanner() {
            List<Object> list = convertPromoEnhancePlayBanner();
            return DataLayer.mapOf(
                    "event", "promoView",
                    "eventCategory", "homepage-cmp",
                    "eventAction", "impression on play dynamic banner",
                    "eventLabel", "Play-CMP_OTHERS_indonesian-idol",
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            list.toArray(new Object[list.size()])
                                    )
                            )

                    )
            );
        }

        private List<Object> convertPromoEnhancePlayBanner() {
            List<Object> list = new ArrayList<>();

            /**
             * Banner always in position 1 because only 1 banner shown
             */
            list.add(
                    DataLayer.mapOf(
                            "id", getBanner().getId(),
                            "name", "/ - p1 - play dynamic banner - " + getHeader().name,
                            "creative", "Play-CMP_OTHERS_indonesian-idol",
                            "creative_url", getBanner().getImageUrl(),
                            "position", String.valueOf(1)
                    )
            );
            return list;
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

        @Expose
        @SerializedName("freeOngkir")
        private FreeOngkir freeOngkir;

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

        public FreeOngkir getFreeOngkir() {
            return freeOngkir;
        }

        public void setFreeOngkir(FreeOngkir freeOngkir) {
            this.freeOngkir = freeOngkir;
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

        @Expose
        @SerializedName("textColor")
        private String textColor;

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

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }
    }

    public class Banner extends ImpressHolder{
        @Expose
        @SerializedName("id")
        private String id;

        @Expose
        @SerializedName("title")
        private String title;

        @Expose
        @SerializedName("description")
        private String description;

        @Expose
        @SerializedName("cta")
        private CtaData cta;

        @Expose
        @SerializedName("url")
        private String url;

        @Expose
        @SerializedName("applink")
        private String applink;

        @Expose
        @SerializedName("text_color")
        private String textColor;

        @Expose
        @SerializedName("image_url")
        private String imageUrl;

        @Expose
        @SerializedName("attribution")
        private String attribution;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public CtaData getCta() {
            return cta;
        }

        public void setCta(CtaData cta) {
            this.cta = cta;
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

        public void setApplink(String applink) {
            this.applink = applink;
        }

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getAttribution() {
            return attribution;
        }

        public void setAttribution(String attribution) {
            this.attribution = attribution;
        }
    }

    public class CtaData {
        @Expose
        @SerializedName("type")
        private String type;

        @Expose
        @SerializedName("mode")
        private String mode;

        @Expose
        @SerializedName("text")
        private String text;

        @Expose
        @SerializedName("coupon_code")
        private String couponCode;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getCouponCode() {
            return couponCode;
        }

        public void setCouponCode(String couponCode) {
            this.couponCode = couponCode;
        }
    }
}
