package com.tokopedia.search.result.presentation.view.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.search.result.presentation.model.ShopViewModel;

public interface ShopListTypeFactory extends SearchSectionTypeFactory {
    int type(ShopViewModel.ShopViewItem shopItem);

    AbstractViewHolder createViewHolder(View view, int type);
}
