package com.tokopedia.digital.topupbillsproduct.compoundview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.design.base.BaseCustomView
import org.jetbrains.annotations.NotNull
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.model.DigitalProductSubMenu

/**
 * Created by nabillasabbaha on 24/04/19.
 */
class DigitalProductHeaderView @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                         defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val titleHeaderLeft : TextView
    private val titleHeaderRight : TextView
    private val layoutHeaderLeft : RelativeLayout
    private val layoutHeaderRight : RelativeLayout
    private lateinit var listener: ActionListener

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
        titleHeaderLeft.setText(subMenus.get(HEADER_LEFT).name)
        titleHeaderRight.setText(subMenus.get(HEADER_RIGHT).name)

        headerLeftActive(subMenus.get(HEADER_LEFT))
        layoutHeaderLeft.setOnClickListener {
            headerLeftActive(subMenus.get(HEADER_LEFT))
        }

        layoutHeaderRight.setOnClickListener {
            headerRightActive(subMenus.get(HEADER_RIGHT))
        }
    }

    fun headerLeftActive(submenu: DigitalProductSubMenu) {
        listener.onClickSubMenu(submenu)
        titleHeaderLeft.setTextColor(resources.getColor(R.color.font_white_primary_70))
        titleHeaderRight.setTextColor(resources.getColor( R.color.digital_title_header_non_active))
        layoutHeaderLeft.setBackgroundResource(R.drawable.bg_round_corner_solid_green)
        layoutHeaderRight.setBackgroundResource(R.color.digital_grey_bg_header)
    }

    fun headerRightActive(submenu: DigitalProductSubMenu) {
        listener.onClickSubMenu(submenu)
        titleHeaderLeft.setTextColor(resources.getColor( R.color.digital_title_header_non_active))
        titleHeaderRight.setTextColor(resources.getColor( R.color.font_white_primary_70))
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