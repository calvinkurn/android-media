package com.tokopedia.shop;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by hendry on 03/09/18.
 */
public class ShopPageInternalRouter {

    public static Intent getShopPageIntent(Context context, String shopId) {
        Toast.makeText(context, "Shop Page", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.getShopPageIntent.com"));
        return intent;
    }

    public static Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId) {
        Toast.makeText(context, "Shop Product List", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.getShoProductListIntent.com"));
        return intent;
    }
}
