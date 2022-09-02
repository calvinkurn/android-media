package com.tokopedia.play.broadcaster.ui.model.game

data class GameParticipantUiModel(
    val id :String,
    val name:String,
    val imageUrl:String,
    val isWinner:Boolean,
)
