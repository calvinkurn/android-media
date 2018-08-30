package com.tokopedia.challenges.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.challenges.ChallengesAnalytics;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.adapter.ChallengesListAdapter;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.presenter.ChallengeHomePresenter;
import com.tokopedia.challenges.view.presenter.ChallengesBaseContract;

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
    private LinearLayout emptyLayout;
    private View progressBar;
    private List<Result> openChallenges;
    private List<Result> pastChallenges;
    @Inject
    ChallengesAnalytics analytics;

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
        emptyLayout = view.findViewById(R.id.empty_view);
        progressBar = view.findViewById(R.id.progress_bar_layout);
        tvActiveChallenges.setOnClickListener(v -> {
            challengeHomePresenter.getOpenChallenges();
            tvActiveChallenges.setBackgroundResource(R.drawable.bg_ch_bubble_selected);
            tvPastChallenges.setBackgroundResource(R.drawable.bg_ch_bubble_default);
            analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_CHALLENGES,
                    ChallengesAnalytics.EVENT_CATEGORY_CHALLENGES,
                    ChallengesAnalytics.EVENT_ACTION_CLICK,
                    ChallengesAnalytics.EVENT_CATEGORY_ACTIVE_CHALLENGES);

        });

        tvPastChallenges.setOnClickListener(v -> {
            challengeHomePresenter.getPastChallenges();
            tvPastChallenges.setBackgroundResource(R.drawable.bg_ch_bubble_selected);
            tvActiveChallenges.setBackgroundResource(R.drawable.bg_ch_bubble_default);
            analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_CHALLENGES,
                    ChallengesAnalytics.EVENT_CATEGORY_CHALLENGES,
                    ChallengesAnalytics.EVENT_ACTION_CLICK,
                    ChallengesAnalytics.EVENT_CATEGORY_PAST_CHALLENGES);
        });

        challengeHomePresenter.getOpenChallenges();
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
        recyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        if (isPastChallenge) {
            pastChallenges = resultList;
        } else {
            openChallenges = resultList;
        }
        if (listAdpater != null) {
            listAdpater.setData(resultList,isPastChallenge);
            listAdpater.notifyDataSetChanged();
        } else {
            listAdpater = new ChallengesListAdapter(getActivity(), resultList, isPastChallenge);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(listAdpater);
        }
    }

    @Override
    public void showErrorNetwork(String errorMessage) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                "Oops!",
                "There are no challenges available.\n" +
                        "Please check again later.",
                "Coba lagi", R.drawable.ic_offline2,
                getChallengesRetryListener()
        );
    }

    @Override
    public void renderEmptyList() {
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void removeProgressBarView() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBarView() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private NetworkErrorHelper.RetryClickedListener getChallengesRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {

            }
        };
    }

    @Override
    public List<Result> getOpenChallenges() {
        return openChallenges;
    }

    @Override
    public List<Result> getPastChallenges() {
        return pastChallenges;
    }
}
