package com.tokopedia.kol.feature.postdetail.view.fragment;

import static com.tokopedia.kolcommon.util.PostMenuUtilKt.createBottomMenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalContent;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.Menus;
import com.tokopedia.feedcomponent.analytics.posttag.PostTagAnalytics;
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker;
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard;
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXLike;
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia;
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct;
import com.tokopedia.feedcomponent.data.pojo.FeedPostRelated;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItemShop;
import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist;
import com.tokopedia.feedcomponent.util.FeedScrollListener;
import com.tokopedia.feedcomponent.util.util.ShareBottomSheets;
import com.tokopedia.feedcomponent.view.adapter.viewholder.highlight.HighlightAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.relatedpost.RelatedPostAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsBannerViewHolder;
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel;
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightCardViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.mention.MentionableUserViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel;
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModelNew;
import com.tokopedia.feedcomponent.view.viewmodel.relatedpost.RelatedPostViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticCommissionUiModel;
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticDetailType;
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel;
import com.tokopedia.feedcomponent.view.widget.CardTitleView;
import com.tokopedia.feedcomponent.view.widget.CreatePostFabView;
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView;
import com.tokopedia.feedcomponent.view.widget.PostStatisticBottomSheet;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.R;
import com.tokopedia.kol.analytics.KolEventTracking;
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.post.di.DaggerKolProfileComponent;
import com.tokopedia.kol.feature.post.di.KolProfileModule;
import com.tokopedia.kol.feature.postdetail.view.activity.KolPostDetailActivity;
import com.tokopedia.kol.feature.postdetail.view.adapter.KolPostDetailAdapter;
import com.tokopedia.kol.feature.postdetail.view.adapter.typefactory.KolPostDetailTypeFactory;
import com.tokopedia.kol.feature.postdetail.view.adapter.typefactory.KolPostDetailTypeFactoryImpl;
import com.tokopedia.kol.feature.postdetail.view.analytics.KolPostDetailAnalytics;
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.postdetail.view.viewmodel.PostDetailViewModel;
import com.tokopedia.kol.feature.report.view.activity.ContentReportActivity;
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase;
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase;
import com.tokopedia.kolcommon.util.PostMenuListener;
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.track.TrackApp;
import com.tokopedia.unifycomponents.Toaster;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import io.embrace.android.embracesdk.Embrace;
import kotlin.Unit;

/**
 * @author by yfsx on 23/07/18.
 */

