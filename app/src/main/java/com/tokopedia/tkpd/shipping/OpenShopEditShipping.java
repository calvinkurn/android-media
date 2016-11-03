package com.tokopedia.tkpd.shipping;

import android.os.Bundle;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.shipping.fragment.FragmentEditShipping;

/**
 * Created by Kris on 6/13/2016.
 * TOKOPEDIA
 */
public class OpenShopEditShipping extends TActivity {

    public static String RESUME_OPEN_SHOP_KEY = "resume_open_shop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_shipping_shop_editor);

        if(getIntent().getExtras()!=null && getIntent().getExtras().containsKey(RESUME_OPEN_SHOP_KEY)){
            getFragmentManager().beginTransaction().add(R.id.main_view, FragmentEditShipping
                    .resumeShopInstance(getIntent().getExtras().getParcelable(RESUME_OPEN_SHOP_KEY))).commit();
        } else if(savedInstanceState == null){
            getFragmentManager().beginTransaction().add(R.id.main_view, FragmentEditShipping.createShopInstance()).commit();
        }
    }
}
