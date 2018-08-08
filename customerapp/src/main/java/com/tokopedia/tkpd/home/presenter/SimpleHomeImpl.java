package com.tokopedia.tkpd.home.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.tkpd.home.SimpleHomeActivity;


/**
 * Created by m.normansyah on 01/12/2015.
 * this class don't hold context
 */
public class SimpleHomeImpl implements SimpleHome {
    int fragmentType;
    SimpleHomeView simpleHomeView;

    public SimpleHomeImpl(SimpleHomeView simpleHomeView){
        this.simpleHomeView = simpleHomeView;
    }

    @Override
    public void fetchExtras(Intent intent) {
        if(intent!=null){
            int fragmentType = intent.getExtras().getInt(SimpleHomeActivity.FRAGMENT_TYPE,
                    SimpleHomeActivity.INVALID_FRAGMENT);
            if(fragmentType
                != SimpleHomeActivity.INVALID_FRAGMENT){
                simpleHomeView.setTitle(fragmentType);
                simpleHomeView.initFragment(fragmentType);
            }else{
                throw new RuntimeException("please add new type at SimpleHomeView !!!");
            }
        }
    }



    @Override
    public void saveDataBeforeRotate(Bundle outstate) {

    }

    @Override
    public void fetchDataAfterRotate(Bundle instate) {

    }

    @Override
    public void initDataInstance() {

    }
}
