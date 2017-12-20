package com.tokopedia.digital.tokocash.compoundview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.digital.R;

/**
 * Created by nabillasabbaha on 12/20/17.
 */

public class ScannerLaserView extends LinearLayout {

    private View scannerLasser;

    public ScannerLaserView(Context context) {
        super(context);
        init();
    }

    public ScannerLaserView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScannerLaserView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.digital_scanner_laser_view, this, true);
        scannerLasser = (View) view.findViewById(R.id.scanner_laser);
    }
}
