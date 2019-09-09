package com.tokopedia.train.scheduledetail.presentation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.tokopedia.tkpdtrain.R;

/**
 * Created by Rizky on 06/07/18.
 */
public class ArrowTrainView extends FrameLayout {

    public ArrowTrainView(@NonNull Context context) {
        super(context);
        init();
    }

    public ArrowTrainView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArrowTrainView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ArrowTrainView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.include_arrow_train, this);
    }

}
