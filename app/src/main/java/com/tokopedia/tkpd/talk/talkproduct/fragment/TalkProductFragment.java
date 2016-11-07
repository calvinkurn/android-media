package com.tokopedia.tkpd.talk.talkproduct.fragment;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.talk.talkproduct.activity.TalkAddNew;
import com.tokopedia.tkpd.app.BasePresenterFragment;
import com.tokopedia.tkpd.customwidget.SwipeToRefresh;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.network.SnackbarRetry;
import com.tokopedia.tkpd.talk.talkproduct.activity.TalkProductActivity;
import com.tokopedia.tkpd.talk.talkproduct.adapter.TalkProductAdapter;
import com.tokopedia.tkpd.talk.talkproduct.listener.TalkProductView;
import com.tokopedia.tkpd.talk.talkproduct.model.Talk;
import com.tokopedia.tkpd.talk.talkproduct.model.TalkProductModel;
import com.tokopedia.tkpd.talk.talkproduct.presenter.TalkProductPresenter;
import com.tokopedia.tkpd.talk.talkproduct.presenter.TalkProductPresenterImpl;
import com.tokopedia.tkpd.util.RefreshHandler;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.var.RecyclerViewItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by stevenfredian on 4/5/16.
 */
public class TalkProductFragment extends BasePresenterFragment<TalkProductPresenter>
        implements TalkProductView, TalkProductView.addTemporary {
    public final static int ADD_TALK = 0;
    public final static int GO_TO_DETAIL = 2;
    public final static int RESULT_ADD = 3;
    public final static int RESULT_DELETE = 4;
    public final static int RESULT_ADD_COMMENT = 5;
    public final static int RESULT_READ = 6;
    private String shopID;
    private String isShopOwner = "0";
    private String productID;
    private String productName;
    private String productImage;
    public boolean stateHasAdded = false;
    Bundle bundle;
    int paramMaster = 0;

    List<RecyclerViewItem> items;
    RefreshHandler refresh;
    private boolean isRequest;
    TalkProductAdapter adapter;
    GridLayoutManager layoutManager;

    @Bind(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;
    @Bind(R2.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R2.id.include_loading)
    ProgressBar progressBar;
    SnackbarRetry snackbarRetry;


    public static TalkProductFragment createInstance(Bundle bundle) {
        TalkProductFragment fragment = new TalkProductFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        displayLoading(true);
        firstRequest();
    }

    @Override
    public void onSaveState(Bundle state) {
        presenter.saveState(state, items, layoutManager.findLastCompletelyVisibleItemPosition());
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        presenter.restoreState(savedState);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (SessionHandler.isV4Login(getActivity()) && !isShopOwner.equals("1"))
            inflater.inflate(R.menu.talk_product, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;

            case R.id.action_talk_add:
                ShowDialogAddNew();
                return true;
        }
        return false;
    }

    @Override
    protected void initialPresenter() {
        this.presenter = new TalkProductPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        productID = arguments.getString("product_id");
        shopID = arguments.getString("shop_id");
        isShopOwner = arguments.getString("is_owner");
        productName = arguments.getString("prod_name");
        productImage = arguments.getString("product_image");
        bundle = arguments;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_talk_product;
    }

    @Override
    protected void initView(View view) {
        layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        refresh = new RefreshHandler(getActivity(), view, onRefreshListener());
        displayLoading(true);
    }


    private RefreshHandler.OnRefreshHandlerListener onRefreshListener() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                if (!isRequest && !isSnackbarVisible(snackbarRetry)) {
                    doRefresh();
                }
            }
        };
    }


    @Override
    protected void setViewListener() {
        recyclerView.addOnScrollListener(OnScrollRecyclerView());
    }

    @Override
    protected void initialVar() {
//        pagingHandler = new PagingHandler();
        items = new ArrayList<>();
        adapter = TalkProductAdapter.createAdapter(getActivity(), this, items, false, false, bundle,presenter);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void setActionVar() {
    }


    public void doRefresh() {
        if (!refresh.isRefreshing()) {
            refresh.setRefreshing(true);
        }
        isRequest = true;
        removeLoadingFooter();
        firstRequest();
    }

    private void request() {
        isRequest = true;
        refresh.setPullEnabled(false);
        presenter.getTalkProduct(getActivity(), getParam());
    }

    private void firstRequest() {
        isRequest = true;
        refresh.setPullEnabled(false);
        presenter.refreshTalkProduct(getActivity(), getParam());
    }

    @Override
    public void cancelRequest(){
        isRequest = false;
        refresh.setPullEnabled(true);
    }


    private Map<String, String> getParam() {
        Map<String, String> param = new HashMap<>();
//        param.put("page", String.valueOf(pagingHandler.getPage()));
        param.put("per_page", "10");
        param.put("product_id", productID);
        param.put("shop_domain", shopID);
        if(paramMaster==1) param.put("master", String.valueOf(paramMaster));
        return param;
    }


    private RecyclerView.OnScrollListener OnScrollRecyclerView() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLastPosition() && !isRequest && !refresh.isRefreshing() && !isSnackbarVisible(snackbarRetry)) {
                    request();
                }
            }
        };
    }

    private boolean isSnackbarVisible(SnackbarRetry snackbarRetry) {
        return snackbarRetry != null && snackbarRetry.isShown();
    }

    @Override
    public void onSuccess() {
        doRefresh();
    }

    public void onErrorAction(Bundle resultData, int resultCode) {
        adapter.onErrorAction(resultData, resultCode);
    }

    public void onSuccessAction(Bundle resultData, int resultCode) {
        adapter.onSuccessAction(resultData, resultCode, presenter.getPositionAction());
    }


    @Override
    public void onSuccessConnection(TalkProductModel parse, int page) {
        isRequest = false;
        refresh.setPullEnabled(true);
        if (page == 1) {
            refresh.finishRefresh();
            items.clear();
            displayLoading(false);
        }
        items.addAll(parse.getTalk());
        adapter.notifyDataSetChanged();
        adapter.setEnableAction(true);
        displayLoading(false);
    }

    @Override
    public void onTimeoutConnection(int page) {
        onTimeoutConnection("",page);
    }

    @Override
    public void onTimeoutConnection(String error, int page) {
        isRequest = false;
        if (page == 1) {
            refresh.finishRefresh();
            if (items.size() > 0) {
                refresh.setPullEnabled(true);
                removeLoadingFooter();
                adapter.notifyDataSetChanged();
                NetworkErrorHelper.showSnackbar(getActivity());
            } else {
                displayView(false);
                if (error.length() <= 0) {
                    NetworkErrorHelper.showEmptyState(getActivity(), getView(), retryListener());
                } else {
                    NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, retryListener());
                }
            }
        } else {
            refresh.setPullEnabled(false);
            removeLoadingFooter();
            snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), retrySnackbarListener());
            adapter.setEnableAction(false);
            adapter.notifyDataSetChanged();
            snackbarRetry.showRetrySnackbar();
        }
    }

    private NetworkErrorHelper.RetryClickedListener refreshSnackbarListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                if (!isRequest) {
                    doRefresh();
                }
            }
        };
    }


    private NetworkErrorHelper.RetryClickedListener retrySnackbarListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                request();
                setLoadingFooter();
                adapter.notifyDataSetChanged();
            }
        };
    }

    private NetworkErrorHelper.RetryClickedListener retryListener() {
        return new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                displayLoading(true);
                firstRequest();
            }
        };
    }

    @Override
    public void showError(String s) {
        SnackbarManager.make(getActivity(), s, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onStateResponse(List list, int position, int page) {
        isRequest = false;
        refresh.setPullEnabled(true);
        items.addAll(list);
        adapter.notifyDataSetChanged();
        displayLoading(false);
    }

    public void displayLoading(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void displayView(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void setLoadingFooter() {
        adapter.setIsLoading(true);
    }

    public void removeLoadingFooter() {
        adapter.setIsLoading(false);
    }

    private boolean isLastPosition() {
        int lastIndex = layoutManager.findLastCompletelyVisibleItemPosition();
        int size = layoutManager.getItemCount() - 1;
        return lastIndex == size;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    public void ShowDialogAddNew() {
        Intent intent = new Intent(getActivity(),TalkAddNew.class);
        intent.putExtra("prod_id",productID);
        intent.putExtra("prod_name",productName);
        startActivityForResult(intent,ADD_TALK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_TALK:
                if (resultCode == RESULT_ADD) {
                    refreshFromDB();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(TalkProductActivity.RESULT_TALK_HAS_ADDED, true);
                    SnackbarManager.make(getActivity(), getString(R.string.message_add_new_talk),
                            Snackbar.LENGTH_LONG).show();
                    getActivity().setResult(Activity.RESULT_OK, new Intent().putExtras(bundle));
                }
                break;
            case GO_TO_DETAIL:
                if (resultCode == RESULT_DELETE) {
                    int position = data.getExtras().getInt("position");
                    items.remove(position);
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
                    ((Talk) items.get(position)).setTalkTotalComment(String.valueOf(size));
                    ((Talk) items.get(position)).setTalkFollowStatus(followStatus);
                    ((Talk) items.get(position)).setTalkReadStatus(readStatus);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void refreshFromDB() {
        setParamMaster();
        doRefresh();
        resetParamMaster();
    }

    private void resetParamMaster() {
        paramMaster = 0;
    }

    private void setParamMaster() {
        paramMaster = 1;
    }
}
