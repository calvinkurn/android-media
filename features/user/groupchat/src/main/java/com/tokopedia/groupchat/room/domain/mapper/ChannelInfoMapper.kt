package com.tokopedia.groupchat.room.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.groupchat.chatroom.domain.pojo.OverlayMessageAssetPojo
import com.tokopedia.groupchat.chatroom.domain.pojo.OverlayMessagePojo
import com.tokopedia.groupchat.chatroom.domain.pojo.PinnedMessagePojo
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.*
import com.tokopedia.groupchat.chatroom.domain.pojo.poll.ActivePollPojo
import com.tokopedia.groupchat.chatroom.domain.pojo.poll.Option
import com.tokopedia.groupchat.chatroom.domain.pojo.poll.StatisticOption
import com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.*
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.InteruptViewModel
import com.tokopedia.groupchat.chatroom.view.viewmodel.interupt.OverlayViewModel
import com.tokopedia.groupchat.vote.view.model.VoteInfoViewModel
import com.tokopedia.groupchat.vote.view.model.VoteViewModel
import retrofit2.Response
import rx.functions.Func1
import java.util.*
import javax.inject.Inject

/**
 * @author by yfsx on 12/02/19.
 */
class ChannelInfoMapper @Inject constructor() : Func1<Response<DataResponse<ChannelInfoPojo>>, ChannelInfoViewModel> {
    private val OPTION_TEXT = "Text"
    private val OPTION_IMAGE = "Image"

    private val DEFAULT_NO_POLL = 0
    private val FORMAT_DISCOUNT_LABEL = "%d%% OFF"

    override fun call(response: Response<DataResponse<ChannelInfoPojo>>): ChannelInfoViewModel? {
        val pojo = response.body().data
        return pojo.channel?.let {
            ChannelInfoViewModel(
                    it.channelId.toString(),
                    it.title,
                    it.channelUrl,
                    it.coverUrl,
                    it.bannerBlurredUrl,
                    it.adsImageUrl,
                    it.adsLink,
                    it.adsName,
                    it.adsId,
                    it.bannerName,
                    it.gcToken,
                    it.moderatorName,
                    it.coverUrl,
                    it.moderatorProfileUrl,
                    it.description,
                    it.totalViews,
                    convertChannelPartner(it),
                    mapToVoteViewModel(it.activePolls),
                    mapToSprintSaleViewModel(it.flashsale),
                    it.bannedMessage,
                    it.kickedMessage,
                    it.isIsFreeze,
                    mapToPinnedMessageViewModel(it.pinnedMessage),
                    it.exitMessage,
                    convertChannelQuickReply(it),
                    it.videoId,
                    it.settingGroupChat,
                    convertOverlayModel(it.overlayMessage)
            )
        }
    }

    private fun mapToPinnedMessageViewModel(pinnedMessage: PinnedMessagePojo?): PinnedMessageViewModel? {
        return when {
            hasPinnedMessage(pinnedMessage) ->
                pinnedMessage?.let {
                    PinnedMessageViewModel(
                            it.message,
                            it.title,
                            it.redirectUrl,
                            it.imageUrl)
                }
            else -> null
        }

    }

    private fun hasPinnedMessage(pinnedMessage: PinnedMessagePojo?): Boolean {
        return pinnedMessage != null
    }

    private fun mapToSprintSaleViewModel(flashsale: Flashsale?): SprintSaleViewModel? {
        return when{
            hasSprintSale(flashsale) ->
                flashsale?.let {
                    SprintSaleViewModel(
                            it.campaignId,
                            mapToListFlashSaleProducts(it.campaignId, it.products),
                            it.campaignName,
                            it.startDate *1000L,
                            it.endDate * 1000L,
                            it.appLink,
                            it.status
                    )
                }
            else -> null
        }
    }

    private fun hasSprintSale(flashsale: Flashsale?): Boolean {
        return flashsale?.products?.isNotEmpty() ?: false
    }

    private fun mapToListFlashSaleProducts(campaignId: String?, products: List<Product>?): ArrayList<SprintSaleProductViewModel> {
        val list = ArrayList<SprintSaleProductViewModel>()
        for (product in products!!) {
            list.add(mapToFlashSaleProduct(campaignId, product))
        }
        return list
    }

    private fun mapToFlashSaleProduct(campaignId: String?, product: Product): SprintSaleProductViewModel {
        return product.let {
            SprintSaleProductViewModel(
                    campaignId,
                    it.productId,
                    it.name,
                    it.imageUrl,
                    String.format(Locale.getDefault(), FORMAT_DISCOUNT_LABEL, it.discountPercentage),
                    it.discountedPrice,
                    it.originalPrice,
                    it.remainingStockPercentage,
                    it.stockText,
                    it.urlMobile
            )
        }
    }


