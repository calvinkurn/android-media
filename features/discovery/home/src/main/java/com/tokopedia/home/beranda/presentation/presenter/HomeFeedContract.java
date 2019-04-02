package com.tokopedia.home.beranda.presentation.presenter;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;

public class HomeFeedContract {
    public interface Presenter extends CustomerPresenter<View> {

    }

    public interface View extends BaseListViewListener<HomeFeedViewModel> {

    }
}
