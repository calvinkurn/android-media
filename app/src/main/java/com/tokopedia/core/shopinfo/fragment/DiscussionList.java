package com.tokopedia.core.shopinfo.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.app.V2BaseFragment;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.shopinfo.adapter.DiscussionAdapter;
import com.tokopedia.core.shopinfo.facades.ActionShopTalkRetrofit;
import com.tokopedia.core.shopinfo.facades.GetShopTalkRetrofit;
import com.tokopedia.core.shopinfo.models.talkmodel.TalkModel;
import com.tokopedia.core.talk.inboxtalk.fragment.InboxTalkFragment;
import com.tokopedia.core.talk.talkproduct.activity.TalkProductActivity;
import com.tokopedia.core.talkview.activity.TalkViewActivity;

import java.util.ArrayList;

/**
 * Created by Tkpd_Eka on 10/8/2015.
 */
public class DiscussionList extends V2BaseFragment {

    class ViewHolder {
        RecyclerView list;
    }

    ViewHolder holder;
    DiscussionAdapter adapter;
    int page = 1;
    GetShopTalkRetrofit facadeGet;
    ActionShopTalkRetrofit facadeAction;
    TalkModel modelList;
    TkpdProgressDialog progress;
    private String shopId;
    private String shopDomain;

    public static DiscussionList create() {
        return new DiscussionList();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && modelList.list.isEmpty() && !adapter.hasNoResult() && page == 1 && !facadeGet.isFetching()) {
            adapter.setLoading();
            facadeGet.getShopTalk(Integer.toString(page));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getActivity().getIntent().getExtras().getString(ShopInfoActivity.SHOP_ID, "");
        shopDomain = getActivity().getIntent().getExtras().getString(ShopInfoActivity.SHOP_DOMAIN, "");
        modelList = new TalkModel();
        modelList.list = new ArrayList<>();
//        if(savedInstanceState!=null)
//            loadInstanceState(savedInstanceState);
        adapter = DiscussionAdapter.createAdapter(modelList, getActivity());
        progress = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
        initFacade();
    }

    private void loadInstanceState(Bundle savedInstanceState) {
//        page = savedInstanceState.getInt("page");
//        model = savedInstanceState.getParcelable("shop_info");
//        modelList = savedInstanceState.getParcelableArrayList("list");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putParcelable("shop_info", model);
//        outState.putParcelableArrayList("list", modelList);
//        outState.putInt("page", page);
    }

    private void initFacade() {
        facadeGet = new GetShopTalkRetrofit(getActivity(), shopId, shopDomain);
        facadeGet.setOnGetShopTalkListener(onGetTalkList());
        facadeAction = new ActionShopTalkRetrofit(getActivity(), shopId, shopDomain);
    }

