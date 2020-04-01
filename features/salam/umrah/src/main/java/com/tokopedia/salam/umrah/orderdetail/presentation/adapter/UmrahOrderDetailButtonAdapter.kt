package com.tokopedia.salam.umrah.orderdetail.presentation.adapter

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.orderdetail.presentation.adapter.viewholder.UmrahOrderDetailButtonViewHolder
import com.tokopedia.salam.umrah.orderdetail.presentation.viewmodel.UmrahOrderDetailButtonViewModel

/**
 * @author by furqan on 15/10/2019
 */
class UmrahOrderDetailButtonAdapter(val listener: Listener) : RecyclerView.Adapter<UmrahOrderDetailButtonViewHolder>() {

    private val buttonViewModel = arrayListOf<UmrahOrderDetailButtonViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UmrahOrderDetailButtonViewHolder {
        val linearLayout = LinearLayout(parent.context)
        val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 0, 0, 8)
        linearLayout.layoutParams = layoutParams
        linearLayout.orientation = LinearLayout.VERTICAL

        return UmrahOrderDetailButtonViewHolder(linearLayout)
    }

    override fun getItemCount(): Int = buttonViewModel.size

    override fun onBindViewHolder(viewHolder: UmrahOrderDetailButtonViewHolder, position: Int) {
        viewHolder.bind(buttonViewModel[position])
        viewHolder.itemView.setOnClickListener {
            listener.onItemClicked(buttonViewModel[position], position)
        }
    }

    fun setData(buttonViewModel: List<UmrahOrderDetailButtonViewModel>) {
        this.buttonViewModel.clear()
        this.buttonViewModel.addAll(buttonViewModel)
        notifyDataSetChanged()
    }

    fun addData(buttonViewModel: UmrahOrderDetailButtonViewModel) {
        this.buttonViewModel.add(buttonViewModel)
        notifyDataSetChanged()
    }

    interface Listener {
        fun onItemClicked(buttonViewModel: UmrahOrderDetailButtonViewModel, position: Int)
    }
}