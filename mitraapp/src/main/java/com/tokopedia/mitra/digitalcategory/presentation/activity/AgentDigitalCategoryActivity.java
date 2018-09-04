package com.tokopedia.mitra.digitalcategory.presentation.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.mitra.digitalcategory.presentation.fragment.MitraDigitalCategoryFragment;

/**
 * Created by Rizky on 30/08/18.
 */
public class AgentDigitalCategoryActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return new MitraDigitalCategoryFragment();
    }
}
