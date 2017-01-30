package com.tokopedia.tkpd.home.feed.data.mapper;

import com.google.gson.Gson;
import com.tokopedia.core.network.entity.topads.TopAdsResponse;
import com.tokopedia.tkpd.home.feed.domain.model.Badge;
import com.tokopedia.tkpd.home.feed.domain.model.Label;
import com.tokopedia.tkpd.home.feed.domain.model.TopAds;
import com.tokopedia.tkpd.home.feed.domain.model.TopAdsProduct;
import com.tokopedia.tkpd.home.feed.domain.model.TopAdsShop;
import com.tokopedia.tkpd.home.feed.domain.model.WholesalePrice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author Kulomady on 12/9/16.
 */

public class TopAdsMapperResult implements Func1<Response<String>, List<TopAds>> {

    private final Gson mGson;

    public TopAdsMapperResult(Gson gson) {

        mGson = gson;
    }

    @Override
    public List<TopAds> call(Response<String> response) {
        return mappingResponse(response);
    }

    private List<TopAds> mappingResponse(Response<String> response) {

        if (response.isSuccessful() && response.body() != null) {
            TopAdsResponse topAdsResponse
                    = mGson.fromJson(response.body(), TopAdsResponse.class);
            if (topAdsResponse != null
                    && topAdsResponse.data != null && topAdsResponse.data.size() > 0) {
                return mappingValidResponse(topAdsResponse.data);
            } else {
                return mappingInvalidResponse();
            }
        } else {
            return mappingInvalidResponse();
        }

    }

    private List<TopAds> mappingInvalidResponse() {
        return new ArrayList<>();
    }

    private List<TopAds> mappingValidResponse(List<TopAdsResponse.Data> dataList) {
        List<TopAds> topAdsList = new ArrayList<>();
        for (TopAdsResponse.Data responseDataTopAds : dataList) {
            TopAds topAds = new TopAds();
            topAds.setId(responseDataTopAds.id);
            topAds.setAdRefKey(responseDataTopAds.adRefKey);
            topAds.setProductClickUrl(responseDataTopAds.productClickUrl);
            topAds.setRedirect(responseDataTopAds.redirect);
            topAds.setStickerId(responseDataTopAds.stickerId);
            topAds.setStickerImage(responseDataTopAds.stickerImage);
            topAds.setShop(getMappingShop(responseDataTopAds));
            topAds.setProduct(getMappingProduct(responseDataTopAds));
            topAds.setIsValid(true);
            topAdsList.add(topAds);
        }
        return topAdsList;
    }

    private TopAdsProduct getMappingProduct(TopAdsResponse.Data data) {
        TopAdsResponse.Data.Product productResponse = data.product;
        TopAdsProduct topAdsProduct = new TopAdsProduct();
        if (productResponse != null) {
            topAdsProduct.setId(productResponse.id);
            topAdsProduct.setName(productResponse.name);
            topAdsProduct.setPrice(data.product.priceFormat);
            topAdsProduct.setShopId((Integer.parseInt(data.shop.id)));
            topAdsProduct.setImgUrl(productResponse.image.mUrl);
            topAdsProduct.setLabels(getMappingProductLabel(productResponse.labels));
            topAdsProduct.setCategoryId(
                    productResponse.category != null ? productResponse.category.id : "");
            topAdsProduct.setCountReviewFormat(productResponse.countReviewFormat);
            topAdsProduct.setCountTalkFormat(productResponse.countTalkFormat);
            topAdsProduct.setPriceFormat(productResponse.priceFormat);
            topAdsProduct.setId(productResponse.id);

            topAdsProduct.setPreorder(productResponse.preorder);
            topAdsProduct.setWholesale(productResponse.wholesale);
            topAdsProduct.setWholesalePrice(getMappingProductWholesale(productResponse.wholesalePrice));

        }
        return topAdsProduct;

    }

    private List<WholesalePrice> getMappingProductWholesale(
            List<TopAdsResponse.Data.Product.WholesalePrice> wholesalePriceResponse) {

        List<WholesalePrice> wholesalePricesResult = new ArrayList<>();
        if (wholesalePriceResponse != null && wholesalePriceResponse.size() > 0) {
            for (TopAdsResponse.Data.Product.WholesalePrice eachWholePrice : wholesalePriceResponse) {
                WholesalePrice wholeSalePrice = new WholesalePrice();
                wholeSalePrice.setPriceFormat(eachWholePrice.priceFormat);
                wholeSalePrice.setQuantityMaxFormat(eachWholePrice.quantityMaxFormat);
                wholeSalePrice.setQuantityMinFormat(eachWholePrice.quantityMinFormat);
                wholesalePricesResult.add(wholeSalePrice);
            }
        }
        return wholesalePricesResult;
    }

    private List<Label> getMappingProductLabel(List<com.tokopedia.core.var.Label> labelsResponseList) {

        List<Label> labelList = new ArrayList<>();
        if (labelsResponseList != null) {
            for (com.tokopedia.core.var.Label labelResponse : labelsResponseList) {
                labelList.add(new Label(labelResponse.getTitle(), labelResponse.getColor()));
            }
        }
        return labelList;
    }

    private TopAdsShop getMappingShop(TopAdsResponse.Data responseDataTopAds) {
        TopAdsResponse.Data.Shop shopResponse = responseDataTopAds.shop;
        TopAdsShop topAdsShop = new TopAdsShop();
        if (shopResponse != null) {
            topAdsShop.setOwner(shopResponse.isOwner);
            topAdsShop.setLucky(shopResponse.luckyShop);
            topAdsShop.setLocation(shopResponse.location);
            topAdsShop.setGoldShop(shopResponse.goldShop);
            topAdsShop.setId(shopResponse.id);
            topAdsShop.setUri(shopResponse.uri);
            topAdsShop.setName(shopResponse.name);
            topAdsShop.setBadges(getMappingShopBadge(shopResponse));
        }
        return topAdsShop;
    }

    private List<Badge> getMappingShopBadge(TopAdsResponse.Data.Shop shopResponse) {
        List<Badge> badgeList = new ArrayList<>();
        if (shopResponse != null) {
            for (com.tokopedia.core.var.Badge badgeResponse : shopResponse.badges) {
                badgeList.add(new Badge(badgeResponse.getTitle(), badgeResponse.getImageUrl()));
            }
        }
        return badgeList;
    }


}
