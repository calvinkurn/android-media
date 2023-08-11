package com.tokopedia.oldcatalog.viewholder.containers

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.adapter.components.CatalogReviewAdapter
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.model.datamodel.CatalogReviewDataModel
import com.tokopedia.oldcatalog.model.util.CatalogUtil
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class CatalogReviewContainerViewHolder(private val view : View,
                                       private val catalogDetailListener: CatalogDetailListener): AbstractViewHolder<CatalogReviewDataModel>(view) {

    private val layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)

    companion object {
        val LAYOUT = R.layout.item_catalog_review_container
        const val MIN_SIZE_FOR_LIHAT_BUTTON = 3
    }

    override fun bind(element: CatalogReviewDataModel) {
        element.data.reviews?.let {
            val recyclerView = view.findViewById<RecyclerView>(R.id.review_rv_catalog)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = CatalogReviewAdapter(it.take(MIN_SIZE_FOR_LIHAT_BUTTON), catalogDetailListener,
                false, element.data.catalogName ?: "",element.data.catalogId ?: "")

            view.findViewById<Typography>(R.id.txt_see_all_reviews)?.let { lihatView ->
                if(it.size <= MIN_SIZE_FOR_LIHAT_BUTTON){
                    lihatView.hide()
                }else{
                    lihatView.show()
                    lihatView.setOnClickListener {
                        catalogDetailListener.readMoreReviewsClicked(element.data.catalogId ?: "")
                    }
                }
            }

            view.findViewById<Typography>(R.id.review_rating_catalog)?.displayTextOrHide(CatalogUtil.getRatingString(element.data.avgRating))
            if(element.data.avgRating.isNullOrBlank()){
                view.findViewById<IconUnify>(R.id.rating_review_star_catalog)?.hide()
            }else {
                view.findViewById<IconUnify>(R.id.rating_review_star_catalog)?.show()
            }
            view.findViewById<Typography>(R.id.review_count_catalog)?.displayTextOrHide(view.context?.resources?.getString(R.string.catalog_highlighted_reviews, element.data.totalHelpfulReview) ?: "")
        }
    }
}
