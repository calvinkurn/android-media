package com.tkpd.library.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tokopedia.core2.R;

/**
 * Created by zulfikarrahman on 8/31/16.
 */

public class BadgeUtil {
    public static ImageView createDynamicBadge(Context context){
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                context.getResources().getDimensionPixelOffset(R.dimen.med_dp), context.getResources().getDimensionPixelOffset(R.dimen.med_dp));
        ImageView imageView=new ImageView(context);
        imageView.setLayoutParams(lparams);
        int padding = context.getResources().getDimensionPixelOffset(R.dimen.margin_vvvs);
        imageView.setPadding(padding, padding, padding, padding);
     return imageView;
    }
}
