package com.tokopedia.home.beranda.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.ArrayList;

/**
 * Created by henrypriyono on 1/12/18.
 */

public interface HomeInspirationListener {
    void onGoToProductDetailFromInspiration(String productId,
                                            String imageSource,
                                            String name,
                                            String price);
}
