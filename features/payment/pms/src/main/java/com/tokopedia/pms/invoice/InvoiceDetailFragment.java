package com.tokopedia.pms.invoice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;
import com.tokopedia.abstraction.common.utils.network.URLGenerator;
import com.tokopedia.pms.common.Constant;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by zulfikarrahman on 7/9/18.
 */

public class InvoiceDetailFragment extends BaseWebViewFragment {

    @Override
    protected String getUrl() {
        UserSessionInterface userSession = new UserSession(getActivity());
        return URLGenerator.generateURLSessionLogin(encodeUrl(getArguments().getString(Constant.INVOICE_URL_EXTRA)), userSession.getDeviceId(), getUserIdForHeader());
    }

    private static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Nullable
    @Override
    protected String getUserIdForHeader() {
        UserSessionInterface userSession = new UserSession(getActivity());
        return userSession.getUserId();
    }

    @Nullable
    @Override
    protected String getAccessToken() {
        UserSessionInterface userSession = new UserSession(getActivity());
        return userSession.getAccessToken();
    }

    public static Fragment createInstance(String invoiceUrl) {
        InvoiceDetailFragment invoiceDetailFragment = new InvoiceDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.INVOICE_URL_EXTRA, invoiceUrl);
        invoiceDetailFragment.setArguments(bundle);
        return invoiceDetailFragment;
    }

    @Override
    protected void loadWeb(){
        super.loadWeb();
        webView.getSettings().setBuiltInZoomControls(false);
    }

}
