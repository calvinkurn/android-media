package com.tokopedia.kol.feature.postdetail.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.Menus;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder;
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentOptionViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel;
import com.tokopedia.feedcomponent.view.widget.CardTitleView;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.KolRouter;
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
import com.tokopedia.kol.feature.postdetail.view.adapter.typefactory.KolPostDetailTypeFactoryImpl;
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.report.view.activity.ContentReportActivity;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.vote.domain.model.VoteStatisticDomainModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.kol.common.util.PostMenuUtilKt.createBottomMenu;

/**
 * @author by yfsx on 23/07/18.
 */

public class KolPostDetailFragment extends BaseDaggerFragment
        implements KolPostDetailContract.View, KolPostListener.View.Like,
        KolComment.View.ViewHolder, KolComment.View.SeeAll,
        SwipeRefreshLayout.OnRefreshListener, DynamicPostViewHolder.DynamicPostListener, CardTitleView.CardTitleListener, ImagePostViewHolder.ImagePostListener, YoutubeViewHolder.YoutubePostListener, PollAdapter.PollOptionListener, GridPostAdapter.GridItemListener {

    private static final String PERFORMANCE_POST_DETAIL = "mp_explore_detail";
    private static final int OPEN_KOL_COMMENT = 101;
    private static final int OPEN_KOL_PROFILE = 13;
    private static final int OPEN_CONTENT_REPORT = 1310;

    private Integer postId;
    private SwipeToRefresh swipeToRefresh;
    private RecyclerView recyclerView;
    private ImageView likeButton, commentButton, shareButton;
    private TextView likeCount, commentCount, shareText;
    private View footer;
    private AbstractionRouter abstractionRouter;
    private KolRouter kolRouter;
    private PerformanceMonitoring performanceMonitoring;
    private AnalyticTracker analyticTracker;

    private PostDetailFooterModel postDetailFooterModel;
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

        adapter.setTypeFactory(new KolPostDetailTypeFactoryImpl(this, this, this, this, this, this, this, this));
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
    public void onSuccessGetKolPostDetail(List<Visitable> list,
                                          PostDetailFooterModel postDetailFooterModel) {
        adapter.setList(list);
        setFooter(postDetailFooterModel);
    }

    private void setFooter(PostDetailFooterModel postDetailFooterModel) {
        this.postDetailFooterModel = postDetailFooterModel;

        footer.setVisibility(View.VISIBLE);

       if (postDetailFooterModel.isLiked()) {
            ImageHandler.loadImageWithId(likeButton, R.drawable.ic_thumb_green);
            likeCount.setTextColor(MethodChecker.getColor(getContext(), R.color.tkpd_main_green));
        } else {
            ImageHandler.loadImageWithId(likeButton, R.drawable.ic_thumb_gray);
            likeCount.setTextColor(MethodChecker.getColor(getContext(), R.color.black_54));
        }

        setLikeListener(postDetailFooterModel.isLiked());
        setTotalLike(postDetailFooterModel.getTotalLike());
        setTotalComment(postDetailFooterModel.getTotalComment());

        commentCount.setOnClickListener(v -> onGoToKolComment(0, postDetailFooterModel.getContentId()));
        commentButton.setOnClickListener(v -> onGoToKolComment(0, postDetailFooterModel.getContentId()));

        shareButton.setOnClickListener(v -> onShareClick(0, postDetailFooterModel.getContentId(),
                postDetailFooterModel.getShareData().getTitle(),
                postDetailFooterModel.getShareData().getDescription(),
                postDetailFooterModel.getShareData().getUrl(),
                postDetailFooterModel.getShareData().getImageUrl()));
        shareText.setOnClickListener(v -> onShareClick(0, postDetailFooterModel.getContentId(),
                postDetailFooterModel.getShareData().getTitle(),
                postDetailFooterModel.getShareData().getDescription(),
                postDetailFooterModel.getShareData().getUrl(),
                postDetailFooterModel.getShareData().getImageUrl()));
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
            startActivity(kolRouter.getLoginIntent(getActivity()));
        }
    }

    public void onUnfollowKolClicked(int rowNumber, int id) {
        if (userSession != null && userSession.isLoggedIn()) {
            presenter.unfollowKol(id, rowNumber);
        } else {
            startActivity(kolRouter.getLoginIntent(getActivity()));
        }
    }

    public void onLikeKolClicked(int rowNumber, int id) {
        if (userSession != null && userSession.isLoggedIn()) {
            presenter.likeKol(id, rowNumber, this);
        } else {
            startActivity(kolRouter.getLoginIntent(getActivity()));
        }
    }

    public void onUnlikeKolClicked(int adapterPosition, int id) {
        if (userSession != null && userSession.isLoggedIn()) {
            presenter.unlikeKol(id, adapterPosition, this);
        } else {
            startActivity(kolRouter.getLoginIntent(getActivity()));
        }
    }

    @Override
    public void onGoToKolComment(int rowNumber, int id) {
        if (userSession != null && userSession.isLoggedIn()) {
            Intent intent = KolCommentActivity.getCallingIntent(getContext(), id, rowNumber);
            startActivityForResult(intent, OPEN_KOL_COMMENT);
        } else {
            startActivity(kolRouter.getLoginIntent(getActivity()));
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case OPEN_KOL_COMMENT:
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

    //NEW
    @Override
    public void onAvatarClick(int positionInFeed, @NotNull String redirectUrl) {
        onGoToLink(redirectUrl);
    }

    public void onGoToLink(String link) {
        if (!TextUtils.isEmpty(link)) {
            kolRouter.openRedirectUrl(getActivity(), link);
        }
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
            startActivity(kolRouter.getLoginIntent(getActivity()));
        }
    }

    @Override
    public void onMenuClick(int positionInFeed, int postId, boolean reportable, boolean deletable, boolean editable) {
        if (getContext() != null) {
            Menus menus = createBottomMenu(getContext(), deletable,
                    reportable, false, new PostMenuListener() {
                        @Override
                        public void onDeleteClicked() {

                        }

                        @Override
                        public void onReportClick() {
                            if (userSession != null && userSession.isLoggedIn()) {
                                goToContentReport(postId);
                            } else {
                                startActivity(kolRouter.getLoginIntent(getActivity()));
                            }
                        }

                        @Override
                        public void onEditClick() {

                        }
                    });
            menus.show();
        }
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
    public void onShareClick(int positionInFeed, int id, @NotNull String title,
                             @NotNull String description, @NotNull String url,
                             @NotNull String imageUrl) {
        if (getActivity() != null) {

            kolRouter.shareFeed(
                    getActivity(),
                    String.valueOf(id),
                    url,
                    title,
                    imageUrl,
                    description
            );
        }
    }

    @Override
    public void onFooterActionClick(int positionInFeed, @NotNull String redirectUrl) {
        onGoToLink(redirectUrl);
    }

    @Override
    public void onPostTagItemClick(int positionInFeed, @NotNull String redirectUrl) {
        onGoToLink(redirectUrl);

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
    public void onTitleCtaClick(@NotNull String redirectUrl) {
        onGoToLink(redirectUrl);

    }

    @Override
    public void onImageClick(int positionInFeed, int contentPosition, @NotNull String redirectLink) {
        onGoToLink(redirectLink);

    }

    @Override
    public void onAffiliateTrackClicked(@NotNull List<TrackingViewModel> trackList) {
        for (TrackingViewModel track : trackList) {
            presenter.trackAffiliate(track.getClickURL());
        }
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
    public void onPollOptionClick(int positionInFeed, int contentPosition, int option, @NotNull String pollId, @NotNull String optionId, boolean isVoted, @NotNull String redirectLink) {
        if (userSession != null && userSession.isLoggedIn()) {
            if (isVoted) {
                onGoToLink(redirectLink);
            } else {
                presenter.sendVote(0, pollId, optionId);
            }
        } else {
            startActivity(kolRouter.getLoginIntent(getActivity()));
        }

    }

    @Override
    public void onGridItemClick(int positionInFeed, int contentPosition, @NotNull String redirectLink) {
        onGoToLink(redirectLink);
    }
}
