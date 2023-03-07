package com.tokopedia.kol.feature.comment.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentHeaderNewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentNewModel;

/**
 * @author by milhamj on 18/04/18.
 */

public interface KolCommentTypeFactory {

    int type(KolCommentNewModel viewModel);

    int type(KolCommentHeaderNewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
