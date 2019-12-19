package com.tokopedia.abstraction.base.view.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import androidx.fragment.app.Fragment;

/**
 * Created by Herdi_WORK on 22.11.16.
 */
public abstract class TkpdBaseV4Fragment extends Fragment {

    protected abstract String getScreenName();

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachActivity(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachActivity(activity);
        }
    }

    protected void onAttachActivity(Context context) {
        // to be overriden in child
    }

}
