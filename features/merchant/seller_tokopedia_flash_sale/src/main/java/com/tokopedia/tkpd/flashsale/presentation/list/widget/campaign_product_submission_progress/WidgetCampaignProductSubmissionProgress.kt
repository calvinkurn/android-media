package com.tokopedia.tkpd.flashsale.presentation.list.widget.campaign_product_submission_progress

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.utils.extension.attachDividerItemDecoration
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsWidgetCampaignProductSubmissionProgressLayoutBinding
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionProgress
import com.tokopedia.tkpd.flashsale.presentation.list.widget.campaign_product_submission_progress.adapter.delegate.CampaignProductSubmissionProgressItemDelegate
import com.tokopedia.tkpd.flashsale.presentation.list.widget.campaign_product_submission_progress.adapter.viewholder.CampaignProductSubmissionProgressItemViewHolder
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.seller_tokopedia_flash_sale.R

class WidgetCampaignProductSubmissionProgress(
    context: Context, attrs: AttributeSet
) : CardUnify(context, attrs), CampaignProductSubmissionProgressItemViewHolder.Listener {

    private var viewBinding: StfsWidgetCampaignProductSubmissionProgressLayoutBinding? = null
    private var containerHeader: ViewGroup? = null
    private var containerCampaignList: ViewGroup? = null
    private var loader: View? = null
    private var textTitle: Typography? = null
    private var iconChevron: IconUnify? = null
    private var rvCampaignList: RecyclerView? = null
    private var onCampaignItemClicked: ((campaign: FlashSaleProductSubmissionProgress.Campaign) -> Unit)? =
        null
    private val adapterWidgetCampaignProductSubmissionProgress by lazy {
        CompositeAdapter.Builder()
            .add(CampaignProductSubmissionProgressItemDelegate(this))
            .build()
    }

    init {
        viewBinding = StfsWidgetCampaignProductSubmissionProgressLayoutBinding.inflate(
            LayoutInflater.from(context), this, true
        )
        initView()
        setupRecyclerView()
        containerHeader?.setOnClickListener {
            toggleExpandCollapseCampaignList()
        }
        hide()
    }

    private fun toggleExpandCollapseCampaignList() {
        if (containerCampaignList?.isVisible == true) {
            iconChevron?.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    com.tokopedia.iconunify.R.drawable.iconunify_chevron_down
                )
            )
            containerCampaignList?.hide()
        } else {
            iconChevron?.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    com.tokopedia.iconunify.R.drawable.iconunify_chevron_up
                )
            )
            containerCampaignList?.show()
        }
    }

    private fun initView() {
        viewBinding?.let {
            containerHeader = it.containerHeader
            containerCampaignList = it.containerListCampaignProductSubmission
            loader = it.loader
            textTitle = it.title
            iconChevron = it.iconChevron
            rvCampaignList = it.rvCampaignProductSubmission
        }
    }

    private fun setupRecyclerView() {
        rvCampaignList?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterWidgetCampaignProductSubmissionProgress
            attachDividerItemDecoration(R.drawable.campaign_product_submission_progress_item_separator)
        }
    }

    fun setData(
        listCampaignProductSubmission: List<FlashSaleProductSubmissionProgress.Campaign>,
        onCampaignItemClicked: (FlashSaleProductSubmissionProgress.Campaign) -> Unit
    ) {
        textTitle?.text = getTitle(listCampaignProductSubmission)
        this.onCampaignItemClicked = onCampaignItemClicked
        adapterWidgetCampaignProductSubmissionProgress.submit(listCampaignProductSubmission)
    }

    private fun getTitle(
        listCampaignProductSubmission: List<FlashSaleProductSubmissionProgress.Campaign>
    ): String {
        return context.getString(
            R.string.stfs_widget_campaign_product_submission_title_format,
            listCampaignProductSubmission.size
        )
    }

    override fun onCampaignItemClicked(campaignData: FlashSaleProductSubmissionProgress.Campaign) {
        onCampaignItemClicked?.invoke(campaignData)
    }

}
