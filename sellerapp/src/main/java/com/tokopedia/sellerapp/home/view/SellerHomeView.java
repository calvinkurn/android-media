package com.tokopedia.sellerapp.home.view;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.sellerapp.home.view.model.ShopScoreViewModel;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public interface SellerHomeView extends CustomerView {
    void renderShopScore(ShopScoreViewModel shopScoreViewModel);

    void onErrorShopScore();
}
