package com.tokopedia.imagesearch.search.fragment.product.adapter.listener;

import com.tokopedia.imagesearch.domain.viewmodel.CategoryFilterModel;

public interface CategoryFilterListener {

    boolean isCategoryFilterSelected(String categoryId);

    void onCategoryFilterSelected(CategoryFilterModel.Item item);
}