package com.tokopedia.contactus.orderquery.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.contactus.R;

/**
 * Created by baghira on 01/05/18.
 */

public class PopularFiveArticleFragment extends BaseDaggerFragment {


    public static final String KEY_CONTENT = "KEY_CONTENT";
    private WebView webView;

    public static Fragment newInstance(String slug) {
        Bundle args = new Bundle();
        PopularFiveArticleFragment fragment = new PopularFiveArticleFragment();
        args.putSerializable(KEY_CONTENT, slug);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.top_five_popular_layout, container, false);
        webView = view.findViewById(R.id.popular_tv);
        setContent((String) getArguments().getSerializable(KEY_CONTENT));
        return view;

    }

    public void setContent(String content) {
        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }

    @Override
    protected void initInjector() {

    }
}
