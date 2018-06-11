package com.tokopedia.groupchat.chatroom.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.tokopedia.groupchat.chatroom.domain.pojo.ExitMessage;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChannelPartnerViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatPointsViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PinnedMessageViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.groupchat.vote.view.model.VoteInfoViewModel;

import java.util.List;

/**
 * @author by nisie on 2/22/18.
 */

public class ChannelInfoViewModel implements Parcelable {
    private String title;
    private String channelUrl;
    private String bannerUrl;
    private String blurredBannerUrl;
    private String adsImageUrl;
    private String adsLink;
    private String adsId;
    private String adsName;
    private String bannerName;
    private String sendBirdToken;
    private String adminName;
    private String image;
    private String adminPicture;
    private String description;
    private String totalView;
    private List<ChannelPartnerViewModel> channelPartnerViewModels;
    private String bannedMessage;
    private String kickedMessage;
    private boolean isFreeze;

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

    public ChannelInfoViewModel(String title, String channelUrl, String bannerUrl,
                                String blurredBannerUrl,
                                String adsImageUrl, String adsLink, String adsName, String adsId,
                                String bannerName, String sendBirdToken, String adminName, String image,
                                String adminPicture, String description, String totalView,
                                List<ChannelPartnerViewModel> channelPartnerViewModels,
                                @Nullable VoteInfoViewModel voteInfoViewModel,
                                @Nullable SprintSaleViewModel sprintSaleViewModel,
                                String bannedMessage, String kickedMessage, boolean isFreeze,
                                @Nullable PinnedMessageViewModel pinnedMessageViewModel,
                                @Nullable ExitMessage exitMessage) {
        this.title = title;
        this.channelUrl = channelUrl;
        this.bannerUrl = bannerUrl;
        this.blurredBannerUrl = blurredBannerUrl;
        this.adsImageUrl = adsImageUrl;
        this.adsLink = adsLink;
        this.adsName = adsName;
        this.adsId = adsId;
        this.bannerName = bannerName;
        this.sendBirdToken = sendBirdToken;
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
    }

    protected ChannelInfoViewModel(Parcel in) {
        title = in.readString();
        channelUrl = in.readString();
        bannerUrl = in.readString();
        blurredBannerUrl = in.readString();
        adsImageUrl = in.readString();
        adsLink = in.readString();
        adsId = in.readString();
        adsName = in.readString();
        bannerName = in.readString();
        sendBirdToken = in.readString();
        adminName = in.readString();
        image = in.readString();
        adminPicture = in.readString();
        description = in.readString();
        totalView = in.readString();
        channelPartnerViewModels = in.createTypedArrayList(ChannelPartnerViewModel.CREATOR);
        bannedMessage = in.readString();
        kickedMessage = in.readString();
        isFreeze = in.readByte() != 0;
        voteInfoViewModel = in.readParcelable(VoteInfoViewModel.class.getClassLoader());
        sprintSaleViewModel = in.readParcelable(SprintSaleViewModel.class.getClassLoader());
        groupChatPointsViewModel = in.readParcelable(GroupChatPointsViewModel.class.getClassLoader());
        pinnedMessageViewModel = in.readParcelable(PinnedMessageViewModel.class.getClassLoader());
        exitMessage = in.readParcelable(ExitMessage.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(channelUrl);
        dest.writeString(bannerUrl);
        dest.writeString(blurredBannerUrl);
        dest.writeString(adsImageUrl);
        dest.writeString(adsLink);
        dest.writeString(adsId);
        dest.writeString(adsName);
        dest.writeString(bannerName);
        dest.writeString(sendBirdToken);
        dest.writeString(adminName);
        dest.writeString(image);
        dest.writeString(adminPicture);
        dest.writeString(description);
        dest.writeString(totalView);
        dest.writeTypedList(channelPartnerViewModels);
        dest.writeString(bannedMessage);
        dest.writeString(kickedMessage);
        dest.writeByte((byte) (isFreeze ? 1 : 0));
        dest.writeParcelable(voteInfoViewModel, flags);
        dest.writeParcelable(sprintSaleViewModel, flags);
        dest.writeParcelable(groupChatPointsViewModel, flags);
        dest.writeParcelable(pinnedMessageViewModel, flags);
        dest.writeParcelable(exitMessage, flags);
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

    public String getSendBirdToken() {
        return sendBirdToken;
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

    @Nullable
    public ExitMessage getExitMessage() {
        return exitMessage;
    }
}
