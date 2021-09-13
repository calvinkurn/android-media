package com.tokopedia.review.common.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;

public class ClipboardHandler {

    public static void CopyToClipboard(Activity context, String Text){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Activity.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Tokopedia", Text);
        clipboard.setPrimaryClip(clip);
    }

}
