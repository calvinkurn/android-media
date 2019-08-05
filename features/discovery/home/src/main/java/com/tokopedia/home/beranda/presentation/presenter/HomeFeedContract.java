package com.tokopedia.home.beranda.presentation.presenter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;
import com.tokopedia.trackingoptimizer.TrackingQueue;

public class HomeFeedContract {
    public interface Presenter extends CustomerPresenter<View> {

    }

    public interface View extends BaseListViewListener<Visitable<HomeFeedTypeFactory>> {
        void onProductImpression(HomeFeedViewModel homeFeedViewModel, int position);
        void onProductClick(HomeFeedViewModel homeFeedViewModel, int position);

        TrackingQueue getTrackingQueue();
        String getTabName();
    }
}
