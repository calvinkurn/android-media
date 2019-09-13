package com.tokopedia.imagesearch.search.fragment.product.adapter.typefactory;

import android.view.View;

import com.tokopedia.imagesearch.domain.viewmodel.ProductItem;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.TopAdsViewModel;

public interface ImageProductListTypeFactory {
    int type(ProductItem productItem);

    int type(HeaderViewModel headerViewModel);

    int type(TopAdsViewModel topAdsViewModel);

    int type(EmptySearchModel emptySearchModel);

    int getRecyclerViewItem();

    void setRecyclerViewItem(int recyclerViewItem);
}
