package com.tokopedia.groupchat.chatroom.domain.mapper;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.groupchat.chatroom.domain.pojo.ButtonsPojo;
import com.tokopedia.groupchat.chatroom.domain.pojo.PinnedMessagePojo;
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.Channel;
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.ChannelInfoPojo;
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.Flashsale;
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.ListBrand;
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.ListOfficial;
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.Product;
import com.tokopedia.groupchat.chatroom.domain.pojo.poll.ActivePollPojo;
import com.tokopedia.groupchat.chatroom.domain.pojo.poll.Option;
import com.tokopedia.groupchat.chatroom.domain.pojo.poll.StatisticOption;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChannelPartnerChildViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChannelPartnerViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyItemViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PinnedMessageViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleProductViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel;
import com.tokopedia.groupchat.vote.view.model.VoteInfoViewModel;
import com.tokopedia.groupchat.vote.view.model.VoteViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 2/22/18.
 */

public class ChannelInfoMapper implements Func1<Response<DataResponse<ChannelInfoPojo>>, ChannelInfoViewModel> {

    private static final String OPTION_TEXT = "Text";
    private static final String OPTION_IMAGE = "Image";

    private static final int DEFAULT_NO_POLL = 0;
    private static final String FORMAT_DISCOUNT_LABEL = "%d%% OFF";

    @Inject
    public ChannelInfoMapper() {
    }

    @Override
    public ChannelInfoViewModel call(Response<DataResponse<ChannelInfoPojo>> response) {
        ChannelInfoPojo pojo = response.body().getData();
        return new ChannelInfoViewModel(
                String.valueOf(pojo.getChannel().getChannelId()),
                pojo.getChannel().getTitle() != null ? pojo.getChannel().getTitle() : "",
                pojo.getChannel().getChannelUrl() != null ? pojo.getChannel().getChannelUrl() : "",
                pojo.getChannel().getCoverUrl() != null ? pojo.getChannel().getCoverUrl() : "",
                pojo.getChannel().getBannerBlurredUrl() != null ? pojo.getChannel().getBannerBlurredUrl() : "",
                pojo.getChannel().getAdsImageUrl() != null ? pojo.getChannel().getAdsImageUrl() : "",
                pojo.getChannel().getAdsLink() != null ? pojo.getChannel().getAdsLink() : "",
                pojo.getChannel().getAdsName() != null ? pojo.getChannel().getAdsName() : "",
                pojo.getChannel().getAdsId() != null ? pojo.getChannel().getAdsId() : "",
                pojo.getChannel().getBannerName() != null ? pojo.getChannel().getBannerName() : "",
                pojo.getChannel().getGcToken() != null ? pojo.getChannel().getGcToken() : "",
                pojo.getChannel().getModeratorName() != null ? pojo.getChannel().getModeratorName() : "",
                pojo.getChannel().getCoverUrl() != null ? pojo.getChannel().getCoverUrl() : "",
                pojo.getChannel().getModeratorProfileUrl() != null ? pojo.getChannel().getModeratorProfileUrl() : "",
                pojo.getChannel().getDescription() != null ? pojo.getChannel().getDescription() : "",
                pojo.getChannel().getTotalViews() != null ? pojo.getChannel().getTotalViews() : "",
                convertChannelPartner(pojo.getChannel()),
                mapToVoteViewModel(pojo.getChannel().getActivePolls()),
                mapToSprintSaleViewModel(pojo.getChannel().getFlashsale()),
                pojo.getChannel().getBannedMessage() != null ? pojo.getChannel().getBannedMessage() : "",
                pojo.getChannel().getKickedMessage() != null ? pojo.getChannel().getKickedMessage() : "",
                pojo.getChannel().isIsFreeze(),
                mapToPinnedMessageViewModel(pojo.getChannel().getPinnedMessage()),
                pojo.getChannel().getExitMessage(),
                convertChannelQuickReply(pojo.getChannel()),
                pojo.getChannel().getVideoId(),
                pojo.getChannel().getVideoLive(),
                "",
                pojo.getChannel().getSettingGroupChat(),
                pojo.getChannel().getOverlayMessage(),
                convertDynamicButtons(pojo.getChannel().getButton()),
                pojo.getChannel().getBackgroundViewModel()
        );
    }

