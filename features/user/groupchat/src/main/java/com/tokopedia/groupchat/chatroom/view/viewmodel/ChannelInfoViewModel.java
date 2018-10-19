package com.tokopedia.groupchat.chatroom.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.tokopedia.groupchat.chatroom.domain.pojo.ExitMessage;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChannelPartnerViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatPointsViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyItemViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PinnedMessageViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.groupchat.vote.view.model.VoteInfoViewModel;

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
                                String videoId) {
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
        dest.writeString(this.bannedMessage);
        dest.writeString(this.kickedMessage);
        dest.writeByte(this.isFreeze ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.voteInfoViewModel, flags);
        dest.writeParcelable(this.sprintSaleViewModel, flags);
        dest.writeParcelable(this.groupChatPointsViewModel, flags);
        dest.writeParcelable(this.pinnedMessageViewModel, flags);
        dest.writeParcelable(this.exitMessage, flags);
        dest.writeTypedList(this.quickRepliesViewModel);
        dest.writeString(this.videoId);
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
        this.bannedMessage = in.readString();
        this.kickedMessage = in.readString();
        this.isFreeze = in.readByte() != 0;
        this.voteInfoViewModel = in.readParcelable(VoteInfoViewModel.class.getClassLoader());
        this.sprintSaleViewModel = in.readParcelable(SprintSaleViewModel.class.getClassLoader());
        this.groupChatPointsViewModel = in.readParcelable(GroupChatPointsViewModel.class.getClassLoader());
        this.pinnedMessageViewModel = in.readParcelable(PinnedMessageViewModel.class.getClassLoader());
        this.exitMessage = in.readParcelable(ExitMessage.class.getClassLoader());
        this.quickRepliesViewModel = in.createTypedArrayList(GroupChatQuickReplyItemViewModel.CREATOR);
        this.videoId = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    public void setQuickRepliesViewModel(@Nullable List<GroupChatQuickReplyItemViewModel> quickRepliesViewModel) {
        this.quickRepliesViewModel = quickRepliesViewModel;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
