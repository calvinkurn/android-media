package com.tokopedia.tkpd.inboxticket.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.tokopedia.tkpd.GalleryBrowser;
import com.tokopedia.tkpd.ImageGallery;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.customadapter.ImageUpload;
import com.tokopedia.tkpd.inboxticket.InboxTicketConstant;
import com.tokopedia.tkpd.inboxticket.activity.InboxTicketDetailActivity;
import com.tokopedia.tkpd.inboxticket.fragment.InboxTicketDetailFragment;
import com.tokopedia.tkpd.inboxticket.fragment.InboxTicketFragment;
import com.tokopedia.tkpd.inboxticket.interactor.InboxTicketCacheInteractor;
import com.tokopedia.tkpd.inboxticket.interactor.InboxTicketCacheInteractorImpl;
import com.tokopedia.tkpd.inboxticket.interactor.InboxTicketRetrofitInteractor;
import com.tokopedia.tkpd.inboxticket.interactor.InboxTicketRetrofitInteractorImpl;
import com.tokopedia.tkpd.inboxticket.model.InboxTicketParam;
import com.tokopedia.tkpd.inboxticket.model.inboxticketdetail.InboxTicketDetail;
import com.tokopedia.tkpd.inboxticket.model.inboxticketdetail.TicketReply;
import com.tokopedia.tkpd.inboxticket.model.inboxticketdetail.TicketReplyDatum;
import com.tokopedia.tkpd.inboxticket.model.replyticket.ReplyResult;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.util.ImageUploadHandler;
import com.tokopedia.tkpd.util.PagingHandler;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Nisie on 4/22/16.
 */
