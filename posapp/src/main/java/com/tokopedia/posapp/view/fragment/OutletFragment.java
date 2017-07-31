package com.tokopedia.posapp.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.view.Outlet;
import com.tokopedia.posapp.view.di.DaggerOutletComponent;
import com.tokopedia.posapp.view.presenter.OutletPresenter;
import com.tokopedia.posapp.view.viewmodel.outlet.OutletViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletFragment extends BaseDaggerFragment implements Outlet.View {
    private static final String PARAM_ORDER_BY = "order_by";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_QUERY = "query";

    RecyclerView recyclerOutlet;

    @Inject
    OutletPresenter presenter;

    public static OutletFragment createInstance(Bundle bundle) {
        OutletFragment fragment = new OutletFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_outlet, container, false);
        recyclerOutlet = parentView.findViewById(R.id.recycler_outlet);
        presenter.attachView(this);
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RequestParams params = AuthUtil.generateRequestParamsNetwork(getContext());
        params.putString(PARAM_ORDER_BY, "1");
        params.putString(PARAM_PAGE, "1");
        params.putString(PARAM_QUERY, "");
        presenter.getOutlet(params);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerOutletComponent daggerOutletComponent =
                (DaggerOutletComponent) DaggerOutletComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerOutletComponent.inject(this);
    }

    @Override
    public void onOutletClicked(String outletId) {

    }

    @Override
    public void onSuccessGetOutlet(List<OutletViewModel> outlets) {

    }

    @Override
    public void onErrorGetOutlet(String errorMessage) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
