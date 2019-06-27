package com.tokopedia.train.seat.presentation.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.common.travel.widget.CountdownTimeView;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.Menus;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.common.util.TrainFlowConstant;
import com.tokopedia.train.common.util.TrainFlowExtraConstant;
import com.tokopedia.train.common.util.TrainFlowUtil;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.reviewdetail.presentation.activity.TrainReviewDetailActivity;
import com.tokopedia.train.search.presentation.model.TrainScheduleBookingPassData;
import com.tokopedia.train.seat.di.TrainSeatComponent;
import com.tokopedia.train.seat.presentation.activity.TrainSeatActivity;
import com.tokopedia.train.seat.presentation.contract.TrainSeatContract;
import com.tokopedia.train.seat.presentation.fragment.listener.TrainSeatListener;
import com.tokopedia.train.seat.presentation.fragment.viewpager.TrainWagonsPagerAdapter;
import com.tokopedia.train.seat.presentation.presenter.TrainSeatPresenter;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatPassengerViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainSeatViewModel;
import com.tokopedia.train.seat.presentation.viewmodel.TrainWagonViewModel;
import com.tokopedia.train.seat.presentation.widget.TrainSeatChangesDialog;
import com.tokopedia.train.seat.presentation.widget.TrainSeatPagerIndicator;
import com.tokopedia.train.seat.presentation.widget.TrainSeatPassengerAndWagonView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class TrainSeatFragment extends BaseDaggerFragment implements TrainSeatContract.View, TrainSeatPassengerAndWagonView.TrainSeatActionListener {
    public static final int NEXT_STEP_REQUEST_CODE = 1010;
    private static final String EXTRA_SOFTBOOK = "EXTRA_SOFTBOOK";
    private static final String EXTRA_PASSDATA = "EXTRA_PASSDATA";
    private static final String EXTRA_DEPARTURE_STATE = "EXTRA_DEPARTURE_STATE";
    private static final String TRAIN_SELECT_SEAT_TRACE = "tr_train_select_seat";
    @Inject
    TrainSeatPresenter presenter;
    @Inject
    TrainFlowUtil trainFlowUtil;

    private CountdownTimeView countdownTimeView;
    private TrainSeatPassengerAndWagonView trainSeatHeader;
    private ViewPager wagonViewPager;
    private LinearLayout container;
    private RelativeLayout containerFullPage;
    private ProgressBar progressBar;
    private Button submitButton;
    private TrainSeatPagerIndicator topPagerIndicator;
    private TrainSeatPagerIndicator bottomPagerIndicator;
    private TrainWagonsPagerAdapter adapter;

    private List<TrainSeatPassengerViewModel> passengers;
    private List<TrainSeatPassengerViewModel> originPassengers;
    private List<TrainWagonViewModel> wagons;

    private TrainSoftbook trainSoftbook;
    private TrainScheduleBookingPassData passData;
    private boolean isDeparture;

    private boolean traceStop;
    private PerformanceMonitoring performanceMonitoring;

    private InteractionListener interactionListener;

    public interface InteractionListener {
        void setToolbar(String subtitle);

        int getToolbarHeight();
    }

    public static Fragment newInstance(TrainSoftbook trainSoftbook,
                                       TrainScheduleBookingPassData passData,
                                       boolean isDeparture) {
        Fragment fragment = new TrainSeatFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SOFTBOOK, trainSoftbook);
        bundle.putParcelable(EXTRA_PASSDATA, passData);
        bundle.putBoolean(EXTRA_DEPARTURE_STATE, isDeparture);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        trainSoftbook = getArguments().getParcelable(EXTRA_SOFTBOOK);
        passData = getArguments().getParcelable(EXTRA_PASSDATA);
        isDeparture = getArguments().getBoolean(EXTRA_DEPARTURE_STATE);
        super.onCreate(savedInstanceState);
        performanceMonitoring = PerformanceMonitoring.start(TRAIN_SELECT_SEAT_TRACE);
    }

    @Override
    protected void initInjector() {
        getComponent(TrainSeatComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_seat, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        countdownTimeView = (CountdownTimeView) view.findViewById(R.id.ct_countdown);
        trainSeatHeader = (TrainSeatPassengerAndWagonView) view.findViewById(R.id.train_seat_header);
        wagonViewPager = (ViewPager) view.findViewById(R.id.vp_wagon);
        container = (LinearLayout) view.findViewById(R.id.container);
        containerFullPage = (RelativeLayout) view.findViewById(R.id.container_full_page);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        wagonViewPager = (ViewPager) view.findViewById(R.id.vp_wagon);
        topPagerIndicator = (TrainSeatPagerIndicator) view.findViewById(R.id.top_seat_pager_indicator);
        topPagerIndicator.setListener(getWagonPagerIndicator());
        bottomPagerIndicator = (TrainSeatPagerIndicator) view.findViewById(R.id.seat_pager_indicator);
        bottomPagerIndicator.setListener(getWagonPagerIndicator());
        submitButton = (Button) view.findViewById(R.id.btn_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onSubmitButtonClicked();
            }
        });
        trainSeatHeader.setActionListener(this);
    }

    @NonNull
    private TrainSeatPagerIndicator.ActionListener getWagonPagerIndicator() {
        return new TrainSeatPagerIndicator.ActionListener() {
            @Override
            public void onIndicatorClicked(int position) {
                trainSeatHeader.renderWagon(wagons.get(position).getWagonCode());
                wagonViewPager.setCurrentItem(position);
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.onViewCreated();
        presenter.getSeatMaps();
        countdownTimeView.setListener(new CountdownTimeView.OnActionListener() {
            @Override
            public void onFinished() {
                presenter.onRunningOutOfTime();
            }
        });
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void renderWagon(List<TrainWagonViewModel> trainWagonViewModels, int maxRow) {
        wagons = trainWagonViewModels;
        renderPagerIndicator(trainWagonViewModels);
        trainSeatHeader.renderWagon(trainWagonViewModels.get(0).getWagonCode());
        trainSeatHeader.renderPassenger(getPassengers());
        double height = (maxRow + 1) * getResources().getDimensionPixelOffset(R.dimen.train_seat_with_margin);
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height);
        wagonViewPager.setLayoutParams(layoutParams);

        adapter = new TrainWagonsPagerAdapter(getFragmentManager(),
                trainWagonViewModels,
                getWagonFragmentInteractionListener());

        wagonViewPager.addOnPageChangeListener(getWagonPagerChangeListener());
        wagonViewPager.setOffscreenPageLimit(wagons.size());
        wagonViewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void renderPagerIndicator(List<TrainWagonViewModel> trainWagonViewModels) {
        topPagerIndicator.renderView(trainWagonViewModels.size());
        topPagerIndicator.setCurrentIndicator(0);
        bottomPagerIndicator.renderView(trainWagonViewModels.size());
        bottomPagerIndicator.setCurrentIndicator(0);
    }

    @NonNull
    private ViewPager.OnPageChangeListener getWagonPagerChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pos) {
                trainSeatHeader.renderWagon(wagons.get(pos).getWagonCode());
                wagonViewPager.setCurrentItem(pos);
                setSelectedPager(pos);
                Object fragment = adapter.instantiateItem(wagonViewPager, wagonViewPager.getCurrentItem());
                if (fragment instanceof TrainSeatListener) {
                    ((TrainSeatListener) fragment).notifyPassengerUpdate();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    private void setSelectedPager(int pos) {
        topPagerIndicator.setCurrentIndicator(pos);
        bottomPagerIndicator.setCurrentIndicator(pos);
    }

    @NonNull
    private TrainWagonFragment.OnFragmentInteraction getWagonFragmentInteractionListener() {
        return new TrainWagonFragment.OnFragmentInteraction() {
            @Override
            public List<TrainSeatPassengerViewModel> getPassengers() {
                return passengers;
            }

            @Override
            public List<TrainSeatPassengerViewModel> getOriginPassengers() {
                return originPassengers;
            }


            @Override
            public void onPassengerSeatChange(TrainSeatPassengerViewModel passenger, TrainSeatViewModel seat, String wagonCode) {
                presenter.onPassengerSeatChange(passenger, seat, wagonCode);
            }

            @Override
            public int getToolbarHeight() {
                return interactionListener.getToolbarHeight();
            }
        };
    }

    @Override
    public void updateSelectedWagon() {
        Object fragment = adapter.instantiateItem(wagonViewPager, wagonViewPager.getCurrentItem());
        if (fragment != null && fragment instanceof TrainSeatListener) {
            ((TrainSeatListener) fragment).notifyPassengerUpdate();
        }
        trainSeatHeader.renderPassenger(getPassengers());
    }

    @Override
    public void hidePage() {
        container.setVisibility(View.GONE);
    }

    @Override
    public void showPage() {
        container.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorGetSeatMaps(String message) {
        NetworkErrorHelper.showEmptyState(getActivity(), containerFullPage, message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.getSeatMaps();
            }
        });
    }

    @Override
    public void showErrorChangeSeat(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public String getExpireDate() {
        return trainSoftbook.getExpiryTimestamp();
    }

    @Override
    public void renderExpireDateCountdown(Date expireDate) {
        countdownTimeView.setExpiredDate(expireDate);
        countdownTimeView.start();
    }

    @Override
    public void showExpiredPaymentDialog() {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.RETORIC);
        dialog.setTitle(getString(R.string.train_seat_expired_payment_time));
        dialog.setDesc(getString(R.string.train_seat_expired_payment_time_desc));
        dialog.setBtnOk(getString(R.string.train_seat_expired_payment_time_cta));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                trainFlowUtil.actionSetResultAndClose(
                        getActivity(),
                        getActivity().getIntent(),
                        TrainFlowConstant.RESEARCH
                );
            }
        });
        dialog.show();
    }

    @Override
    public List<TrainSeatPassengerViewModel> getPassengers() {
        return passengers;
    }

    @Override
    public List<TrainSeatPassengerViewModel> getOriginalPassenger() {
        return originPassengers;
    }

    @Override
    public void navigateToReview(TrainSoftbook trainSoftbook) {
        if (!isReturning()) {
            if (trainSoftbook.getReturnTrips() != null && trainSoftbook.getReturnTrips().size() > 0) {
                startActivityForResult(TrainSeatActivity.getCallingIntent(getActivity(), trainSoftbook, passData, false), NEXT_STEP_REQUEST_CODE);
            } else {
                startActivityForResult(TrainReviewDetailActivity.createIntent(getActivity(), trainSoftbook, passData), NEXT_STEP_REQUEST_CODE);
            }
        } else {
            startActivityForResult(TrainReviewDetailActivity.createIntent(getActivity(), trainSoftbook, passData), NEXT_STEP_REQUEST_CODE);
        }
    }

    @Override
    public TrainSoftbook getTrainSoftbook() {
        return trainSoftbook;
    }

    @Override
    public boolean isReturning() {
        return !isDeparture;
    }

    @Override
    public void setOriginPassenger(List<TrainSeatPassengerViewModel> originPassengers) {
        this.originPassengers =  new ArrayList<>();
        this.originPassengers.addAll(originPassengers);
    }

    @Override
    public void setPassengers(List<TrainSeatPassengerViewModel> originPassengers) {
        this.passengers = originPassengers;
    }

    @Override
    public void onWagonClicked() {
        presenter.onWagonChooserClicked();
    }

    @Override
    public void showWagonChooser() {
        Menus menus = new Menus(getActivity());
        String[] wagonsTitle = new String[wagons.size()];
        for (int i = 0; i < wagons.size(); i++) {
            wagonsTitle[i] = wagons.get(i).getWagonCode();
        }

        menus.setItemMenuList(wagonsTitle);
        menus.setActionText(getString(R.string.train_seat_wagon_close_label));
        menus.setOnActionClickListener(view -> menus.dismiss());
        menus.setOnItemMenuClickListener((itemMenus, pos) -> {
            trainSeatHeader.renderWagon(wagons.get(pos).getWagonCode());
            wagonViewPager.setCurrentItem(pos);
            setSelectedPager(pos);
            menus.dismiss();
        });
        menus.show();
    }

    @Override
    public void setToolbarSubTitle(String subtitle) {
        interactionListener.setToolbar(subtitle);
    }

    @Override
    public void showConfirmChangePassengersDialog(List<TrainSeatPassengerViewModel> passengers) {
        TrainSeatChangesDialog dialog = new TrainSeatChangesDialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setPassengers(passengers);
        dialog.setTitle(getString(R.string.train_seat_confirm_change_seat_title));
        dialog.setBtnOk(getString(R.string.train_seat_confirm_dialog_accept));
        dialog.setBtnCancel(getString(R.string.train_seat_confirm_dialog_cancel));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.onChangePassengerConfirmDialogAccepted();
            }
        });
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void clearSeatMaps() {

    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if (context instanceof InteractionListener) {
            interactionListener = (InteractionListener) context;
        } else {
            throw new RuntimeException("Activity must implement " + InteractionListener.class.getSimpleName());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case NEXT_STEP_REQUEST_CODE:
                if (data != null) {
                    if (data.getIntExtra(TrainFlowExtraConstant.EXTRA_FLOW_DATA, -1) != -1) {
                        trainFlowUtil.actionSetResultAndClose(
                                getActivity(),
                                getActivity().getIntent(),
                                data.getIntExtra(TrainFlowExtraConstant.EXTRA_FLOW_DATA, 0)
                        );
                    }
                }
                break;
        }
    }

    @Override
    public void stopTrace() {
        if (!traceStop) {
            performanceMonitoring.stopTrace();
            traceStop = true;
        }
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
