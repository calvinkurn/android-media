package com.tokopedia.topads.dashboard.view.model;

import com.tokopedia.base.list.seller.common.util.ItemType;

/**
 * @author normansyahputa on 3/9/17.
 */

public class EmptyTypeBasedModel implements BaseTopAdsProductModel, ItemType {
    public static final int TYPE = 12921381;

    @Override
    public TopAdsProductViewModel getTopAdsProductViewModel() {
        return null;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
