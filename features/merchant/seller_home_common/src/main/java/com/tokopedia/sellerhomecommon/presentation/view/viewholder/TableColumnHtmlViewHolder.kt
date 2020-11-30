package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setClickableUrlHtml
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import kotlinx.android.synthetic.main.shc_item_table_column_html.view.*

/**
 * Created By @ilhamsuaib on 01/07/20
 */

class TableColumnHtmlViewHolder(itemView: View?,
                                private val listener: Listener) : AbstractViewHolder<TableRowsUiModel.RowColumnHtml>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_item_table_column_html
    }

    override fun bind(element: TableRowsUiModel.RowColumnHtml) {
        with(itemView) {
            tvTableColumnHtml?.setClickableUrlHtml(element.valueStr) { url ->
                listener.onHyperlinkClicked(url)
                try {
                    val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context?.startActivity(myIntent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            if (element.isLeftAlign) {
                tvTableColumnHtml.gravity = Gravity.START
            } else {
                tvTableColumnHtml.gravity = Gravity.END
            }
        }
    }

    interface Listener {
        fun onHyperlinkClicked(url: String)
    }
}