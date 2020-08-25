package com.tokopedia.review.feature.ovoincentive.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.review.R
import com.tokopedia.review.common.analytics.ReviewTracking
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.incentive_ovo_bottom_sheet_dialog.view.*
import kotlinx.android.synthetic.main.item_incentive_ovo.view.*

class IncentiveOvoBottomSheet(private val productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain, private val category: String): BottomSheetUnify() {

    companion object {
        val TAG = IncentiveOvoBottomSheet::class.qualifiedName
        val layout = R.layout.incentive_ovo_bottom_sheet_dialog
        const val url = "https://ecs7.tokopedia.net/android/others/ovo_incentive_bottom_sheet_image.png"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, layout, null)
        initView(view)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView(view: View) {
        ImageHandler.LoadImage(view.ivIncentiveOvo, url)

        view.tgIncentiveOvoTitle.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.title
        view.tgIncentiveOvoSubtitle.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.subtitle
        view.tgIncentiveOvoDescription.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.description
        view.btnContinueReview.text = productRevIncentiveOvoDomain.productrevIncentiveOvo?.ctaText

        val adapterIncentiveOvo = ProductRevIncentiveOvoAdapter(productRevIncentiveOvoDomain.productrevIncentiveOvo?.numberedList ?: emptyList())
        view.rvIncentiveOvoExplain.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterIncentiveOvo
        }

        view.btnContinueReview.setOnClickListener {
            dismiss()
            ReviewTracking.onClickContinueIncentiveOvoBottomSheetTracker(category)
        }
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
                    tgIncentiveOvoExplanation.text = HtmlCompat.fromHtml(explanation, HtmlCompat.FROM_HTML_MODE_LEGACY);
                }
            }
        }
    }
}