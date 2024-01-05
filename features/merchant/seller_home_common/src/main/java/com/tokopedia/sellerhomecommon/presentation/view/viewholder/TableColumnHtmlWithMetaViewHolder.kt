package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemTableColumnHtmlWithMetaBinding
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import com.tokopedia.utils.view.DarkModeUtil

class TableColumnHtmlWithMetaViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<TableRowsUiModel.RowColumnHtmlWithMeta>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_item_table_column_html_with_meta
    }

    private val binding by lazy { ShcItemTableColumnHtmlWithMetaBinding.bind(itemView) }

    override fun bind(element: TableRowsUiModel.RowColumnHtmlWithMeta) {
        setupValue(element)
        setupMeta(element.htmlMeta)
    }

    private fun setupValue(element: TableRowsUiModel.RowColumnHtmlWithMeta) {
        with(binding.tvTableColumnHtmlMeta) {
            text = element.valueStr.parseAsHtml()
            gravity = if (element.isLeftAlign) {
                Gravity.START
            } else {
                Gravity.END
            }
        }
    }

    private fun setupMeta(meta: TableRowsUiModel.RowColumnHtmlWithMeta.HtmlMeta?) {
        with(binding.tvTableColumnHtmlMetaLabel) {
            if (meta == null) {
                gone()
            } else {
                show()
                setOnClickListener {
                    listener.onLabelMetaClicked(meta)
                }

                text = meta.label
                try {
                    background.colorFilter = PorterDuffColorFilter(
                        Color.parseColor(
                            DarkModeUtil.getUnifyHexColor(
                                itemView.context,
                                meta.labelColor
                            )
                        ),
                        PorterDuff.Mode.SRC_ATOP
                    )
                } catch (ignored: Exception) {
                }
            }
        }
    }

    interface Listener {
        fun onLabelMetaClicked(htmlMeta: TableRowsUiModel.RowColumnHtmlWithMeta.HtmlMeta)
    }
}
