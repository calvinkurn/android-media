package com.tokopedia.analytics.debugger.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.analytics.R;
import com.tokopedia.analytics.debugger.ui.model.AnalyticsDebuggerViewModel;

import static com.tokopedia.analytics.debugger.AnalyticsDebuggerConst.DATA_DETAIL;

/**
 * @author okasurya on 5/17/18.
 */
public class AnalyticsDebuggerDetailFragment extends TkpdBaseV4Fragment {
    private TextView textName;
    private TextView textTimestamp;
    private TextView textData;

    public static Fragment newInstance(Bundle extras) {
        AnalyticsDebuggerDetailFragment fragment = new AnalyticsDebuggerDetailFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics_debugger_detail, container, false);
        textName = view.findViewById(R.id.text_name);
        textTimestamp = view.findViewById(R.id.text_timestamp);
        textData = view.findViewById(R.id.text_data);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        AnalyticsDebuggerViewModel viewModel = getArguments().getParcelable(DATA_DETAIL);
        if (viewModel != null) {
            textName.setText(viewModel.getCategory());
            textTimestamp.setText(viewModel.getTimestamp());
            textData.setText(viewModel.getData());
        }
    }

    @Override
    protected String getScreenName() {
        return AnalyticsDebuggerDetailFragment.class.getSimpleName();
    }
}
