package com.tokopedia.posapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.session.session.activity.Login;

/**
 * Created by okasurya on 8/1/17.
 */

public class LoginActivity extends Login {
    public static Intent getPosLoginIntent(Context context) {
        Intent callingIntent = new Intent(context, LoginActivity.class);
        callingIntent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
        callingIntent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
        return callingIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }
}
