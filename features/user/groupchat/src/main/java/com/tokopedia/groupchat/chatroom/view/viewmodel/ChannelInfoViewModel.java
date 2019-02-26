package com.tokopedia.groupchat.chatroom.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.tokopedia.groupchat.chatroom.domain.pojo.ButtonsPojo;
import com.tokopedia.groupchat.chatroom.domain.pojo.ExitMessage;
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.SettingGroupChat;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.BackgroundViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChannelPartnerViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatPointsViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyItemViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PinnedMessageViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel;
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel;
import com.tokopedia.groupchat.vote.view.model.VoteInfoViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 2/22/18.
 */

public class ChannelInfoViewModel implements Parcelable {
    private String channelId;
    private String title;
    private String channelUrl;
    private String bannerUrl;
    private String blurredBannerUrl;
    private String adsImageUrl;
    private String adsLink;
    private String adsId;
    private String adsName;
    private String bannerName;
    private String groupChatToken;
    private String adminName;
    private String image;
    private String adminPicture;
    private String description;
    private String totalView;
    private List<ChannelPartnerViewModel> channelPartnerViewModels;
    private String bannedMessage;
    private String kickedMessage;
    private boolean isFreeze;
    private String videoId;
    private Boolean videoLive;
    private SettingGroupChat settingGroupChat;
    public OverlayViewModel overlayViewModel;
    private DynamicButtonsViewModel dynamicButtons;
    private BackgroundViewModel backgroundViewModel;

    @Nullable
    private VoteInfoViewModel voteInfoViewModel;

    @Nullable
    private SprintSaleViewModel sprintSaleViewModel;

    @Nullable
    private GroupChatPointsViewModel groupChatPointsViewModel;

    @Nullable
    private PinnedMessageViewModel pinnedMessageViewModel;

    @Nullable
    private ExitMessage exitMessage;

    @Nullable
    private List<GroupChatQuickReplyItemViewModel> quickRepliesViewModel;

    public ChannelInfoViewModel(String channelId) {
        this.channelId = channelId;
        this.title = "";
        this.channelUrl = "";
        this.bannerUrl = "";
        this.blurredBannerUrl = "";
        this.adsImageUrl = "";
        this.adsLink = "";
        this.adsName = "";
        this.adsId = "";
        this.bannerName = "";
        this.groupChatToken = "";
        this.adminName = "";
        this.image = "";
        this.adminPicture = "";
        this.description = "";
        this.totalView = "";
        this.channelPartnerViewModels = new ArrayList<>();
        this.voteInfoViewModel = null;
        this.sprintSaleViewModel = null;
        this.bannedMessage = "";
        this.kickedMessage = "";
        this.isFreeze = false;
        this.pinnedMessageViewModel = null;
        this.exitMessage = null;
        this.quickRepliesViewModel = null;
        this.videoId = "";
        this.videoLive = false;
        this.settingGroupChat = null;
        this.overlayViewModel = null;
        this.dynamicButtons = null;
        this.backgroundViewModel = null;
    }

    public ChannelInfoViewModel(String channelId, String title, String channelUrl, String bannerUrl,
                                String blurredBannerUrl,
                                String adsImageUrl, String adsLink, String adsName, String adsId,
                                String bannerName, String groupChatToken, String adminName, String image,
                                String adminPicture, String description, String totalView,
                                List<ChannelPartnerViewModel> channelPartnerViewModels,
                                @Nullable VoteInfoViewModel voteInfoViewModel,
                                @Nullable SprintSaleViewModel sprintSaleViewModel,
                                String bannedMessage, String kickedMessage, boolean isFreeze,
                                @Nullable PinnedMessageViewModel pinnedMessageViewModel,
                                @Nullable ExitMessage exitMessage,
                                List<GroupChatQuickReplyItemViewModel> quickRepliesViewModel,
                                String videoId, Boolean videoLive,
                                SettingGroupChat settingGroupChat, OverlayViewModel overlayViewModel,
                                DynamicButtonsViewModel dynamicButtons, BackgroundViewModel backgroundViewModel) {
        this.channelId = channelId;
        this.title = title;
        this.channelUrl = channelUrl;
        this.bannerUrl = bannerUrl;
        this.blurredBannerUrl = blurredBannerUrl;
        this.adsImageUrl = adsImageUrl;
        this.adsLink = adsLink;
        this.adsName = adsName;
        this.adsId = adsId;
        this.bannerName = bannerName;
        this.groupChatToken = groupChatToken;
        this.adminName = adminName;
        this.image = image;
        this.adminPicture = adminPicture;
        this.description = description;
        this.totalView = totalView;
        this.channelPartnerViewModels = channelPartnerViewModels;
        this.voteInfoViewModel = voteInfoViewModel;
        this.sprintSaleViewModel = sprintSaleViewModel;
        this.bannedMessage = bannedMessage;
        this.kickedMessage = kickedMessage;
        this.isFreeze = isFreeze;
        this.pinnedMessageViewModel = pinnedMessageViewModel;
        this.exitMessage = exitMessage;
        this.quickRepliesViewModel = quickRepliesViewModel;
        this.videoId = videoId;
        this.videoLive = videoLive;
        this.settingGroupChat = settingGroupChat;
        this.overlayViewModel = overlayViewModel;
        this.dynamicButtons = dynamicButtons;
        this.backgroundViewModel = backgroundViewModel;
    }

