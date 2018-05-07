package com.tokopedia.shop.product.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.tokopedia.abstraction.common.utils.view.CommonUtils;

/**
 * Created by nathan on 3/10/18.
 */

public class ShopPagePromoWebView extends NestedWebView {

    public interface Listener {
        void webViewTouched(boolean touched);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public ShopPagePromoWebView(Context context) {
        super(context);
    }

    public ShopPagePromoWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShopPagePromoWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (listener == null) {
            return true;
        }
        CommonUtils.dumper("Action: " + event.getAction());
        //disables ViewPager when user presses down
        if (event.getAction() == MotionEvent.ACTION_DOWN ||
                event.getAction() == MotionEvent.ACTION_MOVE) {
            listener.webViewTouched(true);
        } else if (event.getAction() == MotionEvent.ACTION_UP ||
                event.getAction() == MotionEvent.ACTION_CANCEL) {
            listener.webViewTouched(false);
        }
        return true;
    }
}