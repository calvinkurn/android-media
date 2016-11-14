package com.tokopedia.core;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.ImageView;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.fragment.FragmentBuyCreditCart;


public class BuyCreditCart extends TActivity {

    private ViewHolder holder;
    private static int STATE_ACTIVE = R.drawable.ic_shopping_cart_done;
    private static int STATE_INACTIVE = R.drawable.ic_shopping_cart_undone;

    private class ViewHolder{
        int container;
        ImageView stateCart;
        ImageView stateConfirm;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_CREDIT_CARD_CART_BUY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_buy_credit_cart);

        initVar();
        initView();

        if (savedInstanceState == null) {
            createCartFragment();
        }

        setCartState(true);
    }

    private void initVar(){
        holder = new ViewHolder();
    }

    private void initView(){
        holder.container = R.id.container;
        holder.stateCart = (ImageView) findViewById(R.id.cart_state);
        holder.stateConfirm = (ImageView) findViewById(R.id.confirm_state);
    }

    private void createCartFragment(){
        doFragmentTransaction(new FragmentBuyCreditCart());
    }

    private void doFragmentTransaction(Fragment fragment){
        getFragmentManager().beginTransaction()
                .add(holder.container, fragment)
                .commit();
    }

    private void setCartState(boolean isConfirm){
        if(isConfirm){
            changeStateLogo(holder.stateCart, STATE_INACTIVE);
            changeStateLogo(holder.stateConfirm, STATE_ACTIVE);
        }else{
            changeStateLogo(holder.stateCart, STATE_ACTIVE);
            changeStateLogo(holder.stateConfirm, STATE_INACTIVE);
        }
    }

    private void changeStateLogo(ImageView view, int STATE){
        view.setImageResource(STATE);
    }

}
