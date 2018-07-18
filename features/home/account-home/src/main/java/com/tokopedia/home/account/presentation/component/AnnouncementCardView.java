package com.tokopedia.home.account.presentation.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.home.account.R;

/**
 * @author okasurya on 7/18/18.
 */
public class AnnouncementCardView extends BaseCustomView {
    public AnnouncementCardView(@NonNull Context context) {
        super(context);
        init();
    }

    public AnnouncementCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnnouncementCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_announcement_card, this);
    }
}
