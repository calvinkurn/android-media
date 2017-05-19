package com.tokopedia.tkpd.tkpdcustomer.feedplus.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.viewmodel.OfficialStoreViewModel;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.viewmodel.ProductCardViewModel;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.viewmodel.PromoViewModel;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.viewmodel.PromotedShopViewModel;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlusTypeFactory  {

    int type(ProductCardViewModel viewModel);

    int type(PromotedShopViewModel viewModel);

    int type(PromoViewModel viewModel);

    int type(OfficialStoreViewModel officialStoreViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);


}