public class InboxTicketDetailFragmentPresenterImpl implements InboxTicketDetailFragmentPresenter,
        InboxTicketConstant {

    InboxTicketDetailFragment viewListener;
    InboxTicketRetrofitInteractor networkInteractor;
    InboxTicketCacheInteractor cacheInteractor;
    PagingHandler pagingHandler;
    InboxTicketDetailFragment.DoActionInboxTicketListener listener;
    ImageUploadHandler imageUploadHandler;


    public InboxTicketDetailFragmentPresenterImpl(InboxTicketDetailFragment viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new InboxTicketRetrofitInteractorImpl();
        this.cacheInteractor = new InboxTicketCacheInteractorImpl();
        this.pagingHandler = new PagingHandler();
        this.listener = (InboxTicketDetailActivity) viewListener.getActivity();
        this.imageUploadHandler = ImageUploadHandler.createInstance(viewListener);
    }

    @Override
    public void getViewMore() {
        viewListener.setRefreshing(true);
        viewListener.setActionsEnabled(false);
        networkInteractor.viewMore(viewListener.getActivity(), getViewMoreParam(), new InboxTicketRetrofitInteractor.ViewMoreListener() {
            @Override
            public void onSuccess(@NonNull TicketReply result) {

                int startData;
                if (pagingHandler.getPage() == 1) {
                    startData = result.getTicketReplyData().size() - 3;
                } else {
                    startData = result.getTicketReplyData().size() - 1;
                }

                InboxTicketDetail data = viewListener.getAdapter().getData();
                for (int i = startData; i >= 0; i--) {
                    data.getTicketReply().getTicketReplyData().add(0, result.getTicketReplyData().get(i));
                }

                data.getTicketReply().setTicketReplyTotalData(result.getTicketReplyTotalData());
                data.getTicketReply().setTicketReplyTotalPage(result.getTicketReplyTotalPage());

                pagingHandler.setHasNext(viewListener.getAdapter().getData().getTicketReply().getTicketReplyData().size() < result.getTicketReplyTotalPage());
                pagingHandler.nextPage();

                viewListener.getAdapter().setData(data);
                viewListener.getAdapter().notifyDataSetChanged();
                viewListener.finishLoading();
            }

            @Override
            public void onTimeout(String message) {
                viewListener.finishLoading();
                viewListener.showError(message);

            }

            @Override
            public void onError(String error) {
                viewListener.finishLoading();
                viewListener.showError(error);

            }

            @Override
            public void onNullData() {
                viewListener.finishLoading();

            }

            @Override
            public void onNoConnectionError() {
                viewListener.finishLoading();
                viewListener.showError("");

            }
        });
    }

    private Map<String, String> getViewMoreParam() {
        InboxTicketParam param = new InboxTicketParam();
        param.setTicketId(viewListener.getArguments().getString(InboxTicketFragment.TICKET_ID_BUNDLE));
        param.setPage(String.valueOf(pagingHandler.getPage()));
        return param.getViewMoreParam();
    }

    @Override
    public void onRefresh() {
        pagingHandler.resetPage();
        getInboxTicketDetail();
    }

    @Override
    public void setCache() {
        cacheInteractor.getInboxTicketDetailCache(viewListener.getArguments().getString(TICKET_ID_BUNDLE),
                new InboxTicketCacheInteractor.GetInboxTicketDetailCacheListener() {
                    @Override
                    public void onSuccess(InboxTicketDetail result) {
                        viewListener.onSuccessGetInboxTicketDetail(result);
                        viewListener.getAdapter().setData(result);
                        viewListener.finishLoading();

                        getInboxTicketDetail();
                    }


                    @Override
                    public void onError(Throwable e) {
                        getInboxTicketDetail();
                    }
                });
    }

    @Override
    public void onSendButtonClicked() {
        if (isReplyValid()) {
            viewListener.showReplyDialog();
        }
    }

    @Override
    public void sendReply() {
        viewListener.showProgressDialog();

        networkInteractor.sendReply(viewListener.getActivity(), getSendReplyParam(), new InboxTicketRetrofitInteractor.ReplyTicketListener() {
                    @Override
                    public void onSuccess(final ReplyResult replyResult) {
                        viewListener.finishLoading();
                        cacheInteractor.getInboxTicketDetailCache(viewListener.getArguments().getString(TICKET_ID_BUNDLE),
                                new InboxTicketCacheInteractor.GetInboxTicketDetailCacheListener() {
                                    @Override
                                    public void onSuccess(InboxTicketDetail result) {
                                        TicketReplyDatum newTicket = convertToTicketReplyDatum(replyResult);
                                        result = addTicketToList(result, newTicket);
                                        cacheInteractor.setInboxTicketDetailCache(viewListener.getArguments().getString(TICKET_ID_BUNDLE), result);
                                        viewListener.onSuccessReply(newTicket);

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        TicketReplyDatum newTicket = convertToTicketReplyDatum(replyResult);
                                        viewListener.onSuccessReply(newTicket);
                                    }
                                }

                        );


                    }

                    @Override
                    public void onTimeout() {
                        viewListener.finishLoading();
                        viewListener.showError("");
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.finishLoading();
                        viewListener.showError(error);
                    }

                    @Override
                    public void onNullData() {
                        viewListener.finishLoading();
                        viewListener.showError("");
                    }

                    @Override
                    public void onNoConnectionError() {
                        viewListener.finishLoading();
                        viewListener.showError("");
                    }

                    @Override
                    public void onFailAuth() {
                        viewListener.finishLoading();
                        viewListener.showError("");
                    }
                }

        );
    }

    private InboxTicketDetail addTicketToList(InboxTicketDetail result, TicketReplyDatum newTicket) {
        if (result.getTicketReply().getTicketReplyData().size() < 2)
            result.getTicketReply().getTicketReplyData().add(newTicket);
        else {
            result.getTicketReply().getTicketReplyData().remove(0);
            result.getTicketReply().getTicketReplyData().add(newTicket);
        }
        return result;
    }

    private TicketReplyDatum convertToTicketReplyDatum(ReplyResult replyResult) {
        TicketReplyDatum ticketReplyDatum = new TicketReplyDatum();
        ticketReplyDatum.setTicketDetailCreateTimeFmt(replyResult.getTicketDetail().getCreateTimeFmt());
        ticketReplyDatum.setTicketDetailCreateTime(replyResult.getTicketDetail().getCreateTime());
        ticketReplyDatum.setTicketDetailNewStatus(String.valueOf(replyResult.getTicketDetail().getTicketNewStatus()));
        ticketReplyDatum.setTicketDetailAttachment(replyResult.getTicketDetail().getAttachment());
        ticketReplyDatum.setTicketDetailId(String.valueOf(replyResult.getTicketDetailId()));
        ticketReplyDatum.setTicketDetailIsCs(String.valueOf(replyResult.getTicketDetail().getIsCs()));
        ticketReplyDatum.setTicketDetailUserLabelId(String.valueOf(replyResult.getTicketDetail().getUserLabelId()));
        ticketReplyDatum.setTicketDetailUserLabel(replyResult.getTicketDetail().getUserLabel());
        ticketReplyDatum.setTicketDetailUserId(String.valueOf(replyResult.getTicketDetail().getUserId()));
        ticketReplyDatum.setTicketDetailMessage(replyResult.getTicketDetail().getTicketDetailMsg());
        ticketReplyDatum.setTicketDetailNewRating(String.valueOf(replyResult.getTicketDetail().getTicketNewRating()));
        ticketReplyDatum.setTicketDetailUserImage(replyResult.getTicketDetail().getUserPic());
        ticketReplyDatum.setTicketDetailUserUrl(replyResult.getTicketDetail().getUserUrl());
        ticketReplyDatum.setTicketDetailUserName(replyResult.getTicketDetail().getUserName());
        return ticketReplyDatum;
    }

    private InboxTicketParam getSendReplyParam() {
        InboxTicketParam param = new InboxTicketParam();
        param.setTicketId(viewListener.getArguments().getString(TICKET_ID_BUNDLE));
        param.setReplyMessage(viewListener.getComment());
        if (viewListener.getImageAdapter().getList().size() > 0) {
            param.setImageUploads(viewListener.getImageAdapter().getList());
        }
        return param;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == ImageUploadHandler.REQUEST_CODE)
                && (resultCode == Activity.RESULT_OK || resultCode == GalleryBrowser.RESULT_CODE)) {

            int position = viewListener.getImageAdapter().getList().size();
            ImageUpload image = new ImageUpload();
            image.setPosition(position);
            image.setImageId("image" + UUID.randomUUID().toString());

            switch (resultCode) {
                case GalleryBrowser.RESULT_CODE:
                    image.setFileLoc(data.getStringExtra(ImageGallery.EXTRA_URL));
                    break;
                case Activity.RESULT_OK:
                    image.setFileLoc(imageUploadHandler.getCameraFileloc());
                    break;
                default:
                    break;
            }
            viewListener.addImage(image);

        }
    }

    @Override
    public void actionImagePicker() {
        imageUploadHandler.actionImagePicker();
    }

    @Override
    public void actionCamera() {
        imageUploadHandler.actionCamera();

    }

    @Override
    public void onDestroyView() {
        networkInteractor.unsubscribe();
    }

    private boolean isReplyValid() {
        if (viewListener.getComment().trim().equals("")) {
            viewListener.showError(viewListener.getString(R.string.error_field_required));
            return false;
        } else {
            return true;
        }
    }

    private void getInboxTicketDetail() {
        showLoading();
        viewListener.removeError();
        networkInteractor.getInboxTicketDetail(viewListener.getActivity(), getInboxTicketDetailParam(), new InboxTicketRetrofitInteractor.GetInboxTicketDetailListener() {
            @Override
            public void onSuccess(@NonNull InboxTicketDetail result) {
                cacheInteractor.setInboxTicketDetailCache(
                        viewListener.getArguments().getString(TICKET_ID_BUNDLE), result);
                viewListener.onSuccessGetInboxTicketDetail(result);
                viewListener.finishLoading();
                viewListener.setResultSuccess();
            }

            @Override
            public void onTimeout(String message) {
                viewListener.finishLoading();
                showError(message);
            }

            @Override
            public void onError(String error) {
                viewListener.finishLoading();
                showError(error);

            }

            @Override
            public void onNullData() {
                viewListener.finishLoading();

            }

            @Override
            public void onNoConnectionError() {
                viewListener.finishLoading();
                showError("");
            }
        });
    }

    private void showLoading() {
        if (!isDataEmpty())
            viewListener.showRefreshing();
        else
            viewListener.showLoadingAll();
    }

    private void showError(String message) {
        if (!isDataEmpty())
            viewListener.showError(message);
        else
            viewListener.showEmptyState(message, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    getInboxTicketDetail();

                }
            });
    }

    private boolean isDataEmpty() {
        return viewListener.getAdapter().getData() == null
                || viewListener.getAdapter().getData().getTicket() == null
                || viewListener.getAdapter().getData().getTicket().getTicketFirstMessage() == null
                || viewListener.getAdapter().getData().getTicket().getTicketFirstMessage().equals("");
    }

    private Map<String, String> getInboxTicketDetailParam() {
        InboxTicketParam param = new InboxTicketParam();
        param.setInboxId(viewListener.getArguments().getString(InboxTicketFragment.INBOX_ID_BUNDLE));
        return param.getInboxTicketDetailParam();
    }
}
