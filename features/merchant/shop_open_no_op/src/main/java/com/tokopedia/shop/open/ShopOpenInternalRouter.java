package com.tokopedia.shop.open;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by hendry on 03/09/18.
 */
public class ShopOpenInternalRouter {

    public static Intent getOpenShopIntent(Context context) {
        Toast.makeText(context, "Shop Open", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.getOpenShopIntent.com"));
        return intent;
    }
}
