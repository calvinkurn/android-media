package com.tokopedia.tkpd.home.feed.view.viewModel;

import android.support.annotation.NonNull;

import com.tokopedia.core.home.model.HistoryProductListItem;
import com.tokopedia.core.home.model.HorizontalProductList;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.home.feed.domain.model.DataFeed;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;
import com.tokopedia.tkpd.home.feed.domain.model.Label;
import com.tokopedia.tkpd.home.feed.domain.model.ProductFeed;
import com.tokopedia.tkpd.home.feed.domain.model.TopAds;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kulomady on 12/20/16.
 */

public class ProductFeedViewModel {
    private List<RecyclerViewItem> result;
    private PagingHandler.PagingHandlerModel pagingHandlerModel;
    private List<RecyclerViewItem> data;

    public ProductFeedViewModel(DataFeed productFeed) {
        result = new ArrayList<>();
        preparedViewModelData(productFeed);
        pagingHandlerModel = prepareFeedPaging(productFeed.getFeed());
    }


    public List<RecyclerViewItem> getData() {
        return result;
    }

    public PagingHandler.PagingHandlerModel getPagingHandlerModel() {
        return pagingHandlerModel;
    }

    private List<RecyclerViewItem> preparedViewModelData(DataFeed dataFeed) {
        if (dataFeed.getRecentProductList() != null && dataFeed.getRecentProductList().size() > 0) {
            result.add(new HistoryProductListItem(
                    getHistoryProductItems(dataFeed.getRecentProductList())));
        }
        if (dataFeed.getFeed() != null
                && dataFeed.getFeed().getProducts() != null
                && dataFeed.getFeed().getProducts().size() > 0) {

            if (dataFeed.getTopAds() != null
                    && !dataFeed.getTopAds().isEmpty() && dataFeed.getTopAds().size() > 0) {
                if (dataFeed.getTopAds().size() <= 2) {
                    result.add(convertTopAdsDomainToViewModel(prepareTopAdsTop(dataFeed.getTopAds())));
                    result.addAll(getProductFeedItemsListFromFeed(dataFeed.getFeed().getProducts()));

                } else {
                    result.add(convertTopAdsDomainToViewModel(prepareTopAdsTop(dataFeed.getTopAds())));
                    result.addAll(getProductFeedItemsListFromFeed(dataFeed.getFeed().getProducts()));
                    result.add(convertTopAdsDomainToViewModel(prepareTopAdsBottom(dataFeed.getTopAds())));
                }

            } else {
                result.addAll(getProductFeedItemsListFromFeed(dataFeed.getFeed().getProducts()));
            }
        }


        return result;
    }

    private List<TopAds> prepareTopAdsTop(List<TopAds> topAdsList) {
        List<TopAds> topAdsListTop = new ArrayList<>();
        if (topAdsList.size() >= 2) {
            topAdsListTop = topAdsList.subList(0, 2);
        } else if (topAdsList.size() == 1) {
            topAdsListTop = topAdsList.subList(0, 1);
        }
        return topAdsListTop;
    }

    private List<TopAds> prepareTopAdsBottom(List<TopAds> topAdsList) {
        List<TopAds> topAdsListBottom = new ArrayList<>();
        if (topAdsList.size() >= 4) {
            topAdsListBottom = topAdsList.subList(2, 4);
        } else if (topAdsList.size() == 3) {
            topAdsListBottom = topAdsList.subList(2, 3);
        }

        return topAdsListBottom;
    }

    @NonNull
    private HorizontalProductList convertTopAdsDomainToViewModel(List<TopAds> topAdsList) {
        List<RecyclerViewItem> topAdsProductItemList = new ArrayList<>();

        for (TopAds domainTopAds : topAdsList) {
            ProductItem productItem = new ProductItem();
            productItem.setId(domainTopAds.getProduct().getId());
            productItem.setPrice(domainTopAds.getProduct().getPrice());
            productItem.setWholesale(
                    domainTopAds.getProduct().getWholesalePrice().size() > 0 ? "1" : "0");
            productItem.setName(domainTopAds.getProduct().getName());
            productItem.setShopId(Integer.parseInt(domainTopAds.getShop().getId()));
            String imageUri = domainTopAds.getProduct().getImgUrl();
            productItem.setImgUri(imageUri);
            productItem.setShop(domainTopAds.getShop().getName());
            productItem.setIsGold(domainTopAds.getShop().isGoldShop() ? "1" : "0");
            productItem.setLuckyShop(domainTopAds.getShop().getLucky());
            productItem.setPreorder(domainTopAds.getProduct().isPreorder() ? "1" : "0");
            productItem.setShopLocation(domainTopAds.getShop().getLocation());
            productItem.setBadges(
                    convertDomainBadgeToViewModelBadge(domainTopAds.getShop().getBadges()));
            productItem.setLabels(
                    convertDomainLabelToViewModelLabel(domainTopAds.getProduct().getLabels()));


            productItem.setTopAds(convertDomainTopAdsToViewModel(domainTopAds));
            productItem.setIsTopAds(true);
            topAdsProductItemList.add(productItem);

        }
        HorizontalProductList topAdsProducts = new HorizontalProductList(topAdsProductItemList);
        topAdsProducts.setType(TkpdState.RecyclerView.VIEW_TOP_ADS);
        return topAdsProducts;
    }

