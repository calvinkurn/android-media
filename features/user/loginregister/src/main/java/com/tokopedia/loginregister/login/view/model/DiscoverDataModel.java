package com.tokopedia.loginregister.login.view.model;


import com.tokopedia.loginregister.discover.data.DiscoverItemDataModel;

import java.util.ArrayList;

/**
 * @author by nisie on 10/10/17.
 */

public class DiscoverDataModel {
    private ArrayList<DiscoverItemDataModel> providers;
    private String urlBackground;

    public DiscoverDataModel(ArrayList<DiscoverItemDataModel> providers,
                             String urlBackground) {
        this.providers = providers;
        this.urlBackground = urlBackground;
    }

    public ArrayList<DiscoverItemDataModel> getProviders() {
        return providers;
    }

    public String getUrlBackground() {
        return urlBackground;
    }
}
