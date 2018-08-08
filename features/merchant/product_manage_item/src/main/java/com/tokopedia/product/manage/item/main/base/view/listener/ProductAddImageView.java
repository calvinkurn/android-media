package com.tokopedia.product.manage.item.main.base.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

import java.util.ArrayList;

/**
 * Created by hendry on 28/03/18.
 */

public interface ProductAddImageView extends CustomerView {
    void onSuccessStoreImageToLocal(ArrayList<String> imageUrls);
    void onError (Throwable e);

    Context getContext();
}
