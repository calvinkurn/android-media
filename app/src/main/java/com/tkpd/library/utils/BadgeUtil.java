package com.tkpd.library.utils;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.tkpd.R;

/**
 * Created by zulfikarrahman on 8/31/16.
 */

public class BadgeUtil {
    public static ImageView createDynamicBadge(Context context){
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                context.getResources().getDimensionPixelOffset(R.dimen.large_dp), context.getResources().getDimensionPixelOffset(R.dimen.large_dp));
        ImageView imageView=new ImageView(context);
        imageView.setLayoutParams(lparams);
        int padding = context.getResources().getDimensionPixelOffset(R.dimen.margin_vvs);
        imageView.setPadding(padding, padding, padding, padding);
     return imageView;
    }
}
