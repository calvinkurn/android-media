package com.tokopedia.topads.common.view.listener;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.topads.common.view.fragment.BaseFilterContentFragment;


/**
 * Created by normansyahputa on 5/29/17.
 */

public interface BaseFilterContentViewListener {
    String getTitle(Context context);

    Intent addResult(Intent intent);

    boolean isActive();

    void setActive(boolean active);

    void setCallback(BaseFilterContentFragment.Callback callback);
}
