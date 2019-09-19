package com.tokopedia.kol.feature.postdetail.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.Menus;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.feedcomponent.analytics.posttag.PostTagAnalytics;
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker;
import com.tokopedia.feedcomponent.data.pojo.FeedPostRelated;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem;
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateFooter;
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
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightCardViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentOptionViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.relatedpost.RelatedPostViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel;
import com.tokopedia.feedcomponent.view.widget.CardTitleView;
import com.tokopedia.feedcomponent.view.widget.CreatePostFabView;
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.R;
import com.tokopedia.kol.analytics.KolEventTracking;
import com.tokopedia.kol.common.util.PostMenuListener;
import com.tokopedia.kol.feature.comment.view.activity.KolCommentActivity;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.post.di.DaggerKolProfileComponent;
import com.tokopedia.kol.feature.post.di.KolProfileModule;
import com.tokopedia.kol.feature.post.domain.usecase.FollowKolPostGqlUseCase;
import com.tokopedia.kol.feature.post.domain.usecase.LikeKolPostUseCase;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.PostDetailFooterModel;
import com.tokopedia.kol.feature.postdetail.view.activity.KolPostDetailActivity;
import com.tokopedia.kol.feature.postdetail.view.adapter.KolPostDetailAdapter;
import com.tokopedia.kol.feature.postdetail.view.adapter.typefactory.KolPostDetailTypeFactory;
import com.tokopedia.kol.feature.postdetail.view.adapter.typefactory.KolPostDetailTypeFactoryImpl;
import com.tokopedia.kol.feature.postdetail.view.analytics.KolPostDetailAnalytics;
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.postdetail.view.viewmodel.PostDetailViewModel;
import com.tokopedia.kol.feature.report.view.activity.ContentReportActivity;
import com.tokopedia.kol.feature.video.view.activity.MediaPreviewActivity;
import com.tokopedia.kol.feature.video.view.activity.VideoDetailActivity;
import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

import static com.tokopedia.kol.common.util.PostMenuUtilKt.createBottomMenu;

/**
 * @author by yfsx on 23/07/18.
 */

