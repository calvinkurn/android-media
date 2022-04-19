package com.tokopedia.flight.filter.presentation.adapter.viewholder

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.search.presentation.model.statistics.AirlineStat

/**
 * Created by furqan on 06/10/21.
 */
class FlightFilterAirlineViewHolder(itemView: View, checkableInteractionListener: CheckableInteractionListener?)
    : BaseCheckableViewHolder<AirlineStat>(itemView, checkableInteractionListener), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    var context: Context = itemView.context
    var ivLogo: ImageView = itemView.findViewById<View>(R.id.iv_logo) as ImageView
    var tvTitle: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
    var tvDesc: TextView = itemView.findViewById<View>(R.id.tv_desc) as TextView
    var checkBox: CheckBox = itemView.findViewById<View>(R.id.checkbox) as AppCompatCheckBox

    override fun bind(airlineStat: AirlineStat) {
        super.bind(airlineStat)
        loadImageWithPlaceholder(ivLogo, airlineStat.airlineDB.logo, ContextCompat.getDrawable(itemView.context, R.drawable.flight_ic_airline_default))
        tvTitle.text = airlineStat.airlineDB.name
        tvDesc.text = getString(R.string.start_from_x, airlineStat.minPriceString)
        itemView.setOnClickListener(this)
    }

    override fun getCheckable(): CompoundButton = checkBox

    override fun onClick(v: View) {
        toggle()
    }

    private fun loadImageWithPlaceholder(imageview: ImageView, url: String?, resId: Drawable?) {
        if (url != null && !TextUtils.isEmpty(url)) {
            Glide.with(imageview.context)
                    .load(url)
                    .placeholder(resId)
                    .dontAnimate()
                    .error(resId)
                    .into(imageview)
        } else {
            Glide.with(imageview.context)
                    .load(url)
                    .placeholder(resId)
                    .error(resId)
                    .into(imageview)
        }
    }

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_flight_airline_filter
    }

}