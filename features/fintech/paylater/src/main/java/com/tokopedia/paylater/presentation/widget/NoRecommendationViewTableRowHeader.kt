package com.tokopedia.paylater.presentation.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.paylater.R

class NoRecommendationViewTableRowHeader(val context: Context, val layoutParams: ViewGroup.LayoutParams) {

    fun getLayout() = R.layout.paylater_simulation_table_row_header

    fun initUI(showBackGround: Boolean): View {
        val rowHeaderNoRecom = LayoutInflater.from(context).inflate(getLayout(), null)
        rowHeaderNoRecom.layoutParams = layoutParams
        val ivPayLaterPartner = rowHeaderNoRecom.findViewById<ImageView>(R.id.ivPaylaterPartner)
        ImageHandler.loadImage(context,
                ivPayLaterPartner,
                "https://ecs7.tokopedia.net/assets-fintech-frontend/pdp/kredivo/kredivo.png",
                R.drawable.ic_loading_image)
        if (showBackGround)
            rowHeaderNoRecom.setBackgroundColor(ContextCompat.getColor(context, R.color.Unify_N50))
        val recomBadge = rowHeaderNoRecom.findViewById<ImageView>(R.id.ivRecommendationBadge)
        recomBadge.gone()
        return rowHeaderNoRecom
    }
}