package com.tokopedia.flight.booking.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;
import com.tokopedia.abstraction.common.utils.network.URLGenerator;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

public class FlightInsuranceWebViewFragment extends BaseWebViewFragment {
    private static final String EXTRA_URL = "EXTRA_URL";
    private static final String EXTRA_UID = "EXTRA_UID";

    private UserSessionInterface userSession;

    public static FlightInsuranceWebViewFragment newInstance(String url) {
        FlightInsuranceWebViewFragment fragment = new FlightInsuranceWebViewFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = new UserSession(getActivity());
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        getComponent(FlightComponent.class).inject(this);
    }

    @Override
    protected String getUrl() {
        String url = getArguments().getString(EXTRA_URL, FlightUrl.WEB_DOMAIN);
        url = Uri.encode(url);
        return URLGenerator.generateURLSessionLogin(url, userSession.getDeviceId(), userSession.getUserId());
    }

    @Nullable
    @Override
    protected String getUserIdForHeader() {
        return userSession.getUserId();
    }

    @Nullable
    @Override
    protected String getAccessToken() {
        return userSession.getAccessToken();
    }
}
