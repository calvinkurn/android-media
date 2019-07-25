package com.tokopedia.search.result.presentation.view.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.search.result.presentation.model.HeaderViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.search.result.presentation.model.RelatedSearchViewModel;
import com.tokopedia.search.result.presentation.model.TopAdsViewModel;

public interface ProductListTypeFactory extends SearchSectionTypeFactory {
    int type(ProductItemViewModel productItem);

    int type(HeaderViewModel headerViewModel);

    int type(TopAdsViewModel topAdsViewModel);

    int type(RelatedSearchViewModel relatedSearchModel);

    AbstractViewHolder createViewHolder(View view, int type);
}
