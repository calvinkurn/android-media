package com.tokopedia.entertainment.pdp.common.util

import android.annotation.SuppressLint
import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.entertainment.home.utils.DateUtils
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.Participant
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.ParticipantDetail
import com.tokopedia.entertainment.pdp.uimodel.ParticipantTitleUiModel
import com.tokopedia.entertainment.pdp.uimodel.ParticipantUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import java.text.SimpleDateFormat
import java.util.Date
import com.tokopedia.entertainment.R.string as stringRedeem

/**
 * Author firmanda on 17,Nov,2022
 */

object EventRedeemMapper {

    private const val REDEEM_TIME_FORMAT = "dd MMM yyyy HH:mm"
    private const val REDEEM_RAW_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"

    fun participantToVisitableMapper(mainTitle: String, participants: List<Participant>, context: Context): List<Visitable<*>> {
        val participantList = mutableListOf<Visitable<*>>()
        val participantGrouping = participants.groupBy {
            it.day
        }

        val participantSize = participantGrouping.size
        participantGrouping.map {
            participantList.add(
                ParticipantTitleUiModel(
                    id = it.key.toString(),
                    day = it.key,
                    title = if (participantSize > Int.ONE) dayTitle(it.key, context) else mainTitle,
                    isChecked = isAllChecked(it.value),
                    isDisabled = isAllParticipantDisabled(it.value)
                )
            )

            it.value.forEach {
                participantList.add(
                    ParticipantUiModel(
                        id = it.id,
                        day = it.day,
                        title = it.participantDetails.firstOrNull()?.value ?: "",
                        subTitle = participantsListMapping(it.participantDetails, context),
                        isChecked = it.checked,
                        isDisabled = it.redemptionTime.isMoreThanZero(),
                        redeemTime = getRedemptionTime(context, it.redemptionTime)
                    )
                )
            }
        }

        return participantList
    }

    fun isStatusNotAllDisabled(participants: List<Participant>): Boolean {
        return participants.firstOrNull {
            it.redemptionTime.isZero()
        } != null
    }

    fun isEmptyParticipant(participants: List<Participant>): Boolean {
        return participants.isEmpty()
    }

    fun isOneParticipant(participants: List<Participant>): Boolean {
        return participants.size == Int.ONE
    }

    fun getCheckedIdsSize(participants: List<Participant>): Int {
        var size = 0
        participants.forEach{ participant ->
            if (participant.checked) {
                size += Int.ONE
            }
        }

        return size
    }

    fun getCheckedIds(participants: List<Participant>): List<Int> {
        val list = participants.filter { participant ->
            participant.checked
        }.map {
            it.id.toIntSafely()
        }
        return list
    }

    fun updateCheckedIds(participants: List<Participant>, listCheckedIds: List<Pair<String, Boolean>>) {
        participants.forEachIndexed { _, participant ->
            val listCheckedId = listCheckedIds.firstOrNull {
                it.first == participant.id
            }
            if (listCheckedId != null) {
                participant.checked = listCheckedId.second
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getAllRedeemedTime(context: Context, redemptionTime: String): String {
        val parser = SimpleDateFormat(REDEEM_RAW_TIME_FORMAT)
        val formatter = SimpleDateFormat(REDEEM_TIME_FORMAT)
        val formattedDate = formatter.format(parser.parse(redemptionTime)) ?: ""

        return context.resources.getString(
            stringRedeem.ent_redeem_success_all_redeem_time,
            formattedDate
        )
    }

    fun getOneRedemptionPair(participants: List<Participant>): Pair<String, Boolean>? {
        val redemption = participants.firstOrNull()
        return redemption?.run {
            Pair(id, true)
        }
    }

    fun getListParticipantDetails(participants: List<Participant>): List<ParticipantDetail>{
        val redemption = participants.firstOrNull()
        return redemption?.participantDetails ?: emptyList<ParticipantDetail>()
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

    private fun getRedemptionTime(context: Context, redemptionTime: Int): String {
        return if (redemptionTime.isMoreThanZero()) {
            context.resources.getString(stringRedeem.ent_redeem_success_redeem_time,
                DateUtils.dateToString(
                    Date(redemptionTime.toLong() * DateUtils.SECOND_IN_MILIS),
                    REDEEM_TIME_FORMAT
                )
            )
        } else {
            ""
        }
    }

    private fun isAllParticipantDisabled(participants: List<Participant>): Boolean {
        var isAllParticipantDisable = true

        run breaking@ {
            participants.forEach {
                if (it.redemptionTime.isZero()) {
                    isAllParticipantDisable = false
                    return@breaking
                }
            }
        }

        return isAllParticipantDisable
    }

    private fun isAllChecked(participants: List<Participant>): Boolean {
        var isAllChecked = true

        run breaking@ {
            participants.forEach {
                if (!it.checked) {
                    isAllChecked = false
                    return@breaking
                }
            }
        }

        return isAllChecked
    }
}
