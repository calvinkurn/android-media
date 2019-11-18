package com.tokopedia.imagesearch.search.fragment.product.adapter.typefactory;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.imagesearch.domain.viewmodel.ProductItem;
import com.tokopedia.imagesearch.domain.viewmodel.CategoryFilterModel;

public interface ImageProductListTypeFactory extends AdapterTypeFactory {
    int type(ProductItem productItem);
    int type(CategoryFilterModel categoryFilterModel);
}
