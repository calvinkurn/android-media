package com.tokopedia.logisticaddaddress.features.manageaddress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.logisticaddaddress.R;
import com.tokopedia.logisticaddaddress.di.DaggerManageAddressComponent;
import com.tokopedia.logisticaddaddress.di.ManageAddressModule;
import com.tokopedia.logisticaddaddress.domain.AddressViewModelMapper;
import com.tokopedia.logisticaddaddress.adapter.EndLessScrollBehavior;
import com.tokopedia.logisticaddaddress.adapter.ManageAddressAdapter;
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressActivity;
import com.tokopedia.logisticaddaddress.di.AddressModule;
import com.tokopedia.logisticdata.data.entity.address.AddressModel;
import com.tokopedia.logisticdata.data.entity.address.Token;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.logisticaddaddress.AddressConstants.REQUEST_CODE_PARAM_CREATE;
import static com.tokopedia.logisticaddaddress.AddressConstants.REQUEST_CODE_PARAM_EDIT;
import static com.tokopedia.logisticaddaddress.AddressConstants.RESULT_ERROR;

/**
 * A placeholder fragment containing a simple view.
 */
public class ManagePeopleAddressFragment extends BaseDaggerFragment
        implements ManagePeopleAddressView {

    private static final String EXTRA_PARAM_ARRAY_LIST = "EXTRA_PARAM_ARRAY_LIST";

    RecyclerView recyclerView;

    private String querySearch;
    private int sortID;
    private boolean IS_EMPTY = true;

    private View rootView;
    private LinearLayoutManager layoutManager;

    private ArrayList<AddressModel> list;
    private MPAddressActivityListener listener;
    private RefreshHandler refreshHandler;

    private Token token;

    @Inject
    ManageAddressAdapter adapter;
    @Inject
    ManagePeopleAddressPresenter presenter;

    public static Fragment newInstance() {
        ManagePeopleAddressFragment fragment = new ManagePeopleAddressFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public ManagePeopleAddressFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.list = new ArrayList<>();
        this.listener = (ManagePeopleAddressActivity) getActivity();
        presenter.setView(this);
    }

    @Override
    protected void initInjector() {
        BaseAppComponent appComponent = ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent();
        DaggerManageAddressComponent.builder()
                .baseAppComponent(appComponent)
                .addressModule(new AddressModule())
                .manageAddressModule(new ManageAddressModule(getContext()))
                .build().inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.setActionOnLaunchFirstTime(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logistic_fragment_manage_people_address, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.setActionOnActivityKilled(getActivity());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(EXTRA_PARAM_ARRAY_LIST, list);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (list != null && savedInstanceState != null) {
            final ArrayList<AddressModel> arrayListTemp = savedInstanceState.getParcelableArrayList(EXTRA_PARAM_ARRAY_LIST);
            if (list.isEmpty() && arrayListTemp != null) {
                addAddressItemList(arrayListTemp);
            }
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
    }

    private void initView(View view) {
        this.setRootView(view);
        this.recyclerView = view.findViewById(R.id.recycler_view);
        this.prepareRecyclerView();
        this.refreshHandler =
                new RefreshHandler(getActivity(), getRootView(), new RefreshHandler.OnRefreshHandlerListener() {
                    @Override
                    public void onRefresh(View view) {
                        presenter.setActionOnRefreshing(getActivity());
                    }
                });
    }

    @Override
    public void prepareRecyclerView() {
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.addOnScrollListener(new EndLessScrollBehavior(layoutManager) {
            @Override
            protected void setOnLoadMore() {
                presenter.setActionOnLazyLoad(getActivity());
            }
        });
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public View getRootView() {
        return rootView;
    }

    @Override
    public String getQuerySearch() {
        if (querySearch == null) {
            return "";
        } else {
            return querySearch;
        }
    }

    @Override
    public void setQuerySearch(String querySearch) {
        this.querySearch = querySearch;
    }

    @Override
    public int getSortID() {
        if (sortID == 0) {
            return 1;
        } else {
            return sortID;
        }
    }

    @Override
    public void setSortID(int sortID) {
        this.sortID = sortID;
    }

    @Override
    public void setRefreshEnable(boolean isRefreshAble) {
        this.refreshHandler.setPullEnabled(isRefreshAble);
    }

    @Override
    public void setRefreshView(boolean isRefreshActive) {
        this.refreshHandler.setRefreshing(isRefreshActive);
    }

    @Override
    public void setFilterView(boolean isAble) {
        listener.setFilterViewVisibility(isAble);
    }

    @Override
    public void setLoadingView(boolean isAble) {
        if(isAble) this.adapter.showLoading();
        else this.adapter.hideLoading();
    }

    @Override
    public void setNoResultView(boolean isAble) {
        if(isAble) this.adapter.showEmptyState();
        else this.adapter.hideEmptyState();
        IS_EMPTY = isAble;
//        this.adapter.showEmptyState();
    }

    @Override
    public void showCache(List<AddressModel> list) {
        this.clearCurrentList();
        this.list.addAll(list);
        this.adapter.addElement(AddressViewModelMapper.convertToViewModel(list));
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void clearCurrentList() {
        this.list.clear();
        this.adapter.clearAllElements();
    }

    @Override
    public void replaceCache(List<AddressModel> list) {
        this.clearCurrentList();
        this.list.addAll(list);
        this.adapter.addElement(AddressViewModelMapper.convertToViewModel(list));
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void showRetryView(NetworkErrorHelper.RetryClickedListener clickedListener) {
        if (!list.isEmpty()) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), clickedListener);
        }
    }

    @Override
    public ArrayList<AddressModel> getList() {
        return list;
    }

    @Override
    public void showErrorMessageEmptyState(String message, NetworkErrorHelper.RetryClickedListener clickedListener) {
        if (message == null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), clickedListener);
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, clickedListener);
        }
    }

    @Override
    public void showErrorMessageSnackBar(String message) {
        if (message == null) {
            NetworkErrorHelper.showSnackbar(getActivity());
        } else {
            NetworkErrorHelper.showSnackbar(getActivity(), message);
        }
    }

    @Override
    public void addAddressItemList(List<AddressModel> newData) {
        int positionStart = this.list.size();
        int itemCount = newData.size();

        if(this.adapter.isLoading()) this.adapter.hideLoading();
        this.adapter.addElement(AddressViewModelMapper.convertToViewModel(newData));
        this.list.addAll(newData);
//        this.adapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    @Override
    public void setOnGetFilterActivated(int sortID, String query) {
        this.setSortID(sortID);
        this.setQuerySearch(query);
        presenter.setActionOnRefreshing(getActivity());
    }

    @Override
    public void showDialogConfirmation(String message, DialogInterface.OnClickListener onPositiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(MethodChecker.fromHtml(message))
                .setPositiveButton(R.string.title_yes, onPositiveClickListener)
                .setNegativeButton(R.string.title_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void setOnActionReceiveResult(int resultCode, Bundle resultData) {
        presenter.setOnActionReceiveResult(getActivity(), resultCode, resultData);
    }

    @Override
    public void showTimeOutMessage(String message, View.OnClickListener listener) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void openFormAddressView(AddressModel data) {
        if (data == null) {
            startActivityForResult(AddAddressActivity.createInstance(getActivity(), this.token, IS_EMPTY),
                    REQUEST_CODE_PARAM_CREATE);
        } else {
            startActivityForResult(AddAddressActivity.createInstance(getActivity(), data, this.token),
                    REQUEST_CODE_PARAM_EDIT);
        }
    }

    @Override
    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            NetworkErrorHelper.removeEmptyState(getView());
            presenter.setAllowConnection(true);
            presenter.setActionOnRefreshing(getActivity());
        } else if(resultCode == RESULT_ERROR){
            showErrorMessageSnackBar(data.getExtras().getString("message"));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.manage_people_address, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_address) {
            this.openFormAddressView(null);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
