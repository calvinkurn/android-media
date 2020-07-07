
package com.tokopedia.core.drawer2.data.pojo.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopStats {

    @SerializedName("shop_service_rate")
    @Expose
    private String shopServiceRate;
    @SerializedName("shop_speed_rate")
    @Expose
    private String shopSpeedRate;
    @SerializedName("shop_total_transaction")
    @Expose
    private String shopTotalTransaction;
    @SerializedName("shop_speed_description")
    @Expose
    private String shopSpeedDescription;
    @SerializedName("shop_badge_level")
    @Expose
    private ShopBadgeLevel shopBadgeLevel;
    @SerializedName("shop_last_one_month")
    @Expose
    private ShopLastOneMonth shopLastOneMonth;
    @SerializedName("shop_total_product")
    @Expose
    private String shopTotalProduct;
    @SerializedName("shop_reputation_score")
    @Expose
    private String shopReputationScore;
    @SerializedName("shop_total_transaction_canceled")
    @Expose
    private String shopTotalTransactionCanceled;
    @SerializedName("shop_last_six_months")
    @Expose
    private ShopLastSixMonths shopLastSixMonths;
    @SerializedName("shop_total_etalase")
    @Expose
    private String shopTotalEtalase;
    @SerializedName("shop_service_description")
    @Expose
    private String shopServiceDescription;
    @SerializedName("shop_item_sold")
    @Expose
    private String shopItemSold;
    @SerializedName("shop_last_twelve_months")
    @Expose
    private ShopLastTwelveMonths shopLastTwelveMonths;
    @SerializedName("shop_accuracy_rate")
    @Expose
    private String shopAccuracyRate;
    @SerializedName("shop_accuracy_description")
    @Expose
    private String shopAccuracyDescription;
}
