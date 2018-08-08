package com.tokopedia.challenges.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.IndiUserModel;
import com.tokopedia.challenges.presenter.SubmitDetailContract;
import com.tokopedia.challenges.presenter.SubmitDetailPresenter;

import javax.inject.Inject;

public class SubmitDetailFragment extends BaseDaggerFragment implements SubmitDetailContract.View{
    private ImageView profilePic;
    private TextView profileText;
    private ImageView challengeImage;
    private TextView likesCountView;
    private TextView pointsView;
    private ImageView shareView;
    private TextView dateView;
    private TextView approvedView;
    private TextView detailTitle;
    private TextView detailContent;
    private ImageView challengeIcon;
    private TextView challengeTitle;
    private TextView challengeAt;
    private TextView challengeHash;
    private TextView submitBtn;
    @Inject
    SubmitDetailPresenter presenter;
    private IndiUserModel model;

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
        dateView = view.findViewById(R.id.date);
        approvedView = view.findViewById(R.id.approved);
        detailTitle = view.findViewById(R.id.detail_title);
        detailContent = view.findViewById(R.id.detail_content);
//        challengeIcon = view.findViewById(R.id.challenge_icon);
//        challengeTitle = view.findViewById(R.id.challenge_title);
//        challengeAt = view.findViewById(R.id.challenge_at);
//        challengeHash = view.findViewById(R.id.challenge_hash);
//        submitBtn = view.findViewById(R.id.submit_btn);
        presenter.attachView(this);
        presenter.setDataInFields(model);

        return view;
    }

    @Override
    public void setProfilePic(String url) {
        profilePic.setImageDrawable(null);
    }

    public void setProfileText(String text) {
        profileText.setText(text);
    }

    public void setChallengeImage(String url) {
        //this.challengeImage = challengeImage;
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

    public void setDateView(String date) {
        this.dateView.setText(date);
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

    public void setChallengeTitle(String title) {
        this.challengeTitle.setText(title);
    }

    public void setChallengeAt(String at) {
        this.challengeAt.setText(at);
    }

    public void setChallengeHash(String hash) {
        this.challengeHash.setText(hash);
    }

    public void setSubmitBtnClick(String submitBtn) {

    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
