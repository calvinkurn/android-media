package com.tokopedia.challenges.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.activity.ChallengeDetailsActivity;
import com.tokopedia.challenges.view.activity.SubmitDetailActivity;
import com.tokopedia.challenges.view.analytics.ChallengesGaAnalyticsTracker;
import com.tokopedia.challenges.view.customview.CustomVideoPlayer;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.SubmitDetailContract;
import com.tokopedia.challenges.view.presenter.SubmitDetailPresenter;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.challenges.view.utils.ChallengesCacheHandler;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.ToasterError;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by ashwanityagi on 06/08/18.
 */
public class SubmitDetailFragment extends BaseDaggerFragment implements SubmitDetailContract.View, CustomVideoPlayer.CustomVideoPlayerListener {
    private LinearLayout statusView;
    private TextView statusText;
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

    public interface ImageListener {
        void openImage(ArrayList<String> urls);
    }

    private ImageListener listener;

    @Inject
    SubmitDetailPresenter presenter;
    private SubmissionResult submissionResult;
    private TextView participateTextView;
    private String submissionId;
    private View progressBarLayout;
    private boolean fromSubmission;
    private TextView tvWinnerNumber;
    private ScrollView scrollView;
    @Inject
    public ChallengesGaAnalyticsTracker analytics;
    private final static String SCREEN_NAME = "challenges/submission_detail";


    public static Fragment newInstance() {
        return new SubmitDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        listener = (SubmitDetailActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_submit_detail, container, false);

        statusView = view.findViewById(R.id.conditional_status);
        statusText = view.findViewById(R.id.status_text);

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

        scrollView = view.findViewById(R.id.submit_detail_scrollview);

        isPastChallenge = getArguments().getBoolean(Utils.QUERY_PARAM_IS_PAST_CHALLENGE, false);
        setClickListeners();
        submissionResult = getArguments().getParcelable(Utils.QUERY_PARAM_SUBMISSION_RESULT);
        fromSubmission = getArguments().getBoolean(Utils.QUERY_PARAM_FROM_SUBMISSION);
        if (submissionResult == null) {
            submissionId = getArguments().getString(Utils.QUERY_PARAM_SUBMISSION_ID);
            ChallengesCacheHandler.resetCache();
            presenter.getSubmissionDetails(submissionId);
        } else {
            hidProgressBar();
            submissionId = submissionResult.getId();
            presenter.setDataInFields(submissionResult);
            if (fromSubmission) {
                ShareBottomSheet.show(getActivity().getSupportFragmentManager(), submissionResult, true);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY || scrollY < oldScrollY || scrollY == 0) {
                        if (challengeImage != null)
                            challengeImage.hideMediaController();
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onResume() {
        if (ChallengeDetailsFragment.VIDEO_POS != -1) {
            if (challengeImage != null)
                challengeImage.startPlay(ChallengeDetailsFragment.VIDEO_POS, ChallengeDetailsFragment.isVideoPlaying);
        }
        analytics.sendScreenEvent(getActivity(), SCREEN_NAME);
        super.onResume();

    }

    @Override
    public void onPause() {
        if (challengeImage != null && challengeImage.isVideoPlaying()) {
            challengeImage.pause();
            ChallengeDetailsFragment.VIDEO_POS = challengeImage.getPosition();
            ChallengeDetailsFragment.isVideoPlaying = false;
        }
        super.onPause();
    }

    private void setClickListeners() {
        btnShare.setOnClickListener(v -> {
            ShareBottomSheet.show((getActivity()).getSupportFragmentManager(), submissionResult, false);
            if (submissionResult.getCollection() != null && submissionResult.getMe() != null) {
                if (presenter.getParticipatedStatus(submissionResult)) {
                    analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_SHARE,
                            ChallengesGaAnalyticsTracker.EVENT_CATEGORY_MYSUBMISSIONS,
                            ChallengesGaAnalyticsTracker.EVENT_ACTION_SHARE,
                            submissionResult.getCollection().getTitle());
                } else {
                    analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_SHARE,
                            ChallengesGaAnalyticsTracker.EVENT_CATEGORY_OTHER_SUBMISSION,
                            ChallengesGaAnalyticsTracker.EVENT_ACTION_SHARE,
                            submissionResult.getCollection().getTitle());
                }
            }
        });

        likeBtn.setOnClickListener(v -> {
            presenter.likeBtnClick(submissionResult);

            if (submissionResult.getMe() != null) {
                setLikes(!submissionResult.getMe().isLiked());
            }

            if (submissionResult.getCollection() != null && submissionResult.getMe() != null) {
                String action = submissionResult.getMe().isLiked() ? ChallengesGaAnalyticsTracker.EVENT_ACTION_LIKE :
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_UNLIKE;
                analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_LIKE,
                        ChallengesGaAnalyticsTracker.EVENT_CATEGORY_SUBMISSIONS,
                        action, submissionResult.getCollection().getTitle());
            }
        });
    }

