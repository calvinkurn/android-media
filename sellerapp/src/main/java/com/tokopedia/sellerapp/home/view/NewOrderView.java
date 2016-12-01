package com.tokopedia.sellerapp.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.home.model.notification.Notification;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by normansyahputa on 9/8/16.
 */

public class NewOrderView extends FrameLayout implements BaseView<Notification.Sales>{

    @BindView(R.id.seller_home_new_order_number)
    TextView sellerHomeNewOrderNumber;

    @BindView(R.id.line_break)
    View lineBreak;

    public NewOrderView(Context context){
        this(context, null);
    }

    public NewOrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.seller_home_new_order_layout, this, true);
        ButterKnife.bind(this);
    }

    @Override
    public void init(Notification.Sales data) {
        sellerHomeNewOrderNumber.setText(data.getSales_new_order());
    }

    public void setLineBreak(boolean shown){
        if(shown){
            lineBreak.setVisibility(View.VISIBLE);
        }else{
            lineBreak.setVisibility(View.INVISIBLE);
        }
    }
}
