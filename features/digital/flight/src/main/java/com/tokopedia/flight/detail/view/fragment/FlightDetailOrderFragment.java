package com.tokopedia.flight.detail.view.fragment;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.common.travel.data.entity.TravelCrossSelling;
import com.tokopedia.common.travel.utils.TrackingCrossSellUtil;
import com.tokopedia.common.travel.widget.TravelCrossSellWidget;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.adapter.FlightSimpleAdapter;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.cancellation.view.activity.FlightCancellationActivity;
import com.tokopedia.flight.cancellation.view.activity.FlightCancellationListActivity;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.dashboard.view.activity.FlightDashboardActivity;
import com.tokopedia.flight.detail.presenter.ExpandableOnClickListener;
import com.tokopedia.flight.detail.presenter.FlightDetailOrderContract;
import com.tokopedia.flight.detail.presenter.FlightDetailOrderPresenter;
import com.tokopedia.flight.detail.view.activity.FlightInvoiceActivity;
import com.tokopedia.flight.detail.view.adapter.FlightDetailOrderAdapter;
import com.tokopedia.flight.detail.view.adapter.FlightDetailOrderTypeFactory;
import com.tokopedia.flight.detail.view.adapter.FlightOrderDetailInsuranceAdapter;
import com.tokopedia.flight.detail.view.model.FlightDetailOrderJourney;
import com.tokopedia.flight.orderlist.domain.model.FlightInsurance;
import com.tokopedia.flight.orderlist.domain.model.FlightOrder;
import com.tokopedia.flight.orderlist.util.FlightErrorUtil;
import com.tokopedia.flight.orderlist.view.fragment.FlightResendETicketDialogFragment;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.flight.review.view.adapter.FlightBookingReviewPassengerAdapter;
import com.tokopedia.flight.review.view.adapter.FlightBookingReviewPassengerAdapterTypeFactory;
import com.tokopedia.flight.review.view.model.FlightDetailPassenger;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifycomponents.ticker.TickerCallback;

import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.flight.orderlist.view.FlightOrderListActivity.EXTRA_IS_AFTER_CANCELLATION;
import static com.tokopedia.flight.orderlist.view.FlightOrderListActivity.EXTRA_IS_CANCELLATION;

/**
 * Created by zulfikarrahman on 12/12/17.
 */

public class FlightDetailOrderFragment extends BaseDaggerFragment implements FlightDetailOrderContract.View, ExpandableOnClickListener {

    private static final int REQUEST_CODE_RESEND_ETICKET_DIALOG = 1;
    private static final int REQUEST_CODE_CANCELLATION = 2;
    private static final String RESEND_ETICKET_DIALOG_TAG = "resend_eticket_dialog_tag";
    public static final String EXTRA_ORDER_DETAIL_PASS = "EXTRA_ORDER_DETAIL_PASS";
    private static final float JOURNEY_TITLE_FONT_SIZE = 18;

    @Inject
    FlightDetailOrderPresenter flightDetailOrderPresenter;
    @Inject
    TrackingCrossSellUtil trackingCrossSellUtil;

    private RemoteConfig remoteConfig;

    private TextView orderId;
    private ImageView copyOrderId;
    private View containerSendEticket;
    private LinearLayout containerCancellation;
    private TextView orderStatus;
    private TextView transactionDate;
    private View layoutExpendablePassenger;
    private AppCompatImageView imageExpendablePassenger;
    private VerticalRecyclerView recyclerViewFlight;
    private VerticalRecyclerView recyclerViewPassenger;
    private RecyclerView recyclerViewPrice;
    private View containerDownloadInvoice;
    private TextView totalPrice;
    private TextView orderHelp;
    private Button buttonCancelTicket;
    private Button buttonRescheduleTicket;
    private Button buttonReorder;
    private ProgressDialog progressDialog;
    private LinearLayout showEticket;

    private FlightDetailOrderAdapter flightDetailOrderAdapter;
    private FlightBookingReviewPassengerAdapter flightBookingReviewPassengerAdapter;
    private FlightSimpleAdapter flightBookingReviewPriceAdapter;
    private FlightOrderDetailPassData flightOrderDetailPassData;
    private FlightOrder flightOrder;
    private String eticketLink = "";
    private String invoiceLink = "";
    private String cancelMessage = "";
    private boolean isPassengerInfoShowed = true;
    private LinearLayout paymentInfoLayout;
    private LinearLayout paymentCostLayout;
    private LinearLayout paymentDueDateLayout;
    private TextView tvPaymentDescriptionLabel;
    private TextView tvPaymentDescription;
    private TextView tvPaymentCost;
    private TextView tvPaymentCostLabel;
    private TextView tvPaymentDueDate;
    private Ticker cancellationWarningTicker;
    private LinearLayout insuranceLayout;
    private RecyclerView insuranceRecyclerView;
    private TravelCrossSellWidget travelCrossSellWidget;

