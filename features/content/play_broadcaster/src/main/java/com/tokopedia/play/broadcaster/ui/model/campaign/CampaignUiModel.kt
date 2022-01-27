package com.tokopedia.play.broadcaster.ui.model.campaign

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
data class CampaignUiModel(
    val id: String,
    val title: String,
    val imageUrl: String,
    val startDateFmt: String,
    val endDateFmt: String,
    val status: CampaignStatusUiModel,
    val totalProduct: Int,
)

data class CampaignStatusUiModel(
    val status: CampaignStatus,
    val text: String,
)

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
}