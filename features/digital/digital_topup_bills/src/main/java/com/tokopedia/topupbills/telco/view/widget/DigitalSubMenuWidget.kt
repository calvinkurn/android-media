package com.tokopedia.topupbills.telco.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.getColorFromResources
import com.tokopedia.topupbills.telco.view.model.DigitalProductSubMenu
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 24/04/19.
 */
class DigitalSubMenuWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val titleHeaderLeft: TextView
    private val titleHeaderRight: TextView
    private val layoutHeaderLeft: RelativeLayout
    private val layoutHeaderRight: RelativeLayout
    private lateinit var listener: ActionListener

    private var headerSelected: Int = HEADER_LEFT

    init {
        val view = View.inflate(context, R.layout.view_digital_header_product, this)
        titleHeaderLeft = view.findViewById(R.id.header_left)
        titleHeaderRight = view.findViewById(R.id.header_right)
        layoutHeaderLeft = view.findViewById(R.id.layout_header_left)
        layoutHeaderRight = view.findViewById(R.id.layout_header_right)
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setHeader(subMenus: List<DigitalProductSubMenu>) {
        titleHeaderLeft.text = subMenus.get(HEADER_LEFT).label
        titleHeaderRight.text = subMenus.get(HEADER_RIGHT).label

        layoutHeaderLeft.setOnClickListener {
            if (headerSelected != HEADER_LEFT)
                headerSelected = HEADER_LEFT
            headerLeftActive(subMenus.get(HEADER_LEFT))
        }

        layoutHeaderRight.setOnClickListener {
            if (headerSelected != HEADER_RIGHT)
                headerSelected = HEADER_RIGHT
            headerRightActive(subMenus.get(HEADER_RIGHT))
        }
    }

    fun setHeaderActive(type: Int) {
        if (type == HEADER_LEFT)
            headerSelected = HEADER_LEFT
        else headerSelected = HEADER_RIGHT
    }

    fun headerLeftActive(submenu: DigitalProductSubMenu) {
        listener.onClickSubMenu(submenu)
        titleHeaderLeft.setTextColor(resources.getColorFromResources(context, R.color.font_white_primary_70))
        titleHeaderRight.setTextColor(resources.getColorFromResources(context, R.color.digital_title_header_non_active))
        layoutHeaderLeft.setBackgroundResource(R.drawable.bg_round_corner_solid_green)
        layoutHeaderRight.setBackgroundResource(R.color.digital_grey_bg_header)
    }

    fun headerRightActive(submenu: DigitalProductSubMenu) {
        listener.onClickSubMenu(submenu)
        titleHeaderLeft.setTextColor(resources.getColorFromResources(context, R.color.digital_title_header_non_active))
        titleHeaderRight.setTextColor(resources.getColorFromResources(context, R.color.font_white_primary_70))
        layoutHeaderLeft.setBackgroundResource(R.color.digital_grey_bg_header)
        layoutHeaderRight.setBackgroundResource(R.drawable.bg_round_corner_solid_green)
    }

    interface ActionListener {
        fun onClickSubMenu(subMenu: DigitalProductSubMenu)
    }

    companion object {
        val HEADER_LEFT = 0
        val HEADER_RIGHT = 1
    }
}