package com.tokopedia.shop.settings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by hendry on 03/09/18.
 */
public class ShopSettingsInternalRouter {
    public static Intent getShopSettingsLocationActivity(Context context) {
        Toast.makeText(context, "Shop Settings Location", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.getShopSettingsLocationActivity.com"));
        return intent;
    }

    public static Intent getShopSettingsBasicInfoActivity(Context context) {
        Toast.makeText(context, "Shop Settings Basic Info", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.getShopSettingsBasicInfoActivity.com"));
        return intent;
    }

    public static Intent getShopSettingsEtalaseActivity(Context context) {
        Toast.makeText(context, "Shop Settings Etalase", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.getShopSettingsEtalaseActivity.com"));
        return intent;
    }

    public static Intent getShopSettingsNotesActivity(Context context) {
        Toast.makeText(context, "Shop Settings Notes", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.getShopSettingsNotesActivity.com"));
        return intent;
    }
}
