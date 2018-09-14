package com.tokopedia.mitra.digitalcategory.presentation.compoundview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.mitra.R;

/**
 * Created by Rizky on 14/09/18.
 */
public class MitraDigitalInquiryResultView extends LinearLayout {

    public MitraDigitalInquiryResultView(Context context) {
        super(context);
        init(context);
    }

    public MitraDigitalInquiryResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MitraDigitalInquiryResultView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MitraDigitalInquiryResultView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_mitra_digital_inquiry_result,
                this, true);
    }

}
