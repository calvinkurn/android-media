package com.tokopedia.sellerapp;


import com.tokopedia.sellerapp.gmstat.activities.GMStatActivity;
import com.tokopedia.sellerapp.gmstat.activities.GMStatActivity2;
import com.tokopedia.sellerapp.home.view.SellerHomeActivity;

/**
 * Created by normansyahputa on 8/26/16.
 */

public interface BaseComponent {

    void inject(SellerMainApplication application);

    void inject(SellerHomeActivity sellerHomeActivity);

    void inject(GMStatActivity gmStatActivity);

    void inject(GMStatActivity2 gmStatActivity2);
}
