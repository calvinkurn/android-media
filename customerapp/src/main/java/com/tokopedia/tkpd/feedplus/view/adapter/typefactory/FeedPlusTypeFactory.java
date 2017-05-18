package com.tokopedia.tkpd.feedplus.view.adapter.typefactory;

import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.feedplus.view.viewmodel.ProductCardViewModel;
import com.tokopedia.tkpd.feedplus.view.viewmodel.PromoViewModel;
import com.tokopedia.tkpd.feedplus.view.viewmodel.PromotedShopViewModel;
import com.tokopedia.tkpd.feedplus.view.viewmodel.PromotedShopViewModel;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlusTypeFactory  {

    int type(ProductCardViewModel viewModel);

    int type(PromotedShopViewModel viewModel);

    int type(PromoViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}