    private GetShopTalkRetrofit.OnGetShopTalkListener onGetTalkList() {
        return new GetShopTalkRetrofit.OnGetShopTalkListener() {
            @Override
            public void onSuccess(TalkModel model) {
                onSuccessLoad(model);
            }

            @Override
            public void onFailure() {
                finishLoading();
                adapter.setRetry();
            }
        };
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    protected void onCreateView() {
        if (modelList.list.isEmpty() && page == 1)
            if(adapter.hasNoResult())
                adapter.setNoResult();
            else if(adapter.hasRetry())
                adapter.setRetry();
            else
                adapter.setLoading();
    }

    @Override
    protected Object getHolder() {
        return holder;
    }

    @Override
    protected void setHolder(Object holder) {
        this.holder = (ViewHolder) holder;
    }

    @Override
    protected void initView() {
        holder = new ViewHolder();
        holder.list = (RecyclerView) findViewById(R.id.list);
        holder.list.setAdapter(adapter);
        holder.list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void setListener() {
        holder.list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (canLoadNext() && isAtBottom())
                    loadNextPage();
            }
        });
        adapter.setAdapterListener(onDiscussionListAction());
        adapter.setRetryListener(onRetryClickListener());
    }

    private View.OnClickListener onRetryClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeRetry();
                loadNextPage();
            }
        };
    }

    private DiscussionAdapter.DiscussionAdapterListener onDiscussionListAction() {
        return new DiscussionAdapter.DiscussionAdapterListener() {
            @Override
            public void onOpenTalk(int pos) {
                openTalk(pos);
            }

            @Override
            public void onDeleteTalk(int pos) {
                progress.showDialog();
                facadeAction.setListener(OnDeleteConnection(pos));
                facadeAction.actionDeleteTalk(modelList.list.get(pos).talkId);
            }

            @Override
            public void onFollowTalk(int pos) {
                progress.showDialog();
                facadeAction.setListener(OnFollowConnection(pos));
                facadeAction.actionFollowTalk(modelList.list.get(pos).talkProductId, modelList.list.get(pos).talkId);
            }

            @Override
            public void onReportTalk(int pos) {
                createReportDialog(pos);
            }

            @Override
            public void onUnfollowTalk(int pos) {
                progress.showDialog();
                facadeAction.setListener(OnFollowConnection(pos));
                facadeAction.actionFollowTalk(modelList.list.get(pos).talkProductId, modelList.list.get(pos).talkId);
            }

            @Override
            public void onUserClick(int pos) {
                startActivity(
                        PeopleInfoNoDrawerActivity.createInstance(getActivity(), modelList.list.get(pos).talkUserId)
                );
            }

            @Override
            public void onProductClick(int pos) {
                openTalk(pos);
            }
        };
    }

    private ActionShopTalkRetrofit.OnActionShopListener OnDeleteConnection(final int pos) {
        return new ActionShopTalkRetrofit.OnActionShopListener() {
            @Override
            public void onSuccess() {
                progress.dismiss();
                modelList.list.remove(pos);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        };
    }

    private void openTalk(int pos) {
        com.tokopedia.core.shopinfo.models.talkmodel.List model = modelList.list.get(pos);
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getActivity(), TalkViewActivity.class);
        bundle.putString("from", TalkViewActivity.SHOP_TALK);
        bundle.putParcelable("talk", model);
        bundle.putInt("position", pos);
        intent.putExtras(bundle);

        this.startActivityForResult(intent, InboxTalkFragment.GO_TO_DETAIL);
    }

    private ActionShopTalkRetrofit.OnActionShopListener OnFollowConnection(final int pos) {
        return new  ActionShopTalkRetrofit.OnActionShopListener() {
            @Override
            public void onSuccess() {
                modelList.list.get(pos).talkFollowStatus = (modelList.list.get(pos).talkFollowStatus + 1) % 2;
                progress.dismiss();
            }

            @Override
            public void onFailure() {
                progress.dismiss();
            }
        };
    }

    private void createReportDialog(final int pos) {
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
                            onPostReportTalk(pos, userInput.getText().toString());
                            alertDialog.dismiss();
                        } else
                            userInput.setError(getActivity().getString(R.string.error_field_required));
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void onPostReportTalk(int pos, String message) {
        progress.showDialog();
        facadeAction.setListener(onReportTalkConnection());
        facadeAction.actionReportTalk(modelList.list.get(pos).talkProductId, modelList.list.get(pos).talkId, message);
    }

    private ActionShopTalkRetrofit.OnActionShopListener onReportTalkConnection() {
        return new ActionShopTalkRetrofit.OnActionShopListener() {
            @Override
            public void onSuccess() {
                progress.dismiss();
            }

            @Override
            public void onFailure() {
                progress.dismiss();
            }
        };
    }

    private boolean isAtBottom() {
        int visibleItem = holder.list.getLayoutManager().getChildCount();
        int totalItem = holder.list.getLayoutManager().getItemCount();
        int pastVisibleItem = ((LinearLayoutManager) holder.list.getLayoutManager()).findFirstVisibleItemPosition();
        return ((visibleItem + pastVisibleItem) >= totalItem);
    }

    private boolean canLoadNext() {
        return (!adapter.hasLoading() && page > 0);
    }

    private void loadNextPage() {
        addLoading();
        facadeGet.getShopTalk(Integer.toString(page));
    }

    private void addLoading() {
        adapter.setLoading();
    }

    private void finishLoading() {
        adapter.removeLoading();
    }

    private void onSuccessLoad(TalkModel models) {
        finishLoading();
        modelList.list.addAll(models.list);
        adapter.notifyDataSetChanged();
        checkNoResult();
        if (models.list.size() == 0)
            page = -1;
        else
            page++;
    }

    private void checkNoResult() {
        if (modelList.list.size() == 0)
            adapter.setNoResult();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case InboxTalkFragment.GO_TO_DETAIL:
                if (resultCode == InboxTalkFragment.RESULT_DELETE) {
                    int position = data.getExtras().getInt("position");
                    modelList.list.remove(position);
                    adapter.notifyDataSetChanged();
                    SnackbarManager.make(getActivity(),
                            getString(R.string.message_success_delete_talk),Snackbar.LENGTH_LONG).show();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(TalkProductActivity.RESULT_TALK_HAS_ADDED, true);
                    getActivity().setResult(Activity.RESULT_OK, new Intent().putExtras(bundle));
                } else if (resultCode == Activity.RESULT_OK) {
                    int position = data.getExtras().getInt("position");
                    int size = data.getExtras().getInt("total_comment");
                    int followStatus = data.getExtras().getInt("is_follow");
                    int readStatus = data.getExtras().getInt("read_status");
                    (modelList.list.get(position)).setTalkTotalComment(String.valueOf(size));
                    (modelList.list.get(position)).setTalkFollowStatus(followStatus);
                    (modelList.list.get(position)).setTalkReadStatus(readStatus);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
