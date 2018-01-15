package com.tokopedia.abstraction.base.view.adapter.model;


import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.Visitable;

/**
 * @author Kulomady on 1/25/17.
 */

public class LoadingModel implements Visitable<AdapterTypeFactory> {

    private boolean isFullScreen;

    public LoadingModel() {
    }

    @Override
    public int type(AdapterTypeFactory adapterTypeFactory) {
        return adapterTypeFactory.type(this);
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }
}
