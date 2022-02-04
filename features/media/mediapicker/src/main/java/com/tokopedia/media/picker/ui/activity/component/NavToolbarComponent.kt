package com.tokopedia.media.picker.ui.activity.component

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.media.common.component.UiComponent
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.R
import com.tokopedia.media.picker.utils.Unify_N700_96
import com.tokopedia.media.picker.utils.Unify_Static_White
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class NavToolbarComponent(
    listener: Listener,
    parent: ViewGroup,
    private var useArrowIcon: Boolean = true,
) : UiComponent(parent, R.id.uc_navigation_toolbar) {

    private val btnAction = findViewById<IconUnify>(R.id.btn_action)
    private val txtTitle = findViewById<Typography>(R.id.txt_title)
    private val btnDone = findViewById<UnifyButton>(R.id.btn_done)

    init {
        btnAction.setOnClickListener {
            listener.onCloseClicked()
        }

        btnDone.setOnClickListener {
            listener.onContinueClicked()
        }
    }

    fun setTitle(title: String) {
        txtTitle.text = title
    }

    fun setNavToolbarColorState(isTransparent: Boolean) {
        setTitleColor(isTransparent)
        setCloseActionColor(isTransparent)
    }

    fun showContinueButtonWithCondition(isShown: Boolean) {
        btnDone.showWithCondition(isShown)
    }

    fun showContinueButton() {
        btnDone.show()
    }

    fun hideContinueButton() {
        btnDone.hide()
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
        val icon = if (useArrowIcon) IconUnify.ARROW_BACK else IconUnify.CLOSE

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
        = ContextCompat.getColor(context, Unify_N700_96)

    private fun colorForTransparentMode()
        = ContextCompat.getColor(context, Unify_Static_White)

    override fun release() {}

    interface Listener {
        fun onCloseClicked()
        fun onContinueClicked()
    }

}