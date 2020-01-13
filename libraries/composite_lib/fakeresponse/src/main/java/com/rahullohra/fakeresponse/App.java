package com.rahullohra.fakeresponse;

import android.app.Application;

public class App {

    public static Application INSTANCE;

    public static void setContext(Application context) {
        if (App.INSTANCE == null) {
            App.INSTANCE = context;
        }
    }
}
