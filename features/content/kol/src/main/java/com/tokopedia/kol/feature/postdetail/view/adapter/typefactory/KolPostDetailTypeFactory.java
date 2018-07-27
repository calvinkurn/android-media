package com.tokopedia.kol.feature.postdetail.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

/**
 * @author by milhamj on 27/07/18.
 */

public interface KolPostDetailTypeFactory {

    int type(KolPostViewModel kolPostViewModel);

    int type(KolCommentViewModel kolCommentViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
