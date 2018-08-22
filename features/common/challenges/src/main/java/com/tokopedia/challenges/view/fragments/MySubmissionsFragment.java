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

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.adapter.MySubmissionsListAdapter;
import com.tokopedia.challenges.view.adapter.MySubmissionsViewHolder;
import com.tokopedia.challenges.view.adapter.SubmissionItemAdapter;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.MySubmissionsBaseContract;
import com.tokopedia.challenges.view.presenter.MySubmissionsHomePresenter;

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
    private LinearLayout emptyLayout;
    private View progressBar;
    private Boolean isFirst = true;
    private LinearLayoutManager layoutManager;


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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_submissions, container, false);
        recyclerView = view.findViewById(R.id.rv_home_submissions);
        emptyLayout = view.findViewById(R.id.empty_view);
        progressBar = view.findViewById(R.id.progress_bar_layout);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        listAdpater = new MySubmissionsListAdapter(getActivity(), null, this);
        recyclerView.setAdapter(listAdpater);
        recyclerView.addOnScrollListener(rvOnScrollListener);
        recyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        //mySubmissionsHomePresenter.getMySubmissionsList();
        //isFirst = false;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mySubmissionsHomePresenter.getMySubmissionsList(isFirst);
        if (isFirst) {
            isFirst = false;
        }
    }

    @Override
    public void setSubmissionsDataToUI(List<SubmissionResult> resultList) {
        if (resultList != null) {
            listAdpater.addAll(resultList);
        }
    }

    @Override
    public void showErrorNetwork(String errorMessage) {
        NetworkErrorHelper.showEmptyState(
                getActivity(), getView(),
                "title",
                "message",
                "choba lagi", 0,
                getMySubmissionsRetryListener()
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
        emptyLayout.setVisibility(View.GONE);
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

    private NetworkErrorHelper.RetryClickedListener getMySubmissionsRetryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
            }
        };
    }

    @Override
    public void onLikeClick(SubmissionResult challengesResult) {
        mySubmissionsHomePresenter.setSubmissionLike(challengesResult);
    }
}
