package com.tokopedia.core.manage.shop.notes.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.manage.shop.notes.adapter.ShopNotesAdapter;
import com.tokopedia.core.manage.shop.notes.listener.ManageShopNotesView;
import com.tokopedia.core.manage.shop.notes.model.ShopNote;
import com.tokopedia.core.manage.shop.notes.model.ShopNotesResult;
import com.tokopedia.core.manage.shop.notes.presenter.ManageShopNotesPresenter;
import com.tokopedia.core.manage.shop.notes.presenter.ManageShopNotesPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.shopinfo.ShopNotesDetail;
import com.tokopedia.core.util.RefreshHandler;

import butterknife.BindView;

/**
 * Created by nisie on 10/26/16.
 */

public class ManageShopNotesFragment extends BasePresenterFragment<ManageShopNotesPresenter>
        implements ManageShopNotesView {

    @BindView(R2.id.shop_notes)
    RecyclerView shopNotes;

    @BindView(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;

    ShopNotesAdapter adapter;
    RefreshHandler refreshHandler;
    TkpdProgressDialog progressDialog;
    OnActionShopNoteListener listener;

    public interface OnActionShopNoteListener {
        void onAddShopNote(boolean isReturnablePolicy, ShopNote isEdit);
    }

    public static ManageShopNotesFragment createInstance(Bundle extras) {
        ManageShopNotesFragment fragment = new ManageShopNotesFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        shopNotes.setVisibility(View.GONE);
        presenter.initData();
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.manage_people_notes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_add_notes){
            presenter.addNote();
            return true;
        }else if (item.getItemId() == R.id.action_add_returnable_policy){
            presenter.addReturnablePolicy();
            return true;
        }else{
            return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void initialPresenter() {
        presenter = new ManageShopNotesPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_manage_shop_notes;
    }

    @Override
    protected void initView(View view) {
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        adapter = ShopNotesAdapter.createInstance(getActivity(), new ShopNotesAdapter.ActionShopNotesListener() {
            @Override
            public void onDeleteNote(ShopNote shopNote) {
                onAskDeleteNote(shopNote);
            }

            @Override
            public void onEditNote(ShopNote shopNote) {
                if (shopNote.getNoteStatus().equals(ShopNotesAdapter.RETURN_POLICY))
                    listener.onAddShopNote(true, shopNote);
                else
                    listener.onAddShopNote(false, shopNote);
            }

            @Override
            public void onGoToDetail(ShopNote shopNote) {
                startActivity(ShopNotesDetail.createIntent(getActivity(), shopNote));
            }
        });
        shopNotes.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        shopNotes.setAdapter(adapter);

        refreshHandler = new RefreshHandler(getActivity(), swipeToRefresh, new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.onRefresh();
            }
        });
    }

    private void onAskDeleteNote(final ShopNote shopNote) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(
                getString(R.string.title_delete_notes) + " "
                        + shopNote.getNoteTitle() + " ?")
                .setCancelable(true)
                .setPositiveButton(getString(R.string.title_yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                presenter.onDeleteNote(shopNote);
                            }
                        })
                .setNegativeButton(getString(R.string.title_no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showLoading() {
        adapter.showLoadingFull(true);
    }

    @Override
    public void finishLoading() {
        adapter.showLoadingFull(false);
        refreshHandler.finishRefresh();
    }

    @Override
    public void setResult(ShopNotesResult data) {
        shopNotes.setVisibility(View.VISIBLE);
        adapter.getList().clear();
        adapter.addList(data.getList());
    }

    @Override
    public void showError(String error) {
        if (error.equals(""))
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.initData();
                }
            });
        else
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    presenter.initData();
                }
            });
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog != null)
            progressDialog.showDialog();
    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onSuccessDeleteNote(int position) {
        showSnackBar(getString(R.string.success_delete_shop_note));
        adapter.getList().remove(position);
        adapter.notifyDataSetChanged();
    }

    private void showSnackBar(String message) {
        SnackbarManager.make(getActivity(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorSnackbar(String message) {
        if (message.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), message);
    }

    @Override
    public ShopNotesAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setOnActionShopNoteListener(OnActionShopNoteListener listener) {
        this.listener = listener;
    }

    @Override
    public OnActionShopNoteListener getActionListener() {
        return listener;
    }

    @Override
    public void refresh() {
        presenter.onRefresh();
    }

    @Override
    public void showRefreshing() {
        refreshHandler.setRefreshing(true);
        refreshHandler.setIsRefreshing(true);
    }

    @Override
    public void setViewEnabled(boolean isEnabled) {
        setHasOptionsMenu(isEnabled);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }
}
