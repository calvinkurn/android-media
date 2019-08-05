package com.tokopedia.kol.feature.postdetail.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder;
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopViewModel;
import com.tokopedia.feedcomponent.view.widget.CardTitleView;
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView;
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactory;
import com.tokopedia.kol.feature.comment.view.adapter.viewholder.KolCommentViewHolder;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderViewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostDetailViewHolder;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.kol.feature.post.view.viewmodel.EmptyKolPostViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.EntryPointViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.ExploreViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostYoutubeViewModel;
import com.tokopedia.kol.feature.postdetail.view.adapter.viewholder.EmptyDetailViewHolder;
import com.tokopedia.kol.feature.postdetail.view.adapter.viewholder.EmptyPostDetailViewHolder;
import com.tokopedia.kol.feature.postdetail.view.adapter.viewholder.SeeAllCommentsViewHolder;
import com.tokopedia.kol.feature.postdetail.view.listener.KolPostDetailContract;
import com.tokopedia.kol.feature.postdetail.view.viewmodel.EmptyDetailViewModel;
import com.tokopedia.kol.feature.postdetail.view.viewmodel.SeeAllCommentsViewModel;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

/**
 * @author by milhamj on 27/07/18.
 */

public class KolPostDetailTypeFactoryImpl extends BaseAdapterTypeFactory
        implements KolPostDetailTypeFactory, KolPostTypeFactory, KolCommentTypeFactory, DynamicFeedTypeFactory {

    private final KolComment.View.ViewHolder kolCommentListener;
    private final DynamicPostViewHolder.DynamicPostListener listener;
    private final CardTitleView.CardTitleListener cardTitleListener;
    private final ImagePostViewHolder.ImagePostListener imagePostListener;
    private final YoutubeViewHolder.YoutubePostListener youtubePostListener;
    private final PollAdapter.PollOptionListener pollOptionListener;
    private final GridPostAdapter.GridItemListener gridItemListener;
    private final VideoViewHolder.VideoViewListener videoViewListener;
    private final FeedMultipleImageView.FeedMultipleImageViewListener feedMultipleImageViewListener;
    private final KolComment.View.SeeAll seeAll;
    private final UserSessionInterface userSession;
    private final KolPostDetailContract.View mainView;

    public KolPostDetailTypeFactoryImpl(KolPostDetailContract.View mainView,
                                        KolComment.View.ViewHolder kolCommentListener,
                                        KolComment.View.SeeAll seeAll,
                                        DynamicPostViewHolder.DynamicPostListener listener,
                                        CardTitleView.CardTitleListener cardTitleListener,
                                        ImagePostViewHolder.ImagePostListener imagePostListener,
                                        YoutubeViewHolder.YoutubePostListener youtubePostListener,
                                        PollAdapter.PollOptionListener pollOptionListener,
                                        GridPostAdapter.GridItemListener gridItemListener,
                                        VideoViewHolder.VideoViewListener videoViewListener,
                                        FeedMultipleImageView.FeedMultipleImageViewListener feedMultipleImageViewListener,
                                        UserSessionInterface userSession) {
        this.mainView = mainView;
        this.kolCommentListener = kolCommentListener;
        this.seeAll = seeAll;
        this.listener = listener;
        this.cardTitleListener = cardTitleListener;
        this.imagePostListener = imagePostListener;
        this.youtubePostListener = youtubePostListener;
        this.pollOptionListener = pollOptionListener;
        this.gridItemListener = gridItemListener;
        this.videoViewListener = videoViewListener;
        this.feedMultipleImageViewListener = feedMultipleImageViewListener;
        this.userSession = userSession;
    }

    @Override
    public int type(EmptyModel viewModel) {
        return EmptyPostDetailViewHolder.LAYOUT;
    }

    @Override
    public int type(DynamicPostViewModel dynamicPostViewModel) {
        return DynamicPostViewHolder.Companion.getLAYOUT();
    }

    @Override
    public int type(KolCommentViewModel kolCommentViewModel) {
        return KolCommentViewHolder.LAYOUT;
    }

    @Override
    public int type(SeeAllCommentsViewModel seeAllCommentsViewModel) {
        return SeeAllCommentsViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyDetailViewModel emptyDetailViewModel) {
        return EmptyDetailViewHolder.Companion.getLAYOUT();
    }

    @Override
    public int type(KolCommentHeaderViewModel viewModel) {
        throw new IllegalStateException(this.getClass().getSimpleName() + " doesn't support "
                + KolCommentHeaderViewModel.class.getSimpleName());
    }

    @Override
    public int type(EmptyKolPostViewModel emptyKolPostViewModel) {
        throw new IllegalStateException(this.getClass().getSimpleName() + " doesn't support "
                + EmptyKolPostViewModel.class.getSimpleName());
    }

    @Override
    public int type(ExploreViewModel exploreViewModel) {
        throw new IllegalStateException(this.getClass().getSimpleName() + " doesn't support "
                + ExploreViewModel.class.getSimpleName());
    }

    @Override
    public int type(KolPostViewModel kolPostViewModel) {
        return KolPostViewHolder.LAYOUT;
    }

    @Override
    public int type(KolPostYoutubeViewModel kolPostYoutubeViewModel) {
        throw new IllegalStateException(this.getClass().getSimpleName() + " doesn't support "
                + KolPostYoutubeViewModel.class.getSimpleName());
    }

    @Override
    public int type(EntryPointViewModel entryPointViewModel) {
        throw new IllegalStateException(this.getClass().getSimpleName() + " doesn't support "
                + EntryPointViewModel.class.getSimpleName());
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder abstractViewHolder;
        if (viewType == DynamicPostViewHolder.Companion.getLAYOUT()) {
            abstractViewHolder = new KolPostDetailViewHolder(view,
                    listener, cardTitleListener, imagePostListener, youtubePostListener,
                    pollOptionListener, gridItemListener, videoViewListener,
                    feedMultipleImageViewListener, userSession
            );
        } else if (viewType == KolCommentViewHolder.LAYOUT)
            abstractViewHolder = new KolCommentViewHolder(view, kolCommentListener);
        else if (viewType == SeeAllCommentsViewHolder.LAYOUT)
            abstractViewHolder = new SeeAllCommentsViewHolder(view, seeAll);
        else if (viewType == EmptyDetailViewHolder.Companion.getLAYOUT()) {
            abstractViewHolder = new EmptyDetailViewHolder(view, mainView);
        } else
            abstractViewHolder = super.createViewHolder(view, viewType);
        return abstractViewHolder;
    }

    @Override
    public void setType(KolPostViewHolder.Type type) {

    }

    @Override
    public int type(@NotNull FeedRecommendationViewModel feedRecommendationViewModel) {
        return 0;
    }

    @Override
    public int type(@NotNull BannerViewModel bannerViewModel) {
        return 0;
    }

    @Override
    public int type(@NotNull TopadsShopViewModel topadsShopViewModel) {
        return 0;
    }
}
