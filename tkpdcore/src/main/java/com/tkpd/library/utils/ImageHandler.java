package com.tkpd.library.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.tokopedia.core.gcm.BuildAndShowNotification;
import com.tokopedia.core.R;

import java.io.File;

@Deprecated
public class ImageHandler extends com.tokopedia.abstraction.utils.image.ImageHandler {

    public static void loadImageBitmapNotification(Context context, String url, BuildAndShowNotification.
            OnGetFileListener listener) {
        FutureTarget<File> futureTarget = Glide.with(context)
                .load(url)
                .downloadOnly(210, 100);

        try {
            File file = futureTarget.get();
            if (file.exists()) {
                listener.onFileReady(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}