    @NonNull
    private com.tokopedia.core.network.entity.topads.TopAds convertDomainTopAdsToViewModel
            (TopAds domainTopAds) {

        com.tokopedia.core.network.entity.topads.TopAds topAds
                = new com.tokopedia.core.network.entity.topads.TopAds();
        topAds.setAdRefKey(domainTopAds.getAdRefKey());
        topAds.setId(domainTopAds.getId());
        topAds.setProductClickUrl(domainTopAds.getProductClickUrl());
        topAds.setRedirect(domainTopAds.getRedirect());
        topAds.setShopClickUrl(domainTopAds.getShopClickUrl());
        topAds.setStickerId(domainTopAds.getStickerId());
        topAds.setStickerImage(domainTopAds.getStickerImage());
        return topAds;
    }


    @NonNull
    private List<ProductItem> getProductFeedItemsListFromFeed(List<ProductFeed> productFeeds) {
        List<ProductItem> productItemList = new ArrayList<>();
        for (ProductFeed domainProductFeed : productFeeds) {
            ProductItem productItem = getProductItemFromDomainFeed(domainProductFeed);
            productItem.setIsTopAds(false);
            productItem.setIsWishlist(false);
            productItemList.add(productItem);
        }
        return productItemList;
    }


    @NonNull
    private List<ProductItem> getHistoryProductItems(List<ProductFeed> domainHistoryProductList) {
        List<ProductItem> historyProductListItem = new ArrayList<>();
        for (ProductFeed productFeed : domainHistoryProductList) {
            ProductItem productItem = getProductItemFromDomainFeed(productFeed);
            productItem.setIsTopAds(false);
            productItem.setIsWishlist(false);
            historyProductListItem.add(productItem);
        }
        return historyProductListItem;
    }

    @NonNull
    private ProductItem getProductItemFromDomainFeed(ProductFeed productFeed) {
        ProductItem productItem = new ProductItem();
        productItem.setBadges(convertDomainBadgeToViewModelBadge(productFeed.getBadges()));
        productItem.setFree_return(productFeed.getFreeReturn());
        productItem.setId(productFeed.getId());
        productItem.setImgUri(productFeed.getImgUrl());
        productItem.setIsAvailable(true);
        productItem.setIsGold(productFeed.getShop().getGoldStatus());
        productItem.setLabels(convertDomainLabelToViewModelLabel(productFeed.getLabels()));
        productItem.setLuckyShop(productFeed.getShop().getLucky());
        productItem.setName(productFeed.getName());

        productItem.setPreorder(productFeed.getPreorder());
        productItem.setPrice(productFeed.getPrice());
        productItem.setWholesale(productFeed.getWholesale());
        productItem.setShop(productFeed.getShop().getName());
        productItem.setShopLocation(productFeed.getShop().getLocation());

        return productItem;
    }

    private List<com.tokopedia.core.var.Label> convertDomainLabelToViewModelLabel
            (List<Label> domainLabels) {

        List<com.tokopedia.core.var.Label> results = new ArrayList<>();
        for (Label domainLabel : domainLabels) {
            com.tokopedia.core.var.Label label = new com.tokopedia.core.var.Label();
            label.setTitle(domainLabel.getTitle());
            label.setColor(domainLabel.getColor());
            results.add(label);
        }
        return results;
    }

    private List<Badge> convertDomainBadgeToViewModelBadge(
            List<com.tokopedia.tkpd.home.feed.domain.model.Badge> domainBadges) {

        List<Badge> badgeList = new ArrayList<>();
        for (com.tokopedia.tkpd.home.feed.domain.model.Badge badgeDomain : domainBadges) {
            Badge badge = new Badge();
            badge.setImageUrl(badgeDomain.getImageUrl());
            badge.setTitle(badgeDomain.getTitle());
            badge.setImgUrl(badgeDomain.getImageUrl());
            badgeList.add(badge);
        }
        return badgeList;
    }

    private PagingHandler.PagingHandlerModel prepareFeedPaging(Feed feed) {
        PagingHandler.PagingHandlerModel pager = new PagingHandler.PagingHandlerModel();
        if (feed != null && feed.getPaging() != null) {
            if (feed.getPaging().getStartIndex() <= 0) {
                String EmptyUriNext = "0";
                pager.setUriNext(EmptyUriNext);
            } else {
                pager.setUriNext(feed.getPaging().getUriNext());
                pager.setUriPrevious(feed.getPaging().getUriPrevious());
                pager.setStartIndex(feed.getPaging().getStartIndex());
            }
        }
        return pager;
    }

    public void setData(List<RecyclerViewItem> data) {
        this.data = data;
    }
}
