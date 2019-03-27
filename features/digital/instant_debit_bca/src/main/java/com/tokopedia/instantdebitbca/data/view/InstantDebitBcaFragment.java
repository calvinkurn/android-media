package com.tokopedia.instantdebitbca.data.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bca.xco.widget.BCARegistrasiXCOWidget;
import com.bca.xco.widget.BCAXCOListener;
import com.bca.xco.widget.XCOEnum;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.instantdebitbca.R;
import com.tokopedia.instantdebitbca.data.di.InstantDebitBcaComponent;
import com.tokopedia.instantdebitbca.data.view.utils.InstantDebitBcaInstance;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
public class InstantDebitBcaFragment extends BaseDaggerFragment implements BCAXCOListener, InstantDebitBcaContract.View {

    @Inject
    InstantDebitBcaPresenter presenter;
    @Inject
    UserSessionInterface userSession;

    private RelativeLayout layoutWidget;
    private BCARegistrasiXCOWidget widgetBca;
    private String applinkUrl;

    public static Fragment newInstance(Context context, String callbackUrl) {
        InstantDebitBcaFragment fragment = new InstantDebitBcaFragment();
        Bundle bundle = new Bundle();
        bundle.putString("CALLBACK_URL", callbackUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instant_debit_bca, container, false);
        layoutWidget = view.findViewById(R.id.layoutWidget);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        applinkUrl = getArguments().getString("CALLBACK_URL");
        widgetBca = new BCARegistrasiXCOWidget(getActivity(), XCOEnum.ENVIRONMENT.DEV);
        widgetBca.setListener(this);
        layoutWidget.addView(widgetBca);
        presenter.getAccessTokenBca();
    }

    @Override
    protected void initInjector() {
        GraphqlClient.init(getActivity());
        InstantDebitBcaComponent instantDebitBcaComponent = InstantDebitBcaInstance.getComponent(getActivity().getApplication());
        instantDebitBcaComponent.inject(this);
        presenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void openWidgetBca(String accessToken) {
        widgetBca.openWidget(accessToken, "f333e9e6-4de0-46ec-aa6f-5683afd56bc0", "7a272369-4c29-44a6-ab84-94b120298a35",
                userSession.getUserId(), "61017");
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), throwable));
    }

    @Override
    public void onBCASuccess(String s, String s1, String s2, String s3) {
        if (RouteManager.isSupportApplink(getActivity(), applinkUrl)) {
            Intent intentRegisteredApplink = RouteManager.getIntent(getActivity(), applinkUrl);
            startActivity(intentRegisteredApplink);
            getActivity().finish();
        }
    }

    @Override
    public void onBCATokenExpired(String s) {

    }

    @Override
    public void onBCARegistered(String s) {

    }

    @Override
    public void onBCACloseWidget() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
