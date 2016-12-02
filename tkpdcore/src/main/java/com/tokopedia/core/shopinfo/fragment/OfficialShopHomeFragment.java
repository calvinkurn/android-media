package com.tokopedia.core.shopinfo.fragment;

/**
 * Created by nakama on 02/12/16.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Erry Suorayogi on 18/11/16.
 */

public class OfficialShopHomeFragment extends Fragment {

    public static final String SHOP_URL = "SHOP_URL";

    public static OfficialShopHomeFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString(SHOP_URL, url);
        OfficialShopHomeFragment fragment = new OfficialShopHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R2.id.webview)
    WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_official_shop_home, container, false);
        ButterKnife.bind(this, parentView);
        return parentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String url = getArguments().getString(SHOP_URL);
        webView.loadUrl(url.replace("www", "m"));
    }
}