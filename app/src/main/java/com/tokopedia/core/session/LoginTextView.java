package com.tokopedia.core.session;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tokopedia.core.R;

/**
 * Created by stevenfredian on 6/2/16.
 */
public class LoginTextView extends LinearLayout {
    public LoginTextView(Context context) {
        super(context);
        init(context);
    }

    public LoginTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoginTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LoginTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.provider_login_text_view, this);
    }
}
