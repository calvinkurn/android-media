package com.tokopedia.kol.feature.post.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.feature.post.view.viewmodel.EmptyKolPostViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.ExploreViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

/**
 * @author by milhamj on 20/02/18.
 */

public interface KolPostTypeFactory {

    int type(KolPostViewModel kolPostViewModel);

    int type(EmptyKolPostViewModel emptyKolPostViewModel);

    int type(ExploreViewModel exploreViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
