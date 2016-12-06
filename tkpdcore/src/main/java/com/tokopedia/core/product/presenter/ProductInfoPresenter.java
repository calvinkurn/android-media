package com.tokopedia.core.product.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.product.model.share.ShareData;

/**
 * Created by Angga.Prasetiyo on 18/11/2015.
 */
public interface ProductInfoPresenter {

    void initialFragment(@NonNull Context context, Uri uri, Bundle bundle);

    void setLocalyticFlow(@NonNull Context context);

    void processToShareProduct(Context context, @NonNull ShareData data);
}
