package com.tokopedia.salam.umrah.common.presentation.widget

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.AttributeSet
import android.view.View
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentWidgetModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_umrah_travel_agent.view.*

/**
 * @author by Firman on 06/01/20
 */

class UmrahTravelAgentWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var umrahTravelAgentModel: UmrahTravelAgentWidgetModel

    init {
        View.inflate(context, R.layout.widget_umrah_travel_agent, this)

    }

    fun buildView() {
        if (::umrahTravelAgentModel.isInitialized) {
            tg_umrah_travel_agent_established.text = resources.getString(R.string.umrah_pdp_travel_agent_established, umrahTravelAgentModel.establishedSince).toSpanned()
            tg_umrah_travel_agent_pilgrims.text = resources.getString(R.string.umrah_pdp_travel_agent_pilgrims, umrahTravelAgentModel.pilgrimsPerYear).toSpanned()
            tg_umrah_travel_agent_availabel.text = resources.getString(R.string.umrah_pdp_travel_agent_available_seat,umrahTravelAgentModel.availableSeat).toSpanned()

        } else {
            container_widget_umrah_travel_agent.visibility = View.GONE
        }
    }

    fun String.toSpanned(): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            return Html.fromHtml(this)
        }
    }
}