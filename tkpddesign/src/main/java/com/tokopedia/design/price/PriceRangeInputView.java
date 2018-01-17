package com.tokopedia.design.price;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.tokopedia.design.text.DecimalRangeInputView;

/**
 * Created by henrypriyono on 8/14/17.
 */

public class PriceRangeInputView extends DecimalRangeInputView {

    public PriceRangeInputView(@NonNull Context context) {
        super(context);
    }

    public PriceRangeInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PriceRangeInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
