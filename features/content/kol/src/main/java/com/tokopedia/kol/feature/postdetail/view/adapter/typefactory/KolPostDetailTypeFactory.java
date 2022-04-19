package com.tokopedia.kol.feature.postdetail.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel;
import com.tokopedia.kol.feature.comment.view.viewmodel.KolCommentViewModel;
import com.tokopedia.kol.feature.postdetail.view.viewmodel.EmptyDetailViewModel;
import com.tokopedia.kol.feature.postdetail.view.viewmodel.SeeAllCommentsViewModel;

/**
 * @author by milhamj on 27/07/18.
 */

public interface KolPostDetailTypeFactory extends AdapterTypeFactory {

    int type(DynamicPostViewModel dynamicPostViewModel);

    int type(KolCommentViewModel kolCommentViewModel);

    int type(SeeAllCommentsViewModel seeAllCommentsViewModel);

    int type(EmptyDetailViewModel emptyDetailViewModel);
    AbstractViewHolder createViewHolder(View view, int viewType);
}
