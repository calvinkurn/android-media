package com.tokopedia.reksadana.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.reksadana.R;
import com.tokopedia.reksadana.di.DaggerReksaDanaComponent;
import com.tokopedia.reksadana.di.ReksaDanaComponent;
import com.tokopedia.reksadana.view.presenter.BuySellTxContract;
import com.tokopedia.reksadana.view.presenter.TxListPresenter;

import javax.inject.Inject;

public class TxListFragment extends BaseDaggerFragment implements BuySellTxContract.TxListView {
    @Inject
    TxListPresenter presenter;
    ReksaDanaComponent reksaDanaComponent;

    public static Fragment createInstance() {
        return new TxListFragment();
    }

    @Override
    protected void initInjector() {
        reksaDanaComponent = DaggerReksaDanaComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        reksaDanaComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tx_list_fragment, container, false);
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setClickListeners();
    }

    private void setClickListeners() {}
}
