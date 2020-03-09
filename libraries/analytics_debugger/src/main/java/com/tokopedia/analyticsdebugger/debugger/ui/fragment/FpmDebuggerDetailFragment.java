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
import com.tokopedia.analyticsdebugger.debugger.ui.model.FpmDebuggerViewModel;

import static com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst.DATA_DETAIL;

public class FpmDebuggerDetailFragment extends TkpdBaseV4Fragment {
    private TextView textTimestamp;
    private TextView duration;
    private TextView metrics;
    private TextView txtAtrributes;
    private FpmDebuggerViewModel viewModel;

    public static Fragment newInstance(Bundle extras) {
        FpmDebuggerDetailFragment fragment = new FpmDebuggerDetailFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fpm_debugger_detail, container, false);
        textTimestamp = view.findViewById(R.id.fpm_text_timestamp);
        duration = view.findViewById(R.id.fpm_text_duration);
        metrics = view.findViewById(R.id.fpm_text_metrics);
        txtAtrributes = view.findViewById(R.id.fpm_text_attributes);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        viewModel = getArguments().getParcelable(DATA_DETAIL);
        if (viewModel != null) {
            textTimestamp.setText(viewModel.getTimestamp());
            duration.setText(String.format("Duration: %dms", viewModel.getDuration()));
            metrics.setText(viewModel.getMetrics());
            txtAtrributes.setText(viewModel.getAttributes());
        }
    }

    @Override
    protected String getScreenName() {
        return FpmDebuggerDetailFragment.class.getSimpleName();
    }
}
