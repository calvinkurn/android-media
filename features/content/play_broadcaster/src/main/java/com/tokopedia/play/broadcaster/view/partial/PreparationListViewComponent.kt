package com.tokopedia.play.broadcaster.view.partial

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created By : Jonathan Darwin on January 24, 2022
 */
class PreparationListViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container, R.id.cl_preparation_list) {

    private val clSetTitle = findViewById<ConstraintLayout>(R.id.cl_bro_set_title)
    private val clSetCover = findViewById<ConstraintLayout>(R.id.cl_bro_set_cover)
    private val clSetProduct = findViewById<ConstraintLayout>(R.id.cl_bro_set_product)

    private val icSetTitleChecked = findViewById<IconUnify>(R.id.ic_bro_title_checked)
    private val icSetCoverChecked = findViewById<IconUnify>(R.id.ic_bro_cover_checked)
    private val icSetProductChecked = findViewById<IconUnify>(R.id.ic_bro_product_checked)

    init {
        clSetTitle.setOnClickListener { listener.onClickSetTitle() }
        clSetCover.setOnClickListener { listener.onClickSetCover() }
        clSetProduct.setOnClickListener { listener.onClickSetProduct() }
    }

    fun isSetTitleChecked(isChecked: Boolean) {
        icSetTitleChecked.visibility = if(isChecked) View.VISIBLE else View.INVISIBLE
    }

    fun isSetCoverChecked(isChecked: Boolean) {
        icSetCoverChecked.visibility = if(isChecked) View.VISIBLE else View.INVISIBLE
    }

    fun isSetProductChecked(isChecked: Boolean) {
        icSetProductChecked.visibility = if(isChecked) View.VISIBLE else View.INVISIBLE
    }

    interface Listener {
        fun onClickSetTitle()
        fun onClickSetCover()
        fun onClickSetProduct()
    }
}