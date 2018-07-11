package com.tokopedia.topads.sdk.listener;

import com.tokopedia.topads.sdk.domain.model.Data;

/**
 * @author by errysuprayogi on 3/31/17.
 */

public interface LocalAdsClickListener {

    void onShopItemClicked(int position, Data data);

    void onProductItemClicked(int position, Data data);

    void onAddFavorite(int position, Data dataShop);

}
