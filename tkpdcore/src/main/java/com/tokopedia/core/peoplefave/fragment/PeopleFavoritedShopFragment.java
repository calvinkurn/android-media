package com.tokopedia.core.peoplefave.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.app.BasePresenterFragmentV4;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.peoplefave.customadapter.PeopleFavoritedShopAdapter;
import com.tokopedia.core.peoplefave.listener.PeopleFavoritedShopFragmentView;
import com.tokopedia.core.peoplefave.model.PeopleFavoritedShopData;
import com.tokopedia.core.peoplefave.presenter.PeopleFavoritedShopFragmentPresenter;
import com.tokopedia.core.peoplefave.presenter.PeopleFavoritedShopFramentImpl;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by hangnadi on 10/11/16.
 */

public class PeopleFavoritedShopFragment extends BasePresenterFragmentV4<PeopleFavoritedShopFragmentPresenter>
    implements PeopleFavoritedShopFragmentView {

    private static final String TAG = PeopleFavoritedShopFragmentView.class.getSimpleName();

    private static final String ARGS_PARAM_KEY_USER_ID = "param_key_user_id";
    private static final String ARGS_PARAM_KEY_STORED_DATA = "param_key_stored_data";
    private String userID;

    @BindView(R2.id.listview_favoritee)
    RecyclerView recyclerView;

    private boolean connectionStatus;
    private LinearLayoutManager layoutManager;
    private PeopleFavoritedShopAdapter adapter;
    private PeopleFavoritedShopData data;
    private SessionHandler sessionHandler;
    private RefreshHandler refreshHandler;
    private TkpdProgressDialog progressDialog;

    public PeopleFavoritedShopFragment() {
    }

    public static Fragment createInstance(String userID) {
        PeopleFavoritedShopFragment fragment = new PeopleFavoritedShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_PARAM_KEY_USER_ID, userID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initialPresenter() {
        presenter = new PeopleFavoritedShopFramentImpl(this);
    }

    @Override
    protected void setViewListener() {
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                presenter.getNextPage(getActivity(), lastItemPosition, visibleItem);
            }
        });
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        userID = arguments.getString(ARGS_PARAM_KEY_USER_ID);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_people_favorited_shop;
    }

    @Override
    public boolean isOnConnection() {
        return connectionStatus;
    }

    @Override
    public void setConnectionStatus(boolean connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    @Override
    public PeopleFavoritedShopData getData() {
        return data;
    }

    @Override
    public String getUserID() {
        return userID;
    }

    @Override
    protected void initView(View view) {
        adapter = PeopleFavoritedShopAdapter.createAdapter();
        adapter.setViewListener(this);
        this.layoutManager = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initialVar() {
        refreshHandler =
                new RefreshHandler(getActivity(), getView(), new RefreshHandler.OnRefreshHandlerListener() {
                    @Override
                    public void onRefresh(View view) {
                        presenter.setOnRefreshing(getActivity());
                    }
                });
        sessionHandler = new SessionHandler(getActivity());
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(ARGS_PARAM_KEY_USER_ID, userID);
        state.putParcelable(ARGS_PARAM_KEY_STORED_DATA, data);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        userID = savedState.getString(ARGS_PARAM_KEY_USER_ID);
        data = savedState.getParcelable(ARGS_PARAM_KEY_STORED_DATA);
        renderData(data);
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.setOnFirstTimeLaunch(getActivity());
    }

    @Override
    public void renderData(PeopleFavoritedShopData data) {
        this.data = data;
        this.adapter.setList(data.getList());
    }

    @Override
    public boolean isOwner() {
        return sessionHandler.getLoginID() != null && sessionHandler.getLoginID().equals(userID);
    }

    @Override
    public void openShopPage(String shopID){
        Intent intent = ((TkpdCoreRouter) getActivity().getApplication()).getShopPageIntent(getActivity(), shopID);
        startActivity(intent);
    }

    @Override
    public void onActionUnfavoriteClick(final PeopleFavoritedShopData.ShopFavorited shopFavorited) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.dialog_delete_fav_shop))
                .setCancelable(true)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                        presenter.postUnfavoriteClick(getActivity(), shopFavorited.getShopId());
                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
    }

    @Override
    public void setPullToRefresh(boolean able) {
        refreshHandler.setPullEnabled(able);
    }

    @Override
    public void showEmptyState(NetworkErrorHelper.RetryClickedListener listener) {
        Log.e(TAG, String.valueOf(1));
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), listener);
    }

    @Override
    public void showEmptyState(String message) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, null);
    }

    @Override
    public void showEmptyState() {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), null);
    }

    @Override
    public void showSnackBar() {
        NetworkErrorHelper.showSnackbar(getActivity());
    }

    @Override
    public void showSnackBar(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        this.refreshHandler.setRefreshing(refreshing);
    }

    @Override
    public PeopleFavoritedShopAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onDestroyView() {
        presenter.setOnDestroyView(getActivity());
        super.onDestroyView();
    }

    @Override
    public void showLoadingDialog() {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        progressDialog.showDialog();
    }

    @Override
    public void dismissLoadingDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void resetData() {
        adapter.setList(new ArrayList<PeopleFavoritedShopData.ShopFavorited>());
    }
}
