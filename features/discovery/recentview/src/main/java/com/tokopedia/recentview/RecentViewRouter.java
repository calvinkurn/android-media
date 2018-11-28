package com.tokopedia.recentview;

import android.content.Context;
import android.support.annotation.NonNull;

public interface RecentViewRouter {

    void goToProductDetail(
            @NonNull Context context,
            @NonNull String productId,
            @NonNull String productImage,
            @NonNull String productName,
            @NonNull String productPrice
    );

}
