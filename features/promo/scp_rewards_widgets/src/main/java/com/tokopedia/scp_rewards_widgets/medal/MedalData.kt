package com.tokopedia.scp_rewards_widgets.medal

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class MedalData(
    val title: String? = null,
    val description: String? = null,
    val textColor: String? = null,
    val medalList: List<MedalItem>? = null
) : Visitable<MedalViewTypeFactory> {
    override fun type(typeFactory: MedalViewTypeFactory): Int = typeFactory.type(this)
}

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
    val isEarned: Boolean = false,
    val isPlaceHolder: Boolean = false,
) : Visitable<MedalViewTypeFactory> {
    override fun type(typeFactory: MedalViewTypeFactory): Int = typeFactory.type(this)

}

data class Cta(
    val appLink: String? = null,
    val deepLink: String? = null
)
