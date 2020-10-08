package com.tokopedia.review.feature.ovoincentive.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.common.analytics.ReviewTracking
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.incentive_ovo_bottom_sheet_dialog.view.*
import kotlinx.android.synthetic.main.item_incentive_ovo.view.*

class IncentiveOvoBottomSheet(private val productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain, private val category: String): BottomSheetUnify() {

    companion object {
        val TAG = IncentiveOvoBottomSheet::class.qualifiedName
        val layout = R.layout.incentive_ovo_bottom_sheet_dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, layout, null)
        initView(view)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView(view: View) {
        view.apply {
            tgIncentiveOvoTitle.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.title
            tgIncentiveOvoSubtitle.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.subtitle
            incentiveOvoBtnContinueReview.apply {
                setOnClickListener {
                    dismiss()
                    ReviewTracking.onClickContinueIncentiveOvoBottomSheetTracker(category)
                }
                text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.ctaText
            }
        }

        view.tgIncentiveOvoDescription.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.description

        val adapterIncentiveOvo = ProductRevIncentiveOvoAdapter(productRevIncentiveOvoDomain.productrevIncentiveOvo?.numberedList ?: emptyList())
        view.rvIncentiveOvoExplain.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterIncentiveOvo
        }
        isFullpage = false
        showKnob = true
        showCloseIcon = false
    }

    private inner class ProductRevIncentiveOvoAdapter(private val list: List<String>)
        : RecyclerView.Adapter<ProductRevIncentiveOvoAdapter.ViewHolder>() {

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindHero(list[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_incentive_ovo, parent, false))
        }

        override fun getItemCount(): Int = list.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bindHero(explanation: String) {
                itemView.apply {
                    tgIncentiveOvoNumber.text = "${adapterPosition+1}."
                    tgIncentiveOvoExplanation.text = HtmlLinkHelper(context, explanation).spannedString
                }
            }
        }
    }
}