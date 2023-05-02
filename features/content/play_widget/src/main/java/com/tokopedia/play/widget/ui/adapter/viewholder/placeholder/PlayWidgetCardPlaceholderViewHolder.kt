package com.tokopedia.play.widget.ui.adapter.viewholder.placeholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play_common.util.extension.updateLayoutParams
import com.tokopedia.unifycomponents.LoaderUnify

/**
 * Created by jegul on 12/10/20
 */
class PlayWidgetCardPlaceholderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val loaderImage: LoaderUnify = itemView.findViewById(R.id.loader_image)
    private val loaderDetail1: LoaderUnify = itemView.findViewById(R.id.loader_detail_1)
    private val loaderDetail2: LoaderUnify = itemView.findViewById(R.id.loader_detail_2)
    private val loaderDetail3: LoaderUnify = itemView.findViewById(R.id.loader_detail_3)

    fun bind() {
        loaderImage.type = LoaderUnify.TYPE_RECT
        loaderDetail1.type = LoaderUnify.TYPE_LINE
        loaderDetail2.type = LoaderUnify.TYPE_LINE
        loaderDetail3.type = LoaderUnify.TYPE_LINE
    }

    fun setType(type: Type){
        val (mWidth, mHeight) = with(itemView.resources){
               when(type) {
                   Type.MEDIUM -> Pair(getDimensionPixelSize(R.dimen.play_widget_card_medium_width), itemView.resources.getDimensionPixelSize(R.dimen.play_widget_card_medium_height))
                   Type.LARGE -> Pair(getDimensionPixelSize(R.dimen.play_widget_card_large_width), itemView.resources.getDimensionPixelSize(R.dimen.play_widget_card_large_height))
               }
           }

        itemView.rootView.updateLayoutParams {
            width = mWidth
            height = mHeight
        }
    }

    enum class Type {
        MEDIUM,
        LARGE
    }

    companion object {
        val layout = R.layout.item_play_widget_card_placeholder
    }
}
