package com.tokopedia.challenges.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.customview.CustomVideoPlayer;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.SubmitDetailContract;
import com.tokopedia.challenges.view.presenter.SubmitDetailPresenter;
import com.tokopedia.challenges.view.share.ShareBottomSheet;

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
    private ImageView likesImageView;
    private TextView likesCountView;
    private TextView pointsView;
    private FloatingActionButton likeBtn;
    private TextView approvedView;
    private TextView pendingView;
    private TextView declinedView;
    private TextView detailTitle;
    private TextView detailContent;
    private TextView participateTitle;
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

        likesImageView = view.findViewById(R.id.likes_img);
        likesCountView = view.findViewById(R.id.tv_likes);
        pointsView = view.findViewById(R.id.tv_points);

        approvedView = view.findViewById(R.id.approved);
        pendingView = view.findViewById(R.id.pending);
        declinedView = view.findViewById(R.id.declined);

        detailTitle = view.findViewById(R.id.detail_title);
        detailContent = view.findViewById(R.id.detail_content);

        participateTitle = view.findViewById(R.id.participate_content);
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
           ShareBottomSheet.show((getActivity()).getSupportFragmentManager(), ChallengesUrl.AppLink.CHALLENGES_DETAILS, model.getTitle(), model.getSharing().getMetaTags().getOgUrl(), model.getSharing().getMetaTags().getOgTitle(), model.getSharing().getMetaTags().getOgImage());

        });

        likeBtn.setOnClickListener(v -> {
            presenter.likeBtnClick(model);
            if (model.getMe() != null) {
                    setLikes(!model.getMe().isLiked());
            }
        });
    }

    public void showStatusInfo() {
        closeBtn.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setProfilePic(String url) {
        ImageHandler.loadImageCircle2(getActivity(), profilePic, url);
    }

    public void setProfileText(String text) {
        profileText.setText(text);
    }

    public void setChallengeImage(String thumbnailUrl, String videoUrl) {
        challengeImage.setVideoThumbNail(thumbnailUrl, videoUrl, false);
    }

    public void setLikesCountView(String likesCount) {
        this.likesCountView.setText(likesCount);
    }

    public void setPointsView(String points) {
        this.pointsView.setText(points);
    }


    private void statusContent(TextView view, String text) {
        view.setVisibility(View.VISIBLE);
        view.setText(text);
    }

    public void setApprovedView(String approveText) {
        statusContent(approvedView, approveText);
    }

    @Override
    public void setPendingView(String pendingText) {
        statusContent(pendingView, pendingText);
    }

    @Override
    public void setDeclinedView(String declineText) {
        statusContent(declinedView, declineText);
    }

    public void setDetailTitle(String title) {
        this.challengeTitle.setText(title);
    }

    public void setDetailContent(String content) {
        this.detailContent.setText(content);
    }

    public void setParticipateTitle(String participateTitle) {
        this.participateTitle.setText(participateTitle);
    }

    @Override
    public void updateLikeCount(boolean liked) {
        if(!TextUtils.isEmpty(likesCountView.getText())) {
            int count = Integer.parseInt(likesCountView.getText().toString());
            if (count >= 0 && liked) {
                likesCountView.setText(""+(count + 1));
            } else if(count > 0){
                likesCountView.setText(""+(count - 1));
            }
        }
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

    public void setLikes(boolean isLiked) {

        model.getMe().setLiked(isLiked);
        if (isLiked) {
            updateLikeCount(true);
            likeBtn.setImageResource(R.drawable.ic_wishlist_checked);
            likesImageView.setImageResource(R.drawable.ic_wishlist_checked);
        } else {
            updateLikeCount(false);
            likeBtn.setImageResource(R.drawable.ic_wishlist_unchecked);
            likesImageView.setImageResource(R.drawable.ic_wishlist_unchecked);
        }
    }
}
