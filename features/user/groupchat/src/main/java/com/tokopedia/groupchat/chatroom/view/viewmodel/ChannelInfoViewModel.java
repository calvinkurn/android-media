package com.tokopedia.groupchat.chatroom.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.tokopedia.groupchat.chatroom.domain.pojo.ExitMessage;
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.SettingGroupChat;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.BackgroundViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.BanViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChannelPartnerViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatPointsViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyItemViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.KickViewModel;
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
    private BanViewModel banViewModel;
    private KickViewModel kickViewModel;
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
        this.banViewModel = null;
        this.kickViewModel = null;
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
                                BanViewModel banViewModel, KickViewModel kickViewModel, boolean isFreeze,
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
        this.banViewModel = banViewModel;
        this.kickViewModel = kickViewModel;
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
        return "";
    }

    public String getKickedMessage() {
        return "";
    }

    public BanViewModel getBanViewModel() {
        return banViewModel;
    }

    public KickViewModel getKickViewModel() {
        return kickViewModel;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.channelId);
        dest.writeString(this.title);
        dest.writeString(this.channelUrl);
        dest.writeString(this.bannerUrl);
        dest.writeString(this.blurredBannerUrl);
        dest.writeString(this.adsImageUrl);
        dest.writeString(this.adsLink);
        dest.writeString(this.adsId);
        dest.writeString(this.adsName);
        dest.writeString(this.bannerName);
        dest.writeString(this.groupChatToken);
        dest.writeString(this.adminName);
        dest.writeString(this.image);
        dest.writeString(this.adminPicture);
        dest.writeString(this.description);
        dest.writeString(this.totalView);
        dest.writeTypedList(this.channelPartnerViewModels);
        dest.writeParcelable(this.banViewModel, flags);
        dest.writeParcelable(this.kickViewModel, flags);
        dest.writeByte(this.isFreeze ? (byte) 1 : (byte) 0);
        dest.writeString(this.videoId);
        dest.writeByte(this.videoLive ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.settingGroupChat, flags);
        dest.writeParcelable(this.overlayViewModel, flags);
        dest.writeParcelable(this.voteInfoViewModel, flags);
        dest.writeParcelable(this.sprintSaleViewModel, flags);
        dest.writeParcelable(this.groupChatPointsViewModel, flags);
        dest.writeParcelable(this.pinnedMessageViewModel, flags);
        dest.writeParcelable(this.exitMessage, flags);
        dest.writeTypedList(this.quickRepliesViewModel);
        dest.writeParcelable(this.dynamicButtons, flags);
    }

    protected ChannelInfoViewModel(Parcel in) {
        this.channelId = in.readString();
        this.title = in.readString();
        this.channelUrl = in.readString();
        this.bannerUrl = in.readString();
        this.blurredBannerUrl = in.readString();
        this.adsImageUrl = in.readString();
        this.adsLink = in.readString();
        this.adsId = in.readString();
        this.adsName = in.readString();
        this.bannerName = in.readString();
        this.groupChatToken = in.readString();
        this.adminName = in.readString();
        this.image = in.readString();
        this.adminPicture = in.readString();
        this.description = in.readString();
        this.totalView = in.readString();
        this.channelPartnerViewModels = in.createTypedArrayList(ChannelPartnerViewModel.CREATOR);
        this.banViewModel = in.readParcelable(BanViewModel.class.getClassLoader());
        this.kickViewModel = in.readParcelable(KickViewModel.class.getClassLoader());
        this.isFreeze = in.readByte() != 0;
        this.videoId = in.readString();
        this.videoLive = in.readByte() != 0;
        this.settingGroupChat = in.readParcelable(SettingGroupChat.class.getClassLoader());
        this.overlayViewModel = in.readParcelable(OverlayViewModel.class.getClassLoader());
        this.voteInfoViewModel = in.readParcelable(VoteInfoViewModel.class.getClassLoader());
        this.sprintSaleViewModel = in.readParcelable(SprintSaleViewModel.class.getClassLoader());
        this.groupChatPointsViewModel = in.readParcelable(GroupChatPointsViewModel.class.getClassLoader());
        this.pinnedMessageViewModel = in.readParcelable(PinnedMessageViewModel.class.getClassLoader());
        this.exitMessage = in.readParcelable(ExitMessage.class.getClassLoader());
        this.quickRepliesViewModel = in.createTypedArrayList(GroupChatQuickReplyItemViewModel.CREATOR);
        this.dynamicButtons = in.readParcelable(DynamicButtonsViewModel.class.getClassLoader());
    }

    public static final Creator<ChannelInfoViewModel> CREATOR = new Creator<ChannelInfoViewModel>() {
        @Override
        public ChannelInfoViewModel createFromParcel(Parcel source) {
            return new ChannelInfoViewModel(source);
        }

        @Override
        public ChannelInfoViewModel[] newArray(int size) {
            return new ChannelInfoViewModel[size];
        }
    };

}
