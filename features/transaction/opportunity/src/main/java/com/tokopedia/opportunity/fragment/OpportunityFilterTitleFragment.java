package com.tokopedia.opportunity.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.opportunity.R;
import com.tokopedia.opportunity.activity.OpportunityFilterActivity;
import com.tokopedia.opportunity.adapter.OpportunityFilterTitleAdapter;
import com.tokopedia.opportunity.viewmodel.FilterViewModel;

import java.util.ArrayList;

/**
 * Created by nisie on 4/7/17.
 */

public class OpportunityFilterTitleFragment extends BasePresenterFragment
        implements OpportunityFilterActivity.FilterTitleListener {

    private static final String ARGS_LIST_FILTER = "ARGS_LIST_FILTER";
    RecyclerView opportunityFilter;
    OpportunityFilterTitleAdapter adapter;
    ArrayList<FilterViewModel> listFilter;

    public OpportunityFilterTitleFragment() {
        this.listFilter = new ArrayList<>();
    }

    public static OpportunityFilterTitleFragment createInstance(ArrayList<FilterViewModel> listTitle) {
        OpportunityFilterTitleFragment fragment = new OpportunityFilterTitleFragment();
        Bundle bundle = new Bundle();
        for (FilterViewModel filterViewModel : listTitle) {
            filterViewModel.setSelected(false);
        }
        listTitle.get(0).setSelected(true);
        bundle.putParcelableArrayList(ARGS_LIST_FILTER, listTitle);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        if (getArguments().getParcelableArrayList(ARGS_LIST_FILTER) != null)
            listFilter = getArguments().getParcelableArrayList(ARGS_LIST_FILTER);
        adapter.setList(listFilter);
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_opportunity_filter_title;
    }

    @Override
    protected void initView(View view) {
        opportunityFilter = (RecyclerView) view.findViewById(R.id.opportunity_filter);
        opportunityFilter.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = OpportunityFilterTitleAdapter.createInstance(getActivity(), (OpportunityFilterActivity) getActivity());
        opportunityFilter.setAdapter(adapter);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void updateData(ArrayList<FilterViewModel> listFilter) {
        this.listFilter = listFilter;
        if (adapter != null) {
            adapter.setList(listFilter);
            adapter.notifyDataSetChanged();
        }
    }
}
