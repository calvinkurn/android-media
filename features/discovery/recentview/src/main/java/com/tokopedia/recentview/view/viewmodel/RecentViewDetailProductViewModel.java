package com.tokopedia.recentview.view.viewmodel;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.recentview.view.adapter.typefactory.RecentViewTypeFactory;

import java.util.List;

import static com.tokopedia.design.utils.CurrencyFormatHelper.convertRupiahToInt;

/**
 * @author by nisie on 7/4/17.
 */

public class RecentViewDetailProductViewModel implements Visitable<RecentViewTypeFactory> {

    public static final String DEFAULT_VALUE_NONE_OTHER = "none / other";

    private String name;
    private String price;
    private String imageSource;
    private boolean isFreeReturn;
    private boolean isWishlist;
    private int rating;
    private Integer productId;
    private boolean isGold;
    private final boolean isOfficial;
    private final String shopName;
    private final String shopLocation;
    private List<LabelsViewModel> labels;
    private int positionForRecentViewTracking;

    public RecentViewDetailProductViewModel(Integer productId,
                                            String name,
                                            String price,
                                            String imageSource,
                                            List<LabelsViewModel> labels,
                                            boolean isFreeReturn,
                                            boolean isWishlist,
                                            int rating,
                                            boolean isGold,
                                            boolean isOfficial,
                                            String shopName,
                                            String shopLocation,
                                            int positionForRecentViewTracking) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageSource = imageSource;
        this.labels = labels;
        this.isFreeReturn = isFreeReturn;
        this.isWishlist = isWishlist;
        this.rating = rating;
        this.isGold = isGold;
        this.isOfficial = isOfficial;
        this.shopName = shopName;
        this.shopLocation = shopLocation;
        this.positionForRecentViewTracking = positionForRecentViewTracking;
    }

    public int getPositionForRecentViewTracking() {
        return positionForRecentViewTracking;
    }

    public void setPositionForRecentViewTracking(int position) {
        this.positionForRecentViewTracking = position;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImageSource() {
        return imageSource;
    }

    public boolean isFreeReturn() {
        return isFreeReturn;
    }

    public boolean isWishlist() {
        return isWishlist;
    }

    public int getRating() {
        return rating;
    }

    public Integer getProductId() {
        return productId;
    }

    public boolean isGold() {
        return isGold;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    @Override
    public int type(RecentViewTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public List<LabelsViewModel> getLabels() {
        return labels;
    }

    public Object getRecentViewAsObjectDataLayerForClick() {
        return DataLayer.mapOf(
                "name", getName(),
                "id", getProductId(),
                "price", Integer.toString(convertRupiahToInt(getPrice())),
                "brand", DEFAULT_VALUE_NONE_OTHER,
                "category", "",
                "position", Integer.toString(getPositionForRecentViewTracking())
        );
    }
}
