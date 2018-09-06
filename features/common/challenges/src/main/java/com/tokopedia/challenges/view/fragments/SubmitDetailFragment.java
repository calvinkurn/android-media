package com.tokopedia.challenges.view.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.challenges.ChallengesAnalytics;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.view.adapter.TouchImageAdapter;
import com.tokopedia.challenges.view.customview.CustomVideoPlayer;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.presenter.SubmitDetailContract;
import com.tokopedia.challenges.view.presenter.SubmitDetailPresenter;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.ToasterError;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

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
    private ScrollView scrollView;
    @Inject
    public ChallengesAnalytics analytics;

    public static Fragment newInstance() {
        return new SubmitDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        scrollView = view.findViewById(R.id.submit_detail_scrollview);

        isPastChallenge = getArguments().getBoolean("isPastChallenge", false);
        setClickListeners();
        submissionResult = getArguments().getParcelable("submissionsResult");
        fromSubmission = getArguments().getBoolean("fromSubmission");
        if (submissionResult == null) {
            submissionId = getArguments().getString(Utils.QUERY_PARAM_SUBMISSION_ID);
            presenter.getSubmissionDetails(submissionId);
        } else {
            hidProgressBar();
            submissionId = submissionResult.getId();
            presenter.setDataInFields(submissionResult);
            if (fromSubmission) {
                String mediaUrl;
                boolean isVideo;
//                if (TextUtils.isEmpty(submissionResult.getSharing().getAssets().getVideo())) {
////                    mediaUrl = submissionResult.getThumbnailUrl();
////                    isVideo = false;
////                } else {
////                    mediaUrl = submissionResult.getSharing().getAssets().getVideo();
////                    isVideo = true;
////                }
                ShareBottomSheet.showSubmissionShare(getActivity().getSupportFragmentManager(), submissionResult);
//                ShareBottomSheet.show(getActivity().getSupportFragmentManager(), submissionResult.getSharing().getMetaTags().getOgUrl(),
//                        submissionResult.getTitle(), submissionResult.getSharing().getMetaTags().getOgUrl(), submissionResult.getSharing().getMetaTags().getOgTitle(),
//                        submissionResult.getSharing().getMetaTags().getOgImage(), submissionResult.getId(), Utils.getApplinkPathForBranch(ChallengesUrl.AppLink.SUBMISSION_DETAILS,
//                                submissionResult.getId()), false, mediaUrl, submissionResult.getCollection().getHashTag(), isVideo);

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
        super.onResume();
        if (ChallegeneSubmissionFragment.VIDEO_POS != -1) {
            if (challengeImage != null)
                challengeImage.startPlay(ChallegeneSubmissionFragment.VIDEO_POS, ChallegeneSubmissionFragment.isVideoPlaying);
        }
    }

    @Override
    public void onPause() {
        if (challengeImage != null && challengeImage.isVideoPlaying()) {
            ChallegeneSubmissionFragment.VIDEO_POS = challengeImage.getPosition();
            ChallegeneSubmissionFragment.isVideoPlaying = false;
        }
        super.onPause();
    }

    private void setClickListeners() {
        btnShare.setOnClickListener(v -> {
            String mediaUrl;
            boolean isVideo;
//            if (TextUtils.isEmpty(submissionResult.getSharing().getAssets().getVideo())) {
//                mediaUrl = submissionResult.getThumbnailUrl();
//                isVideo = false;
//            } else {
//                mediaUrl = submissionResult.getSharing().getAssets().getVideo();
//                isVideo = true;
//            }
//            ShareBottomSheet.show((getActivity()).getSupportFragmentManager(), submissionResult.getSharing().getMetaTags().getOgUrl(), submissionResult.getTitle(), submissionResult.getSharing().getMetaTags().getOgUrl(),
//                    submissionResult.getSharing().getMetaTags().getOgTitle(), submissionResult.getSharing().getMetaTags().getOgImage(), submissionResult.getId(),
//                    Utils.getApplinkPathForBranch(ChallengesUrl.AppLink.SUBMISSION_DETAILS, submissionResult.getId()), false,mediaUrl,submissionResult.getCollection().getHashTag(),isVideo);
            ShareBottomSheet.showSubmissionShare((getActivity()).getSupportFragmentManager(), submissionResult);
            analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_SHARE,
                    ChallengesAnalytics.EVENT_CATEGORY_SUBMISSIONS,
                    ChallengesAnalytics.EVENT_ACTION_SHARE,
                    submissionResult.getCollection().getTitle());
        });

        likeBtn.setOnClickListener(v -> {
            presenter.likeBtnClick(submissionResult);
            String action = ChallengesAnalytics.EVENT_ACTION_LIKE;
            if (submissionResult.getMe() != null) {
                setLikes(!submissionResult.getMe().isLiked());
                action = ChallengesAnalytics.EVENT_ACTION_UNLIKE;
            }
            if (submissionResult.getCollection() != null) {
                analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_LIKE,
                        ChallengesAnalytics.EVENT_CATEGORY_SUBMISSIONS,
                        action, submissionResult.getCollection().getTitle());
            }
        });
    }

    public void showStatusInfo(String statusMessage) {
        statusView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(statusMessage))
            statusText.setText(statusMessage);
    }

    @Override
    public void setProfilePic(String url) {
        ImageHandler.loadImageCircle2(getActivity(), profilePic, url);
    }

    public void setProfileText(String text) {
        profileText.setText(text);
    }

    public void setChallengeImage(String thumbnailUrl, String videoUrl) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(getContext(), "Challenge Submission");
        String videoPath = localCacheHandler.getString(submissionId);
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
                    ImageViewerFragment fragemnt = ImageViewerFragment.newInstance(0, imageUrls);
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.add(R.id.image_viewer, fragemnt);
                    transaction.addToBackStack("ImageViewer");
                    transaction.commit();
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
        if ("Encoding".equalsIgnoreCase(approveText)) {
            approveText = Utils.STATUS_WAITING;
            statusMessage = "status is pending";
        }
        Utils.setTextViewBackground(getContext(), approvedView, approveText);
        if (Utils.STATUS_APPROVED.equalsIgnoreCase(approveText)) {
            likeBtn.setVisibility(View.VISIBLE);
        } else if (Utils.STATUS_DECLINED.equalsIgnoreCase(approveText)) {
            btnSubmit.setVisibility(View.VISIBLE);
            llShare.setVisibility(View.GONE);
            btnSubmit.setOnClickListener(v -> presenter.onSubmitButtonClick(submissionResult.getCollection().getId()));
        } else if (Utils.STATUS_WAITING.equalsIgnoreCase(approveText)) {
            showStatusInfo(statusMessage);
        } else if (Utils.STATUS_DECLINED.equalsIgnoreCase(approveText)) {
            showStatusInfo(statusMessage);
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
            hideShareAndLikeButtons();
        } else {
            this.participateTitle.setText(participateTitle);
            String applink = Utils.getApplinkPathWithPrefix(ChallengesUrl.AppLink.CHALLENGES_DETAILS, submissionResult.getCollection().getId());
            this.participateTitle.setOnClickListener(view -> RouteManager.route(getContext(), applink));
            showShareAndLikeButtons();
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
        if (submissionResult != null && !presenter.getParticipatedStatus(submissionResult)) {
            presenter.sendBuzzPointEvent(submissionResult.getId());
        }
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

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (presenter.getParticipatedStatus(submissionResult)) {
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
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete && submissionResult != null) {
            presenter.deleteSubmittedPost(submissionResult.getId(), submissionResult.getCollection().getId());
        }
        return true;
    }

    private void hideShareAndLikeButtons() {
        btnShare.setVisibility(View.GONE);
        likeBtn.setVisibility(View.GONE);
    }

    private void showShareAndLikeButtons() {
        btnShare.setVisibility(View.VISIBLE);
        likeBtn.setVisibility(View.VISIBLE);
    }
}
