package com.tokopedia.challenges.view.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.challenges.ChallengesAnalytics;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.customview.CustomVideoPlayer;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.SubmitDetailContract;
import com.tokopedia.challenges.view.presenter.SubmitDetailPresenter;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.ToasterError;

import javax.inject.Inject;

public class SubmitDetailFragment extends BaseDaggerFragment implements SubmitDetailContract.View, CustomVideoPlayer.CustomVideoPlayerListener {
    private LinearLayout statusView;
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
    private TextView detailTitle;
    private TextView detailContent;
    private TextView participateTitle;
    private TextView btnShare;
    private boolean isPastChallenge;
    private TextView btnSubmit;
    private View llShare;

    @Inject
    SubmitDetailPresenter presenter;
    private SubmissionResult submissionResult;
    private TextView participateTextView;
    private String submissionId;
    private View progressBarLayout;
    private boolean fromSubmission;
    private TextView tvWinnerNumber;
    private View progressBar;
    private Menu mMenu;
    @Inject
    public ChallengesAnalytics analytics;

    public static Fragment newInstance() {
        return new SubmitDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_submit_detail, container, false);

        statusView = view.findViewById(R.id.conditional_status);
        statusText = view.findViewById(R.id.status_text);

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

        detailTitle = view.findViewById(R.id.detail_title);
        detailContent = view.findViewById(R.id.detail_content);

        participateTitle = view.findViewById(R.id.participate_content);
        participateTextView = view.findViewById(R.id.participate_title);
        btnShare = view.findViewById(R.id.btn_share);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        btnSubmit = view.findViewById(R.id.btn_submit);
        llShare = view.findViewById(R.id.ll_share);
        tvWinnerNumber = view.findViewById(R.id.tv_winner_number);
        progressBar = view.findViewById(R.id.progress_bar);


