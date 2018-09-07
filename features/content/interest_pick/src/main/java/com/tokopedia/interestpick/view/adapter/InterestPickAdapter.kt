package com.tokopedia.interestpick.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.interestpick.R
import com.tokopedia.interestpick.view.listener.InterestPickContract
import com.tokopedia.interestpick.view.viewmodel.InterestPickItemViewModel
import kotlinx.android.synthetic.main.item_interest_pick.view.*

/**
 * @author by milhamj on 07/09/18.
 */

class InterestPickAdapter(val listener: (InterestPickContract.View) -> Unit)
    : RecyclerView.Adapter<InterestPickAdapter.ViewHolder>() {

    val list: ArrayList<InterestPickItemViewModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.item_interest_pick, parent, false).let {
            return ViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], listener)
    }

    override fun getItemCount() = list.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(viewModel: InterestPickItemViewModel,
                 listener: (InterestPickContract.View) -> Unit) = with(itemView) {
            val colorId = if (viewModel.isSelected)
                R.color.interest_background_active else
                R.color.interest_background_inactive
            backgroundView.setBackgroundColor(MethodChecker.getColor(itemView.context, colorId))
            category.text = viewModel.categoryName
            ImageHandler.LoadImage(image, viewModel.image)

            backgroundView.setOnClickListener {

            }
        }
    }

}