    @Override
    public void showStatusInfo(String statusMessage, boolean show) {
        if (show) {
            statusView.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(statusMessage))
                statusText.setText(statusMessage);
        }
    }

    @Override
    public void setProfilePic(String url) {
        ImageHandler.loadImageCircle2(getActivity(), profilePic, url);
    }

    public void setProfileText(String text) {
        profileText.setText(text);
    }

    public void setChallengeImage(String thumbnailUrl, String videoUrl) {
        String videoPath = ChallengesCacheHandler.getLocalVideoPath(getActivity(), submissionId);
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(thumbnailUrl);

        if (!TextUtils.isEmpty(videoPath)) {
            File file = new File(videoPath);
            if (file.exists())
                challengeImage.setVideoThumbNail(thumbnailUrl, videoPath, false, this, true);
        } else {
            challengeImage.setVideoThumbNail(thumbnailUrl, videoUrl, false, this, false);
        }

        if ((!TextUtils.isEmpty(thumbnailUrl) && TextUtils.isEmpty(videoUrl)) || (!TextUtils.isEmpty(videoPath) && Utils.isImage(videoPath))) {
            challengeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.openImage(imageUrls);
                }
            });
        }
    }

    public void setLikesCountView(String likesCount) {
        this.likesCountView.setText(likesCount);
    }

    public void setPointsView(String points) {
        this.pointsView.setText(points);
    }


    public void setApprovedView(String approveText, String statusMessage) {
        if (Utils.STATUS_ENCODING.equalsIgnoreCase(approveText)) {
            approveText = Utils.STATUS_WAITING;
            statusMessage = getString(R.string.ch_pending_status_msg);
        }
        Utils.setTextViewBackground(getContext(), approvedView, approveText);
        if (Utils.STATUS_APPROVED.equalsIgnoreCase(approveText)) {
            likeBtn.setVisibility(View.VISIBLE);
            showStatusInfo(null, false);
        } else if (Utils.STATUS_DECLINED.equalsIgnoreCase(approveText)) {
            btnSubmit.setVisibility(View.VISIBLE);
            llShare.setVisibility(View.GONE);
            showStatusInfo(statusMessage, true);
            btnSubmit.setOnClickListener(v -> presenter.onSubmitButtonClick(submissionResult.getCollection().getId()));
        } else if (Utils.STATUS_WAITING.equalsIgnoreCase(approveText)) {
            showStatusInfo(statusMessage, true);
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
            hideShareAndLikeButtons();
        } else {
            showShareAndLikeButtons();
        }

        this.participateTitle.setText(participateTitle);

        this.participateTitle.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ChallengeDetailsActivity.class);
            intent.putExtra(Utils.QUERY_PARAM_CHALLENGE_ID, submissionResult.getCollection().getId());
            intent.putExtra(Utils.QUERY_PARAM_IS_PAST_CHALLENGE, isPastChallenge);
            navigateToActivity(intent);
        });
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
        if (submissionResult != null && !presenter.getParticipatedStatus(submissionResult)) {
            presenter.sendBuzzPointEvent(submissionResult.getId());
        }
    }

    @Override
    public void setSubmittResult(SubmissionResult submissionResult) {
        this.submissionResult = submissionResult;
        getActivity().invalidateOptionsMenu();
        isPastChallenge = presenter.checkIsPastChallenge(submissionResult.getCollection());
    }

    @Override
    public void hidProgressBar() {
        progressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDialogInfo(String title, String desc) {
        if (getActivity() != null) {
            Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
            dialog.setTitle(title);
            dialog.setDesc(desc);
            dialog.setBtnOk("OK");
            dialog.setOnOkClickListener(v -> getActivity().finish());
            dialog.show();
        }
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

    @Override
    public void isParticipated(boolean participated) {
        if (participated) {
            likesCountView.setVisibility(View.VISIBLE);
            approvedView.setVisibility(View.VISIBLE);
            likesImageView.setVisibility(View.VISIBLE);

        } else {
            likesCountView.setVisibility(View.GONE);
            approvedView.setVisibility(View.GONE);
            likesImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setResubmitInvisible() {
        btnSubmit.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (presenter.getParticipatedStatus(submissionResult)) {
            inflater.inflate(R.menu.menu_submission_detail, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete && submissionResult != null) {
            showDeleteDialog();
        }
        return true;
    }

    private void hideShareAndLikeButtons() {
        llShare.setVisibility(View.GONE);
        likeBtn.setVisibility(View.GONE);
        setResubmitInvisible();
    }

    private void showShareAndLikeButtons() {
        btnShare.setVisibility(View.VISIBLE);
    }

    private void showDeleteDialog() {
        Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.ch_delete_warning_msg));
        dialog.setBtnOk(getString(R.string.ch_delete_label));
        dialog.setBtnCancel(getString(R.string.ch_cancel));
        dialog.show();
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteSubmittedPost(submissionResult.getId(), submissionResult.getCollection().getId(), submissionResult.getCollection().getTitle());
                dialog.dismiss();
            }
        });
        dialog.setOnCancelClickListener(v -> dialog.dismiss());

    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }
}