public class KolPostDetailFragment extends BaseDaggerFragment
        implements KolPostDetailContract.View, KolPostListener.View.Like,
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
        HighlightAdapter.HighlightListener {

    private static final String PERFORMANCE_POST_DETAIL = "mp_explore_detail";
    private static final int OPEN_KOL_COMMENT = 101;
    private static final int OPEN_KOL_PROFILE = 13;
    private static final int OPEN_CONTENT_REPORT = 1310;
    private static final int OPEN_VIDEO_DETAIL = 1311;

    private Integer postId;
    private SwipeToRefresh swipeToRefresh;
    private RecyclerView recyclerView;
    private CreatePostFabView createPostFab;
    private ImageView likeButton, commentButton, shareButton;
    private TextView likeCount, commentCount, shareText;
    private View footer;
    private PerformanceMonitoring performanceMonitoring;

    private DynamicPostViewModel dynamicPostViewModel;
    private PostDetailFooterModel postDetailFooterModel;
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

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kol_post_detail, container, false);
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
        createPostFab = view.findViewById(R.id.create_post_fab);
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
            this.dynamicPostViewModel = ((DynamicPostViewModel) postDetailViewModel.getDynamicPostViewModel().getPostList().get(0));
            trackImpression(dynamicPostViewModel);
            presenter.getRelatedPost(String.valueOf(dynamicPostViewModel.getId()));

            if (isOwner()) {
                presenter.getWhitelist();
            }
        }
        setFooter(postDetailViewModel);
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

    private void trackImpression(DynamicPostViewModel dynamicPostViewModel) {
        if (dynamicPostViewModel.getPostTag().getTotalItems() != 0 && !dynamicPostViewModel.getPostTag().getItems().isEmpty()) {
            if (isOwner()) {
                postTagAnalytics.trackViewPostTagProfileDetailSelf(
                        dynamicPostViewModel.getId(),
                        dynamicPostViewModel.getPostTag().getItems(),
                        dynamicPostViewModel.getTrackingPostModel());
            } else {
                postTagAnalytics.trackViewPostTagProfileDetailOther(
                        dynamicPostViewModel.getId(),
                        dynamicPostViewModel.getPostTag().getItems(),
                        dynamicPostViewModel.getHeader().getFollowCta().getAuthorType(),
                        dynamicPostViewModel.getTrackingPostModel());
            }
        }
        onAffiliateTrackClicked(dynamicPostViewModel.getTracking(), false);
    }

    private boolean isOwner() {
        return dynamicPostViewModel != null
                && userSession.getUserId().equals(
                        dynamicPostViewModel.getHeader().getFollowCta().getAuthorID());
    }

    private void setFooter(PostDetailViewModel postDetailViewModel) {
        this.postDetailFooterModel = postDetailViewModel.getFooterModel();
        footer.setVisibility(View.VISIBLE);
        TemplateFooter template = null;
        if (postDetailViewModel.getDynamicPostViewModel().getPostList().size() != 0) {
            template = dynamicPostViewModel.getTemplate().getCardpost().getFooter();
        }
        if (template != null) {
            bindLike(postDetailFooterModel, template);
            bindComment(postDetailFooterModel, template);
            bindShare(postDetailFooterModel, template);

        } else {
            footer.setVisibility(View.GONE);
        }
    }

    private void bindLike(PostDetailFooterModel model, TemplateFooter template) {
        if (template.getLike()) {
            setTotalLike(model.getTotalLike());
            if (model.isLiked()) {
                ImageHandler.loadImageWithId(likeButton, R.drawable.ic_thumb_green);
                likeCount.setTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
            } else {
                ImageHandler.loadImageWithId(likeButton, R.drawable.ic_thumb_gray);
                likeCount.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_54));
            }
            setLikeListener(model.isLiked());
        } else {
            likeButton.setVisibility(View.GONE);
            likeCount.setVisibility(View.GONE);
        }
    }

    private void bindComment(PostDetailFooterModel model, TemplateFooter template) {
        if (template.getComment()) {
            setTotalComment(model.getTotalComment());
            commentCount.setOnClickListener(v -> onGoToKolComment(0, model.getContentId()));
            commentButton.setOnClickListener(v -> onGoToKolComment(0, model.getContentId()));
        } else {
            commentButton.setVisibility(View.GONE);
            commentCount.setVisibility(View.GONE);
        }
    }

    private void bindShare(PostDetailFooterModel model, TemplateFooter template) {
        if (template.getShare()) {
            View.OnClickListener onClickListener = v -> onShareClick(0, model.getContentId(),
                    model.getShareData().getTitle(),
                    model.getShareData().getDescription(),
                    model.getShareData().getUrl(),
                    model.getShareData().getImageUrl());

            shareButton.setOnClickListener(onClickListener);
            shareText.setOnClickListener(onClickListener);
        } else {
            shareButton.setVisibility(View.GONE);
            shareText.setVisibility(View.GONE);
        }
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
        adapter.hideLoading();
    }

    @Override
    public void showLoadingMore() {
        adapter.showLoading();
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
            likeCount.setTextColor(MethodChecker.getColor(getContext(), R.color.tkpd_main_green));
        } else {
            int totalLike = 1;
            if (!likeCount.getText().toString().equals(getString(R.string.kol_action_like))) {
                totalLike = Integer.valueOf(likeCount.getText().toString());
            }
            setTotalLike(totalLike - 1);
            ImageHandler.loadImageWithId(likeButton, R.drawable.ic_thumb_gray);
            likeCount.setTextColor(MethodChecker.getColor(getContext(), R.color.black_54));
        }

        setLikeListener(action == LikeKolPostUseCase.ACTION_LIKE);
    }

    private void setLikeListener(boolean isLiked) {
        if (postDetailFooterModel != null) {
            if (isLiked) {
                likeCount.setOnClickListener(v -> onUnlikeKolClicked(0, postDetailFooterModel.getContentId()));
                likeButton.setOnClickListener(v -> onUnlikeKolClicked(0, postDetailFooterModel.getContentId()));
            } else {
                likeCount.setOnClickListener(v -> onLikeKolClicked(0, postDetailFooterModel.getContentId()));
                likeButton.setOnClickListener(v -> onLikeKolClicked(0, postDetailFooterModel.getContentId()));
            }
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
    public void onGoToProfile(String url) {
        onGoToLink(url);
    }

    @Override
    public void onClickMentionedProfile(String id) {
        onGoToLink(ApplinkConst.PROFILE.replace(ApplinkConst.Profile.PARAM_USER_ID, id));
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
        if (adapter.getList().get(rowNumber) instanceof DynamicPostViewModel) {
            DynamicPostViewModel dynamicPostViewModel = (DynamicPostViewModel) adapter.getList().get(rowNumber);
            dynamicPostViewModel.getHeader().getFollowCta().setFollow(!dynamicPostViewModel.getHeader().getFollowCta().isFollow());
            adapter.notifyItemChanged(rowNumber, KolPostViewHolder.PAYLOAD_FOLLOW);

            if (dynamicPostViewModel.getHeader().getFollowCta().isFollow()) {
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
    public void onErrorToggleFavoriteShop(String errorMessage, String shopId) {
        ToasterError.make(getView(), errorMessage, BaseToaster.LENGTH_LONG)
                .setAction(R.string.title_try_again,
                        v -> presenter.toggleFavoriteShop(shopId)
                )
                .show();
    }

    @Override
    public void onSuccessToggleFavoriteShop() {
        if (adapter.getList().get(0) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model = (DynamicPostViewModel) adapter.getList().get(0);
            model.getHeader().getFollowCta().setFollow(
                    !model.getHeader().getFollowCta().isFollow()
            );
            adapter.notifyItemChanged(0, DynamicPostViewHolder.PAYLOAD_FOLLOW);
        }
    }

    @Override
    public void onErrorSendVote(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessSendVote(int rowNumber, String optionId,
                                  VoteStatisticDomainModel voteStatisticDomainModel) {

        int DEFAULT = 0;
        int UNSELECTED = 1;
        int SELECTED = 2;

        if (adapter.getList().size() > 0
                && adapter.getList().get(0) instanceof DynamicPostViewModel) {
            DynamicPostViewModel model = (DynamicPostViewModel) adapter.getList().get(0);
            for (BasePostViewModel basePostViewModel : model.getContentList()) {
                if (basePostViewModel instanceof PollContentViewModel) {
                    PollContentViewModel pollContentViewModel = (PollContentViewModel) basePostViewModel;
                    pollContentViewModel.setVoted(true);

                    int totalVoter;
                    try {
                        totalVoter = Integer.valueOf(voteStatisticDomainModel.getTotalParticipants());
                    } catch (NumberFormatException ignored) {
                        totalVoter = 0;
                    }
                    pollContentViewModel.setTotalVoterNumber(totalVoter);

                    for (int i = 0; i < pollContentViewModel.getOptionList().size(); i++) {
                        PollContentOptionViewModel optionViewModel
                                = pollContentViewModel.getOptionList().get(i);

                        optionViewModel.setSelected(optionId.equals(optionViewModel.getOptionId()) ?
                                SELECTED : UNSELECTED);

                        int newPercentage = 0;
                        try {
                            newPercentage = Integer.valueOf(
                                    voteStatisticDomainModel.getListOptions().get(i).getPercentage()
                            );
                        } catch (NumberFormatException | IndexOutOfBoundsException ignored) {
                        }
                        optionViewModel.setPercentage(newPercentage);
                    }
                }
            }
        }

        adapter.notifyItemChanged(0);
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
        ToasterNormal
                .make(getView(),
                        getString(R.string.feed_content_reported),
                        BaseToaster.LENGTH_LONG)
                .setAction(R.string.label_close, v -> {
                })
                .show();
    }

    private void onErrorReportContent(String errorMsg) {
        ToasterError
                .make(getView(), errorMsg, BaseToaster.LENGTH_LONG)
                .setAction(R.string.label_close, v -> {
                })
                .show();
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
    public void onAvatarClick(int positionInFeed, @NotNull String redirectUrl) {
        onGoToLink(redirectUrl);
    }

    @Override
    public void onHeaderActionClick(int positionInFeed, @NotNull String id, @NotNull String type, boolean isFollow) {

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
    public void onMenuClick(int positionInFeed, int postId, boolean reportable, boolean deletable, boolean editable) {
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
        dialog.setTitle(getString(R.string.card_dialog_delete_post));
        dialog.setDesc(getString(R.string.card_dialog_after_delete_cant));
        dialog.setBtnOk(getString(R.string.card_dialog_title_delete));
        dialog.setBtnCancel(getString(R.string.card_dialog_title_cancel));
        dialog.setOnOkClickListener(v -> {
            presenter.deletePost(postId, positionInFeed);
            dialog.dismiss();
        });
        dialog.setOnCancelClickListener(v -> dialog.dismiss());
        return dialog;
    }

    private void goToContentReport(int contentId) {
        if (getContext() != null) {
            Intent intent = ContentReportActivity.Companion.createIntent(
                    getContext(),
                    contentId
            );
            startActivityForResult(intent, OPEN_CONTENT_REPORT);
        }
    }

    @Override
    public void onCaptionClick(int positionInFeed, @NotNull String redirectUrl) {
        onGoToLink(redirectUrl);
    }

    @Override
    public void onLikeClick(int positionInFeed, int id, boolean isLiked) {
//NO NEED TO IMPLEMENT THIS IN DETAIL
    }

    @Override
    public void onCommentClick(int positionInFeed, int id) {
//NO NEED TO IMPLEMENT THIS IN DETAIL
    }

    @Override
    public void onLikeClick(int positionInFeed, int columnNumber, int id, boolean isLiked) {

    }

    @Override
    public void onCommentClick(int positionInFeed, int columnNumber, int id) {

    }

    @Override
    public void onShareClick(int positionInFeed, int id, @NotNull String title,
                             @NotNull String description, @NotNull String url,
                             @NotNull String imageUrl) {
        if (getActivity() != null) {
            new ShareBottomSheets().show(getActivity().getSupportFragmentManager(),
                    ShareBottomSheets.Companion.constructShareData("", imageUrl, url, description, title),
                    packageName -> {

                    });
        }
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
                    dynamicPostViewModel.getId(),
                    postTagItem,
                    itemPosition,
                    dynamicPostViewModel.getTrackingPostModel());
        } else {
            postTagAnalytics.trackClickPostTagProfileDetailOther(
                    dynamicPostViewModel.getId(),
                    postTagItem,
                    itemPosition,
                    dynamicPostViewModel.getHeader().getFollowCta().getAuthorType(),
                    dynamicPostViewModel.getTrackingPostModel());
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
            startActivity(MediaPreviewActivity.createIntent(getActivity(), postId.toString(), contentPosition));
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
    public void onPostTagItemBuyClicked(int positionInFeed, @NotNull PostTagItem postTagItem) {
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
                presenter.sendVote(0, pollId, optionId);
            }
        } else {
            RouteManager.route(getActivity(), ApplinkConst.LOGIN);
        }

    }

    @Override
    public void onGridItemClick(int positionInFeed, int contentPosition, int productPosition,
                                @NotNull String redirectLink) {
        onGoToLink(redirectLink);
    }

    @Override
    public void onVideoPlayerClicked(int positionInFeed, int contentPosition, @NotNull String postId) {
        startActivityForResult(VideoDetailActivity.Companion.getInstance(getActivity(), postId), OPEN_VIDEO_DETAIL);
    }

    @Override
    public void onAddToCartSuccess(int positionInFeed, PostTagItem postTagItem) {
        RouteManager.route(getContext(), ApplinkConstInternalMarketplace.CART);
        postTagAnalytics.trackClickPostTagBuyKol(
                postTagItem,
                positionInFeed,
                dynamicPostViewModel.getHeader().getFollowCta().getAuthorType()
        );
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

}
