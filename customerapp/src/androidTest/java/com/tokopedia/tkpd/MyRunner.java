package com.tokopedia.tkpd;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.test.runner.AndroidJUnitRunner;

/**
 * Created by normansyahputa on 3/22/18.
 */

public class MyRunner extends AndroidJUnitRunner {

    @Override
    public void onCreate(Bundle arguments) {
        MultiDex.installInstrumentation(getContext(), getTargetContext());
        super.onCreate(arguments);
    }

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        return super.newApplication(cl, MyCustomEspressoApplication.class.getName(), context);
    }
}
