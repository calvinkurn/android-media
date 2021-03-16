package com.tokopedia.explore.view.adapter.factory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.explore.view.uimodel.ExploreImageViewModel;

/**
 * @author by milhamj on 20/02/18.
 */

public interface ExploreImageTypeFactory {

    int type(ExploreImageViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
