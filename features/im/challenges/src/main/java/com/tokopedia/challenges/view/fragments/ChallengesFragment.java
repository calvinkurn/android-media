package com.tokopedia.challenges.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.adapter.ChallengesListAdapter;
import com.tokopedia.challenges.view.analytics.ChallengesGaAnalyticsTracker;
import com.tokopedia.challenges.view.analytics.ChallengesMoengageAnalyticsTracker;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.presenter.ChallengeHomePresenter;
import com.tokopedia.challenges.view.presenter.ChallengesBaseContract;
import com.tokopedia.challenges.view.utils.ChallengesCacheHandler;
import com.tokopedia.challenges.view.utils.EmptyStateViewHelper;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ashwanityagi on 06/08/18.
 */
public class ChallengesFragment extends BaseDaggerFragment implements ChallengesBaseContract.View {

    @Inject
    public ChallengeHomePresenter challengeHomePresenter;
    private ChallengesListAdapter listAdpater;
    private RecyclerView recyclerView;
    private TextView tvActiveChallenges;
    private TextView tvPastChallenges;
    private View progressBar;
    private List<Result> openChallenges;
    private List<Result> pastChallenges;
    @Inject
    ChallengesGaAnalyticsTracker analytics;
    private boolean isPastChallenge;
    private boolean isFirst = true;
    private boolean isFirstPastChallengeItem;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initInjector() {
        getComponent(ChallengesComponent.class).inject(this);
        challengeHomePresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenges, container, false);
        recyclerView = view.findViewById(R.id.rv_home_challenges);
        tvActiveChallenges = view.findViewById(R.id.tv_active_challenges);
        tvPastChallenges = view.findViewById(R.id.tv_past_challenges);
        progressBar = view.findViewById(R.id.progress_bar_layout);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        ChallengesMoengageAnalyticsTracker.challengeScreenLaunched(getActivity(), getString(R.string.ch_active_challenges_label));

        tvActiveChallenges.setOnClickListener(v -> {
            challengeHomePresenter.getOpenChallenges();
            tvActiveChallenges.setBackgroundResource(R.drawable.bg_ch_bubble_selected);
            tvPastChallenges.setBackgroundResource(R.drawable.bg_ch_bubble_default);
            tvActiveChallenges.setTextColor(getResources().getColor(R.color.tkpd_main_green));
            tvPastChallenges.setTextColor(getResources().getColor(R.color.black_56));
            analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_CHALLENGES,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_HOME_PAGE,
                    ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_ACTIVE_CHALLENGES);
            isPastChallenge = false;
            ChallengesMoengageAnalyticsTracker.challengeScreenLaunched(getActivity(), getString(R.string.ch_active_challenges_label));

        });

        tvPastChallenges.setOnClickListener(v -> {
            challengeHomePresenter.getPastChallenges();
            tvPastChallenges.setBackgroundResource(R.drawable.bg_ch_bubble_selected);
            tvActiveChallenges.setBackgroundResource(R.drawable.bg_ch_bubble_default);
            tvPastChallenges.setTextColor(getResources().getColor(R.color.tkpd_main_green));
            tvActiveChallenges.setTextColor(getResources().getColor(R.color.black_56));
            analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_CHALLENGES,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_CHALLENGES_HOME_PAGE,
                    ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK,
                    ChallengesGaAnalyticsTracker.EVENT_CATEGORY_PAST_CHALLENGES);
            isPastChallenge = true;
            ChallengesMoengageAnalyticsTracker.challengeScreenLaunched(getActivity(), getString(R.string.ch_past_challenges_label));
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ChallengesCacheHandler.resetCache();
                getChallenges();
            }
        });


        challengeHomePresenter.getOpenChallenges();
        if (isFirst) {
            isFirst = false;
        }
        return view;
    }


    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void setChallengeDataToUI(List<Result> resultList, boolean isPastChallenge) {
        this.isPastChallenge = isPastChallenge;
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        if (isPastChallenge) {
            pastChallenges = resultList;
        } else {
            openChallenges = resultList;
        }
        if (listAdpater != null && isFirstPastChallengeItem) {
            listAdpater.setData(resultList, isPastChallenge);
            listAdpater.notifyDataSetChanged();

        } else {
            listAdpater = new ChallengesListAdapter(getActivity(), resultList, isPastChallenge);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(listAdpater);
        }
        EmptyStateViewHelper.hideEmptyState(getView());
        if (isPastChallenge) {
            isFirstPastChallengeItem = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        /**
         * Fetch data without cache
         */
        if (ChallengesCacheHandler.OPEN_CHALLENGES_LIST_CACHE) {
            getChallenges();
        }

    }

    @Override
    public void showErrorNetwork(String errorMessage) {
        swipeRefreshLayout.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.ch_oops),
                getString(R.string.ch_network_error_msg),
                getString(R.string.ch_try_again), R.drawable.ic_offline2,
                getChallengesRetryListener()
        );
    }

    @Override
    public void renderEmptyList() {
        swipeRefreshLayout.setVisibility(View.GONE);
        String error_msg;
        if (isPastChallenge) {
            error_msg = getString(R.string.ch_no_past_challenge_msg);
        } else {
            error_msg = getString(R.string.ch_no_challenge_msg);
        }
        EmptyStateViewHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.ch_oops),
                error_msg,
                getString(R.string.ch_try_again), R.drawable.empty_challenge_active,
                getChallengesRetryClickedListener()
        );

    }

    @Override
    public void removeProgressBarView() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBarView() {
        progressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        EmptyStateViewHelper.hideEmptyState(getView());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private EmptyStateViewHelper.RetryClickedListener getChallengesRetryClickedListener() {
        return () -> getChallenges();
    }

    private NetworkErrorHelper.RetryClickedListener getChallengesRetryListener() {
        return () -> getChallenges();
    }

    @Override
    public List<Result> getOpenChallenges() {
        return openChallenges;
    }

    @Override
    public List<Result> getPastChallenges() {
        return pastChallenges;
    }

    private void getChallenges() {
        if (isPastChallenge) {
            challengeHomePresenter.getPastChallenges();
        } else {
            challengeHomePresenter.getOpenChallenges();
        }
    }

    @Override
    public void onDestroyView() {
        challengeHomePresenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void setSwipeRefreshing() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }
}
