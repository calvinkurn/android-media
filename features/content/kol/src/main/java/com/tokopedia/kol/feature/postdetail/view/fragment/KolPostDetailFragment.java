package com.tokopedia.kol.feature.postdetail.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder;
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel;
import com.tokopedia.feedcomponent.view.widget.CardTitleView;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.KolRouter;
import com.tokopedia.kol.R;
import com.tokopedia.kol.analytics.KolEventTracking;
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.post.di.DaggerKolProfileComponent;
import com.tokopedia.kol.feature.post.di.KolProfileModule;
import com.tokopedia.kol.feature.post.domain.usecase.FollowKolPostGqlUseCase;
import com.tokopedia.kol.feature.post.domain.usecase.LikeKolPostUseCase;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactoryImpl;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.BaseKolViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.postdetail.view.activity.KolPostDetailActivity;
import com.tokopedia.kol.feature.postdetail.view.adapter.KolPostDetailAdapter;
import com.tokopedia.kol.feature.postdetail.view.adapter.typefactory.KolPostDetailTypeFactoryImpl;
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by yfsx on 23/07/18.
 */

public class KolPostDetailFragment extends BaseDaggerFragment
        implements KolPostDetailContract.View, KolPostListener.View.Like,
        KolPostListener.View.ViewHolder, KolComment.View.ViewHolder, KolComment.View.SeeAll,
        SwipeRefreshLayout.OnRefreshListener, DynamicPostViewHolder.DynamicPostListener, CardTitleView.CardTitleListener, ImagePostViewHolder.ImagePostListener, YoutubeViewHolder.YoutubePostListener, PollAdapter.PollOptionListener, GridPostAdapter.GridItemListener {

    private static final String PERFORMANCE_POST_DETAIL = "mp_explore_detail";
    private static final int OPEN_KOL_COMMENT = 101;
    private static final int OPEN_KOL_PROFILE = 13;

    private Integer postId;
    private SwipeToRefresh swipeToRefresh;
    private RecyclerView recyclerView;
    private ImageView likeButton, commentButton, shareButton;
    private TextView likeCount, commentCount, shareText;
    private View footer;
    private AbstractionRouter abstractionRouter;
    private KolRouter kolRouter;
    private PerformanceMonitoring performanceMonitoring;

    private KolPostViewModel kolPostViewModel;
    private boolean isTraceStopped;

    @Inject
    KolPostDetailContract.Presenter presenter;

    @Inject
    UserSessionInterface userSession;

    @Inject
    KolPostDetailAdapter adapter;

    public static KolPostDetailFragment getInstance(Bundle bundle) {
        KolPostDetailFragment fragment = new KolPostDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        if (getActivity() != null && getActivity().getApplication() != null) {
            DaggerKolProfileComponent.builder()
                    .kolComponent(KolComponentInstance.getKolComponent(getActivity().getApplication()))
                    .kolProfileModule(new KolProfileModule())
                    .build()
                    .inject(this);
        }
    }

    @Override
    protected String getScreenName() {
        return KolEventTracking.Screen.SCREEN_KOL_POST_DETAIL;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performanceMonitoring = PerformanceMonitoring.start(PERFORMANCE_POST_DETAIL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kol_post_detail, container, false);
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
        likeButton = view.findViewById(R.id.like_button);
        commentButton = view.findViewById(R.id.comment_button);
        shareButton = view.findViewById(R.id.share_button);
        likeCount = view.findViewById(R.id.like_count);
        commentCount = view.findViewById(R.id.comment_count);
        shareText = view.findViewById(R.id.share_text);
        footer = view.findViewById(R.id.footer);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null
                && getActivity().getApplicationContext() != null
                && getActivity().getApplicationContext() instanceof AbstractionRouter) {
            abstractionRouter = (AbstractionRouter) getActivity().getApplication();
        } else {
            throw new IllegalStateException("Application must be an instance of "
                    + AbstractionRouter.class.getSimpleName());
        }

        if (getActivity().getApplicationContext() instanceof KolRouter) {
            kolRouter = (KolRouter) getActivity().getApplicationContext();
        } else {
            throw new IllegalStateException("Application must be an instance of "
                    + KolRouter.class.getSimpleName());
        }

        initVar();

        swipeToRefresh.setOnRefreshListener(this);

        KolPostTypeFactoryImpl typeFactory = new KolPostTypeFactoryImpl(this);
        typeFactory.setType(KolPostViewHolder.Type.EXPLORE);
        adapter.setTypeFactory(new KolPostDetailTypeFactoryImpl(this, this, this,
                this, this, this, this, this, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        presenter.attachView(this);
        presenter.getCommentFirstTime(postId);
    }

    @Override
    public void onStart() {
        super.onStart();
        abstractionRouter.getAnalyticTracker().sendScreen(getActivity(), getScreenName());
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    private void initVar() {
        if (getArguments() != null) {
            try {
                postId = Integer.valueOf(getArguments().getString(
                        KolPostDetailActivity.PARAM_POST_ID,
                        KolPostDetailActivity.DEFAULT_POST_ID)
                );
            } catch (NumberFormatException e) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
                postId = 0;
            }
        }
    }

    @Override
    public void onSuccessGetKolPostDetail(List<Visitable> list, KolPostViewModel kolPostViewModel) {
        adapter.setList(list);
        setFooter(kolPostViewModel);
    }

    private void setFooter(KolPostViewModel postKol) {
        this.kolPostViewModel = postKol;

        footer.setVisibility(View.VISIBLE);

        setLikeListener(postKol.isLiked());
        setTotalLike(postKol.getTotalLike());
        setTotalComment(postKol.getTotalComment());

        commentCount.setOnClickListener(v -> onGoToKolComment(0, postKol.getContentId(), false, ""));
        commentButton.setOnClickListener(v -> onGoToKolComment(0, postKol.getContentId(), false, ""));

    }

    private void setTotalComment(int totalComment) {
        String commentCountText = totalComment == 0 ? getString(R.string.kol_action_comment) :
                String.valueOf(totalComment);
        commentCount.setText(commentCountText);
    }

    private void setTotalLike(int totalLike) {
        String likeCountText = totalLike == 0 ? getString(R.string.kol_action_like) :
                String.valueOf(totalLike);
        likeCount.setText(likeCountText);
    }

    @Override
    public void onErrorGetKolPostDetail(String message) {
        footer.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(
                getContext(),
                getView(),
                message,
                () -> presenter.getCommentFirstTime(postId)
        );
    }

    @Override
    public void showLoading() {
        if (!isLoading()) {
            swipeToRefresh.setRefreshing(true);
        }
    }

    @Override
    public void dismissLoading() {
        if (isLoading()) {
            swipeToRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onLikeKolSuccess(int rowNumber, int action) {
        if (action == LikeKolPostUseCase.ACTION_LIKE) {
            int totalLike = 0;
            if (!likeCount.getText().toString().equals(getString(R.string.kol_action_like))) {
                totalLike = Integer.valueOf(likeCount.getText().toString());
            }
            setTotalLike(totalLike + 1);
            ImageHandler.loadImageWithId(likeButton, R.drawable.ic_thumb_green);
        } else {
            int totalLike = 1;
            if (!likeCount.getText().toString().equals(getString(R.string.kol_action_like))) {
                totalLike = Integer.valueOf(likeCount.getText().toString());
            }
            setTotalLike(totalLike - 1);
            ImageHandler.loadImageWithId(likeButton, R.drawable.ic_thumb_gray);
        }

        setLikeListener(action == LikeKolPostUseCase.ACTION_LIKE);
    }

    private void setLikeListener(boolean isLiked) {
        //TODO ANALYTICS
        if (kolPostViewModel != null) {
            if (isLiked) {
                likeCount.setOnClickListener(v -> onUnlikeKolClicked(0, kolPostViewModel.getContentId(),
                        kolPostViewModel.isMultipleContent(),
                        kolPostViewModel.getActivityType()));
                likeButton.setOnClickListener(v -> onUnlikeKolClicked(0, kolPostViewModel.getContentId(),
                        kolPostViewModel.isMultipleContent(),
                        kolPostViewModel.getActivityType()));
            } else {
                likeCount.setOnClickListener(v -> onLikeKolClicked(0, kolPostViewModel.getContentId(),
                        kolPostViewModel.isMultipleContent(),
                        kolPostViewModel.getActivityType()));
                likeButton.setOnClickListener(v -> onLikeKolClicked(0, kolPostViewModel.getContentId(),
                        kolPostViewModel.isMultipleContent(),
                        kolPostViewModel.getActivityType()));
            }
        }
    }

    @Override
    public void onLikeKolError(String message) {
        showError(message);
    }

    @Override
    public UserSessionInterface getUserSession() {
        return userSession;
    }

    @Override
    public AbstractionRouter getAbstractionRouter() {
        return abstractionRouter;
    }

    @Override
    public void onGoToKolProfile(int rowNumber, String userId, int postId) {
        Intent intent = kolRouter.getTopProfileIntent(getContext(), userId);
        startActivityForResult(intent, OPEN_KOL_PROFILE);
    }

    @Override
    public void onGoToKolProfileUsingApplink(int rowNumber, String applink) {
        kolRouter.openRedirectUrl(getActivity(), applink);
    }

    @Override
    public void onOpenKolTooltip(int rowNumber, String uniqueTrackingId, String url) {
        kolRouter.openRedirectUrl(getActivity(), url);
    }

    @Override
    public void trackContentClick(boolean hasMultipleContent, String activityId, String
            activityType, String position) {

    }

    @Override
    public void trackTooltipClick(boolean hasMultipleContent, String activityId, String
            activityType, String position) {

    }

    @Override
    public void onFollowKolClicked(int rowNumber, int id) {
        if (userSession != null && userSession.isLoggedIn()) {
            presenter.followKol(id, rowNumber);
        } else {
            startActivity(kolRouter.getLoginIntent(getActivity()));
        }
    }

    @Override
    public void onUnfollowKolClicked(int rowNumber, int id) {
        if (userSession != null && userSession.isLoggedIn()) {
            presenter.unfollowKol(id, rowNumber);
        } else {
            startActivity(kolRouter.getLoginIntent(getActivity()));
        }
    }

    @Override
    public void onLikeKolClicked(int rowNumber, int id, boolean hasMultipleContent,
                                 String activityType) {
        if (userSession != null && userSession.isLoggedIn()) {
            presenter.likeKol(id, rowNumber, this);
        } else {
            startActivity(kolRouter.getLoginIntent(getActivity()));
        }
    }

    @Override
    public void onUnlikeKolClicked(int adapterPosition, int id, boolean hasMultipleContent,
                                   String activityType) {
        if (userSession != null && userSession.isLoggedIn()) {
            presenter.unlikeKol(id, adapterPosition, this);
        } else {
            startActivity(kolRouter.getLoginIntent(getActivity()));
        }
    }

    @Override
    public void onGoToKolComment(int rowNumber, int id, boolean hasMultipleContent,
                                 String activityType) {
        Intent intent = KolCommentActivity.getCallingIntent(getContext(), id, rowNumber);
        startActivityForResult(intent, OPEN_KOL_COMMENT);
    }

    @Override
    public void onGoToKolComment(int rowNumber, int id) {
        onGoToKolComment(rowNumber, id, false, "");
    }

    @Override
    public void onEditClicked(boolean hasMultipleContent, String activityId,
                              String activityType) {

    }

    @Override
    public void onMenuClicked(int rowNumber, BaseKolViewModel element) {

    }

    @Override
    public void onGoToProfile(String url) {
        kolRouter.openRedirectUrl(getActivity(), url);
    }

    @Override
    public boolean onDeleteCommentKol(String id, boolean canDeleteComment, int adapterPosition) {
        return false;
    }

    @Override
    public void onErrorFollowKol(String errorMessage, int id, int status, int rowNumber) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), errorMessage, () -> {
            if (status == FollowKolPostGqlUseCase.PARAM_UNFOLLOW) {
                presenter.unfollowKol(id, rowNumber);
            } else {
                presenter.followKol(id, rowNumber);
            }
        }).showRetrySnackbar();
    }

    @Override
    public void onSuccessFollowUnfollowKol(int rowNumber) {
        if (adapter.getList().get(rowNumber) instanceof KolPostViewModel) {
            KolPostViewModel kolPostViewModel = (KolPostViewModel) adapter.getList().get(rowNumber);
            kolPostViewModel.setFollowed(!(kolPostViewModel.isFollowed()));
            kolPostViewModel.setTemporarilyFollowed(!(kolPostViewModel.isTemporarilyFollowed()));
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_FOLLOW);

            if (kolPostViewModel.isFollowed()) {
                ToasterNormal
                        .make(swipeToRefresh,
                                getString(R.string.post_detail_follow_success_toast),
                                BaseToaster.LENGTH_LONG)
                        .setAction(getString(R.string.post_detail_follow_success_check_now),
                                followSuccessOnClickListener())
                        .show();
            }
        }
    }

    @Override
    public void stopTrace() {
        if (performanceMonitoring != null && !isTraceStopped) {
            performanceMonitoring.stopTrace();
            isTraceStopped = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case OPEN_KOL_COMMENT:
            case OPEN_KOL_PROFILE:
                presenter.getCommentFirstTime(postId);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        presenter.getCommentFirstTime(postId);
    }

    private boolean isLoading() {
        return swipeToRefresh.isRefreshing();
    }

    private void showError(String message) {
        if (message == null) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), message);
        }
    }

    private View.OnClickListener followSuccessOnClickListener() {
        return v -> {
            if (getContext() != null) RouteManager.route(getContext(), ApplinkConst.FEED);
        };
    }

    //NEW

    @Override
    public void onAvatarClick(int positionInFeed, @NotNull String redirectUrl) {

    }

    @Override
    public void onHeaderActionClick(int positionInFeed, @NotNull String id, @NotNull String type, boolean isFollow) {

    }

    @Override
    public void onMenuClick(int positionInFeed, int postId, boolean reportable, boolean deletable, boolean editable) {

    }

    @Override
    public void onCaptionClick(int positionInFeed, @NotNull String redirectUrl) {

    }

    @Override
    public void onLikeClick(int positionInFeed, int id, boolean isLiked) {

    }

    @Override
    public void onCommentClick(int positionInFeed, int id) {

    }

    @Override
    public void onShareClick(int positionInFeed, int id, @NotNull String title, @NotNull String description, @NotNull String url, @NotNull String iamgeUrl) {

    }

    @Override
    public void onFooterActionClick(int positionInFeed, @NotNull String redirectUrl) {

    }

    @Override
    public void onPostTagItemClick(int positionInFeed, @NotNull String redirectUrl) {

    }

    @Override
    public void onActionPopup() {

    }

    @Override
    public void onActionRedirect(@NotNull String redirectUrl) {

    }

    @Override
    public void onTitleCtaClick(@NotNull String redirectUrl) {

    }

    @Override
    public void onImageClick(int positionInFeed, int contentPosition, @NotNull String redirectLink) {

    }

    @Override
    public void onAffiliateTrackClicked(@NotNull List<TrackingViewModel> trackList) {

    }

    @Override
    public void onYoutubeThumbnailClick(int positionInFeed, int contentPosition, @NotNull String youtubeId) {

    }

    @Override
    public void onPollOptionClick(int positionInFeed, int contentPosition, int option, @NotNull String pollId, @NotNull String optionId, boolean isVoted, @NotNull String redirectLink) {

    }

    @Override
    public void onGridItemClick(int positionInFeed, int contentPosition, @NotNull String redirectLink) {

    }
}
