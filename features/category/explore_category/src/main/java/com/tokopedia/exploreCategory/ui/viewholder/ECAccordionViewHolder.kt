package com.tokopedia.exploreCategory.ui.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.exploreCategory.R
import com.tokopedia.exploreCategory.adapter.ECServiceAdapter
import com.tokopedia.exploreCategory.adapter.ECServiceAdapterFactory
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.ECAccordionVHViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class ECAccordionViewHolder(itemView: View,
                            private val accordionListener: AccordionListener?)
    : AbstractViewHolder<ECAccordionVHViewModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.ec_accordion_view_layout

        const val COLUMN_COUNT = 4
    }

    private val adapter: ECServiceAdapter = ECServiceAdapter(ECServiceAdapterFactory(null))

    private var ecTypographyTitle = itemView.findViewById<Typography>(R.id.ec_typography_title)
    private var ecRecyclerView = itemView.findViewById<RecyclerView>(R.id.ec_icons_recycler_view)
    private var ecIconChevron = itemView.findViewById<ImageView>(R.id.ec_icon_chevron)
    private var ecAccordion = itemView.findViewById<View>(R.id.ec_accordion)

    override fun bind(element: ECAccordionVHViewModel?) {
        val layoutManager = GridLayoutManager(itemView.context, COLUMN_COUNT)
        adapter.setVisitables(ArrayList())

        ecTypographyTitle?.text = element?.categoryGroup?.title
        ecRecyclerView?.layoutManager = layoutManager
        ecRecyclerView?.adapter = adapter
        adapter.addMoreData(element?.getRows())
        handleAccordion(element?.categoryGroup?.isOpen)

        ecAccordion?.setOnClickListener {
            accordionListener?.onAccordionClick(adapterPosition)
        }
    }

    private fun handleAccordion(isOpen: Boolean?) {
        if (isOpen == false) {
            ecIconChevron?.rotation = 0f
            ecRecyclerView?.hide()
        } else {
            ecIconChevron?.rotation = 180f
            ecRecyclerView?.show()
        }
    }

    interface AccordionListener {
        fun onAccordionClick(categoryIndex: Int)
    }
}