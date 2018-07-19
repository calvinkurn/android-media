package com.tokopedia.paymentmanagementsystem.invoice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;
import com.tokopedia.abstraction.common.utils.network.URLGenerator;
import com.tokopedia.paymentmanagementsystem.common.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by zulfikarrahman on 7/9/18.
 */

public class InvoiceDetailFragment extends BaseWebViewFragment {

    @Override
    protected String getUrl() {
        String deviceId = "";
        if(getActivity().getApplication() instanceof AbstractionRouter){
            deviceId =  ((AbstractionRouter)getActivity().getApplication()).getSession().getDeviceId();
        }
        return URLGenerator.generateURLSessionLogin(encodeUrl(getArguments().getString(Constant.INVOICE_URL_EXTRA)), deviceId, getUserIdForHeader());
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
        if(getActivity().getApplication() instanceof AbstractionRouter){
            return ((AbstractionRouter)getActivity().getApplication()).getSession().getUserId();
        }
        return "";
    }

    @Nullable
    @Override
    protected String getAccessToken() {
        if(getActivity().getApplication() instanceof AbstractionRouter){
            return ((AbstractionRouter)getActivity().getApplication()).getSession().getAccessToken();
        }
        return "";
    }

    public static Fragment createInstance(String invoiceUrl) {
        InvoiceDetailFragment invoiceDetailFragment = new InvoiceDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.INVOICE_URL_EXTRA, invoiceUrl);
        invoiceDetailFragment.setArguments(bundle);
        return invoiceDetailFragment;
    }


}
