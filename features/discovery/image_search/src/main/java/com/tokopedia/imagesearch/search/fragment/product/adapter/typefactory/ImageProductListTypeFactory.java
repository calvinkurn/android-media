package com.tokopedia.imagesearch.search.fragment.product.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.imagesearch.domain.viewmodel.ProductItem;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.discovery.TopAdsViewModel;

public interface ImageProductListTypeFactory extends AdapterTypeFactory {
    int type(ProductItem productItem);
}
