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
import com.tokopedia.challenges.view.adapter.MySubmissionsListAdpater;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.MySubmissionsBaseContract;
import com.tokopedia.challenges.view.presenter.MySubmissionsHomePresenter;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by ashwanityagi on 06/08/18.
 */
public class MySubmissionsFragment extends BaseDaggerFragment implements MySubmissionsBaseContract.View {

    @Inject
    public MySubmissionsHomePresenter mySubmissionsHomePresenter;
    private MySubmissionsListAdpater listAdpater;
    private RecyclerView recyclerView;
    private LinearLayout emptyLayout;
    private ProgressBar progressBar;


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
        emptyLayout= view.findViewById(R.id.empty_view);
        progressBar=view.findViewById(R.id.progressbar);
        mySubmissionsHomePresenter.getMySubmissionsList();
        return view;
    }

    @Override
    public void setSubmissionsDataToUI(List<SubmissionResult> resultList) {
        recyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        listAdpater = new MySubmissionsListAdpater(getActivity(), resultList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(listAdpater);
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
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
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
}
