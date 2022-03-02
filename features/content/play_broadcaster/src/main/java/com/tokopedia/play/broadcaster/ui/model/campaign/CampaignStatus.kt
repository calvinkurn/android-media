package com.tokopedia.play.broadcaster.ui.model.campaign

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
enum class CampaignStatus(val id: Int) {

    Ready(6),
    ReadyLocked(14),
    Ongoing(7),
    Unknown(-1);

    companion object {
        private val values = values()

        fun getById(id: Int): CampaignStatus {
            values.forEach {
                if (it.id == id) return it
            }
            return Unknown
        }
    }

    fun isUpcoming() = (this == Ready || this == ReadyLocked)
}