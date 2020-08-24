package com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.analytic.AppScreen;
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTracking;
import com.tokopedia.tkpd.tkpdreputation.constant.Constant;
import com.tokopedia.tkpd.tkpdreputation.di.DaggerReputationComponent;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.activity.InboxReputationFilterActivity;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.InboxReputationFilterAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.filter.HeaderOptionViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.filter.OptionViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

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
    String timeFilterName;
    String scoreFilter;

    @Inject
    ReputationTracking reputationTracking;

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
        BaseAppComponent baseAppComponent = ((BaseMainApplication) requireContext().getApplicationContext()).getBaseAppComponent();
        DaggerReputationComponent reputationComponent =
                (DaggerReputationComponent) DaggerReputationComponent
                        .builder()
                        .baseAppComponent(baseAppComponent)
                        .build();
        reputationComponent.inject(this);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null) {
            reputationTracking.onSeeFilterPageTracker(getArguments().getInt(InboxReputationFragment.PARAM_TAB));
        }
    }

    private void prepareView() {
        list.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        adapter = InboxReputationFilterAdapter.createInstance(getContext(),this, listOption);
        list.setAdapter(adapter);

        saveButton.setOnClickListener(view -> {
            if(getArguments() != null) {
                reputationTracking.onSaveFilterReviewTracker(timeFilterName, getArguments().getInt(InboxReputationFragment.PARAM_TAB));
            }
            Intent data = new Intent();
            data.putExtra(SELECTED_TIME_FILTER, timeFilter);
            data.putExtra(SELECTED_SCORE_FILTER, scoreFilter);
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();

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
                Constant.TAB_BUYER_REVIEW) {
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
        timeFilterName = "";
        scoreFilter = "";
    }

    @Override
    public void onFilterSelected(OptionViewModel optionViewModel) {
        if (optionViewModel.getKey().equals(GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER)) {
            timeFilter = optionViewModel.getValue();
            timeFilterName = optionViewModel.getName();
            if(getArguments() != null) {
                reputationTracking.onClickFilterItemTracker(timeFilterName, getArguments().getInt(InboxReputationFragment.PARAM_TAB));
            }
        } else if (optionViewModel.getKey().equals(GetFirstTimeInboxReputationUseCase
                .PARAM_SCORE_FILTER)) {
            scoreFilter = optionViewModel.getValue();
        }
    }

    @Override
    public void onFilterUnselected(OptionViewModel optionViewModel) {
        if (optionViewModel.getKey().equals(GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER)) {
            timeFilter = "";
            timeFilterName = "";
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