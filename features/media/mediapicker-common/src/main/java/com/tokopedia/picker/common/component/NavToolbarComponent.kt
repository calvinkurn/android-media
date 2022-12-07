package com.tokopedia.picker.common.component

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.picker.common.R
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.picker.common.mapper.Unify_G500
import com.tokopedia.picker.common.mapper.Unify_N700_96
import com.tokopedia.picker.common.mapper.Unify_Static_White
import com.tokopedia.unifyprinciples.Typography

sealed class ToolbarTheme {
    object Solid: ToolbarTheme()
    object Transparent: ToolbarTheme()
}

class NavToolbarComponent(
    listener: Listener,
    parent: ViewGroup,
    private var useArrowIcon: Boolean = true,
) : UiComponent(parent, R.id.uc_navigation_toolbar) {

    private val btnAction = findViewById<IconUnify>(R.id.btn_action)
    private val txtTitle = findViewById<Typography>(R.id.txt_title)
    private val btnDone = findViewById<Typography>(R.id.action_text_done)

    init {
        btnAction.setOnClickListener {
            listener.onCloseClicked()
        }

        btnDone.setOnClickListener {
            listener.onContinueClicked()
        }
    }

    fun setContinueTitle(title: String) {
        btnDone.text = title
    }

    fun setTitle(title: String) {
        txtTitle.text = title
    }

    fun onToolbarThemeChanged(theme: ToolbarTheme) {
        val isTransparent = theme == ToolbarTheme.Transparent

        setCloseActionColor(isTransparent)
        setTitleColor(isTransparent)
        setActionColor(isTransparent)
    }

    fun showContinueButtonAs(visibility: Boolean) {
        btnDone.showWithCondition(visibility)
    }

    private fun setActionColor(isTransparent: Boolean) {
        toolbarThemeCondition(isTransparent,
        transparentMode = {
            btnDone.setTextColor(colorForTransparentMode())
        },
        solidMode = {
            btnDone.setTextColor(colorActionSolidMode())
        })

    }

    private fun setTitleColor(isTransparent: Boolean) {
        toolbarThemeCondition(
            isTransparent,
            transparentMode = {
                txtTitle.setTextColor(colorForTransparentMode())
            },
            solidMode = {
                txtTitle.setTextColor(colorForSolidMode())
            }
        )
    }

    private fun setCloseActionColor(isTransparent: Boolean) {
        val icon = if (useArrowIcon) {
            IconUnify.ARROW_BACK
        } else {
            IconUnify.CLOSE
        }

        toolbarThemeCondition(
            isTransparent,
            transparentMode = {
                btnAction.setImage(
                    icon,
                    newLightEnable = colorForTransparentMode()
                )
            },
            solidMode = {
                btnAction.setImage(
                    icon,
                    newLightEnable = colorForSolidMode()
                )
            }
        )
    }

    private fun toolbarThemeCondition(
        isTransparent: Boolean,
        transparentMode: () -> Unit = {},
        solidMode: () -> Unit = {}
    ) {
        if (isTransparent) {
            transparentMode.invoke()
        } else {
            solidMode.invoke()
        }
    }

    private fun colorForSolidMode()
        = ContextCompat.getColor(context,
        Unify_N700_96
    )

    private fun colorForTransparentMode()
        = ContextCompat.getColor(context,
        Unify_Static_White
    )

    private fun colorActionSolidMode()
        = ContextCompat.getColor(context,
        Unify_G500
    )

    override fun release() {}

    interface Listener {
        fun onCloseClicked()
        fun onContinueClicked()
    }

}
