package com.tokopedia.challenges.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.adapter.SubmissionItemAdapter;
import com.tokopedia.challenges.view.contractor.ChallengeSubmissonContractor;
import com.tokopedia.challenges.view.customview.ExpandableTextView;
import com.tokopedia.challenges.view.model.Result;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.presenter.ChallengeSubmissionPresenter;
import com.tokopedia.challenges.di.DaggerChallengesComponent;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.usecase.RequestParams;

import org.w3c.dom.Text;

import javax.inject.Inject;

public class ChallegeneSubmissionFragment extends BaseDaggerFragment implements ChallengeSubmissonContractor.View, View.OnClickListener, SubmissionItemAdapter.INavigateToActivityRequest {

    @Inject
    ChallengeSubmissionPresenter challengeSubmissionPresenter;
    ImageView challengeImage;
    TextView challengeTitle;
    TextView challengeDueDate;
    TextView tv_participated;
    TextView seeMoreButton;
    ImageView seeMoreArrow;
    ExpandableTextView description;
    RecyclerView submissionRecyclerView;

    SubmissionItemAdapter submissionItemAdapter;

    Result challengeResult;

    public static Fragment createInstance(Bundle extras) {
        Fragment fragment = new ChallegeneSubmissionFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.challengeResult = getArguments().getParcelable("challengesResult");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.challenges_detail_fragment, container, false);
        challengeImage = (ImageView) view.findViewById(R.id.image_challenge);
        challengeTitle = (TextView) view.findViewById(R.id.tv_title);
        challengeDueDate = (TextView) view.findViewById(R.id.tv_expiry_date);
        tv_participated = (TextView) view.findViewById(R.id.tv_participated);
        description = (ExpandableTextView) view.findViewById(R.id.tv_expandable_description);
        seeMoreButton = (TextView) view.findViewById(R.id.seemorebutton_description);
        seeMoreArrow = (ImageView) view.findViewById(R.id.down_arrow_description);
        submissionRecyclerView = view.findViewById(R.id.rv_submissions);


        challengeSubmissionPresenter.attachView(this);
        challengeSubmissionPresenter.initialize();
        return view;
    }

    @Override
    protected void initInjector() {
        NetworkClient.init(getActivity());
        ChallengesComponent component = DaggerChallengesComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        component.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void renderChallengeDetail(SubmissionResponse submissionResponse) {
        Log.d("Naveen", "Get the response");

        ImageHandler.loadImage(getActivity(), challengeImage, challengeResult.getThumbnailUrl(), R.color.grey_1100, R.color.grey_1100);
        challengeTitle.setText(challengeResult.getTitle());
        challengeDueDate.setText(challengeResult.getEndDate());
        description.setText(challengeResult.getDescription());
        description.setInterpolator(new OvershootInterpolator());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        submissionRecyclerView.setLayoutManager(mLayoutManager);
        submissionItemAdapter = new SubmissionItemAdapter(submissionResponse.getSubmissionResults(), this);
        submissionRecyclerView.setAdapter(submissionItemAdapter);

    }

    @Override
    public RequestParams getParams() {
        return null;
    }

    @Override
    public View getRootView() {
        return null;
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return null;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.expand_view_description) {
            if (description.isExpanded()) {
                seeMoreButton.setText(R.string.expand);
//                seeMoreArrow.animate().rotation(0f);

            } else {
                seeMoreButton.setText(R.string.collapse);
//                seeMoreArrow.animate().rotation(180f);

            }
            description.toggle();
        }
    }

    @Override
    public void onNavigateToActivityRequest(Intent intent, int requestCode, int position) {

    }
}
