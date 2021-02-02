package com.tokopedia.pdpsimulation.paylater.presentation.simulation.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.pdpsimulation.R

class BlankViewTableRowHeader(val context: Context, val layoutParams: ViewGroup.LayoutParams) {


    fun getLayout() = R.layout.paylater_simulation_table_row_header

    fun initUI(): View {
        val rowHeaderBlank = LayoutInflater.from(context).inflate(getLayout(), null)
        val parent = rowHeaderBlank.findViewById<ConstraintLayout>(R.id.clSimulationTableRowHeader)
        parent.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N50))
        parent.layoutParams = layoutParams
        val recomBadge = rowHeaderBlank.findViewById<ImageView>(R.id.ivRecommendationBadge)
        val image = rowHeaderBlank.findViewById<ImageView>(R.id.ivPaylaterPartner)
        image.gone()
        recomBadge.gone()
        return rowHeaderBlank
    }
}