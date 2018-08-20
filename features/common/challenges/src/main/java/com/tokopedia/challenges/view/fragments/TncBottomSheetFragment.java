package com.tokopedia.challenges.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.challenges.R;

public class TncBottomSheetFragment extends TkpdBaseV4Fragment {


    private Toolbar toolbar;
    private TextView textView;
    public final static String TOOLBAR_TITLE = "TOOLBAR_TITLE";
    public final static String TEXT = "TEXT";
    private String toolBarTitle;
    private String text;


    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new TncBottomSheetFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tandc_bottomsheet, container, false);

        if (getArguments() != null) {
            text = getArguments().getString(TEXT);
            toolBarTitle = getArguments().getString(TOOLBAR_TITLE);
            setViewIds(view);
            if (toolBarTitle != null)
                toolbar.setTitle(toolBarTitle);
                textView.setText(Html.fromHtml(text));
        }
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void setViewIds(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        textView = view.findViewById(R.id.tv_expandable_description);
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_ticker_action_close_12dp));
    }

    @Override
    protected String getScreenName() {
        return null;
    }


}
