package com.tokopedia.challenges.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
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

        mySubmissionsHomePresenter.getMySubmissionsList();
        return view;
    }

    @Override
    public void setSubmissionsDataToUI(List<SubmissionResult> resultList) {
        listAdpater = new MySubmissionsListAdpater(getActivity(), resultList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(listAdpater);
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
}
