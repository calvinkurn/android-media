package com.tokopedia.kol.feature.postdetail.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.feature.comment.view.adapter.viewholder.KolCommentViewHolder;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

/**
 * @author by milhamj on 27/07/18.
 */

public class KolPostDetailTypeFactoryImpl extends BaseAdapterTypeFactory
        implements KolPostDetailTypeFactory {

    private final KolPostListener.View.ViewHolder kolPostListener;
    private final KolComment.View.ViewHolder kolCommentListener;

    public KolPostDetailTypeFactoryImpl(KolPostListener.View.ViewHolder kolPostListener,
                                        KolComment.View.ViewHolder kolCommentListener) {
        this.kolPostListener = kolPostListener;
        this.kolCommentListener = kolCommentListener;
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
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder abstractViewHolder;
        if (viewType == KolPostViewHolder.LAYOUT) {
            abstractViewHolder = new KolPostViewHolder(view,
                    kolPostListener,
                    KolPostViewHolder.Type.EXPLORE);
        }
        else if (viewType == KolCommentViewHolder.LAYOUT)
            abstractViewHolder = new KolCommentViewHolder(view, kolCommentListener);
        else
            abstractViewHolder = super.createViewHolder(view, viewType);
        return abstractViewHolder;
    }
}
