package com.tokopedia.play.view.type

/**
 * @author by astidhiyaa on 18/02/22
 */
enum class CampaignReminderType {
    ON,
    OFF,
    NotAvailable
}

fun getCampaignReminderType(isReminder: Boolean): CampaignReminderType = if (isReminder) CampaignReminderType.ON else CampaignReminderType.OFF