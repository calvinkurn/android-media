package com.tokopedia.groupchat.chatroom.view.listener;

import android.content.Context;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.groupchat.chatroom.domain.pojo.ExitMessage;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PendingChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PinnedMessageViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel;
import com.tokopedia.groupchat.common.analytics.EEPromotion;
import com.tokopedia.groupchat.vote.view.model.VoteInfoViewModel;

import java.util.List;

/**
 * @author by nisie on 3/21/18.
 */

public interface GroupChatContract {

    interface View extends CustomerView {

        Context getContext();

        void onErrorGetChannelInfo(String errorMessage);

        void onSuccessGetChannelInfo(ChannelInfoViewModel channelInfoViewModel);

        void updateVoteViewModel(VoteInfoViewModel voteInfoViewModel, String voteType);

        void showInfoDialog();

        void updateSprintSaleData(SprintSaleAnnouncementViewModel messageItem);

        void handleVoteAnnouncement(VoteAnnouncementViewModel messageItem, String voteType);

        @Nullable
        SprintSaleViewModel getSprintSaleViewModel();

        @Nullable
        ChannelInfoViewModel getChannelInfoViewModel();

        void eventClickComponentEnhancedEcommerce(String componentType, String campaignName, String
                attributeName, List<EEPromotion> list);

        void eventViewComponentEnhancedEcommerce(String componentType, String campaignName, String
                attributeName, List<EEPromotion> list);

        String generateAttributeApplink(String applink,
                                        String attributeBanner);

        void startApplink(String applink);

        void transitionToTabChat();

        void transitionToTabVote();

        void transitionToTabInfo();

        void vibratePhone();

        String getAttributionTracking(String attributePartnerLogo);

        void removeGroupChatPoints();

        void onSuccessLogin();

        void onChannelFrozen();

        @Nullable
        PinnedMessageViewModel getPinnedMessage();

        @Nullable
        ExitMessage getExitMessage();

        void onMessageReceived(Visitable text, boolean hideMessage);

        void setSnackBarRetry();

        void setSnackBarErrorLoading();

        void onOpenWebSocket();

        void onSuccessEnterChannel();

        void clearMessageEditText();

        void afterSendMessage(PendingChatViewModel pendingChatViewModel, Exception errorSendIndicator);

        void reportWebSocket(String url, String error);

        void initVideoFragment();

        void onSuccessRefreshChannelInfo(ChannelInfoViewModel channelInfoViewModel);

        void showOverlayDialogOnScreen();
    }

    interface Presenter extends CustomerPresenter<GroupChatContract.View> {

        void getChannelInfo(String channelUuid);

        void getChannelInfo(String channelUuid, boolean reInit);
    }
}
