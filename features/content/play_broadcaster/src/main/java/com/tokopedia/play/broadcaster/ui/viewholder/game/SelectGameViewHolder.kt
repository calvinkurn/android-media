package com.tokopedia.play.broadcaster.ui.viewholder.game

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.databinding.ItemSelectGameListBinding
import com.tokopedia.play.broadcaster.ui.model.game.GameType
import com.tokopedia.play.broadcaster.view.adapter.SelectGameAdapter

/**
 * Created By : Jonathan Darwin on March 29, 2022
 */
class SelectGameViewHolder private constructor(
    private val binding: ItemSelectGameListBinding,
    private val listener: Listener,
) : BaseViewHolder(binding.root) {

    fun bind(gameType: GameType) {
        binding.root.apply {
            setGameType(gameType)
            setListener {
                listener.onGameOptionClick(gameType)
            }
        }
    }

    interface Listener {
        fun onGameOptionClick(gameType: GameType)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener,
        ) : SelectGameViewHolder {
            return SelectGameViewHolder(
                ItemSelectGameListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }
}