package com.tokopedia.home.beranda.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.ArrayList;

/**
 * Created by henrypriyono on 1/12/18.
 */

/**
 * No further development for this viewholder
 * Backend possibly use this class for version android  >= 2.19
 */

@Deprecated
public interface HomeInspirationListener {
    void onGoToProductDetailFromInspiration(String productId,
                                            String imageSource,
                                            String name,
                                            String price);
}
