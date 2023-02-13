package com.tokopedia.catalog_library.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogMostViralDataModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogMostViralItemViewHolder(
    val view: View,
    private val catalogLibraryListener: CatalogLibraryListener
) : AbstractViewHolder<CatalogMostViralDataModel>(view) {

    private val mostViralImage: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_product_viral_image)
    }

    private val mostViralTitle: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_product_viral_title)
    }

    private val mostViralSubtitle: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_product_viral_category)
    }

    private val mostViralIcon: IconUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_most_viral_icon)
    }

    private val mostViralLayout: ConstraintLayout by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.catalog_most_viral_item_layout)
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_most_viral
    }

    override fun bind(element: CatalogMostViralDataModel?) {
        element?.catalogMostViralData?.imageUrl?.let { iconUrl ->
            mostViralImage.loadImage(iconUrl)
        }
        mostViralLayout.background =
            VectorDrawableCompat.create(
                view.context.resources,
                R.drawable.background,
                view.context.theme
            )
        mostViralTitle.text = element?.catalogMostViralData?.name
        mostViralIcon.apply {
            setImage(
                newLightEnable = MethodChecker.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Static_White
                )
            )
        }
        mostViralLayout.setOnClickListener {
            catalogLibraryListener.onProductCardClicked(element?.catalogMostViralData?.applink)
        }

        mostViralSubtitle.text = String.format(
            view.context.getString(R.string.most_viral_subtitle_common),
            element?.categoryName ?: ""
        )
    }
}
