package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter

/**
 * Created By : Jonathan Darwin on March 29, 2022
 */
class SelectGameAdapter(

) : BaseDiffUtilAdapter<SelectGameAdapter.Model>(){

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        if(oldItem is Model.Game && newItem is Model.Game)
            return oldItem.type == newItem.type
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    sealed class Model {
        data class Game(val type: String): Model()
    }
}