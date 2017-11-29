package com.tokopedia.core.inboxreputation.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.PreviewProductImage;
import com.tokopedia.core.R;
import com.tokopedia.core.inboxreputation.InboxReputationConstant;
import com.tokopedia.core.inboxreputation.activity.InboxReputationDetailActivity;
import com.tokopedia.core.inboxreputation.adapter.viewbinder.HeaderReputationDataBinder;
import com.tokopedia.core.inboxreputation.fragment.InboxReputationDetailFragment;
import com.tokopedia.core.inboxreputation.fragment.InboxReputationFormFragment;
import com.tokopedia.core.inboxreputation.intentservice.InboxReviewIntentService;
import com.tokopedia.core.inboxreputation.interactor.CacheInboxReputationInteractor;
import com.tokopedia.core.inboxreputation.interactor.CacheInboxReputationInteractorImpl;
import com.tokopedia.core.inboxreputation.interactor.InboxReputationRetrofitInteractor;
import com.tokopedia.core.inboxreputation.interactor.InboxReputationRetrofitInteractorImpl;
import com.tokopedia.core.inboxreputation.listener.InboxReputationDetailFragmentView;
import com.tokopedia.core.inboxreputation.model.ImageUpload;
import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputation;
import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetail;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;
import com.tokopedia.core.inboxreputation.model.param.ActReviewPass;
import com.tokopedia.core.inboxreputation.model.param.InboxReputationPass;
import com.tokopedia.core.inboxreputation.model.param.ReputationDetailPass;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.var.TkpdState;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.http.POST;

/**
 * Created by Nisie on 1/26/16.
 */
