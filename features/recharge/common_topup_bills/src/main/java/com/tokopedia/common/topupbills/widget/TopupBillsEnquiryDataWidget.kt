package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsEnquiry
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryMainInfo
import kotlinx.android.synthetic.main.view_widget_enquiry_data.view.*
import kotlinx.android.synthetic.main.view_widget_enquiry_data_item.view.*
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 12/12/19.
 */
class TopupBillsEnquiryDataWidget @JvmOverloads constructor(@NotNull context: Context,
                                                            attrs: AttributeSet? = null,
                                                            defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    var enquiryData: TopupBillsEnquiry = TopupBillsEnquiry()
        set(value) {
            value.attributes?.run {
                field = value
                with(rv_enquiry_data.adapter as TopupBillsEnquiryDataAdapter) {
                    items = mainInfoList
                    setupDecoration(mainInfoList)
                    notifyDataSetChanged()
                }
            }
        }
    var title: String = ""
        set(value) {
            field = value
            enquiry_data_title.text = value
        }

    init {
        View.inflate(context, R.layout.view_widget_enquiry_data, this)

        enquiryData.attributes?.run {
            val enquiryDataItems = mainInfoList
            rv_enquiry_data.adapter = TopupBillsEnquiryDataAdapter(enquiryDataItems)
            setupDecoration(enquiryDataItems)
        }
    }

    private fun setupDecoration(data: List<TopupBillsEnquiryMainInfo>) {
        (rv_enquiry_data as VerticalRecyclerView).clearItemDecoration()
        rv_enquiry_data.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                // Add offset to all items except the last one
                if (parent.getChildAdapterPosition(view) < data.size - 1) {
                    context.resources.getDimension(ITEM_DECORATOR_SIZE).toInt().let { dimen -> outRect.bottom = dimen }
                }
            }
        })
    }

    inner class TopupBillsEnquiryDataAdapter(var items: List<TopupBillsEnquiryMainInfo>) : RecyclerView.Adapter<TopupBillsEnquiryDataViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopupBillsEnquiryDataViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_widget_enquiry_data_item, parent, false)
            return TopupBillsEnquiryDataViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: TopupBillsEnquiryDataViewHolder, position: Int) {
            holder.bind(items[position])
        }

    }

    inner class TopupBillsEnquiryDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: TopupBillsEnquiryMainInfo) {
            with(itemView) {
                enquiry_item_label.text = element.label
                enquiry_item_value.text = element.value
            }
        }
    }

    companion object {
        val ITEM_DECORATOR_SIZE = com.tokopedia.unifyprinciples.R.dimen.unify_space_4
    }
}