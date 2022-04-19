package com.tokopedia.kol.feature.comment.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.feature.comment.view.adapter.viewholder.KolCommentHeaderNewViewHolder;
import com.tokopedia.kol.feature.comment.view.adapter.viewholder.KolCommentHeaderViewHolder;
import com.tokopedia.kol.feature.comment.view.adapter.viewholder.KolCommentNewViewHolder;
import com.tokopedia.kol.feature.comment.view.adapter.viewholder.KolCommentViewHolder;
import com.tokopedia.kol.feature.comment.view.listener.KolComment;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderNewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderViewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentNewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;

/**
 * @author by milhamj on 18/04/18.
 */

public class KolCommentTypeFactoryImpl extends BaseAdapterTypeFactory
        implements KolCommentTypeFactory {

    private final KolComment.View viewListener;
    private final KolComment.View.ViewHolder viewHolderListener;

    public KolCommentTypeFactoryImpl(KolComment.View viewListener,
                                     KolComment.View.ViewHolder viewHolderListener) {
        this.viewListener = viewListener;
        this.viewHolderListener = viewHolderListener;
    }

    @Override
    public int type(KolCommentViewModel viewModel) {
        return KolCommentViewHolder.LAYOUT;
    }

    @Override
    public int type(KolCommentHeaderViewModel viewModel) {
        return KolCommentHeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(KolCommentNewModel viewModel) {
        return KolCommentNewViewHolder.LAYOUT;
    }

    @Override
    public int type(KolCommentHeaderNewModel viewModel) {
        return KolCommentHeaderNewViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == KolCommentViewHolder.LAYOUT)
            viewHolder = new KolCommentViewHolder(view, viewHolderListener, true);
        else if (type == KolCommentHeaderViewHolder.LAYOUT)
            viewHolder = new KolCommentHeaderViewHolder(view, viewListener);
        else if (type == KolCommentNewViewHolder.LAYOUT)
            viewHolder = new KolCommentNewViewHolder(view, viewHolderListener, true);
        else if (type == KolCommentHeaderNewViewHolder.LAYOUT)
            viewHolder = new KolCommentHeaderNewViewHolder(view, viewListener);
        else viewHolder = super.createViewHolder(view, type);

        return viewHolder;
    }
}
