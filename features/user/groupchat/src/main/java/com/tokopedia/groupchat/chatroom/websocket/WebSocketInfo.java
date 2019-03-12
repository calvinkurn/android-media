package com.tokopedia.groupchat.chatroom.websocket;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.chatroom.domain.pojo.AdminMsg;
import com.tokopedia.groupchat.chatroom.domain.pojo.EventHandlerPojo;
import com.tokopedia.groupchat.chatroom.domain.pojo.GeneratedMessagePojo;
import com.tokopedia.groupchat.chatroom.domain.pojo.ParticipantPojo;
import com.tokopedia.groupchat.chatroom.domain.pojo.PinnedMessagePojo;
import com.tokopedia.groupchat.chatroom.domain.pojo.UserMsg;
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.Channel;
import com.tokopedia.groupchat.chatroom.domain.pojo.imageannouncement.AdminImagePojo;
import com.tokopedia.groupchat.chatroom.domain.pojo.poll.ActivePollPojo;
import com.tokopedia.groupchat.chatroom.domain.pojo.poll.Option;
import com.tokopedia.groupchat.chatroom.domain.pojo.poll.StatisticOption;
import com.tokopedia.groupchat.chatroom.domain.pojo.sprintsale.FlashSalePojo;
import com.tokopedia.groupchat.chatroom.domain.pojo.sprintsale.Product;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.AdsViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.EventGroupChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GeneratedMessageViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyItemViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.GroupChatQuickReplyViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ImageAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ParticipantViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.PinnedMessageViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleProductViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.VibrateViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.VideoViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.WebSocketResponse;
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayCloseViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel;
import com.tokopedia.groupchat.vote.view.model.VoteInfoViewModel;
import com.tokopedia.groupchat.vote.view.model.VoteViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.WebSocket;
import okio.ByteString;

/**
 * Created by dhh on 2017/9/21.
 */

public class WebSocketInfo {

    private static final int DEFAULT_NO_POLL = 0;
    private static final String OPTION_TEXT = "Text";
    private static final String OPTION_IMAGE = "Image";
    private static final String FORMAT_DISCOUNT_LABEL = "%d%% OFF";

    private WebSocket mWebSocket;
    private WebSocketResponse response;
    private Visitable item;
    private String mString;
    private ByteString mByteString;
    private boolean onOpen;
    private boolean onReconnect;
    private String error;

    private WebSocketInfo() {
    }

    WebSocketInfo(WebSocket webSocket, boolean onOpen) {
        mWebSocket = webSocket;
        this.onOpen = onOpen;
    }

    WebSocketInfo(WebSocket webSocket, ByteString byteString) {
        mWebSocket = webSocket;
        mByteString = byteString;
    }

    WebSocketInfo(WebSocket webSocket, String data) {
        mWebSocket = webSocket;
        this.mString = data;
        this.response = new GsonBuilder().create().fromJson(data, WebSocketResponse.class);
        this.item = mapping(response);
    }

