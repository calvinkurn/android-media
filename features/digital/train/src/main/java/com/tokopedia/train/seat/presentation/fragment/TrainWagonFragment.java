package com.tokopedia.train.seat.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.seat.presentation.fragment.adapter.TrainSeatAdapter;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class TrainWagonFragment extends BaseDaggerFragment implements TrainSeatAdapter.ActionListener {
    private static final String EXTRA_WAGON = "EXTRA_WAGON";
    private TrainWagonViewModel trainWagonViewModel;

    private RecyclerView seatsRecyclerView;

    public static TrainWagonFragment newInstance(TrainWagonViewModel trainWagonViewModel) {
        TrainWagonFragment fragment = new TrainWagonFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_WAGON, trainWagonViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trainWagonViewModel = getArguments().getParcelable(EXTRA_WAGON);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_wagon, container, false);
        seatsRecyclerView = view.findViewById(R.id.rv_seats);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int columnCount = calculateColumn(trainWagonViewModel.getSeats());
        TrainSeatAdapter adapter = new TrainSeatAdapter();
        adapter.setSeats(trainWagonViewModel.getSeats());
        adapter.setSelecteds(new ArrayList<>());
        adapter.setListener(this);
        seatsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnCount));
        seatsRecyclerView.setAdapter(adapter);
    }

    private int calculateColumn(List<TrainSeatViewModel> seats) {
        int column = 4;
        if (seats.size() > 5) {
            column = seats.get(4).getColumn().equalsIgnoreCase("E") ? 5 : 4;
        }
        return column;
    }

    @Override
    public void seatClicked(TrainSeatViewModel viewModel, int top, int left, int width, int height) {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.view_train_seat_popup, null);

        // create the popup window
        int widthL = LinearLayout.LayoutParams.MATCH_PARENT;
        int heightL = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, widthL, heightL, focusable);
        RecyclerView passengerRecyclerView = popupView.findViewById(R.id.rv_passenger);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        int x = 0;
        int y = top > height ? top - height : top;
        popupWindow.showAtLocation(getView(), Gravity.CENTER, x, y);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
