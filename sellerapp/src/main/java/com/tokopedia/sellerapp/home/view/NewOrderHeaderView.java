package com.tokopedia.sellerapp.home.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.sellerapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by normansyahputa on 9/8/16.
 */

public class NewOrderHeaderView extends FrameLayout {

    @OnClick(R.id.new_order_see_detail)
    public void newOrderSeeDetail(){
        Intent intent = SellerRouter.getActivitySellingTransactionNewOrder(getContext());
        getContext().startActivity(intent);
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
