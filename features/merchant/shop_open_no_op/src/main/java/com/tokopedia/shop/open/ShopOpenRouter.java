package com.tokopedia.shop.open;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by yoshua on 07/06/18.
 */

public class ShopOpenRouter {
    public static Intent getIntentCreateEditShop(Context context){
        Toast.makeText(context, "Create Edit Shop", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.getIntentCreateEditShop.com"));
        return intent;
    }
}
