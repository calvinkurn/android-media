package com.tokopedia.checkout.view.feature.addressoptions.recyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.domain.datamodel.addressoptions.CornerAddressModel
import com.tokopedia.design.component.Tooltip

/**
 * Created by fajarnuha on 2019-05-21.
 */
class SampaiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mButton: Button = itemView.findViewById(R.id.button_tkpd_corner)
    private var mCornerView: View = itemView.findViewById(R.id.view_tkpd_corner)
    private var mCornerName: TextView = itemView.findViewById(R.id.text_view_corner)
    private var mBranchName: TextView = itemView.findViewById(R.id.text_view_branch)
    private var mRadio: RadioButton = itemView.findViewById(R.id.radio_button_corner)
    private var mInfo: ImageView = itemView.findViewById(R.id.img_info)

    fun bind(model: CornerAddressModel, listener: ShipmentAddressListAdapter.ActionListener, position: Int) {
        if (model.cornerModel != null) {
            mCornerView.visibility = View.VISIBLE
            mCornerView.setOnClickListener { view -> listener.onCornerAddressClicked(model.cornerModel, position) }
            mBranchName.text = model.cornerModel.addressName
            mCornerName.text = model.cornerModel.partnerName
            mRadio.isChecked = model.isSelected
            mButton.text = itemView.context.getString(R.string.button_change_corner)
        } else {
            mCornerView.visibility = View.GONE
            mButton.text = itemView.context.getString(R.string.button_choose_corner)
        }
        mInfo.setOnClickListener(showCornerInfo)
        mButton.setOnClickListener { view -> listener.onCornerButtonClicked() }
    }

    private val showCornerInfo: (view: View) -> Unit = {
        val tooltip = Tooltip(it.context)
        tooltip.setTitle(it.context.getString(R.string.brand_tokopedia_corner))
        tooltip.setDesc(it.context.getString(R.string.tooltip_corner_info))
        tooltip.setTextButton(it.context.getString(R.string.button_mengerti))
        tooltip.btnAction.setOnClickListener { tooltip.dismiss() }
        tooltip.show()
    }

    companion object {
        @JvmStatic
        val TYPE: Int = R.layout.item_sampai
    }
}