    private DynamicButtonsViewModel convertDynamicButtons(ButtonsPojo button) {
        DynamicButtonsViewModel dynamicButtonsViewModel = new DynamicButtonsViewModel();
        if (button.getFloatingButton() != null) {
            dynamicButtonsViewModel.setFloatingButton(new DynamicButtonsViewModel.Button(
                    button.getFloatingButton().getImageUrl(),
                    button.getFloatingButton().getLinkUrl(),
                    button.getFloatingButton().getContentType(),
                    button.getFloatingButton().getContentText(),
                    button.getFloatingButton().getContentLinkUrl(),
                    button.getFloatingButton().getContentImageUrl(),
                    button.getFloatingButton().getRedDot(),
                    button.getFloatingButton().getTooltip()
            ));
        }

        if (button.getListDynamicButton() != null) {
            for (ButtonsPojo.Button buttonItem : button.getListDynamicButton()) {
                dynamicButtonsViewModel.getListDynamicButton().add(
                        new DynamicButtonsViewModel.Button(
                                buttonItem.getImageUrl(),
                                buttonItem.getLinkUrl(),
                                buttonItem.getContentType(),
                                buttonItem.getContentText(),
                                buttonItem.getContentLinkUrl(),
                                buttonItem.getContentImageUrl(),
                                buttonItem.getRedDot(),
                                buttonItem.getTooltip()
                        )
                );
            }
        }

        return dynamicButtonsViewModel;
    }

    private PinnedMessageViewModel mapToPinnedMessageViewModel(PinnedMessagePojo pinnedMessage) {
        if (hasPinnedMessage(pinnedMessage)) {
            return new PinnedMessageViewModel(pinnedMessage.getMessage(), pinnedMessage.getTitle(), pinnedMessage.getRedirectUrl(), pinnedMessage.getImageUrl());
        } else {
            return null;
        }
    }

    private boolean hasPinnedMessage(PinnedMessagePojo pinnedMessage) {
        return pinnedMessage != null;
    }

    private SprintSaleViewModel mapToSprintSaleViewModel(Flashsale flashsale) {
        if (hasSprintSale(flashsale)) {
            return new SprintSaleViewModel(
                    flashsale.getCampaignId() != null ? flashsale.getCampaignId() : "",
                    mapToListFlashSaleProducts(flashsale.getCampaignId(), flashsale.getProducts()),
                    flashsale.getCampaignName() != null ? flashsale.getCampaignName() : "",
                    flashsale.getStartDate() * 1000L,
                    flashsale.getEndDate() * 1000L,
                    flashsale.getAppLink() != null ? flashsale.getAppLink() : "",
                    flashsale.getStatus() != null ? flashsale.getStatus() : ""
            );
        } else {
            return null;
        }
    }

    private boolean hasSprintSale(Flashsale flashsale) {
        return flashsale != null
                && flashsale.getProducts() != null
                && !flashsale.getProducts().isEmpty();
    }

    private ArrayList<SprintSaleProductViewModel> mapToListFlashSaleProducts(String campaignId, List<Product> products) {
        ArrayList<SprintSaleProductViewModel> list = new ArrayList<>();
        for (Product product : products) {
            list.add(mapToFlashSaleProduct(campaignId, product));
        }
        return list;
    }

    private SprintSaleProductViewModel mapToFlashSaleProduct(String campaignId, Product product) {
        return new SprintSaleProductViewModel(
                campaignId != null ? campaignId : "",
                product.getProductId() != null ? product.getProductId() : "",
                product.getName() != null ? product.getName() : "",
                product.getImageUrl() != null ? product.getImageUrl() : "",
                String.format(Locale.getDefault(), FORMAT_DISCOUNT_LABEL, product
                        .getDiscountPercentage()),
                product.getDiscountedPrice() != null ? product.getDiscountedPrice() : "",
                product.getOriginalPrice() != null ? product.getOriginalPrice() : "",
                product.getRemainingStockPercentage(),
                product.getStockText() != null ? product.getStockText() : "",
                product.getUrlMobile() != null ? product.getUrlMobile() : "");
    }


    private boolean hasPoll(ActivePollPojo activePoll) {
        return activePoll != null
                && activePoll.getStatistic() != null
                && activePoll.getPollId() != DEFAULT_NO_POLL;
    }

    private VoteInfoViewModel mapToVoteViewModel(ActivePollPojo activePollPojo) {
        if (hasPoll(activePollPojo)) {
            return new VoteInfoViewModel(
                    String.valueOf(activePollPojo.getPollId()),
                    activePollPojo.getTitle(),
                    activePollPojo.getQuestion(),
                    mapToListOptions(activePollPojo.isIsAnswered(),
                            activePollPojo.getOptionType(),
                            activePollPojo.getStatistic().getStatisticOptions(),
                            activePollPojo.getOptions()),
                    String.valueOf(activePollPojo.getStatistic().getTotalVoter()),
                    activePollPojo.getPollType(),
                    getVoteOptionType(activePollPojo.getOptionType()),
                    activePollPojo.getStatus(),
                    activePollPojo.getStatusId(),
                    activePollPojo.isIsAnswered(),
                    VoteInfoViewModel.getStringVoteInfo(activePollPojo.getPollTypeId()),
                    activePollPojo.getWinnerUrl().trim(),
                    activePollPojo.getStartTime(),
                    activePollPojo.getEndTime()
            );
        } else {
            return null;
        }
    }

