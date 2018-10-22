package com.tokopedia.core.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.var.TkpdCache;

/**
 * Created by Tkpd_Eka on 4/16/2015.
 */
public class TokopediaBankAccount {

    public static void createShowAccountDialog(Context context){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_tokopedia_bank);
        TextView bca = (TextView)dialog.findViewById(R.id.bca_number);
        TextView mandiri = (TextView)dialog.findViewById(R.id.mandiri_number_dialog);
        TextView bri = (TextView)dialog.findViewById(R.id.bri_number_dialog);
        bca.setText(getBCANumber(context));
        mandiri.setText(getMandiriNumber(context));
        bri.setText(getBRINumber(context));
        dialog.show();
    }

    private static String getBCANumber(Context context){
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.LAST_BCA);
        return cache.getString(TkpdCache.Key.ACCOUNT_NUMBER, context.getString(R.string.bca_no_alt));
    }

    private static String getMandiriNumber(Context context){
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.LAST_MANDIRI);
        return cache.getString(TkpdCache.Key.ACCOUNT_NUMBER, context.getString(R.string.mandiri_no));
    }

    private static String getBRINumber(Context context){
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.LAST_BRI);
        return cache.getString(TkpdCache.Key.ACCOUNT_NUMBER, context.getString(R.string.bri_no));
    }

    public static void setBCANUmber(Context context, String accNo){
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.LAST_BCA);
        cache.putString(TkpdCache.Key.ACCOUNT_NUMBER, accNo);
        cache.applyEditor();
    }

    public static void setMandiriNumber (Context context, String accNo){
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.LAST_MANDIRI);
        cache.putString(TkpdCache.Key.ACCOUNT_NUMBER, accNo);
        cache.applyEditor();
    }

    public static void setBRINumber (Context context, String accNo){
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.LAST_BRI);
        cache.putString(TkpdCache.Key.ACCOUNT_NUMBER, accNo);
        cache.applyEditor();
    }
}
