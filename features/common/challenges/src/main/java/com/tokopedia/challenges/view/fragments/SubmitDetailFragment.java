package com.tokopedia.challenges.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.model.IndiUserModel;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.customview.CustomVideoPlayer;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.SubmitDetailContract;
import com.tokopedia.challenges.view.presenter.SubmitDetailPresenter;

import javax.inject.Inject;

public class SubmitDetailFragment extends BaseDaggerFragment implements SubmitDetailContract.View {
    private LinearLayout statusView;
    private ImageView closeBtn;
    private TextView statusText;
    private LinearLayout profileLayout;
    private ImageView profilePic;
    private TextView profileText;
    private CustomVideoPlayer challengeImage;
    private TextView challengeTitle;
    private TextView likesCountView;
    private TextView pointsView;
    private FloatingActionButton likeBtn;
    private TextView approvedView;
    private TextView pendingView;
    private TextView declinedView;
    private TextView detailTitle;
    private TextView detailContent;
    private TextView participateTitle;
    private TextView participateContent;
    private TextView btnShare;

    @Inject
    SubmitDetailPresenter presenter;
    private SubmissionResult model;

    public static Fragment newInstance() {
        return new SubmitDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_submit_detail, container, false);

        statusView = view.findViewById(R.id.conditional_status);
        statusText = view.findViewById(R.id.status_text);
        closeBtn = view.findViewById(R.id.close_status);

        profileLayout = view.findViewById(R.id.profile);
        profilePic = view.findViewById(R.id.profile_pic);
        profileText = view.findViewById(R.id.profile_name);

        challengeImage = view.findViewById(R.id.img_challenge);
        likeBtn = view.findViewById(R.id.like_btn);

        challengeTitle = view.findViewById(R.id.challenge_title);

        likesCountView = view.findViewById(R.id.tv_likes);
        pointsView = view.findViewById(R.id.tv_points);

        approvedView = view.findViewById(R.id.approved);
        pendingView = view.findViewById(R.id.pending);
        declinedView = view.findViewById(R.id.declined);

        detailTitle = view.findViewById(R.id.detail_title);
        detailContent = view.findViewById(R.id.detail_content);

        participateTitle = view.findViewById(R.id.participate_title);
        participateContent = view.findViewById(R.id.participate_content);
        btnShare = view.findViewById(R.id.btn_share);

        model = getArguments().getParcelable("submissionsResult");

        setClickListeners();
        presenter.setDataInFields(model);

        return view;
    }

    private void setClickListeners() {
        closeBtn.setOnClickListener(view -> {
            closeBtn.setVisibility(View.GONE);
            statusView.setVisibility(View.GONE);
        });

        btnShare.setOnClickListener(v -> {
            ((ChallengesModuleRouter)(getActivity().getApplication())).shareChallenge(getActivity(),"tokopedia://referral", model.getSharing().getMetaTags().getOgTitle(),model.getSharing().getMetaTags().getOgImage());

        });
    }

    @Override
    public void setProfilePic(String url) {
        ImageHandler.loadImageCircle2(getActivity(), profilePic, url);
    }

    public void setProfileText(String text) {
        profileText.setText(text);
    }

    public void setChallengeImage(String url) {
        //challengeImage.setVideoThumbNail(url);
    }

    public void setLikesCountView(String likesCount) {
        this.likesCountView.setText(likesCount);
    }

    public void setPointsView(String points) {
        this.pointsView.setText(points);
    }

    public void setShareView(String url) {
    }

    public void setApprovedView(String approveText) {
        this.approvedView.setText(approveText);
    }

    @Override
    public void setPendingView(String status) {
        this.pendingView.setText(status);
    }

    @Override
    public void setDeclinedView(String status) {
        this.declinedView.setText(status);
    }

    public void setDetailTitle(String title) {
        this.challengeTitle.setText(title);
    }

    public void setDetailContent(String content) {
        this.detailContent.setText(content);
    }

    @Override
    protected void initInjector() {
        getComponent(ChallengesComponent.class).inject(this);

        presenter.attachView(this);

    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
