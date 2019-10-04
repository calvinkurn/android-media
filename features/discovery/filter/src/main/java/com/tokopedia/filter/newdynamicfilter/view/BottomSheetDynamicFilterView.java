package com.tokopedia.filter.newdynamicfilter.view;

import com.tokopedia.filter.common.data.Option;

public interface BottomSheetDynamicFilterView extends DynamicFilterView {
    boolean isSelectedCategory(Option option);
    void selectCategory(Option option, String filterTitle);
    void saveCheckedState(Option option, Boolean isChecked, String filterTitle);
    void removeSelectedOption(Option option, String filterTitle);
}
