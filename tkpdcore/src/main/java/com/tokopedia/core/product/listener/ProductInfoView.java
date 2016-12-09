package com.tokopedia.core.product.listener;

import android.app.Fragment;

/**
 * Created by Angga.Prasetiyo on 09/11/2015.
 */
public interface ProductInfoView extends ViewListener {

    void inflateFragment(Fragment fragment, String tag);

}
