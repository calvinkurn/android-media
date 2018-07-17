package com.tokopedia.flight.orderlist.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo;
import com.tokopedia.flight.cancellation.view.activity.FlightCancellationActivity;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.common.constant.FlightUrl;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.dashboard.view.activity.FlightDashboardActivity;
import com.tokopedia.flight.detail.view.activity.FlightDetailOrderActivity;
import com.tokopedia.flight.orderlist.contract.FlightOrderListContract;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;
import com.tokopedia.flight.orderlist.presenter.FlightOrderListPresenter;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderAdapter;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderAdapterTypeFactory;
import com.tokopedia.flight.orderlist.view.adapter.FlightOrderTypeFactory;
import com.tokopedia.flight.orderlist.view.fragment.FlightResendETicketDialogFragment;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderBaseViewModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderSuccessViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.flight.detail.view.fragment.FlightDetailOrderFragment.EXTRA_IS_AFTER_CANCELLATION;


/**
 * @author by zulfikarrahman on 11/28/17.
 */

public class FlightOrderListFragment extends BaseListFragment<Visitable, FlightOrderTypeFactory>
        implements FlightOrderListContract.View,
        QuickSingleFilterView.ActionListener,
        FlightOrderAdapter.OnAdapterInteractionListener {

    private static final int REQUEST_CODE_RESEND_ETICKET_DIALOG = 1;
    private static final int REQUEST_CODE_CANCELLATION = 2;
    private static final int REQUEST_CODE_ORDER_DETAIL = 3;
    private static final String RESEND_ETICKET_DIALOG_TAG = "resend_eticket_dialog_tag";
    public static final int PER_PAGE = 10;
    @Inject
    FlightOrderListPresenter presenter;
    private QuickSingleFilterView quickSingleFilterView;

    private String selectedFilter;

    public static FlightOrderListFragment createInstance() {
        return new FlightOrderListFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightOrderComponent.class)
                .inject(this);
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
        quickSingleFilterView.setListener(this);
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
        presenter.loadData(selectedFilter, page, PER_PAGE);
        presenter.onGetProfileData();
    }

    @Override
    protected FlightOrderTypeFactory getAdapterTypeFactory() {
        return new FlightOrderAdapterTypeFactory(this);
    }

    @Override
    public void renderOrderStatus(List<QuickFilterItem> filterItems) {
        quickSingleFilterView.setDefaultItem(filterItems.get(0));
        quickSingleFilterView.renderFilter(filterItems);
    }

    @Override
    public String getSelectedFilter() {
        return String.valueOf(selectedFilter);
    }

    @Override
    public void navigateToInputEmailForm(String invoiceId, String userId, String userEmail) {
        DialogFragment dialogFragment = FlightResendETicketDialogFragment.newInstace(invoiceId, userId, userEmail);
        dialogFragment.setTargetFragment(this, REQUEST_CODE_RESEND_ETICKET_DIALOG);
        dialogFragment.show(getFragmentManager().beginTransaction(), RESEND_ETICKET_DIALOG_TAG);
    }

    @Override
    public Observable<ProfileInfo> getProfileObservable() {
        if (getActivity().getApplication() instanceof FlightModuleRouter
                && ((FlightModuleRouter) getActivity().getApplication())
                .getProfile() != null) {
            return ((FlightModuleRouter) getActivity().getApplication())
                    .getProfile();
        }
        return Observable.empty();
    }

    @Override
    public void selectFilter(String typeFilter) {
        selectedFilter = typeFilter;
        showSwipeLoading();
        loadInitialData();
    }

    @Override
    public void onDetailOrderClicked(FlightOrderDetailPassData viewModel) {
        startActivityForResult(FlightDetailOrderActivity.createIntent(getActivity(), viewModel),
                REQUEST_CODE_ORDER_DETAIL);
    }

    @Override
    public void onDetailOrderClicked(String orderId) {
        FlightOrderDetailPassData passData = new FlightOrderDetailPassData();
        passData.setOrderId(orderId);
        startActivityForResult(FlightDetailOrderActivity.createIntent(getActivity(), passData),
                REQUEST_CODE_ORDER_DETAIL);
    }

    @Override
    public void onHelpOptionClicked(String invoiceId, int status) {
        StringBuilder result = new StringBuilder(FlightUrl.CONTACT_US_FLIGHT_PREFIX_GLOBAL);
        result.append("&iv=" + invoiceId);
        result.append("&ostat=" + status);
        String url = result.toString();
        if (getActivity().getApplication() instanceof FlightModuleRouter
                && ((FlightModuleRouter) getActivity().getApplication())
                .getDefaultContactUsIntent(getActivity(), url) != null) {
            startActivity(((FlightModuleRouter) getActivity().getApplication())
                    .getDefaultContactUsIntent(getActivity(), url));
        }
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
        if (getActivity().getApplication() instanceof FlightModuleRouter
                && ((FlightModuleRouter) getActivity().getApplication())
                .getHomeIntent(getActivity()) != null) {
            Intent intent = ((FlightModuleRouter) getActivity().getApplication())
                    .getHomeIntent(getActivity());
            taskStackBuilder.addNextIntent(intent);
        }
        taskStackBuilder.addNextIntent(FlightDashboardActivity.getCallingIntent(getActivity()));
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
                    showGreenSnackbar(R.string.resend_eticket_success);
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

    private void showGreenSnackbar(int resId) {
        NetworkErrorHelper.showGreenCloseSnackbar(getActivity(), getString(resId));
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void showLessThan6HoursDialog() {
        int color = getContext().getResources().getColor(R.color.green_500);
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.RETORIC);
        dialog.setTitle(getString(R.string.flight_cancellation_dialog_title));
        dialog.setDesc(MethodChecker.fromHtml(getString(
            R.string.flight_cancellation_recommendation_to_contact_airlines_description,
            color, "#")));
        dialog.setBtnOk("OK");
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showNonRefundableCancelDialog(final String invoiceId, final List<FlightCancellationJourney> item, final String departureTime) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.flight_cancellation_dialog_title));
        dialog.setDesc(
            MethodChecker.fromHtml(getString(
                R.string.flight_cancellation_dialog_non_refundable_description)));
        dialog.setBtnOk(getString(R.string.flight_cancellation_dialog_back_button_text));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setBtnCancel("Lanjut");
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkIfFlightCancellable(departureTime, invoiceId, item);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showRefundableCancelDialog(final String invoiceId, final List<FlightCancellationJourney> item, final String departureTime) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.flight_cancellation_dialog_title));
        dialog.setDesc(
            MethodChecker.fromHtml(getString(R.string.flight_cancellation_dialog_refundable_description)));
        dialog.setBtnOk(getString(R.string.flight_cancellation_dialog_back_button_text));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setBtnCancel("Lanjut");
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkIfFlightCancellable(departureTime, invoiceId, item);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void goToCancellationPage(String invoiceId, List<FlightCancellationJourney> item) {
        startActivityForResult(FlightCancellationActivity.createIntent(getContext(),
                invoiceId,
                item
        ), REQUEST_CODE_CANCELLATION);
    }
}
