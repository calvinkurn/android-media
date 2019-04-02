package com.tokopedia.core.manage.people.address.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customView.EndLessScrollBehavior;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.activity.AddAddressActivity;
import com.tokopedia.core.manage.people.address.adapter.ManagePeopleAddressAdapter;
import com.tokopedia.core.manage.people.address.listener.MPAddressActivityListener;
import com.tokopedia.core.manage.people.address.listener.MPAddressFragmentListener;
import com.tokopedia.core.manage.people.address.model.AddressModel;
import com.tokopedia.core.manage.people.address.model.Token;
import com.tokopedia.core.manage.people.address.presenter.ManagePeopleAddressFragmentImpl;
import com.tokopedia.core.manage.people.address.presenter.ManagePeopleAddressFragmentPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RefreshHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A placeholder fragment containing a simple view.
 */
public class ManagePeopleAddressFragment extends BasePresenterFragment<ManagePeopleAddressFragmentPresenter>
        implements MPAddressFragmentListener {

    private static final String EXTRA_PARAM_ARRAY_LIST = "EXTRA_PARAM_ARRAY_LIST";

    @BindView(R2.id.recycler_view)
    RecyclerView recyclerView;

    private String querySearch;
    private int sortID;

    private View rootView;
    private LinearLayoutManager layoutManager;

    private ArrayList<AddressModel> list;
    private MPAddressActivityListener listener;
    private ManagePeopleAddressAdapter adapter;
    private RefreshHandler refreshHandler;

    private Token token;

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
        this.adapter = new ManagePeopleAddressAdapter(this.list, this.presenter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.setActionOnActivityKilled(getActivity());
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.setActionOnLaunchFirstTime(getActivity());
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelableArrayList(EXTRA_PARAM_ARRAY_LIST, list);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        if (list != null) {
            final ArrayList<AddressModel> arrayListTemp = savedState.getParcelableArrayList(EXTRA_PARAM_ARRAY_LIST);
            if (list.isEmpty() && arrayListTemp != null) {
                addAddressItemList(arrayListTemp);
            }
        }
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ManagePeopleAddressFragmentImpl(this, listener);
    }

    @Override
    protected void initialListener(Activity activity) {
        this.listener = (MPAddressActivityListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_manage_people_address;
    }

    @Override
    protected void initView(View view) {
        this.setRootView(view);
        this.prepareRecyclerView();
    }

    @Override
    public void prepareRecyclerView() {
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void setViewListener() {
        this.recyclerView.addOnScrollListener(new EndLessScrollBehavior(layoutManager) {
            @Override
            protected void setOnLoadMore() {
                presenter.setActionOnLazyLoad(getActivity());
            }
        });
    }

    @Override
    protected void initialVar() {
        this.refreshHandler =
                new RefreshHandler(getActivity(), getRootView(), new RefreshHandler.OnRefreshHandlerListener() {
                    @Override
                    public void onRefresh(View view) {
                        presenter.setActionOnRefreshing(getActivity());
                    }
                });
    }

    @Override
    protected void setActionVar() {

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
        this.adapter.showLoading(isAble);
    }

    @Override
    public void setNoResultView(boolean isAble) {
        this.adapter.showEmpty(isAble);
    }

    @Override
    public void showCache(List<AddressModel> list) {
        this.clearCurrentList();
        this.list.addAll(list);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void clearCurrentList() {
        this.list.clear();
    }

    @Override
    public void replaceCache(List<AddressModel> list) {
        this.clearCurrentList();
        this.list.addAll(list);
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

        this.list.addAll(newData);
        this.adapter.notifyItemRangeInserted(positionStart, itemCount);
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
            startActivityForResult(AddAddressActivity.createInstance(getActivity(), this.token),
                    ManageAddressConstant.REQUEST_CODE_PARAM_CREATE);
        } else {
            startActivityForResult(AddAddressActivity.createInstance(getActivity(), data, this.token),
                    ManageAddressConstant.REQUEST_CODE_PARAM_EDIT);
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
        } else if(resultCode == ManageAddressConstant.RESULT_ERROR){
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
