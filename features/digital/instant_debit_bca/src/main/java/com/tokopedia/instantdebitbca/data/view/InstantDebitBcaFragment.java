package com.tokopedia.instantdebitbca.data.view;

import android.content.Context;
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
import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.instantdebitbca.R;
import com.tokopedia.instantdebitbca.data.di.InstantDebitBcaComponent;
import com.tokopedia.instantdebitbca.data.domain.NotifyDebitRegisterBcaUseCase;
import com.tokopedia.instantdebitbca.data.view.utils.DeviceUtil;
import com.tokopedia.instantdebitbca.data.view.utils.InstantDebitBcaInstance;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.user.session.UserSessionInterface;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
public class InstantDebitBcaFragment extends BaseDaggerFragment implements InstantDebitBcaContract.View, BCAXCOListener {

    @Inject
    InstantDebitBcaPresenter presenter;
    @Inject
    UserSessionInterface userSession;

    protected RelativeLayout layoutWidget;
    private BCARegistrasiXCOWidget widgetBca;
    private ActionListener listener;
    private String applinkUrl;

    public static Fragment newInstance(Context context, String callbackUrl) {
        InstantDebitBcaFragment fragment = new InstantDebitBcaFragment();
        Bundle bundle = new Bundle();
        bundle.putString(InstantDebitBcaActivity.CALLBACK_URL, callbackUrl);
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
        applinkUrl = getArguments().getString(InstantDebitBcaActivity.CALLBACK_URL);
        presenter.getAccessTokenBca();
        createAndSetBcaWidget();
    }

    @Override
    public void createAndSetBcaWidget(){
        widgetBca = new BCARegistrasiXCOWidget(getActivity(), XCOEnum.ENVIRONMENT.DEV);
        widgetBca.setListener(this);
        layoutWidget.addView(widgetBca);
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
        widgetBca.openWidget(accessToken, AuthUtil.KEY.API_KEY_INSTANT_DEBIT_BCA, AuthUtil.KEY.API_SEED_INSTANT_DEBIT_BCA,
                userSession.getUserId(), AuthUtil.KEY.INSTANT_DEBIT_BCA_MERCHANT_ID);
    }

    @Override
    public void showErrorMessage(Throwable throwable) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), ErrorHandler.getErrorMessage(getActivity(), throwable));
    }

    private String getDeviceId() {
        Map<String, String> deviceMap = new HashMap<>();
        deviceMap.put(NotifyDebitRegisterBcaUseCase.USER_AGENT, DeviceUtil.getUserAgent());
        deviceMap.put(NotifyDebitRegisterBcaUseCase.IP_ADDRESS, DeviceUtil.getLocalIpAddress());
        return convertObjToJsonString(deviceMap);
    }

    private String convertObjToJsonString(Object obj) {
        JSONObject jsonObj = new JSONObject();
        Gson gsonObj = new Gson();
        String data = gsonObj.toJson(obj);
        data.replace("\"", "\\\"");
        return data;
    }

    @Override
    public void onBCASuccess(String xcoID, String credentialType, String credentialNo, String maxLimit) {
        Map<String, String> mapCardData = new HashMap<>();
        mapCardData.put(NotifyDebitRegisterBcaUseCase.XCOID, xcoID);
        mapCardData.put(NotifyDebitRegisterBcaUseCase.CREDENTIAL_TYPE, credentialType);
        mapCardData.put(NotifyDebitRegisterBcaUseCase.CREDENTIAL_NO, credentialNo);
        mapCardData.put(NotifyDebitRegisterBcaUseCase.MAX_LIMIT, maxLimit);
        String debitData = convertObjToJsonString(mapCardData);

        presenter.notifyDebitRegisterBca(debitData, getDeviceId());
    }

    @Override
    public void onBCATokenExpired(String tokenStatus) {

    }

    @Override
    public void onBCARegistered(String xcoID) {

    }

    @Override
    public void onBCACloseWidget() {

    }

    @Override
    public void redirectPageAfterRegisterBca() {
        listener.redirectPage(applinkUrl);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    protected void onAttachActivity(Context context) {
        listener = (ActionListener) context;
    }

    public interface ActionListener {
        void redirectPage(String applinkUrl);
    }
}
