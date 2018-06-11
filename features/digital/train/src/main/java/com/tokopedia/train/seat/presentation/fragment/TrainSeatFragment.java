package com.tokopedia.train.seat.presentation.fragment;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.train.seat.di.TrainSeatComponent;
import com.tokopedia.train.seat.presentation.presenter.TrainSeatPresenter;

import javax.inject.Inject;

public class TrainSeatFragment extends BaseDaggerFragment {

    @Inject
    TrainSeatPresenter presenter;

    public static Fragment newInstance() {
        return new TrainSeatFragment();
    }

    @Override
    protected void initInjector() {
        getComponent(TrainSeatComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
