package com.tokopedia.sellerapp.gmstat.activities;

import android.util.Log;
import android.view.View;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.gmstat.utils.GMStatNetworkController;
import com.tokopedia.sellerapp.SellerMainApplication;
import com.tokopedia.sellerapp.drawer.DrawerVariableSeller;
import com.tokopedia.sellerapp.home.view.SellerToolbarVariable;

import javax.inject.Inject;

/**
 * Created by normansyahputa on 1/16/17.
 */

public class GMStatActivity extends com.tokopedia.seller.gmstat.views.GMStatActivity {
    SellerToolbarVariable sellerToolbarVariable;
    private DrawerVariableSeller drawer;

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

    protected void initDrawer() {
        drawer = new DrawerVariableSeller(this);
        sellerToolbarVariable = new SellerToolbarVariable(this, toolbar);
        sellerToolbarVariable.createToolbarWithDrawer();
        drawer.setToolbar(sellerToolbarVariable);
        drawer.createDrawer();
        drawer.setEnabled(true);
        drawer.setDrawerPosition(TkpdState.DrawerPosition.SELLER_GM_STAT);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("NIS", "CLICK");
                drawer.openDrawer();
            }
        });
    }
}