    private fun hasPoll(activePoll: ActivePollPojo?): Boolean {
        return activePoll?.statistic != null && activePoll.pollId != DEFAULT_NO_POLL
    }

    private fun mapToVoteViewModel(activePollPojo: ActivePollPojo?): VoteInfoViewModel? {
        return when{
            hasPoll(activePollPojo) ->
                activePollPojo?.let {
                    VoteInfoViewModel(
                            it.pollId.toString(),
                            it.title,
                            it.question,
                            mapToListOptions(it.isIsAnswered,
                                    it.optionType,
                                    it.statistic?.statisticOptions,
                                    it.options),
                            it.statistic?.totalVoter.toString(),
                            it.pollType,
                            getVoteOptionType(it.optionType),
                            it.status,
                            it.statusId,
                            it.isIsAnswered,
                            VoteInfoViewModel.getStringVoteInfo(it.pollTypeId),
                            it.winnerUrl.trim(),
                            it.startTime,
                            it.endTime
                    )
                }
            else -> null
        }
    }

    private fun mapToListOptions(isAnswered: Boolean, optionType: String,
                                 statisticOptions: List<StatisticOption>?,
                                 options: List<Option>): List<Visitable<*>> {
        val list = ArrayList<Visitable<*>>()
        for (i in statisticOptions!!.indices) {

            val statisticOptionPojo = statisticOptions[i]
            val optionPojo = options[i]

            if (optionType.equals(OPTION_TEXT, ignoreCase = true)) {
                list.add(VoteViewModel(
                        statisticOptionPojo.optionId.toString(),
                        statisticOptionPojo.option,
                        statisticOptionPojo.percentage,
                        checkIfSelected(isAnswered, statisticOptionPojo.isIsSelected)
                ))
            } else if (optionType.equals(OPTION_IMAGE, ignoreCase = true)) {
                list.add(VoteViewModel(
                        statisticOptionPojo.optionId.toString(),
                        statisticOptionPojo.option,
                        optionPojo.imageOption.trim(),
                        statisticOptionPojo.percentage,
                        checkIfSelected(isAnswered, statisticOptionPojo.isIsSelected)
                ))
            }
        }
        return list
    }

    private fun checkIfSelected(isAnswered: Boolean, isSelected: Boolean): Int {
        return when {
            isAnswered && isSelected -> VoteViewModel.SELECTED
            isAnswered -> VoteViewModel.UNSELECTED
            else -> VoteViewModel.DEFAULT
        }
    }

    private fun getVoteOptionType(type: String): String {
        return when {
            type.equals(OPTION_IMAGE, true) -> VoteViewModel.IMAGE_TYPE
            else -> VoteViewModel.BAR_TYPE
        }
    }

    private fun convertChannelPartner(channel: Channel): List<ChannelPartnerViewModel> {
        return channel.let {
            val channelPartnerViewModelList = ArrayList<ChannelPartnerViewModel>()
            it.listOfficials?.forEach { official ->
                channelPartnerViewModelList.add(
                        ChannelPartnerViewModel(
                                official.title,
                                convertChannelPartnerChild(official)
                        ))
            }
            channelPartnerViewModelList
        }

    }

    private fun convertChannelQuickReply(channel: Channel): List<GroupChatQuickReplyItemViewModel> {
        return channel.let {
            val list = ArrayList<GroupChatQuickReplyItemViewModel>()
            it.listQuickReply?.forEach { item ->
                var id: Int = 1
                list.add(GroupChatQuickReplyItemViewModel(id.toString(), item))
                id++

            }
            list
        }
    }


    private fun convertChannelPartnerChild(official: ListOfficial?): List<ChannelPartnerChildViewModel>? {
        return official?.let {
            val childViewModelList = ArrayList<ChannelPartnerChildViewModel>()

            it.listBrands?.forEach { brand ->
                val childViewModel = ChannelPartnerChildViewModel(
                        brand.brandId,
                        brand.imageUrl,
                        brand.title,
                        brand.brandUrl
                )
                childViewModelList.add(childViewModel)
            }
            childViewModelList
        }
    }

    private fun convertOverlayModel(pojo: OverlayMessagePojo): OverlayViewModel {
        return pojo.let {
            OverlayViewModel(
                    it.isCloseable,
                    it.status,
                    convertInteruptViewModel(it.assets)
            )
        }
    }

    private fun convertInteruptViewModel(pojo: OverlayMessageAssetPojo): InteruptViewModel {
        return pojo.let {
            InteruptViewModel(
                    it.title,
                    it.description,
                    it.imageUrl,
                    it.imageLink,
                    it.btnTitle,
                    it.btnLink
            )
        }
    }
}