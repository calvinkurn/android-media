package com.tokopedia.loginregister.login.view.model;


import com.tokopedia.loginregister.discover.data.DiscoverItemViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 10/10/17.
 */

public class DiscoverViewModel {
    private ArrayList<DiscoverItemViewModel> providers;
    private String urlBackground;

    public DiscoverViewModel(ArrayList<DiscoverItemViewModel> providers,
                             String urlBackground) {
        this.providers = providers;
        this.urlBackground = urlBackground;
    }

    public ArrayList<DiscoverItemViewModel> getProviders() {
        return providers;
    }

    public String getUrlBackground() {
        return urlBackground;
    }
}