    private Visitable mapping(WebSocketResponse response) {
        JsonObject data = response.getData();
        if (response.getType() == null) {
            return null;
        }
        switch (this.response.getType().toLowerCase()) {
            case VoteAnnouncementViewModel.POLLING_START:
            case VoteAnnouncementViewModel.POLLING_FINISHED:
            case VoteAnnouncementViewModel.POLLING_END:
            case VoteAnnouncementViewModel.POLLING_CANCEL:
            case VoteAnnouncementViewModel.POLLING_UPDATE:
                return mapToPollingViewModel(response, data);
            case ChatViewModel.ADMM:
                return mapToAdminChat(data);
            case ImageAnnouncementViewModel.ADMIN_ANNOUNCEMENT:
                return mapToAdminImageChat(data);
            case SprintSaleAnnouncementViewModel.SPRINT_SALE_UPCOMING:
            case SprintSaleAnnouncementViewModel.SPRINT_SALE_START:
            case SprintSaleAnnouncementViewModel.SPRINT_SALE_FINISH:
                return mapToSprintSale(data, response);
            case VibrateViewModel.TYPE:
                return new VibrateViewModel();
            case GeneratedMessageViewModel.TYPE:
                return mapToGeneratedMessage(data);
            case PinnedMessageViewModel.TYPE:
                return mapToPinnedMessage(data);
            case AdsViewModel.TYPE:
                return mapToAds(data);
            case GroupChatQuickReplyViewModel.TYPE:
                return mapToQuickReply(data);
            case VideoViewModel.TYPE:
                return mapToVideo(data);
            case ChatViewModel.USER_MESSAGE:
                return mapToUserChat(data);
            case EventHandlerPojo.BANNED:
            case EventHandlerPojo.FREEZE:
                return mapToEventHandler(data);
            case ParticipantViewModel.TYPE:
                return mapToParticipant(data);
            case OverlayViewModel.TYPE:
                return mapToOverlay(data);
            case OverlayCloseViewModel.TYPE:
                return mapToOverlayClose(data);
            default:
                return null;
        }
    }

    private Visitable mapToParticipant(JsonObject data) {
        Gson gson = new Gson();
        ParticipantPojo pojo = gson.fromJson(data, ParticipantPojo.class);
        return new ParticipantViewModel(pojo.getChannelId(), pojo.getTotalView());
    }

    private Visitable mapToEventHandler(JsonObject data) {
        Gson gson = new Gson();
        EventHandlerPojo pojo = gson.fromJson(data, EventHandlerPojo.class);
        return new EventGroupChatViewModel(pojo.isFreeze(), pojo.isBanned(), pojo.getChannelId(), pojo.getUserId());
    }

    private Visitable mapToGeneratedMessage(JsonObject data) {
        Gson gson = new Gson();
        GeneratedMessagePojo pojo = gson.fromJson(data, GeneratedMessagePojo.class);

        return new GeneratedMessageViewModel(
                pojo.getMessage(),
                pojo.getTimestamp(),
                pojo.getTimestamp(),
                String.valueOf(pojo.getMessageId()),
                pojo.getUser().getId(),
                pojo.getUser().getName(),
                pojo.getUser().getImage(),
                false,
                true
        );
    }

    private Visitable mapToSprintSale(JsonObject data, WebSocketResponse response) {
        Gson gson = new Gson();
        FlashSalePojo pojo = gson.fromJson(data, FlashSalePojo.class);

        return new SprintSaleAnnouncementViewModel(
                pojo.getCampaignId() != null ? pojo.getCampaignId() : "",
                pojo.getTimestamp(),
                pojo.getTimestamp(),
                String.valueOf(pojo.getMessageId()),
                pojo.getUser().getId(),
                pojo.getUser().getName(),
                pojo.getUser().getImage(),
                false,
                true,
                pojo.getAppLink() != null ? pojo.getAppLink() : "",
                mapToListSprintSaleProducts(pojo.getCampaignId(), pojo.getProducts()),
                pojo.getCampaignName() != null ? pojo.getCampaignName() : "",
                pojo.getStartDate() * 1000L,
                pojo.getEndDate() * 1000L,
                response.getType()
        );
    }

    private ArrayList<SprintSaleProductViewModel> mapToListSprintSaleProducts(String campaignId, List<Product> pojo) {
        ArrayList<SprintSaleProductViewModel> list = new ArrayList<>();
        for (Product product : pojo) {
            list.add(mapToSprintSaleProduct(campaignId, product));
        }
        return list;
    }

    private SprintSaleProductViewModel mapToSprintSaleProduct(String campaignId, Product product) {
        return new SprintSaleProductViewModel(
                campaignId != null ? campaignId : "",
                product.getProductId() != null ? product.getProductId() : "",
                product.getName() != null ? product.getName() : "",
                product.getImageUrl() != null ? product.getImageUrl() : "",
                String.format(Locale.getDefault(), FORMAT_DISCOUNT_LABEL, product.getDiscountPercentage()),
                product.getDiscountedPrice() != null ? product.getDiscountedPrice() : "",
                product.getOriginalPrice() != null ? product.getOriginalPrice() : "",
                product.getRemainingStockPercentage(),
                product.getStockText() != null ? product.getStockText() : "",
                product.getUrlMobile() != null ? product.getUrlMobile() : "");
    }

