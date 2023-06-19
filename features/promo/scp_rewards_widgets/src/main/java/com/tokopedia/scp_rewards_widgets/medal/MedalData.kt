package com.tokopedia.scp_rewards_widgets.medal

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.scp_rewards_widgets.constants.MedalType

data class MedalData(
    val title: String? = null,
    val description: String? = null,
    val textColor: String? = null,
    val medalType: MedalType = MedalType.None,
    val medalList: List<MedalItem>? = null,
    val bannerData: BannerData? = null,
    val cta: Cta? = null
) : Visitable<MedalViewTypeFactory> {
    override fun type(typeFactory: MedalViewTypeFactory): Int = typeFactory.type(this)
}

data class BannerData(
    val imageUrl: String? = null,
    val webLink: String? = null,
    val appLink: String? = null
)

data class MedalItem(
    val id: Int? = null,
    val name: String? = null,
    val provider: String? = null,
    val extraInfo: String? = null,
    val imageUrl: String? = null,
    val celebrationUrl: String? = null,
    val isNewMedal: Boolean? = false,
    val isDisabled: Boolean? = false,
    val progression: Int? = 0,
    val cta: Cta? = null,
    val medalType: MedalType = MedalType.None,
    val isPlaceHolder: Boolean = false
) : Visitable<MedalViewTypeFactory> {

    fun isEarned() = medalType == MedalType.Earned

    fun isNewlyEarned() = isEarned() && isNewMedal == true
    override fun type(typeFactory: MedalViewTypeFactory): Int = typeFactory.type(this)
}

data class Cta(
    val text: String? = null,
    val isShown: Boolean? = false,
    val appLink: String? = null,
    val deepLink: String? = null
)
