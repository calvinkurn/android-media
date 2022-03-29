package com.tokopedia.play.broadcaster.ui.model.game

/**
 * Created By : Jonathan Darwin on March 29, 2022
 */
sealed class GameType {
    abstract val name: String

    object Taptap: GameType() {
        override val name: String
            get() = "Game Hadiah"
    }

    object Quiz: GameType() {
        override val name: String
            get() = "Quiz"
    }

    object Unknown: GameType() {
        override val name: String
            get() = ""
    }
}