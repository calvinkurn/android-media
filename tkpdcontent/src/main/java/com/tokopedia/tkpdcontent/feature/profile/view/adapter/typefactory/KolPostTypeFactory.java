package com.tokopedia.tkpdcontent.feature.profile.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.EmptyKolPostViewModel;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolPostViewModel;

/**
 * @author by milhamj on 20/02/18.
 */

public interface KolPostTypeFactory {

    int type(KolPostViewModel kolPostViewModel);

    int type(EmptyKolPostViewModel emptyKolPostViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
