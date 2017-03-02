package com.tokopedia.sellerapp.gmstat.activities;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.seller.gmstat.utils.GMStatNetworkController;
import com.tokopedia.seller.gmstat.views.BaseGMStatActivity;
import com.tokopedia.sellerapp.SellerMainApplication;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 1/18/17.
 */

public class GMStatActivity extends BaseGMStatActivity {
    @Override
    public void inject() {
        SellerMainApplication.get(this).getComponent().inject(this);
    }

    @Inject
    public void setupImageHandler(ImageHandler imageHandler){
        super.imageHandler = imageHandler;
    }

    @Inject
    public void setupGMStatNetworkController(GMStatNetworkController gmStatNetworkController){
        super.gmStatNetworkController = gmStatNetworkController;
    }
}