public class InboxReputationDetailFragmentPresenterImpl implements
        InboxReputationDetailFragmentPresenter, InboxReputationConstant {


    InboxReputationDetailFragmentView viewListener;

    private InboxReputationRetrofitInteractor inboxReputationRetrofitInteractor;
    private CacheInboxReputationInteractor cacheInboxReputationInteractor;
    InboxReputationDetailFragment.DoActionReputationListener listener;


    public InboxReputationDetailFragmentPresenterImpl(InboxReputationDetailFragmentView viewListener) {
        this.viewListener = viewListener;
        this.inboxReputationRetrofitInteractor = new InboxReputationRetrofitInteractorImpl();
        this.cacheInboxReputationInteractor = new CacheInboxReputationInteractorImpl();
        this.listener = (InboxReputationDetailFragment.DoActionReputationListener) viewListener.getActivity();
    }

    @Override
    public void initData() {
        if (viewListener.getAdapter().getList().size() == 0) {
            cacheInboxReputationInteractor.getInboxReputationDetailCache(viewListener.getInboxReputation().getReputationId(),
                    new CacheInboxReputationInteractor.GetInboxReputationDetailCacheListener() {
                        @Override
                        public void onSuccess(InboxReputationDetail inboxReputation) {
                            setData(inboxReputation);
                            getProductList(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            getProductList(false);
                        }
                    });

        }
    }

    @Override
    public void refreshList(boolean isUsingSlave) {
        getSingleInboxReputation(viewListener.getAdapter().getInboxReputation()
                .getInvoiceRefNum(), isUsingSlave);
    }

    @Override
    public void getSingleInboxReputation(final String invoiceId, final boolean isUsingSlave) {

        showLoading();
        viewListener.setActionEnabled(false);
        viewListener.removeError();

        inboxReputationRetrofitInteractor.getInboxReputation(viewListener.getActivity().getApplicationContext(),
                getSingleInboxReputationParam(invoiceId), new InboxReputationRetrofitInteractor.InboxReputationListener() {
                    @Override
                    public void onSuccess(@NonNull InboxReputation data) {
                        viewListener.getAdapter().setInboxReputation(data.getList().get(0));
                        getProductList(isUsingSlave);
                    }

                    @Override
                    public void onTimeout() {
                        viewListener.finishLoading();
                        showNetworkError(new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getSingleInboxReputation(invoiceId, isUsingSlave);
                            }
                        });

                    }

                    @Override
                    public void onFailAuth() {
                        viewListener.finishLoading();
                        showNetworkError(new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getSingleInboxReputation(invoiceId, isUsingSlave);
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.finishLoading();
                        viewListener.setActionEnabled(true);
                        showNetworkError(error, new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getSingleInboxReputation(invoiceId, isUsingSlave);
                            }
                        });
                    }

                    @Override
                    public void onNullData() {
                        viewListener.finishLoading();
                        viewListener.setActionEnabled(true);
                        viewListener.showNoResult();
                    }

                    @Override
                    public void onNoNetworkConnection() {
                        viewListener.finishLoading();
                        showNetworkError(new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getSingleInboxReputation(invoiceId, isUsingSlave);
                            }
                        });
                    }
                });
    }

    private void showNetworkError(String error, NetworkErrorHelper.RetryClickedListener listener) {
//        if (viewListener.getAdapter().getList().size() == 0)
//            viewListener.showErrorSnackbar(error, listener);
//        else
            viewListener.showSnackbar(error, listener);
    }

    private void showNetworkError(NetworkErrorHelper.RetryClickedListener listener) {
//        if (viewListener.getAdapter().getList().size() == 0)
//            viewListener.showErrorSnackbar(listener);
//        else
            viewListener.showSnackbar(listener);
    }

    private Map<String, String> getSingleInboxReputationParam(String invoiceId) {
        InboxReputationPass param = new InboxReputationPass();
        param.setAct(ACT_GET_REPUTATION);
        param.setFilter(ALL);
        param.setKeyword(invoiceId);
        param.setNav(NAV_INBOX_REPUTATION);
        param.setPage(0);
        return param.getInboxReputationParam();
    }


    @Override
    public void getProductList(final boolean isUsingSlave) {
        showLoading();
        viewListener.removeError();
        viewListener.setActionEnabled(false);

        inboxReputationRetrofitInteractor.getInboxReputationDetail(
                viewListener.getActivity(), getProductListParam(isUsingSlave),
                new InboxReputationRetrofitInteractor.InboxReputationDetailListener() {
                    @Override
                    public void onSuccess(@NonNull InboxReputationDetail response) {
                        viewListener.finishLoading();
                        viewListener.setActionEnabled(true);

                        setCache(response);
                        setData(response);
                    }

                    @Override
                    public void onTimeout() {
                        viewListener.finishLoading();
                        showNetworkError(new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getProductList(isUsingSlave);
                            }
                        });


                    }

                    @Override
                    public void onFailAuth() {
                        viewListener.finishLoading();
                        showNetworkError(new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getProductList(isUsingSlave);
                            }
                        });

                    }

                    @Override
                    public void onError(String error) {
                        viewListener.finishLoading();
                        viewListener.setActionEnabled(true);
                        showNetworkError(error, new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getProductList(isUsingSlave);
                            }
                        });
                    }

                    @Override
                    public void onNullData() {
                        viewListener.finishLoading();
                        viewListener.setActionEnabled(true);
                        viewListener.showNoResult();
                    }

                    @Override
                    public void onNoNetworkConnection() {
                        viewListener.finishLoading();
                        showNetworkError(new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getProductList(isUsingSlave);
                            }
                        });
                    }
                });
    }

    private void showLoading() {
        if (viewListener.getAdapter().getList().size() == 0) {
            viewListener.getAdapter().showLoading(true);
        } else {
            viewListener.showRefresh();
        }
    }

    private void setData(InboxReputationDetail response) {


        if (response.getInboxReputationDetailItemList().size() == 0) {
            viewListener.showNoResult();
        }
        viewListener.onSuccessGetDetail(response);

    }

    private void setCache(InboxReputationDetail response) {
        if (!response.getInboxReputationDetailItemList().isEmpty()) {
            cacheInboxReputationInteractor.setInboxReputationDetailCache(viewListener.getInboxReputation().getReputationId(), response);
        }
    }

    @Override
    public Map<String, String> getProductListParam(boolean isUsingSlave) {
        ReputationDetailPass param = new ReputationDetailPass();
        param.setBuyerSeller(viewListener.getInboxReputation().getRevieweeRole());
        param.setReputationId(viewListener.getInboxReputation().getReputationId());
        param.setReputationInboxId(viewListener.getInboxReputation().getReputationInboxId());
        if (isUsingSlave) param.setN(1);
        return param.getInboxReputationParam();
    }

    @Override
    public int getSmiley(String status) {
        switch (status) {
            case HeaderReputationDataBinder.STATUS_BAD:
                return R.drawable.ic_icon_repsis_sad_active;
            case HeaderReputationDataBinder.STATUS_NEUTRAL:
                return R.drawable.ic_icon_repsis_neutral_active;
            case HeaderReputationDataBinder.STATUS_GOOD:
                return R.drawable.ic_icon_repsis_smile_active;
            case HeaderReputationDataBinder.STATUS_HIDDEN:
                return R.drawable.ic_check_green;
            default:
                return R.drawable.ic_icon_repsis_question;
        }
    }

    @Override
    public void onUsernameClicked(InboxReputationItem inboxReputation) {
        if (inboxReputation.getRole() == BUYER) {
            Intent intent = new Intent(viewListener.getActivity(), ShopInfoActivity.class);
            Bundle bundle = ShopInfoActivity.createBundle(inboxReputation.getShopId(), "");
            intent.putExtras(bundle);
            viewListener.getActivity().startActivity(intent);
        } else {
            viewListener.getActivity().startActivity(
                    PeopleInfoNoDrawerActivity.createInstance(viewListener.getActivity(), inboxReputation.getBuyerId())
            );
        }
    }

    @Override
    public String getMessageForSmileyFromOpponent(InboxReputationItem inboxReputation) {
        String message;
        if (inboxReputation.getRole() == BUYER) {
            switch (inboxReputation.getReviewerScore()) {
                case HeaderReputationDataBinder.STATUS_BAD:
                    message = viewListener.getActivity().getString(R.string.message_from_seller_bad);
                    break;
                case HeaderReputationDataBinder.STATUS_NEUTRAL:
                    message = viewListener.getActivity().getString(R.string.message_from_seller_neutral);
                    break;
                case HeaderReputationDataBinder.STATUS_GOOD:
                    message = viewListener.getActivity().getString(R.string.message_from_seller_smile);
                    break;
                case HeaderReputationDataBinder.STATUS_HIDDEN:
                    message = viewListener.getActivity().getString(R.string.message_give_review_to_shop);
                    break;
                default:
                    message = viewListener.getActivity().getString(R.string.message_seller_no_give_reputation_yet);
                    break;
            }
        } else {
            switch (inboxReputation.getReviewerScore()) {
                case HeaderReputationDataBinder.STATUS_BAD:
                    message = viewListener.getActivity().getString(R.string.message_from_buyer_bad);
                    break;
                case HeaderReputationDataBinder.STATUS_NEUTRAL:
                    message = viewListener.getActivity().getString(R.string.message_from_buyer_neutral);
                    break;
                case HeaderReputationDataBinder.STATUS_GOOD:
                    message = viewListener.getActivity().getString(R.string.message_from_buyer_smile);
                    break;
                case HeaderReputationDataBinder.STATUS_HIDDEN:
                    message = viewListener.getActivity().getString(R.string.message_give_feedback_to_buyer);
                    break;
                default:
                    message = viewListener.getActivity().getString(R.string.message_buyer_no_give_reputation_yet);
                    break;
            }
        }
        return message;
    }

    public void afterPostForm(Bundle bundle) {
        int isSuccess = bundle.getInt("is_success", 0);
        if (isSuccess == 1) {
            switch (bundle.getString("action", "")) {
                case ACTION_UPDATE_PRODUCT:
                    refreshList(true);
                    viewListener.setActivityResult();
                    break;
                default:
                    break;
            }

            try {
                float qualityRating = Float.parseFloat(bundle.getString("quality_rating"));
                if(qualityRating >= 3) {
                    viewListener.showRatingDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void skipReview(ActReviewPass pass, int position) {
        viewListener.showDialogLoading();
        viewListener.setActionEnabled(false);
        Bundle param = new Bundle();
        param.putParcelable(InboxReviewIntentService.PARAM_SKIP_REVIEW,
                pass);
        param.putInt(InboxReviewIntentService.EXTRA_PRODUCT_POSITION, position);
        listener.skipReview(param);
    }

    @Override
    public void onEditReview(InboxReputationItem inboxReputation,
                             InboxReputationDetail inboxReputationDetail,
                             int position) {
        Intent intent = new Intent(viewListener.getActivity(), InboxReputationDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxReputationFormFragment.PARAM_INBOX_REPUTATION, inboxReputation);
        bundle.putParcelable(InboxReputationFormFragment.PARAM_INBOX_REPUTATION_DETAIL, inboxReputationDetail.getInboxReputationDetailItemList().get(position));
        bundle.putString("token", inboxReputationDetail.getToken());
        bundle.putInt("position", position);
        bundle.putString("nav", InboxReputationDetailActivity.NAV_EDIT_PRODUCT);
        intent.putExtras(bundle);
        viewListener.getActivity().startActivityForResult(intent,
                TkpdState.RequestCode.CODE_OPEN_DETAIL_PRODUCT_REVIEW);
    }

    @Override
    public void onGiveReview(InboxReputationItem inboxReputation, InboxReputationDetail inboxReputationDetail, int position) {
        Intent intent = new Intent(viewListener.getActivity(), InboxReputationDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxReputationFormFragment.PARAM_INBOX_REPUTATION_DETAIL, inboxReputationDetail.getInboxReputationDetailItemList().get(position));
        bundle.putParcelable(InboxReputationFormFragment.PARAM_INBOX_REPUTATION, inboxReputation);
        bundle.putString("token", inboxReputationDetail.getToken());
        bundle.putInt("position", position);
        bundle.putString("nav", InboxReputationDetailActivity.NAV_POST_PRODUCT);
        intent.putExtras(bundle);
        viewListener.getActivity().startActivityForResult(intent,
                TkpdState.RequestCode.CODE_OPEN_DETAIL_PRODUCT_REVIEW);
    }

    @Override
    public void onGiveReply(InboxReputationItem inboxReputation, InboxReputationDetail inboxReputationDetail, int position) {

        Intent intent = new Intent(viewListener.getActivity(), InboxReputationDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(InboxReputationFormFragment.PARAM_INBOX_REPUTATION_DETAIL, inboxReputationDetail.getInboxReputationDetailItemList().get(position));
        bundle.putParcelable(InboxReputationFormFragment.PARAM_INBOX_REPUTATION, inboxReputation);
        bundle.putString("token", inboxReputationDetail.getToken());
        bundle.putInt("position", position);
        bundle.putString("nav", InboxReputationDetailActivity.NAV_RESPONSE_PRODUCT);
        intent.putExtras(bundle);
        viewListener.getActivity().startActivityForResult(intent,
                TkpdState.RequestCode.CODE_OPEN_DETAIL_PRODUCT_REVIEW);

    }

    @Override
    public void deleteResponse(ActReviewPass paramDelete, int position) {
        viewListener.showDialogLoading();
        viewListener.setActionEnabled(false);
        Bundle param = new Bundle();
        param.putParcelable(InboxReviewIntentService.PARAM_DELETE_RESPONSE,
                paramDelete);
        param.putInt(InboxReviewIntentService.EXTRA_PRODUCT_POSITION, position);
        listener.deleteResponse(param);

    }

    @Override
    public void postReputation(ActReviewPass pass) {
        viewListener.showDialogLoading();
        viewListener.setActionEnabled(false);
        Bundle param = new Bundle();
        param.putParcelable(InboxReviewIntentService.PARAM_POST_REPUTATION,
                pass);
        listener.postReputation(param);

    }

    @Override
    public String getSmileyString(String smiley_score) {
        switch (smiley_score) {
            case HeaderReputationDataBinder.SCORE_BAD:
                return HeaderReputationDataBinder.STATUS_BAD;
            case HeaderReputationDataBinder.SCORE_NEUTRAL:
                return HeaderReputationDataBinder.STATUS_NEUTRAL;
            case HeaderReputationDataBinder.SCORE_GOOD:
                return HeaderReputationDataBinder.STATUS_GOOD;
            default:
                return "";
        }
    }

    @Override
    public void updateCacheSkippedReview(final String reputationId, final int productPosition) {
        cacheInboxReputationInteractor.getInboxReputationDetailCache(reputationId, new CacheInboxReputationInteractor.GetInboxReputationDetailCacheListener() {
            @Override
            public void onSuccess(InboxReputationDetail inboxReputationDetail) {
                inboxReputationDetail.getInboxReputationDetailItemList().get(productPosition).setIsSkipped(1);
                cacheInboxReputationInteractor.setInboxReputationDetailCache(reputationId, inboxReputationDetail);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void updateCacheDeletedResponse(final String reputationId, final int productPosition) {
        cacheInboxReputationInteractor.getInboxReputationDetailCache(reputationId, new CacheInboxReputationInteractor.GetInboxReputationDetailCacheListener() {
            @Override
            public void onSuccess(InboxReputationDetail inboxReputationDetail) {

                inboxReputationDetail.getInboxReputationDetailItemList().get(productPosition).getReviewResponse().setResponseMessage("0");
                inboxReputationDetail.getInboxReputationDetailItemList().get(productPosition).getReviewResponse().setResponseTime("0");
                inboxReputationDetail.getInboxReputationDetailItemList().get(productPosition).getReviewResponse().setIsResponseRead(0);

                cacheInboxReputationInteractor.setInboxReputationDetailCache(reputationId, inboxReputationDetail);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void postReport(ActReviewPass pass) {
        viewListener.showDialogLoading();
        viewListener.setActionEnabled(false);
        Bundle param = new Bundle();
        param.putParcelable(InboxReviewIntentService.PARAM_POST_REPORT,
                pass);
        listener.postReport(param);
    }

    @Override
    public void onPreviewImageClicked(int position, ArrayList<ImageUpload> list) {

        ArrayList<String> listImage = new ArrayList<>();
        ArrayList<String> listDesc = new ArrayList<>();
        for (ImageUpload imageUpload : list) {
            listImage.add(imageUpload.getPicSrcLarge());
            listDesc.add(imageUpload.getDescription());
        }

        Intent intent = new Intent(viewListener.getActivity(), PreviewProductImage.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("fileloc", listImage);
        bundle.putStringArrayList("image_desc", listDesc);
        bundle.putInt("img_pos", position);
        intent.putExtras(bundle);
        viewListener.getActivity().startActivity(intent);
    }

    @Override
    public ActReviewPass getReportParam(InboxReputationDetailItem inboxReputationDetail, String message) {
        ActReviewPass param = new ActReviewPass();
        param.setReviewId(inboxReputationDetail.getReviewId());
        param.setShopId(inboxReputationDetail.getShopId());
        param.setReportMessage(message);
        return param;
    }

    @Override
    public void showShareProvider(InboxReputationDetailItem inboxReputationDetailItem) {
        viewListener.showShareProvider(inboxReputationDetailItem);
    }

    @Override
    public String getMessageForSmileyForOpponent(String smiley) {
        switch (smiley) {
            case HeaderReputationDataBinder.STATUS_BAD:
                return viewListener.getActivity().getString(R.string.message_smiley_prompt_bad);
            case HeaderReputationDataBinder.STATUS_NEUTRAL:
                return viewListener.getActivity().getString(R.string.message_smiley_prompt_neutral);
            case HeaderReputationDataBinder.STATUS_GOOD:
                return viewListener.getActivity().getString(R.string.message_smiley_prompt_good);
            default:
                return viewListener.getActivity().getString(R.string.msg_reputation_locked);
        }
    }

    @Override
    public ActReviewPass getSmileyParam(InboxReputationItem inboxReputation, String status) {
        ActReviewPass param = new ActReviewPass();
        param.setReputationId(inboxReputation.getReputationId());
        param.setReputationScore(getReputationScore(status));
        param.setRole(String.valueOf(inboxReputation.getRole()));
        return param;
    }

    private String getReputationScore(String status) {
        switch (status) {
            case HeaderReputationDataBinder.STATUS_BAD:
                return HeaderReputationDataBinder.SCORE_BAD;
            case HeaderReputationDataBinder.STATUS_NEUTRAL:
                return HeaderReputationDataBinder.SCORE_NEUTRAL;
            case HeaderReputationDataBinder.STATUS_GOOD:
                return HeaderReputationDataBinder.SCORE_GOOD;
            default:
                return "";
        }
    }

    public ActReviewPass getSkipParam(InboxReputationItem inboxReputation, InboxReputationDetailItem inboxReputationDetail) {
        ActReviewPass param = new ActReviewPass();
        param.setReputationId(inboxReputation.getReputationId());
        param.setShopId(inboxReputation.getShopId());
        param.setReviewId(inboxReputationDetail.getReviewId());
        param.setProductId(inboxReputationDetail.getProductId());
        return param;
    }

    @Override
    public void onDestroyView() {
        inboxReputationRetrofitInteractor.unSubscribeObservable();
    }

}
