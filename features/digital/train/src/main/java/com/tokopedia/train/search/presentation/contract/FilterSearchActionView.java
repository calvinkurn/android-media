package com.tokopedia.train.search.presentation.contract;

import com.tokopedia.train.search.presentation.model.FilterSearchData;

/**
 * Created by nabillasabbaha on 3/22/18.
 */

public interface FilterSearchActionView {

    FilterSearchData getFilterSearchData();

    void onChangeFilterSearchData(FilterSearchData filterSearchData);

    void setTitleToolbar(String titleToolbar);

    void onNameFilterSearchTrainClicked();

    void onDepartureFilterSearchTrainClicked();

    void onClassFilterSearchTrainClicked();

    void setCloseButton(boolean showCloseButton);
}