    private Visitable mapToUserChat(JsonObject data) {
        Gson gson = new Gson();
        UserMsg pojo = gson.fromJson(data, UserMsg.class);

        return new ChatViewModel(
                pojo.getMessage(),
                pojo.getTimestamp(),
                pojo.getTimestamp(),
                String.valueOf(pojo.getMessageId()),
                pojo.getUser().getId(),
                pojo.getUser().getName(),
                pojo.getUser().getImage(),
                false,
                false
        );
    }

    private Visitable mapToAdminImageChat(JsonObject data) {
        Gson gson = new Gson();
        AdminImagePojo pojo = gson.fromJson(data, AdminImagePojo.class);

        return new ImageAnnouncementViewModel(
                pojo.getImageId(),
                pojo.getImageUrl().trim(),
                pojo.getTimestamp(),
                pojo.getTimestamp(),
                String.valueOf(pojo.getMessageId()),
                pojo.getUser().getId(),
                pojo.getUser().getName(),
                pojo.getUser().getImage(),
                false,
                true,
                pojo.getRedirectUrl()
        );
    }

    private Visitable mapToAdminChat(JsonObject jsonObject) {
        Gson gson = new Gson();
        AdminMsg pojo = gson.fromJson(jsonObject, AdminMsg.class);

        return new ChatViewModel(
                pojo.getMessage(),
                pojo.getTimestamp(),
                pojo.getTimestamp(),
                String.valueOf(pojo.getMessageId()),
                pojo.getUser().getId(),
                pojo.getUser().getName(),
                pojo.getUser().getImage(),
                false,
                true
        );
    }

    private VoteAnnouncementViewModel mapToPollingViewModel(WebSocketResponse response, JsonObject data) {
        Gson gson = new Gson();
        ActivePollPojo pojo = gson.fromJson(data, ActivePollPojo.class);

        return new VoteAnnouncementViewModel(
                pojo.getDescription(),
                response.getType(),
                pojo.getTimestamp(),
                pojo.getTimestamp(),
                String.valueOf(""),
                pojo.getUser().getId(),
                pojo.getUser().getName(),
                pojo.getUser().getImage(),
                false,
                true,
                mappingToVoteInfoViewModel(pojo)
        );
    }

    private boolean hasPoll(ActivePollPojo activePoll) {
        return activePoll != null
                && activePoll.getStatistic() != null
                && activePoll.getPollId() != DEFAULT_NO_POLL;
    }

