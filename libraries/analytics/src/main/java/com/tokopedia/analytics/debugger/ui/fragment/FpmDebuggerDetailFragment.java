package com.tokopedia.analytics.debugger.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.analytics.R;
import com.tokopedia.analytics.debugger.ui.model.FpmDebuggerViewModel;

import static com.tokopedia.analytics.debugger.AnalyticsDebuggerConst.DATA_DETAIL;

public class FpmDebuggerDetailFragment extends TkpdBaseV4Fragment {
    private TextView textName;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fpm_debugger_detail, container, false);
        textName = view.findViewById(R.id.fpm_text_name);
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
            textName.setText(viewModel.getName());
            textTimestamp.setText(viewModel.getTimestamp());
            duration.setText(String.valueOf(viewModel.getDuration()));
            metrics.setText(viewModel.getMetrics());
            txtAtrributes.setText(viewModel.getAttributes());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_analytics_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (viewModel != null) {
            if(item.getItemId() == R.id.action_share_text) {

                return true;
            } else if (item.getItemId() == R.id.action_copy_text) {

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String getScreenName() {
        return FpmDebuggerDetailFragment.class.getSimpleName();
    }
}
