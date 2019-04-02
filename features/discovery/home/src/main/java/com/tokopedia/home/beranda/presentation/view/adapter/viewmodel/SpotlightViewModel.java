package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.domain.model.Spotlight;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.List;

public class SpotlightViewModel implements Visitable<HomeTypeFactory> {
    private List<SpotlightItemViewModel> spotlightItems;

    public SpotlightViewModel(List<SpotlightItemViewModel> spotlightItems) {
        this.spotlightItems = spotlightItems;
    }

    public List<SpotlightItemViewModel> getSpotlightItems() {
        return spotlightItems;
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
