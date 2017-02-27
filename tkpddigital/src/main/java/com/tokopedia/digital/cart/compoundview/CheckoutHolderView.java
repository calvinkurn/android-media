package com.tokopedia.digital.cart.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.tokopedia.digital.R;

import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 2/22/17.
 */

public class CheckoutHolderView extends RelativeLayout {


    public CheckoutHolderView(Context context) {
        super(context);
//        if (isInEditMode()) return;
        initView(context);
    }

    public CheckoutHolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        if (isInEditMode()) return;
        initView(context);
    }

    public CheckoutHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        if (isInEditMode()) return;
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(
                R.layout.view_holder_checkout_digital_module, this, true
        );
        ButterKnife.bind(this);
    }

    public interface IAction {
        void onClickButtonNext();
    }



}
