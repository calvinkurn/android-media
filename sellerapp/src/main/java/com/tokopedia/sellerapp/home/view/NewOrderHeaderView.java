package com.tokopedia.sellerapp.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.tokopedia.sellerapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tokopedia.sellerapp.drawer.DrawerVariableSeller.goToShopNewOrder;

/**
 * Created by normansyahputa on 9/8/16.
 */

public class NewOrderHeaderView extends FrameLayout {

    @OnClick(R.id.new_order_see_detail)
    public void newOrderSeeDetail(){
        goToShopNewOrder(getContext());
    }


    public NewOrderHeaderView(Context context) {
        this(context, null);
    }

    public NewOrderHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.new_order_header_layout, this, true);
        ButterKnife.bind(this);
    }
}
