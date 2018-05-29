package com.tokopedia.digital_deals.view.utils;

import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.OutletViewModel;

import java.util.List;

public interface DealFragmentCallbacks {

    void replaceFragment(List<OutletViewModel> outlets, int flag);

    void replaceFragment(DealsDetailsViewModel detailsViewModel, int flag);

    List<OutletViewModel> getOutlets();

    DealsDetailsViewModel getDealDetails();
}