public class KolPostDetailFragment extends BaseDaggerFragment
        implements KolPostDetailContract.View, KolPostLikeListener,
        KolComment.View.ViewHolder, KolComment.View.SeeAll,
        SwipeRefreshLayout.OnRefreshListener,
        DynamicPostViewHolder.DynamicPostListener,
        CardTitleView.CardTitleListener,
        ImagePostViewHolder.ImagePostListener,
        YoutubeViewHolder.YoutubePostListener,
        PollAdapter.PollOptionListener,
        GridPostAdapter.GridItemListener,
        VideoViewHolder.VideoViewListener,
        FeedMultipleImageView.FeedMultipleImageViewListener,
        RelatedPostAdapter.RelatedPostListener,
        HighlightAdapter.HighlightListener, TopAdsBannerViewHolder.TopAdsBannerListener {

    @NotNull
    @Override
    public Context getAndroidContext() {
        return requireContext();
    }

    private static final String PERFORMANCE_POST_DETAIL = "mp_explore_detail";
    private static final int OPEN_KOL_COMMENT = 101;
    private static final int OPEN_KOL_PROFILE = 13;
    private static final int OPEN_CONTENT_REPORT = 1310;
    private static final int OPEN_VIDEO_DETAIL = 1311;
    private static final String PARAM_VIDEO_INDEX = "video_index";


    private Integer postId;
    private SwipeToRefresh swipeToRefresh;
    private RecyclerView recyclerView;
    private CreatePostFabView createPostFab;
    private PerformanceMonitoring performanceMonitoring;

    private PostStatisticBottomSheet postStatisticBottomSheet;

    private DynamicPostUiModel dynamicPostUiModel;
    private boolean isTraceStopped;

    @Inject
    KolPostDetailContract.Presenter presenter;

    @Inject
    UserSessionInterface userSession;

    @Inject
    PostTagAnalytics postTagAnalytics;

    @Inject
    KolPostDetailAnalytics analytics;

    @Inject
    FeedAnalyticTracker feedAnalytics;

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
        Embrace.getInstance().startEvent(PERFORMANCE_POST_DETAIL, null, false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kol_post_detail, container, false);
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
        createPostFab = view.findViewById(R.id.create_post_fab);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar();

        swipeToRefresh.setOnRefreshListener(this);

        KolPostDetailTypeFactory typeFactory = new KolPostDetailTypeFactoryImpl(this,
                this,
                this, this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this,
                this, userSession);
        adapter = new KolPostDetailAdapter(typeFactory);
        recyclerView.setAdapter(adapter);

        presenter.attachView(this);
        presenter.getCommentFirstTime(postId);
    }

    @Override
    public void onStart() {
        super.onStart();
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(getScreenName());
    }

    @Override
    public void onPause() {
        super.onPause();
        feedAnalytics.sendPendingAnalytics();
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
    public void onEmptyDetailFeed() {
        adapter.showEmpty();
    }

    @Override
    public void onEmptyDetailClicked() {
        getActivity().finish();
    }

    private void finishActivity() {
        if (getActivity() != null && !getActivity().isTaskRoot()) {
            getActivity().finish();
        } else if (getActivity() != null) {
            getActivity().finish();
            RouteManager.route(getContext(), ApplinkConst.HOME);
        }
    }

    @Override
    public void onSuccessGetKolPostDetail(List<Visitable> list,
                                          PostDetailViewModel postDetailViewModel) {
        adapter.setList(list);
        if (!postDetailViewModel.getDynamicPostViewModel().getPostList().isEmpty()) {
            this.dynamicPostUiModel = ((DynamicPostUiModel) postDetailViewModel.getDynamicPostViewModel().getPostList().get(0));
            trackImpression(dynamicPostUiModel);
            if(!GlobalConfig.isSellerApp()) {
                presenter.getRelatedPost((dynamicPostUiModel.getFeedXCard().getId()));
            }

            if (isOwner()) {
                presenter.getWhitelist();
            }
        }
        recyclerView.clearOnScrollListeners();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    FeedScrollListener.onFeedScrolled(recyclerView, list);
                }
            }
        });
    }

    private void trackImpression(DynamicPostUiModel dynamicPostUiModel) {
        if (dynamicPostUiModel.getFeedXCard().getMedia().size() != 0 && !dynamicPostUiModel.getFeedXCard().getMedia().isEmpty()) {
            if (isOwner()) {
                postTagAnalytics.trackViewPostTagProfileDetailSelf(
                        Integer.parseInt(dynamicPostUiModel.getFeedXCard().getId()),
                        dynamicPostUiModel.getFeedXCard().getTags(),
                        dynamicPostUiModel.getTrackingPostModel());
            } else {
                postTagAnalytics.trackViewPostTagProfileDetailOther(
                        Integer.parseInt(dynamicPostUiModel.getFeedXCard().getId()),
                        dynamicPostUiModel.getFeedXCard().getTags(),
                        dynamicPostUiModel.getFeedXCard().getAuthor().getType(),
                        dynamicPostUiModel.getTrackingPostModel());

            }
        }
    }

    private boolean isOwner() {
        return dynamicPostUiModel != null
                && userSession.getUserId().equals(
                dynamicPostUiModel.getFeedXCard().getAuthor().getId());
    }

    @Override
    public void onErrorGetKolPostDetail(String message) {
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
        adapter.hideLoading();
    }

    @Override
    public void showLoadingMore() {
        adapter.showLoading();
    }

    @Override
    public void onLikeKolSuccess(int rowNumber, LikeKolPostUseCase.LikeKolPostAction action) {
        if (adapter.getList().size() > rowNumber && adapter.getList().get(rowNumber) instanceof DynamicPostUiModel) {
            DynamicPostUiModel dynamicPostUiModel = (DynamicPostUiModel) adapter.getList().get(rowNumber);
            FeedXLike like = dynamicPostUiModel.getFeedXCard().getLike();
            like.setLiked(!like.isLiked());
            if (like.isLiked()) {
                try {
                    int likeValue = Integer.parseInt(like.getCountFmt()) + 1;
                    like.setCountFmt(String.valueOf(likeValue));
                } catch (NumberFormatException ignored) {}

                like.setCount(like.getCount() + 1);
            } else {
                try {
                    int likeValue = Integer.parseInt(like.getCountFmt()) - 1;
                    like.setCountFmt(String.valueOf(likeValue));
                } catch (NumberFormatException ignored) {}

                like.setCount(like.getCount() - 1);
            }
            adapter.notifyItemChanged(rowNumber, DynamicPostViewHolder.PAYLOAD_LIKE);
        }
    }

    @Override
    public void onLikeKolError(String message) {
        showError(message);
    }

    public void onFollowKolClicked(int rowNumber, int id) {
        if (userSession != null && userSession.isLoggedIn()) {
            presenter.followKol(id, rowNumber);
        } else {
            RouteManager.route(getActivity(), ApplinkConst.LOGIN);
        }
    }

    public void onUnfollowKolClicked(int rowNumber, int id) {
        if (userSession != null && userSession.isLoggedIn()) {
            presenter.unfollowKol(id, rowNumber);
        } else {
            RouteManager.route(getActivity(), ApplinkConst.LOGIN);
        }
    }

    public void onLikeKolClicked(int rowNumber, int id) {
        analytics.eventClickLike(userSession.getUserId());
        if (userSession != null && userSession.isLoggedIn()) {
            presenter.likeKol(id, rowNumber, this);
        } else {
            RouteManager.route(getActivity(), ApplinkConst.LOGIN);
        }
    }

    public void onUnlikeKolClicked(int adapterPosition, int id) {
        analytics.eventClickLike(userSession.getUserId());
        if (userSession != null && userSession.isLoggedIn()) {
            presenter.unlikeKol(id, adapterPosition, this);
        } else {
            RouteManager.route(getActivity(), ApplinkConst.LOGIN);
        }
    }

    @Override
    public void onGoToKolComment(int rowNumber, int id) {
        analytics.eventClickComment(userSession.getUserId());
        if (userSession != null && userSession.isLoggedIn()) {
            Intent intent = KolCommentActivity.getCallingIntent(getContext(), id, rowNumber);
            startActivityForResult(intent, OPEN_KOL_COMMENT);
        } else {
            RouteManager.route(getActivity(), ApplinkConst.LOGIN);
        }
    }

    @Override
    public void onGoToProfile(String url, String userId) {
        onGoToLink(url);
    }

    @Override
    public void onClickMentionedProfile(String id) {
        onGoToLink(ApplinkConst.PROFILE.replace(ApplinkConst.Profile.PARAM_USER_ID, id));
    }

    @Override
    public void replyToUser(MentionableUserViewModel user) {

    }

    @Override
    public boolean onDeleteCommentKol(String id, boolean canDeleteComment, int adapterPosition) {
        return false;
    }

    @Override
    public void onMenuClicked(String id, boolean canDeleteComment, int adapterPosition) {

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
        if (adapter.getList().get(rowNumber) instanceof DynamicPostUiModel) {
            DynamicPostUiModel dynamicPostViewModel = (DynamicPostUiModel) adapter.getList().get(rowNumber);
            dynamicPostViewModel.getFeedXCard().getFollowers().setFollowed(!dynamicPostViewModel.getFeedXCard().getFollowers().isFollowed());
            adapter.notifyItemChanged(rowNumber, DynamicPostViewHolder.PAYLOAD_FOLLOW);

            if (dynamicPostViewModel.getFeedXCard().getFollowers().isFollowed()) {
                Toaster.INSTANCE.make(getView(), getString(R.string.post_detail_follow_success_toast), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, "", v->{});
            }
        }
    }

    @Override
    public void stopTrace() {
        if (performanceMonitoring != null && !isTraceStopped) {
            performanceMonitoring.stopTrace();
            Embrace.getInstance().endEvent(PERFORMANCE_POST_DETAIL);
            isTraceStopped = true;
        }
    }

    @Override
    public void onErrorToggleFavoriteShop(String errorMessage, String shopId) {
        Toaster.INSTANCE.make(getView(), errorMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                getString(com.tokopedia.abstraction.R.string.title_try_again), v -> presenter.toggleFavoriteShop(shopId));
    }

    @Override
    public void onSuccessToggleFavoriteShop() {
        if (adapter.getList().get(0) instanceof DynamicPostUiModel) {
            DynamicPostUiModel model = (DynamicPostUiModel) adapter.getList().get(0);
            model.getFeedXCard().getFollowers().setFollowed(
                    !model.getFeedXCard().getFollowers().isFollowed()
            );
            adapter.notifyItemChanged(0, DynamicPostViewHolder.PAYLOAD_FOLLOW);
        }
    }

    @Override
    public void onErrorDeletePost(Throwable e) {
        NetworkErrorHelper.showSnackbar(getActivity(), ErrorHandler.getErrorMessage(getContext(), e));
    }

    @Override
    public void onSuccessDeletePost(int rowNumber) {
        if (getActivity() != null && !getActivity().isTaskRoot()) {
            getActivity().setResult(KolPostDetailActivity.RESULT_DELETED);
            getActivity().finish();
        } else if (getActivity() != null) {
            getActivity().setResult(KolPostDetailActivity.RESULT_DELETED);
            getActivity().finish();
            RouteManager.route(getContext(), ApplinkConst.HOME);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case OPEN_KOL_COMMENT:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.getCommentFirstTime(postId);
                }
                break;
            case OPEN_KOL_PROFILE:
                presenter.getCommentFirstTime(postId);
                break;
            case OPEN_CONTENT_REPORT:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getBooleanExtra(ContentReportActivity.RESULT_SUCCESS, false)) {
                        onSuccessReportContent();
                    } else {
                        onErrorReportContent(
                                data.getStringExtra(ContentReportActivity.RESULT_ERROR_MSG)
                        );
                    }
                }
            case OPEN_VIDEO_DETAIL: {
                if (resultCode == Activity.RESULT_OK) {
                    onRefresh();
                }
            }
                break;
            default:
                break;
        }
    }


    private void onSuccessReportContent() {
        Toaster.INSTANCE.make(getView(), getString(com.tokopedia.feedcomponent.R.string.feed_content_reported),
                Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL, getString(com.tokopedia.design.R.string.label_close), v -> {});
    }

    private void onErrorReportContent(String errorMsg) {
        Toaster.INSTANCE.make(getView(), errorMsg, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                getString(com.tokopedia.design.R.string.label_close), v->{});
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

    @Override
    public void onAvatarClick(int positionInFeed, @NonNull String redirectUrl, int activityId, @NonNull String activityName, @NonNull FollowCta followCta, @NonNull String type, boolean isFollowed, @NonNull String shopId, String mediaType, boolean isCaption) {

        if (followCta.getAuthorType().equals(FollowCta.AUTHOR_SHOP)) {
            feedAnalytics.eventContentDetailClickShopNameAvatar(String.valueOf(activityId), followCta.getAuthorID());
        } else {
            feedAnalytics.eventContentDetailClickAffiliateNameAvatar(String.valueOf(activityId), followCta.getAuthorID());
        }
        onGoToLink(redirectUrl);
    }

    @Override
    public void onHeaderActionClick(int positionInFeed, @NotNull String id, @NotNull String type, boolean isFollow, @NotNull String postType, boolean isVideo) {

        if (userSession != null && userSession.isLoggedIn()) {

            if (type.equals(FollowCta.AUTHOR_USER)) {
                int userIdInt = 0;
                try {
                    userIdInt = Integer.valueOf(id);
                } catch (NumberFormatException ignored) {
                }

                if (isFollow) {
                    onUnfollowKolClicked(positionInFeed, userIdInt);
                } else {
                    onFollowKolClicked(positionInFeed, userIdInt);
                }

            } else if (type.equals(FollowCta.AUTHOR_SHOP)) {
                presenter.toggleFavoriteShop(id);
            }

        } else {
            RouteManager.route(getActivity(), ApplinkConst.LOGIN);
        }
    }

    @Override
    public void onMenuClick(int positionInFeed, int postId, boolean reportable, boolean deletable, boolean editable, boolean isFollowed, @NonNull String authorId, @NonNull String authorType, @NonNull String postType,  @NonNull String mediaType, @NonNull String caption, @NonNull String playChannelId) {
        if (getContext() != null) {
            Menus menus = createBottomMenu(getContext(), deletable,
                    reportable, false, new PostMenuListener() {
                        @Override
                        public void onDeleteClicked() {
                            createDeleteDialog(positionInFeed, postId).show();
                        }

                        @Override
                        public void onReportClick() {
                            if (userSession != null && userSession.isLoggedIn()) {
                                goToContentReport(postId);
                            } else {
                                RouteManager.route(getActivity(), ApplinkConst.LOGIN);
                            }
                        }

                        @Override
                        public void onEditClick() {

                        }
                    });
            menus.show();
        }
    }

    private Dialog createDeleteDialog(int positionInFeed, int postId) {
        Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(com.tokopedia.feedcomponent.R.string.card_dialog_delete_post));
        dialog.setDesc(getString(com.tokopedia.feedcomponent.R.string.card_dialog_after_delete_cant));
        dialog.setBtnOk(getString(com.tokopedia.feedcomponent.R.string.card_dialog_title_delete));
        dialog.setBtnCancel(getString(com.tokopedia.feedcomponent.R.string.card_dialog_title_cancel));
        dialog.setOnOkClickListener(v -> {
            presenter.deletePost(postId, positionInFeed);
            dialog.dismiss();
        });
        dialog.setOnCancelClickListener(v -> dialog.dismiss());
        return dialog;
    }

    private void goToContentReport(int contentId) {
        if (getContext() != null) {
            Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalContent.CONTENT_REPORT, String.valueOf(contentId));
            startActivityForResult(intent, OPEN_CONTENT_REPORT);
        }
    }

    @Override
    public void onCaptionClick(int positionInFeed, @NotNull String redirectUrl) {
        onGoToLink(redirectUrl);
    }

    @Override
    public void onCommentClick(int positionInFeed, int id, @NotNull String authorType, @NotNull String type, boolean isFollowed, @NotNull String mediaType, @NotNull String shopId,@NotNull String playChannelId, boolean isIconClick) {
        onGoToKolComment(positionInFeed, id);
    }

    @Override
    public void onLikeClick(int positionInFeed, int columnNumber, int id, boolean isLiked) {

    }

    @Override
    public void onCommentClick(int positionInFeed, int columnNumber, int id) {

    }

    @Override
    public void onShareClick(int positionInFeed, int id, @NotNull String title, @NotNull String description, @NotNull String url, @NotNull String imageUrl, boolean postTypeASGC, @NotNull String type, boolean isFollowed, @NotNull String shopId, @NotNull String mediaType , boolean isTopads, @NotNull String playChannelId) {
        if (getActivity() != null) {
            ShareBottomSheets.Companion.newInstance(packageName -> {
            }, "", imageUrl, url, description, title, "").show(getActivity().getSupportFragmentManager());
        }
    }

    @Override
    public void onStatsClick(@NotNull String title, @NotNull String activityId, @NotNull List<String> productIds, int likeCount, int commentCount) {
        showPostStatistic(title, activityId, productIds, likeCount, commentCount);
    }

    @Override
    public void onSuccessGetPostStatistic(@NotNull PostStatisticCommissionUiModel statisticCommissionModel) {
        getPostStatisticBottomSheet()
                .setPostStatisticCommissionModel(statisticCommissionModel);
    }

    @Override
    public void onErrorGetPostStatistic(@NotNull Throwable error, @NotNull String activityId, @NotNull List<String> productIds) {
        getPostStatisticBottomSheet()
                .setError(error, activityId, productIds);
    }

    private void doShare(String body, String title) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(
                Intent.createChooser(sharingIntent, title)
        );
    }

    @Override
    public void onFooterActionClick(int positionInFeed, @NotNull String redirectUrl) {
        onGoToLink(redirectUrl);
    }

    @Override
    public void onPostTagItemClick(int positionInFeed, @NotNull String redirectUrl, @NotNull PostTagItem postTagItem, int itemPosition) {
        onGoToLink(redirectUrl);

        if (isOwner()) {
            postTagAnalytics.trackClickPostTagProfileDetailSelf(
                    Integer.parseInt(dynamicPostUiModel.getFeedXCard().getId()),
                    postTagItem,
                    itemPosition,
                    dynamicPostUiModel.getTrackingPostModel());
        } else {
            postTagAnalytics.trackClickPostTagProfileDetailOther(
                    Integer.parseInt(dynamicPostUiModel.getFeedXCard().getId()),
                    postTagItem,
                    itemPosition,
                    String.valueOf(dynamicPostUiModel.getFeedXCard().getAuthor().getType()),
                    dynamicPostUiModel.getTrackingPostModel());
        }
    }

    @Override
    public void onActionPopup() {
        //Supposedly not exist in detail
    }

    @Override
    public void onActionRedirect(@NotNull String redirectUrl) {
        onGoToLink(redirectUrl);
    }

    @Override
    public void onTitleCtaClick(@NotNull String redirectUrl, int adapterPosition) {
        onGoToLink(redirectUrl);

    }

    @Override
    public void onImageClick(int positionInFeed, int contentPosition, @NotNull String redirectLink) {
        onGoToLink(redirectLink);
    }

    @Override
    public void onMediaGridClick(int positionInFeed, int contentPosition,
                                 @NotNull String redirectLink, boolean isSingleItem) {
        if (!isSingleItem && getActivity() != null){
            String appLinkPatternWithQueryParams = ApplinkConstInternalContent.INTERNAL_MEDIA_PREVIEW.replace(ApplinkConstInternalContent.DUMMY_MEDIA_INDEX,
                    String.valueOf(contentPosition));
            RouteManager.route(getContext(), appLinkPatternWithQueryParams, postId.toString());
        }
    }

    @Override
    public void onAffiliateTrackClicked(@NotNull List<TrackingViewModel> trackList, boolean isClick) {
        for (TrackingViewModel track : trackList) {
            if (isClick) {
                presenter.trackAffiliate(track.getClickURL());
            } else {
                presenter.trackAffiliate(track.getViewURL());
            }
        }
    }

    @Override
    public void onHighlightItemClicked(int positionInFeed, @NotNull HighlightCardViewModel item) {

    }

    @Override
    public void onPostTagItemBuyClicked(int positionInFeed, @NotNull PostTagItem postTagItem, @NotNull String authorType) {
        try {
            PostTagItemShop shop = postTagItem.getShop().get(0);
            feedAnalytics.eventContentDetailAddToCart(
                    postTagItem.getId(),
                    postTagItem.getText(),
                    postTagItem.getPrice(),
                    1,
                    Integer.parseInt(shop.getShopId()),
                    "",
                    authorType
            );
        } catch (Exception ignored) {}
        presenter.addPostTagItemToCart(positionInFeed, postTagItem);
    }

    @Override
    public void onYoutubeThumbnailClick(int positionInFeed, int contentPosition, @NotNull String youtubeId) {
        String YOUTUBE_URL = "{youtube_url}";
        String redirectUrl = ApplinkConst.KOL_YOUTUBE.replace(YOUTUBE_URL, youtubeId);

        if (getContext() != null) {
            RouteManager.route(
                    getContext(),
                    redirectUrl
            );
        }

    }

    @Override
    public void onPollOptionClick(int positionInFeed, int contentPosition, int option,
                                  @NotNull String pollId, @NotNull String optionId, boolean isVoted,
                                  @NotNull String redirectLink) {
        if (userSession != null && userSession.isLoggedIn()) {
            if (isVoted) {
                onGoToLink(redirectLink);
            } else {

            }
        } else {
            RouteManager.route(getActivity(), ApplinkConst.LOGIN);
        }

    }

    @Override
    public void onGridItemClick(int positionInFeed, int activityId, @NonNull String productId, @NonNull String redirectLink, @NonNull String type, boolean isFollowed, @NonNull String shopId, @NonNull List<FeedXProduct> feedXProducts, int index) {

        onGoToLink(redirectLink);
    }

    @Override
    public void onVideoPlayerClicked(int positionInFeed, int contentPosition, @NotNull String postId, @NotNull String redirectUrl, @NotNull String authorId, @NotNull String authorType, boolean isFollowed) {
        Intent videoDetailIntent = RouteManager.getIntent(getContext(), ApplinkConstInternalContent.VIDEO_DETAIL, postId);
        videoDetailIntent.putExtra(PARAM_VIDEO_INDEX, contentPosition);
        startActivityForResult(videoDetailIntent, OPEN_VIDEO_DETAIL);
    }

    @Override
    public void onAddToCartSuccess(int positionInFeed, PostTagItem postTagItem) {
        RouteManager.route(getContext(), ApplinkConstInternalMarketplace.CART);
    }

    @Override
    public void onAddToCartFailed(String pdpAppLink) {
        onGoToLink(pdpAppLink);
    }

    @Override
    public void onSuccessGetRelatedPost(RelatedPostViewModel relatedPostViewModel) {
        adapter.addElement(relatedPostViewModel);
        adapter.hideLoading();
    }

    @Override
    public void onSuccessGetWhitelist(Whitelist whitelist) {
        if (!whitelist.getAuthors().isEmpty()) {
            createPostFab.render(whitelist);
            createPostFab.setOnFabBymeClicked(() -> {
                goToCreateAffiliate();
                return Unit.INSTANCE;
            });
            createPostFab.setOnFabLinkClicked((link) -> {
                onGoToLink(link);
                return Unit.INSTANCE;
            });
        }
    }

    @Override
    public void onRelatedPostImpression(@NotNull FeedPostRelated.Datum post) {
        analytics.eventImpressionOtherPost(post.getId());
    }

    @Override
    public void onRelatedPostClicked(@NotNull FeedPostRelated.Datum post) {
        if (!post.getContent().getBody().getMedia().isEmpty()) {
            RouteManager.route(getContext(), post.getContent().getBody().getMedia().get(0).getApplink());
        }
        analytics.eventClickOtherPost(post.getId());
    }

    @Override
    public void onHashtagClicked(@NotNull String hashtagText, @NotNull TrackingPostModel trackingPostModel) {
        feedAnalytics.eventDetailClickHashtag(
                String.valueOf(trackingPostModel.getPostId()),
                trackingPostModel.getActivityName(),
                trackingPostModel.getMediaType(),
                hashtagText
        );
    }

    @Override
    public void onReadMoreClicked(@NotNull TrackingPostModel trackingPostModel) {

    }

    private void onGoToLink(String link) {
        if (getActivity() != null && !TextUtils.isEmpty(link)) {
            if (RouteManager.isSupportApplink(getActivity(), link)) {
                Intent intent = RouteManager.getIntent(getActivity(), link);
                intent.putExtras(new Bundle());
                startActivity(intent);
            } else {
                RouteManager.route(
                        getActivity(),
                        String.format("%s?url=%s", ApplinkConst.WEBVIEW, link)
                );
            }
        }
    }

    private void goToCreateAffiliate() {
        if (getContext() != null) {
            Intent intent = RouteManager.getIntent(getContext(), ApplinkConst.AFFILIATE_CREATE_POST, "-1", "-1");
            intent.putExtras(new Bundle());
            startActivity(intent);
        }
    }

    private PostStatisticBottomSheet getPostStatisticBottomSheet() {
        if (postStatisticBottomSheet == null) {
            postStatisticBottomSheet = PostStatisticBottomSheet.newInstance(requireContext());
        }
        return postStatisticBottomSheet;
    }

    private void showPostStatistic(String title, String activityId, List<String> productIds, int likeCount, int commentCount) {
        getPostStatisticBottomSheet()
                .show(
                        requireFragmentManager(),
                        activityId,
                        title,
                        productIds,
                        new PostStatisticBottomSheet.Listener() {
                            @Override
                            public void onGetPostStatisticModelList(@NotNull PostStatisticBottomSheet bottomSheet, @NotNull String activityId, @NotNull List<String> productIds) {
                                presenter.getPostStatistic(activityId, productIds, likeCount, commentCount);
                            }

                            @Override
                            public void onSeeMoreDetailClicked(@NotNull PostStatisticBottomSheet bottomSheet, @NotNull PostStatisticDetailType type) {
                                if (type == PostStatisticDetailType.Comment) {
                                    RouteManager.route(
                                            requireContext(),
                                            ApplinkConst.KOL_COMMENT,
                                            activityId
                                    );
                                    bottomSheet.dismiss();
                                }
                            }
                        }
                );
    }

    @Override
    public void userImagePostImpression(int positionInFeed, int contentPosition) {

    }

    @Override
    public void onTopAdsViewImpression(@NotNull String bannerId, @NotNull String imageUrl) {

    }

    @Override
    public void onImageClicked(@NotNull String activityId, @NotNull String type, boolean isFollowed, @NotNull String shopId) {

    }

    @Override
    public void onPostTagItemBSClick(int positionInFeed, @NotNull String redirectUrl, @NotNull FeedXProduct postTagItem, int itemPosition,@NotNull String mediaType ) {

    }

    @Override
    public void onReadMoreClicked(@NotNull String postId, @NotNull String shopId, @NotNull String postType, boolean isFollowed, @NotNull String mediaType) {

    }

    @Override
    public void onBottomSheetMenuClicked(@NotNull ProductPostTagViewModelNew item, @NotNull Context context, @NotNull String shopId) {

    }


    @Override
    public void userProductImpression(int positionInFeed, @NotNull String activityId, @NotNull String postType, @NotNull String shopId, @NotNull List<FeedXProduct> productList) {

    }

    @Override
    public void userGridPostImpression(int positionInFeed, @NotNull String activityId, @NotNull String postType, @NotNull String shopId) {

    }

    @Override
    public void muteUnmuteVideo(@NotNull String postId, boolean mute, @NotNull String id, boolean isFollowed, boolean isVOD, @NotNull String mediaType) {

    }

    @Override
    public void onTagClicked(int postId, @NotNull List<FeedXProduct> products, @NotNull DynamicPostViewHolder.DynamicPostListener listener, @NotNull String id, @NotNull String type, boolean isFollowed, @NotNull String mediaType, int positionInFeed, @NotNull String playChannelId, @NotNull String shopName) {

    }

    @Override
    public void onPostTagItemBSImpression(@NotNull String activityId, @NotNull List<FeedXProduct> postTagItemList, @NotNull String type, @NotNull String shopId, boolean isFollowed,  @NotNull String mediaType) {

    }

    @Override
    public void onLikeClick(int positionInFeed, int id, boolean isLiked, @NotNull String postType, boolean isFollowed, boolean type, @NotNull String shopId, @NotNull String mediaType, @NotNull String playChannelId) {
        if (isLiked) {
            onUnlikeKolClicked(positionInFeed, id);
        } else {
            onLikeKolClicked(positionInFeed, id);
        }
    }

    @Override
    public void onImpressionTracking(@NotNull FeedXCard feedXCard, int positionInFeed) {

    }

    @Override
    public void onHashtagClickedFeed(@NotNull String hashtagText, @NotNull FeedXCard feedXCard) {

    }

    @Override
    public void onVideoStopTrack(@NotNull FeedXCard feedXCard, long duration) {

    }

    @Override
    public void onPostTagBubbleClick(int positionInFeed, @NotNull String redirectUrl, @NotNull FeedXProduct postTagItem, @NotNull String adClickUrl) {

    }

    @Override
    public void onClickSekSekarang(@NotNull String postId, @NotNull String shopId, @NotNull String type, boolean isFollowed, int positionInFeed, FeedXCard feedXCard) {

    }

    @Override
    public void onFollowClickAds(int positionInFeed, @NonNull String shopId, @NonNull String adId) {

    }

    @Override
    public void userCarouselImpression(@NonNull String activityId, @NonNull FeedXMedia media, int positionInFeed, @NonNull String postType, boolean isFollowed, @NonNull String shopId, int postPosition, @NonNull CpmData cpmData, @NonNull List<? extends Product> products) {

    }

    @Override
    public void onFullScreenCLick(@NotNull FeedXCard feedXCard,int positionInFeed, @NonNull String redirectUrl, long currentTime, boolean shouldTrack, boolean isFullScreen) {

    }
    @Override
    public void addVODView(@NotNull FeedXCard feedXCard,@NonNull String playChannelId, int rowNumber, long time, boolean hitTrackerApi) {

    }


}
