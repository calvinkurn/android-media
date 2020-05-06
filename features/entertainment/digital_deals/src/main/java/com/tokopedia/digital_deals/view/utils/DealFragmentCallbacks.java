package com.tokopedia.digital_deals.view.utils;

import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.digital_deals.view.model.Outlet;

import java.util.List;

public interface DealFragmentCallbacks {

    void replaceFragment(List<Outlet> outlets, int flag);

    void replaceFragment(DealsDetailsResponse detailsViewModel, int flag);

    void replaceFragment(String text, String toolBarText, int flag);

    List<Outlet> getOutlets();

    DealsDetailsResponse getDealDetails();


}
