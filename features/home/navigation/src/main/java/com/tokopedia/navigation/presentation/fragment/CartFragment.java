package com.tokopedia.navigation.presentation.fragment;

import android.view.View;

import com.tokopedia.navigation.R;
import com.tokopedia.navigation.presentation.base.ParentFragment;

/**
 * Created by meta on 19/06/18.
 */
public class CartFragment extends ParentFragment {

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public int resLayout() {
        return R.layout.fragment_cart;
    }

    @Override
    public void initView(View view) {
        setTitle("Keranjang");
    }

    @Override
    public void loadData() {

    }

    @Override
    protected String getScreenName() {
        return "";
    }
}