        isPastChallenge = getArguments().getBoolean("isPastChallenge", false);
        setClickListeners();
        submissionResult = getArguments().getParcelable("submissionsResult");
        fromSubmission = getArguments().getBoolean("fromSubmission");
        if (submissionResult == null) {
            submissionId = getArguments().getString(Utils.QUERY_PARAM_SUBMISSION_ID);
            presenter.getSubmissionDetails(submissionId);
        } else {
            hidProgressBar();
            presenter.setDataInFields(submissionResult);
            if (fromSubmission) {
                ShareBottomSheet.show(getActivity().getSupportFragmentManager(), submissionResult.getSharing().getMetaTags().getOgUrl(), submissionResult.getTitle(), submissionResult.getSharing().getMetaTags().getOgUrl(), submissionResult.getSharing().getMetaTags().getOgTitle(), submissionResult.getSharing().getMetaTags().getOgImage(), submissionResult.getId(), Utils.getApplinkPathForBranch(ChallengesUrl.AppLink.SUBMISSION_DETAILS, submissionResult.getId()), false);

            }
        }


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ChallegeneSubmissionFragment.VIDEO_POS != -1) {
            if (challengeImage != null)
                challengeImage.startPlay(ChallegeneSubmissionFragment.VIDEO_POS);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (challengeImage != null)
            ChallegeneSubmissionFragment.VIDEO_POS = challengeImage.getPosition();
    }

    private void setClickListeners() {
        btnShare.setOnClickListener(v -> {
            analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_SHARE,
                    ChallengesAnalytics.EVENT_CATEGORY_SUBMISSIONS,
                    ChallengesAnalytics.EVENT_ACTION_SHARE,
                    submissionResult.getTitle());
            ShareBottomSheet.show((getActivity()).getSupportFragmentManager(), submissionResult.getSharing().getMetaTags().getOgUrl(), submissionResult.getTitle(), submissionResult.getSharing().getMetaTags().getOgUrl(), submissionResult.getSharing().getMetaTags().getOgTitle(), submissionResult.getSharing().getMetaTags().getOgImage(), submissionResult.getId(), Utils.getApplinkPathForBranch(ChallengesUrl.AppLink.SUBMISSION_DETAILS, submissionResult.getId()), false);

        });

        likeBtn.setOnClickListener(v -> {
            if (submissionResult.getMe() != null) {
                String action = ChallengesAnalytics.EVENT_ACTION_LIKE;
                if (submissionResult.getMe().isLiked())
                    action = ChallengesAnalytics.EVENT_ACTION_UNLIKE;
                analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_LIKE,
                        ChallengesAnalytics.EVENT_CATEGORY_SUBMISSIONS,
                        action, submissionResult.getTitle());
            }
            presenter.likeBtnClick(submissionResult);
            if (submissionResult.getMe() != null) {
                setLikes(!submissionResult.getMe().isLiked());
            }
        });
    }

    public void showStatusInfo() {
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
        challengeImage.setVideoThumbNail(thumbnailUrl, videoUrl, false, this);
    }

    public void setLikesCountView(String likesCount) {
        this.likesCountView.setText(likesCount);
    }

    public void setPointsView(String points) {
        this.pointsView.setText(points);
    }


    public void setApprovedView(String approveText) {
        Utils.setTextViewBackground(getContext(), approvedView, approveText);
        if (Utils.STATUS_APPROVED.equalsIgnoreCase(approveText)) {
            likeBtn.setVisibility(View.VISIBLE);
        } else if (Utils.STATUS_DECLINED.equalsIgnoreCase(approveText)) {
            btnSubmit.setVisibility(View.VISIBLE);
            llShare.setVisibility(View.GONE);
            btnSubmit.setOnClickListener(v -> presenter.onSubmitButtonClick(submissionResult.getCollection().getId()));
        } else if (Utils.STATUS_WAITING.equalsIgnoreCase(approveText)) {
            showStatusInfo();
        }
    }

    public void setDetailTitle(String title) {
        this.challengeTitle.setText(title);
    }

    public void setDetailContent(String content) {
        if (TextUtils.isEmpty(content)) {
            detailTitle.setVisibility(View.GONE);
            detailContent.setVisibility(View.GONE);
        } else {
            detailTitle.setVisibility(View.VISIBLE);
            detailContent.setVisibility(View.VISIBLE);
        }
        this.detailContent.setText(content);
    }

    public void setParticipateTitle(String participateTitle) {
        if (isPastChallenge) {
            this.participateTitle.setVisibility(View.GONE);
            this.participateTextView.setVisibility(View.GONE);
        } else {
            this.participateTitle.setText(participateTitle);
            String applink = Utils.getApplinkPathWithPrefix(ChallengesUrl.AppLink.CHALLENGES_DETAILS, submissionResult.getCollection().getId());
            this.participateTitle.setOnClickListener(view -> RouteManager.route(getContext(), applink));

        }
    }

    @Override
    public void updateLikeCount(boolean liked) {
        if (!TextUtils.isEmpty(likesCountView.getText())) {
            int count = Integer.parseInt(likesCountView.getText().toString());
            if (count >= 0 && liked) {
                likesCountView.setText("" + (count + 1));
            } else if (count > 0) {
                likesCountView.setText("" + (count - 1));
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

        submissionResult.getMe().setLiked(isLiked);
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

    @Override
    public void OnVideoStart() {
        presenter.sendBuzzPointEvent(submissionResult.getId());
    }

    @Override
    public void setSubmittResult(SubmissionResult submissionResult) {
        this.submissionResult = submissionResult;
    }

    @Override
    public void hidProgressBar() {
        progressBarLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDialogInfo(String title, String desc) {
        Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(title);
        dialog.setDesc(desc);
        dialog.setBtnOk("ok");
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        dialog.show();
    }

    @Override
    public void navigateToActivity(Intent intent) {
        getActivity().startActivity(intent);
    }

    @Override
    public void setSnackBarErrorMessage(String message) {
        View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);

        ToasterError
                .make(rootView,
                        message,
                        BaseToaster.LENGTH_LONG)
                .show();
    }

    @Override
    public void setWinnerPosition(String s) {
        tvWinnerNumber.setVisibility(View.VISIBLE);
        tvWinnerNumber.setText(s);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_submission_detail, menu);
        mMenu = menu;
        for (int i = 0; i < mMenu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString s = new SpannableString(item.getTitle());
            s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, s.length(), 0);
            s.setSpan(new StyleSpan(Typeface.NORMAL), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            item.setTitle(s);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete && submissionResult != null) {
            presenter.deleteSubmittedPost(submissionResult.getId());
        }
        return true;
    }
}
