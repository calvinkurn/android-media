package com.tokopedia.core;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tokopedia.core.topads.WebViewTopAdsActivity;
import com.tokopedia.core.topads.WebViewTopAdsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Steven on 24/11/2015.
 * modified on 18/03/2016
 */
public class InfoTopAds extends DialogFragment{

    public static final String TAG = "DialogTopAds";
    private Unbinder unbinder;

    public static InfoTopAds newInstance(String source) {

        Bundle args = new Bundle();
        args.putString(WebViewTopAdsFragment.SOURCE_EXTRA, source);
        InfoTopAds fragment = new InfoTopAds();
        fragment.setArguments(args);
        return fragment;
    }
    @BindView(R2.id.readMore) TextView readMore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.dialog_info_topads, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
     public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R2.id.readMore)
    public void readMore(){
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TkpdUrl.INFO_TOPADS));
        Intent intent = new Intent(getActivity(), WebViewTopAdsActivity.class);
        intent.putExtra(WebViewTopAdsFragment.SOURCE_EXTRA, getArguments().getString(WebViewTopAdsFragment.SOURCE_EXTRA));
        getActivity().startActivity(intent);
    }
}
