package com.tokopedia.sellerapp;

import android.app.Activity;
import android.app.Application;
import android.view.MotionEvent;

import com.instabug.library.Instabug;
import com.instabug.library.InstabugColorTheme;
import com.instabug.library.InstabugCustomTextPlaceHolder;
import com.instabug.library.InstabugTrackingDelegate;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.instabug.library.ui.onboarding.WelcomeMessage;

/**
 * Created by meta on 25/09/18.
 */
public class InstabugInitalize {

    private static final String INSTABUG_BETA_KEY = "4684d4ce46e407b41d54108e689f5734";

    public static void init(Application application) {
        new Instabug.Builder(application, INSTABUG_BETA_KEY)
                .setInvocationEvents(InstabugInvocationEvent.SCREENSHOT_GESTURE,
                        InstabugInvocationEvent.FLOATING_BUTTON)
                .build();

        Instabug.setColorTheme(InstabugColorTheme.InstabugColorThemeLight);
        Instabug.setWelcomeMessageState(WelcomeMessage.State.BETA);

        //To show instabug debug logs if necessary
        Instabug.setDebugEnabled(true);

        //Settings custom strings to replace instabug's strings
        InstabugCustomTextPlaceHolder placeHolder = new InstabugCustomTextPlaceHolder();
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.REPORT_FEEDBACK, "Send Feedback");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.REPORT_BUG, "Send Bug Report");

        Instabug.setCustomTextPlaceHolders(placeHolder);

        //setting user attributes
        Instabug.setUserAttribute("USER_TYPE", "instabug user");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Instabug.setAutoScreenRecordingEnabled(true);
        }
    }

    public static void dispatchTouchEvent(Activity context, MotionEvent ev) {
        InstabugTrackingDelegate.notifyActivityGotTouchEvent(ev, context);
    }
}
