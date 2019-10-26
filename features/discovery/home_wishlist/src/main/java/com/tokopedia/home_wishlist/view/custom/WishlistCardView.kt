package com.tokopedia.home_wishlist.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import com.tokopedia.home_wishlist.R
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.unifyprinciples.Typography

class WishlistCardView : ProductCardView{

    private var deleteActionButton: FrameLayout ? = null
    private var addToCardActionButton: Typography? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun getLayout(): Int {
        return R.layout.wishlist_card_view
    }

    override fun findViews(inflatedView: View) {
        super.findViews(inflatedView)
        deleteActionButton = inflatedView.findViewById(R.id.deleteAction)
        addToCardActionButton = inflatedView.findViewById(R.id.textViewAddToCart)
    }

    fun setDeleteButtonVisible(isVisible: Boolean){
        deleteActionButton?.visibility = if(isVisible) View.VISIBLE else View.GONE
    }

    fun setDeleteButtonOnClickListener(clickListener: OnClickListener){
        deleteActionButton?.setOnClickListener(clickListener)
    }

    fun setAddToCardButtonVisible(isVisible: Boolean){
        addToCardActionButton?.visibility = if(isVisible) View.VISIBLE else View.GONE
    }

    fun setAddToCardButtonOnClickListener(clickListener: OnClickListener){
        addToCardActionButton?.setOnClickListener(clickListener)
    }

    override fun setOnClickListener(clickListener: OnClickListener){
        cardViewProductCard?.setOnClickListener(clickListener)
    }
}