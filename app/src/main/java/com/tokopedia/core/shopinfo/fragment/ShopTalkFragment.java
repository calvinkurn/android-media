package com.tokopedia.core.shopinfo.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.shopinfo.adapter.ShopTalkAdapter;
import com.tokopedia.core.shopinfo.listener.ShopTalkFragmentView;
import com.tokopedia.core.shopinfo.models.talkmodel.ShopTalk;
import com.tokopedia.core.shopinfo.models.talkmodel.ShopTalkResult;
import com.tokopedia.core.shopinfo.presenter.ShopTalkPresenter;
import com.tokopedia.core.shopinfo.presenter.ShopTalkPresenterImpl;
import com.tokopedia.core.talk.inboxtalk.fragment.InboxTalkFragment;
import com.tokopedia.core.talkview.activity.TalkViewActivity;

import butterknife.Bind;

/**
 * Created by nisie on 11/18/16.
 */

public class ShopTalkFragment extends BasePresenterFragment<ShopTalkPresenter>
        implements ShopTalkFragmentView {

    private static final String PARAM_FROM = "from";
    private static final String PARAM_MODEL = "talk";
    private static final String PARAM_POSITION = "position";

    public static Fragment createInstance() {
        return new ShopTalkFragment();
    }

    @Bind(R2.id.list)
    RecyclerView list;

    ShopTalkAdapter adapter;
    TkpdProgressDialog progressDialog;
    LinearLayoutManager layoutManager;

    @Override
    protected boolean isRetainInstance() {
        return true;
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
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ShopTalkPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    protected void initView(View view) {
        adapter = ShopTalkAdapter.createInstance(getActivity(), getShopTalkListener());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);

        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
    }

    @Override
    protected void setViewListener() {
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                if (lastItemPosition == visibleItem)
                    presenter.loadMore();
            }
        });
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    public ShopTalkAdapter.ActionShopTalkListener getShopTalkListener() {
        return new ShopTalkAdapter.ActionShopTalkListener() {
            @Override
            public void onDeleteTalk(ShopTalk shopTalk) {
                createDeleteDialog(shopTalk);
            }

            @Override
            public void onReportTalk(ShopTalk shopTalk) {
                createReportDialog(shopTalk);

            }

            @Override
            public void onFollowTalk(ShopTalk shopTalk) {
                presenter.onFollowTalk(shopTalk);

            }

            @Override
            public void onUnfollowTalk(ShopTalk shopTalk) {
                presenter.onUnfollowTalk(shopTalk);

            }

            @Override
            public void onGoToDetail(ShopTalk shopTalk) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getActivity(), TalkViewActivity.class);
                bundle.putString(PARAM_FROM, TalkViewActivity.SHOP_TALK);
                bundle.putParcelable(PARAM_MODEL, shopTalk);
                bundle.putInt(PARAM_POSITION, shopTalk.getPosition());
                intent.putExtras(bundle);

                getActivity().startActivityForResult(intent, InboxTalkFragment.GO_TO_DETAIL);
            }

            @Override
            public void onGoToProfile(ShopTalk shopTalk) {
                startActivity(
                        PeopleInfoNoDrawerActivity.createInstance(getActivity(), shopTalk.getTalkUserId())
                );
            }

        };
    }

    private void createDeleteDialog(final ShopTalk shopTalk) {
        new AlertDialog.Builder(context)
                .setTitle(getString(R.string.title_delete))
                .setMessage(getString(R.string.prompt_delete_talk))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.onDeleteTalk(shopTalk);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void createReportDialog(final ShopTalk shopTalk) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.prompt_dialog_report, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.reason);

        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton(getActivity().getString(R.string.action_report),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                .setNegativeButton(getActivity().getString(R.string.title_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (userInput.length() > 0) {
                            shopTalk.setReport(userInput.getText().toString());
                            presenter.onReportTalk(shopTalk);
                            alertDialog.dismiss();
                        } else
                            userInput.setError(getActivity().getString(R.string.error_field_required));
                    }
                });
            }
        });
        alertDialog.show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && adapter.getList().isEmpty() && !adapter.isEmpty() && !presenter.isRequesting()) {
            presenter.getShopTalk();
        }
    }

    @Override
    public void showLoading() {
        adapter.showLoading(true);
    }

    @Override
    public void setActionsEnabled(boolean isEnabled) {
        adapter.setActionsEnabled(isEnabled);
    }

    @Override
    public void finishLoading() {
        adapter.showLoading(false);
        progressDialog.dismiss();
    }

    @Override
    public void onGetShopTalk(ShopTalkResult result) {
        adapter.addList(result.getList());
        if (adapter.getList().isEmpty())
            adapter.showEmpty(true);
        else
            adapter.showEmpty(false);

    }

    @Override
    public void showError(String error, NetworkErrorHelper.RetryClickedListener retryClickedListener) {
        if (adapter.getList().isEmpty())
            showErrorEmpty(error, retryClickedListener);
        else
            showErrorSnackbar(error, retryClickedListener);

    }

    private void showErrorSnackbar(String error, NetworkErrorHelper.RetryClickedListener retryClickedListener) {
        if (error.equals(""))
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), retryClickedListener);
        else
            NetworkErrorHelper.createSnackbarWithAction(getActivity(), error, retryClickedListener);
    }

    private void showErrorEmpty(String error, NetworkErrorHelper.RetryClickedListener retryClickedListener) {
        if (error.equals(""))
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), retryClickedListener);
        else
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, retryClickedListener);
    }

    @Override
    public void showProgressDialog() {
        progressDialog.showDialog();
    }

    @Override
    public void showError(String error) {
        showErrorSnackbar(error);
    }

    private void showErrorSnackbar(String error) {
        if (error.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), error);
    }

    @Override
    public void onSuccessDeleteTalk(ShopTalk shopTalk) {
        SnackbarManager.make(getActivity(), getString(R.string.message_success_delete_talk), Snackbar.LENGTH_LONG).show();
        adapter.getList().remove(shopTalk.getPosition());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessReportTalk(ShopTalk shopTalk) {
        SnackbarManager.make(getActivity(), getString(R.string.toast_success_report), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessFollowTalk(ShopTalk shopTalk) {
        SnackbarManager.make(getActivity(), getString(R.string.message_success_follow), Snackbar.LENGTH_LONG).show();
        adapter.getList().get(shopTalk.getPosition()).setTalkFollowStatus((shopTalk.getTalkFollowStatus() + 1) % 2);
        adapter.notifyDataSetChanged();
    }


}
