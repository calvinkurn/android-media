package com.tokopedia.review.feature.inbox.buyerreview.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.review.R;
import com.tokopedia.review.feature.inbox.buyerreview.analytics.AppScreen;
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking;
import com.tokopedia.review.feature.inbox.buyerreview.di.DaggerReputationComponent;
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase;
import com.tokopedia.review.feature.inbox.buyerreview.view.activity.InboxReputationFilterActivity;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.InboxReputationFilterAdapter;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.filter.HeaderOptionUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.filter.OptionUiModel;
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants;

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
    ArrayList<OptionUiModel> listOption;

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

    private void prepareView() {
        list.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        adapter = InboxReputationFilterAdapter.createInstance(getContext(),this, listOption);
        list.setAdapter(adapter);

        saveButton.setOnClickListener(view -> {
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

    private ArrayList<OptionUiModel> createListOption() {
        ArrayList<OptionUiModel> list = new ArrayList<OptionUiModel>();
        list.add(new HeaderOptionUiModel(getString(R.string.filter_time)));
        list.add(new OptionUiModel(getString(R.string.filter_all_time),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_ALL_TIME, list.size
                ()));
        list.add(new OptionUiModel(getString(R.string.filter_last_7_days),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_LAST_WEEK, list.size
                ()));
        list.add(new OptionUiModel(getString(R.string.filter_this_month),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_THIS_MONTH, list.size
                ()));
        list.add(new OptionUiModel(getString(R.string.filter_last_3_month),
                GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER, FILTER_LAST_3_MONTH, list.size
                ()));

        if (getArguments() != null
                && getArguments().getInt(InboxReputationFragment.PARAM_TAB) ==
                ReviewInboxConstants.TAB_BUYER_REVIEW) {
            list.add(new HeaderOptionUiModel(getString(R.string.filter_status)));
            list.add(new OptionUiModel(getString(R.string.filter_given_reputation),
                    GetFirstTimeInboxReputationUseCase.PARAM_SCORE_FILTER, FILTER_GIVEN_SCORE, list
                    .size
                            ()));
            list.add(new OptionUiModel(getString(R.string.filter_no_reputation),
                    GetFirstTimeInboxReputationUseCase.PARAM_SCORE_FILTER, FILTER_NO_SCORE, list.size
                    ()));
        }
        return list;
    }


    private void setSelected(ArrayList<OptionUiModel> listOption) {
        if (!getArguments().getString(SELECTED_TIME_FILTER, "").equals("")
                || !getArguments().getString(SELECTED_SCORE_FILTER, "").equals("")) {
            for (OptionUiModel optionUiModel : listOption) {
                if (optionUiModel.getKey().equals(
                        GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER)
                        && optionUiModel.getValue().equals(
                        getArguments().getString(SELECTED_TIME_FILTER))) {
                    optionUiModel.setSelected(true);
                }

                if (optionUiModel.getKey().equals(
                        GetFirstTimeInboxReputationUseCase.PARAM_SCORE_FILTER)
                        && optionUiModel.getValue().equals(
                        getArguments().getString(SELECTED_SCORE_FILTER))) {
                    optionUiModel.setSelected(true);
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
    public void onFilterSelected(OptionUiModel optionUiModel) {
        if (optionUiModel.getKey().equals(GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER)) {
            timeFilter = optionUiModel.getValue();
            timeFilterName = optionUiModel.getName();
        } else if (optionUiModel.getKey().equals(GetFirstTimeInboxReputationUseCase
                .PARAM_SCORE_FILTER)) {
            scoreFilter = optionUiModel.getValue();
        }
    }

    @Override
    public void onFilterUnselected(OptionUiModel optionUiModel) {
        if (optionUiModel.getKey().equals(GetFirstTimeInboxReputationUseCase.PARAM_TIME_FILTER)) {
            timeFilter = "";
            timeFilterName = "";
        } else if (optionUiModel.getKey().equals(GetFirstTimeInboxReputationUseCase
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