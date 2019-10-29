package com.tokopedia.discovery.categoryrevamp.catalogcard

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.discovery.R
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography


abstract class CatalogCardView : BaseCustomView {

    protected var cardViewProductCard: CardView? = null
    protected var imageCatalog: SquareImageView? = null
    protected var catalogCountLabel: Label? = null
    protected var catalogName: Typography? = null
    protected var catalogDescription: Typography? = null
    protected var catalogPrice: Typography? = null
    protected var mulaiDariText: Typography? = null


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }


    private fun init() {
        val inflatedView = View.inflate(context, getLayout(), this)

        findViews(inflatedView)

        postInit()
    }

    protected fun findViews(inflatedView: View) {
        cardViewProductCard = inflatedView.findViewById(R.id.cardViewCatalogCard)
        imageCatalog = inflatedView.findViewById(R.id.imageCatalog)
        catalogCountLabel = inflatedView.findViewById(R.id.label_catalog_count)
        catalogName = inflatedView.findViewById(R.id.textViewCatalogName)
        catalogDescription = inflatedView.findViewById(R.id.textViewCatalogDescription)
        catalogPrice = inflatedView.findViewById(R.id.textViewPrice)
        mulaiDariText = inflatedView.findViewById(R.id.text_mula_dari)
    }


    protected open fun postInit() {
        catalogName?.setLineSpacing(0f, 1f)
    }


    /**
     * Provides layout resource to be inflated in init() method.
     * Layouts could be different depending on its subclasses,
     * but all of them should contain all the view components required from a ProductCardView
     *
     */
    @LayoutRes
    protected abstract fun getLayout(): Int

    /**
     * Find view components from the getLayout() method.
     *
     * @param inflatedView View inflated from getLayout()
     */


    open fun setImageCatalogUrl(imageUrl: String) {
        imageCatalog?.let {
            ImageHandler.loadImageThumbs(context, it, imageUrl)
        }
    }

    open fun setLabelCatalogCountText(promoLabelText: String) {
        catalogCountLabel?.text = promoLabelText
    }


    open fun setLabelCatalogNameText(promoLabelText: String) {
        catalogName?.text = promoLabelText
    }

    open fun setLabelCatalogDescriptionText(promoLabelText: String) {
        if (promoLabelText.isNotEmpty()) {
            //  catalogDescription?.show()
            catalogDescription?.visibility = View.VISIBLE
            catalogDescription?.text = promoLabelText
        } else {
            //  catalogDescription?.hide()
            catalogDescription?.visibility = View.GONE
        }
    }

    open fun setLabelCatalogPriceText(promoLabelText: String) {
        catalogPrice?.text = promoLabelText
    }

    open fun setLabelMualaiDariText(promoLabelText: String) {
        mulaiDariText?.text = promoLabelText
    }


}