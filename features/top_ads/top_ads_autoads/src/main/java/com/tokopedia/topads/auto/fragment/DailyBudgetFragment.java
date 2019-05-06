package com.tokopedia.topads.auto.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.topads.auto.InfoAutoAdsSheet;
import com.tokopedia.topads.auto.ManualAdsConfirmationSheet;
import com.tokopedia.topads.auto.R;
import com.tokopedia.topads.auto.activity.AutoAdsActivatedActivity;

/**
 * Author errysuprayogi on 07,May,2019
 */
public class DailyBudgetFragment extends BaseDaggerFragment implements View.OnClickListener {

    private Button startAutoAdsBtn;
    private Button startManualAdsBtn;

    public static DailyBudgetFragment newInstance() {

        Bundle args = new Bundle();

        DailyBudgetFragment fragment = new DailyBudgetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_start_daily_budget, container, false);

        startAutoAdsBtn = view.findViewById(R.id.start_autoads_btn);
        startManualAdsBtn = view.findViewById(R.id.start_manual_ads_btn);

        startManualAdsBtn.setOnClickListener(this);
        startAutoAdsBtn.setOnClickListener(this);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_info) {
            InfoAutoAdsSheet.newInstance(getActivity());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.start_manual_ads_btn) {
            ManualAdsConfirmationSheet.newInstance(getActivity()).show();
        } else if (id == R.id.start_autoads_btn) {
            startActivity(new Intent(getActivity(), AutoAdsActivatedActivity.class));
        }
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
