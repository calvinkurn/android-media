package com.tokopedia.core.shopinfo.presenter;

import com.tokopedia.core.R;
import com.tokopedia.core.database.model.PagingHandler;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.shopinfo.interactor.ShopTalkRetrofitInteractor;
import com.tokopedia.core.shopinfo.interactor.ShopTalkRetrofitInteractorImpl;
import com.tokopedia.core.shopinfo.listener.ShopTalkFragmentView;
import com.tokopedia.core.shopinfo.models.talkmodel.ShopTalk;
import com.tokopedia.core.shopinfo.models.talkmodel.ShopTalkResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nisie on 11/18/16.
 */
@Deprecated
public class ShopTalkPresenterImpl implements ShopTalkPresenter {

    public static String SHOP_ID = "shop_id";
    public static String SHOP_DOMAIN = "shop_domain";

    private final ShopTalkFragmentView viewListener;
    private ShopTalkRetrofitInteractor networkInteractor;
    private PagingHandler paging;

    public ShopTalkPresenterImpl(ShopTalkFragmentView viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new ShopTalkRetrofitInteractorImpl();
        this.paging = new PagingHandler();
    }


    @Override
    public void onDeleteTalk(final ShopTalk shopTalk) {
        viewListener.showProgressDialog();
        viewListener.setActionsEnabled(false);
        networkInteractor.deleteTalk(viewListener.getActivity(), getDeleteTalkParam(shopTalk), new ShopTalkRetrofitInteractor.ActionShopTalkListener() {
            @Override
            public void onSuccess() {
                viewListener.finishLoading();
                viewListener.setActionsEnabled(true);
                viewListener.onSuccessDeleteTalk(shopTalk);
            }

            @Override
            public void onError(String error) {
                viewListener.finishLoading();
                viewListener.showError(error);
            }

            @Override
            public void onNullData() {
                viewListener.finishLoading();
                viewListener.showError(viewListener.getActivity().getString(R.string.default_request_error_null_data));
            }

            @Override
            public void onNoNetworkConnection() {
                viewListener.finishLoading();
                viewListener.showError("");
            }

            @Override
            public void onTimeout() {
                viewListener.finishLoading();
                viewListener.showError(viewListener.getActivity().getString(R.string.default_request_error_timeout));
            }

            @Override
            public void onFailAuth() {
                viewListener.finishLoading();
                viewListener.showError(viewListener.getActivity().getString(R.string.default_request_error_forbidden_auth));
            }
        });
    }

    private Map<String, String> getDeleteTalkParam(ShopTalk shopTalk) {
        HashMap<String, String> param = new HashMap<>();
        param.put("shop_domain", viewListener.getActivity().getIntent().getExtras().getString(SHOP_DOMAIN, ""));
        param.put("shop_id", viewListener.getActivity().getIntent().getExtras().getString(SHOP_ID, ""));
        param.put("talk_id", shopTalk.getTalkId());
        return param;
    }

    @Override
    public void onReportTalk(final ShopTalk shopTalk) {
        viewListener.showProgressDialog();
        viewListener.setActionsEnabled(false);
        networkInteractor.reportTalk(viewListener.getActivity(), getReportTalkParam(shopTalk),
                new ShopTalkRetrofitInteractor.ActionShopTalkListener() {
                    @Override
                    public void onSuccess() {
                        viewListener.finishLoading();
                        viewListener.setActionsEnabled(true);
                        viewListener.onSuccessReportTalk(shopTalk);
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.finishLoading();
                        viewListener.showError(error);
                    }

                    @Override
                    public void onNullData() {
                        viewListener.finishLoading();
                        viewListener.showError(viewListener.getActivity().getString(R.string.default_request_error_null_data));
                    }

                    @Override
                    public void onNoNetworkConnection() {
                        viewListener.finishLoading();
                        viewListener.showError("");
                    }

                    @Override
                    public void onTimeout() {
                        viewListener.finishLoading();
                        viewListener.showError(viewListener.getActivity().getString(R.string.default_request_error_timeout));
                    }

                    @Override
                    public void onFailAuth() {
                        viewListener.finishLoading();
                        viewListener.showError(viewListener.getActivity().getString(R.string.default_request_error_forbidden_auth));
                    }
                });
    }

    private Map<String, String> getReportTalkParam(ShopTalk shopTalk) {
        HashMap<String, String> param = new HashMap<>();
        param.put("shop_domain", viewListener.getActivity().getIntent().getExtras().getString(SHOP_DOMAIN, ""));
        param.put("shop_id", viewListener.getActivity().getIntent().getExtras().getString(SHOP_ID, ""));
        param.put("product_id", String.valueOf(shopTalk.getTalkProductId()));
        param.put("talk_id", shopTalk.getTalkId());
        param.put("text_message", shopTalk.getReport());
        return param;
    }

