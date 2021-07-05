package com.tokopedia.flight.orderlist.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.TaskStackBuilder;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalTravel;
import com.tokopedia.dialog.DialogUnify;
import com.tokopedia.flight.orderlist.R;
import com.tokopedia.flight.orderlist.di.DaggerFlightOrderComponent;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.flight.orderlist.util.FlightErrorUtil;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderAdapter;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderAdapterTypeFactory;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderTypeFactory;
import com.tokopedia.flight.orderlist.view.contract.FlightOrderListContract;
import com.tokopedia.flight.orderlist.view.presenter.FlightOrderListPresenter;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderBaseViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderSuccessViewModel;
import com.tokopedia.sortfilter.SortFilter;
import com.tokopedia.sortfilter.SortFilterItem;
import com.tokopedia.flight.resend_email.presentation.bottomsheet.FlightOrderResendEmailBottomSheet;
import com.tokopedia.unifycomponents.Toaster;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.flight.orderlist.view.FlightOrderListActivity.EXTRA_IS_AFTER_CANCELLATION;

/**
 * @author by zulfikarrahman on 11/28/17.
 */

public class FlightOrderListFragment extends BaseListFragment<Visitable, FlightOrderTypeFactory>
        implements FlightOrderListContract.View,
        FlightOrderAdapter.OnAdapterInteractionListener {

    public static final String EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID";
    public static final String EXTRA_CANCEL_JOURNEY = "EXTRA_CANCEL_JOURNEY";
    private static final int REQUEST_CODE_RESEND_ETICKET_DIALOG = 1;
    private static final int REQUEST_CODE_CANCELLATION = 2;
    private static final int REQUEST_CODE_ORDER_DETAIL = 3;
    public static final int PER_PAGE = 10;
    public static final boolean DEFAULT_CHECK_PRELOAD = true;

    private String selectedFilter = "";

    @Inject
    FlightOrderListPresenter presenter;

    private SortFilter quickSingleFilterView;

    public static FlightOrderListFragment createInstance() {
        Bundle bundle = new Bundle();

        FlightOrderListFragment fragment = new FlightOrderListFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        FlightOrderComponent component = DaggerFlightOrderComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();

        component.inject(this);
    }

    @NonNull
    @Override
    protected BaseListAdapter<Visitable, FlightOrderTypeFactory> createAdapterInstance() {
        return new BaseListAdapter<>(getAdapterTypeFactory());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_order_list, container, false);
        quickSingleFilterView = view.findViewById(R.id.quick_filter);
        return view;
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return view.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public RecyclerView getRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_orders);
        recyclerView.setHasFixedSize(true);
        return recyclerView;
    }

    @Override
    public void loadData(int page) {
        presenter.attachView(this);
        loadPageData(page);
        presenter.onGetProfileData();
    }

    @Override
    protected FlightOrderTypeFactory getAdapterTypeFactory() {
        return new FlightOrderAdapterTypeFactory(this);
    }

    @Override
    public void renderOrderStatus(List<SortFilterItem> filterItems) {
        quickSingleFilterView.addItem(new ArrayList(filterItems));
    }

    @Override
    public String getSelectedFilter() {
        return String.valueOf(selectedFilter);
    }

    @Override
    public void navigateToInputEmailForm(String invoiceId, String userId, String userEmail) {
        FlightOrderResendEmailBottomSheet bottomSheet = FlightOrderResendEmailBottomSheet.Companion
                .getInstance(userEmail, invoiceId);
        bottomSheet.setTargetFragment(this, REQUEST_CODE_RESEND_ETICKET_DIALOG);
        bottomSheet.setShowListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                bottomSheet.getBottomSheet().setState(BottomSheetBehavior.STATE_EXPANDED);
                return null;
            }
        });
        bottomSheet.show(getFragmentManager(), FlightOrderResendEmailBottomSheet.TAG);
    }

    @Override
    public void selectFilter(String typeFilter) {
        selectedFilter = typeFilter;
        showSwipeLoading();
        loadInitialData();
    }

    @Override
    public void onDetailOrderClicked(FlightOrderDetailPassData viewModel) {
        startActivityForResult(RouteManager.getIntent(getContext(),
                String.format(getString(R.string.flight_order_detail_applink_pattern), ApplinkConst.FLIGHT_ORDER, viewModel.getOrderId())),
                REQUEST_CODE_ORDER_DETAIL);
    }

    @Override
    public void onDetailOrderClicked(String orderId) {
        startActivityForResult(RouteManager.getIntent(getContext(),
                String.format(getString(R.string.flight_order_detail_applink_pattern), ApplinkConst.FLIGHT_ORDER, orderId)),
                REQUEST_CODE_ORDER_DETAIL);
    }

    @Override
    public void onHelpOptionClicked(String contactUsUrl) {
        RouteManager.route(getContext(), contactUsUrl);
    }

    @Override
    public void onItemClicked(Visitable visitable) {

    }

    @Override
    public void onDestroyView() {
        presenter.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onReBookingClicked(FlightOrderBaseViewModel item) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getActivity());
        Intent homeIntent = RouteManager.getIntent(getContext(), ApplinkConst.HOME);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(RouteManager.getIntent(getContext(), ApplinkConst.FLIGHT));
        taskStackBuilder.startActivities();
    }

    @Override
    public void onDownloadETicket(String invoiceId) {
        presenter.onDownloadEticket(invoiceId);
    }

    @Override
    public void onCancelOptionClicked(FlightOrderSuccessViewModel item) {
        presenter.onCancelButtonClicked(item);
    }

    @Override
    protected String getMessageFromThrowable(Context context, Throwable t) {
        return FlightErrorUtil.getMessageFromException(context, t);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_RESEND_ETICKET_DIALOG:
                if (resultCode == RESULT_OK) {
                    showSnackbarSuccess(R.string.resend_eticket_success);
                }
                break;
            case REQUEST_CODE_CANCELLATION:
                if (resultCode == RESULT_OK) {
                    getAdapter().clearAllElements();
                    getAdapter().notifyDataSetChanged();
                    loadData(getDefaultInitialPage());
                }
                break;
            case REQUEST_CODE_ORDER_DETAIL:
                if (resultCode == RESULT_OK && data != null &&
                        data.getBooleanExtra(EXTRA_IS_AFTER_CANCELLATION, false)) {
                    getAdapter().clearAllElements();
                    getAdapter().notifyDataSetChanged();
                    loadData(getDefaultInitialPage());
                }
        }
    }

    private void showSnackbarSuccess(int resId) {
        Toaster.make(requireView(), getString(resId), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL);
    }

    @NonNull
    private SpannableString buildAirlineContactInfo(String fullText, String mark) {
        final int color = getContext().getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500);
        int startIndex = fullText.indexOf(mark);
        int stopIndex = fullText.length();
        SpannableString description = new SpannableString(fullText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                presenter.onMoreAirlineInfoClicked();
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
    public void showNonRefundableCancelDialog(final String invoiceId, final List<FlightCancellationJourney> item, final List<FlightOrderJourney> orderJourneyList) {
        final DialogUnify dialog = new DialogUnify(getActivity(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE);
        dialog.setTitle(getString(R.string.flight_cancellation_dialog_title));
        dialog.setDescription(
                MethodChecker.fromHtml(getString(
                        R.string.flight_cancellation_dialog_non_refundable_description)));
        dialog.setPrimaryCTAText("Lanjut");
        dialog.setPrimaryCTAClickListener(() -> {
            goToCancellationPage(invoiceId, item);
            dialog.dismiss();
            return Unit.INSTANCE;
        });
        dialog.setSecondaryCTAText(getString(R.string.flight_cancellation_dialog_back_button_text));
        dialog.setSecondaryCTAClickListener(() -> {
            dialog.dismiss();
            return Unit.INSTANCE;
        });
        dialog.show();
    }

    @Override
    public void showRefundableCancelDialog(final String invoiceId, final List<FlightCancellationJourney> item, final List<FlightOrderJourney> orderJourneyList) {
        final DialogUnify dialog = new DialogUnify(getActivity(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE);
        dialog.setTitle(getString(R.string.flight_cancellation_dialog_title));
        dialog.setDescription(
                MethodChecker.fromHtml(getString(R.string.flight_cancellation_dialog_refundable_description)));
        dialog.setPrimaryCTAText("Lanjut");
        dialog.setPrimaryCTAClickListener(() -> {
            goToCancellationPage(invoiceId, item);
            dialog.dismiss();
            return Unit.INSTANCE;
        });
        dialog.setSecondaryCTAText(getString(R.string.flight_cancellation_dialog_back_button_text));
        dialog.setSecondaryCTAClickListener(() -> {
            dialog.dismiss();
            return Unit.INSTANCE;
        });
        dialog.show();
    }

    @Override
    public void goToCancellationPage(String invoiceId, List<FlightCancellationJourney> items) {
        Intent intent = RouteManager.getIntent(getContext(), ApplinkConstInternalTravel.CANCELLATION_FLIGHT);
        intent.putExtra(EXTRA_INVOICE_ID, invoiceId);
        intent.putParcelableArrayListExtra(EXTRA_CANCEL_JOURNEY, (ArrayList<? extends Parcelable>) items);

        startActivityForResult(intent, REQUEST_CODE_CANCELLATION);
    }

    @Override
    public void loadPageData(int page) {
        presenter.loadData(selectedFilter, page, PER_PAGE);
    }

    @Override
    public void navigateToWebview(String url) {
        RouteManager.route(getContext(), url);
    }
}
