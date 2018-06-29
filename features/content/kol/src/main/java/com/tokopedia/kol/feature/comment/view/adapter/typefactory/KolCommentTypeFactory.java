package com.tokopedia.kol.feature.comment.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderViewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;

/**
 * @author by milhamj on 18/04/18.
 */

public interface KolCommentTypeFactory {
    int type(KolCommentViewModel viewModel);

    int type(KolCommentHeaderViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
