package com.tokopedia.core.router.productdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.Actions.interfaces.ActionCreator;
import com.tokopedia.abstraction.Actions.interfaces.ActionUIDelegate;

import java.util.ArrayList;

/**
 * @author madi on 5/15/17.
 */

public interface PdpRouter {

    void openImagePreview(Context context, ArrayList<String> images, int position);

    Intent getCartIntent(Activity activity);

    Intent getLoginIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    void eventClickFilterReview(Context context,
                                String filterName,
                                String productId);

    void eventImageClickOnReview(Context context,
                                 String productId,
                                 String reviewId);

}