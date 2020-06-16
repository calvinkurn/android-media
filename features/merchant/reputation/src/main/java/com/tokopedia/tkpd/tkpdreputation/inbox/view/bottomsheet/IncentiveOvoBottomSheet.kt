package com.tokopedia.tkpd.tkpdreputation.inbox.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.tkpd.tkpdreputation.R
import com.tokopedia.tkpd.tkpdreputation.analytic.ReputationTracking
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ProductRevIncentiveOvoDomain
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.incentive_ovo_bottom_sheet_dialog.view.*
import kotlinx.android.synthetic.main.item_incentive_ovo.view.*

class IncentiveOvoBottomSheet(private val productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain, private val category: String): BottomSheetUnify() {

    companion object {
        val TAG = IncentiveOvoBottomSheet::class.qualifiedName
        val layout = R.layout.incentive_ovo_bottom_sheet_dialog
        var checkBtnContinue = false
        const val url = "https://ecs7.tokopedia.net/android/others/ovo_incentive_bottom_sheet_image.png"
    }

    private val reputationTracking: ReputationTracking by lazy { ReputationTracking() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(context, layout, null)
        initView(view)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView(view: View) {
        ImageHandler.LoadImage(view.ivIncentiveOvo, url)
        view.tgIncentiveOvoTitle.text = productRevIncentiveOvoDomain.productrevIncentiveOvo.title
        view.tgIncentiveOvoSubtitle.text = productRevIncentiveOvoDomain.productrevIncentiveOvo.subtitle
        view.tgIncentiveOvoDescription.text = productRevIncentiveOvoDomain.productrevIncentiveOvo.description
        view.btnContinueReview.text = productRevIncentiveOvoDomain.productrevIncentiveOvo.ctaText

        val adapterIncentiveOvo = ProductRevIncentiveOvoAdapter(productRevIncentiveOvoDomain.productrevIncentiveOvo.numberedList)
        view.rvIncentiveOvoExplain.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterIncentiveOvo
        }
        setShowListener {
            val dpSixteen = 16
            val dpZero = 0
            bottomSheetHeader.setPadding(dpSixteen, dpSixteen, dpSixteen, dpZero)
            (bottomSheetHeader.layoutParams as LinearLayout.LayoutParams).setMargins(dpZero, dpZero, dpZero, dpZero)
        }
        setOnDismissListener {
            hitContinueOrDismissTracker(checkBtnContinue)
            checkBtnContinue = false
        }
        view.btnContinueReview.setOnClickListener {
            checkBtnContinue = true
            dismiss()
        }
    }

    private fun hitContinueOrDismissTracker(checkBtnContinue: Boolean) {
        if(checkBtnContinue) {
            reputationTracking.onClickContinueIncentiveOvoBottomSheetTracker(category)
        } else {
            reputationTracking.onClickDismissIncentiveOvoBottomSheetTracker(category)
        }
    }

    private inner class ProductRevIncentiveOvoAdapter(private val list: List<String>)
        : RecyclerView.Adapter<ProductRevIncentiveOvoAdapter.ViewHolder>() {

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindHero(list[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder  {
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