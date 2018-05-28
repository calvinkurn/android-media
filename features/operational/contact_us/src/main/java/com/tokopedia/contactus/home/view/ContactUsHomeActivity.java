package com.tokopedia.contactus.home.view;


import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.contactus.home.view.fragment.ContactUsHomeFragment;

/**
 * Created by sandeepgoyal on 02/04/18.
 */

public class ContactUsHomeActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return ContactUsHomeFragment.newInstance();
    }
}
