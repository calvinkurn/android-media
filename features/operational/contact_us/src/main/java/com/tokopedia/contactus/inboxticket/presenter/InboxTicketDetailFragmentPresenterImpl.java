package com.tokopedia.contactus.inboxticket.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.tokopedia.contactus.createticket.interactor.ContactUsRetrofitInteractor;
import com.tokopedia.contactus.createticket.interactor.ContactUsRetrofitInteractorImpl;
import com.tokopedia.contactus.inboxticket.InboxTicketConstant;
import com.tokopedia.contactus.inboxticket.activity.InboxTicketDetailActivity;
import com.tokopedia.contactus.inboxticket.fragment.InboxTicketDetailFragment;
import com.tokopedia.contactus.inboxticket.fragment.InboxTicketFragment;
import com.tokopedia.contactus.inboxticket.interactor.InboxTicketCacheInteractor;
import com.tokopedia.contactus.inboxticket.interactor.InboxTicketCacheInteractorImpl;
import com.tokopedia.contactus.inboxticket.interactor.InboxTicketRetrofitInteractor;
import com.tokopedia.contactus.inboxticket.interactor.InboxTicketRetrofitInteractorImpl;
import com.tokopedia.contactus.inboxticket.model.InboxTicketParam;
import com.tokopedia.contactus.inboxticket.model.inboxticketdetail.InboxTicketDetail;
import com.tokopedia.contactus.inboxticket.model.inboxticketdetail.TicketReply;
import com.tokopedia.contactus.inboxticket.model.inboxticketdetail.TicketReplyDatum;
import com.tokopedia.contactus.inboxticket.model.replyticket.ReplyResult;
import com.tokopedia.core2.R;
import com.tokopedia.core.customadapter.ImageUpload;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

/**
 * Created by Nisie on 4/22/16.
 */
public class InboxTicketDetailFragmentPresenterImpl implements InboxTicketDetailFragmentPresenter,
        InboxTicketConstant {

    InboxTicketDetailFragment viewListener;
    InboxTicketRetrofitInteractor networkInteractor;
    ContactUsRetrofitInteractor contactUsRetrofitInteractor;
    InboxTicketCacheInteractor cacheInteractor;
    PagingHandler pagingHandler;
    InboxTicketDetailFragment.DoActionInboxTicketListener listener;
    ImageUploadHandler imageUploadHandler;

    public InboxTicketDetailFragmentPresenterImpl(InboxTicketDetailFragment viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new InboxTicketRetrofitInteractorImpl();
        this.contactUsRetrofitInteractor = new ContactUsRetrofitInteractorImpl();
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

                InboxTicketDetail data = viewListener.getAdapter().getHeaderView().getData();
                for (int i = startData; i >= 0; i--) {
                    data.getTicketReply().getTicketReplyData().add(0, result.getTicketReplyData().get(i));
                }

                data.getTicketReply().setTicketReplyTotalData(result.getTicketReplyTotalData());
                data.getTicketReply().setTicketReplyTotalPage(result.getTicketReplyTotalPage());

                pagingHandler.setHasNext(viewListener.getAdapter().getHeaderView().getData()
                        .getTicketReply().getTicketReplyData().size() < result.getTicketReplyTotalPage());
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


    private Map<String, String> getCommentRatingParam(String isHelpful) {
        InboxTicketParam param = new InboxTicketParam();
        param.setCommentId(viewListener.getCommentId());
        param.setRating(isHelpful);
        param.setUserId(SessionHandler.getLoginID(viewListener.context));
        return param.getCommentRatingParam();
    }


    @Override
    public void commentRating(String isHelpful) {
        contactUsRetrofitInteractor.commentRating(viewListener.getActivity(), getCommentRatingParam(isHelpful), new ContactUsRetrofitInteractor.CommentRatingListener() {
            @Override
            public void onSuccess(ReplyResult replyResult) {
                viewListener.showCommentView();
            }

            @Override
            public void onTimeout() {
                showError(viewListener.getString(R.string.default_request_error_timeout));
            }

            @Override
            public void onError(String error) {
                showError(error);
            }

            @Override
            public void onNullData() {
                showError(viewListener.getString(R.string.default_request_error_null_data));
            }

            @Override
            public void onNoConnectionError() {
                showError(viewListener.getString(R.string.error_no_connection2));
            }

            @Override
            public void onFailAuth() {
                showError(viewListener.getString(R.string.default_request_error_unknown));
            }
        });
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
        if (requestCode == InboxTicketDetailFragment.REQUEST_CODE_INBOX_TICKET && resultCode == Activity.RESULT_OK && data != null) {
            int position = viewListener.getImageAdapter().getList().size();
            ImageUpload image = new ImageUpload();
            image.setPosition(position);
            image.setImageId("image" + UUID.randomUUID().toString());
            ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
            if (imageUrlOrPathList!= null && imageUrlOrPathList.size() > 0) {
                image.setFileLoc(imageUrlOrPathList.get(0));
            }
            viewListener.addImage(image);
        }
    }

    @Override
    public void actionImagePicker() {
        
    }

    @Override
    public void actionCamera() {
        imageUploadHandler.actionCamera();

    }

    @Override
    public void onDestroyView() {
        networkInteractor.unsubscribe();
        contactUsRetrofitInteractor.unsubscribe();
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
        return viewListener.getAdapter().getHeaderView().getData() == null
                || viewListener.getAdapter().getHeaderView().getData() == null
                || viewListener.getAdapter().getHeaderView().getData().getTicket().getTicketFirstMessage() == null
                || viewListener.getAdapter().getHeaderView().getData().getTicket().getTicketFirstMessage().equals("");
    }

    private Map<String, String> getInboxTicketDetailParam() {
        InboxTicketParam param = new InboxTicketParam();
        param.setInboxId(viewListener.getArguments().getString(InboxTicketFragment.INBOX_ID_BUNDLE));
        return param.getInboxTicketDetailParam();
    }
}
