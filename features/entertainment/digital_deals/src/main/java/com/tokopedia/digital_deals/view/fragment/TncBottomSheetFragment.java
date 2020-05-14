package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.digital_deals.di.DealsComponent;

public class TncBottomSheetFragment extends BaseDaggerFragment {


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
        View view = inflater.inflate(com.tokopedia.digital_deals.R.layout.fragment_tandc_bottomsheet, container, false);

        if (getArguments() != null) {
            text = getArguments().getString(TEXT);
            toolBarTitle = getArguments().getString(TOOLBAR_TITLE);
            setViewIds(view);
            if (toolBarTitle != null)
                toolbar.setTitle(toolBarTitle);
            setExpandableItemText(text);
        }
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void setViewIds(View view) {
        toolbar = view.findViewById(com.tokopedia.digital_deals.R.id.toolbar);
        textView = view.findViewById(com.tokopedia.digital_deals.R.id.tv_expandable_description);
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), com.tokopedia.digital_deals.R.drawable.ic_close_deals));
    }

    private void setExpandableItemText(String tnc) {
        if (!TextUtils.isEmpty(tnc)) {
            String splitArray[] = tnc.split("~");
            StringBuilder tncBuffer = new StringBuilder();

            for (String line : splitArray) {
                tncBuffer.append(" ").append("\u2022").append("  ").append(line.trim()).append("<br><br>");
            }

            textView.setText(Html.fromHtml(tncBuffer.toString()));
        } else {

        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }


    @Override
    protected void initInjector() {
        getComponent(DealsComponent.class).inject(this);
    }
}
