package com.tokopedia.autocomplete.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.autocomplete.viewmodel.AutoCompleteSearch;
import com.tokopedia.autocomplete.viewmodel.CategorySearch;
import com.tokopedia.autocomplete.viewmodel.DigitalSearch;
import com.tokopedia.autocomplete.viewmodel.HotlistSearch;
import com.tokopedia.autocomplete.viewmodel.InCategorySearch;
import com.tokopedia.autocomplete.viewmodel.PopularSearch;
import com.tokopedia.autocomplete.viewmodel.ProfileSearch;
import com.tokopedia.autocomplete.viewmodel.RecentSearch;
import com.tokopedia.autocomplete.viewmodel.RecentViewSearch;
import com.tokopedia.autocomplete.viewmodel.ShopSearch;
import com.tokopedia.autocomplete.viewmodel.TitleSearch;
import com.tokopedia.autocomplete.viewmodel.TopProfileSearch;

/**
 * @author erry on 14/02/17.
 */

public interface SearchTypeFactory {

    int type(TitleSearch viewModel);

    int type(DigitalSearch viewModel);

    int type(CategorySearch viewModel);

    int type(InCategorySearch viewModel);

    int type(PopularSearch viewModel);

    int type(RecentSearch viewModel);

    int type(ShopSearch viewModel);

    int type(AutoCompleteSearch viewModel);

    int type(RecentViewSearch viewModel);

    int type(HotlistSearch viewModel);

    int type(ProfileSearch viewModel);

    int type(TopProfileSearch viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}