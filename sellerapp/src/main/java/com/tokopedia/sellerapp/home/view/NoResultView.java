package com.tokopedia.sellerapp.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.tokopedia.tkpd.R;

/**
 * Created by normansyahputa on 10/21/16.
 */
public class NoResultView extends RelativeLayout {
    public NoResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_no_result_2, this);
    }
}
