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
import com.tokopedia.common.topupbills.data.TopupBillsEnquiry
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryMainInfo
import com.tokopedia.common.topupbills.databinding.ViewWidgetEnquiryDataBinding
import com.tokopedia.common.topupbills.databinding.ViewWidgetEnquiryDataItemBinding
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 12/12/19.
 */
class TopupBillsEnquiryDataWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewWidgetEnquiryDataBinding.inflate(LayoutInflater.from(context), this, true)

    var enquiryData: TopupBillsEnquiry = TopupBillsEnquiry()
        set(value) {
            value.attributes?.run {
                field = value
                with(binding.rvEnquiryData.adapter as TopupBillsEnquiryDataAdapter) {
                    items = mainInfoList
                    setupDecoration(mainInfoList)
                    notifyDataSetChanged()
                }
            }
        }
    var title: String = ""
        set(value) {
            field = value
            binding.enquiryDataTitle.text = value
        }

    init {
        enquiryData.attributes?.run {
            val enquiryDataItems = mainInfoList
            binding.rvEnquiryData.adapter = TopupBillsEnquiryDataAdapter(enquiryDataItems)
            setupDecoration(enquiryDataItems)
        }
    }

    private fun setupDecoration(data: List<TopupBillsEnquiryMainInfo>) {
        (binding.rvEnquiryData as VerticalRecyclerView).clearItemDecoration()
        binding.rvEnquiryData.addItemDecoration(object : RecyclerView.ItemDecoration() {
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
            val binding = ViewWidgetEnquiryDataItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TopupBillsEnquiryDataViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: TopupBillsEnquiryDataViewHolder, position: Int) {
            holder.bind(items[position])
        }
    }

    inner class TopupBillsEnquiryDataViewHolder(
        private val binding: ViewWidgetEnquiryDataItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(element: TopupBillsEnquiryMainInfo) {
            with(binding) {
                enquiryItemLabel.text = element.label
                enquiryItemValue.text = element.value
            }
        }
    }

    companion object {
        val ITEM_DECORATOR_SIZE = com.tokopedia.unifyprinciples.R.dimen.unify_space_4
    }
}
