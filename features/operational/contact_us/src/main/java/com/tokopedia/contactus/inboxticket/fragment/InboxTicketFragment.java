package com.tokopedia.contactus.inboxticket.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.contactus.inboxticket.InboxTicketConstant;
import com.tokopedia.contactus.inboxticket.adapter.InboxTicketAdapter;
import com.tokopedia.contactus.inboxticket.listener.InboxTicketView;
import com.tokopedia.contactus.inboxticket.presenter.InboxTicketFragmentPresenter;
import com.tokopedia.contactus.inboxticket.presenter.InboxTicketFragmentPresenterImpl;

import butterknife.BindView;

/**
 * Created by Nisie on 4/21/16.
 */
public class InboxTicketFragment extends BasePresenterFragment<InboxTicketFragmentPresenter>
        implements InboxTicketView, InboxTicketConstant {

    @BindView(R2.id.message_list)
    RecyclerView listMessage;

    @BindView(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;

    @BindView(R2.id.fab)
    FloatingActionButton fab;

    View filterLayout;
    TextView confirmButton;
    TextView cancelButton;
    Spinner spinnerStatus;
    RadioButton radioAll;
    RadioButton radioUnread;

    InboxTicketAdapter adapter;
    RefreshHandler refreshHandler;
    BottomSheetDialog bottomSheetDialog;
    LinearLayoutManager layoutManager;

    public static InboxTicketFragment createInstance() {
        InboxTicketFragment fragment = new InboxTicketFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }



    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.inbox_ticket, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            goToHelp();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void initialPresenter() {
        presenter = new InboxTicketFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_inbox_ticket;
    }

    @Override
    protected void initView(View view) {
        filterLayout = getActivity().getLayoutInflater().inflate(R.layout.inbox_ticket_filter, null);
        confirmButton = (Button) filterLayout.findViewById(R.id.button_confirm);
        cancelButton = (Button) filterLayout.findViewById(R.id.button_cancel);
        spinnerStatus = (Spinner) filterLayout.findViewById(R.id.spinner_status);
        radioAll = (RadioButton) filterLayout.findViewById(R.id.radio_all);
        radioUnread = (RadioButton) filterLayout.findViewById(R.id.radio_unread);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(filterLayout);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog bsd = (BottomSheetDialog) dialog;
                FrameLayout frameLayout = (FrameLayout) bsd.findViewById(R.id.design_bottom_sheet);
                if (frameLayout != null) {
                    BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(frameLayout);
                    behavior.setHideable(false);
                }
            }
        });
        adapter = InboxTicketAdapter.createAdapter(this);
        refreshHandler = new RefreshHandler(getActivity(), swipeToRefresh, presenter.onRefresh());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listMessage.setLayoutManager(layoutManager);
        listMessage.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(getActivity(), R.array.ticket_status, android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapterStatus);
    }

    @Override
    protected void setViewListener() {
        fab.setOnClickListener(onFabClicked());
        listMessage.addOnScrollListener(onScrollListener());
        confirmButton.setOnClickListener(onConfirmFilterClicked());
        cancelButton.setOnClickListener(onCancelFilter());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshHandler.setRefreshing(true);
        presenter.setCache();
    }

    private RecyclerView.OnScrollListener onScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!presenter.isLoading() && adapter.getList().size() > 0 && layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                    presenter.loadMore();
                }
            }
        };
    }

    private View.OnClickListener onCancelFilter() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerStatus.setSelection(0);
                radioAll.setChecked(true);
                fab.show();
            }
        };
    }

    private View.OnClickListener onConfirmFilterClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                presenter.onConfirmFilterClicked();

            }
        };
    }

    private View.OnClickListener onFabClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.show();

            }
        };
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void goToHelp() {
        Intent intent = InboxRouter.getContactUsActivityIntent(getActivity());
        startActivity(intent);
    }

    @Override
    public InboxTicketAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void finishLoading() {
        setActionEnabled(true);
        refreshHandler.finishRefresh();
        adapter.showLoading(false);
        adapter.showEmptyFull(false);
    }

    @Override
    public void removeError() {
    }


    @Override
    public void setActionEnabled(boolean isEnabled) {
        if (isEnabled) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
        adapter.setActionEnabled(isEnabled);
    }

    @Override
    public String getFilter() {
        if (radioAll.isChecked())
            return STATUS_ALL;
        else {
            return STATUS_UNREAD;
        }
    }

    @Override
    public String getStatus() {
        return String.valueOf(spinnerStatus.getSelectedItemPosition());
    }

    @Override
    public void showSnackbar(String error) {
        if (error.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), error);

    }

    @Override
    public void showSnackbar(String error, View.OnClickListener listener) {
        setActionEnabled(false);
        if (error.equals("")) {
            SnackbarManager.make(getActivity(), getString(R.string.msg_network_error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.title_retry), listener)
                    .show();
        } else {
            SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.title_retry), listener)
                    .show();
        }

    }

    @Override
    public void finishRefreshing() {
        refreshHandler.finishRefresh();
    }

    @Override
    public void showEmptyState(String error, NetworkErrorHelper.RetryClickedListener listener) {
        setActionEnabled(false);
        if (error.equals(""))
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), listener);
        else
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, listener);
    }

    @Override
    public void showRefreshLoading() {
        if(!refreshHandler.isRefreshing()) {
            refreshHandler.setIsRefreshing(true);
            refreshHandler.setRefreshing(true);
        }
    }

    @Override
    public void showLoadingBottom() {
        if (adapter.getList().size() == 0) {
            adapter.showLoadingFull(true);
        } else {
            adapter.showLoading(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null
                && adapter != null
                && adapter.getList().size() > 0
                && requestCode == START_INBOX_TICKET_DETAIL
                && resultCode == Activity.RESULT_OK) {
            adapter.setIsRead(data.getExtras().getInt(POSITION_BUNDLE));
        }
    }
}
