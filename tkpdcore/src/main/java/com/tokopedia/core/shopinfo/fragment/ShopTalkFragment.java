package com.tokopedia.core.shopinfo.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.tokopedia.core.app.BasePresenterFragmentV4;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.shopinfo.adapter.ShopTalkAdapter;
import com.tokopedia.core.shopinfo.listener.ShopTalkFragmentView;
import com.tokopedia.core.shopinfo.models.talkmodel.ShopTalk;
import com.tokopedia.core.shopinfo.models.talkmodel.ShopTalkResult;
import com.tokopedia.core.shopinfo.presenter.ShopTalkPresenter;
import com.tokopedia.core.shopinfo.presenter.ShopTalkPresenterImpl;
import com.tokopedia.core.talkview.activity.TalkViewActivity;
import com.tokopedia.core.util.PagingHandler;

import butterknife.BindView;

import static com.tokopedia.core.talk.talkproduct.fragment.TalkProductFragment.RESULT_DELETE;

/**
 * Created by nisie on 11/18/16.
 */

public class ShopTalkFragment extends BasePresenterFragmentV4<ShopTalkPresenter>
        implements ShopTalkFragmentView {

    public final static int GO_TO_DETAIL = 2;
    private static final String PARAM_FROM = "from";
    private static final String PARAM_MODEL = "talk";
    private static final String PARAM_POSITION = "position";
    private boolean isViewShown;

    public static Fragment createInstance() {
        return new ShopTalkFragment();
    }

    @BindView(R2.id.list)
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

        if(!isViewShown) {
            fetchData();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void setViewListener() {
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                if (lastItemPosition == visibleItem && !presenter.isRequesting() && !adapter.getList().isEmpty())
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

                startActivityForResult(intent, GO_TO_DETAIL);
            }

            @Override
            public void onGoToProfile(ShopTalk shopTalk) {
                if (getActivity().getApplicationContext() instanceof TkpdCoreRouter) {
                    startActivity(((TkpdCoreRouter) getActivity().getApplicationContext())
                            .getTopProfileIntent(getActivity(),
                                    shopTalk.getTalkUserId()));
                }
            }
        };
    }

    private void createDeleteDialog(final ShopTalk shopTalk) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.title_delete_discussion))
                .setMessage(getString(R.string.prompt_delete_talk))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.onDeleteTalk(shopTalk);
                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
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
        isViewShown = getView() != null;
        if (isVisibleToUser) {
            if (getActivity() != null &&
                    getActivity() instanceof ShopInfoActivity) {
                ((ShopInfoActivity) getActivity()).swipeAble(true);
            }
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
        adapter.setHaveNext(PagingHandler.CheckHasNext(result.getPaging()));
        adapter.addList(result.getList());
        if (adapter.getList().isEmpty())
            adapter.showEmptyFull(true);
        else
            adapter.showEmptyFull(false);

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
        setActionsEnabled(true);
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

    @Override
    public void onSuccessUnfollowTalk(ShopTalk shopTalk) {
        SnackbarManager.make(getActivity(), getString(R.string.message_success_unfollow), Snackbar.LENGTH_LONG).show();
        adapter.getList().get(shopTalk.getPosition()).setTalkFollowStatus((shopTalk.getTalkFollowStatus() + 1) % 2);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null)
            adapter.getList().clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GO_TO_DETAIL:
                if (data == null) {
                    return;
                }

                if (resultCode == RESULT_DELETE) {
                    int position = data.getExtras().getInt("position");
                    if (!adapter.getList().isEmpty()) {
                        adapter.getList().remove(position);
                        adapter.notifyDataSetChanged();
                    }
                    SnackbarManager.make(getActivity(),
                            getString(R.string.message_success_delete_talk), Snackbar.LENGTH_LONG).show();
                } else if (resultCode == Activity.RESULT_OK) {
                    int position = data.getExtras().getInt("position");
                    int size = data.getExtras().getInt("total_comment");
                    int followStatus = data.getExtras().getInt("is_follow");
                    int readStatus = data.getExtras().getInt("read_status");
                    if (!adapter.getList().isEmpty()) {
                        (adapter.getList().get(position)).setTalkTotalComment(String.valueOf(size));
                        (adapter.getList().get(position)).setTalkFollowStatus(followStatus);
                        (adapter.getList().get(position)).setTalkReadStatus(readStatus);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            default:
                break;
        }

    }

    private void fetchData() {
        if (presenter != null
                && adapter != null
                && !adapter.isEmpty()
                && adapter.getList().isEmpty()
                && !presenter.isRequesting()) {
            presenter.getShopTalk();
        }
    }
}
