package com.tokopedia.core.geolocation.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

/**
 * Created by hangnadi on 1/29/16.
 */
public interface GeolocationPresenter {

    void initFragment(@NonNull Context context, Uri uri, Bundle bundle);

}
