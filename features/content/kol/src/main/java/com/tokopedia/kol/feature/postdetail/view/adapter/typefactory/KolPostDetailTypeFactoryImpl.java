package com.tokopedia.kol.feature.postdetail.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.feature.comment.view.adapter.typefactory.KolCommentTypeFactory;
import com.tokopedia.kol.feature.comment.view.adapter.viewholder.KolCommentViewHolder;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderViewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostDetailViewHolder;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.EmptyKolPostViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.EntryPointViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.ExploreViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostYoutubeViewModel;
import com.tokopedia.kol.feature.postdetail.view.adapter.viewholder.SeeAllCommentsViewHolder;
import com.tokopedia.kol.feature.postdetail.view.viewmodel.SeeAllCommentsViewModel;

/**
 * @author by milhamj on 27/07/18.
 */

public class KolPostDetailTypeFactoryImpl extends BaseAdapterTypeFactory
        implements KolPostDetailTypeFactory, KolPostTypeFactory, KolCommentTypeFactory {

    private final KolPostListener.View.ViewHolder kolPostListener;
    private final KolComment.View.ViewHolder kolCommentListener;
    private final KolComment.View.SeeAll seeAll;

    public KolPostDetailTypeFactoryImpl(KolPostListener.View.ViewHolder kolPostListener,
                                        KolComment.View.ViewHolder kolCommentListener,
                                        KolComment.View.SeeAll seeAll) {
        this.kolPostListener = kolPostListener;
        this.kolCommentListener = kolCommentListener;
        this.seeAll = seeAll;
    }

    @Override
    public int type(KolPostViewModel kolPostViewModel) {
        return KolPostViewHolder.LAYOUT;
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
        if (viewType == KolPostViewHolder.LAYOUT) {
            abstractViewHolder = new KolPostDetailViewHolder(view,
                    kolPostListener,
                    KolPostViewHolder.Type.EXPLORE);
        }
        else if (viewType == KolCommentViewHolder.LAYOUT)
            abstractViewHolder = new KolCommentViewHolder(view, kolCommentListener);
        else if (viewType == SeeAllCommentsViewHolder.LAYOUT)
            abstractViewHolder = new SeeAllCommentsViewHolder(view, seeAll);
        else
            abstractViewHolder = super.createViewHolder(view, viewType);
        return abstractViewHolder;
    }

    @Override
    public void setType(KolPostViewHolder.Type type) {

    }
}
