package com.tokopedia.instantdebitbca.data.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.bca.xco.widget.BCAEditXCOWidget;
import com.bca.xco.widget.XCOEnum;
import com.tokopedia.instantdebitbca.data.domain.NotifyDebitRegisterBcaUseCase;
import com.tokopedia.network.utils.AuthUtil;

import java.util.HashMap;
import java.util.Map;

public class EditLimitFragment extends InstantDebitBcaFragment {

    private String xcoid;
    private BCAEditXCOWidget widgetBca;

    public static Fragment newInstance(Context context, String callbackUrl, String xcoid) {
        InstantDebitBcaFragment fragment = new EditLimitFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BcaEditLimitActivity.CALLBACK_URL, callbackUrl);
        bundle.putString(BcaEditLimitActivity.XCOID, xcoid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        xcoid = getArguments().getString(BcaEditLimitActivity.XCOID);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void createAndSetBcaWidget(){
        widgetBca = new BCAEditXCOWidget(getActivity(), XCOEnum.ENVIRONMENT.DEV);
        widgetBca.setListener(this);
        layoutWidget.addView(widgetBca);
    }

    @Override
    public void openWidgetBca(String accessToken) {
        widgetBca.openWidget(accessToken, AuthUtil.KEY.API_KEY_INSTANT_DEBIT_BCA, AuthUtil.KEY.API_SEED_INSTANT_DEBIT_BCA,
                userSession.getUserId(), AuthUtil.KEY.INSTANT_DEBIT_BCA_MERCHANT_ID, xcoid);
    }

    @Override
    public void onBCASuccess(String xcoID, String credentialType, String credentialNo, String maxLimit) {
        Map<String, String> mapCardData = new HashMap<>();
        mapCardData.put(NotifyDebitRegisterBcaUseCase.XCOID, xcoID);
        mapCardData.put(NotifyDebitRegisterBcaUseCase.MAX_LIMIT, maxLimit);
        String debitData = convertObjToJsonString(mapCardData);
        presenter.notifyDebitRegisterBca(debitData, "");
    }

}