    private List<Visitable> mapToListOptions(boolean isAnswered, String optionType,
                                             List<StatisticOption> statisticOptions,
                                             List<Option> options) {
        List<Visitable> list = new ArrayList<>();
        for (int i = 0; i < statisticOptions.size(); i++) {

            StatisticOption statisticOptionPojo = statisticOptions.get(i);
            Option optionPojo = options.get(i);

            if (optionType.equalsIgnoreCase(OPTION_TEXT)) {
                list.add(new VoteViewModel(
                        String.valueOf(statisticOptionPojo.getOptionId()),
                        statisticOptionPojo.getOption(),
                        statisticOptionPojo.getPercentage(),
                        checkIfSelected(isAnswered, statisticOptionPojo.isIsSelected())
                ));
            } else if (optionType.equalsIgnoreCase(OPTION_IMAGE)) {
                list.add(new VoteViewModel(
                        String.valueOf(statisticOptionPojo.getOptionId()),
                        statisticOptionPojo.getOption(),
                        optionPojo.getImageOption().trim(),
                        statisticOptionPojo.getPercentage(),
                        checkIfSelected(isAnswered, statisticOptionPojo.isIsSelected())
                ));
            }

        }

        return list;
    }

    private int checkIfSelected(boolean isAnswered, boolean isSelected) {
        if (isAnswered && isSelected) {
            return VoteViewModel.SELECTED;
        } else if (isAnswered) {
            return VoteViewModel.UNSELECTED;
        } else {
            return VoteViewModel.DEFAULT;
        }
    }

    private String getVoteOptionType(String type) {
        if (type.equalsIgnoreCase(OPTION_IMAGE)) {
            return VoteViewModel.IMAGE_TYPE;
        }
        return VoteViewModel.BAR_TYPE;
    }

    private List<ChannelPartnerViewModel> convertChannelPartner(Channel channel) {
        ArrayList<ChannelPartnerViewModel> channelPartnerViewModelList = new ArrayList<>();

        if (channel.getListOfficials() != null) {
            for (ListOfficial official : channel.getListOfficials()) {

                ChannelPartnerViewModel channelPartnerViewModel = new ChannelPartnerViewModel(
                        official.getTitle(),
                        convertChannelPartnerChild(official)
                );

                channelPartnerViewModelList.add(channelPartnerViewModel);
            }
        }

        return channelPartnerViewModelList;
    }

    private List<GroupChatQuickReplyItemViewModel> convertChannelQuickReply(Channel channel) {
        ArrayList<GroupChatQuickReplyItemViewModel> list = new ArrayList<>();

        if (channel.getListQuickReply() != null) {
            int id = 1;
            for (String quickReply : channel.getListQuickReply()) {
                GroupChatQuickReplyItemViewModel item = new GroupChatQuickReplyItemViewModel(String.valueOf(id), quickReply);
                list.add(item);
                id++;
            }
        }
        return list;
    }

    private List<ChannelPartnerChildViewModel> convertChannelPartnerChild(ListOfficial official) {
        ArrayList<ChannelPartnerChildViewModel> childViewModelList = new ArrayList<>();

        for (ListBrand brand : official.getListBrands()) {
            ChannelPartnerChildViewModel childViewModel = new ChannelPartnerChildViewModel(
                    brand.getBrandId(),
                    brand.getImageUrl(),
                    brand.getTitle(),
                    brand.getBrandUrl()
            );
            childViewModelList.add(childViewModel);
        }

        return childViewModelList;
    }

//    private OverlayViewModel convertOverlayModel(OverlayMessagePojo pojo) {
//        return new OverlayViewModel(
//                pojo.getTypeMessage(),
//                pojo.get
//                pojo.isCloseable(),
//                pojo.getStatus(),
//                convertInteruptViewModel(pojo.getAssets())
//        );
//    }
//
//    private InteruptViewModel convertInteruptViewModel(OverlayMessageAssetPojo pojo) {
//        return new InteruptViewModel(
//                pojo.getTitle(),
//                pojo.getDescription(),
//                pojo.getImageUrl(),
//                pojo.getImageLink(),
//                pojo.getBtnTitle(),
//                pojo.getBtnLink()
//        );
//    }
}
