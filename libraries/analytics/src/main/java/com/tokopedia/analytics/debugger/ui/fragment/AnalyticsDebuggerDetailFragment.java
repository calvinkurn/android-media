package com.tokopedia.analytics.debugger.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    private AnalyticsDebuggerViewModel viewModel;

    public static Fragment newInstance(Bundle extras) {
        AnalyticsDebuggerDetailFragment fragment = new AnalyticsDebuggerDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_analytics_debugger_detail, container, false);
        textName = view.findViewById(R.id.text_name);
        textTimestamp = view.findViewById(R.id.text_timestamp);
        textData = view.findViewById(R.id.text_data);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        viewModel = getArguments().getParcelable(DATA_DETAIL);
        if (viewModel != null) {
            textName.setText(viewModel.getCategory());
            textTimestamp.setText(viewModel.getTimestamp());
            textData.setText(viewModel.getData());
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
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, viewModel.getData());
                startActivity(Intent.createChooser(sharingIntent, null));
                return true;
            } else if (item.getItemId() == R.id.action_copy_text) {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("tokopedia_analytics_debugger", viewModel.getData());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String getScreenName() {
        return AnalyticsDebuggerDetailFragment.class.getSimpleName();
    }
}
