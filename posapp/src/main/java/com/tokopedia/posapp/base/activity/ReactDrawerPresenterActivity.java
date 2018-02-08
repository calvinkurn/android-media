package com.tokopedia.posapp.base.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.data.factory.CartFactory;
import com.tokopedia.posapp.di.component.CartComponent;
import com.tokopedia.posapp.di.component.DaggerCartComponent;
import com.tokopedia.posapp.domain.model.cart.CartDomain;

import java.util.List;

import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/26/17.
 */

public abstract class ReactDrawerPresenterActivity<T> extends DrawerPresenterActivity<T> {
    private ReactInstanceManager reactInstanceManager;
    protected View vCart;
    private TextView tvNotif;
    private CartFactory cartFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reactInstanceManager = ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager();
    }

    protected ReactInstanceManager getReactInstanceManager() {
        return reactInstanceManager;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && reactInstanceManager != null) {
            reactInstanceManager.showDevOptionsDialog();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onGetNotificationDrawer(DrawerNotification notification) {

    }

    @Override
    public void onGetNotif() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getCartCount();
    }

    protected void initCartInjector() {
        AppComponent appComponent = ((MainApplication) this.getApplicationContext()).getAppComponent();
        CartComponent cartComponent = DaggerCartComponent.builder().appComponent(appComponent).build();
        cartFactory = cartComponent.provideCartFactory();
        getCartCount();
    }

    protected void getCartCount() {
        if(cartFactory != null) {
            cartFactory.local().getAllCartProducts().map(new Func1<List<CartDomain>, String>() {
                @Override
                public String call(List<CartDomain> cartDomains) {
                    if (cartDomains.size() > 0)
                        return cartDomains.size() + "";
                    else
                        return "null";
                }
            }).subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(String o) {
                    updateNotification(o);
                }
            });
        }
    }

    private void updateNotification(String s) {
        if (vCart != null) {
            tvNotif = vCart.findViewById(R.id.toggle_notif);
            if (!s.equals("null")) {
                tvNotif.setVisibility(View.VISIBLE);
                tvNotif.setText(s);
            } else {
                tvNotif.setVisibility(View.GONE);
            }
        }
    }
}
