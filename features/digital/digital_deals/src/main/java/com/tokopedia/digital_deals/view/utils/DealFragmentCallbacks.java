package com.tokopedia.digital_deals.view.utils;

import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.OutletViewModel;

import java.util.List;

public interface DealFragmentCallbacks {

    void replaceFragment(List<OutletViewModel> outlets, int flag);

    void replaceFragment(CategoryItemsViewModel categoryItemsViewModel, int flag);

    List<OutletViewModel> getOutlets();

    CategoryItemsViewModel getDealDetails();
}
