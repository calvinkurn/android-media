package com.tokopedia.topchat.chatroom.view.listener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RawRes;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse;
import com.tokopedia.topchat.chatroom.domain.pojo.invoicesent.InvoiceLinkPojo;
import com.tokopedia.topchat.chatroom.domain.pojo.reply.Attachment;
import com.tokopedia.topchat.chatroom.domain.pojo.replyaction.ReplyActionData;
import com.tokopedia.topchat.chatroom.view.adapter.ChatRoomAdapter;
import com.tokopedia.topchat.chatroom.view.presenter.WebSocketInterface;
import com.tokopedia.topchat.chatroom.view.viewmodel.ChatRoomViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.ChatShopInfoViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.chatactionbubble.ChatActionBubbleViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageupload.ImageUploadViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.quickreply.QuickReplyListViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.quickreply.QuickReplyViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.rating.ChatRatingViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevenfredian on 9/14/17.
 */

public class ChatRoomContract {

    public interface View extends CustomerView {

        Bundle getArguments();

        void setHeader();

        void displayReplyField(boolean b);

        ChatRoomAdapter getAdapter();

        Context getContext();

        String getReplyMessage();

        String getString(int error_empty_report);

        void showError(String string);

        void setViewEnabled(boolean b);

        void addDummyMessage(String dummyText, String startTime);

        void setCanLoadMore(boolean hasNext);

        void scrollToBottom();

        void hideMainLoading();

        void setOnlineDesc(String s, boolean isOnline);

        WebSocketInterface getInterface();

        void onGoToTimeMachine(String url);

        void addTimeMachine();

        String getKeyword();

        void setResult(ChatRoomViewModel model);

        void notifyConnectionWebSocket();

        void startActivity(Intent instance);

        void startActivityForResult(Intent intent, int requestCode);

        Context getActivity();

        void onSuccessSendReply(ReplyActionData data, String reply);

        void onErrorSendReply();

        void resetReplyColumn();

        @Deprecated
        boolean isCurrentThread(int msgId);

        boolean isCurrentThread(String msgId);

        boolean isMyMessage(int fromUid);

        boolean isMyMessage(String fromUid);

        void setTemplate(List<Visitable> listTemplate);

        void addTemplateString(String message);

        void goToSettingTemplate();

        void onGoToGallery(Attachment attachment, String fullTime);

        void onGoToImagePreview(String imageUrl, String replyTime);

        void onGoToWebView(String attachment, String id);

        void handleBranchIOLinkClick(String url);

        boolean isBranchIOLink(String url);

        boolean needCreateWebSocket();

        void hideNotifier();

        void onSuccessInitMessage();

        void disableAction();

        void onErrorInitMessage(String s);

        boolean isAllowedTemplate();

        Fragment getFragment();

        void onErrorUploadImages(String throwable, ImageUploadViewModel model);

        void onRetrySendImage(ImageUploadViewModel element);

        void onSuccessSendAttach(ReplyActionData data, ImageUploadViewModel model);

        void setUploadingMode(boolean b);

        void scrollToBottomWithCheck();

        void setHeaderModel(String nameHeader, String imageHeader);

        void startAttachProductActivity(String shopId, String shopName, boolean isSeller);

        void productClicked(Integer productId, String productName, String productPrice, Long dateTime, String url);

        boolean isChatBot();

        void onQuickReplyClicked(QuickReplyListViewModel quickReplyListViewModel, QuickReplyViewModel quickReply);

        void onChatActionBalloonSelected(ChatActionBubbleViewModel message, Visitable
                modelToBeRemoved);

        void showQuickReplyView(QuickReplyListViewModel model);

        void onInvoiceSelected(InvoiceLinkPojo selectedInvoice);

        void onClickRating(ChatRatingViewModel element, int rating);

        void onSuccessSetRating(ChatRatingViewModel element);

        void onErrorSetRating(String errorMessage);

        void showSearchInvoiceScreen();

        boolean shouldHandleUrlManually(String url);

        void showSnackbarError(String string);

        UserSession getUserSession();

        void setMessageId(String messageId);

        void enableWebSocket();

        void showReasonRating(String messageId, long replyTimeNano, ArrayList<String> reasons);

        void setUserStatus(String status, boolean isOnline);

        void successDeleteChat();

        void setChatShopInfoData(ChatShopInfoViewModel viewModel);

        void setMenuVisible(boolean isVisible);

        void toggleFollowSuccess();

        void finishActivity();

        void setInboxMessageVisibility(ChatSettingsResponse data, boolean isVisible);

        android.view.View getRootView();

        void shouldShowChatSettingsMenu(boolean showChatSettingMenu);

        void enableChatSettings();

        String getQueryString(@RawRes int id);
    }

    public interface Presenter extends CustomerPresenter<View> {
        void getReply(int mode);

        void getAttachProductDialog(String shopId, String shopName, String senderRole);

        void onOpenWebSocket();

        void closeWebSocket();

        void onGoToDetail(String id, String role, String string);

        void sendMessage(int networkType);

        void sendMessage(int networkType, String reply);

        void initMessage(String s, String string, String string1, String string2);

        void openCamera();

        void startUpload(List<ImageUploadViewModel> list, int network);


        String getFileLocFromCamera();

        void setChatRating(ChatRatingViewModel model, int userId, int rating);

        void getExistingChat();

        void sendReasonRating(String messageId, long replyTimeNano, String reason);

        void getUserStatus(String userId, String role);

        void deleteChat(String messageId);

        void getFollowStatus(String shopId);

        void doFollowUnfollowToggle(String shopId);

        void sendQuickReply(String messageId, QuickReplyViewModel quickReply, String startTime);
    }
}
