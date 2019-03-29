package com.tokopedia.kol.feature.post.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.kol.feature.post.view.viewmodel.EmptyKolPostViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.EntryPointViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.ExploreViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostYoutubeViewModel;

/**
 * @author by milhamj on 20/02/18.
 */

public interface KolPostTypeFactory {

    int type(KolPostViewModel kolPostViewModel);

    int type(KolPostYoutubeViewModel kolPostYoutubeViewModel);

    int type(EmptyKolPostViewModel emptyKolPostViewModel);

    int type(ExploreViewModel exploreViewModel);

    int type(EntryPointViewModel entryPointViewModel);

    void setType(KolPostViewHolder.Type type);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
