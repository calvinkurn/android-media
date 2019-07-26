package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;
import com.tokopedia.home.beranda.presentation.view.viewmodel.FeedTabModel;

import java.util.List;

/**
 * Created by henrypriyono on 31/01/18.
 */

public class HomeRecommendationFeedViewModel implements Visitable<HomeTypeFactory> {

    private List<FeedTabModel> feedTabModel;

    private boolean isNewData = true;

    public void setFeedTabModel(List<FeedTabModel> feedTabModel){
        this.feedTabModel = feedTabModel;
    }

    public List<FeedTabModel> getFeedTabModel() {
        return feedTabModel;
    }

    public void setNewData(boolean newData) {
        isNewData = newData;
    }

    public boolean isNewData() {
        return isNewData;
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
