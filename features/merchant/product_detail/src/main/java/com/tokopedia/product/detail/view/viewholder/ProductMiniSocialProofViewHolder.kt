package com.tokopedia.product.detail.view.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofDataModel
import com.tokopedia.product.detail.data.util.productThousandFormatted
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.share.ekstensions.layoutInflater
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_hierarchycal_social_proof.view.*
import kotlinx.android.synthetic.main.item_social_proof_with_divider.view.*

/**
 * Created by Yehezkiel on 18/05/20
 */
class ProductMiniSocialProofViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductMiniSocialProofDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_hierarchycal_social_proof
    }

    override fun bind(element: ProductMiniSocialProofDataModel) {
        val availableData = element.getLastThreeHirarchyData

        if (!element.shouldRenderSocialProof) {
            showLoading()
        } else {
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
                    val socProofView: View = inflater.inflate(R.layout.item_social_proof_with_divider, null)
                    generateSingleTextSocialProof(availableData.firstOrNull()
                            ?: Pair("", 0), socProofView)
                    rootView.addView(socProofView, 0)
                } else {
                    availableData.forEachIndexed { index, i ->
                        val socProofView: View = inflater.inflate(R.layout.item_social_proof_with_divider, null)
                        renderSocialProofData(i, element.rating ?: 0F, element, socProofView, index)
                        rootView.addView(socProofView, index)
                    }
                }
            }
        }
    }

    private fun showLoading() = with(view) {
        root_socproof.hide()
        pdp_shimmering_social_proof.show()
    }

    private fun hideLoading() = with(view) {
        root_socproof.show()
        pdp_shimmering_social_proof.hide()
    }

    private fun generateSingleTextSocialProof(element: Pair<String, Int>, view: View) {
        val textSocialProofValue = view.txt_soc_proof_value
        val socProofDivider = view.social_proof_horizontal_separator
        val socProofTitle = view.txt_soc_proof_title
        socProofDivider?.hide()
        socProofTitle?.hide()

        when (element.first) {
            ProductMiniSocialProofDataModel.PAYMENT_VERIFIED -> {
                textSocialProofValue?.text = view.context.getString(R.string.terjual_single_text_template_builder, element.second.productThousandFormatted())
            }
            ProductMiniSocialProofDataModel.VIEW_COUNT -> {
                textSocialProofValue?.text = view.context.getString(R.string.product_view_single_text__template_builder, element.second.productThousandFormatted())
            }
        }
    }

    private fun renderSocialProofData(element: Pair<String, Int>, rating: Float, data: ProductMiniSocialProofDataModel, view: View, index: Int) {
        val textSocialProofValueView = view.txt_soc_proof_value
        val textSocialProofTitleView = view.txt_soc_proof_title
        val socProofDivider = view.social_proof_horizontal_separator
        var textSocialProofValue = ""
        var textSocialProofTitle = ""
        socProofDivider.showWithCondition(index != 0)

        when (element.first) {
            ProductMiniSocialProofDataModel.RATING -> {
                view.isClickable = true
                view.setOnClickListener { listener.onReviewClick() }
                textSocialProofTitleView?.run {
                    setTextColor(MethodChecker.getColor(view.context, R.color.light_N700_96))
                    setWeight(Typography.BOLD)
                    setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(view.context, R.drawable.ic_review_one_small), null, null, null)
                }

                textSocialProofTitle = rating.toString()
                textSocialProofValue = view.context.getString(R.string.bracket_formated, element.second.productThousandFormatted())
            }
            ProductMiniSocialProofDataModel.TALK -> {
                view.isClickable = true
                view.setOnClickListener { listener.onDiscussionClicked(getComponentTrackData(data)) }
                textSocialProofTitleView.setWeight(Typography.BOLD)
                textSocialProofTitleView.setTextColor(MethodChecker.getColor(view.context, R.color.light_N700_96))
                textSocialProofTitle = view.context.getString(R.string.label_qna)
                textSocialProofValue = element.second.productThousandFormatted()
            }
            ProductMiniSocialProofDataModel.PAYMENT_VERIFIED -> {
                view.isClickable = false
                textSocialProofTitle = view.context.getString(R.string.label_terjual)
                textSocialProofValue = element.second.productThousandFormatted()
            }
            ProductMiniSocialProofDataModel.WISHLIST -> {
                view.isClickable = false
                textSocialProofTitle = view.context.getString(R.string.label_wishlist)
                textSocialProofValue = element.second.productThousandFormatted()
            }
            ProductMiniSocialProofDataModel.VIEW_COUNT -> {
                view.isClickable = false
                textSocialProofTitle = view.context.getString(R.string.label_seen)
                textSocialProofValue = element.second.productThousandFormatted()
            }
        }

        textSocialProofValueView?.text = MethodChecker.fromHtml(textSocialProofValue)
        textSocialProofTitleView?.text = MethodChecker.fromHtml(textSocialProofTitle)
    }

    private fun getComponentTrackData(element: ProductMiniSocialProofDataModel) = ComponentTrackDataModel(element.type, element.name, adapterPosition + 1)
}