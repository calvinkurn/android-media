package com.tokopedia.search.result.presentation.mapper;

import android.text.TextUtils;

import com.tokopedia.search.result.domain.model.SearchShopModel;
import com.tokopedia.search.result.presentation.model.ShopViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShopViewModelMapper {

    public ShopViewModel convertToShopViewModel(SearchShopModel searchShopModel) {
        ShopViewModel viewModel = new ShopViewModel();

        List<ShopViewModel.ShopViewItem> shopItemList = new ArrayList<>();
        for (SearchShopModel.ShopItem shop : searchShopModel.shopItemList) {
            shopItemList.add(convertToShopItem(shop));
        }

        viewModel.setShopItemList(shopItemList);
        viewModel.setHasNextPage(!TextUtils.isEmpty(searchShopModel.paging.uriNext));
        viewModel.setQuery(searchShopModel.query);
        return viewModel;
    }

    private ShopViewModel.ShopViewItem convertToShopItem(SearchShopModel.ShopItem shop) {
        ShopViewModel.ShopViewItem shopItem = new ShopViewModel.ShopViewItem();
        shopItem.setShopGoldShop(shop.shopGoldShop);
        shopItem.setShopDescription(shop.shopDescription);
        shopItem.setShopLucky(shop.shopLucky);
        shopItem.setShopId(shop.shopId);
        shopItem.setReputationScore(shop.reputationScore);
        shopItem.setShopDomain(shop.shopDomain);
        shopItem.setShopImage(shop.shopImage);
        shopItem.setShopTotalFavorite(shop.shopTotalFavorite);
        shopItem.setShopTotalTransaction(shop.shopTotalTransaction);
        shopItem.setShopRateAccuracy(shop.shopRateAccuracy);
        shopItem.setShopImage300(shop.shopImage300);
        shopItem.setShopName(shop.shopName);
        shopItem.setShopIsImg(shop.shopIsImg);
        shopItem.setShopLocation(shop.shopLocation);
        shopItem.setReputationImageUri(shop.reputationImageUri);
        shopItem.setShopRateService(shop.shopRateService);
        shopItem.setShopIsOwner(shop.shopIsOwner);
        shopItem.setShopUrl(shop.shopUrl);
        shopItem.setShopRateSpeed(shop.shopRateSpeed);
        shopItem.setShopTagLine(shop.shopTagLine);
        shopItem.setShopStatus(shop.shopStatus);
        shopItem.setOfficial(shop.isOfficial);
        shopItem.setFavorited(shop.isFavorited);

        List<String> productImages = new ArrayList<>(shop.productImages);
        shopItem.setProductImages(productImages);

        return shopItem;
    }
}