    private boolean isCancellation;

    public static Fragment createInstance(FlightOrderDetailPassData flightOrderDetailPassData, boolean isCancellation) {
        FlightDetailOrderFragment flightDetailOrderFragment = new FlightDetailOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ORDER_DETAIL_PASS, flightOrderDetailPassData);
        bundle.putBoolean(EXTRA_IS_CANCELLATION, isCancellation);
        flightDetailOrderFragment.setArguments(bundle);
        return flightDetailOrderFragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightComponent.class)
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flightOrderDetailPassData = getArguments().getParcelable(EXTRA_ORDER_DETAIL_PASS);
        isCancellation = getArguments().getBoolean(EXTRA_IS_CANCELLATION, false);
        remoteConfig = new FirebaseRemoteConfigImpl(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_detail_order, container, false);
        orderId = view.findViewById(R.id.order_id_detail);
        copyOrderId = view.findViewById(R.id.copy_order_id);
        containerSendEticket = view.findViewById(R.id.container_send_eticket);
        orderStatus = view.findViewById(R.id.status_ticket);
        transactionDate = view.findViewById(R.id.transaction_date);
        layoutExpendablePassenger = view.findViewById(R.id.layout_expendable_passenger);
        imageExpendablePassenger = view.findViewById(R.id.image_expendable_passenger);
        recyclerViewFlight = view.findViewById(R.id.recycler_view_flight);
        recyclerViewPassenger = view.findViewById(R.id.recycler_view_data_passenger);
        recyclerViewPrice = view.findViewById(R.id.recycler_view_detail_price);
        containerDownloadInvoice = view.findViewById(R.id.container_download_invoice);
        totalPrice = view.findViewById(R.id.total_price);
        orderHelp = view.findViewById(R.id.help);
        buttonCancelTicket = view.findViewById(R.id.button_cancel);
        buttonRescheduleTicket = view.findViewById(R.id.button_reschedule);
        buttonReorder = view.findViewById(R.id.button_reorder);
        paymentInfoLayout = view.findViewById(R.id.payment_info_layout);
        paymentCostLayout = view.findViewById(R.id.payment_cost_layout);
        paymentDueDateLayout = view.findViewById(R.id.payment_due_date_layout);
        tvPaymentDescriptionLabel = view.findViewById(R.id.tv_payment_description_label);
        tvPaymentDescription = view.findViewById(R.id.tv_payment_description);
        tvPaymentCost = view.findViewById(R.id.tv_payment_cost);
        tvPaymentCostLabel = view.findViewById(R.id.tv_payment_cost_label);
        tvPaymentDueDate = view.findViewById(R.id.tv_payment_due_date);
        insuranceLayout = view.findViewById(R.id.insurance_layout);
        insuranceRecyclerView = view.findViewById(R.id.rv_insurance);
        showEticket = view.findViewById(R.id.tv_lihat_e_ticket);
        travelCrossSellWidget = view.findViewById(R.id.cross_sell_widget);
        progressDialog = new ProgressDialog(getActivity());

        containerCancellation = view.findViewById(com.tokopedia.flight.R.id.cancellation_container);
        cancellationWarningTicker = view.findViewById(com.tokopedia.flight.R.id.cancellation_warning);

        setViewClickListener();

        FlightDetailOrderTypeFactory flightDetailOrderTypeFactory = new FlightDetailOrderTypeFactory(this, JOURNEY_TITLE_FONT_SIZE);
        flightDetailOrderAdapter = new FlightDetailOrderAdapter(flightDetailOrderTypeFactory);
        FlightBookingReviewPassengerAdapterTypeFactory flightBookingReviewPassengerAdapterTypeFactory = new FlightBookingReviewPassengerAdapterTypeFactory();
        flightBookingReviewPassengerAdapter = new FlightBookingReviewPassengerAdapter(flightBookingReviewPassengerAdapterTypeFactory);
        flightBookingReviewPriceAdapter = new FlightSimpleAdapter();

        recyclerViewFlight.setAdapter(flightDetailOrderAdapter);
        recyclerViewPassenger.setAdapter(flightBookingReviewPassengerAdapter);
        recyclerViewPrice.setAdapter(flightBookingReviewPriceAdapter);
        recyclerViewPrice.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressDialog.setMessage(getString(com.tokopedia.flight.R.string.flight_booking_loading_title));
        progressDialog.setCancelable(false);
        orderId.setText(flightOrderDetailPassData.getOrderId());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flightDetailOrderPresenter.attachView(this);
        flightDetailOrderPresenter.getDetail(flightOrderDetailPassData.getOrderId(), flightOrderDetailPassData);
        if (remoteConfig.getBoolean(RemoteConfigKey.ANDROID_CUSTOMER_TRAVEL_ENABLE_CROSS_SELL))
            flightDetailOrderPresenter.getCrossSellingItems(flightOrderDetailPassData.getOrderId(), GraphqlHelper.loadRawString(getResources(), com.tokopedia.common.travel.R.raw.query_travel_cross_selling));
        flightDetailOrderPresenter.onGetProfileData();
    }

    void setViewClickListener() {
        copyOrderId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getString(com.tokopedia.flight.R.string.flight_label_order_id), orderId.getText().toString());
                clipboard.setPrimaryClip(clip);
                clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                    @Override
                    public void onPrimaryClipChanged() {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), com.tokopedia.flight.R.string.flight_label_copy_clipboard, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        buttonCancelTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flightDetailOrderPresenter.actionCancelOrderButtonClicked();
            }
        });

        buttonRescheduleTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        containerSendEticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flightDetailOrderPresenter.onSendEticketButtonClicked();
            }
        });

        showEticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToShowEticket();
            }
        });

        containerDownloadInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FlightInvoiceActivity.newInstance(getActivity(), invoiceLink));
            }
        });

        containerCancellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCancellationListPage();
            }
        });

        orderHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flightDetailOrderPresenter.onHelpButtonClicked(getFlightOrder().getContactUsUrl());
            }
        });
        buttonReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flightDetailOrderPresenter.actionReorderButtonClicked();
            }
        });

        layoutExpendablePassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageExpendablePassenger.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.flight_rotate_reverse));
                togglePassengerInfo();
            }
        });
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onErrorGetOrderDetail(Throwable e) {
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), FlightErrorUtil.getMessageFromException(getActivity(), e), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                flightDetailOrderPresenter.getDetail(flightOrderDetailPassData.getOrderId(), flightOrderDetailPassData);
                if (remoteConfig.getBoolean(RemoteConfigKey.ANDROID_CUSTOMER_TRAVEL_ENABLE_CROSS_SELL))
                    flightDetailOrderPresenter.getCrossSellingItems(flightOrderDetailPassData.getOrderId(), GraphqlHelper.loadRawString(getResources(), com.tokopedia.common.travel.R.raw.query_travel_cross_selling));

            }
        }).showRetrySnackbar();
    }

    @Override
    public void updateFlightList(List<FlightDetailOrderJourney> journeys) {
        flightDetailOrderAdapter.addElement(journeys);
        flightDetailOrderAdapter.notifyDataSetChanged();
    }

    @Override
    public void updatePassengerList(List<FlightDetailPassenger> flightDetailPassengers) {
        if (flightDetailPassengers.size() < 2) {
            removePassengerRecyclerDivider();
        } else {
            addPassengerRecyclerDivider();
        }

        flightBookingReviewPassengerAdapter.addElement(flightDetailPassengers);
        flightBookingReviewPassengerAdapter.notifyDataSetChanged();
    }

    @Override
    public void updatePrice(List<SimpleViewModel> priceList, String totalPrice) {
        flightBookingReviewPriceAdapter.setViewModels(priceList);
        flightBookingReviewPriceAdapter.notifyDataSetChanged();
        this.totalPrice.setText(totalPrice);
    }

    @Override
    public void setTransactionDate(String transactionDate) {
        this.transactionDate.setText(transactionDate);
    }

    @Override
    public void hideInsuranceLayout() {
        insuranceLayout.setVisibility(View.GONE);
    }

    @Override
    public void showInsuranceLayout() {
        insuranceLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderInsurances(List<FlightInsurance> insurances) {
        FlightOrderDetailInsuranceAdapter adapter = new FlightOrderDetailInsuranceAdapter(insurances);
        insuranceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        insuranceRecyclerView.setAdapter(adapter);
    }

    @Override
    public void checkIfShouldGoToCancellation() {
        if (isCancellation) {
            navigateToCancellationListPage();
        }
    }

    @Override
    public void showCrossSellingItems(TravelCrossSelling travelCrossSelling) {
        trackingCrossSellUtil.crossSellImpression(travelCrossSelling.getItems());
        travelCrossSellWidget.setVisibility(View.VISIBLE);
        travelCrossSellWidget.buildView(travelCrossSelling);
        travelCrossSellWidget.setListener((item, position) -> {
            trackingCrossSellUtil.crossSellClick(item, position);
            RouteManager.route(getContext(), item.getUri());
        });
    }

    @Override
    public void hideCrossSellingItems() {
        travelCrossSellWidget.setVisibility(View.GONE);
    }

    @Override
    public void updateOrderData(String eTicketLink, String invoiceLink, String cancelMessage) {
        this.eticketLink = eTicketLink;
        this.invoiceLink = invoiceLink;
        this.cancelMessage = cancelMessage;
    }

    private void removePassengerRecyclerDivider() {
        recyclerViewPassenger.clearItemDecoration();
    }

    private void addPassengerRecyclerDivider() {
        recyclerViewPassenger.addItemDecoration(new DividerItemDecoration(recyclerViewPassenger.getContext()));
    }

    private void togglePassengerInfo() {
        if (isPassengerInfoShowed) {
            hidePassengerInfo();
        } else {
            showPassengerInfo();
        }
    }

    private void hidePassengerInfo() {
        isPassengerInfoShowed = false;
        recyclerViewPassenger.setVisibility(View.GONE);
        imageExpendablePassenger.setRotation(180);
    }

    private void showPassengerInfo() {
        isPassengerInfoShowed = true;
        recyclerViewPassenger.setVisibility(View.VISIBLE);
        imageExpendablePassenger.setRotation(0);
    }

    @Override
    public void updateViewStatus(String orderStatusString, int color, boolean isTicketVisible, boolean isScheduleVisible,
                                 boolean isCancelVisible, boolean isReorderVisible) {
        orderStatus.setText(orderStatusString);
        orderStatus.setTextColor(ContextCompat.getColor(getActivity(), color));
        if (isTicketVisible) {
            containerSendEticket.setVisibility(View.VISIBLE);
        } else {
            containerSendEticket.setVisibility(View.GONE);
        }
        if (isScheduleVisible) {
            buttonRescheduleTicket.setVisibility(View.VISIBLE);
        } else {
            buttonRescheduleTicket.setVisibility(View.GONE);
        }
        if (isCancelVisible) {
            buttonCancelTicket.setVisibility(View.VISIBLE);
        } else {
            buttonCancelTicket.setVisibility(View.GONE);
        }
        if (isReorderVisible) {
            buttonReorder.setVisibility(View.VISIBLE);
        } else {
            buttonReorder.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        flightDetailOrderPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public String getCancelMessage() {
        return cancelMessage;
    }

    @Override
    public void navigateToWebview(String url) {
        RouteManager.route(getContext(), url);
    }

    @Override
    public void navigateToFlightHomePage() {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getActivity());
        Intent homeIntent = RouteManager.getIntent(getContext(), ApplinkConst.HOME);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(FlightDashboardActivity.getCallingIntent(getActivity()));
        taskStackBuilder.startActivities();
    }

    @Override
    public void renderFlightOrder(FlightOrder flightOrder) {
        this.flightOrder = flightOrder;
    }

    @Override
    public FlightOrder getFlightOrder() {
        return flightOrder;
    }

    @Override
    public void navigateToCancellationPage(String invoiceId, List<FlightCancellationJourney> items) {
        startActivityForResult(
                FlightCancellationActivity.createIntent(getContext(), invoiceId, items),
                REQUEST_CODE_CANCELLATION
        );

    }

    @Override
    public void onCloseExpand(int position) {
        // do something to scroll the view
    }

    @Override
    public void showPaymentInfoLayout() {
        paymentInfoLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePaymentInfoLayout() {
        paymentInfoLayout.setVisibility(View.GONE);
    }

    @Override
    public void setPaymentLabel(int resId) {
        tvPaymentDescriptionLabel.setText(resId);
    }

    @Override
    public void setPaymentDescription(CharSequence description) {
        tvPaymentDescription.setText(description);
    }

    @Override
    public void setTotalTransfer(String price) {
        tvPaymentCost.setVisibility(View.VISIBLE);
        tvPaymentCost.setText(price);
    }

    @Override
    public void hideTotalTransfer() {
        paymentCostLayout.setVisibility(View.GONE);
        tvPaymentCostLabel.setVisibility(View.GONE);
        tvPaymentCost.setVisibility(View.GONE);
    }

    @Override
    public void setPaymentDueDate(String dueDate) {
        paymentDueDateLayout.setVisibility(View.VISIBLE);
        tvPaymentDueDate.setText(dueDate);
    }

    @Override
    public void hidePaymentDueDate() {
        paymentDueDateLayout.setVisibility(View.GONE);
    }

    @Override
    public void showLihatEticket() {
        showEticket.setVisibility(View.VISIBLE);
        showEticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToShowEticket();
            }
        });
    }

    @Override
    public void hideLihatEticket() {
        showEticket.setVisibility(View.GONE);
    }

    @Override
    public void showCancellationStatus(String status) {
        cancellationWarningTicker.setHtmlDescription(status);
        cancellationWarningTicker.setDescriptionClickEvent(new TickerCallback() {
            @Override
            public void onDescriptionViewClick(CharSequence charSequence) {
                navigateToCancellationListPage();
            }

            @Override
            public void onDismiss() {

            }
        });
        cancellationWarningTicker.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCancellationContainer() {
        containerCancellation.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCancellationContainer() {
        containerCancellation.setVisibility(View.GONE);
    }

    @Override
    public void hideCancelButton() {
        buttonCancelTicket.setVisibility(View.GONE);
    }

    @Override
    public void navigateToInputEmailForm(String userId, String userEmail) {
        DialogFragment dialogFragment = FlightResendETicketDialogFragment.newInstace(
                flightOrderDetailPassData.getOrderId(),
                userId, userEmail);
        dialogFragment.setTargetFragment(this, REQUEST_CODE_RESEND_ETICKET_DIALOG);
        dialogFragment.show(getFragmentManager().beginTransaction(), RESEND_ETICKET_DIALOG_TAG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_RESEND_ETICKET_DIALOG:
                if (resultCode == RESULT_OK) {
                    showGreenSnackbar(com.tokopedia.flight.orderlist.R.string.resend_eticket_success);
                }
                break;
            case REQUEST_CODE_CANCELLATION:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_IS_AFTER_CANCELLATION, true);
                    getActivity().setResult(RESULT_OK, intent);
                    getActivity().finish();
                }
                break;
        }
    }

    @NonNull
    private SpannableString buildAirlineContactInfo(String fullText, String mark) {
        final int color = getContext().getResources().getColor(com.tokopedia.design.R.color.green_500);
        int startIndex = fullText.indexOf(mark);
        int stopIndex = fullText.length();
        SpannableString description = new SpannableString(fullText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                flightDetailOrderPresenter.onMoreAirlineInfoClicked();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(color);
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            }
        };
        description.setSpan(clickableSpan, startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return description;
    }

    @Override
    public void showRefundableCancelDialog(final String invoiceId, final List<FlightCancellationJourney> items) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(com.tokopedia.flight.orderlist.R.string.flight_cancellation_dialog_title));
        dialog.setDesc(MethodChecker.fromHtml(
                getString(com.tokopedia.flight.orderlist.R.string.flight_cancellation_dialog_refundable_description)));
        dialog.setBtnOk("Lanjut");
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCancellationPage(invoiceId, items);
                dialog.dismiss();
            }
        });
        dialog.setBtnCancel(getString(com.tokopedia.flight.orderlist.R.string.flight_cancellation_dialog_back_button_text));
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showNonRefundableCancelDialog(final String invoiceId, final List<FlightCancellationJourney> items) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(com.tokopedia.flight.orderlist.R.string.flight_cancellation_dialog_title));
        dialog.setDesc(MethodChecker.fromHtml(getString(
                com.tokopedia.flight.orderlist.R.string.flight_cancellation_dialog_non_refundable_description)));
        dialog.setBtnOk("Lanjut");
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCancellationPage(invoiceId, items);
                dialog.dismiss();
            }
        });
        dialog.setBtnCancel(getString(com.tokopedia.flight.orderlist.R.string.flight_cancellation_dialog_back_button_text));
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void navigateToShowEticket() {
        RouteManager.route(getContext(), flightOrder.getEticketUri());
    }

    private void showGreenSnackbar(int resId) {
        NetworkErrorHelper.showGreenCloseSnackbar(getActivity(), getString(resId));
    }

    private void navigateToCancellationListPage() {
        startActivity(FlightCancellationListActivity.createIntent(getContext(), getFlightOrder().getId()));
    }
}
