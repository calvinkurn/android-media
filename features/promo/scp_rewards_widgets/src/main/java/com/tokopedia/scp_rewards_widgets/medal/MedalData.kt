package com.tokopedia.scp_rewards_widgets.medal

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.scp_rewards_common.EARNED_BADGE

data class MedalData(
    val id: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val textColor: String? = null,
    val medalType: String = "",
    val medalList: List<MedalItem>? = null,
    val bannerData: BannerData? = null,
    val cta: Cta? = null
) : Visitable<MedalViewTypeFactory> {

    fun isEarned() = medalType == EARNED_BADGE

    override fun type(typeFactory: MedalViewTypeFactory): Int = typeFactory.type(this)
}

data class BannerData(
    val imageUrl: String? = null,
    val webLink: String? = null,
    val appLink: String? = null,
    val creativeName: String? = null
)

data class MedalItem(
    val id: Int? = null,
    val name: String? = null,
    val provider: String? = null,
    val extraInfo: String? = null,
    val imageUrl: String? = null,
    val celebrationUrl: String? = null,
    val showConfetti: Boolean? = false,
    val isDisabled: Boolean? = false,
    val progression: Int? = 0,
    val cta: Cta? = null,
    val medalType: String = "",
    val isPlaceHolder: Boolean = false
) : Visitable<MedalViewTypeFactory> {

    fun isEarned() = medalType == EARNED_BADGE

    fun isNewlyEarned() = isEarned() && showConfetti == true
    override fun type(typeFactory: MedalViewTypeFactory): Int = typeFactory.type(this)
}

data class Cta(
    val text: String? = null,
    val isShown: Boolean? = false,
    val appLink: String? = null,
    val deepLink: String? = null
)

class MedalError(val imageUrl: String?) : Visitable<MedalViewTypeFactory> {
    override fun type(typeFactory: MedalViewTypeFactory) = typeFactory.type(this)
}
