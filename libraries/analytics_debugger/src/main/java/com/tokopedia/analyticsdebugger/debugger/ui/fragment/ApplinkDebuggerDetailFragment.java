package com.tokopedia.analyticsdebugger.debugger.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.analyticsdebugger.R;
import com.tokopedia.analyticsdebugger.debugger.ui.model.ApplinkDebuggerViewModel;

import static com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst.DATA_DETAIL;

public class ApplinkDebuggerDetailFragment extends TkpdBaseV4Fragment {
    private TextView textApplink;
    private TextView textTimestamp;
    private TextView textTraces;
    private ApplinkDebuggerViewModel viewModel;

    public static Fragment newInstance(Bundle extras) {
        ApplinkDebuggerDetailFragment fragment = new ApplinkDebuggerDetailFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applink_debugger_detail, container, false);
        textApplink = view.findViewById(R.id.applink_text_name);
        textTimestamp = view.findViewById(R.id.applink_text_timestamp);
        textTraces = view.findViewById(R.id.applink_text_traces);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        viewModel = getArguments().getParcelable(DATA_DETAIL);
        if (viewModel != null) {
            textApplink.setText(viewModel.getApplink());
            textTimestamp.setText(viewModel.getTimestamp());
            textTraces.setText(viewModel.getTrace());
        }
    }

    @Override
    protected String getScreenName() {
        return ApplinkDebuggerDetailFragment.class.getSimpleName();
    }
}
