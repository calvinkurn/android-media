package com.tokopedia.ovo.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

public class QrOvoPayTxDetailActivity  extends BaseSimpleActivity {

    public static Intent createInstance(Context context){
        return new Intent(context, QrOvoPayTxDetailActivity.class);
    }
    @Override
    protected Fragment getNewFragment() {
        return QrOvoPayTxDetailFragment.createInstance();
    }
}
