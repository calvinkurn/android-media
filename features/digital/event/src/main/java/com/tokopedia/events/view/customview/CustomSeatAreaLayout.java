package com.tokopedia.events.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.view.presenter.SeatSelectionPresenter;

/**
 * Created by naveengoyal on 1/19/18.
 */

public class CustomSeatAreaLayout extends LinearLayout {


    TextView seatAreaText;
    LinearLayout seatTextLayout;

    SeatSelectionPresenter mPresenter;

    public CustomSeatAreaLayout(Context context) {
        super(context);
        initView();
    }

    public CustomSeatAreaLayout(Context context, SeatSelectionPresenter presenter) {
        super(context);
        this.mPresenter = presenter;
        initView();
    }

    public CustomSeatAreaLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomSeatAreaLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.individual_seat_area_text, this);
        seatAreaText = view.findViewById(R.id.seat_areaID);
        seatTextLayout = view.findViewById(R.id.seatTextLayout);
    }

    public void setSeatRow(String text) {
        if (text.length() > 0) {
            seatAreaText.setText(text);
        } else {
            seatAreaText.setBackground(null);
            seatAreaText.setClickable(false);
        }
    }

    public void addColumn(String text, int status, int maxTickets, int rowId, String rowName) {
        final CustomSeatLayout customSeatLayout = new CustomSeatLayout(getContext(), mPresenter, maxTickets, rowId, rowName);
        customSeatLayout.setText(text, status);
        seatTextLayout.addView(customSeatLayout);
    }
}
