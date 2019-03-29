package com.tokopedia.core.util.getproducturlutil;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tkpd.library.ui.view.CustomSearchView;
import com.tokopedia.core2.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.shopinfo.models.productmodel.ShopProduct;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.getproducturlutil.adapter.ProductAdapter;
import com.tokopedia.core.util.getproducturlutil.interactor.GetProductUrlRetrofitInteractor;
import com.tokopedia.core.util.getproducturlutil.interactor.GetProductUrlRetrofitInteractorImpl;
import com.tokopedia.core.util.getproducturlutil.model.GetProductPass;

import java.util.Map;

/**
 * Created by Tkpd_Eka on 4/9/2015.
 */
public class GetProductUrlUtil {

    private class ViewHolder {
        CustomSearchView search2;
        RecyclerView recyclerView;
        View openShop;
        View viewNoShop;
    }

    public interface OnGetUrlInterface {
        void onGetUrl(String url);
    }

    public static GetProductUrlUtil createInstance(Context context) {
        GetProductUrlUtil util = new GetProductUrlUtil(context);
        util.context = context;
        util.paging = new PagingHandler();
        return util;
    }

    public static GetProductUrlUtil createInstance(Context context, String shopId) {
        GetProductUrlUtil util = new GetProductUrlUtil(context, shopId);
        util.context = context;
        util.paging = new PagingHandler();
        return util;
    }

    public GetProductUrlUtil(Context context) {
        this.context = context;
        this.paging = new PagingHandler();
        this.shopId = SessionHandler.getShopID(MainApplication.getAppContext());
    }

    public GetProductUrlUtil(Context context, String shopId) {
        this.context = context;
        this.paging = new PagingHandler();
        this.shopId = shopId;
    }

    private PagingHandler paging;
    private ViewHolder holder;
    private Context context;
    private Dialog dialog;
    private GetProductUrlRetrofitInteractor networkInteractor;
    private LinearLayoutManager linearLayoutManager;
    private ProductAdapter adapter;
    private boolean hasShop;
    private GetProductPass pass;
    private String shopId;


    public void getOwnShopProductUrl(OnGetUrlInterface listener) {
        initVar();
        setDialog();
        setListener(listener);

        dialog.show();
        checkHasShop();
        getProductList("");
    }

    private void initVar() {
        paging = new PagingHandler();
        holder = new ViewHolder();
        dialog = new Dialog(context);
        networkInteractor = new GetProductUrlRetrofitInteractorImpl();
        adapter = ProductAdapter.createInstance(context);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        pass = new GetProductPass();
        pass.setShopId(shopId);

    }

    private void setDialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(R.layout.dialog_get_url);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                networkInteractor.unSubscribeObservable();
            }
        });
        initDialogView();
    }

    private void initDialogView() {
        holder.recyclerView = (RecyclerView) dialog.findViewById(R.id.list);
        holder.search2 = (CustomSearchView) dialog.findViewById(R.id.search2);
        holder.viewNoShop = dialog.findViewById(R.id.view_no_shop);
        holder.openShop = dialog.findViewById(R.id.open_shop);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        holder.recyclerView.setAdapter(adapter);
    }

    private void setListener(final OnGetUrlInterface listener) {
        adapter.setOnGetUrlListener(new OnGetUrlInterface() {
            @Override
            public void onGetUrl(String url) {
                listener.onGetUrl(getUrlWithoutParameters(url));
                dialog.dismiss();
            }
        });
        adapter.setOnRetryListenerRV(onRetry());
        holder.search2.setOnQuerySendListener(OnSearch());
        holder.openShop.setOnClickListener(OnOpenShop());
        holder.recyclerView.addOnScrollListener(onScrollListener());
    }

    private String getUrlWithoutParameters(String url) {
        if (url.contains("?"))
            return url.substring(0, url.lastIndexOf('?'));
        else
            return url;

    }

    private RetryDataBinder.OnRetryListener onRetry() {
        return new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                getProductList(pass.getQuery());
            }
        };
    }

    private CustomSearchView.OnQuerySendListener OnSearch() {
        return new CustomSearchView.OnQuerySendListener() {
            @Override
            public void onQuerySend(String query) {
                clearData();
                getProductList(query);
            }
        };
    }

    private View.OnClickListener OnOpenShop() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = SellerRouter.getActivityShopCreateEdit(context);
                context.startActivity(intent);
            }
        };
    }

    public RecyclerView.OnScrollListener onScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                int visibleItem = linearLayoutManager.getItemCount() - 1;
                loadNextPage();
            }
        };
    }

    private void loadNextPage() {
        if (!networkInteractor.isRequesting() && paging.CheckNextPage()) {
            paging.nextPage();
            getProductList(holder.search2.getQuery());
        }
    }

    private void clearData() {
        adapter.getList().clear();
        adapter.notifyDataSetChanged();
        paging.resetPage();
    }

    private void getProductList(String query) {
        if (hasShop) {
            adapter.showLoading(true);
            adapter.showEmpty(false);
            adapter.showRetry(false);
            networkInteractor.getProductUrl(context, getParamProductUrl(query), new GetProductUrlRetrofitInteractor.GetProductUrlListener() {
                @Override
                public void onSuccess(ShopProduct shopProduct) {
                    paging.setHasNext(PagingHandler.CheckHasNext(shopProduct.getPaging()));
                    onConnectionFinish();
                    onGetProductSuccess(shopProduct);
                }

                @Override
                public void onTimeout() {
                    onConnectionFinish();
                    adapter.showRetry(true);
                }

                @Override
                public void onFailAuth() {
                    onConnectionFinish();
                    adapter.showRetry(true);
                }

                @Override
                public void onThrowable(Throwable e) {
                    onConnectionFinish();
                    adapter.showRetry(true);
                }

                @Override
                public void onError(String error) {
                    onConnectionFinish();
                    adapter.showRetry(true);
                }

                @Override
                public void onNullData() {
                    onConnectionFinish();
                    adapter.showEmpty(true);
                }

                @Override
                public void onNoConnection() {
                    onConnectionFinish();
                    adapter.showRetry(true);
                }
            });
        }
    }

    private Map<String, String> getParamProductUrl(String query) {
        pass.setStart(String.valueOf((paging.getPage() - 1) * Integer.parseInt(GetProductPass.DEFAULT_ROWS)));
        pass.setQuery(query);
        pass.setShopId(shopId);
        return pass.getProductUrlParam();
    }

    private void checkHasShop() {
        if (shopId.equals("0")) {
            setHasNoShop();
        } else {
            setHasShop();
        }
    }

    private void setHasShop() {
        hasShop = true;
        holder.viewNoShop.setVisibility(View.GONE);
        holder.search2.setVisibility(View.VISIBLE);
    }

    private void setHasNoShop() {
        hasShop = false;
        holder.viewNoShop.setVisibility(View.VISIBLE);
        holder.search2.setVisibility(View.GONE);
    }

    private void onConnectionFinish() {
        adapter.showLoading(false);
    }

    private void onGetProductSuccess(ShopProduct result) {
        adapter.addList(result.getProducts());
        if (adapter.getList().size() == 0) {
            adapter.showEmpty(true);
        }
    }

}