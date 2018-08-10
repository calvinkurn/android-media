package com.tokopedia.challenges.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.model.IndiUserModel;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.SubmitDetailContract;
import com.tokopedia.challenges.view.presenter.SubmitDetailPresenter;

import javax.inject.Inject;

public class SubmitDetailFragment extends BaseDaggerFragment implements SubmitDetailContract.View{
    private ImageView profilePic;
    private TextView profileText;
    private ImageView challengeImage;
    private TextView likesCountView;
    private TextView pointsView;
    private FloatingActionButton shareView;
    private TextView approvedView;
    private TextView detailTitle;
    private TextView detailContent;

    @Inject
    SubmitDetailPresenter presenter;
    private SubmissionResult model;

    public static Fragment newInstance(){
        return new SubmitDetailFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_submit_detail, container, false);
        profilePic = view.findViewById(R.id.profile_pic);
        profileText = view.findViewById(R.id.profile_name);
        challengeImage = view.findViewById(R.id.img_challenge);
        likesCountView = view.findViewById(R.id.tv_likes);
        pointsView = view.findViewById(R.id.tv_points);
        shareView = view.findViewById(R.id.share_btn);
        approvedView = view.findViewById(R.id.approved);
        detailTitle = view.findViewById(R.id.detail_title);
        detailContent = view.findViewById(R.id.detail_content);
        model = getArguments().getParcelable("submissionsResult");
        presenter.setDataInFields(model);

        return view;
    }

    @Override
    public void setProfilePic(String url) {
        ImageHandler.loadImageCircle2(getActivity(),profilePic, url);
    }

    public void setProfileText(String text) {
        profileText.setText(text);
    }

    public void setChallengeImage(String url) {
        ImageHandler.loadImageAndCache(challengeImage, url);
    }

    public void setLikesCountView(String likesCount) {
        this.likesCountView.setText(likesCount);
    }

    public void setPointsView(String points) {
        this.pointsView.setText(points);
    }

    public void setShareView(String url) {
        //this.shareView = shareView;
    }

    public void setApprovedView(String approveText) {
        this.approvedView.setText(approveText);
    }

    public void setDetailTitle(String title) {
        this.detailTitle.setText(title);
    }

    public void setDetailContent(String content) {
        this.detailContent.setText(content);
    }

    public void setChallengeIcon(String url) {
       // this.challengeIcon = challengeIcon;
    }

    public void setSubmitBtnClick(String submitBtn) {

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
