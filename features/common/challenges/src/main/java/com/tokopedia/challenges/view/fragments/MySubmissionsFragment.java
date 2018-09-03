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
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.adapter.MySubmissionsListAdapter;
import com.tokopedia.challenges.view.adapter.MySubmissionsViewHolder;
import com.tokopedia.challenges.view.adapter.SubmissionItemAdapter;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.MySubmissionsBaseContract;
import com.tokopedia.challenges.view.presenter.MySubmissionsHomePresenter;
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
        progressBar = view.findViewById(R.id.progress_bar_layout);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        listAdpater = new MySubmissionsListAdapter(getActivity(), null, this);
        recyclerView.setAdapter(listAdpater);
        //recyclerView.addOnScrollListener(rvOnScrollListener);
        recyclerView.setVisibility(View.VISIBLE);
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
//        EmptyStateViewHelper.showEmptyState(
//                getActivity(), getView(),
//                "Oops!",
//                "You have not participate in any challenges.\n" +
//                        "Click this button to see active challenges.",
//                "Show Challenges", R.drawable.ic_offline2,
//                getChallengesRetryListener()
//        );
    }


    private EmptyStateViewHelper.RetryClickedListener getChallengesRetryListener() {
        return new EmptyStateViewHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {

            }
        };
    }


    @Override
    public void renderEmptyList() {
        recyclerView.setVisibility(View.GONE);
        //emptyLayout.setVisibility(View.VISIBLE);
        EmptyStateViewHelper.showEmptyState(
                getActivity(), getView(),
                "Oops!",
                "There are no challenges available.\n" +
                        "Please check again later.",
                "Show challenges", R.drawable.empty_mysubmission_active,
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
            getActivity().onBackPressed();
        };
    }

    @Override
    public void onLikeClick(SubmissionResult challengesResult) {
        mySubmissionsHomePresenter.setSubmissionLike(challengesResult);
    }
}
