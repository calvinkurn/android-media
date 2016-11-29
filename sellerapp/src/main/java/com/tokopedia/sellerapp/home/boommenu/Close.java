package com.tokopedia.sellerapp.home.boommenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.sellerapp.R;

/**
 * Created by sebastianuskh on 10/18/16.
 */

public class Close extends View {

    public Close(Context context) {
        this(context, null);
    }

    public Close(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(R.drawable.ic_close);
    }


}
