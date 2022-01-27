package com.tokopedia.play.broadcaster.setup.product.view.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ItemPlayBroEtalaseListBodyBinding
import com.tokopedia.play.broadcaster.databinding.ItemPlayBroEtalaseListHeaderBinding
import com.tokopedia.play.broadcaster.setup.product.view.model.EtalaseListModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play_common.view.loadImage
import com.tokopedia.unifycomponents.Label

/**
 * Created by kenny.hadisaputra on 27/01/22
 */
internal class PlayBroEtalaseListViewHolder private constructor() {

    internal class Header(
        private val binding: ItemPlayBroEtalaseListHeaderBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EtalaseListModel.Header) {
            binding.root.text = item.text
        }

        companion object {
            fun create(parent: ViewGroup): Header {
                return Header(
                    ItemPlayBroEtalaseListHeaderBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    internal class Body(
        private val binding: ItemPlayBroEtalaseListBodyBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EtalaseListModel.Campaign) {
            binding.ivEtalase.loadImage(item.campaignUiModel.imageUrl)
            binding.tvEtalaseTitle.text = item.campaignUiModel.title
            binding.tvTotalProduct.text = itemView.context.getString(
                R.string.play_bro_etalase_product_stock,
                item.campaignUiModel.totalProduct
            )
            binding.tvDateDesc.text = itemView.context.getString(
                R.string.play_bro_campaign_date_start_end,
                item.campaignUiModel.startDateFmt,
                item.campaignUiModel.endDateFmt,
            )

            when (item.campaignUiModel.status.status) {
                CampaignStatus.Ongoing -> {
                    binding.labelStatus.setLabel(
                        itemView.context.getString(R.string.play_bro_ongoing_campaign)
                    )
                    binding.labelStatus.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
                }
                CampaignStatus.Ready, CampaignStatus.ReadyLocked -> {
                    binding.labelStatus.setLabel(
                        itemView.context.getString(R.string.play_bro_upcoming_campaign)
                    )
                    binding.labelStatus.setLabelType(Label.HIGHLIGHT_LIGHT_ORANGE)
                }
                else -> {}
            }

            binding.tvDateDesc.visibility = View.VISIBLE
            binding.labelStatus.visibility =
                if (item.campaignUiModel.status.status == CampaignStatus.Unknown) View.GONE
                else View.VISIBLE
        }

        fun bind(item: EtalaseListModel.Etalase) {
            binding.ivEtalase.loadImage(item.etalaseUiModel.imageUrl)
            binding.tvEtalaseTitle.text = item.etalaseUiModel.title
            binding.tvTotalProduct.text = itemView.context.getString(
                R.string.play_bro_etalase_product_stock,
                item.etalaseUiModel.totalProduct
            )

            binding.tvDateDesc.visibility = View.GONE
            binding.labelStatus.visibility = View.GONE
        }

        companion object {
            fun create(parent: ViewGroup): Body {
                return Body(
                    ItemPlayBroEtalaseListBodyBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }
}