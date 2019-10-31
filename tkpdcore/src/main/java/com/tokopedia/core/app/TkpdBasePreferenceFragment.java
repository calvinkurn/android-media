package com.tokopedia.core.app;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Herdi_WORK on 22.11.16.
 */

public abstract class TkpdBasePreferenceFragment extends PreferenceFragment{

    protected abstract String getScreenName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (view != null) {
            configureCustomPadding(view);
        }

        return view;
    }

    // Google Feedback T60724
    private void configureCustomPadding(@NonNull View view) {
        ListView listView = view.findViewById(android.R.id.list);
        final int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        listView.setPadding(padding, 0, padding, 0);
    }
}