    @Override
    public void onFollowTalk(final ShopTalk shopTalk) {
        viewListener.showProgressDialog();
        viewListener.setActionsEnabled(false);
        networkInteractor.followTalk(viewListener.getActivity(), getFollowTalkParam(shopTalk),
                new ShopTalkRetrofitInteractor.ActionShopTalkListener() {
                    @Override
                    public void onSuccess() {
                        viewListener.finishLoading();
                        viewListener.setActionsEnabled(true);
                        if (shopTalk.getTalkFollowStatus() == ShopTalk.IS_FOLLOWING)
                            viewListener.onSuccessUnfollowTalk(shopTalk);
                        else
                            viewListener.onSuccessFollowTalk(shopTalk);
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.finishLoading();
                        viewListener.showError(error);
                    }

                    @Override
                    public void onNullData() {
                        viewListener.finishLoading();
                        viewListener.showError(viewListener.getActivity().getString(R.string.default_request_error_null_data));
                    }

                    @Override
                    public void onNoNetworkConnection() {
                        viewListener.finishLoading();
                        viewListener.showError("");
                    }

                    @Override
                    public void onTimeout() {
                        viewListener.finishLoading();
                        viewListener.showError(viewListener.getActivity().getString(R.string.default_request_error_timeout));
                    }

                    @Override
                    public void onFailAuth() {
                        viewListener.finishLoading();
                        viewListener.showError(viewListener.getActivity().getString(R.string.default_request_error_forbidden_auth));
                    }
                });
    }

    private Map<String, String> getFollowTalkParam(ShopTalk shopTalk) {
        HashMap<String, String> param = new HashMap<>();
        param.put("shop_domain", viewListener.getActivity().getIntent().getExtras().getString(SHOP_DOMAIN, ""));
        param.put("shop_id", viewListener.getActivity().getIntent().getExtras().getString(SHOP_ID, ""));
        param.put("product_id", String.valueOf(shopTalk.getTalkProductId()));
        param.put("talk_id", shopTalk.getTalkId());
        return param;
    }

    @Override
    public void onUnfollowTalk(final ShopTalk shopTalk) {
        onFollowTalk(shopTalk);
    }

    @Override
    public boolean isRequesting() {
        return networkInteractor.isRequesting();
    }

    @Override
    public void getShopTalk() {
        viewListener.showLoading();
        viewListener.setActionsEnabled(false);
        networkInteractor.getShopTalk(viewListener.getActivity(), getShopTalkParam(), new ShopTalkRetrofitInteractor.GetShopTalkListener() {
            @Override
            public void onSuccess(ShopTalkResult result) {
                viewListener.finishLoading();
                viewListener.setActionsEnabled(true);
                viewListener.onGetShopTalk(result);
                paging.setHasNext(PagingHandler.CheckHasNext(result.getPaging()));
                if (PagingHandler.CheckHasNext(result.getPaging())) {
                    paging.nextPage();
                }
            }

            @Override
            public void onError(String error) {
                viewListener.finishLoading();
                viewListener.showError(error, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getShopTalk();
                    }
                });
            }

            @Override
            public void onNullData() {
                viewListener.finishLoading();
                viewListener.showError(viewListener.getActivity().getString(R.string.default_request_error_null_data),
                        new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getShopTalk();
                            }
                        });
            }

            @Override
            public void onNoNetworkConnection() {
                viewListener.finishLoading();
                viewListener.showError("",
                        new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getShopTalk();
                            }
                        });
            }

            @Override
            public void onTimeout() {
                viewListener.finishLoading();
                viewListener.showError(viewListener.getActivity().getString(R.string.default_request_error_timeout),
                        new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getShopTalk();
                            }
                        });
            }

            @Override
            public void onFailAuth() {
                viewListener.finishLoading();
                viewListener.showError(viewListener.getActivity().getString(R.string.default_request_error_forbidden_auth),
                        new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getShopTalk();
                            }
                        });
            }
        });
    }

    private Map<String, String> getShopTalkParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put("shop_domain", viewListener.getActivity().getIntent().getExtras().getString(SHOP_DOMAIN, ""));
        param.put("shop_id", viewListener.getActivity().getIntent().getExtras().getString(SHOP_ID, ""));
        param.put("page", String.valueOf(paging.getPage()));
        param.put("type","s");
        return param;
    }

    @Override
    public void loadMore() {
        if (paging.CheckNextPage() && !networkInteractor.isRequesting())
            getShopTalk();

    }

    @Override
    public void unsubscribe() {
        networkInteractor.unSubscribeObservable();
    }

}
