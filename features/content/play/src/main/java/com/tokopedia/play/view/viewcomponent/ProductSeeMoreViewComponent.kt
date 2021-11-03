package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By : Jonathan Darwin on November 02, 2021
 */
class ProductSeeMoreViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int,
    private val listener: Listener
) : ViewComponent(container, idRes) {

    private val tvProductCount = findViewById<Typography>(R.id.tv_play_product_count)
    private val icProductSeeMore = findViewById<AppCompatImageView>(R.id.ic_play_product_see_more)

    init {
        rootView.setOnClickListener { listener.onProductSeeMoreClick(this) }
    }

    fun setTotalProduct(total: Int) {
        tvProductCount.text = total.toString()
    }

    interface Listener {
        fun onProductSeeMoreClick(view: ProductSeeMoreViewComponent)
    }
}