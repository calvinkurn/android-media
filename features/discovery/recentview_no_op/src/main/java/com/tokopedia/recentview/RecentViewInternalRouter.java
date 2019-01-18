package com.tokopedia.recentview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class RecentViewInternalRouter {
    public static Intent getRecentViewIntent(Context context) {
        Toast.makeText(context, "getRecentViewIntent", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.getRecentViewIntent.com"));
        return intent;
    }
}
