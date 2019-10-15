package com.tokopedia.salam.umrah.orderdetail.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.tokopedia.salam.umrah.orderdetail.presentation.adapter.viewholder.UmrahOrderDetailButtonViewHolder
import com.tokopedia.salam.umrah.orderdetail.presentation.viewmodel.UmrahOrderDetailButtonViewModel
import com.tokopedia.unifycomponents.UnifyButton

/**
 * @author by furqan on 15/10/2019
 */
class UmrahOrderDetailButtonAdapter(val listener: Listener) : RecyclerView.Adapter<UmrahOrderDetailButtonViewHolder>() {

    private val buttonViewModel = arrayListOf<UmrahOrderDetailButtonViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UmrahOrderDetailButtonViewHolder {
        val button = UnifyButton(parent.context)
        button.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

        return UmrahOrderDetailButtonViewHolder(button)
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