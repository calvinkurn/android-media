package com.tokopedia.core.base.presentation;

import android.app.Fragment;
import android.app.FragmentTransaction;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.module.ActivityModule;

/**
 * @author kulomady on 1/9/17.
 */

public abstract class BaseActivity extends TkpdActivity {

    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }


    protected AppComponent getApplicationComponent() {
        return ((MainApplication) getApplication())
                .getApplicationComponent(getActivityModule());
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }


}
