package com.tokopedia.sellerapp.gmstat.presenters;

import com.tokopedia.sellerapp.gmstat.utils.GMStatNetworkController;
import com.tokopedia.sellerapp.home.utils.ImageHandler;

/**
 * Created by normansyahputa on 11/2/16.
 */

public interface GMStat {
    GMStatNetworkController getGmStatNetworkController();
    ImageHandler getImageHandler();
    boolean isGoldMerchant();
    String getShopId();
}
