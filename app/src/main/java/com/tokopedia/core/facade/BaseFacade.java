package com.tokopedia.core.facade;

import android.content.Context;

/**
 * Created by Tkpd_Eka on 3/16/2015.
 */
public abstract class BaseFacade {

    protected Context context;

    public BaseFacade(Context context){
        this.context = context;
    }

}
