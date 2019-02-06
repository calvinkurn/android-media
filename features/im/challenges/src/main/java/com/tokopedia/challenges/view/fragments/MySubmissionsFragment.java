package com.tokopedia.challenges.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.adapter.MySubmissionsListAdapter;
import com.tokopedia.challenges.view.adapter.MySubmissionsViewHolder;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.MySubmissionsBaseContract;
import com.tokopedia.challenges.view.presenter.MySubmissionsHomePresenter;
import com.tokopedia.challenges.view.utils.ChallengesCacheHandler;
import com.tokopedia.challenges.view.utils.EmptyStateViewHelper;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by ashwanityagi on 06/08/18.
 */
public class MySubmissionsFragment extends BaseDaggerFragment implements MySubmissionsBaseContract.View, MySubmissionsViewHolder.ISubmissionsViewHolderListner {

    @Inject
    public MySubmissionsHomePresenter mySubmissionsHomePresenter;
    private MySubmissionsListAdapter listAdpater;
    private RecyclerView recyclerView;
    private View progressBar;
    private Boolean isFirst = true;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void initInjector() {
        getComponent(ChallengesComponent.class).inject(this);
        mySubmissionsHomePresenter.attachView(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_submissions, container, false);
        recyclerView = view.findViewById(R.id.rv_home_submissions);
        progressBar = view.findViewById(R.id.progress_bar_layout);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        listAdpater = new MySubmissionsListAdapter(getActivity(), null, this);
        recyclerView.setAdapter(listAdpater);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        mySubmissionsHomePresenter.getMySubmissionsList(isFirst);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        mySubmissionsHomePresenter.getMySubmissionsList(isFirst);
                    }
                }, 4000);
            }
        });
        if (isFirst) {
            isFirst = false;
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ChallengesCacheHandler.MY_SUBMISSTIONS_LIST_CACHE) {
            mySubmissionsHomePresenter.getMySubmissionsList(isFirst);
        }

    }

    @Override
    public void setSubmissionsDataToUI(List<SubmissionResult> resultList) {

        if (resultList != null) {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            listAdpater.addAll(resultList);
            EmptyStateViewHelper.hideEmptyState(getView());
        }
    }

    @Override
    public void showErrorNetwork(String errorMessage) {
        swipeRefreshLayout.setVisibility(View.GONE);
        EmptyStateViewHelper.showEmptyState(
                getActivity(), getView(),
                "",
                getString(R.string.ch_network_error_msg),
                getString(R.string.ch_try_again), R.drawable.ic_offline2,
                getMySubmissionsRetryListener()
        );
    }


    @Override
    public void renderEmptyList() {
        swipeRefreshLayout.setVisibility(View.GONE);
        EmptyStateViewHelper.showEmptyState(
                getActivity(), getView(),
                getString(R.string.ch_oops),
                getString(R.string.ch_not_participated_error_msg),
                getString(R.string.ch_try_again), R.drawable.empty_mysubmission_active,
                getMySubmissionsRetryListener()
        );
    }

    @Override
    public void removeProgressBarView() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBarView() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeFooter() {
        listAdpater.removeFooter();

    }

    @Override
    public void addFooter() {
        listAdpater.addFooter();
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        // mListener = null;
    }

    private RecyclerView.OnScrollListener rvOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mySubmissionsHomePresenter.onRecyclerViewScrolled(layoutManager);
        }
    };

    @Override
    protected String getScreenName() {
        return null;
    }

    private EmptyStateViewHelper.RetryClickedListener getMySubmissionsRetryListener() {
        return () -> {
            EmptyStateViewHelper.hideEmptyState(getView());
            showProgressBarView();
            mySubmissionsHomePresenter.getMySubmissionsList(isFirst);
        };
    }

    @Override
    public void onLikeClick(SubmissionResult challengesResult) {
        mySubmissionsHomePresenter.setSubmissionLike(challengesResult);
    }

    @Override
    public void onDestroyView() {
        mySubmissionsHomePresenter.onDestroy();
        super.onDestroyView();
    }
}
