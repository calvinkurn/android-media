package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFilterActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.InboxReputationFilterAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.filter.HeaderOptionViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.filter.OptionViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationFilterFragment extends BaseDaggerFragment
        implements InboxReputationFilterAdapter.FilterListener,
        InboxReputationFilterActivity.ResetListener {

    private static final String FILTER_ALL_TIME = "1";
    private static final String FILTER_LAST_WEEK = "2";
    private static final String FILTER_THIS_MONTH = "3";
    private static final String FILTER_LAST_3_MONTH = "4";

    private static final String FILTER_NO_SCORE = "1";
    private static final String FILTER_GIVEN_SCORE = "2";

    public static final String SELECTED_TIME_FILTER = "SELECTED_TIME_FILTER";
    public static final String SELECTED_SCORE_FILTER = "SELECTED_SCORE_FILTER";

    RecyclerView list;
    Button saveButton;
    InboxReputationFilterAdapter adapter;
    ArrayList<OptionViewModel> listOption;

    String timeFilter;
    String scoreFilter;

    public static Fragment createInstance(String timeFilter, String statusFilter, int tab) {
        InboxReputationFilterFragment fragment = new InboxReputationFilterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SELECTED_TIME_FILTER, timeFilter);
        bundle.putString(SELECTED_SCORE_FILTER, statusFilter);
        bundle.putInt(InboxReputationFragment.PARAM_TAB, tab);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            timeFilter = getArguments().getString(SELECTED_TIME_FILTER, "");
            scoreFilter = getArguments().getString(SELECTED_SCORE_FILTER, "");
        } else if (savedInstanceState != null) {
            timeFilter = savedInstanceState.getString(SELECTED_TIME_FILTER, "");
            scoreFilter = savedInstanceState.getString(SELECTED_SCORE_FILTER, "");
        }

        initData();
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View parentView = inflater.inflate(R.layout.fragment_inbox_reputation_filter, container,
                false);
        list = (RecyclerView) parentView.findViewById(R.id.list);
        saveButton = (Button) parentView.findViewById(R.id.save_button);
        prepareView();
        return parentView;

    }

    private void prepareView() {
        list.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        adapter = InboxReputationFilterAdapter.createInstance(this, listOption);
        list.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(SELECTED_TIME_FILTER, timeFilter);
                data.putExtra(SELECTED_SCORE_FILTER, scoreFilter);
                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();

            }
        });
    }


    private void initData() {
        listOption = createListOption();
        setSelected(listOption);
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_INBOX_REPUTATION_FILTER;
    }

    private ArrayList<OptionViewModel> createListOption() {
        ArrayList<OptionViewModel> list = new ArrayList<OptionViewModel>();
        list.add(new HeaderOptionViewModel(getString(R.string.filter_time)));
        list.add(new OptionViewModel(getString(R.string.filter_all_time),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_ALL_TIME, list.size
                ()));
        list.add(new OptionViewModel(getString(R.string.filter_last_7_days),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_LAST_WEEK, list.size
                ()));
        list.add(new OptionViewModel(getString(R.string.filter_this_month),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_THIS_MONTH, list.size
                ()));
        list.add(new OptionViewModel(getString(R.string.filter_last_3_month),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_LAST_3_MONTH, list.size
                ()));

        if (getArguments() != null
                && getArguments().getInt(InboxReputationFragment.PARAM_TAB) ==
                InboxReputationActivity.TAB_BUYER_REVIEW) {
            list.add(new HeaderOptionViewModel(getString(R.string.filter_status)));
            list.add(new OptionViewModel(getString(R.string.filter_given_reputation),
                    GetFirstTimeInboxReputationUseCase.PARAM_SCORE_FILTER, FILTER_GIVEN_SCORE, list
                    .size
                            ()));
            list.add(new OptionViewModel(getString(R.string.filter_no_reputation),
                    GetFirstTimeInboxReputationUseCase.PARAM_SCORE_FILTER, FILTER_NO_SCORE, list.size
                    ()));
        }
        return list;
    }


    private void setSelected(ArrayList<OptionViewModel> listOption) {
        if (!getArguments().getString(SELECTED_TIME_FILTER, "").equals("")
                || !getArguments().getString(SELECTED_SCORE_FILTER, "").equals("")) {
            for (OptionViewModel optionViewModel : listOption) {
                if (optionViewModel.getKey().equals(
                        GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER)
                        && optionViewModel.getValue().equals(
                        getArguments().getString(SELECTED_TIME_FILTER))) {
                    optionViewModel.setSelected(true);
                }

                if (optionViewModel.getKey().equals(
                        GetFirstTimeInboxReputationUseCase.PARAM_SCORE_FILTER)
                        && optionViewModel.getValue().equals(
                        getArguments().getString(SELECTED_SCORE_FILTER))) {
                    optionViewModel.setSelected(true);
                }
            }
        }
    }

    @Override
    public void resetFilter() {
        adapter.resetFilter();
        timeFilter = "";
        scoreFilter = "";
    }

    @Override
    public void onFilterSelected(OptionViewModel optionViewModel) {
        if (optionViewModel.getKey().equals(GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER)) {
            timeFilter = optionViewModel.getValue();
        } else if (optionViewModel.getKey().equals(GetFirstTimeInboxReputationUseCase
                .PARAM_SCORE_FILTER)) {
            scoreFilter = optionViewModel.getValue();
        }
    }

    @Override
    public void onFilterUnselected(OptionViewModel optionViewModel) {
        if (optionViewModel.getKey().equals(GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER)) {
            timeFilter = "";
        } else if (optionViewModel.getKey().equals(GetFirstTimeInboxReputationUseCase
                .PARAM_SCORE_FILTER)) {
            scoreFilter = "";
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SELECTED_TIME_FILTER, timeFilter);
        outState.putString(SELECTED_SCORE_FILTER, scoreFilter);
    }
}