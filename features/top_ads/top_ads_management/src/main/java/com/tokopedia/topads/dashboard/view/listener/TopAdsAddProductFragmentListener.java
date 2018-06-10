package com.tokopedia.topads.dashboard.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.topads.dashboard.data.model.data.Product;

import java.util.List;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsAddProductFragmentListener {

    void onProductListLoaded(@NonNull List<Product> creditList);

    void onLoadProductListError();
}
