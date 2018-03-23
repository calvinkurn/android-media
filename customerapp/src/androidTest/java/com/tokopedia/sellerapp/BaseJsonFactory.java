package com.tokopedia.sellerapp;

import android.content.Context;

/**
 * Created by normansyahputa on 3/21/18.
 */

public class BaseJsonFactory {
    private Context context;

    public BaseJsonFactory(Context context) {
        this.context = context;
    }

    public String convertFromAndroidResource(String filePath) {
        try {
            return RestServiceTestHelper.getStringFromFile(context, filePath);
        } catch (Exception e) {
            return null;
        }
    }
}
