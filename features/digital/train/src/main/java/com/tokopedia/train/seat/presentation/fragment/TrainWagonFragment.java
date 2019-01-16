package com.tokopedia.train.seat.presentation.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.presentation.TrainFullDividerItemDecoration;
import com.tokopedia.train.seat.di.TrainSeatComponent;
import com.tokopedia.train.seat.presentation.contract.TrainWagonContract;
import com.tokopedia.train.seat.presentation.fragment.adapter.TrainSeatAdapter;
import com.tokopedia.train.seat.presentation.fragment.adapter.TrainSeatAdapterTypeFactory;
import com.tokopedia.train.seat.presentation.fragment.adapter.TrainSeatPopupAdapter;
import com.tokopedia.train.seat.presentation.fragment.listener.TrainSeatListener;
import com.tokopedia.train.seat.presentation.presenter.TrainWagonPresenter;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class TrainWagonFragment extends BaseDaggerFragment implements TrainWagonContract.View, TrainSeatAdapterTypeFactory.ActionListener, TrainSeatPopupAdapter.ActionListener, TrainSeatListener {
    private static final String EXTRA_WAGON = "EXTRA_WAGON";
    private static final Float DEFAULT_DIM_OPACITY = 0.7f;
    private TrainWagonViewModel trainWagonViewModel;
    private List<TrainSeatViewModel> selectedSeatsInWagon;
    private RecyclerView seatsRecyclerView;
    private PopupWindow popupWindow;
    private TrainSeatAdapter adapter;
    private GridLayoutManager gridLayoutManager;

    @Inject
    TrainWagonPresenter presenter;

    public static TrainWagonFragment newInstance(TrainWagonViewModel trainWagonViewModel, OnFragmentInteraction interactionListener) {
        TrainWagonFragment fragment = new TrainWagonFragment();
        fragment.setInteraction(interactionListener);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_WAGON, trainWagonViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    private OnFragmentInteraction interaction;

    public void setInteraction(OnFragmentInteraction interaction) {
        this.interaction = interaction;
    }

    @Override
    public void onPassengerClicked(TrainSeatPassengerViewModel selectedPassenger, TrainSeatViewModel seat) {
        presenter.onSeatClicked(selectedPassenger, seat);
    }

    @Override
    public void notifyPassengerUpdate() {
        presenter.onPassengerSeatUpdate();
    }

    public interface OnFragmentInteraction {
        List<TrainSeatPassengerViewModel> getPassengers();

        List<TrainSeatPassengerViewModel> getOriginPassengers();

        void onPassengerSeatChange(TrainSeatPassengerViewModel passenger, TrainSeatViewModel seat, String wagonCode);

        int getToolbarHeight();
    }

    @Override
    protected void initInjector() {
        getComponent(TrainSeatComponent.class).inject(this);
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
        if (getParentFragment() instanceof OnFragmentInteraction) {
            if (interaction == null) {
                interaction = (OnFragmentInteraction) getParentFragment();
            }
        }
        
        TrainSeatAdapterTypeFactory adapterTypeFactory = new TrainSeatAdapterTypeFactory(this);
        initView(adapterTypeFactory);
        presenter.attachView(this);
        presenter.onViewCreated();
    }

    private void initView(TrainSeatAdapterTypeFactory adapterTypeFactory) {
        adapter = new TrainSeatAdapter(adapterTypeFactory);
        seatsRecyclerView.setNestedScrollingEnabled(false);
        gridLayoutManager = new GridLayoutManager(getContext(), trainWagonViewModel.getMaxColumn() + 1);
        gridLayoutManager.setSpanSizeLookup(getSpanSeatRule());
        seatsRecyclerView.setLayoutManager(gridLayoutManager);
        seatsRecyclerView.setAdapter(adapter);
    }

    @NonNull
    private GridLayoutManager.SpanSizeLookup getSpanSeatRule() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (trainWagonViewModel.getMaxColumn() == 4 && (position + 1) % trainWagonViewModel.getMaxColumn() == 2)
                    return 2;
                else if (trainWagonViewModel.getMaxColumn() == 5 && (position + 1) % trainWagonViewModel.getMaxColumn() == 3)
                    return 2;
                else
                    return 1;
            }
        };
    }


    @Override
    public TrainWagonViewModel getWagon() {
        return trainWagonViewModel;
    }

    @Override
    public List<TrainSeatPassengerViewModel> getPassengers() {
        return interaction != null ? interaction.getPassengers() : new ArrayList<>();
    }

    @Override
    public List<TrainSeatPassengerViewModel> getOriginPassengers() {
        return  interaction != null ? interaction.getOriginPassengers() : new ArrayList<>();
    }

    @Override
    public void setSelectedSeat(List<TrainSeatViewModel> trainSeatViewModels) {
        this.selectedSeatsInWagon = trainSeatViewModels;
    }

    @Override
    public List<TrainSeatViewModel> getSelectedSeat() {
        return this.selectedSeatsInWagon;
    }

    @Override
    public void renderSeats(List<Visitable> seats) {
        adapter.clearAllElements();
        adapter.addElement(seats);
    }

    @Override
    public void refreshSeats() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updatePassengersSeat(TrainSeatPassengerViewModel selectedPassenger, TrainSeatViewModel seat, String wagonCode) {
        interaction.onPassengerSeatChange(selectedPassenger, seat, wagonCode);
        popupWindow.dismiss();
    }

    @Override
    public void updatePreviousSelectionToAvailable(TrainSeatViewModel trainSeatViewModel) {

    }

    @Override
    public List<TrainSeatViewModel> getSelectedSeatInCurrentWagon() {
        return selectedSeatsInWagon;
    }

    @Override
    public void seatClicked(TrainSeatViewModel viewModel, int position, int height) {
        int pos[] = new int[2];
        gridLayoutManager.findViewByPosition(position).getLocationOnScreen(pos);
        int top = pos[1];
        int popupHeight = getResources().getDimensionPixelSize(R.dimen.train_seat_popup);
        TrainSeatPopupWindow trainPopup = new TrainSeatPopupWindow();
        trainPopup.showPopup(viewModel, height, pos[0], top, popupHeight);
    }

    @Override
    public String getWagonCode() {
        return trainWagonViewModel.getWagonCode();
    }


    public class TrainSeatPopupWindow {
        RecyclerView passengerRecyclerView;
        LinearLayout topSeatLayout;
        AppCompatImageView topSeatArrow;
        LinearLayout bottomSeatLayout;
        AppCompatImageView bottomSeatArrow;

        TrainSeatPopupWindow() {
        }

        void showPopup(TrainSeatViewModel viewModel, int height, int left, int top, int popupHeight) {
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.view_train_seat_popup, null);

            popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            bindView(popupView);
            setupRecyclerView(viewModel);
            preparePopUp(viewModel, height, left, top, popupHeight, popupView);

            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return true;
                }
            });
        }

        private void preparePopUp(TrainSeatViewModel viewModel, int height, int left, int top, int popupHeight, View popupView) {
            int newTop = 0;
            if (top > interaction.getToolbarHeight()) {
                if (top > popupHeight) {
                    // top view
                    newTop = top - popupHeight + height;
                    renderTopPopUpView(viewModel, left, popupView);
                } else {
                    // bottom view
                    newTop = top;
                    renderBottomPopUpView(viewModel, left, popupView);
                }

                popupWindow.showAtLocation(getView(), Gravity.TOP | Gravity.LEFT, 0, newTop);
                dimScreenBehind(popupWindow, DEFAULT_DIM_OPACITY);
            }

        }

        private void renderBottomPopUpView(TrainSeatViewModel viewModel, int left, View popupView) {
            topSeatLayout.setX(left);
            topSeatLayout.setLeft(left);
            bottomSeatLayout.setVisibility(View.GONE);
            bottomSeatArrow.setVisibility(View.GONE);
            AppCompatTextView topSeatTextView = popupView.findViewById(R.id.tv_top_seat_label);
            topSeatTextView.setText(String.format("%d%s", viewModel.getRow(), viewModel.getColumn()));
        }

        private void renderTopPopUpView(TrainSeatViewModel viewModel, int left, View popupView) {
            topSeatLayout.setVisibility(View.GONE);
            topSeatArrow.setVisibility(View.GONE);
            bottomSeatLayout.setX(left);
            bottomSeatLayout.setLeft(left);
            AppCompatTextView bottomSeatTextView = popupView.findViewById(R.id.tv_bottom_seat_label);
            bottomSeatTextView.setText(String.format("%d%s", viewModel.getRow(), viewModel.getColumn()));
        }

        private void setupRecyclerView(TrainSeatViewModel viewModel) {
            TrainSeatPopupAdapter adapter = new TrainSeatPopupAdapter(trainWagonViewModel.getWagonCode(), interaction.getPassengers(), viewModel);
            adapter.setListener(TrainWagonFragment.this);
            passengerRecyclerView.addItemDecoration(new TrainFullDividerItemDecoration(getActivity()));
            passengerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            passengerRecyclerView.setAdapter(adapter);
        }

        private void bindView(View popupView) {
            passengerRecyclerView = popupView.findViewById(R.id.rv_passenger);
            topSeatLayout = popupView.findViewById(R.id.top_seat_layout);
            topSeatArrow = popupView.findViewById(R.id.top_seat_arrow);
            bottomSeatLayout = popupView.findViewById(R.id.bottom_seat_layout);
            bottomSeatArrow = popupView.findViewById(R.id.bottom_seat_arrow);
        }


        void dimScreenBehind(PopupWindow popupWindow, Float dimAmount) {
            View container;
            if (popupWindow.getBackground() == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    container = (View) popupWindow.getContentView().getParent();
                } else {
                    container = popupWindow.getContentView();
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    container = (View) popupWindow.getContentView().getParent().getParent();
                } else {
                    container = (View) popupWindow.getContentView().getParent();
                }
            }
            Context context = popupWindow.getContentView().getContext();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
            p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = dimAmount;
            wm.updateViewLayout(container, p);
        }
    }
}
