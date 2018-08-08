package com.tokopedia.challenges.view.fragments;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.activity.ChallengeDetailActivity;
import com.tokopedia.challenges.view.adapter.SubmissionItemAdapter;
import com.tokopedia.challenges.view.contractor.AllSubmissionContract;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.AllSubmissionPresenter;

import java.util.List;

import com.tokopedia.challenges.R;
import com.tokopedia.challenges.view.utils.ChallengesFragmentCallbacks;
import com.tokopedia.challenges.view.utils.Utils;

import javax.inject.Inject;

public class AllSubmissionFragment extends BaseDaggerFragment implements AllSubmissionContract.View, SubmissionItemAdapter.INavigateToActivityRequest, View.OnClickListener {

    private LinearLayout baseMainContent;
    private FrameLayout progressBarLayout;
    LinearLayoutManager layoutManager;
    private RecyclerView recyclerview;
    @Inject
    AllSubmissionPresenter mPresenter;
    private Toolbar toolbar;
    private ChallengesFragmentCallbacks fragmentCallbacks;
    private TextView mostRecent;
    private TextView buzzPoints;
    private static final int SORT_RECENT=1;
    private static final int SORT_POINTS=2;
    private int currentFilter;


    public static Fragment createInstance() {
        AllSubmissionFragment categoryFragment = new AllSubmissionFragment();
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPresenter.setPageStart(fragmentCallbacks.getSubmissions().size());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_submissions, container, false);
        setUpVariables(view);
        if (fragmentCallbacks != null)
            recyclerview.setAdapter(new SubmissionItemAdapter(fragmentCallbacks.getSubmissions(), this, LinearLayoutManager.VERTICAL));
        recyclerview.addOnScrollListener(rvOnScrollListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setUpVariables(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(getActivity().getResources().getString(R.string.submissions));
        recyclerview = view.findViewById(R.id.rv_submissions);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        baseMainContent = view.findViewById(R.id.base_main_content);
        mostRecent = view.findViewById(R.id.tv_most_recent);
        buzzPoints = view.findViewById(R.id.tv_buzz_points);
        mostRecent.setOnClickListener(this);
        buzzPoints.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        currentFilter=SORT_RECENT;
    }

    @Override
    protected void initInjector() {
        getComponent(ChallengesComponent.class).inject(this);
        mPresenter.attachView(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallbacks = (ChallengeDetailActivity) activity;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void renderSubmissionList(List<SubmissionResult> submissionResultList) {

    }

    @Override
    public void showProgressBar() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void showViews() {
        baseMainContent.setVisibility(View.VISIBLE);
    }

    @Override
    public View getRootView() {
        return baseMainContent;
    }


    @Override
    public void removeFooter() {
        ((SubmissionItemAdapter) recyclerview.getAdapter()).removeFooter();

    }

    @Override
    public void addFooter() {
        ((SubmissionItemAdapter) recyclerview.getAdapter()).addFooter();

    }

    @Override
    public void addSubmissionToCards(List<SubmissionResult> brandList) {
        if (brandList != null) {
            ((SubmissionItemAdapter) recyclerview.getAdapter()).addAll(brandList);
        }

    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }


    private RecyclerView.OnScrollListener rvOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mPresenter.onRecyclerViewScrolled(layoutManager);
        }
    };

    @Override
    public void onDestroyView() {
        mPresenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {
        navigateToActivityRequest(intent, requestCode);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_most_recent){
            if(currentFilter==SORT_POINTS){
                currentFilter=SORT_RECENT;
                mostRecent.setBackgroundResource(R.drawable.bg_ch_bubble_selected);
                buzzPoints.setBackgroundResource(R.drawable.bg_ch_bubble_default);
                mPresenter.setPageStart(0);
                mPresenter.setSortType(Utils.QUERY_PARAM_KEY_SORT_RECENT);
                ((SubmissionItemAdapter)recyclerview.getAdapter()).clearList();
                ((SubmissionItemAdapter)recyclerview.getAdapter()).notifyDataSetChanged();
                mPresenter.loadMoreItems();
            }

        }else if(v.getId() == R.id.tv_buzz_points){
            if(currentFilter==SORT_RECENT){
                currentFilter=SORT_POINTS;
                buzzPoints.setBackgroundResource(R.drawable.bg_ch_bubble_selected);
                mostRecent.setBackgroundResource(R.drawable.bg_ch_bubble_default);
                mPresenter.setPageStart(0);
                mPresenter.setSortType(Utils.QUERY_PARAM_KEY_SORT_POINTS);
                ((SubmissionItemAdapter)recyclerview.getAdapter()).clearList();
                ((SubmissionItemAdapter)recyclerview.getAdapter()).notifyDataSetChanged();
                mPresenter.loadMoreItems();
            }
        }
    }
}
