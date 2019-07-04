package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import kotlinx.android.synthetic.main.chips_item.view.*

/**
 * Created by fwidjaja on 2019-06-21.
 */
class LabelAlamatChipsAdapter(context: Context?, private var actionListener: ActionListener) : RecyclerView.Adapter<LabelAlamatChipsAdapter.ViewHolder>() {
    var labelAlamatList = mutableListOf<String>()
    private var drawablePressed = context?.let { ContextCompat.getDrawable(it, R.drawable.bg_chips_pressed) }
    private var drawableDefault = context?.let { ContextCompat.getDrawable(it, R.drawable.bg_chips_item_layout) }

    interface ActionListener {
        fun onLabelAlamatChipClicked(labelAlamat: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chips_item, parent, false))
    }

    override fun getItemCount(): Int {
        return labelAlamatList.size
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val res = holder.itemView.context.resources
        holder.itemView.tv_chips_item.run {
            text = labelAlamatList[position]
            // background = drawableDefault
            setTextColor(res.getColor(R.color.font_black_secondary_54))
            // setPadding(AddNewAddressUtils.toDp(12), AddNewAddressUtils.toDp(10),
                    // AddNewAddressUtils.toDp(12), AddNewAddressUtils.toDp(10))
            setOnClickListener {
                // background = drawablePressed
                setTextColor(res.getColor(R.color.tkpd_green))
                actionListener.onLabelAlamatChipClicked(labelAlamatList[position])
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}