    private VoteInfoViewModel mappingToVoteInfoViewModel(ActivePollPojo activePollPojo) {
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
                    activePollPojo.getEndTime(),
                    activePollPojo.getVoteUrl()
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

    private Visitable mapToPinnedMessage(JsonObject json) {
        if (TextUtils.isEmpty(json.toString())) {
            return new PinnedMessageViewModel("", "", "", "");
        }
        Gson gson = new Gson();
        PinnedMessagePojo pinnedMessage = gson.fromJson(json, PinnedMessagePojo.class);
        return new PinnedMessageViewModel(pinnedMessage.getMessage(), pinnedMessage.getTitle(), pinnedMessage.getRedirectUrl(), pinnedMessage.getImageUrl());
    }

    private Visitable mapToAds(JsonObject json) {
        if (TextUtils.isEmpty(json.toString())) {
            return new AdsViewModel("", "", "");
        }
        Gson gson = new Gson();
        return gson.fromJson(json, AdsViewModel.class);
    }

    private Visitable mapToQuickReply(JsonObject json) {
        if (TextUtils.isEmpty(json.toString())) {
            return new GroupChatQuickReplyViewModel();
        }
        Gson gson = new Gson();
        Channel channel = gson.fromJson(json, Channel.class);
        GroupChatQuickReplyViewModel model = new GroupChatQuickReplyViewModel();

        List<GroupChatQuickReplyItemViewModel> list = new ArrayList<>();
        if (channel != null && channel.getListQuickReply() != null) {
            int id = 1;
            for (String quickReply : channel.getListQuickReply()) {
                GroupChatQuickReplyItemViewModel item = new GroupChatQuickReplyItemViewModel(String.valueOf(id), quickReply);
                list.add(item);
                id++;
            }
        }
        model.setList(list);

        return model;
    }

    private Visitable mapToVideo(JsonObject data) {
        if (TextUtils.isEmpty(data.toString())) {
            return new VideoViewModel("");
        }
        Gson gson = new Gson();
        return gson.fromJson(data, VideoViewModel.class);
    }

    private Visitable mapToOverlay(JsonObject data) {
        if (TextUtils.isEmpty(data.toString())) {
            return new OverlayViewModel();
        }
        Gson gson = new Gson();
        return gson.fromJson(data, OverlayViewModel.class);
    }

    private Visitable mapToOverlayClose(JsonObject data) {
        if (TextUtils.isEmpty(data.toString())) {
            return new OverlayCloseViewModel();
        }
        Gson gson = new Gson();
        return gson.fromJson(data, OverlayCloseViewModel.class);
    }

    static WebSocketInfo createReconnect() {
        return createReconnect("");
    }

    static WebSocketInfo createReconnect(String error) {
        WebSocketInfo socketInfo = new WebSocketInfo();
        socketInfo.onReconnect = true;
        socketInfo.error = error;
        return socketInfo;
    }

    public WebSocket getWebSocket() {
        return mWebSocket;
    }

    public void setWebSocket(WebSocket webSocket) {
        mWebSocket = webSocket;
    }

    @Nullable
    public String getString() {
        return mString;
    }

    public void setString(String string) {
        this.mString = string;
    }

    public WebSocketResponse getResponse() {
        return response;
    }

    public void setResponse(WebSocketResponse response) {
        this.response = response;
    }

    @Nullable
    public ByteString getByteString() {
        return mByteString;
    }

    public boolean isOnOpen() {
        return onOpen;
    }

    public boolean isOnReconnect() {
        return onReconnect;
    }

    public Visitable getItem() {
        return item;
    }

    public boolean shouldHideMessage(Visitable mappedMessage) {
        if (mappedMessage instanceof VoteAnnouncementViewModel
                && ((VoteAnnouncementViewModel) mappedMessage).getVoteType().equalsIgnoreCase(
                VoteAnnouncementViewModel.POLLING_CANCEL)) {
            return true;
        } else if (mappedMessage instanceof VoteAnnouncementViewModel
                && ((VoteAnnouncementViewModel) mappedMessage).getVoteType().equalsIgnoreCase
                (VoteAnnouncementViewModel.POLLING_UPDATE)) {
            return true;
        } else if (mappedMessage instanceof VibrateViewModel) {
            return true;
        } else if (mappedMessage instanceof SprintSaleAnnouncementViewModel
                && ((SprintSaleAnnouncementViewModel) mappedMessage).getSprintSaleType().equalsIgnoreCase(
                SprintSaleAnnouncementViewModel.SPRINT_SALE_UPCOMING)) {
            return true;
        } else if (mappedMessage instanceof PinnedMessageViewModel) {
            return true;
        } else if (mappedMessage instanceof AdsViewModel) {
            return true;
        } else if (mappedMessage instanceof GroupChatQuickReplyViewModel) {
            return true;
        } else if (mappedMessage instanceof VideoViewModel) {
            return true;
        } else if (mappedMessage instanceof EventGroupChatViewModel) {
            return true;
        } else if (mappedMessage instanceof ParticipantViewModel) {
            return true;
        } else if (mappedMessage instanceof OverlayViewModel) {
            return true;
        } else if (mappedMessage instanceof OverlayCloseViewModel) {
            return true;
        } else {
            return false;
        }
    }
}