    protected ChannelInfoViewModel(Parcel in) {
        channelId = in.readString();
        title = in.readString();
        channelUrl = in.readString();
        bannerUrl = in.readString();
        blurredBannerUrl = in.readString();
        adsImageUrl = in.readString();
        adsLink = in.readString();
        adsId = in.readString();
        adsName = in.readString();
        bannerName = in.readString();
        groupChatToken = in.readString();
        adminName = in.readString();
        image = in.readString();
        adminPicture = in.readString();
        description = in.readString();
        totalView = in.readString();
        channelPartnerViewModels = in.createTypedArrayList(ChannelPartnerViewModel.CREATOR);
        bannedMessage = in.readString();
        kickedMessage = in.readString();
        isFreeze = in.readByte() != 0;
        videoId = in.readString();
        byte tmpVideoLive = in.readByte();
        videoLive = tmpVideoLive == 0 ? null : tmpVideoLive == 1;
        settingGroupChat = in.readParcelable(SettingGroupChat.class.getClassLoader());
        overlayViewModel = in.readParcelable(OverlayViewModel.class.getClassLoader());
        dynamicButtons = in.readParcelable(DynamicButtonsViewModel.class.getClassLoader());
        backgroundViewModel = in.readParcelable(BackgroundViewModel.class.getClassLoader());
        voteInfoViewModel = in.readParcelable(VoteInfoViewModel.class.getClassLoader());
        sprintSaleViewModel = in.readParcelable(SprintSaleViewModel.class.getClassLoader());
        groupChatPointsViewModel = in.readParcelable(GroupChatPointsViewModel.class.getClassLoader());
        pinnedMessageViewModel = in.readParcelable(PinnedMessageViewModel.class.getClassLoader());
        exitMessage = in.readParcelable(ExitMessage.class.getClassLoader());
        quickRepliesViewModel = in.createTypedArrayList(GroupChatQuickReplyItemViewModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(channelId);
        dest.writeString(title);
        dest.writeString(channelUrl);
        dest.writeString(bannerUrl);
        dest.writeString(blurredBannerUrl);
        dest.writeString(adsImageUrl);
        dest.writeString(adsLink);
        dest.writeString(adsId);
        dest.writeString(adsName);
        dest.writeString(bannerName);
        dest.writeString(groupChatToken);
        dest.writeString(adminName);
        dest.writeString(image);
        dest.writeString(adminPicture);
        dest.writeString(description);
        dest.writeString(totalView);
        dest.writeTypedList(channelPartnerViewModels);
        dest.writeString(bannedMessage);
        dest.writeString(kickedMessage);
        dest.writeByte((byte) (isFreeze ? 1 : 0));
        dest.writeString(videoId);
        dest.writeByte((byte) (videoLive == null ? 0 : videoLive ? 1 : 2));
        dest.writeParcelable(settingGroupChat, flags);
        dest.writeParcelable(overlayViewModel, flags);
        dest.writeParcelable(dynamicButtons, flags);
        dest.writeParcelable(backgroundViewModel, flags);
        dest.writeParcelable(voteInfoViewModel, flags);
        dest.writeParcelable(sprintSaleViewModel, flags);
        dest.writeParcelable(groupChatPointsViewModel, flags);
        dest.writeParcelable(pinnedMessageViewModel, flags);
        dest.writeParcelable(exitMessage, flags);
        dest.writeTypedList(quickRepliesViewModel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChannelInfoViewModel> CREATOR = new Creator<ChannelInfoViewModel>() {
        @Override
        public ChannelInfoViewModel createFromParcel(Parcel in) {
            return new ChannelInfoViewModel(in);
        }

        @Override
        public ChannelInfoViewModel[] newArray(int size) {
            return new ChannelInfoViewModel[size];
        }
    };

    public String getChannelId() {
        return channelId;
    }

    public String getTitle() {
        return title;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public String getBlurredBannerUrl() {
        return blurredBannerUrl;
    }

    public String getAdsImageUrl() {
        return adsImageUrl;
    }

    public void setAdsImageUrl(String adsImageUrl) {
        this.adsImageUrl = adsImageUrl;
    }

    public String getAdsLink() {
        return adsLink;
    }

    public void setAdsLink(String adsLink) {
        this.adsLink = adsLink;
    }

    public String getBannerName() {
        return bannerName;
    }

    public String getGroupChatToken() {
        return groupChatToken;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getImage() {
        return image;
    }

    public String getAdminPicture() {
        return adminPicture;
    }

    public String getDescription() {
        return description;
    }

    void setTotalView(String totalView) {
        this.totalView = totalView;
    }

    public String getTotalView() {
        return totalView;
    }

    public List<ChannelPartnerViewModel> getChannelPartnerViewModels() {
        return channelPartnerViewModels;
    }

    @Nullable
    public VoteInfoViewModel getVoteInfoViewModel() {
        return voteInfoViewModel;
    }

    public void setVoteInfoViewModel(@Nullable VoteInfoViewModel voteInfoViewModel) {
        this.voteInfoViewModel = voteInfoViewModel;
    }

    @Nullable
    public SprintSaleViewModel getSprintSaleViewModel() {
        return sprintSaleViewModel;
    }

    public void setSprintSaleViewModel(@Nullable SprintSaleViewModel sprintSaleViewModel) {
        this.sprintSaleViewModel = sprintSaleViewModel;
    }

    @Nullable
    public GroupChatPointsViewModel getGroupChatPointsViewModel() {
        return groupChatPointsViewModel;
    }

    public void setGroupChatPointsViewModel(@Nullable GroupChatPointsViewModel groupChatPointsViewModel) {
        this.groupChatPointsViewModel = groupChatPointsViewModel;
    }

    public String getAdsId() {
        return adsId;
    }

    public void setAdsId(String adsId) {
        this.adsId = adsId;
    }

    public String getAdsName() {
        return adsName;
    }

    public void setAdsName(String adsName) {
        this.adsName = adsName;
    }

    public String getBannedMessage() {
        return bannedMessage;
    }

    public String getKickedMessage() {
        return kickedMessage;
    }

    public boolean isFreeze() {
        return isFreeze;
    }

    @Nullable
    public PinnedMessageViewModel getPinnedMessageViewModel() {
        return pinnedMessageViewModel;
    }

    public void setPinnedMessageViewModel(@Nullable PinnedMessageViewModel pinnedMessageViewModel) {
        this.pinnedMessageViewModel = pinnedMessageViewModel;
    }

    @Nullable
    public ExitMessage getExitMessage() {
        return exitMessage;
    }

    @Nullable
    public List<GroupChatQuickReplyItemViewModel> getQuickRepliesViewModel() {
        return quickRepliesViewModel;
    }

    public void setQuickRepliesViewModel(@Nullable List<GroupChatQuickReplyItemViewModel> quickRepliesViewModel) {
        this.quickRepliesViewModel = quickRepliesViewModel;
    }

    public String getVideoId() {
        return videoId;
    }

    public Boolean isVideoLive() {
        return videoLive;
    }


    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public SettingGroupChat getSettingGroupChat() {
        return settingGroupChat;
    }

    public OverlayViewModel getOverlayViewModel() {
        return overlayViewModel;
    }


    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public void setBlurredBannerUrl(String blurredBannerUrl) {
        this.blurredBannerUrl = blurredBannerUrl;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public void setGroupChatToken(String groupChatToken) {
        this.groupChatToken = groupChatToken;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAdminPicture(String adminPicture) {
        this.adminPicture = adminPicture;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChannelPartnerViewModels(List<ChannelPartnerViewModel> channelPartnerViewModels) {
        this.channelPartnerViewModels = channelPartnerViewModels;
    }

    public void setBannedMessage(String bannedMessage) {
        this.bannedMessage = bannedMessage;
    }

    public void setKickedMessage(String kickedMessage) {
        this.kickedMessage = kickedMessage;
    }

    public void setFreeze(boolean freeze) {
        isFreeze = freeze;
    }

    public void setSettingGroupChat(SettingGroupChat settingGroupChat) {
        this.settingGroupChat = settingGroupChat;
    }

    public void setOverlayViewModel(OverlayViewModel overlayViewModel) {
        this.overlayViewModel = overlayViewModel;
    }

    public void setExitMessage(@Nullable ExitMessage exitMessage) {
        this.exitMessage = exitMessage;
    }
    public DynamicButtonsViewModel getDynamicButtons() {
        return dynamicButtons;
    }

    public void setDynamicButtons(DynamicButtonsViewModel dynamicButtons) {
        this.dynamicButtons = dynamicButtons;
    }

    public BackgroundViewModel getBackgroundViewModel() {
        return backgroundViewModel;
    }
}
