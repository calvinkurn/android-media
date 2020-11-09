package com.tokopedia.product.detail.view.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofDataModel
import com.tokopedia.product.detail.data.util.productThousandFormatted
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.share.ekstensions.layoutInflater
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_hierarchycal_social_proof.view.*
import kotlinx.android.synthetic.main.shimmering_social_proof.view.*

/**
 * Created by Yehezkiel on 18/05/20
 */
class ProductMiniSocialProofViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductMiniSocialProofDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_hierarchycal_social_proof
    }

    override fun bind(element: ProductMiniSocialProofDataModel) {
        if (!element.shouldRenderSocialProof) {
            setupLoading(element.shouldShowSingleViewSocialProof())
            showLoading()
        } else {
            val availableData = element.getLastThreeHirarchyData
            view.run {
                if (availableData.isEmpty()) {
                    layoutParams.height = 0
                    return@run
                } else {
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                }

                hideLoading()

                val rootView = findViewById<ViewGroup>(R.id.root_socproof)
                val inflater: LayoutInflater = context.layoutInflater

                rootView.removeAllViews()

                if (element.shouldShowSingleViewSocialProof()) {
                    val socProofView: View = inflater.inflate(R.layout.social_proof_item, null)
                    renderSingleSocialProof(element.generateSingleView(context), socProofView, rootView)
                } else {
                    availableData.forEachIndexed { index, pairValue ->
                        if (element.isFirstData(pairValue)) {
                            val firstSocProofView: View = inflater.inflate(R.layout.social_proof_item, null)
                            renderFirstSocialProof(element, firstSocProofView)
                            rootView.addView(firstSocProofView, index)
                        } else {
                            val clickedSocialProof: View = inflater.inflate(R.layout.chip_social_proof_item, null)
                            renderClickedSocialProof(pairValue, clickedSocialProof, element.rating
                                    ?: 0F, getComponentTrackData(element))
                            rootView?.addView(clickedSocialProof, index)
                        }
                    }
                }

            }
        }
    }

    private fun renderSingleSocialProof(value: String, view: View, rootView: ViewGroup) {
        val firstSocialProofTxt = view.findViewById<Typography>(R.id.social_proof_first_text)
        firstSocialProofTxt.text = value
        rootView.addView(view, 0)
    }

    private fun renderClickedSocialProof(value: Pair<String, Int>, clickedSocialProof: View, rating: Float, componentTrackDataModel: ComponentTrackDataModel) {
        val firstSocialProofTxt = clickedSocialProof.findViewById<Typography>(R.id.chip_social_proof_title)
        val firstSocialProofValue = clickedSocialProof.findViewById<Typography>(R.id.chip_social_proof_value)
        when (value.first) {
            ProductMiniSocialProofDataModel.RATING -> {
                clickedSocialProof.isClickable = true
                clickedSocialProof.setOnClickListener { listener.onReviewClick() }
                firstSocialProofTxt?.run {
                    text = rating.toString()
                    setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, R.drawable.ic_review_one_small), null, null, null)
                }
                firstSocialProofValue?.run {
                    text = view.context.getString(R.string.bracket_formated, value.second.productThousandFormatted())
                }
            }
            ProductMiniSocialProofDataModel.TALK -> {
                clickedSocialProof.isClickable = true
                clickedSocialProof.setOnClickListener { listener.onDiscussionClicked(componentTrackDataModel) }
                firstSocialProofTxt?.run {
                    text = view.context.getString(R.string.product_detail_discussion_label)
                }
                firstSocialProofValue?.run {
                    text = view.context.getString(R.string.bracket_formated, value.second.productThousandFormatted())
                }
            }
        }
    }

    private fun renderFirstSocialProof(element: ProductMiniSocialProofDataModel, view: View) = with(view) {
        val firstSocialProofTxt = view.findViewById<Typography>(R.id.social_proof_first_text)
        firstSocialProofTxt.text = element.generateFirstSocialProofText(context)
    }

    private fun showLoading() = with(view) {
        root_socproof.hide()
        pdp_shimmering_social_proof.show()
    }

    private fun hideLoading() = with(view) {
        root_socproof.show()
        pdp_shimmering_social_proof.hide()
    }

    private fun setupLoading(shouldShowSingleSocialProof: Boolean) = with(view) {
        if (shouldShowSingleSocialProof)
            pdp_shimmering_social_proof.setPadding(16.toPx(), 0, 16.toPx(), 16.toPx())
        else
            pdp_shimmering_social_proof.setPadding(16.toPx(), 8.toPx(), 16.toPx(), 20.toPx())
    }

    private fun getComponentTrackData(element: ProductMiniSocialProofDataModel) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)
}