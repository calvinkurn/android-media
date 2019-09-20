package com.tokopedia.promocheckout.detail.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.promocheckout.R;

public class BottomSheetFragmentCustom  extends BottomSheetDialog {

    private Activity mContext;
    private FragmentManager fragmentManager;
    private Bundle mBundle;

    public BottomSheetFragmentCustom(@NonNull Context context, FragmentManager manager, Bundle bundle) {
        super(context);
        this.mBundle = bundle;
        this.fragmentManager = manager;
        this.mContext = (Activity)context;
    }

}
