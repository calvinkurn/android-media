package com.tokopedia.reksadana.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.reksadana.R;
import com.tokopedia.reksadana.di.DaggerReksaDanaComponent;
import com.tokopedia.reksadana.di.ReksaDanaComponent;
import com.tokopedia.reksadana.view.activities.BuySellTabActivity;
import com.tokopedia.reksadana.view.activities.ReksaDanaHomeActivity;
import com.tokopedia.reksadana.view.activities.TxListActivity;
import com.tokopedia.reksadana.view.presenter.DashBoardContract;
import com.tokopedia.reksadana.view.presenter.DashBoardPresenter;

import javax.inject.Inject;

public class DashBoardFragment extends BaseDaggerFragment implements DashBoardContract.View {
    private static final String BUY_TAB = "BUY";
    private static final String SELL_TAB = "SELL";
    @Inject
    DashBoardPresenter presenter;
    ReksaDanaComponent reksaDanaComponent;
    TextView registerBtn;
    LinearLayout buyTab;
    LinearLayout sellTab;
    LinearLayout txListTab;

    public static Fragment createInstance() {
        return new DashBoardFragment();
    }

    @Override
    protected void initInjector() {
        reksaDanaComponent = DaggerReksaDanaComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        reksaDanaComponent.inject(this);
        GraphqlClient.init(getActivity());
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dash_board_initial, container, false);
        presenter.attachView(this);
        registerBtn = view.findViewById(R.id.register_button);
        buyTab = view.findViewById(R.id.buy_tab);
        sellTab = view.findViewById(R.id.sell_tab);
        txListTab = view.findViewById(R.id.transact_list_tab);
        presenter.getData();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setClickListeners();
    }

    private void setClickListeners() {
        registerBtn.setOnClickListener(view -> ((ReksaDanaHomeActivity) getActivity()).navigateToRegistration());
        buyTab.setOnClickListener(view -> startActivity(BuySellTabActivity.createInstance(getActivity(), BUY_TAB)));
        sellTab.setOnClickListener(view -> startActivity(BuySellTabActivity.createInstance(getActivity(), SELL_TAB)));
        txListTab.setOnClickListener(view -> startActivity(TxListActivity.createInstance(getActivity())));
    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }
}
