package com.tokopedia.challenges.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.analytics.ChallengesGaAnalyticsTracker;
import com.tokopedia.challenges.view.utils.MarkdownProcessor;

import javax.inject.Inject;

public class TncBottomSheetFragment extends TkpdBaseV4Fragment {


    private Toolbar toolbar;
    private WebView webView;
    public final static String TOOLBAR_TITLE = "TOOLBAR_TITLE";
    public final static String TEXT = "TEXT";
    private String toolBarTitle;
    private String text;
    private final static String SCREEN_NAME = "challenges/tnc";
    public ChallengesGaAnalyticsTracker analytics;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new TncBottomSheetFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tnc_layout, container, false);

        if (getArguments() != null) {
            text = getArguments().getString(TEXT);
            toolBarTitle = getArguments().getString(TOOLBAR_TITLE);
            toolbar = view.findViewById(R.id.toolbar);
            webView = view.findViewById(R.id.tnc_webview);
            ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_ticker_action_close_12dp));
            if (toolBarTitle != null)
                toolbar.setTitle(toolBarTitle);

            MarkdownProcessor m = new MarkdownProcessor();
            String html = m.markdown(text);
            webView.loadDataWithBaseURL("fake://", html, "text/html", "UTF-8", null);
        }
        analytics = new ChallengesGaAnalyticsTracker(getActivity());
        return view;
    }

    @Override
    public void onResume() {
        analytics.sendScreenEvent(getActivity(), SCREEN_NAME);
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    protected String getScreenName() {
        return null;
    }


}
