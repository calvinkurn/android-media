package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.presentation.adapter.SellerFeatureAdapterTypeFactory
import com.tokopedia.seller_migration_common.presentation.adapter.viewholder.CardSellerFeatureViewHolder
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import kotlinx.android.synthetic.main.seller_feature_carousel.view.*

class SellerFeatureCarousel(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private var listener: SellerFeatureClickListener? = null
    private var recyclerViewListener: RecyclerViewListener? = null
    private var adapter: BaseAdapter<SellerFeatureAdapterTypeFactory>? = null

    init {
        inflate(context, R.layout.seller_feature_carousel, this)
    }

    fun toggleTitle(show: Boolean) {
        tvSellerFeatureCarousel.showWithCondition(show)
        labelSellerFeatureCarousel.showWithCondition(show)
    }

    fun setItems(items: List<SellerFeatureUiModel>) {
        adapter = BaseAdapter(SellerFeatureAdapterTypeFactory(listener))
        rvSellerMigrationProductFeatures.adapter = adapter
        adapter?.setElements(items)
    }

    fun toggleDivider(show: Boolean) {
        divider.showWithCondition(show)
    }

    fun setListener(listener: SellerFeatureClickListener) {
        this.listener = listener
    }

    fun setRecyclerViewListener(listener: RecyclerViewListener) {
        this.recyclerViewListener = listener
        rvSellerMigrationProductFeatures.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    // all recyclerview item inflated
                    recyclerViewListener?.onRecyclerViewBindFinished()
                    rvSellerMigrationProductFeatures.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
    }

    fun setRecyclerViewLayoutManager(manager: RecyclerView.LayoutManager) {
        rvSellerMigrationProductFeatures.layoutManager = manager
    }

    fun addItemDecoration() {
        rvSellerMigrationProductFeatures.addItemDecoration(CardSellerFeatureViewHolder.ItemDecoration(getDimens(R.dimen.dp_12)))
    }

    interface SellerFeatureClickListener {
        fun onSellerFeatureClicked(item: SellerFeatureUiModel)
    }

    interface RecyclerViewListener {
        fun onRecyclerViewBindFinished()
    }
}