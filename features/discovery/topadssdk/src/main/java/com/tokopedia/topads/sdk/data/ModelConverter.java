package com.tokopedia.topads.sdk.data;

import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductBigViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductCarouselListViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ProductListViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ShopGridViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.ShopListViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feed.ProductFeedViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feed.ShopFeedViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feednew.ProductFeedNewViewModel;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.feednew.ShopFeedNewViewModel;

import java.util.List;

/**
 * @author by errysuprayogi on 4/14/17.
 */

public class ModelConverter {

    public static ProductBigViewModel convertToProductBigViewModel(Data data) {
        ProductBigViewModel viewModel = new ProductBigViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    public static ProductGridViewModel convertToProductGridViewModel(Data data) {
        ProductGridViewModel viewModel = new ProductGridViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    public static ProductListViewModel convertToProductListViewModel(Data data) {
        ProductListViewModel viewModel = new ProductListViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    public static ShopGridViewModel convertToShopGridViewModel(Data data) {
        ShopGridViewModel viewModel = new ShopGridViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    public static ShopListViewModel convertToShopListViewModel(Data data) {
        ShopListViewModel viewModel = new ShopListViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    public static ShopFeedViewModel convertToShopFeedViewModel(Data data, DisplayMode displayMode) {
        ShopFeedViewModel viewModel = new ShopFeedViewModel();
        viewModel.setData(data);
        viewModel.setDisplayMode(displayMode);
        return viewModel;
    }

    public static ProductFeedViewModel convertToProductFeedViewModel(Data data) {
        ProductFeedViewModel viewModel = new ProductFeedViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    public static ShopFeedNewViewModel convertToShopFeedNewViewModel(Data data) {
        ShopFeedNewViewModel viewModel = new ShopFeedNewViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    public static ProductFeedNewViewModel convertToProductFeedNewViewModel(Data data) {
        ProductFeedNewViewModel viewModel = new ProductFeedNewViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    public static void convertList(List<Item> list, DisplayMode displayMode) {
        for (int i = 0; i < list.size(); i++) {
            Item visitable = list.get(i);
            if (displayMode == DisplayMode.GRID && visitable instanceof ProductListViewModel) {
                list.set(i, ModelConverter.convertToProductGridViewModel(((ProductListViewModel) visitable).getData()));
            } else if (displayMode == DisplayMode.GRID && visitable instanceof ShopListViewModel) {
                list.set(i, ModelConverter.convertToShopGridViewModel(((ShopListViewModel) visitable).getData()));
            } else if (displayMode == DisplayMode.LIST && visitable instanceof ProductBigViewModel) {
                list.set(i, ModelConverter.convertToProductListViewModel(((ProductBigViewModel) visitable).getData()));
            } else if (displayMode == DisplayMode.LIST && visitable instanceof ShopGridViewModel) {
                list.set(i, ModelConverter.convertToShopListViewModel(((ShopGridViewModel) visitable).getData()));
            } else if (displayMode == DisplayMode.FEED && visitable instanceof ShopGridViewModel
                    || displayMode == DisplayMode.FEED_EMPTY && visitable instanceof ShopGridViewModel) {
                list.set(i, ModelConverter.convertToShopFeedViewModel(((ShopGridViewModel)
                        visitable).getData(), displayMode));
            } else if (displayMode == DisplayMode.BIG && visitable instanceof ProductGridViewModel) {
                list.set(i, ModelConverter.convertToProductBigViewModel(((ProductGridViewModel)
                        visitable).getData()));
            }
        }
    }

    public static Item convertToCarouselListViewModel(Data data) {
        ProductCarouselListViewModel viewModel = new ProductCarouselListViewModel();
        viewModel.setData(data);
        return viewModel;
    }

    public static Item convertProductData(Data d, DisplayMode mode) {
        switch (mode) {
            case GRID:
                return convertToProductGridViewModel(d);
            case LIST:
                return convertToProductListViewModel(d);
            case BIG:
                return convertToProductBigViewModel(d);
            case FEED:
                return convertToProductFeedViewModel(d);
            default:
                return convertToProductGridViewModel(d);
        }
    }

    public static Item convertShopData(Data d, DisplayMode mode) {
        switch (mode) {
            case GRID:
                return convertToShopGridViewModel(d);
            case LIST:
                return convertToShopListViewModel(d);
            case BIG:
                return convertToProductBigViewModel(d);
            case FEED:
                return convertToShopFeedViewModel(d, DisplayMode.FEED);
            default:
                return convertToShopGridViewModel(d);
        }
    }
}
