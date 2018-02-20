package com.tokopedia.tkpdcontent.feature.profile.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdcontent.feature.profile.view.viewmodel.KolViewModel;

/**
 * @author by milhamj on 20/02/18.
 */

public interface KolTypeFactory {

    int type(KolViewModel emptyFeedBeforeLoginModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
