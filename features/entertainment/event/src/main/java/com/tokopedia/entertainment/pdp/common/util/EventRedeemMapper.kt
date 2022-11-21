package com.tokopedia.entertainment.pdp.common.util

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.Participant
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.ParticipantDetail
import com.tokopedia.entertainment.pdp.uimodel.ParticipantTitleUiModel
import com.tokopedia.entertainment.pdp.uimodel.ParticipantUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.entertainment.R.string as stringRedeem

/**
 * Author firmanda on 17,Nov,2022
 */

object EventRedeemMapper {

    fun participantToVisitableMapper(participants: List<Participant>, context: Context): List<Visitable<*>> {
        val participantList = mutableListOf<Visitable<*>>()
        participants.groupBy {
            it.day
        }.map {
            participantList.add(
                ParticipantTitleUiModel(
                    id = it.key.toString(),
                    title = dayTitle(it.key, context)
                )
            )

            it.value.forEach {
                participantList.add(
                    ParticipantUiModel(
                       id = it.id,
                       title = it.participantDetails.first().value,
                       subTitle = participantsListMapping(it.participantDetails, context),
                       isChecked = it.checked
                    )
                )
            }
        }

        return participantList
    }

    private fun dayTitle(day: Int, context: Context) : String {
        return context.resources.getString(stringRedeem.ent_redeem_revamp_day, day)
    }

    private fun participantsListMapping(participantDetails: List<ParticipantDetail>, context: Context): String {
        var subtitle = ""
        if (participantDetails.size > Int.ONE) {
            participantDetails.drop(Int.ONE).forEach {
                subtitle += getSubtitle(context, it)
            }
        }
        return subtitle
    }

    private fun getSubtitle(context: Context, participantDetail: ParticipantDetail) : String {
        return context.resources.getString(stringRedeem.ent_redeem_revamp_subtitle,
            participantDetail.label, participantDetail.value)
    }

}
