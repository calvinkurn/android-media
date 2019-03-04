package com.tokopedia.ovo;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

public class QrOvoPayTxDetailActivity  extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return QrOvoPayTxDetailFragment.createInstance();
    }
}
