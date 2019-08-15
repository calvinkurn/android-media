package com.tokopedia.events.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.events.R;
import com.tokopedia.events.view.presenter.SeatSelectionPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveengoyal on 1/19/18.
 */

public class CustomSeatLayout extends LinearLayout implements View.OnClickListener {

    TextView individualSeat;
    SeatSelectionPresenter mPresenter;

    public static int numoFSeats;
    int maxCount;
    String rowName;
    String columnName;
    int rowId;
    public static List<String> selectedSeatList = new ArrayList<>();
    public static List<String> rowids = new ArrayList<>();
    public static List<String> actualSeatNos = new ArrayList<>();

    public CustomSeatLayout(Context context) {
        super(context);
        initView();
    }

    public CustomSeatLayout(Context context, SeatSelectionPresenter presenter, int maxTicket, int rowId, String rowName) {
        super(context);
        this.mPresenter = presenter;
        maxCount = maxTicket;
        this.rowId = rowId;
        this.rowName = rowName;
        initView();
    }

    public CustomSeatLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomSeatLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.individual_seat, this);
        individualSeat = view.findViewById(R.id.tv_seat);
        individualSeat.setOnClickListener(this);
        mPresenter.setSelectedSeatText(selectedSeatList, rowids, actualSeatNos);
    }

    public void setText(String text, int status) {
        columnName = text;
        if (numoFSeats >= maxCount) {
            individualSeat.setClickable(false);
        }
        if (text.length() > 0 && status == 1) {
            individualSeat.setText(text);
            individualSeat.setClickable(true);
        } else if (text.length() > 0 && status == 2) {
            individualSeat.setText(text);
            individualSeat.setClickable(false);
            individualSeat.setBackgroundResource(R.drawable.cannot_select_seat_bg);
            individualSeat.setTextColor(ContextCompat.getColor(getContext(), R.color.black_36));
        } else if (text.equalsIgnoreCase(".") || status == 0) {
            individualSeat.setText(text);
            individualSeat.setTextSize(getContext().getResources().getDimension(R.dimen.dp_16));
            individualSeat.setTextColor(ContextCompat.getColor(getContext(), R.color.black_36));
            individualSeat.setBackground(null);
            individualSeat.setClickable(false);
        }
    }

    public static void destroy() {
        numoFSeats = 0;
        CustomSeatLayout.selectedSeatList.clear();
        CustomSeatLayout.rowids.clear();
        CustomSeatLayout.actualSeatNos.clear();
    }

    @Override
    public void onClick(View v) {
        if (!individualSeat.isSelected() && numoFSeats < maxCount) {
            individualSeat.setSelected(true);
            individualSeat.setBackgroundResource(R.drawable.currently_selected_seat_bg);
            individualSeat.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            numoFSeats++;
            if (rowName != null && rowName.length() > 0) {
                selectedSeatList.add("" + rowName + columnName);
            } else {
                selectedSeatList.add(columnName);
            }
            actualSeatNos.add(columnName);
            rowids.add(Integer.toString(rowId));
        } else if (individualSeat.isSelected()) {
            individualSeat.setSelected(false);
            individualSeat.setBackgroundResource(R.drawable.seat_bg);
            individualSeat.setTextColor(ContextCompat.getColor(getContext(), R.color.black_38));
            numoFSeats--;
            if (rowName != null && rowName.length() > 0) {
                selectedSeatList.remove("" + rowName + columnName);
            } else {
                selectedSeatList.remove(columnName);
            }
            actualSeatNos.remove(columnName);
            rowids.remove(Integer.toString(rowId));
        } else {
            Toast.makeText(getContext(),
                    String.format(getContext().getString(R.string.more_seat_than_tiket_warning_toast), maxCount),
                    Toast.LENGTH_SHORT).show();
        }
        mPresenter.setSeatData();
    }
}
