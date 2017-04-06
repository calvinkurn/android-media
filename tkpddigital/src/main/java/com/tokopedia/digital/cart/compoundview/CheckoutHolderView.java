package com.tokopedia.digital.cart.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 2/22/17.
 */

public class CheckoutHolderView extends RelativeLayout {

    @BindView(R2.id.tv_value_price)
    TextView tvPrice;
    @BindView(R2.id.tv_value_sub_total)
    TextView tvSubTotalPrice;
    @BindView(R2.id.btn_next)
    TextView btnNext;

    public CheckoutHolderView(Context context) {
        super(context);
        initView(context);
    }

    public CheckoutHolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CheckoutHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(
                R.layout.view_holder_checkout_digital_module, this, true
        );
        ButterKnife.bind(this);
    }

    public void renderData(final IAction actionListener, String price, String totalPrice) {
        tvPrice.setText(price);
        tvSubTotalPrice.setText(totalPrice);
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onClickButtonNext();
            }
        });
    }

    public interface IAction {
        void onClickButtonNext();
    }


}
