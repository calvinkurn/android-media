package com.tokopedia.search.result.presentation.view.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.search.result.presentation.model.CatalogHeaderViewModel;
import com.tokopedia.search.result.presentation.model.CatalogViewModel;

public interface CatalogListTypeFactory extends SearchSectionTypeFactory {

    int type(CatalogViewModel viewModel);

    int type(CatalogHeaderViewModel headerViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
