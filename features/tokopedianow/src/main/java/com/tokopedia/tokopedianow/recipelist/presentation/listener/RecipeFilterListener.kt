package com.tokopedia.tokopedianow.recipelist.presentation.listener

import android.content.Context
import android.content.Intent
import com.tokopedia.tokopedianow.recipehome.presentation.activity.TokoNowRecipeFilterActivity
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeChipFilterUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeChipFilterUiModel.ChipType.MORE_FILTER
import com.tokopedia.tokopedianow.recipelist.presentation.viewholder.RecipeChipFilterViewHolder.RecipeChipFilterListener

class RecipeFilterListener(private val context: Context?) : RecipeChipFilterListener {

    override fun onClickItem(filter: RecipeChipFilterUiModel) {
        if(filter.type == MORE_FILTER) {
            val intent = Intent(context, TokoNowRecipeFilterActivity::class.java)
            context?.startActivity(intent)
        }
    }
}