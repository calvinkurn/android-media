package com.tokopedia.buyerorderdetail.presentation.adapter

import android.graphics.Color
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailAddonsListBinding
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography


class AddonsItemAdapter(private val addonsItemList: List<AddonsListUiModel.AddonItemUiModel>) :
    RecyclerView.Adapter<AddonsItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBuyerOrderDetailAddonsListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (addonsItemList.isNotEmpty()) {
            holder.bind(addonsItemList[position])
        }
    }

    override fun getItemCount(): Int = addonsItemList.size

    inner class ViewHolder(private val binding: ItemBuyerOrderDetailAddonsListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AddonsListUiModel.AddonItemUiModel) {
            with(binding) {
                setupDividerAddon()
                setDataViews(item)
                setupAddNoteViews(item)
            }
        }

        private fun ItemBuyerOrderDetailAddonsListBinding.setupDividerAddon() {
            ivBomDetailAddonsDash.showWithCondition(adapterPosition.isMoreThanZero())
        }

        private fun ItemBuyerOrderDetailAddonsListBinding.setupAddNoteViews(item: AddonsListUiModel.AddonItemUiModel) {
            if (item.isCustomNote) {
                setupToMetadata(item.toStr)
                setupFromMetadata(item.fromStr)
                setupMessageMetadata(item.message)
            } else {
                hideNoteViews()
            }
        }

        private fun ItemBuyerOrderDetailAddonsListBinding.hideNoteViews() {
            tvBomDetailAddonsToValue.hide()
            tvBomDetailAddonsToLabel.hide()
            tvBomDetailAddonsFromLabel.hide()
            tvBomDetailAddonsFromValue.hide()
            tvBomDetailAddonsMessageValue.hide()
            tvBomDetailAddonsReadMoreMessage.hide()
        }

        private fun ItemBuyerOrderDetailAddonsListBinding.setDataViews(item: AddonsListUiModel.AddonItemUiModel) {
            tvBomDetailAddonsName.text = root.context.getString(
                R.string.order_addons_type_and_name,
                item.type,
                item.addOnsName
            )
            ivBomDetailAddonsThumbnail.setImageUrl(item.addOnsThumbnailUrl)
            tvBomDetailAddonsPriceQuantity.text =
                root.context.getString(
                    R.string.label_product_price_and_quantity,
                    item.quantity,
                    item.priceText
                )
        }

        private fun ItemBuyerOrderDetailAddonsListBinding.setupToMetadata(toStr: String) {
            if (toStr.isBlank()) {
                tvBomDetailAddonsToValue.hide()
                tvBomDetailAddonsToLabel.hide()
            } else {
                tvBomDetailAddonsToValue.text =
                    root.context.getString(R.string.order_addons_to_value, toStr)
                tvBomDetailAddonsToValue.show()
                tvBomDetailAddonsToLabel.show()
            }
        }

        private fun ItemBuyerOrderDetailAddonsListBinding.setupFromMetadata(fromStr: String) {
            if (fromStr.isBlank()) {
                tvBomDetailAddonsFromLabel.hide()
                tvBomDetailAddonsFromValue.hide()
            } else {
                tvBomDetailAddonsFromValue.text =
                    root.context.getString(R.string.order_addons_to_value, fromStr)
                tvBomDetailAddonsFromLabel.show()
                tvBomDetailAddonsFromValue.show()
            }
        }

        private fun ItemBuyerOrderDetailAddonsListBinding.setupMessageMetadata(message: String) {
            if (message.isBlank()) {
                tvBomDetailAddonsMessageValue.hide()
                tvBomDetailAddonsReadMoreMessage.hide()
            } else {
                tvBomDetailAddonsMessageValue.run {
                    show()
                    setAddonMessageFormatted(message)
                }
            }
        }

        private fun setAddonMessageFormatted(
            message: String
        ) {
            with(binding) {
                tvBomDetailAddonsMessageValue.apply {
                    if (ellipsize == TextUtils.TruncateAt.END && lineCount == Int.ONE) {
                        expandAddonMessage(message)
                    } else {
                        initCollapseAddonMessage(message)
                    }
                }
            }
        }

        private fun toggleAddonMessage(message: String) {
            with(binding) {
                tvBomDetailAddonsMessageValue.apply {
                    if (ellipsize == TextUtils.TruncateAt.END && lineCount == Int.ONE) {
                        expandAddonMessage(message)
                    } else {
                        collapseAddonMessage(message)
                    }
                }
            }
        }

        private fun expandAddonMessage(message: String) {
            with(binding) {
                val hyperLinkText = root.context.getString(R.string.order_addons_close)
                val messageFmt = message + hyperLinkText
                tvBomDetailAddonsReadMoreMessage.hide()
                tvBomDetailAddonsMessageValue.run {
                    setupHyperlinkText(messageFmt, message)
                    maxLines = Integer.MAX_VALUE
                    ellipsize = null
                    isSingleLine = false
                }
            }
        }

        private fun initCollapseAddonMessage(message: String) {
            with(binding) {
                tvBomDetailAddonsMessageValue.run {
                    text = message
                    post {
                        if (lineCount > Int.ONE) {
                            ellipsize = TextUtils.TruncateAt.END
                        }
                        maxLines = Int.ONE
                        isSingleLine = true
                        setupReadMoreMessage(message)
                    }
                }
            }
        }

        private fun collapseAddonMessage(message: String) {
            with(binding) {
                tvBomDetailAddonsMessageValue.run {
                    text = message
                    if (lineCount > Int.ONE) {
                        ellipsize = TextUtils.TruncateAt.END
                    }
                    maxLines = Int.ONE
                    isSingleLine = true
                    setupReadMoreMessage(message)
                }
            }
        }

        private fun setupReadMoreMessage(message: String) {
            with(binding) {
                tvBomDetailAddonsReadMoreMessage.let {
                    if (tvBomDetailAddonsMessageValue.ellipsize == TextUtils.TruncateAt.END) {
                        it.show()
                        it.setupHyperlinkText(
                            root.context.getString(R.string.order_addons_read_more),
                            message
                        )
                    } else {
                        it.hide()
                    }
                }
            }
        }

        private fun Typography.setupHyperlinkText(
            hyperLinkText: String,
            message: String
        ) {
            val getHtmlLinkHelper = HtmlLinkHelper(context, hyperLinkText)
            text = getHtmlLinkHelper.spannedString
            this.movementMethod = LinkMovementMethod.getInstance()
            this.highlightColor = Color.TRANSPARENT
            getHtmlLinkHelper.urlList.getOrNull(Int.ZERO)?.setOnClickListener {
                toggleAddonMessage(message)
            }
        }
    }
}