package com.tokopedia.smartbills.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.SmartBillsItemDetail
import com.tokopedia.smartbills.databinding.ViewSmartBillsItemDetailBinding
import com.tokopedia.smartbills.databinding.ViewSmartBillsItemDetailItemBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * @author by resakemal on 17/05/20
 */

class SmartBillsItemDetailBottomSheet : BottomSheetUnify() {

    private var categoryName = ""
    private var details: List<SmartBillsItemDetail> = listOf()

    private var binding by autoClearedNullable<ViewSmartBillsItemDetailBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initBottomSheet() {
        setCloseClickListener { dismiss() }
        binding = ViewSmartBillsItemDetailBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun initView(view: View) {
        binding?.run {
            tvSmartBillsItemDetailTitle.text = if (categoryName.isNotEmpty()) {
                String.format(getString(R.string.smart_bills_item_detail_title), categoryName)
            } else {
                getString(R.string.smart_bills_item_detail_title_default)
            }

            rvSmartBillsItemDetail.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = SmartBillsItemDetailAdapter(details)
            }
        }
    }

    inner class SmartBillsItemDetailAdapter(
        var items: List<SmartBillsItemDetail>
    ) : RecyclerView.Adapter<SmartBillsItemDetailAdapter.SmartBillsItemDetailViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartBillsItemDetailViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.view_smart_bills_item_detail_item, parent, false)
            return SmartBillsItemDetailViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: SmartBillsItemDetailViewHolder, position: Int) {
            holder.bind(items[position])
        }

        inner class SmartBillsItemDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(element: SmartBillsItemDetail) {
                val binding = ViewSmartBillsItemDetailItemBinding.bind(itemView)
                with(binding) {
                    tvSmartBillsItemDetailLabel.text = element.label
                    tvSmartBillsItemDetailValue.text = element.value
                }
            }
        }
    }

    companion object {
        private const val TAG = "SmartBillsItemDetailBottomSheet"

        @JvmStatic
        fun newInstance(
            categoryName: String = "",
            details: List<SmartBillsItemDetail>
        ): SmartBillsItemDetailBottomSheet {
            return SmartBillsItemDetailBottomSheet().apply {
                this.categoryName = categoryName
                this.details = details
            }
        }
    }
}
