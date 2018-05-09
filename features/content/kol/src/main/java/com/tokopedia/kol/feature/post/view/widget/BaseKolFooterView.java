package com.tokopedia.kol.feature.post.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.kol.R;

/**
 * @author by milhamj on 09/05/18.
 */

public class BaseKolFooterView extends BaseCustomView {
    public BaseKolFooterView(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseKolFooterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseKolFooterView(@NonNull Context context, @Nullable AttributeSet attrs,
                             int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.base_kol_footer, this);
    }
}
