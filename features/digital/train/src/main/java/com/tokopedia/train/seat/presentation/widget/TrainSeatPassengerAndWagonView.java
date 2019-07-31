package com.tokopedia.train.seat.presentation.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.widget.adapter.TrainSeatPassengerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrainSeatPassengerAndWagonView extends BaseCustomView {
    private AppCompatTextView passengerLabel, wagonLabel;
    private View wagonBottomSeparator;
    private RecyclerView passengerRecyclerView;
    private TrainSeatActionListener actionListener;

    private TrainSeatPassengerAdapter adapter;

    public void setActionListener(TrainSeatActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public interface TrainSeatActionListener {
        void onWagonClicked();
    }

    public TrainSeatPassengerAndWagonView(@NonNull Context context) {
        super(context);
        init();
    }

    public TrainSeatPassengerAndWagonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TrainSeatPassengerAndWagonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_train_seat_passenger_wagon, this);
        passengerLabel = view.findViewById(R.id.tv_passenger_label);
        passengerLabel.setCompoundDrawablesWithIntrinsicBounds(null, null, MethodChecker.getDrawable
                (getContext(), R.drawable.ic_arrow_up_grey), null);
        wagonLabel = view.findViewById(R.id.tv_wagon_label);
        wagonLabel.setCompoundDrawablesWithIntrinsicBounds(null, null, MethodChecker.getDrawable
                (getContext(), R.drawable.ic_arrow_up_grey), null);
        passengerRecyclerView = view.findViewById(R.id.rv_passenger);
        wagonBottomSeparator = view.findViewById(R.id.wagon_bottom_separator);
        adapter = new TrainSeatPassengerAdapter(new ArrayList<>());
        passengerRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        passengerRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        wagonLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionListener != null) {
                    actionListener.onWagonClicked();
                }
            }
        });

        passengerLabel.setOnClickListener(view -> {
            if (passengerRecyclerView.getVisibility() == VISIBLE) {
                Drawable img = ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_down_grey);
                passengerLabel.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                passengerRecyclerView.setVisibility(GONE);
                wagonBottomSeparator.setVisibility(GONE);
            } else {
                Drawable img = ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_up_grey);
                passengerRecyclerView.setVisibility(VISIBLE);
                wagonBottomSeparator.setVisibility(VISIBLE);
                passengerLabel.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
            }
        });
    }

    private void init(AttributeSet attributeSet) {
        init();
    }

    public void renderWagon(String wagon) {
        wagonLabel.setText(wagon);
    }

    public void renderPassenger(List<TrainSeatPassengerViewModel> passengers) {
        adapter.setPassenger(passengers);
        adapter.notifyDataSetChanged();
    }
}
