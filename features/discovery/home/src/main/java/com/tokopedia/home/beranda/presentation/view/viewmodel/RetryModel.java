package com.tokopedia.home.beranda.presentation.view.viewmodel;


import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory;

public class RetryModel implements Visitable<HomeAdapterFactory> {

    @Override
    public int type(HomeAdapterFactory adapterFactory) {
        return adapterFactory.type(this);
    }

}
