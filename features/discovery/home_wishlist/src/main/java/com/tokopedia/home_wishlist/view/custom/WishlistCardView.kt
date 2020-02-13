package com.tokopedia.home_wishlist.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.constraintlayout.widget.Barrier
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.view.ext.setSafeOnClickListener
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyButton.Variant.FILLED
import com.tokopedia.unifycomponents.UnifyButton.Variant.GHOST
import com.tokopedia.unifyprinciples.Typography

class WishlistCardView : ProductCardView{

    private var deleteActionButton: FrameLayout ? = null
    private var addToCardActionButton: UnifyButton? = null
    private var barrier: Barrier? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun getLayout(): Int {
        return R.layout.wishlist_card_view
    }

    override fun findViews(inflatedView: View) {
        deleteActionButton = inflatedView.findViewById(R.id.deleteAction)
        addToCardActionButton = inflatedView.findViewById(R.id.textViewAddToCart)
        barrier = inflatedView.findViewById(R.id.barrier)
        cardViewProductCard = inflatedView.findViewById(R.id.cardViewProductCard)
        constraintLayoutProductCard = inflatedView.findViewById(R.id.constraintLayoutProductCard)
        imageProduct = inflatedView.findViewById(R.id.imageProduct)
        buttonWishlist = inflatedView.findViewById(R.id.buttonWishlist)
        labelPromo = inflatedView.findViewById(R.id.labelPromo)
        textViewShopName = inflatedView.findViewById(R.id.textViewShopName)
        textViewProductName = inflatedView.findViewById(R.id.textViewProductName)
        labelDiscount = inflatedView.findViewById(R.id.labelDiscount)
        textViewSlashedPrice = inflatedView.findViewById(R.id.textViewSlashedPrice)
        textViewPrice = inflatedView.findViewById(R.id.textViewPrice)
        linearLayoutShopBadges = inflatedView.findViewById(R.id.linearLayoutShopBadges)
        textViewShopLocation = inflatedView.findViewById(R.id.textViewShopLocation)
        linearLayoutImageRating = inflatedView.findViewById(R.id.linearLayoutImageRating)
        imageViewRating1 = inflatedView.findViewById(R.id.imageViewRating1)
        imageViewRating2 = inflatedView.findViewById(R.id.imageViewRating2)
        imageViewRating3 = inflatedView.findViewById(R.id.imageViewRating3)
        imageViewRating4 = inflatedView.findViewById(R.id.imageViewRating4)
        imageViewRating5 = inflatedView.findViewById(R.id.imageViewRating5)
        textViewReviewCount = inflatedView.findViewById(R.id.textViewReviewCount)
        labelCredibility = inflatedView.findViewById(R.id.labelCredibility)
        imageFreeOngkirPromo = inflatedView.findViewById(R.id.imageFreeOngkirPromo)
        labelOffers = inflatedView.findViewById(R.id.labelOffers)
        imageTopAds = inflatedView.findViewById(R.id.imageTopAds)
    }

    override fun realignLayout() {
        barrier?.referencedIds = intArrayOf(
                R.id.textViewAddToCart,R.id.textViewShopName,R.id.imageTopAds,R.id.labelDiscount,R.id.labelOffers,R.id.textViewSlashedPrice,R.id.deleteAction,R.id.buttonWishlist,R.id.labelPromo,R.id.textViewProductName,R.id.cardViewImageProduct,R.id.textViewPrice,R.id.textViewReviewCount,R.id.labelCredibility,R.id.textViewShopLocation,R.id.linearLayoutShopBadges,R.id.linearLayoutImageRating,R.id.imageFreeOngkirPromo
        )
        super.realignLayout()
    }

    fun setDeleteButtonVisible(isVisible: Boolean){
        deleteActionButton?.visibility = if(isVisible) View.VISIBLE else View.GONE
    }

    fun setDeleteButtonOnClickListener(clickListener: () -> Unit){
        deleteActionButton?.setSafeOnClickListener { clickListener.invoke() }
    }

    fun setAddToCartButtonVisible(isVisible: Boolean){
        addToCardActionButton?.visibility = if(isVisible) View.VISIBLE else View.GONE
    }

    fun setAddToCartButtonOnClickListener(clickListener: () -> Unit){
        addToCardActionButton?.setSafeOnClickListener{ clickListener.invoke() }
    }

    fun disableAddToCartButton(){
        addToCardActionButton?.isEnabled = false
        addToCardActionButton?.text = "Tambah ke Keranjang"
    }

    fun setOutOfStock(){
        addToCardActionButton?.isEnabled = false
        addToCardActionButton?.buttonVariant = FILLED
        addToCardActionButton?.text = "Stok Habis"
    }

    fun enableAddToCartButton(){
        addToCardActionButton?.isEnabled = true
        addToCardActionButton?.buttonVariant = GHOST
        addToCardActionButton?.text = "Tambah ke Keranjang"
    }

    fun setOnClickListener(clickListener: () -> Unit){
        cardViewProductCard?.setSafeOnClickListener { clickListener.invoke() }
    }
}